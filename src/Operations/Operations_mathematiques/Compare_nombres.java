	/**
	 * Compare deux CalculsDouble en tenant compte du signe.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egaux, 1 si A > B, 2 si B > A, -1 si erreur.
	 */
	public int compareNombres(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.estNegatif && !nombreB.estNegatif) {
			return 2;
		}
		if (!nombreA.estNegatif && nombreB.estNegatif) {
			return 1;
		}
		if (nombreA.estNegatif && nombreB.estNegatif) {
			return inverserResultatComparaison(compareNombres_abs(nombreA, nombreB));
		}
		if (!nombreA.estNegatif && !nombreB.estNegatif) {
			return compareNombres_abs(nombreA, nombreB);
		}
		return -1;
	}

	/**
	 * Inverse le resultat de comparaison pour nombres negatifs.
	 * @param resultat Resultat original.
	 * @return Resultat inverse (1 devient 2, 2 devient 1).
	 */
	private int inverserResultatComparaison(int resultat) {
		if (resultat == 0) {
			return 0;
		}
		if (resultat == 1) {
			return 2;
		}
		if (resultat == 2) {
			return 1;
		}
		return -1;
	}

// ------------------------------------------------------------------------------
// Comparaison en valeur absolue
// ------------------------------------------------------------------------------

	/**
	 * Compare deux CalculsDouble en valeur absolue.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egaux, 1 si |A| > |B|, 2 si |B| > |A|.
	 */
	public int compareNombres_abs(CalculsDouble nombreA, CalculsDouble nombreB) {
		int comparaisonEntiere = comparerPartiesEntieres(nombreA, nombreB);
		if (comparaisonEntiere != 0) {
			return comparaisonEntiere;
		}
		return comparerPartiesDecimales(nombreA, nombreB);
	}

	/**
	 * Compare les parties entieres de deux nombres.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerPartiesEntieres(CalculsDouble nombreA, CalculsDouble nombreB) {
		if (nombreA.partieEntiere.length() > nombreB.partieEntiere.length()) {
			return 1;
		}
		if (nombreA.partieEntiere.length() < nombreB.partieEntiere.length()) {
			return 2;
		}
		int longueur = nombreA.partieEntiere.length();
		for (int index = 0; index < longueur; index++) {
			int chiffreA = Integer.parseInt("" + nombreA.partieEntiere.charAt(index));
			int chiffreB = Integer.parseInt("" + nombreB.partieEntiere.charAt(index));
			if (chiffreA > chiffreB) {
				return 1;
			}
			if (chiffreA < chiffreB) {
				return 2;
			}
		}
		return 0;
	}

	/**
	 * Compare les parties decimales de deux nombres.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerPartiesDecimales(CalculsDouble nombreA, CalculsDouble nombreB) {
		int longueurMin = Math.min(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		for (int index = 0; index < longueurMin; index++) {
			int chiffreA = Integer.parseInt("" + nombreA.partieDecimale.charAt(index));
			int chiffreB = Integer.parseInt("" + nombreB.partieDecimale.charAt(index));
			if (chiffreA > chiffreB) {
				return 1;
			}
			if (chiffreB > chiffreA) {
				return 2;
			}
		}
		return comparerDecimalesRestantes(nombreA, nombreB, longueurMin);
	}

	/**
	 * Compare les decimales restantes apres la longueur commune.
	 * @param nombreA Premier nombre.
	 * @param nombreB Second nombre.
	 * @param longueurMin Longueur commune deja comparee.
	 * @return 0 si egales, 1 si A > B, 2 si B > A.
	 */
	private int comparerDecimalesRestantes(CalculsDouble nombreA, CalculsDouble nombreB, int longueurMin) {
		int longueurMax = Math.max(nombreA.partieDecimale.length(), nombreB.partieDecimale.length());
		if (nombreA.partieDecimale.length() != nombreB.partieDecimale.length() && longueurMax > longueurMin) {
			if (nombreA.partieDecimale.length() > longueurMin) {
				if (Integer.parseInt("" + nombreA.partieDecimale.charAt(longueurMin)) > 0) {
					return 1;
				}
			}
			if (nombreB.partieDecimale.length() > longueurMin) {
				if (Integer.parseInt("" + nombreB.partieDecimale.charAt(longueurMin)) > 0) {
					return 2;
				}
			}
		}
		return 0;
	}
