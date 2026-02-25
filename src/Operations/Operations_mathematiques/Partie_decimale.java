
	/**
	 * Extrait la partie decimale d'un double.
	 * Gere les notations avec virgule ou point.
	 * @param nombre Double dont extraire la decimale.
	 * @return Chaine representant la partie decimale.
	 */
	public String partieDecimale(double nombre) {
		String nombreChaine = "" + nombre;
		String separateur = ".";
		if (nombreChaine.indexOf(",") != -1) {
			separateur = ",";
		}
		if (nombreChaine.indexOf(separateur) == -1) {
			return "0";
		}
		return extraireApresPoint(nombreChaine, separateur.charAt(0));
	}

	/**
	 * Extrait la partie apres le separateur decimal.
	 * @param nombreChaine Chaine du nombre complet.
	 * @param separateur Caractere separateur (. ou ,).
	 * @return Partie apres le separateur.
	 */
	private String extraireApresPoint(String nombreChaine, char separateur) {
		boolean pointTrouve = false;
		String resultat = "";
		int longueur = nombreChaine.length();
		for (int index = 0; index < longueur; index++) {
			if (pointTrouve) {
				resultat += nombreChaine.charAt(index);
			}
			if (nombreChaine.charAt(index) == separateur) {
				pointTrouve = true;
			}
		}
		return resultat;
	}
