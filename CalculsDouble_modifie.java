import java.util.Stack;
import java.util.HashSet;
import java.util.ArrayList;

class CalculsDouble {
	boolean estNegatif=false;
	boolean representation_anglaise=false;

	String ent;
	String dec;

// ############################### Constructeurs ####################################### //

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


// ################################# Opérations ######################################## //

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

	public CalculsDouble mult (CalculsDouble a, CalculsDouble b) {
		boolean estNegatif = ((a.estNegatif || b.estNegatif) 
			&& (!(a.estNegatif && b.estNegatif)));
		int retenue = 0;
		int nbr_de_chiffres = 0;
		String tmp0 = "";
		String [] matricetmp;

		if (!a.dec.isEmpty()) nbr_de_chiffres+=a.dec.length();
		if (!b.dec.isEmpty()) nbr_de_chiffres+=b.dec.length();
		// On ajoute le nombre de chiffres avant la virgule si 
		// la virgule existe.

		//aff("nbr_de_chiffres = "+nbr_de_chiffres);

		if (b.ent.length()+b.dec.length()>a.ent.length()+a.dec.length()) {
			CalculsDouble d = a;
			a = b; // On fait en sorte que len(a)>len(b)
			b = d; // On échange a et b sinon.
		}

		String aent = ""+a.ent+a.dec, bent = ""+b.ent+b.dec;
		int aentlen = aent.length(), bentlen = bent.length();

		matricetmp = new String [bentlen];
		for (int i=0; i<matricetmp.length; i++) matricetmp[i] = "";

		//aff("a = "+a+"\nb = "+b+"\n");
		//aff("aent = "+aent+"\nbent = "+bent+"\n");

		String restmp = "";
		retenue = 0;
		for (int i = bentlen-1; i>-1; i--) {
			for (int j = aentlen-1; j>-1; j--) {
				//aff("retenue : "+retenue);

				restmp = ""+(retenue+Integer.parseInt(""+
			bent.charAt(i))*Integer.parseInt(""+aent.charAt(j)));

				//aff("restmp = "+restmp);

				if (restmp.length()>1 && j>0) {
					tmp0 = restmp.substring(0, 
						restmp.length()-1);
					restmp = restmp.substring(restmp.length()-
						1, restmp.length());
					retenue = 
				((!tmp0.isEmpty())?Integer.parseInt(tmp0):0);
				}
				else retenue = 0;

				matricetmp[bentlen-i-1] = restmp + 
					matricetmp[bentlen-i-1];
			}
			for (int k=0; k<bentlen-i-1; k++) 
				matricetmp[bentlen-i-1]+="0";
		}

		//afftab2dim(matricetmp, "matricetmp");

		CalculsDouble res = new CalculsDouble(0);
		CalculsDouble tmp = new CalculsDouble(0);
		for (int i = 0; i<bentlen; i++) {
			tmp.ent = matricetmp[i];
			res.ent = additionneNombres(tmp, res).ent;
		}

		if (!a.dec.isEmpty() || !b.dec.isEmpty()) {
			res.dec = res.ent.substring(res.ent.length()-
				nbr_de_chiffres, res.ent.length());
			res.ent = res.ent.substring(0, res.ent.length()-
				nbr_de_chiffres);
		}

		res.estNegatif = estNegatif;

		return res;
	}

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


// ########################## Fonctions mathématiques ################################## //

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

	public String partieDecimale (double n0) {
		String n0tmp = ""+n0;
		String point=".";

		//n0tmp = "16575.76812"; //test pour ',' ou '.'

		if (n0tmp.indexOf(",")!= -1) point=",";
		if(n0tmp.indexOf(point)== -1) return "0";

		boolean aTrouveLePoint=false;
		String res="";
		int lenn0tmp=n0tmp.length();
		for (int i=0; i<lenn0tmp; i++) {
			if (aTrouveLePoint) res+=n0tmp.charAt(i);
			if (n0tmp.charAt(i)==point.charAt(0)) aTrouveLePoint=true;
		}

		//aff("res = "+res);

		return res;
	}

