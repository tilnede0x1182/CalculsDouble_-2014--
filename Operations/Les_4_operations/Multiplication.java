	public CalculsDouble mult (CalculsDouble a, CalculsDouble b) {
		boolean estNegatif = ((a.estNegatif || b.estNegatif) 
			&& (!(a.estNegatif && b.estNegatif)));
		int retenue = 0;
		int nbr_de_chiffres = 0;
		String tmp0 = "";
		String [] matricetmp;

		if (!a.dec.isEmpty()) nbr_de_chiffres+=a.dec.length();
		if (!b.dec.isEmpty()) nbr_de_chiffres+=b.dec.length();
		// On ajoute le nombre de chiffres avant la virgule si 
		// la virgule existe.

		//aff("nbr_de_chiffres = "+nbr_de_chiffres);

		if (b.ent.length()+b.dec.length()>a.ent.length()+a.dec.length()) {
			CalculsDouble d = a;
			a = b; // On fait en sorte que len(a)>len(b)
			b = d; // On échange a et b sinon.
		}

		String aent = ""+a.ent+a.dec, bent = ""+b.ent+b.dec;
		int aentlen = aent.length(), bentlen = bent.length();

		matricetmp = new String [bentlen];
		for (int i=0; i<matricetmp.length; i++) matricetmp[i] = "";

		//aff("a = "+a+"\nb = "+b+"\n");
		//aff("aent = "+aent+"\nbent = "+bent+"\n");

		String restmp = "";
		retenue = 0;
		for (int i = bentlen-1; i>-1; i--) {
			for (int j = aentlen-1; j>-1; j--) {
				//aff("retenue : "+retenue);

				restmp = ""+(retenue+Integer.parseInt(""+
			bent.charAt(i))*Integer.parseInt(""+aent.charAt(j)));

				//aff("restmp = "+restmp);

				if (restmp.length()>1 && j>0) {
					tmp0 = restmp.substring(0, 
						restmp.length()-1);
					restmp = restmp.substring(restmp.length()-
						1, restmp.length());
					retenue = 
				((!tmp0.isEmpty())?Integer.parseInt(tmp0):0);
				}
				else retenue = 0;

				matricetmp[bentlen-i-1] = restmp + 
					matricetmp[bentlen-i-1];
			}
			for (int k=0; k<bentlen-i-1; k++) 
				matricetmp[bentlen-i-1]+="0";
		}

		//afftab2dim(matricetmp, "matricetmp");

		CalculsDouble res = new CalculsDouble(0);
		CalculsDouble tmp = new CalculsDouble(0);
		for (int i = 0; i<bentlen; i++) {
			tmp.ent = matricetmp[i];
			res.ent = additionneNombres(tmp, res).ent;
		}

		if (!a.dec.isEmpty() || !b.dec.isEmpty()) {
			res.dec = res.ent.substring(res.ent.length()-
				nbr_de_chiffres, res.ent.length());
			res.ent = res.ent.substring(0, res.ent.length()-
				nbr_de_chiffres);
		}

		res.estNegatif = estNegatif;

		return res;
	}
