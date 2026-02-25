	/**
	 * Division avec precision par defaut (8 chiffres significatifs).
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @return Tableau [quotient, reste].
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur) {
		return division_boucle(dividende, diviseur, true, 8);
	}

	/**
	 * Division avec precision specifiee.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param chiffresSignificatifs Precision souhaitee.
	 * @return Tableau [quotient, reste].
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur, int chiffresSignificatifs) {
		return division_boucle(dividende, diviseur, true, chiffresSignificatifs);
	}

	/**
	 * Division complete avec tous les parametres.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat sinon arrondit.
	 * @param chiffresSignificatifs Precision souhaitee (max 8).
	 * @return Tableau [quotient, reste] ou null si division par zero.
	 */
	public CalculsDouble[] division_boucle(CalculsDouble dividende, CalculsDouble diviseur, boolean tronquee, int chiffresSignificatifs) {
		if (!tronquee) {
			chiffresSignificatifs++;
		}
		chiffresSignificatifs = validerChiffresSignificatifs(chiffresSignificatifs);
		CalculsDouble[] casParticulier = verifierCasParticuliers(dividende, diviseur);
		if (casParticulier != null) {
			return casParticulier;
		}
		return effectuerDivision(dividende, diviseur, tronquee, chiffresSignificatifs);
	}

