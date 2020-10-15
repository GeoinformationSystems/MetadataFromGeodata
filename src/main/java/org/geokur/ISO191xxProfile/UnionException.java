/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO191xxProfile;

public class UnionException extends Exception {
    String className;

    public UnionException(String className) {
        this.className = className;
    }

    @Override
    public String getMessage() {
        return ("\nUnionException\n" +
                "Class " + className + " must not have more than one attribute entry");
    }
}
