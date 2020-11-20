/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO191xxProfile;

public class MaximumOccurrenceException extends Exception {
    String fieldName;
    int maximumOccurrence;

    public MaximumOccurrenceException(String fieldName, int maximumOccurrence) {
        this.fieldName = fieldName;
        this.maximumOccurrence = maximumOccurrence;
    }

    public String getMessage() {
        if (maximumOccurrence == 1) {
            return ("\nMaximum Occurrence Exception\n" +
                    fieldName + ": maximum number of " + maximumOccurrence + " item exceeded" +
                    "\nlast item not added");
        }
        else {
            return ("\nMaximum Occurrence Exception\n" +
                    fieldName + ": maximum number of " + maximumOccurrence + " items exceeded" +
                    "\nlast item not added");
        }
    }
}
