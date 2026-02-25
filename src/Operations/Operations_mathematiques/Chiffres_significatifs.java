	/**
	 * Ajuste les chiffres significatifs de la partie decimale.
	 * Si nombreChiffres = -1, enleve les zeros inutiles (mode maths).
	 * Sinon, ajuste a exactement nombreChiffres decimales.
	 * @param partieDecimale Chaine de la partie decimale.
	 * @param nombreChiffres Nombre de chiffres souhaites (-1 = minimum).
	 * @return Partie decimale ajustee.
	 */
	public String chiffresSignificatifs(String partieDecimale, int nombreChiffres) {
		int longueur = partieDecimale.length();
		int zerosAAjouter = nombreChiffres - longueur;
		partieDecimale = envleveZerosInutils(partieDecimale);
		if (nombreChiffres == -1) {
			return partieDecimale;
		}
		for (int index = 0; index < zerosAAjouter; index++) {
			partieDecimale += '0';
		}
		return partieDecimale;
	}
