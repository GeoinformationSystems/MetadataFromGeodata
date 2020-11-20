/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19115_2Schema.MI_Georeferenceable;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Georeferenceable", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_Georeferenceable extends MD_GridSpatialRepresentation {

    // occurrence and obligation
    private final String[] elementName = {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "controlPointAvailability", "orientationParameterAvailability", "orientationParameterDescription", "georeferencesParameters", "parameterCitation"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true, true, true, false, true, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "controlPointAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Boolean> controlPointAvailability;

    @XmlElement(name = "orientationParameterAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Boolean> orientationParameterAvailability;

    @XmlElement(name = "orientationParameterDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> orientationParameterDescription;

    @XmlElement(name = "georeferencedParameters", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Record> georeferencedParameters;

    @XmlElementWrapper(name = "parameterCitation", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<CI_Citation> parameterCitation;

    // variables for correct marshalling of specified classes
    public List<MI_Georeferenceable> georeferenceable;

    // methods
    public MD_Georeferenceable(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Georeferenceable);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Georeferenceable);
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

    public void addControlPointAvailability(Boolean controlPointAvailability) {
        if (this.controlPointAvailability == null) {
            this.controlPointAvailability = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.controlPointAvailability.add(controlPointAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrientationParameterAvailability(Boolean orientationParameterAvailability) {
        if (this.orientationParameterAvailability == null) {
            this.orientationParameterAvailability = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orientationParameterAvailability.add(orientationParameterAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrientationParameterDescription(String orientationParameterDescription) {
        if (this.orientationParameterDescription == null) {
            this.orientationParameterDescription = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orientationParameterDescription.add(orientationParameterDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGeoreferencedParameters(Record georeferencedParameters) {
        if (this.georeferencedParameters == null) {
            this.georeferencedParameters = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.georeferencedParameters.add(georeferencedParameters);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParameterCitation(CI_Citation parameterCitation) {
        if (this.parameterCitation == null) {
            this.parameterCitation = new ArrayList<>();
        }

        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameterCitation.add(parameterCitation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finalizeClass() {
        for (int i = 0; i < elementName.length; i++) {
            try {
                List<?> tempList = (List<?>) this.getClass().getField(elementName[i]).get(this);
                if (!elementUsed[i] && tempList != null && !tempList.isEmpty()) {
                    // test profile use
                    throw new ProfileException(className + " - " + elementName[i]);
                }
                if (elementObligation[i] && (tempList == null || tempList.isEmpty())) {
                    // test filling and obligation of all variable lists
                    throw new ObligationException(className + " - " + elementName[i]);
                }
            } catch (ProfileException | ObligationException | NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
