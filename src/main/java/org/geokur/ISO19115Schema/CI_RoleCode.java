/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CI_RoleCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
public class CI_RoleCode {
    // codelist - can be extended

    public enum CI_RoleCodes {
        resourceProvider,
        custodian,
        owner,
        user,
        distributor,
        originator,
        pointOfContact,
        principalInvestigator,
        processor,
        publisher,
        author,
        sponsor,
        coAuthor,
        collaborator,
        editor,
        mediator,
        rightsHolder,
        contributor,
        funder,
        stakeholder;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/cit/1.0/codelists.html#CI_RoleCode";

    @XmlAttribute
    public CI_RoleCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CI_RoleCode(){}

    public CI_RoleCode(CI_RoleCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CI_RoleCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}