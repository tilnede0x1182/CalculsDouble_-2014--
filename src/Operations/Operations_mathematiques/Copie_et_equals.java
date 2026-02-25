	/**
	 * Cree une copie profonde d'un CalculsDouble.
	 * @param original CalculsDouble a copier.
	 * @return Nouvelle instance avec les memes valeurs.
	 */
	public CalculsDouble copie(CalculsDouble original) {
		CalculsDouble resultat = new CalculsDouble(0);
		resultat.partieEntiere = original.partieEntiere;
		resultat.partieDecimale = original.partieDecimale;
		resultat.estNegatif = original.estNegatif;
		resultat.representation_anglaise = original.representation_anglaise;
		return resultat;
	}

	/**
	 * Compare l'instance courante avec un autre CalculsDouble.
	 * @param autre CalculsDouble a comparer.
	 * @return True si les deux nombres sont egaux.
	 */
	public boolean equals(CalculsDouble autre) {
		return (compareNombres(this, autre) == 0);
	}
