	public CalculsDouble additionneNombres (CalculsDouble a, CalculsDouble b){
		int recul = 0;
		int chiffres_avant_la_virgule = Math.min(compteOcurrences(a.dec, 
			'0'), compteOcurrences(b.dec, '0'));
		CalculsDouble c = new CalculsDouble ();

		//aff("a = "+a+"\nb = "+b+"\n");

		CalculsDouble [] tmpCD = egaliseNombres(a, b); 
		a = tmpCD[0]; b = tmpCD[1];

		/*************** Intégration du signe "-" ************************/
		if (a.estNegatif && b.estNegatif) {
			c.estNegatif = true;
		}
		else if (a.estNegatif || b.estNegatif) {
			//aff("a.estNegatif || b.estNegatif");

			int cmpnbr0 = compareNombres_abs (a, b);

			if (cmpnbr0==0) { 
				c.ent="0"; c.dec=""; c.estNegatif=false;
			}
			if (cmpnbr0==2) {
				CalculsDouble d = a;
				a = b; // on fait en sorte que a>b
				b = d; // (échange de a et b sinon)
			}

			if (!a.estNegatif && b.estNegatif) {
				a.estNegatif=false; b.estNegatif=false;

				return (soustraitNombres(a, b));
			}
			if (a.estNegatif && !b.estNegatif) {
				a.estNegatif=false; b.estNegatif=false;

				c = soustraitNombres(a, b);
				c.estNegatif = true;

				return c;
			}
		}
		/*****************************************************************/


		c.ent = ""+(Integer.parseInt(a.ent)+Integer.parseInt(b.ent));
		if (!a.dec.isEmpty() && !b.dec.isEmpty()) c.dec = ""+
			(Integer.parseInt(a.dec)+Integer.parseInt(b.dec));
		else c.dec="";

		if (!a.dec.isEmpty() && !b.dec.isEmpty()) {
			if (Integer.parseInt(""+a.dec.charAt(0))+
					Integer.parseInt(""+b.dec.charAt(0))>9) {
				c.ent = ""+(Integer.parseInt(c.ent)+1);
				c.dec = c.dec.substring(1, c.dec.length());
			}
		}

		if (c.dec.contains("-")) {
			c.estNegatif = true;
			c.dec = c.dec.replace("-", "");
		}

		c.ent = c.ent.replace("-", ""); // au cas où

		if (!c.dec.isEmpty()) {
			if (a.dec.charAt(0)=='0' || b.dec.charAt(0)=='0') 
				recul = 1;
	
			for (int i = 0; i<chiffres_avant_la_virgule+recul-2; i++){
				c.dec = "0"+c.dec;
			}
		}

		return c;
	}
