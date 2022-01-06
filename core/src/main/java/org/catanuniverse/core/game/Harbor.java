/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.util.Random;

public class Harbor {
    private final Resource resource;
    private final int coeff;
    public Harbor(Resource resource, int coeff) {
        this.resource = resource;
        this.coeff = coeff;
    }

    public Harbor(int coeff) {
        this(null, coeff);
    }

    public boolean isSpecialised() {
        return this.resource != null;
    }

    public Resource getResource() {
        return this.resource;
    }

    public int getCoeff() {
        return this.coeff;
    }

    public static Harbor random() {
        Random r = new Random();
        return new Harbor(Resource.random(), r.nextInt(3) + 1);
    }
}
