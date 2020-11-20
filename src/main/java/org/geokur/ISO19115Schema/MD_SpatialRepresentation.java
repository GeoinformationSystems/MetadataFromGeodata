/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import java.util.List;

public abstract class MD_SpatialRepresentation {

    // variables for correct marshalling of specified classes
    public List<MD_GridSpatialRepresentation> gridSpatialRepresentation;

    public List<MD_VectorSpatialRepresentation> vectorSpatialRepresentation;
}
