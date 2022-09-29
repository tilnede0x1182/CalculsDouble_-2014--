	public CalculsDouble [] division_boucle (CalculsDouble a, 
			CalculsDouble b) {
		return division_boucle(a, b, true, 8);
	}
	public CalculsDouble [] division_boucle (CalculsDouble a, 
			CalculsDouble b, int chiffres_significatifs) {
		return division_boucle(a, b, true, chiffres_significatifs);
	}
	public CalculsDouble [] division_boucle (CalculsDouble a, 
			CalculsDouble b, boolean tronquee, 
			int chiffres_significatifs) {
		if (!tronquee) chiffres_significatifs++;
		if (chiffres_significatifs>9) {
			affnn("Erreur : impossible de faire ");
			aff("plus de 8 chiffres significatifs.");
			chiffres_significatifs=9;
		}
		int cs = chiffres_significatifs;
		CalculsDouble r = new CalculsDouble(0); 
			r.ent = a.ent; r.dec = a.dec;
		CalculsDouble q = new CalculsDouble(0);

		CalculsDouble tmp2 = new CalculsDouble(0);
		CalculsDouble [] res = {q, r};

		/** ################# Cas particuliers ####################### **/
		if (b.equals(new CalculsDouble(0))) {
			// division par 0 (Erreur)
			aff("Impossible de diviser par 0.");
			aff("Impossible dividing by 0.");
			return null;
		}
		if (a.equals(new CalculsDouble(0))) {
			q = new CalculsDouble(0);
			r = new CalculsDouble(0);
			res[0] = q; res[1] = r;
			return res;
		}
		if (b.equals(new CalculsDouble(1))) {
			q.ent = a.ent; q.dec = a.dec;
			r = new CalculsDouble(0);
			res[0] = q; res[1] = r;
			return res;			
		} // a/1
		if (a.equals(b)) {
			q = new CalculsDouble(1);
			r = new CalculsDouble(0);
			res[0] = q; res[1] = r;
			return res;
		} // a/a
		/**############################################################**/

		r.ent=r.ent+r.dec;
		CalculsDouble tmp = new CalculsDouble(0);
		tmp.ent=b.ent+b.dec;

		String tmp3="";
		//while (r >= b) {
		boolean apres_la_virgule = false;
		boolean apres_la_virgule_tmp = false;
		do {
			//########### Instruction r = r - b #####################
			apres_la_virgule = (!(compareNombres(r, tmp)==0 
				|| compareNombres(r, tmp)==1));
			if (apres_la_virgule) apres_la_virgule_tmp = true;
			if (cs>0 && apres_la_virgule) {
				r = mult(r, new CalculsDouble(10));
				//r = r*10
				cs--;
			}
			r = soustraitNombres(r, tmp); //r = r - b;

			//########### Instruction q = q + 1 #####################
			if (!apres_la_virgule_tmp) {tmp2.ent="1"; tmp2.dec="";}
			else {
				tmp2.ent="0";
				tmp3="";
				for (int tmp4=cs+1; tmp4<chiffres_significatifs; 
						tmp4++) {
					tmp3+="0";
				}
				tmp2.dec=tmp3+"1";
				//aff("tmp2 = "+tmp2);
			}
			q = additionneNombres(q, tmp2);

			//########### Instruction r = r - b (On est obligé #######
			//##################de le mettre deux fois) ##############
			apres_la_virgule = (!(compareNombres(r, tmp)==0 
				|| compareNombres(r, tmp)==1));
			if (apres_la_virgule) apres_la_virgule_tmp = true;
			if (cs>0 && apres_la_virgule) {
				r = mult(r, new CalculsDouble(10));
				//r = r*10
				cs--;
			}
		} while (compareNombres(r, tmp)==0 
			|| compareNombres(r, tmp)==1);

		//### Signe du quotient ##########################################
		q.estNegatif = ((a.estNegatif || b.estNegatif) && !(a.estNegatif 
			&& b.estNegatif));
		//################################################################

		res[0] = q; res[1] = r;
		if (!tronquee) res[0] = arrondi(res[0], 
			chiffres_significatifs-1);
		return res;		
	}

	/**public static void main (String [] args) {
		int cs = 10;

		CalculsDouble a = new CalculsDouble(7);
		CalculsDouble b = new CalculsDouble(10);

		aff(""+a.division_boucle(a, b, false, cs)[0]);

		//CalculsDouble a = new CalculsDouble(0.89);
		//CalculsDouble b = new CalculsDouble(9);
		//
		//aff(""+a.arrondi(a, 2));
	}**/
