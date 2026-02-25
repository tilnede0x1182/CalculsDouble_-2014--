
// ==============================================================================
// Donnees
// ==============================================================================

	Character[] listeOperateurs = {'+', '-', '*', '/'};
	Stack<String> pileCalcul = new Stack<String>();
	ArrayList<Character> operateursAutorises = new ArrayList<Character>();

// ==============================================================================
// Fonctions utilitaires
// ==============================================================================

	/**
	 * Initialise la liste des operateurs autorises.
	 * Copie les operateurs du tableau vers l'ArrayList.
	 */
	public void operateurs() {
		for (Character caractere : listeOperateurs) {
			operateursAutorises.add(caractere);
		}
	}

// ------------------------------------------------------------------------------
// Gestion de la pile
// ------------------------------------------------------------------------------

	/**
	 * Depile un element avec gestion d'erreur.
	 * @param pile Pile a depiler.
	 * @return Element depile ou chaine vide si pile vide.
	 */
	public String popAvecErreur(Stack<String> pile) {
		if (!pile.empty()) {
			return pile.pop();
		} else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	/**
	 * Effectue un calcul avec les 3 elements en haut de pile.
	 * Depile operande, operateur, operande puis empile le resultat.
	 * @param pile Pile de calcul.
	 */
	public void calculPile(Stack<String> pile) {
		String operandeA = popAvecErreur(pile);
		String symbole = popAvecErreur(pile);
		String operandeB = popAvecErreur(pile);
		int resultatTemporaire = traiteSymbolesInt(operandeB, operandeA, symbole);
		aff("resultatTemporaire = " + resultatTemporaire);
		pile.push("" + resultatTemporaire);
	}

// ------------------------------------------------------------------------------
// Verification des nombres doubles
// ------------------------------------------------------------------------------

	/**
	 * Verifie et nettoie un nombre double avec separateurs.
	 * @param nombre Nombre a verifier.
	 * @param separateur Separateur decimal utilise.
	 * @return Nombre nettoye ou chaine vide si invalide.
	 */
	public String verifieDoubleCorrecte(String nombre, char separateur) {
		if (nombre == null || nombre.isEmpty()) {
			aff("Le nombre est null ou vide.");
			return "";
		}
		char separateurAlternatif = (separateur == '.') ? ',' : '.';
		String[] partiesNombre = nombre.split("" + separateur);
		if (partiesNombre == null) {
			return "";
		}
		if (partiesNombre.length == 1) {
			return verifierPartieUnique(partiesNombre[0], separateur);
		}
		if (partiesNombre.length < 2) {
			return "";
		}
		return verifierPartieDecimale(partiesNombre, separateurAlternatif);
	}

	/**
	 * Verifie une partie unique du nombre.
	 * @param partie Partie a verifier.
	 * @param separateur Separateur a retirer.
	 * @return Partie nettoyee ou chaine vide si invalide.
	 */
	private String verifierPartieUnique(String partie, char separateur) {
		if (partie == null || partie.isEmpty() || partie.equals(",")) {
			return "";
		}
		return partie.replace("" + separateur, "");
	}

// ------------------------------------------------------------------------------
// Verification partie decimale
// ------------------------------------------------------------------------------

	/**
	 * Verifie la partie decimale avec separateurs de milliers.
	 * @param partiesNombre Tableau des parties du nombre.
	 * @param separateurMilliers Separateur de milliers attendu.
	 * @return Nombre nettoye ou chaine vide si invalide.
	 */
	private String verifierPartieDecimale(String[] partiesNombre, char separateurMilliers) {
		String partieDecimale = partiesNombre[1];
		int longueurDecimale = partieDecimale.length();
		for (int index = 0; index < longueurDecimale; index++) {
			if (index % 3 == 0) {
				if (partieDecimale.charAt(index) != separateurMilliers) {
					return "";
				}
			} else {
				if (!isCharNumber(partieDecimale.charAt(index))) {
					return "";
				}
			}
		}
		return partiesNombre[0] + partieDecimale.replace("" + separateurMilliers, "");
	}

// ------------------------------------------------------------------------------
// Analyse des nombres
// ------------------------------------------------------------------------------

	/**
	 * Analyse et convertit un nombre double depuis une chaine.
	 * Gere les formats avec virgule ou point decimal.
	 * @param nombre Chaine representant le nombre.
	 * @return Valeur double ou -1 en cas d'erreur.
	 */
	public double analyseNombreDouble(String nombre) {
		int nombreVirgules = compteOcurrences(nombre, ',');
		int nombrePoints = compteOcurrences(nombre, '.');
		aff("nombreVirgules = " + nombreVirgules);
		aff("nombrePoints = " + nombrePoints);
		if (nombreVirgules == 0 && nombrePoints == 0) {
			if (isInteger(nombre)) {
				return Integer.parseInt(nombre);
			}
		} else {
			nombre = normaliserSeparateurs(nombre, nombreVirgules, nombrePoints);
			nombreVirgules = compteOcurrences(nombre, ',');
			nombrePoints = compteOcurrences(nombre, '.');
			aff("nombreVirgules = " + nombreVirgules);
			aff("nombrePoints = " + nombrePoints);
			aff("nombre = " + nombre);
			return convertirEnDouble(nombre, nombreVirgules, nombrePoints);
		}
		return -1;
	}

	/**
	 * Normalise les separateurs du nombre.
	 * @param nombre Nombre a normaliser.
	 * @param nombreVirgules Compte des virgules.
	 * @param nombrePoints Compte des points.
	 * @return Nombre normalise.
	 */
	private String normaliserSeparateurs(String nombre, int nombreVirgules, int nombrePoints) {
		if (nombreVirgules == 1 && nombrePoints > 1) {
			return verifieDoubleCorrecte(nombre, ',');
		}
		if (nombreVirgules > 1 && nombrePoints == 1) {
			return verifieDoubleCorrecte(nombre, '.');
		}
		return nombre;
	}

// ------------------------------------------------------------------------------
// Conversion en double
// ------------------------------------------------------------------------------

	/**
	 * Convertit la chaine normalisee en double.
	 * @param nombre Nombre normalise.
	 * @param nombreVirgules Compte des virgules.
	 * @param nombrePoints Compte des points.
	 * @return Valeur double ou -1 si invalide.
	 */
	private double convertirEnDouble(String nombre, int nombreVirgules, int nombrePoints) {
		if (nombreVirgules > 1 && nombrePoints > 1) {
			return -1;
		}
		if (nombreVirgules == 1 && nombrePoints == 0) {
			return Double.parseDouble(nombre.replace(',', '.'));
		}
		if (nombreVirgules == 0 && nombrePoints == 1) {
			return Double.parseDouble(nombre);
		}
		return -1;
	}

// ==============================================================================
// Fonctions principales
// ==============================================================================

// ------------------------------------------------------------------------------
// Extraction des symboles
// ------------------------------------------------------------------------------

	/**
	 * Extrait un nombre depuis une expression a partir d'un indice.
	 * Gere les virgules et points decimaux.
	 * @param expression Expression source.
	 * @param indiceDebut Indice de debut d'extraction.
	 * @return Nombre extrait sous forme de chaine.
	 */
	public String mangeNombre(String expression, int indiceDebut) {
		int longueurExpression = expression.length();
		boolean contientVirgules = false;
		boolean contientPoints = false;
		boolean estChiffre = true;
		String resultat = "";
		for (int index = indiceDebut; index < longueurExpression && estChiffre; index++) {
			char caractereActuel = expression.charAt(index);
			if (isCharNumber(caractereActuel)) {
				resultat += caractereActuel;
			} else if (caractereActuel == ',') {
				contientVirgules = true;
				resultat += caractereActuel;
			} else if (caractereActuel == '.') {
				contientPoints = true;
				resultat += caractereActuel;
			} else {
				estChiffre = false;
			}
		}
		aff(resultat);
		if (contientVirgules || contientPoints) {
			return "" + analyseNombreDouble(resultat);
		}
		return resultat;
	}

	/**
	 * Extrait un operateur depuis une expression.
	 * @param expression Expression source.
	 * @param indice Position de l'operateur.
	 * @return Caractere operateur ou -1 si hors limites.
	 */
	public char mangeOperateur(String expression, int indice) {
		int longueurExpression = expression.length();
		if (indice >= 0 && indice < longueurExpression) {
			aff(expression.charAt(indice));
			return expression.charAt(indice);
		}
		return (char) (-1);
	}

// ------------------------------------------------------------------------------
// Traitement des symboles
// ------------------------------------------------------------------------------

	/**
	 * Effectue une operation arithmetique entre deux operandes.
	 * @param chaineA Premier operande (chaine).
	 * @param chaineB Second operande (chaine).
	 * @param chaineSymbole Operateur (chaine).
	 * @return Resultat de l'operation ou -1 en cas d'erreur.
	 */
	public int traiteSymbolesInt(String chaineA, String chaineB, String chaineSymbole) {
		if (chaineSymbole == null || chaineSymbole.isEmpty()) {
			aff("Symbole null ou egal a " + '"' + '"' + ".");
			return -1;
		}
		if (!isInteger(chaineA) || !isInteger(chaineB)) {
			aff("chaineA (= " + chaineA + ") ou chaineB (= " + chaineB + ") ne sont pas des nombres.");
			return -1;
		}
		int operandeA = Integer.parseInt(chaineA);
		int operandeB = Integer.parseInt(chaineB);
		char symbole = chaineSymbole.charAt(0);
		if (!operateursAutorises.contains(symbole)) {
			aff("symbole (= " + symbole + ") n'est pas un operateur.");
			return -1;
		}
		return executerOperation(operandeA, operandeB, symbole);
	}

	/**
	 * Execute l'operation arithmetique.
	 * @param operandeA Premier operande.
	 * @param operandeB Second operande.
	 * @param symbole Operateur.
	 * @return Resultat de l'operation.
	 */
	private int executerOperation(int operandeA, int operandeB, char symbole) {
		if (symbole == '+') {
			return operandeA + operandeB;
		}
		if (symbole == '-') {
			return operandeA - operandeB;
		}
		if (symbole == '*') {
			return operandeA * operandeB;
		}
		if (symbole == '/') {
			return operandeA / operandeB;
		}
		return 0;
	}

// ------------------------------------------------------------------------------
// Analyse d'expression
// ------------------------------------------------------------------------------

	/**
	 * Analyse et evalue une expression mathematique sans parentheses.
	 * Utilise une pile pour gerer les operations.
	 * @param expression Expression a analyser.
	 * @return Resultat de l'expression ou -1 en cas d'erreur.
	 */
	public int analyseExpressionSansParentheses(String expression) {
		if (expression == null) {
			aff("L'expression est null");
			return -1;
		}
		if (expression.isEmpty()) {
			aff("L'expression est vide.");
			return -1;
		}
		int longueurExpression = expression.length();
		int compteur = 0;
		String elementTemporaire = "";
		for (int index = 0; index < longueurExpression; index++) {
			if (isCharNumber(expression.charAt(index))) {
				elementTemporaire = mangeNombre(expression, index);
				pileCalcul.push(elementTemporaire);
				while (index < longueurExpression && isCharNumber(expression.charAt(index))) {
					index++;
				}
			}
			if (compteur > 0) {
				calculPile(pileCalcul);
			}
			if (index >= longueurExpression) {
				break;
			}
			if (operateursAutorises.contains(expression.charAt(index))) {
				elementTemporaire = "" + mangeOperateur(expression, index);
				pileCalcul.push(elementTemporaire);
			}
			compteur++;
		}
		String resultat = popAvecErreur(pileCalcul);
		return convertStrInt(resultat);
	}