/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO191xxProfile;

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
