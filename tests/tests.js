/**
 * Tests pour CalculsDouble_2014
 * Validation des operations arithmetiques sur grands nombres
 * Execution: node tests.js
 */

const TESTS_RESULTS = { passed: 0, failed: 0 };

/**
 * Fonction d'assertion simple.
 * @param {string} description Description du test.
 * @param {boolean} condition Condition a verifier.
 */
function assert(description, condition) {
    if (condition) {
        console.log(`[PASS] ${description}`);
        TESTS_RESULTS.passed++;
    } else {
        console.log(`[FAIL] ${description}`);
        TESTS_RESULTS.failed++;
    }
}

/**
 * Classe simulant CalculsDouble.
 */
class CalculsDouble {
    constructor(valeur = 0) {
        this.estNegatif = valeur < 0;
        const valeurAbs = Math.abs(valeur);
        this.entier = Math.floor(valeurAbs).toString();
        const decimale = valeurAbs - Math.floor(valeurAbs);
        this.decimal = decimale > 0 ? decimale.toString().substring(2) : "";
    }

    /**
     * Retourne la valeur numerique.
     * @returns {number} Valeur.
     */
    toNumber() {
        let valeur = parseFloat(this.entier + (this.decimal ? "." + this.decimal : ""));
        return this.estNegatif ? -valeur : valeur;
    }

    /**
     * Additionne deux CalculsDouble.
     * @param {CalculsDouble} autre Autre nombre.
     * @returns {CalculsDouble} Resultat.
     */
    additionner(autre) {
        return new CalculsDouble(this.toNumber() + autre.toNumber());
    }

    /**
     * Soustrait deux CalculsDouble.
     * @param {CalculsDouble} autre Autre nombre.
     * @returns {CalculsDouble} Resultat.
     */
    soustraire(autre) {
        return new CalculsDouble(this.toNumber() - autre.toNumber());
    }

    /**
     * Multiplie deux CalculsDouble.
     * @param {CalculsDouble} autre Autre nombre.
     * @returns {CalculsDouble} Resultat.
     */
    multiplier(autre) {
        return new CalculsDouble(this.toNumber() * autre.toNumber());
    }

    /**
     * Divise deux CalculsDouble.
     * @param {CalculsDouble} autre Autre nombre.
     * @returns {CalculsDouble} Resultat.
     */
    diviser(autre) {
        if (autre.toNumber() === 0) throw new Error("Division par zero");
        return new CalculsDouble(this.toNumber() / autre.toNumber());
    }

    /**
     * Valeur absolue.
     * @returns {CalculsDouble} Valeur absolue.
     */
    valeurAbsolue() {
        return new CalculsDouble(Math.abs(this.toNumber()));
    }
}

// ==================== TESTS ====================

console.log("=== Tests CalculsDouble ===\n");

// Tests constructeur
assert("CalculsDouble positif", new CalculsDouble(42).toNumber() === 42);
assert("CalculsDouble negatif", new CalculsDouble(-42).toNumber() === -42);
assert("CalculsDouble decimal", Math.abs(new CalculsDouble(3.14).toNumber() - 3.14) < 0.001);
assert("CalculsDouble zero", new CalculsDouble(0).toNumber() === 0);

// Tests addition
assert("Addition simples", new CalculsDouble(5).additionner(new CalculsDouble(3)).toNumber() === 8);
assert("Addition negatifs", new CalculsDouble(-5).additionner(new CalculsDouble(-3)).toNumber() === -8);
assert("Addition mixte", new CalculsDouble(5).additionner(new CalculsDouble(-3)).toNumber() === 2);

// Tests soustraction
assert("Soustraction simple", new CalculsDouble(10).soustraire(new CalculsDouble(3)).toNumber() === 7);
assert("Soustraction resultat negatif", new CalculsDouble(3).soustraire(new CalculsDouble(10)).toNumber() === -7);

// Tests multiplication
assert("Multiplication simple", new CalculsDouble(6).multiplier(new CalculsDouble(7)).toNumber() === 42);
assert("Multiplication par zero", new CalculsDouble(100).multiplier(new CalculsDouble(0)).toNumber() === 0);
assert("Multiplication negatifs", new CalculsDouble(-3).multiplier(new CalculsDouble(-4)).toNumber() === 12);

// Tests division
assert("Division simple", new CalculsDouble(20).diviser(new CalculsDouble(4)).toNumber() === 5);
assert("Division decimale", Math.abs(new CalculsDouble(10).diviser(new CalculsDouble(4)).toNumber() - 2.5) < 0.001);

// Tests valeur absolue
assert("Valeur absolue positif", new CalculsDouble(5).valeurAbsolue().toNumber() === 5);
assert("Valeur absolue negatif", new CalculsDouble(-5).valeurAbsolue().toNumber() === 5);

// Test division par zero
assert("Division par zero lance erreur", (() => {
    try {
        new CalculsDouble(10).diviser(new CalculsDouble(0));
        return false;
    } catch (erreur) {
        return true;
    }
})());

// ==================== RESUME ====================

console.log("\n=== Resume ===");
console.log(`Tests passes: ${TESTS_RESULTS.passed}`);
console.log(`Tests echoues: ${TESTS_RESULTS.failed}`);
console.log(`Total: ${TESTS_RESULTS.passed + TESTS_RESULTS.failed}`);

if (TESTS_RESULTS.failed > 0) {
    process.exit(1);
}
