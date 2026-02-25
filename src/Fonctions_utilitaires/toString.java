	/**
	 * Convertit le CalculsDouble en chaine de caracteres.
	 * Gere le signe, la virgule/point et les chiffres significatifs.
	 * @return Representation textuelle du nombre.
	 */
	public String toString() {
		String signeMoins = "";
		String affichageEntier = partieEntiere;
		String affichageDecimal = chiffresSignificatifs(this.partieDecimale, -1);
		char virgule = ',';
		if (representation_anglaise) {
			virgule = '.';
		}
		String chaineDecimale = virgule + affichageDecimal;
		if (affichageEntier.isEmpty()) {
			affichageEntier = "0";
		}
		if (partieDecimaleEstVide(affichageDecimal)) {
			chaineDecimale = "";
		}
		if (estNegatif) {
			signeMoins = "-";
		}
		return (signeMoins + affichageEntier + chaineDecimale);
	}

	/**
	 * Verifie si la partie decimale est vide ou nulle.
	 * @param partieDecimale Chaine de la partie decimale.
	 * @return True si vide ou egale a zero.
	 */
	private boolean partieDecimaleEstVide(String partieDecimale) {
		if (partieDecimale.isEmpty()) {
			return true;
		}
		return Integer.parseInt(partieDecimale) == 0;
	}
