
// ==============================================================================
// Variables de tests
// ==============================================================================

	static int testsPassed = 0;
	static int testsFailed = 0;

// ==============================================================================
// Fonction d'assertion
// ==============================================================================

	/**
	 * Verifie une condition et affiche le resultat.
	 * @param description Description du test.
	 * @param condition Condition a verifier.
	 */
	static void assertion(String description, boolean condition) {
		if (condition) {
			System.out.println("[PASS] " + description);
			testsPassed++;
		} else {
			System.out.println("[FAIL] " + description);
			testsFailed++;
		}
	}

// ==============================================================================
// Tests
// ==============================================================================

	/**
	 * Execute tous les tests.
	 */
	static void runTests() {
		CalculsDouble calc = new CalculsDouble();
		System.out.println("=== Tests CalculsDouble ===\n");
		testsConstructeur();
		testsAddition(calc);
		testsSoustraction(calc);
		testsMultiplication(calc);
		testsDivision(calc);
		testsValeurAbsolue(calc);
		testsEdgeCases(calc);
		afficherResume();
	}

// ------------------------------------------------------------------------------
// Tests unitaires par categorie
// ------------------------------------------------------------------------------

	/**
	 * Tests du constructeur CalculsDouble.
	 */
	static void testsConstructeur() {
		assertion("CalculsDouble positif", new CalculsDouble(42).toDouble() == 42.0);
		assertion("CalculsDouble negatif", new CalculsDouble(-42).toDouble() == -42.0);
		assertion("CalculsDouble decimal", Math.abs(new CalculsDouble(3.14).toDouble() - 3.14) < 0.001);
		assertion("CalculsDouble zero", new CalculsDouble(0).toDouble() == 0.0);
	}

	/**
	 * Tests de l'addition.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsAddition(CalculsDouble calc) {
		assertion("Addition simples",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(3)).toDouble() == 8.0);
		assertion("Addition negatifs",
			calc.additionneNombres(new CalculsDouble(-5), new CalculsDouble(-3)).toDouble() == -8.0);
		assertion("Addition mixte",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(-3)).toDouble() == 2.0);
	}

	/**
	 * Tests de la soustraction.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsSoustraction(CalculsDouble calc) {
		assertion("Soustraction simple",
			calc.soustraitNombres(new CalculsDouble(10), new CalculsDouble(3)).toDouble() == 7.0);
		assertion("Soustraction resultat negatif",
			calc.soustraitNombres(new CalculsDouble(3), new CalculsDouble(10)).toDouble() == -7.0);
	}

	/**
	 * Tests de la multiplication.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsMultiplication(CalculsDouble calc) {
		assertion("Multiplication simple",
			calc.mult(new CalculsDouble(6), new CalculsDouble(7)).toDouble() == 42.0);
		assertion("Multiplication par zero",
			calc.mult(new CalculsDouble(100), new CalculsDouble(0)).toDouble() == 0.0);
		assertion("Multiplication negatifs",
			calc.mult(new CalculsDouble(-3), new CalculsDouble(-4)).toDouble() == 12.0);
	}

	/**
	 * Tests de la division.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsDivision(CalculsDouble calc) {
		assertion("Division simple",
			calc.division_boucle(new CalculsDouble(20), new CalculsDouble(4))[0].toDouble() == 5.0);
		assertion("Division decimale",
			Math.abs(calc.division_boucle(new CalculsDouble(10), new CalculsDouble(4))[0].toDouble() - 2.5) < 0.001);
		CalculsDouble[] resultatDivZero = calc.division_boucle(new CalculsDouble(10), new CalculsDouble(0));
		assertion("Division par zero retourne null", resultatDivZero == null);
	}

	/**
	 * Tests de la valeur absolue.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsValeurAbsolue(CalculsDouble calc) {
		assertion("Valeur absolue positif",
			calc.abs(new CalculsDouble(5)).toDouble() == 5.0);
		assertion("Valeur absolue negatif",
			calc.abs(new CalculsDouble(-5)).toDouble() == 5.0);
	}

// ------------------------------------------------------------------------------
// Tests Edge Cases (bugs connus)
// ------------------------------------------------------------------------------

	/**
	 * Tests des cas limites (bugs connus).
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsEdgeCases(CalculsDouble calc) {
		System.out.println("\n=== Edge Cases (Bugs a corriger) ===\n");
		testsBug1Addition(calc);
		testsBug3GrandsNombres(calc);
	}

	/**
	 * Tests Bug1 : Addition decimal + entier.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsBug1Addition(CalculsDouble calc) {
		assertion("Bug1: Addition decimal + entier (12.5 + 3 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(3)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition entier + decimal (3 + 12.5 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(3), new CalculsDouble(12.5)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition decimal + entier negatif (12.5 + (-3) = 9.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(-3)).toDouble() - 9.5) < 0.001);
	}

	/**
	 * Tests Bug3 : Grands nombres (> 10 chiffres).
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsBug3GrandsNombres(CalculsDouble calc) {
		assertion("Bug3: Grand nombre entier (12345678901)", testerGrandNombre());
		assertion("Bug3: Addition grands nombres", testerAdditionGrandsNombres(calc));
		assertion("Bug3: Multiplication grands nombres", testerMultiplicationGrandsNombres(calc));
	}

// ------------------------------------------------------------------------------
// Fonctions auxiliaires pour tests Bug3
// ------------------------------------------------------------------------------

	/**
	 * Teste la creation d'un grand nombre.
	 * @return True si le test passe.
	 */
	static boolean testerGrandNombre() {
		try {
			CalculsDouble grand = new CalculsDouble(12345678901.0);
			return grand.partieEntiere.equals("12345678901");
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * Teste l'addition de grands nombres.
	 * @param calc Instance de CalculsDouble.
	 * @return True si le test passe.
	 */
	static boolean testerAdditionGrandsNombres(CalculsDouble calc) {
		try {
			CalculsDouble resultatAdd = calc.additionneNombres(new CalculsDouble(9999999999.0), new CalculsDouble(1));
			return resultatAdd.partieEntiere.equals("10000000000");
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * Teste la multiplication de grands nombres.
	 * @param calc Instance de CalculsDouble.
	 * @return True si le test passe.
	 */
	static boolean testerMultiplicationGrandsNombres(CalculsDouble calc) {
		try {
			double resultat = calc.mult(new CalculsDouble(1000000), new CalculsDouble(1000000)).toDouble();
			return (resultat == 1000000000000.0);
		} catch (Exception exception) {
			return false;
		}
	}

// ------------------------------------------------------------------------------
// Resume
// ------------------------------------------------------------------------------

	/**
	 * Affiche le resume des tests.
	 */
	static void afficherResume() {
		System.out.println("\n=== Resume ===");
		System.out.println("Tests passes: " + testsPassed);
		System.out.println("Tests echoues: " + testsFailed);
		System.out.println("Total: " + (testsPassed + testsFailed));
	}

// ==============================================================================
// Main
// ==============================================================================

	/**
	 * Point d'entree du programme.
	 * @param args Arguments de la ligne de commande.
	 */
	public static void main(String[] args) {
		runTests();
	}
