/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

public class SRSDescription {
    private String srsOrganization;
    private Integer srsOrganizationCoordsysID;
    private String srsName = null;

    public SRSDescription() {}

    public void setSrsOrganization(String srsOrganization) {
        this.srsOrganization = srsOrganization;
    }

    public void setSrsOrganizationCoordsysID(Integer srsOrganizationCoordsysID) {
        this.srsOrganizationCoordsysID = srsOrganizationCoordsysID;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public String getSrsOrganization() {
        return srsOrganization;
    }

    public Integer getSrsOrganizationCoordsysID() {
        return srsOrganizationCoordsysID;
    }

    public String getSrsName() {
        return srsName;
    }
}
