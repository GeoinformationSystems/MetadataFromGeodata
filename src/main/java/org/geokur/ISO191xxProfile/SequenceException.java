/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO191xxProfile;

public class SequenceException extends Exception {
    String leadingElement;
    String followingElement;

    public SequenceException(String leadingElement, String followingElement) {
        this.leadingElement = leadingElement;
        this.followingElement = followingElement;
    }

    public String getMessage() {
        return ("\nSequence Exception\n" +
                " Cannot instantiate " + followingElement + " before " + leadingElement);
    }
}
