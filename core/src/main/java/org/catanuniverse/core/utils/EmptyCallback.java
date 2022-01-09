/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.utils;

import java.io.IOException;

@FunctionalInterface
public interface EmptyCallback {
    public void call() throws IOException;
}
