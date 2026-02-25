	/**
		Enlève les zéros en début de string dans la partie entiere (String ent).
	**/
	public String epureZerosEnt (String ent) {
		boolean plus_de_zeros = false;
		int  res = 0, lenent = ent.length();

		/**
		**	Parcours le str pour savoir s'il n'ya a plus de zeros (boolean) 
		**	et dès qu'on a fini, enlève les zeros en trop.
		**/
		res = lenent;
		int i=0;
		while (i<lenent && ent.charAt(i)=='0') i++;
		res = i;

		if (i==lenent) return "0";

		return (ent.substring(res, lenent));
	}

	/**
		-1 : pas de chiffres significatifs (on fait comme en maths : 
		le moins de chiffres possible). n : n chiffres aux minimum.
	**/
	public String envleveZerosInutils (String dec) {
		boolean plus_de_zeros = false;
		int  res = 0, lendec = dec.length();

		/**
		**	Parcours le str pour savoir s'il n'ya a plus de zeros (boolean) 
		**	et dès qu'on a fini, enlève les zeros en trop.
		**/
		res = lendec;
		for (int i=0; i<lendec; i++) {
			if (dec.charAt(i)=='0') {
				plus_de_zeros = true;
				res = i;
			}
			else {
				plus_de_zeros = false;
				res = lendec;
			}
		}

		return (dec.substring(0, res));
	}
