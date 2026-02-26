	/**
	 * Soustrait deux CalculsDouble (A - B).
	 * Gere les signes et delègue à soustraitNombresPositifs.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre a soustraire.
	 * @return Resultat de la soustraction.
	 */
	public CalculsDouble soustraitNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		boolean signeA = nombreA.estNegatif;
		boolean signeB = nombreB.estNegatif;
		CalculsDouble absA = copie(nombreA);
		CalculsDouble absB = copie(nombreB);
		absA.estNegatif = false;
		absB.estNegatif = false;
		if (!signeA && !signeB) {
			return soustraitNombresPositifs(absA, absB);
		}
		if (signeA && signeB) {
			return soustraitNombresPositifs(absB, absA);
		}
		if (!signeA && signeB) {
			CalculsDouble resultat = additionneNombresPositifs(absA, absB);
			return resultat;
		}
		if (signeA && !signeB) {
			CalculsDouble resultat = additionneNombresPositifs(absA, absB);
			resultat.estNegatif = true;
			return resultat;
		}
		return new CalculsDouble();
	}

	/**
	 * Soustrait deux CalculsDouble positifs (A - B).
	 * @param nombreA Premier nombre positif.
	 * @param nombreB Second nombre positif.
	 * @return Resultat de la soustraction.
	 */
	private CalculsDouble soustraitNombresPositifs(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(
			compteOcurrences(nombreA.partieDecimale, '0'),
			compteOcurrences(nombreB.partieDecimale, '0')
		);
		CalculsDouble resultat = new CalculsDouble();
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		resultat = calculerDifference(nombreA, nombreB, resultat, chiffresAvantVirgule);
		return resultat;
	}

	/**
	 * Additionne deux CalculsDouble positifs (sans gestion de signe).
	 * @param nombreA Premier nombre positif.
	 * @param nombreB Second nombre positif.
	 * @return Resultat de l'addition.
	 */
	private CalculsDouble additionneNombresPositifs(CalculsDouble nombreA, CalculsDouble nombreB) {
		int chiffresAvantVirgule = Math.min(
			compteOcurrences(nombreA.partieDecimale, '0'),
			compteOcurrences(nombreB.partieDecimale, '0')
		);
		CalculsDouble[] nombresEgalises = egaliseNombres(nombreA, nombreB);
		nombreA = nombresEgalises[0];
		nombreB = nombresEgalises[1];
		CalculsDouble resultat = new CalculsDouble();
		resultat.partieEntiere = additionneChaines(nombreA.partieEntiere, nombreB.partieEntiere);
		if (!nombreA.partieDecimale.isEmpty() && !nombreB.partieDecimale.isEmpty()) {
			resultat.partieDecimale = additionneChaines(nombreA.partieDecimale, nombreB.partieDecimale);
			int sommePremiersChiffres = chiffreVersInt(nombreA.partieDecimale.charAt(0)) + chiffreVersInt(nombreB.partieDecimale.charAt(0));
			if (sommePremiersChiffres > 9) {
				resultat.partieEntiere = incrementeChaine(resultat.partieEntiere);
				resultat.partieDecimale = resultat.partieDecimale.substring(1);
			}
		} else {
			resultat.partieDecimale = "";
		}
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
