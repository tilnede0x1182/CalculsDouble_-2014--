	/**
	 * Enleve les zeros en debut de la partie entiere.
	 * Exemple : "007" devient "7", "000" devient "0".
	 * @param partieEntiere Chaine representant la partie entiere.
	 * @return Chaine nettoyee des zeros superflus.
	 */
	public String epureZerosEnt(String partieEntiere) {
		int longueur = partieEntiere.length();
		int index = 0;
		while (index < longueur && partieEntiere.charAt(index) == '0') {
			index++;
		}
		if (index == longueur) {
			return "0";
		}
		return partieEntiere.substring(index, longueur);
	}

	/**
	 * Enleve les zeros inutiles en fin de partie decimale.
	 * Exemple : "1200" devient "12", "100" devient "1".
	 * @param partieDecimale Chaine representant la partie decimale.
	 * @return Chaine nettoyee des zeros superflus.
	 */
	public String envleveZerosInutils(String partieDecimale) {
		int longueur = partieDecimale.length();
		int positionFinale = longueur;
		for (int index = 0; index < longueur; index++) {
			if (partieDecimale.charAt(index) == '0') {
				positionFinale = index;
			} else {
				positionFinale = longueur;
			}
		}
		return partieDecimale.substring(0, positionFinale);
	}
