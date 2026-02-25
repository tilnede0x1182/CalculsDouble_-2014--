	/**
	 * Constructeur par defaut.
	 * Initialise un CalculsDouble a zero.
	 */
	public CalculsDouble() {
		partieEntiere = "0";
		partieDecimale = "";
	}

	/**
	 * Constructeur a partir d'un entier.
	 * @param nombre Valeur entiere a stocker.
	 */
	public CalculsDouble(int nombre) {
		partieEntiere = "" + (int)(nombre);
		partieDecimale = "";
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un double.
	 * @param nombre Valeur decimale a stocker.
	 */
	public CalculsDouble(double nombre) {
		partieEntiere = "" + (int)(nombre);
		partieDecimale = partieDecimale(nombre);
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un entier avec representation.
	 * @param nombre Valeur entiere a stocker.
	 * @param representationAnglaise True pour notation avec point.
	 */
	public CalculsDouble(int nombre, boolean representationAnglaise) {
		representation_anglaise = representationAnglaise;
		partieEntiere = "" + (int)(nombre);
		partieDecimale = "0";
		extraireSigneNegatif();
	}

	/**
	 * Constructeur a partir d'un double avec representation.
	 * @param nombre Valeur decimale a stocker.
	 * @param representationAnglaise True pour notation avec point.
	 */
	public CalculsDouble(double nombre, boolean representationAnglaise) {
		representation_anglaise = representationAnglaise;
		partieEntiere = "" + (int)(nombre);
		partieDecimale = partieDecimale(nombre);
		extraireSigneNegatif();
	}

	/**
	 * Extrait le signe negatif de la partie entiere.
	 * Met a jour estNegatif et nettoie partieEntiere.
	 */
	private void extraireSigneNegatif() {
		if (partieEntiere.contains("-")) {
			estNegatif = true;
			partieEntiere = partieEntiere.replace("-", "");
		}
	}
