/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.game;

import java.util.Random;

public class Harbor {
    private final Resource resource;
    private final int coeff;

    /**
     * Creates a new Harbor instance with given resource type and coefficient
     *
     * @param resource The resource type of the harbor
     * @param coeff The coefficient of the harbor
     */
    public Harbor(Resource resource, int coeff) {
        this.resource = resource;
        this.coeff = coeff;
    }

    /**
     * Creates a new Harbor instance with a coefficient
     *
     * @param coeff The coefficient of the harbor
     */
    public Harbor(int coeff) {
        this(null, coeff);
    }

    /**
     * Verify if the current harbor is specialised or not
     *
     * @return True if the current harbor is specialised
     */
    public boolean isSpecialised() {
        return this.resource != null;
    }

    /**
     * Get the resource of the current Harbor
     *
     * @return The resource of the current harbor
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * Get the coefficient of the current Harbor
     *
     * @return The coefficient of the current harbor
     */
    public int getCoeff() {
        return this.coeff;
    }

    /**
     * Creates a random harbor with random coefficient and random resource type
     *
     * @return The created Harbor instance
     */
    public static Harbor random() {
        Random r = new Random();
        return new Harbor(Resource.random(), r.nextInt(3) + 1);
    }

    @Override
    public String toString() {
        return (this.isSpecialised()
                ? String.format(
                        "Harbor resource type %s with exchange rate %d:1",
                        this.getResource(), this.getCoeff())
                : String.format("harbor generic with an exchange rate %d:1", this.getCoeff()));
    }
}
