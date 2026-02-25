
	/**
	 * Calcule la somme des chiffres d'une chaine numerique.
	 * @param nombreChaine Chaine de chiffres.
	 * @return Somme de tous les chiffres.
	 */
	public int somme_des_chiffres(String nombreChaine) {
		if (nombreChaine.isEmpty()) {
			return 0;
		}
		int resultat = 0;
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			resultat += Integer.parseInt("" + nombreChaine.charAt(index));
		}
		return resultat;
	}

	/**
	 * Calcule le produit des chiffres d'une chaine numerique.
	 * @param nombreChaine Chaine de chiffres.
	 * @return Produit de tous les chiffres.
	 */
	public int produit_des_chiffres(String nombreChaine) {
		if (nombreChaine.isEmpty()) {
			return 0;
		}
		int resultat = 1;
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			resultat *= Integer.parseInt("" + nombreChaine.charAt(index));
		}
		return resultat;
	}
