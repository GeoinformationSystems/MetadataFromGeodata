/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_TopicCategoryCode", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_TopicCategoryCode {
    // enumeration - cannot be extended

    public enum MD_TopicCategoryCodes {
        farming,
        biota,
        boundaries,
        climatologyMeteorologyAtmosphere,
        economy,
        elevation,
        environment,
        geoscientificInformation,
        health,
        imageryBaseMapsEarthCover,
        intelligenceMilitary,
        inlandWaters,
        location,
        oceans,
        planningCadastre,
        society,
        structure,
        transportation,
        utilitiesCommunication,
        extraTerrestrial,
        disaster;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mri/1.0/codelists.html#MD_TopicCategoryCode";

    @XmlAttribute
    public MD_TopicCategoryCode.MD_TopicCategoryCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_TopicCategoryCode(){}

    public MD_TopicCategoryCode(MD_TopicCategoryCode.MD_TopicCategoryCodes codeListValue) {
        this.codeListValue = codeListValue;
    }
}
