
	/**
	 * Retourne la valeur absolue d'un entier.
	 * @param nombre Entier a traiter.
	 * @return Valeur absolue.
	 */
	public int abs(int nombre) {
		if (nombre < 0) {
			return -nombre;
		}
		return nombre;
	}

	/**
	 * Met la valeur absolue sur l'instance courante.
	 * Modifie directement estNegatif a false.
	 */
	public void abs() {
		this.estNegatif = false;
	}

	/**
	 * Retourne la valeur absolue d'un CalculsDouble.
	 * Cree une copie avec estNegatif a false.
	 * @param nombre CalculsDouble a traiter.
	 * @return Nouvelle instance avec valeur absolue.
	 */
	public CalculsDouble abs(CalculsDouble nombre) {
		CalculsDouble resultat = new CalculsDouble(0);
		resultat.partieEntiere = nombre.partieEntiere;
		resultat.partieDecimale = nombre.partieDecimale;
		resultat.representation_anglaise = nombre.representation_anglaise;
		resultat.estNegatif = false;
		return resultat;
	}

	/**
	 * Retourne la partie entiere d'un double.
	 * @param nombre Double a tronquer.
	 * @return Partie entiere.
	 */
	public int partieEntiere(double nombre) {
		return (int)(nombre);
	}

	/**
	 * Retourne la partie entiere de l'instance courante.
	 * @return Partie entiere ou 0 si vide.
	 */
	public int partieEntiere() {
		if (this.partieEntiere.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(this.partieEntiere);
	}
