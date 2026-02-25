	/**
		Copie un CalculsDouble dans un autre.
	**/
	public CalculsDouble copie (CalculsDouble a) {
		CalculsDouble res = new CalculsDouble(0);

		res.ent = a.ent;
		res.dec = a.dec;
		res.estNegatif = a.estNegatif;
		res.representation_anglaise = a.representation_anglaise;

		return res;
	}

	public boolean equals (CalculsDouble a) {
		return (compareNombres(this, a)==0);
	}