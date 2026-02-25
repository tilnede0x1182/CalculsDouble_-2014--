	/**
	 * Additionne deux CalculsDouble.
	 * Gere les signes et les retenues.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return Resultat de l'addition.
	 */
	public CalculsDouble additionneNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(compteOcurrences(nombreA.partieDecimale, '0'), compteOcurrences(nombreB.partieDecimale, '0'));
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		CalculsDouble resultat = new CalculsDouble();
		CalculsDouble additionSignee = traiterSignesAddition(nombreA, nombreB, resultat);
		if (additionSignee != null) {
			return additionSignee;
		}
		return calculerSomme(nombreA, nombreB, resultat, chiffresAvantVirgule);
	}

// ------------------------------------------------------------------------------
// Gestion des signes pour l'addition
// ------------------------------------------------------------------------------

	/**
	 * Traite les signes pour l'addition.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat si cas special, null sinon.
	 */
	private CalculsDouble traiterSignesAddition(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (nombreA.estNegatif && nombreB.estNegatif) {
			resultat.estNegatif = true;
			return null;
		}
		if (nombreA.estNegatif || nombreB.estNegatif) {
			return traiterAdditionMixte(nombreA, nombreB, resultat);
		}
		return null;
	}

	/**
	 * Traite l'addition avec un seul nombre negatif.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat de l'addition mixte.
	 */
	private CalculsDouble traiterAdditionMixte(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		int comparaison = compareNombres_abs(nombreA, nombreB);
		if (comparaison == 0) {
			resultat.partieEntiere = "0";
			resultat.partieDecimale = "";
			resultat.estNegatif = false;
			return resultat;
		}
		if (comparaison == 2) {
			CalculsDouble temp = nombreA;
			nombreA = nombreB;
			nombreB = temp;
		}
		return effectuerAdditionMixte(nombreA, nombreB, resultat);
	}

	/**
	 * Effectue l'addition mixte apres reordonnancement.
	 * @param nombreA Premier nombre (le plus grand en valeur absolue).
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat de l'addition.
	 */
	private CalculsDouble effectuerAdditionMixte(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (!nombreA.estNegatif && nombreB.estNegatif) {
			nombreA.estNegatif = false;
			nombreB.estNegatif = false;
			return soustraitNombres(nombreA, nombreB);
		}
		if (nombreA.estNegatif && !nombreB.estNegatif) {
			nombreA.estNegatif = false;
			nombreB.estNegatif = false;
			resultat = soustraitNombres(nombreA, nombreB);
			resultat.estNegatif = true;
			return resultat;
		}
		return null;
	}

// ------------------------------------------------------------------------------
// Calcul de la somme
// ------------------------------------------------------------------------------

	/**
	 * Calcule la somme des parties entieres et decimales.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat de la somme.
	 */
	private CalculsDouble calculerSomme(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		resultat.partieEntiere = "" + (Integer.parseInt(nombreA.partieEntiere) + Integer.parseInt(nombreB.partieEntiere));
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			resultat.partieDecimale = "" + (Integer.parseInt(nombreA.partieDecimale) + Integer.parseInt(nombreB.partieDecimale));
		} else {
			resultat.partieDecimale = "";
		}
		resultat = gererRetenueDecimale(nombreA, nombreB, resultat);
		resultat = nettoyerSignes(resultat);
		resultat = ajouterZerosDecimaux(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Gere la retenue de la partie decimale vers l'entiere.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @return Resultat avec retenue geree.
	 */
	private CalculsDouble gererRetenueDecimale(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat) {
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			int sommePremiersChiffres = Integer.parseInt("" + nombreA.partieDecimale.charAt(0)) + Integer.parseInt("" + nombreB.partieDecimale.charAt(0));
			if (sommePremiersChiffres > 9) {
				resultat.partieEntiere = "" + (Integer.parseInt(resultat.partieEntiere) + 1);
				resultat.partieDecimale = resultat.partieDecimale.substring(1, resultat.partieDecimale.length());
			}
		}
		return resultat;
	}

	/**
	 * Nettoie les signes negatifs parasites.
	 * @param resultat Resultat a nettoyer.
	 * @return Resultat nettoye.
	 */
	private CalculsDouble nettoyerSignes(CalculsDouble resultat) {
		if (resultat.partieDecimale.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieDecimale = resultat.partieDecimale.replace("-", "");
		}
		resultat.partieEntiere = resultat.partieEntiere.replace("-", "");
		return resultat;
	}

	/**
	 * Ajoute les zeros necessaires a la partie decimale.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat avec zeros ajoutes.
	 */
	private CalculsDouble ajouterZerosDecimaux(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		if (!resultat.partieDecimale.isEmpty()) {
			int recul = 0;
			if (nombreA.partieDecimale.charAt(0) == '0' || nombreB.partieDecimale.charAt(0) == '0') {
				recul = 1;
			}
			for (int index = 0; index < chiffresAvantVirgule + recul - 2; index++) {
				resultat.partieDecimale = "0" + resultat.partieDecimale;
			}
		}
		return resultat;
	}
