
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

		// Tests constructeur
		assertion("CalculsDouble positif", new CalculsDouble(42).toDouble() == 42.0);
		assertion("CalculsDouble negatif", new CalculsDouble(-42).toDouble() == -42.0);
		assertion("CalculsDouble decimal", Math.abs(new CalculsDouble(3.14).toDouble() - 3.14) < 0.001);
		assertion("CalculsDouble zero", new CalculsDouble(0).toDouble() == 0.0);

		// Tests addition
		assertion("Addition simples",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(3)).toDouble() == 8.0);
		assertion("Addition negatifs",
			calc.additionneNombres(new CalculsDouble(-5), new CalculsDouble(-3)).toDouble() == -8.0);
		assertion("Addition mixte",
			calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(-3)).toDouble() == 2.0);

		// Tests soustraction
		assertion("Soustraction simple",
			calc.soustraitNombres(new CalculsDouble(10), new CalculsDouble(3)).toDouble() == 7.0);
		assertion("Soustraction resultat negatif",
			calc.soustraitNombres(new CalculsDouble(3), new CalculsDouble(10)).toDouble() == -7.0);

		// Tests multiplication
		assertion("Multiplication simple",
			calc.mult(new CalculsDouble(6), new CalculsDouble(7)).toDouble() == 42.0);
		assertion("Multiplication par zero",
			calc.mult(new CalculsDouble(100), new CalculsDouble(0)).toDouble() == 0.0);
		assertion("Multiplication negatifs",
			calc.mult(new CalculsDouble(-3), new CalculsDouble(-4)).toDouble() == 12.0);

		// Tests division
		assertion("Division simple",
			calc.division_boucle(new CalculsDouble(20), new CalculsDouble(4))[0].toDouble() == 5.0);
		assertion("Division decimale",
			Math.abs(calc.division_boucle(new CalculsDouble(10), new CalculsDouble(4))[0].toDouble() - 2.5) < 0.001);

		// Tests valeur absolue
		assertion("Valeur absolue positif",
			calc.abs(new CalculsDouble(5)).toDouble() == 5.0);
		assertion("Valeur absolue negatif",
			calc.abs(new CalculsDouble(-5)).toDouble() == 5.0);

		// Test division par zero - retourne null (pas d'exception)
		CalculsDouble[] resultatDivZero = calc.division_boucle(new CalculsDouble(10), new CalculsDouble(0));
		assertion("Division par zero retourne null", resultatDivZero == null);

		// ==============================================================================
		// Edge Cases (Bugs connus)
		// ==============================================================================

		System.out.println("\n=== Edge Cases (Bugs a corriger) ===\n");

		// Bug 1 : Addition decimal + entier perd la decimale
		assertion("Bug1: Addition decimal + entier (12.5 + 3 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(3)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition entier + decimal (3 + 12.5 = 15.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(3), new CalculsDouble(12.5)).toDouble() - 15.5) < 0.001);
		assertion("Bug1: Addition decimal + entier negatif (12.5 + (-3) = 9.5)",
			Math.abs(calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(-3)).toDouble() - 9.5) < 0.001);

		// Bug 3 : Grands nombres (> 10 chiffres) - Integer.parseInt plante
		// Ces tests vont echouer tant que le bug n'est pas corrige
		boolean grandNombreOk = false;
		try {
			CalculsDouble grand = new CalculsDouble(12345678901.0);
			grandNombreOk = grand.partieEntiere.equals("12345678901");
		} catch (Exception exception) {
			grandNombreOk = false;
		}
		assertion("Bug3: Grand nombre entier (12345678901)", grandNombreOk);

		boolean additionGrandsNombresOk = false;
		try {
			double resultat = calc.additionneNombres(new CalculsDouble(9999999999.0), new CalculsDouble(1)).toDouble();
			additionGrandsNombresOk = (resultat == 10000000000.0);
		} catch (Exception exception) {
			additionGrandsNombresOk = false;
		}
		assertion("Bug3: Addition grands nombres", additionGrandsNombresOk);

		boolean multiplicationGrandsNombresOk = false;
		try {
			double resultat = calc.mult(new CalculsDouble(1000000), new CalculsDouble(1000000)).toDouble();
			multiplicationGrandsNombresOk = (resultat == 1000000000000.0);
		} catch (Exception exception) {
			multiplicationGrandsNombresOk = false;
		}
		assertion("Bug3: Multiplication grands nombres", multiplicationGrandsNombresOk);

		// ==============================================================================
		// Resume
		// ==============================================================================

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
