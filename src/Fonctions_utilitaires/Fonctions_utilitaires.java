
	/**
	 * Verifie si une chaine represente un entier valide.
	 * Utilise Integer.parseInt pour la verification.
	 * @param chaine Chaine a tester.
	 * @return True si la chaine est un entier valide.
	 */
	public boolean isInteger(String chaine) {
		try {
			int nombre = Integer.parseInt(chaine);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}

	/**
	 * Convertit une chaine en entier avec gestion des erreurs.
	 * @param chaine Chaine a convertir.
	 * @return Valeur entiere ou -1 en cas d'erreur.
	 */
	public int convertStrInt(String chaine) {
		if (chaine == null) {
			aff("n0 est null");
			return -1;
		}
		if (chaine.isEmpty()) {
			aff("n0 est egal a " + '"' + '"' + ".");
			return -1;
		}
		try {
			return Integer.parseInt(chaine);
		} catch (NumberFormatException exception) {
			aff("Impossible de convertir ");
			return -1;
		}
	}

	/**
	 * Verifie si un caractere est un chiffre (0-9).
	 * @param caractere Caractere a tester.
	 * @return True si le caractere est un chiffre.
	 */
	public boolean isCharNumber(char caractere) {
		return (caractere >= '0' && caractere <= '9');
	}

	/**
	 * Verifie si une chaine contient uniquement des chiffres.
	 * @param chaine Chaine a tester.
	 * @return True si tous les caracteres sont des chiffres.
	 */
	public boolean isInteger_deep(String chaine) {
		if (chaine.isEmpty()) {
			return false;
		}
		int longueurChaine = chaine.length();
		for (int index = 0; index < longueurChaine; index++) {
			if (!isCharNumber(chaine.charAt(index))) {
				return false;
			}
		}
		return true;
	}

// ------------------------------------------------------------------------------
// Verification de nombres CalculsDouble
// ------------------------------------------------------------------------------

	/**
	 * Verifie si un CalculsDouble est un nombre valide.
	 * @param nombre CalculsDouble a tester.
	 * @return True si partieEntiere et partieDecimale sont des nombres valides.
	 */
	public boolean isInteger(CalculsDouble nombre) {
		boolean resultat = false;
		String partieEntiere = nombre.partieEntiere;
		String partieDecimale = nombre.partieDecimale;
		if (!partieEntiere.isEmpty()) {
			resultat = isInteger(partieEntiere);
		}
		if (!partieDecimale.isEmpty()) {
			resultat = isInteger(partieDecimale);
		}
		return resultat;
	}

	/**
	 * Verifie si un CalculsDouble est egal a zero.
	 * @param nombre CalculsDouble a tester.
	 * @return True si le nombre vaut 0 ou 0.0.
	 */
	public boolean estNul(CalculsDouble nombre) {
		if (!isInteger(nombre)) {
			return false;
		}
		int longueurEntiere = nombre.partieEntiere.length();
		int longueurDecimale = 0;
		if (!nombre.partieDecimale.isEmpty()) {
			longueurDecimale = nombre.partieDecimale.length();
		}
		for (int index = 0; index < longueurEntiere; index++) {
			if (nombre.partieEntiere.charAt(index) != '0') {
				return false;
			}
		}
		for (int index = 0; index < longueurDecimale; index++) {
			if (nombre.partieDecimale.charAt(index) != '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compte les occurrences d'un caractere dans une chaine.
	 * @param chaine Chaine a analyser.
	 * @param caractere Caractere a compter.
	 * @return Nombre d'occurrences.
	 */
	public int compteOcurrences(String chaine, char caractere) {
		if (chaine == null || chaine.isEmpty()) {
			return 0;
		}
		int resultat = 0;
		int longueurChaine = chaine.length();
		for (int index = 0; index < longueurChaine; index++) {
			if (chaine.charAt(index) == caractere) {
				resultat++;
			}
		}
		return resultat;
	}
