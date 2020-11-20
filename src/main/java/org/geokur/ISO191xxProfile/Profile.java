/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO191xxProfile;

public class Profile {
    public ProfileUsed used;
    public ProfileObligation obligation;

    public void setUsed(ProfileUsed used) {
        this.used = used;
    }

    public void setObligation(ProfileObligation obligation) {
        this.obligation = obligation;
    }
}