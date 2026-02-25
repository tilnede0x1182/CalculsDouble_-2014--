
	public CalculsDouble [] egaliseNombres  (CalculsDouble a, CalculsDouble b) {
		if (a.dec.length()!=b.dec.length()) {
			if (Math.max(a.dec.length(), b.dec.length())==a.dec.length()) {
				String tmpa="";
				int tmplena=a.dec.length()-b.dec.length();
				for (int i=0; i<tmplena; i++)
					tmpa+="0";
				b.dec+=tmpa;
			}

			if (Math.max(a.dec.length(), b.dec.length())==b.dec.length()) {
				String tmpb="";
				int tmplenb=b.dec.length()-a.dec.length();
				for (int i=0; i<tmplenb; i++)
					tmpb+="0";
				a.dec+=tmpb;
			}
		}

		CalculsDouble [] res = new CalculsDouble [2]; res[0] = a; res[1] = b;

		return res;
	}