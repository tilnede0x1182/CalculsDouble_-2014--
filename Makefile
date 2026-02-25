run:
	cd build && java CalculsDouble

compile:
	mkdir -p build
	cd src && python3 Utilitaire/preprocesseur_imports.py CalculsDouble.java
	javac -proc:none -d build src/CalculsDouble_modifie.java

compile_run: compile run

javadoc:
	javadoc -d javadoc -private src/CalculsDouble_modifie.java