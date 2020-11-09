/*
 * Copyright 2020
 * @author: Michael Wagner
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

@XmlRootElement(name = "MD_FeatureCatalogueDescription", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MD_FeatureCatalogueDescription extends MD_ContentInformation {

    // occurrence and obligation
    private final String[] elementName = {"complianceCode", "locale", "includedWithDataset", "featureTypes", "featureCatalogueCitation"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "complianceCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    public List<Boolean> complianceCode;

    @XmlElementWrapper(name = "locale", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<PT_Locale> locale;

    @XmlElement(name = "includedWithDataset", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    public List<Boolean> includedWithDataset;

    @XmlElementWrapper(name = "featureTypes", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<MD_FeatureTypeInfo> featureTypes;

    @XmlElementWrapper(name = "featureCatalogueCitation", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<CI_Citation> featureCatalogueCitation;

    // methods
    public MD_FeatureCatalogueDescription(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_FeatureCatalogueDescription);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_FeatureCatalogueDescription);
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

    public void addComplianceCode(Boolean complianceCode) {
        if (this.complianceCode == null) {
            this.complianceCode = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.complianceCode.add(complianceCode);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLocale(PT_Locale locale) {
        if (this.locale == null) {
            this.locale = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.locale.add(locale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIncludedWithDataset(Boolean includedWithDataset) {
        if (this.includedWithDataset == null) {
            this.includedWithDataset = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.includedWithDataset.add(includedWithDataset);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFeatureTypes(MD_FeatureTypeInfo featureTypes) {
        if (this.featureTypes == null) {
            this.featureTypes = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.featureTypes.add(featureTypes);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFeatureCatalogueCitation(CI_Citation featureCatalogueCitation) {
        if (this.featureCatalogueCitation == null) {
            this.featureCatalogueCitation = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.featureCatalogueCitation.add(featureCatalogueCitation);
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
