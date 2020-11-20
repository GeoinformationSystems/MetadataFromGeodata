/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

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

@XmlRootElement(name = "MD_ApplicationSchemaInformation", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
public class MD_ApplicationSchemaInformation {

    // occurrence and obligation
    private final String[] elementName = {"name", "schemaLanguage", "constraintLanguage", "schemaAscii", "graphicsFile", "softwareDevelopmentFile", "softwareDevelopmentFileFormat"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {true, true, true, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    @XmlElementRef
    public List<CI_Citation> name;

    @XmlElement(name = "schemaLanguage", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    public List<String> schemaLanguage;

    @XmlElement(name = "constraintLanguage", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    public List<String> constraintLanguage;

    @XmlElement(name = "schemaAscii", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    public List<String> schemaAscii;

    @XmlElementWrapper(name = "graphicsFile", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    @XmlElementRef
    public List<CI_OnlineResource> graphicsFile;

    @XmlElementWrapper(name = "softwareDevelopmentFile", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    @XmlElementRef
    public List<CI_OnlineResource> softwareDevelopmentFile;

    @XmlElement(name = "softwareDevelopmentFileFormat", namespace = "http://standards.iso.org/iso/19115/-3/mas/1.0")
    public List<String> softwareDevelopmentFileFormat;

    // methods
    public MD_ApplicationSchemaInformation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_ApplicationSchemaInformation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_ApplicationSchemaInformation);
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

    public void addName(CI_Citation name) {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.name.add(name);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSchemaLanguage(String schemaLanguage) {
        if (this.schemaLanguage == null) {
            this.schemaLanguage = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.schemaLanguage.add(schemaLanguage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConstraintLanguage(String constraintLanguage) {
        if (this.constraintLanguage == null) {
            this.constraintLanguage = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.constraintLanguage.add(constraintLanguage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSchemaAscii(String schemaAscii) {
        if (this.schemaAscii == null) {
            this.schemaAscii = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.schemaAscii.add(schemaAscii);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGraphicsFile(CI_OnlineResource graphicsFile) {
        if (this.graphicsFile == null) {
            this.graphicsFile = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.graphicsFile.add(graphicsFile);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSoftwareDevelopmentFile(CI_OnlineResource softwareDevelopmentFile) {
        if (this.softwareDevelopmentFile == null) {
            this.softwareDevelopmentFile = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.softwareDevelopmentFile.add(softwareDevelopmentFile);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSoftwareDevelopmentFileFormat(String softwareDevelopmentFileFormat) {
        if (this.softwareDevelopmentFileFormat == null) {
            this.softwareDevelopmentFileFormat = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.softwareDevelopmentFileFormat.add(softwareDevelopmentFileFormat);
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