	public CalculsDouble [] egaliseNombres  (CalculsDouble a, CalculsDouble b) {
		if (a.dec.length()!=b.dec.length()) {
			if (Math.max(a.dec.length(), b.dec.length())==a.dec.length()) {
				String tmpa="";
				int tmplena=a.dec.length()-b.dec.length();
				for (int i=0; i<tmplena; i++)
					tmpa+="0";
				b.dec+=tmpa;
			}

			if (Math.max(a.dec.length(), b.dec.length())==b.dec.length()) {
				String tmpb="";
				int tmplenb=b.dec.length()-a.dec.length();
				for (int i=0; i<tmplenb; i++)
					tmpb+="0";
				a.dec+=tmpb;
			}
		}

		CalculsDouble [] res = new CalculsDouble [2]; res[0] = a; res[1] = b;

		return res;
	}
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
	/**
		0 : identiques
		1 : a>b
		2 : b>a

		-1 : erreur
	**/
	public int compareNombres (CalculsDouble a, CalculsDouble b) {
		int tmp = -1;
		if (a.estNegatif && !b.estNegatif) return 2;
		if (!a.estNegatif && b.estNegatif) return 1;

		if (a.estNegatif && b.estNegatif) {
			tmp = compareNombres_abs(a, b);

			if (tmp==0) return 0;
			if (tmp==1) return 2;
			if (tmp==2) return 1;
		}

		if (!a.estNegatif && !b.estNegatif)
			return compareNombres_abs (a, b);

		return -1;
	}

	/**
		Compare nombre avec valeur la absolue de a et de b : 

		0 : identiques
		1 : a>b
		2 : b>a
	**/
	public int compareNombres_abs (CalculsDouble a, CalculsDouble b) {
		int res=0;

		if (a.ent.length()>b.ent.length()) return 1;
		if (a.ent.length()<b.ent.length()) return 2;

		// donc a.ent.length() == b.ent.length()
		int alen = a.ent.length();

		for (int i=0;  i<alen;  i++) {
			if (Integer.parseInt(""+a.ent.charAt(i))>Integer.parseInt(""+b.ent.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.ent.charAt(i))<Integer.parseInt(""+b.ent.charAt(i)))
				return 2;
		}

		// donc les parties entières sont identiques
		int xlen = Math.min(a.dec.length(), b.dec.length());
		for (int i=0;  i<xlen;  i++) {
			if (Integer.parseInt(""+a.dec.charAt(i))>Integer.parseInt(""+b.dec.charAt(i)))
				return 1;
			if (Integer.parseInt(""+a.dec.charAt(i))<Integer.parseInt(""+b.dec.charAt(i)))
				return 2;
		}

		if (a.dec.length()!=b.dec.length() && Math.max(a.dec.length(), b.dec.length())>xlen) {
			if (a.dec.length()>xlen)
				if (Integer.parseInt(""+a.dec.charAt(xlen))>0) return 1;
			if (b.dec.length()>xlen)
				if (Integer.parseInt(""+b.dec.charAt(xlen))>0) return 2;
		}

		return 0;
	}

	public int [] decompose_en_facteurs_premiers (CalculsDouble n0) {
		if (!n0.dec.isEmpty()) aff("n0.dec!="+'"'+'"'+" : on "
					+"prend la troncature entière du nombre.");
		if (n0.ent.isEmpty()) return null; 
		// si n0.ent="", il n'y a rien à décomposer.
		String restmp="";
		char tmpc=0;

		int i=0, tmp1 = Integer.parseInt(n0.ent), tmp1len = (""+tmp1).length();

		aff("n = "+tmp1);

		while (! estPremier(tmp1)) {
			tmp1len = (""+tmp1).length();
			tmpc = (""+tmp1).charAt(tmp1len-1);
			if (tmpc==0 || tmpc==5) {
				restmp+="5:";
				tmp1 = tmp1/5;
			}
			else if (tmpc==2 || tmpc==4 || tmpc==8) {
				restmp+="2:";
				tmp1 = tmp1/2;
			}
			else if (tmp1%3==0) {
				restmp+="3:";
				tmp1 = tmp1/3;
			}
			else {
				for (i=2; i<tmp1 && tmp1%i!=0; i++) {}
				restmp+=i+":";
				tmp1 = tmp1/i;
			}
			if (tmp1==0) tmp1=1;
			// erreur de conversion après une division entière.

			aff("n = "+tmp1);
		}

		restmp+=""+tmp1;
		String [] res0 = restmp.split(":");
		int cmp2=0;
		for (i=0; i<res0.length; i++) {
			if (!res0[i].isEmpty()) cmp2++;
		}
		int [] res = new int[cmp2];
		cmp2=0;
		for (i=0; i<res0.length; i++) {
			if (!res0[i].isEmpty()) {
				res[cmp2] = Integer.parseInt(res0[i]);
				cmp2++;
			}
		}

		aff("restmp = "+restmp);
		int ver=1;
		for (i=0; i<res.length; i++) ver*=res[i];
		aff("Verification : n = "+ver);

		return res;
	}





