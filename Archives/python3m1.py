#python3

### Importations ###
import sys
import os
import codecs

## Tests ##

#gg="ekljdzl\nkejdlezd d\nlzeimport Explication.txt  \nimport blabla\nimport blabla\nimport blabla\nimport blabla\nimport blabla\nkdlejzdkljze import blabla\ndjzkljdz dzkdjezl dkje\n"
gg="ekljdzl\nkejdlezd d\nlzeimport Explication.txt  \n\n\n\n\n\nkdlejzdkljze \ndjzkljdz dzkdjezl dkje\n"

#									import 

###########

'''
	Transforme "   blabla   " en "blabla".
'''
def supprime_espaces_inutiles (gg):
	i=0
	booli=True

	while (booli):
		if (i<len(gg)):
			if (gg[i]!=' '):
				booli=False
		else:
			booli=False

		i+=1

	i-=1
	j=i
	boolj=True
	while (boolj):
		if (j<len(gg)):
			if (gg[j]==' '):
				boolk=False
				for k in range(j, len(gg)):
					if (gg[k]!=' '):boolk=True
				
				boolj=boolk

		else:
			boolj=False

		if (boolj):j+=1


	return (gg[i:j])

'''
	Détecte 'importm1 "fic"' dans le str gg.
'''
def detecte_importm1 (gg):
	tmp = ""
	list_fic = []
	for i in range(len(gg)):
		if (i<len(gg)-len("import ")):
			if (gg[i:i+len("import ")]=="import "):
				#print(gg[i:i+len("import ")])
				tmp=""
				j = i+len("import ")
				boolj=True
				while (boolj):
					if (j<len(gg)):
						if (gg[j]=='\n'):
							boolj=False
						if (boolj):tmp += gg[j]
					else:
						boolj=False
					j+=1
	
				list_fic.append(supprime_espaces_inutiles(tmp))
	
	return list_fic


'''
	Prend un tableau de noms de fichiers (liste python).
	Essaie d'ouvrir les fichier fournis dans le tableau de noms de fichiers.
'''
def ouvre_fichiers (nom_fic):
	res=[]

	tmp=""
	for i in range(len(nom_fic)):
		tmp=""
		try:
			fichier1=open(nom_fic[i], 'r')
			tmp = fichier1.read()
			fichier1.close()

			#fichier2=codecs.open(nom_fic[i], 'r','utf-8')
			#fichier2 = fichier2.replace('\ufeff', "")
			#tmp = fichier2.read()
			#fichier2.close()

			# Correction des problèmes connus de la conversion en utf-8 (en python) #
			tmp = tmp.replace('Ã©', "é")
			tmp = tmp.replace("Ã ", 'à')
			tmp = tmp.replace('ï»¿', "")
			tmp = tmp.replace("Ã¢", "â")
			# --------------------------------------------------------------------- #


		except:
			print("Erreur lors de l'ouverture du fichier "+nom_fic[i]+".")
			tmp="Error"

		res.append(tmp)

	return res

'''
	Remplace 'importm1 "fic"' par le fichier s'il existe.
	Prend tableau de contenus de fichiers.
'''
def remplace_importm1_fic (gg):
	res_0=detecte_importm1(gg)
	res_1=ouvre_fichiers(res_0)

	#print("res_0 = "+(str)(res_0))
	#print("res_1 = "+(str)(res_1))

	if (len(res_0)!=len(res_1)):
		print("len(res_0)!=len(res_1) : Erreur dans la fonction remplace_importm1_fic (gg).")
		return gg

	gg1=gg.split('\n')

	#print("gg1 = "+(str)(gg1))

	#return gg

	res_2=""

	i=0
	j=0
	while (i<len(gg1)):
		if ("import " in gg1[i]):
			#print("gg1 = "+(str)(gg1[i]))
			if (res_1[j]!="Error"):
				res_2+=res_1[j]
			else:
				res_2+=gg1[i]
			if (i<len(res_2)-1 and not res_1[j]==""):res_2+='\n'
			j+=1
		else:
			res_2+=gg1[i]
			if (i<len(res_2)-1):res_2+='\n'

		i+=1

	return res_2

'''
	Retourne l'adresse du fichier si elle est fournie.
'''
def retourne_adresse_nom_fic (nom_fic):
	ff=nom_fic
	res=len(ff)

	if (os.sep in ff):
		i=0

		while (i<len(ff)):
			if (ff[i]==os.sep):
				res=i

			i+=1

	return ff[0:res+1]

#### Test de fonctions ####
def main_test():
	res=""
	res=detecte_importm1 (gg)
	#res=supprime_espaces_inutiles("                jedejkhdnejkh edjhe djkhe z        nkjl   ")
	
	print(remplace_importm1_fic(res))
	
	print((str)(res))

### Début du programme ####

def main ():
	res=""
	modifie="modifie"

	if (len(sys.argv)<2) :
		print("Veuillez entrer un nom de fichier : '> programme nom_de_fichier'")
		sys.exit(1)

	if (len(sys.argv)==3) :
		if (sys.argv[2]!=""):
			modifie = sys.argv[2]
	
	fichier = sys.argv[1]
	print("fichier ouvert en lecture = "+fichier)
	
	fichier1 = open(fichier, 'r')
	gg = fichier1.read()
	fichier1.close()
	
	#print("fichier à modifier = "+gg)

	res = remplace_importm1_fic(gg)

	#adresse = ""
	#adresse = retourne_adresse_nom_fic(fichier) # l'adresse est fournie avec le fichier, en fait.
	#print(fichier[:len(fichier)-3]+"_modifie.py")

	#print("\nres = "+(str)(res))

	#fichier2 = open(fichier[:len(fichier)-3]+"_"+modifie+".py", 'w', encoding='utf-8')
	#fichier2.write(res)
	#fichier2.close()


	# Correction des problèmes connus de la conversion en utf-8 (en python) #
	res = res.replace('Ã©', "é")
	res = res.replace("Ãª", "ê")
	res = res.replace("Ã ", 'à')
	res = res.replace('ï»¿', "")
	res = res.replace("Ã¢", "â")

	# --------------------------------------------------------------------- #

	if (".py" in fichier):
		#print("Fichier ouvert en écriture : "+fichier[:len(fichier)-len(".py")]+"_"+modifie+".py")
		fichier3 = open(fichier[:len(fichier)-len(".py")]+"_"+modifie+".py", 'w', encoding='utf-8')
	elif (".java" in fichier):
		fichier3 = open(fichier[:len(fichier)-len(".java")]+"_"+modifie+".java", 'w')
	#print("res = "+res)
	fichier3.write(res)
	fichier3.close()

	return


main ()

#main_test()