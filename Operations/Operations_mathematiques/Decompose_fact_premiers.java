
	public int [] decompose_en_facteurs_premiers (CalculsDouble n0) {
		if (!n0.dec.isEmpty()) aff("n0.dec!="+'"'+'"'+" : on "
					+"prend la troncature entière du nombre.");
		if (n0.ent.isEmpty()) return null; 
		// si n0.ent="", il n'y a rien à décomposer.
		String restmp="";
		char tmpc=0;

		int i=0, tmp1 = Integer.parseInt(n0.ent), tmp1len = (""+tmp1).length();

		aff("n = "+tmp1);

		while (! estPremier(tmp1)) {
			tmp1len = (""+tmp1).length();
			tmpc = (""+tmp1).charAt(tmp1len-1);
			if (tmpc==0 || tmpc==5) {
				restmp+="5:";
				tmp1 = tmp1/5;
			}
			else if (tmpc==2 || tmpc==4 || tmpc==8) {
				restmp+="2:";
				tmp1 = tmp1/2;
			}
			else if (tmp1%3==0) {
				restmp+="3:";
				tmp1 = tmp1/3;
			}
			else {
				for (i=2; i<tmp1 && tmp1%i!=0; i++) {}
				restmp+=i+":";
				tmp1 = tmp1/i;
			}
			if (tmp1==0) tmp1=1;
			// erreur de conversion après une division entière.

			aff("n = "+tmp1);
		}

		restmp+=""+tmp1;
		String [] res0 = restmp.split(":");
		int cmp2=0;
		for (i=0; i<res0.length; i++) {
			if (!res0[i].isEmpty()) cmp2++;
		}
		int [] res = new int[cmp2];
		cmp2=0;
		for (i=0; i<res0.length; i++) {
			if (!res0[i].isEmpty()) {
				res[cmp2] = Integer.parseInt(res0[i]);
				cmp2++;
			}
		}

		aff("restmp = "+restmp);
		int ver=1;
		for (i=0; i<res.length; i++) ver*=res[i];
		aff("Verification : n = "+ver);

		return res;
	}



