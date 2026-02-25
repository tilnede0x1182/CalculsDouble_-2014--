	/**
		0 : identiques
		1 : a>b
		2 : b>a

		-1 : erreur
	**/
	public int compareNombres (CalculsDouble a, CalculsDouble b) {
		int tmp = -1;
		if (a.estNegatif && !b.estNegatif) return 2;
		if (!a.estNegatif && b.estNegatif) return 1;

		if (a.estNegatif && b.estNegatif) {
			tmp = compareNombres_abs(a, b);

			if (tmp==0) return 0;
			if (tmp==1) return 2;
			if (tmp==2) return 1;
		}

		if (!a.estNegatif && !b.estNegatif)
			return compareNombres_abs (a, b);

		return -1;
	}

	/**
		Compare nombre avec valeur la absolue de a et de b : 

		0 : identiques
		1 : a>b
		2 : b>a
	**/
	public int compareNombres_abs (CalculsDouble a, CalculsDouble b) {
		int res=0;

		if (a.ent.length()>b.ent.length()) return 1;
		if (a.ent.length()<b.ent.length()) return 2;

		// donc a.ent.length() == b.ent.length()
		int alen = a.ent.length();

		for (int i=0;  i<alen;  i++) {
			if (Integer.parseInt(""+a.ent.charAt(i))>Integer.parseInt(""+b.ent.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.ent.charAt(i))<Integer.parseInt(""+b.ent.charAt(i)))
				return 2;
		}

		// donc les parties entières sont identiques
		int xlen = Math.min(a.dec.length(), b.dec.length());
		for (int i=0;  i<xlen;  i++) {
			if (Integer.parseInt(""+a.dec.charAt(i))>Integer.parseInt(""+b.dec.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.dec.charAt(i))<Integer.parseInt(""+b.dec.charAt(i)))
				return 2;
		}

		if (a.dec.length()!=b.dec.length() && Math.max(a.dec.length(), b.dec.length())>xlen) {
			if (a.dec.length()>xlen)
				if (Integer.parseInt(""+a.dec.charAt(xlen))>0) return 1;
			if (b.dec.length()>xlen)
				if (Integer.parseInt(""+b.dec.charAt(xlen))>0) return 2;
		}

		return 0;
	}