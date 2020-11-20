/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.DS_Resource;

public interface Metadata {
    DS_Resource getMetadata();
}
