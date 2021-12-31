/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.commons;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
    EXPERT;

    public String toString() {
        return switch (this) {
            case EASY -> "Easy";
            case MEDIUM -> "Medium";
            case HARD -> "Hard";
            case EXPERT -> "Expert";
        };
    }

    public static Difficulty fromInteger(int i) {
        return switch (i) {
            case 0 -> EASY;
            case 1 -> MEDIUM;
            case 2 -> HARD;
            case 3 -> EXPERT;
            default -> throw new IllegalArgumentException(
                    String.format("No difficulty level for %d", i));
        };
    }
}
