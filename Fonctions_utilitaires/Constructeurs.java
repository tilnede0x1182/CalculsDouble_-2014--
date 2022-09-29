	public CalculsDouble () {
		ent="0";
		dec="";
	}

	public CalculsDouble (int n0) {
		ent = ""+(int)(n0);
		dec = "";

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (double n0) {
		ent = ""+(int)(n0);
		dec = partieDecimale(n0);

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (int n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		ent = ""+(int)(n0);
		dec = "0";

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}

	public CalculsDouble (double n0, boolean repr_anglaise) {
		representation_anglaise = repr_anglaise;

		ent = ""+(int)(n0);
		dec = partieDecimale(n0);

		if (ent.contains("-")) {
			estNegatif = true;
			ent = ent.replace("-", "");
		}
	}
