/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
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