/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "DS_StereoMate", namespace = "http://standards.iso.org/iso/19115/-3/mda/2.0")
public class DS_StereoMate extends DS_OtherAggregate {

    // occurrence and obligation
    private final String[] elementName = {"has", "partOf", "composedOf"};
    private final boolean[] elementObligation = {true, false, true};

    private final boolean[] elementUsed = new boolean[elementName.length];

    // methods
    public DS_StereoMate(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.DS_StereoMate);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.DS_StereoMate);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                } else if (tempList.contains(elementName[i])) {
                    // element mandatory
                    elementObligation[i] = true;
                }
            }
        }
    }
}
