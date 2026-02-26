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
		if (Math.abs(nombre) < 1 && nombre != 0) {
			partieEntiere = "0";
			partieDecimale = extraireDecimaleNombrePetit(nombre);
			if (nombre < 0) {
				estNegatif = true;
			}
		} else {
			partieEntiere = "" + (long)(nombre);
			partieDecimale = partieDecimale(nombre);
			extraireSigneNegatif();
		}
	}

	/**
	 * Extrait la partie decimale pour nombres < 1.
	 * Gere la notation scientifique (ex: 1.0E-4).
	 * @param nombre Nombre dont extraire la decimale.
	 * @return Chaine representant la partie decimale.
	 */
	private String extraireDecimaleNombrePetit(double nombre) {
		double absNombre = Math.abs(nombre);
		String nombreStr = String.format(java.util.Locale.US, "%.15f", absNombre);
		int indexPoint = nombreStr.indexOf('.');
		if (indexPoint != -1) {
			String decimale = nombreStr.substring(indexPoint + 1);
			while (decimale.endsWith("0") && decimale.length() > 1) {
				decimale = decimale.substring(0, decimale.length() - 1);
			}
			return decimale;
		}
		return "0";
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
		partieEntiere = "" + (long)(nombre);
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
