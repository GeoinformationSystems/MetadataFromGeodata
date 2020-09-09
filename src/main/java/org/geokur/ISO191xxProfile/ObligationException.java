/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO191xxProfile;

public class ObligationException extends RuntimeException {
    String fieldName;

    public ObligationException(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return ("\nObligation Exception\n" +
                fieldName + ": mandatory but not instantiated");
    }
}
