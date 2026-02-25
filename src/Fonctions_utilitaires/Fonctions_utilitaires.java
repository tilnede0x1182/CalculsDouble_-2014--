
	/**
		Shows if a String is an Integer 
		using function Integer.parseInt(String) of Java.
	**/
	public boolean isInteger (String n0) {
		try {
			int n1 = Integer.parseInt(n0);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
		Convertit un String en entier (gestion des exceptions).
	**/
	public int convertStrInt (String n0) {
		if (n0==null) {
			aff("n0 est null");
			return -1;
		}
		if (n0.isEmpty()) {
			aff("n0 est egal a "+'"'+'"'+".");
			return -1;
		}
		try {
			return Integer.parseInt(n0);
		}
		catch(NumberFormatException e) {
			aff("Impossible de convertir ");
			return -1;
		}
	}

	/**
		Vérifie si un char est un chiffre 
		ou non.
	**/
	public boolean isCharNumber (char n0) {
		return (n0>='0' && n0<='9');
	}

	public boolean isInteger_deep (String n0) {
		if (n0.isEmpty()) return false;
		int n0len = n0.length();

		for (int i=0; i<n0len; i++) {
			if (!isCharNumber(n0.charAt(i))) return false;
		}

		return true;
	}

	/**
		Regarde si CalculsDouble n0 est un nombre.
		Renvoie true si n0.ent est un nombre
		et que n0.dec est vide ou un nombre aussi.
	**/
	public boolean isInteger (CalculsDouble n0) {
		boolean res = false;
		int i=0;
		String n0ent = n0.ent;
		String n0dec = n0.dec;

		if (!n0ent.isEmpty()) {
			res = isInteger(n0ent);
		}
		if (!n0dec.isEmpty()) {
			res = isInteger(n0dec);
		}

		return res;
	}

	/**
		Renvoie true si n0=0.0
		ou 0.
	**/
	public boolean estNul (CalculsDouble n0) {
		int i=0;
		int n0entlen = n0.ent.length();
		int n0declen = 0;

		if (!isInteger(n0)) return false;

		if (!n0.dec.isEmpty())
			n0declen = n0.dec.length();

		for (i=0; i<n0entlen; i++) {
			if (n0.ent.charAt(i)!='0')
				return false;
		}

		for (i=0; i<n0declen; i++) {
			if (n0.dec.charAt(i)!='0')
				return false;
		}

		return true;
	}

	public int compteOcurrences (String test, char cc) {
		if (test==null) return 0;
		if (test.isEmpty()) return 0;
		// Gestion des erreurs

		int res = 0, lentest = test.length();
		for (int i=0; i<lentest; i++) {
			if (test.charAt(i)==cc) res++;
		}
		return res;
	}