	public int somme_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=0;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res+=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}

	public int produit_des_chiffres (String n0) {
		int i, n0len = n0.length(), res=1;
		if (n0.isEmpty()) return 0; // rien à traiter si n0="".

		for (i=0; i<n0len; i++) {
			res*=Integer.parseInt(""+n0.charAt(i));
		}

		return res;
	}
	public boolean estPremier (int n0) {
		for (int i=2; i<1+(int)(Math.sqrt((double)(n0))); i++) {
			if (n0%i==0) return false;
		}

		return true;
	}

	public void clacule_n_premiers (int n0) {
		int i, cmp=0;

		if (n0<1) return; // rien à afficher

		int [] res = new int[n0];

		i=2;
		while (cmp<n0) {
			if (estPremier(i)) {
				res[cmp] = i;
				cmp++;
			}

			i++;
		}

		affnn("p"+n0+" = {");
		for (i=0; i<res.length-1; i++) {
			affnn(res[i]+", ");
		}
		aff(res[i]+"};");
	}

// ######################### Fonctions utilitaires ##################################### //


	/**
		Shows if a String is an Integer 
		using function Integer.parseInt(String) of Java.
	**/
	public boolean isInteger (String n0) {
		try {
			int n1 = Integer.parseInt(n0);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
		Convertit un String en entier (gestion des exceptions).
	**/
	public int convertStrInt (String n0) {
		if (n0==null) {
			aff("n0 est null");
			return -1;
		}
		if (n0.isEmpty()) {
			aff("n0 est egal a "+'"'+'"'+".");
			return -1;
		}
		try {
			return Integer.parseInt(n0);
		}
		catch(NumberFormatException e) {
			aff("Impossible de convertir ");
			return -1;
		}
	}

	/**
		Vérifie si un char est un chiffre 
		ou non.
	**/
	public boolean isCharNumber (char n0) {
		return (n0>='0' && n0<='9');
	}

	public boolean isInteger_deep (String n0) {
		if (n0.isEmpty()) return false;
		int n0len = n0.length();

		for (int i=0; i<n0len; i++) {
			if (!isCharNumber(n0.charAt(i))) return false;
		}

		return true;
	}

	/**
		Regarde si CalculsDouble n0 est un nombre.
		Renvoie true si n0.ent est un nombre
		et que n0.dec est vide ou un nombre aussi.
	**/
	public boolean isInteger (CalculsDouble n0) {
		boolean res = false;
		int i=0;
		String n0ent = n0.ent;
		String n0dec = n0.dec;

		if (!n0ent.isEmpty()) {
			res = isInteger(n0ent);
		}
		if (!n0dec.isEmpty()) {
			res = isInteger(n0dec);
		}

		return res;
	}

	/**
		Renvoie true si n0=0.0
		ou 0.
	**/
	public boolean estNul (CalculsDouble n0) {
		int i=0;
		int n0entlen = n0.ent.length();
		int n0declen = 0;

		if (!isInteger(n0)) return false;

		if (!n0.dec.isEmpty())
			n0declen = n0.dec.length();

		for (i=0; i<n0entlen; i++) {
			if (n0.ent.charAt(i)!='0')
				return false;
		}

		for (i=0; i<n0declen; i++) {
			if (n0.dec.charAt(i)!='0')
				return false;
		}

		return true;
	}

	public int compteOcurrences (String test, char cc) {
		if (test==null) return 0;
		if (test.isEmpty()) return 0;
		// Gestion des erreurs

		int res = 0, lentest = test.length();
		for (int i=0; i<lentest; i++) {
			if (test.charAt(i)==cc) res++;
		}
		return res;
	}


	public static void affnn (Object s0) {
		System.out.print(""+s0);
	}

	public static void aff (Object s0) {
		System.out.println(""+s0);
	}

	public void affTab(int [] n0) {
		for (int i=0; i<n0.length; i++) {
			aff("t["+i+"] = "+n0[i]);
		}
	}

	public void affTab(int [] n0, String nom) {
		for (int i=0; i<n0.length; i++) {
			aff(nom+"["+i+"] = "+n0[i]);
		}
	}

	public void afftab2dim(String [] t1, String nom) {
		int i;

		//aff(nom+" : ");

		for (i=0; i<t1.length; i++) {
			aff(nom+"["+i+"] = "+t1[i]);
		}
	}

	public int [] agrandieTab (int [] t0) {
		int i;
		int [] t1 = new int[t0.length*2+1];

		for (i = 0; i<t0.length; i++) {
			t1[i] = t0[i];
		}

		return t1;
	}


// ################################ toString ########################################### //

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

// ######################## Reconnaissance opérations ################################## //


	Character [] operateurs_tmp = 
		{'+', '-', '*', '/'};
	Stack<String> st = new Stack<String>();
	ArrayList<Character> operateurs = new ArrayList<Character>();

	public void operateurs () {
		for (Character c1 : operateurs_tmp) {
			operateurs.add(c1);
		}
	}

	/**
		Renvoie -1 en cas d'erreur.
	**/
	public int analyse_expression_sans_parentheses (String expr) {
		if (expr==null) {
			aff("L'expression est null");
			return -1;
		}
		if (expr.isEmpty()) {
			aff("L'expression est vide.");
			return -1;
		}
		int lenexpr = expr.length();
		int cmp = 0;
		String tmp = "";

		for (int i=0; i<lenexpr; i++) {
			if (isCharNumber(expr.charAt(i))) {
				//affnn(expr.charAt(i));
				tmp = mange_nombre(expr, i);
				st.push(tmp);
				while (i<lenexpr 
				&& isCharNumber(expr.charAt(i))) i++;
			}
			if (cmp>0) calcul_pile(st);
			if (i>=lenexpr) break;
			if (operateurs.contains(expr.charAt(i))) {
				tmp = ""+mange_operateur(expr, i);
				st.push(tmp);
			}
			cmp++;
		}
		String res = pop_erreur(st);
		return convertStrInt(res);
	}

// ##################### Fonctions de traitement des symboles ######################### //

	public String pop_erreur (Stack<String> st) {
		if (!st.empty()) {
			return st.pop();
		}
		else {
			aff("Erreur : la pile est vide prematurement.");
			return "";
		}
	}

	public void calcul_pile (Stack<String> st) {
		String aS = pop_erreur(st);
		String symS = pop_erreur(st);
		String bS = pop_erreur(st);

		int res_tmp = traite_symbolesInt(bS, aS, symS);
		aff("res_tmp = "+res_tmp);
		st.push(""+res_tmp);
	}

	/**
		Retourne -1 en cas d'erreur.
	**/
	public int traite_symbolesInt (String aS, String bS, String symS) {
		if (symS==null || symS.isEmpty()) {
			aff("Symbole null ou egal a "+'"'+'"'+".");
			return -1;
		}
		if (!isInteger(aS) || !isInteger(bS)) {
			aff("aS (= "+aS+") ou bS (= "+bS+") ne sont pas des nombres.");
			return -1;
		}
		int a = Integer.parseInt(aS);
		int b = Integer.parseInt(bS);
		char sym = symS.charAt(0);
		if (!operateurs.contains(sym)) {
			aff("sym (= "+sym+") n'est pas un operateur.");
			return -1;
		}
		int res = 0;

		if (sym=='+') {
			res = a+b;
		}

		if (sym=='-') {
			res = a-b;
		}

		if (sym=='*') {
			res = a*b;
		}

		if (sym=='/') {
			res = a/b;
		}

		return res;
	}

// ########################### Fonction mange_symbole ################################# //

	public String verifie_double_correcte (String nombre, char sym) {
		if (nombre==null) {
			aff("Le nombre est null.");
			return "";
		}
		if (nombre.isEmpty()) {
			aff("Le nombre est vide.");
			return "";
		}
		// Gestion des erreurs
		char sym2 = '.';
		if (sym=='.') sym2 = ',';
		String [] double_tmp = nombre.split(""+sym);
		if (double_tmp==null) return "";
		if (double_tmp.length==1) {
			if (double_tmp[0]==null) return "";
			if (double_tmp[0].isEmpty()) return "";
			if (double_tmp[0].equals(",")) return "";
			return nombre.replace(""+sym, "");
		}
		if (double_tmp.length<2) return "";
		// Gestion des erreurs avec le nombre.split(sym).

		String tmp = double_tmp[1];
		int lentmp = tmp.length();
		
		for (int i=0; i<lentmp; i++) {
			if (i%3==0) {
				if (tmp.charAt(i)!=sym2) return "";
			}
			else {
				if (!isCharNumber(tmp.charAt(i)))
					return "";
			}
		}
		return double_tmp[0]+tmp.replace(""+sym2, "");
	}

	/**
		Retourne -1 en cas d'erreur.
	**/
	public double analyse_nombreDouble (String nombre) {
		int i = 0;
		int nbr_virgules = 0, nbr_points = 0;
		boolean virgule = false;
		boolean point = false;

		nbr_virgules = compteOcurrences(nombre, ',');
		nbr_points = compteOcurrences(nombre, '.');

		aff("nbr_virgules = "+nbr_virgules);
		aff("nbr_points = "+nbr_points);

		if (nbr_virgules==0 && nbr_points==0) {
			if (isInteger(nombre)) {
				return Integer.parseInt(nombre);
			}
		}
		else {
			if (nbr_virgules==1 && nbr_points>1) {
				nombre = verifie_double_correcte (nombre, ',');
			}

			if (nbr_virgules>1 && nbr_points==1) {
				nombre = verifie_double_correcte (nombre, '.');
			}

			nbr_virgules = compteOcurrences(nombre, ',');
			nbr_points = compteOcurrences(nombre, '.');

			aff("nbr_virgules = "+nbr_virgules);
			aff("nbr_points = "+nbr_points);
			aff("nombre = "+nombre);

			if (nbr_virgules>1 && nbr_points>1)
				return -1;
			if (nbr_virgules==1 && nbr_points==0) {
				return Double.parseDouble(
				nombre.replace(',', '.'));
			}
			if (nbr_virgules==0 && nbr_points==1) {
				return Double.parseDouble(nombre);	
			}
		}
		return -1;		
	}

	public String mange_nombre (String expr, int indice_debut) {
		int lenexpr = expr.length();
		boolean virgules = false;
		boolean points = false;
		boolean isDigit = true;
		String res = "";

		for (int i=indice_debut; i<lenexpr && isDigit; i++) {
			if (isCharNumber(expr.charAt(i))) {
				res+=expr.charAt(i);
			}
			if (expr.charAt(i)==',') {
				virgules = true;
				res+=expr.charAt(i);
			}
			if (expr.charAt(i)=='.') {
				points = true;
				res+=expr.charAt(i);
			}
			else isDigit = false;
		}
		aff(res);
		if (virgules || points) {
			res = ""+analyse_nombreDouble(res);
			return res;
		}
		else return res;
	}

	public char mange_operateur (String expr, int indice) {
		int lenexpr = expr.length();

		if (indice>=0 && indice<lenexpr) {
			aff(expr.charAt(indice));
			return expr.charAt(indice);
		}
		else return (char)(-1);
	}
	
// ##################### Main ####################### //

	public static void main (String [] args) {
		CalculsDouble cb = new CalculsDouble();
		/**String expr1
		//"103*8/09+3-8"
		 = "";
		int res_verififcation = 0;
		aff("expr1 = "+expr1);
		cb.operateurs();
		int res = cb.analyse_expression_sans_parentheses(expr1);
		aff("res = "+res);
		aff("Verification : "+res_verififcation);
		**/
		String nombre = "12.100,0";
		aff("nombre = "+nombre);
		Double tmpD = cb.analyse_nombreDouble(nombre);
		aff("res = "+tmpD);
	}

}
