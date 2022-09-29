
	public int somme_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=0;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res+=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}

	public int produit_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=1;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res*=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}