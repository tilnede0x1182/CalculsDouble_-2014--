	/**
	 * Teste si un entier est un nombre premier.
	 * @param nombre Entier a tester.
	 * @return True si le nombre est premier.
	 */
	public boolean estPremier(int nombre) {
		int limiteRecherche = 1 + (int)(Math.sqrt((double)(nombre)));
		for (int diviseur = 2; diviseur < limiteRecherche; diviseur++) {
			if (nombre % diviseur == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calcule et affiche les n premiers nombres premiers.
	 * @param nombrePremiers Quantite de nombres premiers a trouver.
	 */
	public void clacule_n_premiers(int nombrePremiers) {
		if (nombrePremiers < 1) {
			return;
		}
		int[] resultat = new int[nombrePremiers];
		int compteur = 0;
		int candidat = 2;
		while (compteur < nombrePremiers) {
			if (estPremier(candidat)) {
				resultat[compteur] = candidat;
				compteur++;
			}
			candidat++;
		}
		afficherTableauPremiers(resultat, nombrePremiers);
	}

	/**
	 * Affiche un tableau de nombres premiers.
	 * @param tableau Tableau des nombres premiers.
	 * @param nombrePremiers Quantite de nombres dans le tableau.
	 */
	private void afficherTableauPremiers(int[] tableau, int nombrePremiers) {
		affnn("p" + nombrePremiers + " = {");
		for (int index = 0; index < tableau.length - 1; index++) {
			affnn(tableau[index] + ", ");
		}
		aff(tableau[tableau.length - 1] + "};");
	}
