/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

public class Extent {

    Double west;
    Double east;
    Double south;
    Double north;

    public Extent(){}

    public Extent(Double missingValue) {
        west = missingValue;
        east = missingValue;
        south = missingValue;
        north = missingValue;
    }
}
