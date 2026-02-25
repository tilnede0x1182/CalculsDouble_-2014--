
	public int arrondiEntier (CalculsDouble n0) {
		String res = arrondi(n0).ent;
		if (res.isEmpty()) return 0;
		else return Integer.parseInt(res);
	}

	public String arrondiString (CalculsDouble n1) {
		return arrondiString(n1, 0);
	}

	public String arrondiString (CalculsDouble n1, int r0) {
		return (""+arrondi(n1, r0));
	}

	public CalculsDouble arrondi (CalculsDouble n1) {
		return arrondi(n1, 0);
	}

	/**
		fonction arrondi (CalculsDouble n0, int r0) :

		r0 = 0 : aucune décimale
		r0 = 1 : 1 décimale
		.
		.
		.
		r0 = n : n décimale(s)

	**/
	public CalculsDouble arrondi (CalculsDouble n1, int r0) {
		String tmp0 = "";
		CalculsDouble n0 = copie(n1);
		boolean isok = true;
		int tmp = r0;
		if (r0<1) { // r0<1 => aucune décimale.
			if (n0.dec.isEmpty()) return n0;
			else {
				if (Integer.parseInt(""+n0.dec.charAt(0))>4)
					n0 = this.additionneNombres(n0, new CalculsDouble(1)); //n0.ent++;
				n0.dec = "";
			}
			return n0;
		}
		if (n0.dec.isEmpty()) {
			n0.estNegatif=true;
			return n0;
		}
		else {
			if (n0.dec.length()>r0) {
				if (Integer.parseInt(""+n0.dec.charAt(r0))>4) {
					tmp = r0;
					isok = true;
					while (tmp>0 && Integer.parseInt(""+n0.dec.charAt(tmp))>4 && isok) {
						tmp0 = ""+(Integer.parseInt(""+n0.dec.charAt(tmp-1))+1);
						if (tmp0.length()>1)
							n0.dec = ""+n0.dec.substring(0, tmp);
						else {
							n0.dec = ""+n0.dec.substring(0, tmp-1)+tmp0;
							isok = false;
						}
						
						tmp--;
					}

					if (tmp == 0 && Integer.parseInt(""+n0.dec.charAt(0))>4) {
						n0.dec="";
						n0.ent = ""+(Integer.parseInt(n0.ent)+1);
					}

					return n0;
				}
				else {
					n0.dec = n0.dec.substring(0, r0);
					return n0;
				}
			}
			else return n0;
		}
	}