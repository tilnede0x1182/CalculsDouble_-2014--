	public String chiffresSignificatifs (String dec, int nn) {
		int lendec = dec.length();
		int nbrZeros = nn-lendec;

		/**
			enlèves les zéros inutils (sans les compter au préalable 
			(plus simple à coder))
		**/
		dec = envleveZerosInutils (dec);

		if (nn == -1) return dec; 
		// cas de base (on enlève 
		// le plus de zéros possibles, comme en maths).

		/* Ajoute des zeros si besoin */
		for (int i=0; i<nbrZeros; i++) {
			dec+='0';
		}

		return dec;
	}