/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "EX_GeographicExtent", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
public abstract class EX_GeographicExtent {

    // occurrence and obligation
    private final String[] elementName = {"extentTypeCode"};
    private final int[] elementMax = {1};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "extentTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<Boolean> extentTypeCode;

    // variables for correct marshalling of specified classes
    public List<EX_BoundingPolygon> boundingPolygon;

    public List<EX_GeographicBoundingBox> geographicBoundingBox;

    public List<EX_GeographicDescription> geographicDescription;

    // methods
    public void addExtentTypeCode(Boolean extentTypeCode) {
        if (this.extentTypeCode == null) {
            this.extentTypeCode = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.extentTypeCode.add(extentTypeCode);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
