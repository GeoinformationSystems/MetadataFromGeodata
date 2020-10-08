/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO191xxProfile;

public class InstantiationCallException extends RuntimeException {
    String className;
    String fieldName;

    public InstantiationCallException(String className, String fieldName) {
        this.className = className;
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return ("\nInstantiation Exception\n" +
                className + " instantiated, but missing " + fieldName);
    }
}
