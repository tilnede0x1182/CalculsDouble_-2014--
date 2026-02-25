
	public int abs (int n0) {
		if (n0<0) return -n0;
		else return n0;
	}

	public void abs () {
		this.estNegatif = false;
	}

	public CalculsDouble abs (CalculsDouble n0) {
		CalculsDouble n1 = new CalculsDouble(0);
		n1.ent = n0.ent; n1.dec = n0.dec; n1.representation_anglaise 
			= n0.representation_anglaise;
		n1.estNegatif = false;

		return n1;
	}

	public int partieEntiere (double n0) {
		return ((int)(n0));
	}

	public int partieEntiere () {
		if (this.ent.isEmpty()) return 0;
		else return Integer.parseInt(this.ent);
	}
