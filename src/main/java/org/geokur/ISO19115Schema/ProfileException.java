/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

public class ProfileException extends RuntimeException {
    String fieldName;

    public ProfileException(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return ("\nProfile Exception\n" +
                fieldName + ": not in current profile, but instantiated");
    }
}
