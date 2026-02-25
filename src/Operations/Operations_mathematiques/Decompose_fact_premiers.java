
	/**
	 * Decompose un nombre en facteurs premiers.
	 * Ignore la partie decimale (troncature).
	 * @param nombre CalculsDouble a decomposer.
	 * @return Tableau des facteurs premiers ou null si invalide.
	 */
	public int[] decompose_en_facteurs_premiers(CalculsDouble nombre) {
		if (!nombre.partieDecimale.isEmpty()) {
			aff("n0.dec!=" + '"' + '"' + " : on prend la troncature entiere du nombre.");
		}
		if (nombre.partieEntiere.isEmpty()) {
			return null;
		}
		int valeurCourante = Integer.parseInt(nombre.partieEntiere);
		aff("n = " + valeurCourante);
		String facteursChaine = construireChaineFacteurs(valeurCourante);
		return convertirEnTableauFacteurs(facteursChaine);
	}

	/**
	 * Construit la chaine des facteurs premiers separes par :.
	 * @param valeur Valeur a factoriser.
	 * @return Chaine des facteurs (ex: "2:3:5:7").
	 */
	private String construireChaineFacteurs(int valeur) {
		String facteursChaine = "";
		while (!estPremier(valeur)) {
			int facteur = trouverPremierFacteur(valeur);
			facteursChaine += facteur + ":";
			valeur = valeur / facteur;
			if (valeur == 0) {
				valeur = 1;
			}
			aff("n = " + valeur);
		}
		facteursChaine += "" + valeur;
		return facteursChaine;
	}

	/**
	 * Trouve le premier facteur premier d'un nombre.
	 * @param valeur Nombre a factoriser.
	 * @return Premier facteur trouve.
	 */
	private int trouverPremierFacteur(int valeur) {
		int longueur = ("" + valeur).length();
		char dernierChiffre = ("" + valeur).charAt(longueur - 1);
		if (dernierChiffre == '0' || dernierChiffre == '5') {
			return 5;
		}
		if (dernierChiffre == '2' || dernierChiffre == '4' || dernierChiffre == '8') {
			return 2;
		}
		if (valeur % 3 == 0) {
			return 3;
		}
		for (int diviseur = 2; diviseur < valeur && valeur % diviseur != 0; diviseur++) {
		}
		return valeur;
	}

// ------------------------------------------------------------------------------
// Conversion et verification
// ------------------------------------------------------------------------------

	/**
	 * Convertit une chaine de facteurs en tableau d'entiers.
	 * @param facteursChaine Chaine de facteurs separes par :.
	 * @return Tableau des facteurs.
	 */
	private int[] convertirEnTableauFacteurs(String facteursChaine) {
		aff("restmp = " + facteursChaine);
		String[] facteursSplit = facteursChaine.split(":");
		int nombreFacteurs = compterFacteursValides(facteursSplit);
		int[] resultat = remplirTableauFacteurs(facteursSplit, nombreFacteurs);
		afficherVerification(resultat);
		return resultat;
	}

	/**
	 * Compte le nombre de facteurs valides dans le tableau.
	 * @param facteurs Tableau de chaines.
	 * @return Nombre de chaines non vides.
	 */
	private int compterFacteursValides(String[] facteurs) {
		int compteur = 0;
		for (int index = 0; index < facteurs.length; index++) {
			if (!facteurs[index].isEmpty()) {
				compteur++;
			}
		}
		return compteur;
	}

	/**
	 * Remplit un tableau d'entiers avec les facteurs valides.
	 * @param facteurs Tableau de chaines.
	 * @param nombreFacteurs Taille du tableau resultat.
	 * @return Tableau d'entiers.
	 */
	private int[] remplirTableauFacteurs(String[] facteurs, int nombreFacteurs) {
		int[] resultat = new int[nombreFacteurs];
		int indexResultat = 0;
		for (int index = 0; index < facteurs.length; index++) {
			if (!facteurs[index].isEmpty()) {
				resultat[indexResultat] = Integer.parseInt(facteurs[index]);
				indexResultat++;
			}
		}
		return resultat;
	}

	/**
	 * Affiche la verification du produit des facteurs.
	 * @param facteurs Tableau des facteurs.
	 */
	private void afficherVerification(int[] facteurs) {
		int verification = 1;
		for (int index = 0; index < facteurs.length; index++) {
			verification *= facteurs[index];
		}
		aff("Verification : n = " + verification);
	}
