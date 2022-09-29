
	public String partieDecimale (double n0) {
		String n0tmp = ""+n0;
		String point=".";

		//n0tmp = "16575.76812"; //test pour ',' ou '.'

		if (n0tmp.indexOf(",")!= -1) point=",";
		if(n0tmp.indexOf(point)== -1) return "0";

		boolean aTrouveLePoint=false;
		String res="";
		int lenn0tmp=n0tmp.length();
		for (int i=0; i<lenn0tmp; i++) {
			if (aTrouveLePoint) res+=n0tmp.charAt(i);
			if (n0tmp.charAt(i)==point.charAt(0)) aTrouveLePoint=true;
		}

		//aff("res = "+res);

		return res;
	}