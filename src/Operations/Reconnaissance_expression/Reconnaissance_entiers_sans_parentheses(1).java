
	Character [] operateurs_tmp = 
		{'+', '-', '*', '/'};
	Stack<String> st = new Stack<String>();
	ArrayList<Character> operateurs = new ArrayList<Character>();

	public void operateurs () {
		for (Character c1 : operateurs_tmp) {
			operateurs.add(c1);
		}
	}

	/**
		Renvoie -1 en cas d'erreur.
	**/
	public int analyse_expression_sans_parentheses (String expr) {
		if (expr==null) {
			aff("L'expression est null");
			return -1;
		}
		if (expr.isEmpty()) {
			aff("L'expression est vide.");
			return -1;
		}
		int lenexpr = expr.length();
		int cmp = 0;
		String tmp = "";

		for (int i=0; i<lenexpr; i++) {
			if (isCharNumber(expr.charAt(i))) {
				//affnn(expr.charAt(i));
				tmp = mange_nombre(expr, i);
				st.push(tmp);
				while (i<lenexpr 
				&& isCharNumber(expr.charAt(i))) i++;
			}
			if (cmp>0) calcul_pile(st);
			if (i>=lenexpr) break;
			if (operateurs.contains(expr.charAt(i))) {
				tmp = ""+mange_operateur(expr, i);
				st.push(tmp);
			}
			cmp++;
		}
		String res = pop_erreur(st);
		return convertStrInt(res);
	}

// ##################### Fonctions de traitement des symboles ######################### //

	public String pop_erreur (Stack<String> st) {
		if (!st.empty()) {
			return st.pop();
		}
		else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	public void calcul_pile (Stack<String> st) {
		String aS = pop_erreur(st);
		String symS = pop_erreur(st);
		String bS = pop_erreur(st);

		int res_tmp = traite_symbolesInt(bS, aS, symS);
		aff("res_tmp = "+res_tmp);
		st.push(""+res_tmp);
	}

	/**
		Retourne -1 en cas d'erreur.
	**/
	public int traite_symbolesInt (String aS, String bS, String symS) {
		if (symS==null || symS.isEmpty()) {
			aff("Symbole null ou egal a "+'"'+'"'+".");
			return -1;
		}
		if (!isInteger(aS) || !isInteger(bS)) {
			aff("aS (= "+aS+") ou bS (= "+bS+") ne sont pas des nombres.");
			return -1;
		}
		int a = Integer.parseInt(aS);
		int b = Integer.parseInt(bS);
		char sym = symS.charAt(0);
		if (!operateurs.contains(sym)) {
			aff("sym (= "+sym+") n'est pas un operateur.");
			return -1;
		}
		int res = 0;

		if (sym=='+') {
			res = a+b;
		}

		if (sym=='-') {
			res = a-b;
		}

		if (sym=='*') {
			res = a*b;
		}

		if (sym=='/') {
			res = a/b;
		}

		return res;
	}

// ########################### Fonction mange_symbole ################################# //

	public String mange_nombre (String expr, int indice_debut) {
		int lenexpr = expr.length();
		boolean isDigit = true;
		String res = "";

		for (int i=indice_debut; i<lenexpr && isDigit; i++) {
			if (isCharNumber(expr.charAt(i))) {
				res+=expr.charAt(i);
			}
			else isDigit = false;
		}
		aff(res);
		return res;
	}

	public char mange_operateur (String expr, int indice) {
		int lenexpr = expr.length();

		if (indice>=0 && indice<lenexpr) {
			aff(expr.charAt(indice));
			return expr.charAt(indice);
		}
		else return (char)(-1);
	}
	
// ##################### Main ####################### //

	/**public static void main (String [] args) {
		CalculsDouble cb = new CalculsDouble();
		String expr1
		//"103*8/09+3-8"
		 = "";
		int res_verififcation = 0;
		aff("expr1 = "+expr1);
		cb.operateurs();
		int res = cb.analyse_expression_sans_parentheses(expr1);
		aff("res = "+res);
		aff("Verification : "+res_verififcation);
	}**/