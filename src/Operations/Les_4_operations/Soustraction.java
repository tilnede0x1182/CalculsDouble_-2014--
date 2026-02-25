	public CalculsDouble soustraitNombres (CalculsDouble a, CalculsDouble b){
		int recul = 0;
		int chiffres_avant_la_virgule = Math.min(compteOcurrences(a.dec, 
			'0'), compteOcurrences(b.dec, '0'));
		CalculsDouble c = new CalculsDouble ();
		CalculsDouble [] tmpCD = egaliseNombres(a, b); a = tmpCD[0]; 
			b = tmpCD[1];

		/******************************** Intégration du signe "-" **/
		if (a.estNegatif || b.estNegatif) {
			if (a.estNegatif) a.ent = "-"+a.ent;
			if (b.estNegatif) b.ent = "-"+b.ent;
		}
		/************************************************************/

		c.ent = ""+(Integer.parseInt(a.ent)-Integer.parseInt(b.ent));
		if (!a.dec.isEmpty() && !b.dec.isEmpty()) 
			c.dec = ""+(Integer.parseInt(a.dec)
				-Integer.parseInt(b.dec));
		else {
			c.dec="";
			if (c.ent.contains("-") || c.dec.contains("-")) {
				c.estNegatif=true;
				c.ent = c.ent.replace("-", "");
				c.dec = c.dec.replace("-", "");
			}
			return c;
		}

		if (c.dec.contains("-")) {
			c.estNegatif = true;
			c.dec = c.dec.replace("-", "");
		}

		c.ent = c.ent.replace("-", ""); // au cas où

		if (a.dec.charAt(0)=='0' || b.dec.charAt(0)=='0') recul = 1;

		for (int i = 0; i<chiffres_avant_la_virgule+recul-2; i++) {
			c.dec = "0"+c.dec;
		}

		return c;
	}