// ------------------------------------------------------------------------------
// Validation et cas particuliers
// ------------------------------------------------------------------------------

	/**
	 * Valide et limite le nombre de chiffres significatifs.
	 * @param chiffresSignificatifs Valeur demandee.
	 * @return Valeur validee (max 9).
	 */
	private int validerChiffresSignificatifs(int chiffresSignificatifs) {
		if (chiffresSignificatifs > 9) {
			affnn("Erreur : impossible de faire ");
			aff("plus de 8 chiffres significatifs.");
			return 9;
		}
		return chiffresSignificatifs;
	}

	/**
	 * Verifie les cas particuliers de division.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @return Resultat si cas particulier, null sinon.
	 */
	private CalculsDouble[] verifierCasParticuliers(CalculsDouble dividende, CalculsDouble diviseur) {
		if (diviseur.equals(new CalculsDouble(0))) {
			aff("Impossible de diviser par 0.");
			aff("Impossible dividing by 0.");
			return null;
		}
		if (dividende.equals(new CalculsDouble(0))) {
			return creerResultatZero();
		}
		if (diviseur.equals(new CalculsDouble(1))) {
			return creerResultatDiviseurUn(dividende);
		}
		if (dividende.equals(diviseur)) {
			return creerResultatEgaux();
		}
		return null;
	}

	/**
	 * Cree le resultat pour dividende = 0.
	 * @return Tableau [0, 0].
	 */
	private CalculsDouble[] creerResultatZero() {
		return new CalculsDouble[]{new CalculsDouble(0), new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour diviseur = 1.
	 * @param dividende Le dividende.
	 * @return Tableau [dividende, 0].
	 */
	private CalculsDouble[] creerResultatDiviseurUn(CalculsDouble dividende) {
		CalculsDouble quotient = new CalculsDouble(0);
		quotient.partieEntiere = dividende.partieEntiere;
		quotient.partieDecimale = dividende.partieDecimale;
		return new CalculsDouble[]{quotient, new CalculsDouble(0)};
	}

	/**
	 * Cree le resultat pour dividende = diviseur.
	 * @return Tableau [1, 0].
	 */
	private CalculsDouble[] creerResultatEgaux() {
		return new CalculsDouble[]{new CalculsDouble(1), new CalculsDouble(0)};
	}

// ------------------------------------------------------------------------------
// Algorithme de division
// ------------------------------------------------------------------------------

	/**
	 * Effectue la division par soustractions successives.
	 * @param dividende Nombre a diviser.
	 * @param diviseur Diviseur.
	 * @param tronquee Si true, tronque le resultat.
	 * @param chiffresSignificatifs Precision.
	 * @return Tableau [quotient, reste].
	 */
	private CalculsDouble[] effectuerDivision(CalculsDouble dividende, CalculsDouble diviseur, boolean tronquee, int chiffresSignificatifs) {
		int chiffresRestants = chiffresSignificatifs;
		CalculsDouble reste = new CalculsDouble(0);
		reste.partieEntiere = dividende.ent + dividende.dec;
		CalculsDouble quotient = new CalculsDouble(0);
		CalculsDouble diviseurComplet = new CalculsDouble(0);
		diviseurComplet.partieEntiere = diviseur.partieEntiere + diviseur.partieDecimale;
		boolean apresVirguleGlobal = false;
		CalculsDouble[] etatDivision = new CalculsDouble[]{quotient, reste};
		etatDivision = boucleDivision(etatDivision, diviseurComplet, chiffresRestants, chiffresSignificatifs, apresVirguleGlobal);
		quotient = etatDivision[0];
		reste = etatDivision[1];
		quotient.estNegatif = calculerSigneDivision(dividende, diviseur);
		if (!tronquee) {
			quotient = arrondi(quotient, chiffresSignificatifs - 1);
		}
		return new CalculsDouble[]{quotient, reste};
	}

	/**
	 * Boucle principale de la division.
	 * @param etat Tableau [quotient, reste].
	 * @param diviseur Diviseur complet.
	 * @param chiffresRestants Chiffres restants a calculer.
	 * @param chiffresTotal Chiffres totaux.
	 * @param apresVirgule Indicateur de position apres virgule.
	 * @return Tableau [quotient, reste] mis a jour.
	 */
	private CalculsDouble[] boucleDivision(CalculsDouble[] etat, CalculsDouble diviseur, int chiffresRestants, int chiffresTotal, boolean apresVirgule) {
		CalculsDouble quotient = etat[0];
		CalculsDouble reste = etat[1];
		do {
			boolean resteInferieur = !resteSuperieurOuEgal(reste, diviseur);
			if (resteInferieur) {
				apresVirgule = true;
			}
			if (chiffresRestants > 0 && resteInferieur) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
			reste = soustraitNombres(reste, diviseur);
			quotient = incrementerQuotient(quotient, apresVirgule, chiffresRestants, chiffresTotal);
			resteInferieur = !resteSuperieurOuEgal(reste, diviseur);
			if (resteInferieur) {
				apresVirgule = true;
			}
			if (chiffresRestants > 0 && resteInferieur) {
				reste = mult(reste, new CalculsDouble(10));
				chiffresRestants--;
			}
		} while (resteSuperieurOuEgal(reste, diviseur));
		return new CalculsDouble[]{quotient, reste};
	}

	/**
	 * Verifie si le reste est superieur ou egal au diviseur.
	 * @param reste Le reste courant.
	 * @param diviseur Le diviseur.
	 * @return True si reste >= diviseur.
	 */
	private boolean resteSuperieurOuEgal(CalculsDouble reste, CalculsDouble diviseur) {
		int comparaison = compareNombres(reste, diviseur);
		return comparaison == 0 || comparaison == 1;
	}

	/**
	 * Incremente le quotient selon la position.
	 * @param quotient Quotient actuel.
	 * @param apresVirgule Si on est apres la virgule.
	 * @param chiffresRestants Chiffres restants.
	 * @param chiffresTotal Chiffres totaux.
	 * @return Quotient incremente.
	 */
	private CalculsDouble incrementerQuotient(CalculsDouble quotient, boolean apresVirgule, int chiffresRestants, int chiffresTotal) {
		CalculsDouble increment = new CalculsDouble(0);
		if (!apresVirgule) {
			increment.partieEntiere = "1";
			increment.partieDecimale = "";
		} else {
			increment.partieEntiere = "0";
			String zeros = "";
			for (int index = chiffresRestants + 1; index < chiffresTotal; index++) {
				zeros += "0";
			}
			increment.partieDecimale = zeros + "1";
		}
		return additionneNombres(quotient, increment);
	}

	/**
	 * Calcule le signe du quotient.
	 * @param dividende Le dividende.
	 * @param diviseur Le diviseur.
	 * @return True si le quotient est negatif.
	 */
	private boolean calculerSigneDivision(CalculsDouble dividende, CalculsDouble diviseur) {
		boolean unSeulNegatif = dividende.estNegatif || diviseur.estNegatif;
		boolean lesDeuxNegatifs = dividende.estNegatif && diviseur.estNegatif;
		return unSeulNegatif && !lesDeuxNegatifs;
	}
