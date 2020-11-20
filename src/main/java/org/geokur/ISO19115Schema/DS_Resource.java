/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DS_Resource", namespace = "http://standards.iso.org/iso/19115/-3/mda/2.0")
public abstract class DS_Resource {

    // occurrence and obligation
    private final String[] elementName = {"has", "partOf"};
    private final int[] elementMax = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "has", namespace = "http://standards.iso.org/iso/19115/-3/mda/2.0")
    @XmlElementRef
    public List<MD_Metadata> has;

    @XmlElementWrapper(name = "partOf", namespace = "http://standards.iso.org/iso/19115/-3/mda/2.0")
    @XmlElementRef
    public List<DS_Aggregate> partOf;

    // variables for correct marshalling of specified classes
    public List<DS_DataSet> dataSet;

    public List<SV_Service> service;

    // methods
    public void addHas(MD_Metadata has) {
        if (this.has == null) {
            this.has = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.has.add(has);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPartOf(DS_Aggregate partOf) {
        if (this.partOf == null) {
            this.partOf = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.partOf.add(partOf);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
