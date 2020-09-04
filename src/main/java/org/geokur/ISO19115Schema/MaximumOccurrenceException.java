/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

public class MaximumOccurrenceException extends RuntimeException {
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
