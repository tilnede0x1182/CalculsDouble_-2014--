	/**
	 * Soustrait deux CalculsDouble (A - B).
	 * Utilise l'addition avec l'oppose de B.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre a soustraire.
	 * @return Resultat de la soustraction.
	 */
	public CalculsDouble soustraitNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(
			compteOcurrences(nombreA.partieDecimale, '0'),
			compteOcurrences(nombreB.partieDecimale, '0')
		);
		CalculsDouble resultat = new CalculsDouble();
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		integrerSignesPourSoustraction(nombreA, nombreB);
		resultat = calculerDifference(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Integre les signes dans les parties entieres pour la soustraction.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 */
	private void integrerSignesPourSoustraction(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.estNegatif || nombreB.estNegatif) {
			if (nombreA.estNegatif) {
				nombreA.partieEntiere = "-" + nombreA.partieEntiere;
			}
			if (nombreB.estNegatif) {
				nombreB.partieEntiere = "-" + nombreB.partieEntiere;
			}
		}
	}

	/**
	 * Calcule la difference des parties entieres et decimales.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param resultat Resultat en cours.
	 * @param chiffresAvantVirgule Nombre de zeros avant la virgule.
	 * @return Resultat de la difference.
	 */
	private CalculsDouble calculerDifference(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		resultat.partieEntiere = soustraitChaines(nombreA.partieEntiere, nombreB.partieEntiere);
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			resultat.partieDecimale = soustraitChaines(nombreA.partieDecimale, nombreB.partieDecimale);
		} else {
			resultat.partieDecimale = "";
			resultat = nettoyerSignesSoustraction(resultat);
			return resultat;
		}
		resultat = nettoyerSignesSoustraction(resultat);
		resultat = ajouterZerosSoustraction(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Nettoie les signes negatifs parasites apres soustraction.
	 * @param resultat Resultat a nettoyer.
	 * @return Resultat nettoye.
	 */
	private CalculsDouble nettoyerSignesSoustraction(CalculsDouble resultat) {
		if (resultat.partieDecimale.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieDecimale = resultat.partieDecimale.replace("-", "");
		}
		if (resultat.partieEntiere.contains("-")) {
			resultat.estNegatif = true;
			resultat.partieEntiere = resultat.partieEntiere.replace("-", "");
		}
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
	private CalculsDouble ajouterZerosSoustraction(CalculsDouble nombreA, CalculsDouble nombreB, CalculsDouble resultat, int chiffresAvantVirgule) {
		int recul = 0;
		if (nombreA.partieDecimale.charAt(0) == '0' || nombreB.partieDecimale.charAt(0) == '0') {
			recul = 1;
		}
		for (int index = 0; index < chiffresAvantVirgule + recul - 2; index++) {
			resultat.partieDecimale = "0" + resultat.partieDecimale;
		}
		return resultat;
	}
