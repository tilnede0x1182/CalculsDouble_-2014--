
	/**
	 * Affiche un objet sans retour a la ligne.
	 * @param objet Objet a afficher.
	 */
	public static void affnn(Object objet) {
		System.out.print("" + objet);
	}

	/**
	 * Affiche un objet avec retour a la ligne.
	 * @param objet Objet a afficher.
	 */
	public static void aff(Object objet) {
		System.out.println("" + objet);
	}

	/**
	 * Affiche un tableau d'entiers avec nom par defaut.
	 * @param tableau Tableau a afficher.
	 */
	public void affTab(int[] tableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff("t[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Affiche un tableau d'entiers avec nom personnalise.
	 * @param tableau Tableau a afficher.
	 * @param nomTableau Nom a utiliser pour l'affichage.
	 */
	public void affTab(int[] tableau, String nomTableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff(nomTableau + "[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Affiche un tableau de chaines avec nom personnalise.
	 * @param tableau Tableau de chaines a afficher.
	 * @param nomTableau Nom a utiliser pour l'affichage.
	 */
	public void afftab2dim(String[] tableau, String nomTableau) {
		for (int index = 0; index < tableau.length; index++) {
			aff(nomTableau + "[" + index + "] = " + tableau[index]);
		}
	}

	/**
	 * Agrandit un tableau d'entiers (double la taille + 1).
	 * @param tableauOriginal Tableau a agrandir.
	 * @return Nouveau tableau agrandi.
	 */
	public int[] agrandieTab(int[] tableauOriginal) {
		int[] nouveauTableau = new int[tableauOriginal.length * 2 + 1];
		for (int index = 0; index < tableauOriginal.length; index++) {
			nouveauTableau[index] = tableauOriginal[index];
		}
		return nouveauTableau;
	}
