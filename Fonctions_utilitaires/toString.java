	public String toString ()  {
		String signeMoins="";
		String affent = ent;
		String dec = chiffresSignificatifs(this.dec, -1);
		char virgule = ',';
		if (representation_anglaise) virgule='.';
		String affDec=virgule+dec;

		if (affent.isEmpty()) affent = "0";

		if (!dec.isEmpty() && Integer.parseInt(dec)==0 || dec.isEmpty()) affDec="";

		if (estNegatif) signeMoins = "-";

		return (signeMoins+affent+affDec);
	}