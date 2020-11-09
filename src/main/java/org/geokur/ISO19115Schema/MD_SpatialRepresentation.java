/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import java.util.List;

public abstract class MD_SpatialRepresentation {

    // variables for correct marshalling of specified classes
    public List<MD_GridSpatialRepresentation> gridSpatialRepresentation;

    public List<MD_VectorSpatialRepresentation> vectorSpatialRepresentation;
}
