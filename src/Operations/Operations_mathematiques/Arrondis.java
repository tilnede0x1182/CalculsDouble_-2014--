
	/**
	 * Arrondit un CalculsDouble a l'entier le plus proche.
	 * @param nombre CalculsDouble a arrondir.
	 * @return Partie entiere arrondie.
	 */
	public int arrondiEntier(CalculsDouble nombre) {
		String resultat = arrondi(nombre).partieEntiere;
		if (resultat.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(resultat);
	}

	/**
	 * Arrondit un CalculsDouble et retourne une chaine.
	 * @param nombre CalculsDouble a arrondir.
	 * @return Chaine de l'arrondi.
	 */
	public String arrondiString(CalculsDouble nombre) {
		return arrondiString(nombre, 0);
	}

	/**
	 * Arrondit un CalculsDouble avec precision et retourne une chaine.
	 * @param nombre CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales souhaitees.
	 * @return Chaine de l'arrondi.
	 */
	public String arrondiString(CalculsDouble nombre, int nombreDecimales) {
		return "" + arrondi(nombre, nombreDecimales);
	}

	/**
	 * Arrondit un CalculsDouble a l'entier le plus proche.
	 * @param nombre CalculsDouble a arrondir.
	 * @return CalculsDouble arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble nombre) {
		return arrondi(nombre, 0);
	}

// ------------------------------------------------------------------------------
// Arrondi avec precision
// ------------------------------------------------------------------------------

	/**
	 * Arrondit un CalculsDouble avec precision specifiee.
	 * @param nombreOriginal CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales (0 = entier).
	 * @return CalculsDouble arrondi.
	 */
	public CalculsDouble arrondi(CalculsDouble nombreOriginal, int nombreDecimales) {
		CalculsDouble nombre = copie(nombreOriginal);
		if (nombreDecimales < 1) {
			return arrondiEntierSeul(nombre);
		}
		if (nombre.partieDecimale.isEmpty()) {
			return nombre;
		}
		if (nombre.partieDecimale.length() <= nombreDecimales) {
			return nombre;
		}
		return arrondiAvecDecimales(nombre, nombreDecimales);
	}

	/**
	 * Arrondit a l'entier sans garder de decimales.
	 * @param nombre CalculsDouble a arrondir.
	 * @return CalculsDouble arrondi a l'entier.
	 */
	private CalculsDouble arrondiEntierSeul(CalculsDouble nombre) {
		if (nombre.partieDecimale.isEmpty()) {
			return nombre;
		}
		if (Integer.parseInt("" + nombre.partieDecimale.charAt(0)) > 4) {
			nombre = this.additionneNombres(nombre, new CalculsDouble(1));
		}
		nombre.partieDecimale = "";
		return nombre;
	}

	/**
	 * Arrondit en conservant un nombre de decimales.
	 * @param nombre CalculsDouble a arrondir.
	 * @param nombreDecimales Nombre de decimales a garder.
	 * @return CalculsDouble arrondi.
	 */
	private CalculsDouble arrondiAvecDecimales(CalculsDouble nombre, int nombreDecimales) {
		if (Integer.parseInt("" + nombre.partieDecimale.charAt(nombreDecimales)) <= 4) {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, nombreDecimales);
			return nombre;
		}
		return propagerArrondi(nombre, nombreDecimales);
	}

	/**
	 * Propage l'arrondi vers les chiffres superieurs.
	 * @param nombre CalculsDouble a traiter.
	 * @param position Position de depart.
	 * @return CalculsDouble avec arrondi propage.
	 */
	private CalculsDouble propagerArrondi(CalculsDouble nombre, int position) {
		int positionCourante = position;
		boolean continuer = true;
		while (positionCourante > 0 && continuer) {
			if (Integer.parseInt("" + nombre.partieDecimale.charAt(positionCourante)) <= 4) {
				continuer = false;
			} else {
				nombre = incrementerDecimale(nombre, positionCourante);
				positionCourante--;
			}
		}
		if (positionCourante == 0 && Integer.parseInt("" + nombre.partieDecimale.charAt(0)) > 4) {
			nombre.partieDecimale = "";
			nombre.partieEntiere = "" + (Integer.parseInt(nombre.partieEntiere) + 1);
		}
		return nombre;
	}

	/**
	 * Incremente une decimale a une position donnee.
	 * @param nombre CalculsDouble a modifier.
	 * @param position Position de la decimale.
	 * @return CalculsDouble modifie.
	 */
	private CalculsDouble incrementerDecimale(CalculsDouble nombre, int position) {
		int chiffreIncrement = Integer.parseInt("" + nombre.partieDecimale.charAt(position - 1)) + 1;
		String chaineIncrement = "" + chiffreIncrement;
		if (chaineIncrement.length() > 1) {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, position);
		} else {
			nombre.partieDecimale = nombre.partieDecimale.substring(0, position - 1) + chaineIncrement;
		}
		return nombre;
	}
