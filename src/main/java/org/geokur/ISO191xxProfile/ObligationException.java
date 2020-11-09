/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO191xxProfile;

public class ObligationException extends Exception {
    String fieldName;

    public ObligationException(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return ("\nObligation Exception\n" +
                fieldName + ": mandatory but not instantiated");
    }
}
