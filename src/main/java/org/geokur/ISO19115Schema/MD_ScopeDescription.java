/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_ScopeDescription", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
public class MD_ScopeDescription {
    // union class - means only one variable allowed

    // occurrence and obligation
    private final String[] elementName = {"attributes", "features", "featureInstances", "attributeInstances", "dataset", "other"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "attributes", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> attributes;

    @XmlElement(name = "features", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> features;

    @XmlElement(name = "featureInstances", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> featureInstances;

    @XmlElement(name = "attributeInstances", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> attributeInstances;

    @XmlElement(name = "dataset", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> dataset;

    @XmlElement(name = "other", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
    public List<String> other;

    // methods
    public MD_ScopeDescription(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_ScopeDescription);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_ScopeDescription);
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

    public void addAttributes(String attributes) {
        try {
            if (this.features != null || this.featureInstances != null || this.attributeInstances != null || this.dataset != null || this.other != null) {
                throw new UnionException(className);
            } else {
                if (this.attributes == null) {
                    this.attributes = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.attributes.add(attributes);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFeatures(String features) {
        try {
            if (this.attributes != null || this.featureInstances != null || this.attributeInstances != null || this.dataset != null || this.other != null) {
                throw new UnionException(className);
            } else {
                if (this.features == null) {
                    this.features = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.features.add(features);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFeatureInstances(String featureInstances) {
        try {
            if (this.attributes != null || this.features != null || this.attributeInstances != null || this.dataset != null || this.other != null) {
                throw new UnionException(className);
            } else {
                if (this.featureInstances == null) {
                    this.featureInstances = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.featureInstances.add(featureInstances);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAttributeInstances(String attributeInstances) {
        try {
            if (this.attributes != null || this.features != null || this.featureInstances != null || this.dataset != null || this.other != null) {
                throw new UnionException(className);
            } else {
                if (this.attributeInstances == null) {
                    this.attributeInstances = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.attributeInstances.add(attributeInstances);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDataset(String dataset) {
        try {
            if (this.attributes != null || this.features != null || this.featureInstances != null || this.attributeInstances != null || this.other != null) {
                throw new UnionException(className);
            } else {
                if (this.dataset == null) {
                    this.dataset = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dataset.add(dataset);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOther(String other) {
        try {
            if (this.attributes != null || this.features != null || this.featureInstances != null || this.attributeInstances != null || this.dataset != null) {
                throw new UnionException(className);
            } else {
                if (this.other == null) {
                    this.other = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.other.add(other);
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
