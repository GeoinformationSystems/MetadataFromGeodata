/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
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
