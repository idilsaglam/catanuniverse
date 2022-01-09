/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.utils;

@FunctionalInterface
public interface TriPredicate<A, B, C> {

    boolean test(A a, B b, C c);
}
