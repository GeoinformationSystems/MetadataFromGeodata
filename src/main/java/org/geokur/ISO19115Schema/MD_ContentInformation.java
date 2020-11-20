/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import java.util.List;

public abstract class MD_ContentInformation {

    // variables for correct marshalling of specified classes
    public List<MD_FeatureCatalogueDescription> featureCatalogueDescription;

    public List<MD_FeatureCatalogue> featureCatalogue;

    public List<MD_CoverageDescription> coverageDescription;
}
