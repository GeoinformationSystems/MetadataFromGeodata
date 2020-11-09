/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO191xxProfile;

public class InstantiationCallException extends Exception {
    String className;
    String fieldName;

    public InstantiationCallException(String className, String fieldName) {
        this.className = className;
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return ("\nInstantiation Call Exception\n" +
                className + " instantiated, but missing " + fieldName);
    }
}
