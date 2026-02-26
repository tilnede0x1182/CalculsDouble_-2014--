
// ==============================================================================
// Variables de tests
// ==============================================================================

	static int testsPassed = 0;
	static int testsFailed = 0;

// ==============================================================================
// Fonctions d'assertion
// ==============================================================================

	/**
	 * Verifie une condition et affiche le resultat.
	 * Affiche la valeur obtenue en cas d'echec.
	 * @param description Description du test.
	 * @param condition Condition a verifier.
	 * @param valeurObtenue Valeur obtenue pour diagnostic.
	 */
	static void assertion(String description, boolean condition, String valeurObtenue) {
		if (condition) {
			System.out.println("[PASS] " + description);
			testsPassed++;
		} else {
			System.out.println("[FAIL] " + description);
			System.out.println("       Valeur obtenue: " + valeurObtenue);
			testsFailed++;
		}
	}

	/**
	 * Verifie une condition sans valeur de diagnostic.
	 * @param description Description du test.
	 * @param condition Condition a verifier.
	 */
	static void assertion(String description, boolean condition) {
		assertion(description, condition, "N/A");
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
		testsComparaison(calc);
		testsCasLimitesNull(calc);
		testsEdgeCases(calc);
		afficherResume();
	}

// ------------------------------------------------------------------------------
// Tests Constructeur
// ------------------------------------------------------------------------------

	/**
	 * Tests du constructeur CalculsDouble.
	 */
	static void testsConstructeur() {
		System.out.println("--- Constructeur ---");
		testsConstructeurCasNominaux();
		testsConstructeurCasLimites();
	}

	/**
	 * Tests constructeur : cas nominaux.
	 */
	static void testsConstructeurCasNominaux() {
		CalculsDouble positif = new CalculsDouble(42);
		assertion("Constructeur positif (42)",
			positif.toDouble() == 42.0,
			String.valueOf(positif.toDouble()));

		CalculsDouble negatif = new CalculsDouble(-42);
		assertion("Constructeur negatif (-42)",
			negatif.toDouble() == -42.0,
			String.valueOf(negatif.toDouble()));

		CalculsDouble decimal = new CalculsDouble(3.14);
		assertion("Constructeur decimal (3.14)",
			Math.abs(decimal.toDouble() - 3.14) < 0.001,
			String.valueOf(decimal.toDouble()));
	}

	/**
	 * Tests constructeur : cas limites.
	 */
	static void testsConstructeurCasLimites() {
		CalculsDouble zero = new CalculsDouble(0);
		assertion("Constructeur zero (0)",
			zero.toDouble() == 0.0,
			String.valueOf(zero.toDouble()));

		CalculsDouble unChiffre = new CalculsDouble(5);
		assertion("Constructeur un chiffre (5)",
			unChiffre.toDouble() == 5.0,
			String.valueOf(unChiffre.toDouble()));

		CalculsDouble tresPetit = new CalculsDouble(0.0001);
		assertion("Constructeur tres petit (0.0001)",
			Math.abs(tresPetit.toDouble() - 0.0001) < 0.00001,
			String.valueOf(tresPetit.toDouble()));

		CalculsDouble negatifPetit = new CalculsDouble(-0.0001);
		assertion("Constructeur negatif tres petit (-0.0001)",
			Math.abs(negatifPetit.toDouble() + 0.0001) < 0.00001,
			String.valueOf(negatifPetit.toDouble()));
	}

