/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DerivedCRS")
public abstract class DerivedCRS extends SingleCRS {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "coordinateSystem", "datum", "datumEnsemble",
            "baseCRS", "derivingConversion"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1,
            1, 1};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            true, false, false,
            true, true};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "baseCRS")
    @XmlElementRef
    public List<SingleCRS> baseCRS;

    @XmlElementWrapper(name = "derivingConversion")
    @XmlElementRef
    public List<Conversion> derivingConversion;

    // methods
    public void addBaseCRS(SingleCRS baseCRS) {
        if (this.baseCRS == null) {
            this.baseCRS = new ArrayList<>();
        }

        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.baseCRS.add(baseCRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDerivingConversion(Conversion derivingConversion) {
        if (this.derivingConversion == null) {
            this.derivingConversion = new ArrayList<>();
        }

        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.derivingConversion.add(derivingConversion);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
