/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19157Schema.DQ_CompletenessOmission;
import org.geokur.ISO19157Schema.DQ_DataQuality;
import org.geokur.ISO19157Schema.DQ_Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.beans.XMLEncoder;
import java.io.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ISOMain {
    public static String profileFilename = "config/profile_GeoKur.json";

    public static void main(String[] argv) throws JAXBException, FileNotFoundException {

        // read profile json file
        ProfileReader.setProfile(profileFilename);

        // create and fill particular fields from ISO 19115
        MD_DataIdentification md_dataIdentification = new MD_DataIdentification();
        md_dataIdentification.createSupplementalInformation();
        md_dataIdentification.addSupplementalInformation("TestSupplementalInformation for MD_DataIdentification");
        md_dataIdentification.createAbstract();
        md_dataIdentification.addAbstract("TestAbstract for MD_DataIdentification");
        md_dataIdentification.finalizeClass();

        SV_ServiceIdentification sv_serviceIdentification = new SV_ServiceIdentification();
        sv_serviceIdentification.createPurpose();
        sv_serviceIdentification.addPurpose("TestPurpose for SV_ServiceIdentification");
        sv_serviceIdentification.createAbstract();
        sv_serviceIdentification.addAbstract("TestAbstract");
        sv_serviceIdentification.finalizeClass();

        CI_Individual ci_individual = new CI_Individual();
        ci_individual.createName();
        ci_individual.addName("TestNameIndividual");
        ci_individual.createPositionName();
        ci_individual.addPositionName("TestPosition");
        ci_individual.finalizeClass();

        CI_Organisation ci_organisation = new CI_Organisation();
        ci_organisation.createName();
        ci_organisation.addName("TestNameOrganisation");
        ci_organisation.createIndividual();
        ci_organisation.addIndividual(ci_individual);
        ci_organisation.finalizeClass();

        CI_Responsibility ci_responsibility = new CI_Responsibility();
        ci_responsibility.createRole();
        ci_responsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
//        ci_responsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.custodian));
//        ci_responsibility.addRole(new CI_RoleCode("extendedOwnRole"));
        ci_responsibility.createParty();
        ci_responsibility.addParty(ci_organisation);
        ci_responsibility.addParty(ci_individual);
        ci_responsibility.finalizeClass();

        CI_Date ci_date = new CI_Date();
        ci_date.createDateType();
        ci_date.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
        ci_date.createDate();
        ci_date.addDate(ZonedDateTime.now(ZoneOffset.UTC).toString());
        ci_date.finalizeClass();

        DQ_Element dq_element = new DQ_CompletenessOmission();
        dq_element.createStandaloneQualityReportDetails();
        dq_element.addStandaloneQualityReportDetails("Test of data quality report");

        DQ_DataQuality dq_dataQuality = new DQ_DataQuality();
        dq_dataQuality.createReport();
        dq_dataQuality.addReport(dq_element);

        MD_Metadata md_metadata = new MD_Metadata();
        md_metadata.createContact();
        md_metadata.addContact(ci_responsibility);
        md_metadata.createDateInfo();
        md_metadata.addDateInfo(ci_date);
        md_metadata.createIdentificationInfo();
        md_metadata.addIdentificationInfo(md_dataIdentification);
        md_metadata.addIdentificationInfo(sv_serviceIdentification);

        md_metadata.createDataQualityInfo();
        md_metadata.addDataQualityInfo(dq_dataQuality);

        md_metadata.finalizeClass();

        DS_Resource ds_resource = new DS_DataSet();
        ds_resource.createHas();
        ds_resource.addHas(md_metadata);

//        serializeToXML(ds_resource);

        JAXBContext contextObj = JAXBContext.newInstance(DS_DataSet.class);
        Marshaller marshaller = contextObj.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(ds_resource, new FileOutputStream("ds_resource.xml"));

    }

    @SuppressWarnings("unused")
    private static void serializeToXML (DS_Resource ds_resource) {
        try {
            FileOutputStream fos = new FileOutputStream("ds_resource.xml");
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.setExceptionListener(e -> System.out.println("Exception: " + e.toString()));
            encoder.writeObject(ds_resource);
            encoder.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
