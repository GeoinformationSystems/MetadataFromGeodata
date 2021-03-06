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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "CI_Citation", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public class CI_Citation {

    // occurrence and obligation
    private final String[] elementName = {"title", "alternateTitle", "date", "edition", "editionDate", "identifier", "citedResponsibleParty", "presentationForm", "series", "otherCitationDetails", "ISBN", "ISSN", "onlineResource", "graphic"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "title", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> title;

    @XmlElement(name = "alternateTitle", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> alternateTitle;

    @XmlElementWrapper(name = "date", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_Date> date;

    @XmlElement(name = "edition", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> edition;

    @XmlElement(name = "editionDate", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> editionDate;

    @XmlElementWrapper(name = "identifier", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<MD_Identifier> identifier;

    @XmlElementWrapper(name = "citedResponsibleParty", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_Responsibility> citedResponsibleParty;

    @XmlElementWrapper(name = "presentationForm", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_PresentationFormCode> presentationForm;

    @XmlElementWrapper(name = "series", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_Series> series;

    @XmlElement(name = "otherCitationDetails", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> otherCitationDetails;

    @XmlElement(name = "ISBN", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> ISBN;

    @XmlElement(name = "ISSN", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> ISSN;

    @XmlElementWrapper(name = "onlineResource", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_OnlineResource> onlineResource;

    @XmlElementWrapper(name = "graphic", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<MD_BrowseGraphic> graphic;

    // methods
    public CI_Citation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.CI_Citation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.CI_Citation);
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

    public void addTitle(String title) {
        if (this.title == null) {
            this.title = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.title.add(title);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAlternateTitle(String alternateTitle) {
        if (this.alternateTitle == null) {
            this.alternateTitle = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.alternateTitle.add(alternateTitle);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDate(CI_Date date) {
        if (this.date == null) {
            this.date = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.date.add(date);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEdition(String edition) {
        if (this.edition == null) {
            this.edition = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.edition.add(edition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEditionDate(String editionDate) {
        if (this.editionDate == null) {
            this.editionDate = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.editionDate.add(editionDate);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdentifier(MD_Identifier identifier) {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identifier.add(identifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCitedResponsibleParty(CI_Responsibility citedResponsibleParty) {
        if (this.citedResponsibleParty == null) {
            this.citedResponsibleParty = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.citedResponsibleParty.add(citedResponsibleParty);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPresentationForm(CI_PresentationFormCode presentationForm) {
        if (this.presentationForm == null) {
            this.presentationForm = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.presentationForm.add(presentationForm);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSeries(CI_Series series) {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }

        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.series.add(series);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherCitationDetails(String otherCitationDetails) {
        if (this.otherCitationDetails == null) {
            this.otherCitationDetails = new ArrayList<>();
        }

        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherCitationDetails.add(otherCitationDetails);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addISBN(String ISBN) {
        if (this.ISBN == null) {
            this.ISBN = new ArrayList<>();
        }

        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.ISBN.add(ISBN);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addISSN(String ISSN) {
        if (this.ISSN == null) {
            this.ISSN = new ArrayList<>();
        }

        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.ISSN.add(ISSN);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOnlineResource(CI_OnlineResource onlineResource) {
        if (this.onlineResource == null) {
            this.onlineResource = new ArrayList<>();
        }

        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.onlineResource.add(onlineResource);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGraphic(MD_BrowseGraphic graphic) {
        if (this.graphic == null) {
            this.graphic = new ArrayList<>();
        }

        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.graphic.add(graphic);
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
