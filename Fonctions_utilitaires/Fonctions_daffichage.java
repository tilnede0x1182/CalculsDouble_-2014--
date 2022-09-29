
	public static void affnn (Object s0) {
		System.out.print(""+s0);
	}

	public static void aff (Object s0) {
		System.out.println(""+s0);
	}

	public void affTab(int [] n0) {
		for (int i=0; i<n0.length; i++) {
			aff("t["+i+"] = "+n0[i]);
		}
	}

	public void affTab(int [] n0, String nom) {
		for (int i=0; i<n0.length; i++) {
			aff(nom+"["+i+"] = "+n0[i]);
		}
	}

	public void afftab2dim(String [] t1, String nom) {
		int i;

		//aff(nom+" : ");

		for (i=0; i<t1.length; i++) {
			aff(nom+"["+i+"] = "+t1[i]);
		}
	}

	public int [] agrandieTab (int [] t0) {
		int i;
		int [] t1 = new int[t0.length*2+1];

		for (i = 0; i<t0.length; i++) {
			t1[i] = t0[i];
		}

		return t1;
	}
