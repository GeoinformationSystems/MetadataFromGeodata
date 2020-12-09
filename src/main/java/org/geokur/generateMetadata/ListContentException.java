/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

public class ListContentException extends Exception {
    String string;
    String filename;

    public ListContentException(String string, String filename) {
        this.string = string;
        this.filename = filename;
    }

    public String getMessage() {
        return ("\nList Content Exception\n" +
                string + " not available in " + filename);
    }
}
