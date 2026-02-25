
// ==============================================================================
// Donnees
// ==============================================================================

	Character[] listeOperateursEntiers = {'+', '-', '*', '/'};
	Stack<String> pileCalculEntiers = new Stack<String>();
	ArrayList<Character> operateursAutorisesEntiers = new ArrayList<Character>();

// ==============================================================================
// Fonctions utilitaires
// ==============================================================================

	/**
	 * Initialise la liste des operateurs autorises pour les entiers.
	 * Copie les operateurs du tableau vers l'ArrayList.
	 */
	public void operateursEntiers() {
		for (Character caractere : listeOperateursEntiers) {
			operateursAutorisesEntiers.add(caractere);
		}
	}

// ------------------------------------------------------------------------------
// Gestion de la pile pour entiers
// ------------------------------------------------------------------------------

	/**
	 * Depile un element avec gestion d'erreur pour les entiers.
	 * @param pile Pile a depiler.
	 * @return Element depile ou chaine vide si pile vide.
	 */
	public String popAvecErreurEntiers(Stack<String> pile) {
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
	public void calculPileEntiers(Stack<String> pile) {
		String operandeA = popAvecErreurEntiers(pile);
		String symbole = popAvecErreurEntiers(pile);
		String operandeB = popAvecErreurEntiers(pile);
		int resultatTemporaire = traiteSymbolesIntEntiers(operandeB, operandeA, symbole);
		aff("resultatTemporaire = " + resultatTemporaire);
		pile.push("" + resultatTemporaire);
	}

// ==============================================================================
// Fonctions principales
// ==============================================================================

// ------------------------------------------------------------------------------
// Extraction des symboles pour entiers
// ------------------------------------------------------------------------------

	/**
	 * Extrait un nombre entier depuis une expression a partir d'un indice.
	 * @param expression Expression source.
	 * @param indiceDebut Indice de debut d'extraction.
	 * @return Nombre extrait sous forme de chaine.
	 */
	public String mangeNombreEntier(String expression, int indiceDebut) {
		int longueurExpression = expression.length();
		boolean estChiffre = true;
		String resultat = "";
		for (int index = indiceDebut; index < longueurExpression && estChiffre; index++) {
			if (isCharNumber(expression.charAt(index))) {
				resultat += expression.charAt(index);
			} else {
				estChiffre = false;
			}
		}
		aff(resultat);
		return resultat;
	}

	/**
	 * Extrait un operateur depuis une expression pour les entiers.
	 * @param expression Expression source.
	 * @param indice Position de l'operateur.
	 * @return Caractere operateur ou -1 si hors limites.
	 */
	public char mangeOperateurEntier(String expression, int indice) {
		int longueurExpression = expression.length();
		if (indice >= 0 && indice < longueurExpression) {
			aff(expression.charAt(indice));
			return expression.charAt(indice);
		}
		return (char) (-1);
	}

// ------------------------------------------------------------------------------
// Traitement des symboles pour entiers
// ------------------------------------------------------------------------------

	/**
	 * Effectue une operation arithmetique entre deux operandes entiers.
	 * @param chaineA Premier operande (chaine).
	 * @param chaineB Second operande (chaine).
	 * @param chaineSymbole Operateur (chaine).
	 * @return Resultat de l'operation ou -1 en cas d'erreur.
	 */
	public int traiteSymbolesIntEntiers(String chaineA, String chaineB, String chaineSymbole) {
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
		if (!operateursAutorisesEntiers.contains(symbole)) {
			aff("symbole (= " + symbole + ") n'est pas un operateur.");
			return -1;
		}
		return executerOperationEntiers(operandeA, operandeB, symbole);
	}

	/**
	 * Execute l'operation arithmetique pour les entiers.
	 * @param operandeA Premier operande.
	 * @param operandeB Second operande.
	 * @param symbole Operateur.
	 * @return Resultat de l'operation.
	 */
	private int executerOperationEntiers(int operandeA, int operandeB, char symbole) {
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
// Analyse d'expression pour entiers
// ------------------------------------------------------------------------------

	/**
	 * Analyse et evalue une expression mathematique sans parentheses (entiers).
	 * Utilise une pile pour gerer les operations.
	 * @param expression Expression a analyser.
	 * @return Resultat de l'expression ou -1 en cas d'erreur.
	 */
	public int analyseExpressionSansParenthesesEntiers(String expression) {
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
				elementTemporaire = mangeNombreEntier(expression, index);
				pileCalculEntiers.push(elementTemporaire);
				while (index < longueurExpression && isCharNumber(expression.charAt(index))) {
					index++;
				}
			}
			if (compteur > 0) {
				calculPileEntiers(pileCalculEntiers);
			}
			if (index >= longueurExpression) {
				break;
			}
			if (operateursAutorisesEntiers.contains(expression.charAt(index))) {
				elementTemporaire = "" + mangeOperateurEntier(expression, index);
				pileCalculEntiers.push(elementTemporaire);
			}
			compteur++;
		}
		String resultat = popAvecErreurEntiers(pileCalculEntiers);
		return convertStrInt(resultat);
	}
