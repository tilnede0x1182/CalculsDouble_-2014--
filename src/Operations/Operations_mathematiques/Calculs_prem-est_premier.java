	public boolean estPremier (int n0) {
		for (int i=2; i<1+(int)(Math.sqrt((double)(n0))); i++) {
			if (n0%i==0) return false;
		}

		return true;
	}

	public void clacule_n_premiers (int n0) {
		int i, cmp=0;

		if (n0<1) return; // rien à afficher

		int [] res = new int[n0];

		i=2;
		while (cmp<n0) {
			if (estPremier(i)) {
				res[cmp] = i;
				cmp++;
			}

			i++;
		}

		affnn("p"+n0+" = {");
		for (i=0; i<res.length-1; i++) {
			affnn(res[i]+", ");
		}
		aff(res[i]+"};");
	}