// ------------------------------------------------------------------------------
// Tests Addition
// ------------------------------------------------------------------------------

	/**
	 * Tests de l'addition.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsAddition(CalculsDouble calc) {
		System.out.println("\n--- Addition ---");
		testsAdditionCasNominaux(calc);
		testsAdditionCasLimites(calc);
	}

	/**
	 * Tests addition : cas nominaux.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsAdditionCasNominaux(CalculsDouble calc) {
		double resultat1 = calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(3)).toDouble();
		assertion("Addition 5 + 3 = 8", resultat1 == 8.0, String.valueOf(resultat1));

		double resultat2 = calc.additionneNombres(new CalculsDouble(-5), new CalculsDouble(-3)).toDouble();
		assertion("Addition -5 + -3 = -8", resultat2 == -8.0, String.valueOf(resultat2));

		double resultat3 = calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(-3)).toDouble();
		assertion("Addition 5 + -3 = 2", resultat3 == 2.0, String.valueOf(resultat3));

		double resultat4 = calc.additionneNombres(new CalculsDouble(-5), new CalculsDouble(3)).toDouble();
		assertion("Addition -5 + 3 = -2", resultat4 == -2.0, String.valueOf(resultat4));
	}

	/**
	 * Tests addition : cas limites.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsAdditionCasLimites(CalculsDouble calc) {
		double resultat1 = calc.additionneNombres(new CalculsDouble(0), new CalculsDouble(0)).toDouble();
		assertion("Addition 0 + 0 = 0", resultat1 == 0.0, String.valueOf(resultat1));

		double resultat2 = calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(0)).toDouble();
		assertion("Addition 5 + 0 = 5", resultat2 == 5.0, String.valueOf(resultat2));

		double resultat3 = calc.additionneNombres(new CalculsDouble(0), new CalculsDouble(5)).toDouble();
		assertion("Addition 0 + 5 = 5", resultat3 == 5.0, String.valueOf(resultat3));

		double resultat4 = calc.additionneNombres(new CalculsDouble(5), new CalculsDouble(-5)).toDouble();
		assertion("Addition 5 + -5 = 0", resultat4 == 0.0, String.valueOf(resultat4));

		double resultat5 = calc.additionneNombres(new CalculsDouble(12.5), new CalculsDouble(3)).toDouble();
		assertion("Addition 12.5 + 3 = 15.5",
			Math.abs(resultat5 - 15.5) < 0.001, String.valueOf(resultat5));

		double resultat6 = calc.additionneNombres(new CalculsDouble(3), new CalculsDouble(12.5)).toDouble();
		assertion("Addition 3 + 12.5 = 15.5",
			Math.abs(resultat6 - 15.5) < 0.001, String.valueOf(resultat6));
	}

// ------------------------------------------------------------------------------
// Tests Soustraction
// ------------------------------------------------------------------------------

	/**
	 * Tests de la soustraction.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsSoustraction(CalculsDouble calc) {
		System.out.println("\n--- Soustraction ---");
		testsSoustractionCasNominaux(calc);
		testsSoustractionCasLimites(calc);
	}

	/**
	 * Tests soustraction : cas nominaux.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsSoustractionCasNominaux(CalculsDouble calc) {
		double resultat1 = calc.soustraitNombres(new CalculsDouble(10), new CalculsDouble(3)).toDouble();
		assertion("Soustraction 10 - 3 = 7", resultat1 == 7.0, String.valueOf(resultat1));

		double resultat2 = calc.soustraitNombres(new CalculsDouble(3), new CalculsDouble(10)).toDouble();
		assertion("Soustraction 3 - 10 = -7", resultat2 == -7.0, String.valueOf(resultat2));

		double resultat3 = calc.soustraitNombres(new CalculsDouble(-5), new CalculsDouble(-3)).toDouble();
		assertion("Soustraction -5 - -3 = -2", resultat3 == -2.0, String.valueOf(resultat3));
	}

	/**
	 * Tests soustraction : cas limites.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsSoustractionCasLimites(CalculsDouble calc) {
		double resultat1 = calc.soustraitNombres(new CalculsDouble(0), new CalculsDouble(0)).toDouble();
		assertion("Soustraction 0 - 0 = 0", resultat1 == 0.0, String.valueOf(resultat1));

		double resultat2 = calc.soustraitNombres(new CalculsDouble(5), new CalculsDouble(0)).toDouble();
		assertion("Soustraction 5 - 0 = 5", resultat2 == 5.0, String.valueOf(resultat2));

		double resultat3 = calc.soustraitNombres(new CalculsDouble(0), new CalculsDouble(5)).toDouble();
		assertion("Soustraction 0 - 5 = -5", resultat3 == -5.0, String.valueOf(resultat3));

		double resultat4 = calc.soustraitNombres(new CalculsDouble(5), new CalculsDouble(5)).toDouble();
		assertion("Soustraction 5 - 5 = 0", resultat4 == 0.0, String.valueOf(resultat4));
	}

// ------------------------------------------------------------------------------
// Tests Multiplication
// ------------------------------------------------------------------------------

	/**
	 * Tests de la multiplication.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsMultiplication(CalculsDouble calc) {
		System.out.println("\n--- Multiplication ---");
		testsMultiplicationCasNominaux(calc);
		testsMultiplicationCasLimites(calc);
	}

	/**
	 * Tests multiplication : cas nominaux.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsMultiplicationCasNominaux(CalculsDouble calc) {
		double resultat1 = calc.mult(new CalculsDouble(6), new CalculsDouble(7)).toDouble();
		assertion("Multiplication 6 * 7 = 42", resultat1 == 42.0, String.valueOf(resultat1));

		double resultat2 = calc.mult(new CalculsDouble(-3), new CalculsDouble(-4)).toDouble();
		assertion("Multiplication -3 * -4 = 12", resultat2 == 12.0, String.valueOf(resultat2));

		double resultat3 = calc.mult(new CalculsDouble(-3), new CalculsDouble(4)).toDouble();
		assertion("Multiplication -3 * 4 = -12", resultat3 == -12.0, String.valueOf(resultat3));

		double resultat4 = calc.mult(new CalculsDouble(3), new CalculsDouble(-4)).toDouble();
		assertion("Multiplication 3 * -4 = -12", resultat4 == -12.0, String.valueOf(resultat4));
	}

	/**
	 * Tests multiplication : cas limites.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsMultiplicationCasLimites(CalculsDouble calc) {
		double resultat1 = calc.mult(new CalculsDouble(100), new CalculsDouble(0)).toDouble();
		assertion("Multiplication 100 * 0 = 0", resultat1 == 0.0, String.valueOf(resultat1));

		double resultat2 = calc.mult(new CalculsDouble(0), new CalculsDouble(100)).toDouble();
		assertion("Multiplication 0 * 100 = 0", resultat2 == 0.0, String.valueOf(resultat2));

		double resultat3 = calc.mult(new CalculsDouble(0), new CalculsDouble(0)).toDouble();
		assertion("Multiplication 0 * 0 = 0", resultat3 == 0.0, String.valueOf(resultat3));

		double resultat4 = calc.mult(new CalculsDouble(5), new CalculsDouble(1)).toDouble();
		assertion("Multiplication 5 * 1 = 5", resultat4 == 5.0, String.valueOf(resultat4));

		double resultat5 = calc.mult(new CalculsDouble(1), new CalculsDouble(5)).toDouble();
		assertion("Multiplication 1 * 5 = 5", resultat5 == 5.0, String.valueOf(resultat5));
	}

// ------------------------------------------------------------------------------
// Tests Division
// ------------------------------------------------------------------------------

	/**
	 * Tests de la division.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsDivision(CalculsDouble calc) {
		System.out.println("\n--- Division ---");
		testsDivisionCasNominaux(calc);
		testsDivisionCasLimites(calc);
	}

	/**
	 * Tests division : cas nominaux.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsDivisionCasNominaux(CalculsDouble calc) {
		CalculsDouble[] res1 = calc.division_boucle(new CalculsDouble(20), new CalculsDouble(4));
		double resultat1 = (res1 != null) ? res1[0].toDouble() : Double.NaN;
		assertion("Division 20 / 4 = 5", resultat1 == 5.0, String.valueOf(resultat1));

		CalculsDouble[] res2 = calc.division_boucle(new CalculsDouble(10), new CalculsDouble(4));
		double resultat2 = (res2 != null) ? res2[0].toDouble() : Double.NaN;
		assertion("Division 10 / 4 = 2.5",
			Math.abs(resultat2 - 2.5) < 0.001, String.valueOf(resultat2));

		CalculsDouble[] res3 = calc.division_boucle(new CalculsDouble(-20), new CalculsDouble(4));
		double resultat3 = (res3 != null) ? res3[0].toDouble() : Double.NaN;
		assertion("Division -20 / 4 = -5", resultat3 == -5.0, String.valueOf(resultat3));
	}

	/**
	 * Tests division : cas limites.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsDivisionCasLimites(CalculsDouble calc) {
		CalculsDouble[] res1 = calc.division_boucle(new CalculsDouble(10), new CalculsDouble(0));
		assertion("Division 10 / 0 retourne null", res1 == null, String.valueOf(res1));

		CalculsDouble[] res2 = calc.division_boucle(new CalculsDouble(0), new CalculsDouble(0));
		assertion("Division 0 / 0 retourne null", res2 == null, String.valueOf(res2));

		CalculsDouble[] res3 = calc.division_boucle(new CalculsDouble(0), new CalculsDouble(5));
		double resultat3 = (res3 != null) ? res3[0].toDouble() : Double.NaN;
		assertion("Division 0 / 5 = 0", resultat3 == 0.0, String.valueOf(resultat3));

		CalculsDouble[] res4 = calc.division_boucle(new CalculsDouble(5), new CalculsDouble(1));
		double resultat4 = (res4 != null) ? res4[0].toDouble() : Double.NaN;
		assertion("Division 5 / 1 = 5", resultat4 == 5.0, String.valueOf(resultat4));
	}

// ------------------------------------------------------------------------------
// Tests Valeur Absolue
// ------------------------------------------------------------------------------

	/**
	 * Tests de la valeur absolue.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsValeurAbsolue(CalculsDouble calc) {
		System.out.println("\n--- Valeur Absolue ---");

		double resultat1 = calc.abs(new CalculsDouble(5)).toDouble();
		assertion("Valeur absolue de 5 = 5", resultat1 == 5.0, String.valueOf(resultat1));

		double resultat2 = calc.abs(new CalculsDouble(-5)).toDouble();
		assertion("Valeur absolue de -5 = 5", resultat2 == 5.0, String.valueOf(resultat2));

		double resultat3 = calc.abs(new CalculsDouble(0)).toDouble();
		assertion("Valeur absolue de 0 = 0", resultat3 == 0.0, String.valueOf(resultat3));

		double resultat4 = calc.abs(new CalculsDouble(-0.5)).toDouble();
		assertion("Valeur absolue de -0.5 = 0.5",
			Math.abs(resultat4 - 0.5) < 0.001, String.valueOf(resultat4));
	}

// ------------------------------------------------------------------------------
// Tests Comparaison
// ------------------------------------------------------------------------------

	/**
	 * Tests de la comparaison de nombres.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsComparaison(CalculsDouble calc) {
		System.out.println("\n--- Comparaison ---");
		testsComparaisonCasNominaux(calc);
		testsComparaisonCasLimites(calc);
	}

	/**
	 * Tests comparaison : cas nominaux.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsComparaisonCasNominaux(CalculsDouble calc) {
		int res1 = calc.compareNombres(new CalculsDouble(5), new CalculsDouble(3));
		assertion("Comparaison 5 > 3", res1 == 1, String.valueOf(res1));

		int res2 = calc.compareNombres(new CalculsDouble(3), new CalculsDouble(5));
		assertion("Comparaison 3 < 5", res2 == 2, String.valueOf(res2));

		int res3 = calc.compareNombres(new CalculsDouble(5), new CalculsDouble(5));
		assertion("Comparaison 5 == 5", res3 == 0, String.valueOf(res3));
	}

	/**
	 * Tests comparaison : cas limites.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsComparaisonCasLimites(CalculsDouble calc) {
		int res1 = calc.compareNombres(new CalculsDouble(0), new CalculsDouble(0));
		assertion("Comparaison 0 == 0", res1 == 0, String.valueOf(res1));

		int res2 = calc.compareNombres(new CalculsDouble(0), new CalculsDouble(5));
		assertion("Comparaison 0 < 5", res2 == 2, String.valueOf(res2));

		int res3 = calc.compareNombres(new CalculsDouble(5), new CalculsDouble(0));
		assertion("Comparaison 5 > 0", res3 == 1, String.valueOf(res3));

		int res4 = calc.compareNombres(new CalculsDouble(-5), new CalculsDouble(5));
		assertion("Comparaison -5 < 5", res4 == 2, String.valueOf(res4));

		int res5 = calc.compareNombres(new CalculsDouble(5), new CalculsDouble(-5));
		assertion("Comparaison 5 > -5", res5 == 1, String.valueOf(res5));

		int res6 = calc.compareNombres(new CalculsDouble(-5), new CalculsDouble(-3));
		assertion("Comparaison -5 < -3", res6 == 2, String.valueOf(res6));
	}

// ------------------------------------------------------------------------------
// Tests Cas Limites NULL
// ------------------------------------------------------------------------------

	/**
	 * Tests des cas limites avec valeurs NULL.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsCasLimitesNull(CalculsDouble calc) {
		System.out.println("\n--- Cas Limites NULL ---");

		try {
			calc.additionneNombres(null, new CalculsDouble(5));
			assertion("Addition null + 5 leve exception", false, "Pas d'exception");
		} catch (Exception exception) {
			assertion("Addition null + 5 leve exception", true, exception.getClass().getName());
		}

		try {
			calc.additionneNombres(new CalculsDouble(5), null);
			assertion("Addition 5 + null leve exception", false, "Pas d'exception");
		} catch (Exception exception) {
			assertion("Addition 5 + null leve exception", true, exception.getClass().getName());
		}
	}

// ------------------------------------------------------------------------------
// Tests Edge Cases (grands nombres)
// ------------------------------------------------------------------------------

	/**
	 * Tests des cas limites avec grands nombres.
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsEdgeCases(CalculsDouble calc) {
		System.out.println("\n--- Edge Cases (Grands Nombres) ---");
		testsGrandsNombres(calc);
	}

	/**
	 * Tests avec grands nombres (> 10 chiffres).
	 * @param calc Instance de CalculsDouble.
	 */
	static void testsGrandsNombres(CalculsDouble calc) {
		CalculsDouble grand = new CalculsDouble(12345678901.0);
		assertion("Grand nombre 12345678901",
			grand.partieEntiere.equals("12345678901"), grand.partieEntiere);

		CalculsDouble resultatAdd = calc.additionneNombres(
			new CalculsDouble(9999999999.0), new CalculsDouble(1));
		assertion("Addition 9999999999 + 1 = 10000000000",
			resultatAdd.partieEntiere.equals("10000000000"), resultatAdd.partieEntiere);

		double resultatMult = calc.mult(
			new CalculsDouble(1000000), new CalculsDouble(1000000)).toDouble();
		assertion("Multiplication 1000000 * 1000000",
			resultatMult == 1000000000000.0, String.valueOf(resultatMult));
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
		if (testsFailed == 0) {
			System.out.println("SUCCES: Tous les tests passent!");
		} else {
			System.out.println("ECHEC: " + testsFailed + " test(s) en echec.");
		}
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

