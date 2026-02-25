
	/**
	 * Egalise les parties decimales de deux CalculsDouble.
	 * Ajoute des zeros pour que les deux aient la meme longueur.
	 * @param nombreA Premier nombre a egaliser.
	 * @param nombreB Second nombre a egaliser.
	 * @return Tableau des deux nombres egalises.
	 */
	public CalculsDouble[] egaliseNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.partieDecimale.length() != nombreB.partieDecimale.length()) {
			completerDecimales(nombreA, nombreB);
		}
		CalculsDouble[] resultat = new CalculsDouble[2];
		resultat[0] = nombreA;
		resultat[1] = nombreB;
		return resultat;
	}

	/**
	 * Complete les decimales du nombre le plus court.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 */
	private void completerDecimales(CalculsDouble nombreA, CalculsDouble nombreB) {
		int longueurMax = Math.max(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		if (longueurMax == nombreA.partieDecimale.length()) {
			nombreB.partieDecimale += genererZeros(nombreA.partieDecimale.length() - nombreB.partieDecimale.length());
		}
		if (longueurMax == nombreB.partieDecimale.length()) {
			nombreA.partieDecimale += genererZeros(nombreB.partieDecimale.length() - nombreA.partieDecimale.length());
		}
	}

	/**
	 * Genere une chaine de zeros.
	 * @param nombreZeros Nombre de zeros a generer.
	 * @return Chaine de zeros.
	 */
	private String genererZeros(int nombreZeros) {
		String zeros = "";
		for (int index = 0; index < nombreZeros; index++) {
			zeros += "0";
		}
		return zeros;
	}
