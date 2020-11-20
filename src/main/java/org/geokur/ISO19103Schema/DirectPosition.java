/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19103Schema;

import org.geokur.ISO19115Schema.MD_Identifier;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DirectPosition")
public class DirectPosition {

    // occurrence and obligation
    private final String[] elementName = {"coordinate", "dimension", "coordinateReferenceSystem"};
    private final int[] elementMax = {1, 1, 1};
    private final boolean[] elementObligation = {false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "coordinate")
    public List<Coordinate> coordinate;

    @XmlElement(name = "dimension")
    public List<Integer> dimension;

    @XmlElementWrapper(name = "coordinateReferenceSystem")
    @XmlElementRef
    public List<MD_Identifier> coordinateReferenceSystem;

    // methods
    public DirectPosition() {}

    public void addCoordinate(Coordinate coordinate) {
        if (this.coordinate == null) {
            this.coordinate = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coordinate.add(coordinate);

                // derive number of dimensions
                if (this.dimension == null) {
                    // dimension not yet instantiated
                    this.dimension = new ArrayList<>();
                    this.dimension.add(coordinate.getDimension());
                }
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCoordinateReferenceSystem(MD_Identifier coordinateReferenceSystem) {
        if (this.coordinateReferenceSystem == null) {
            this.coordinateReferenceSystem = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coordinateReferenceSystem.add(coordinateReferenceSystem);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finalizeClass() {
        for (int i = 0; i < elementName.length; i++) {
            try {
                List<?> tempList = (List<?>) this.getClass().getField(elementName[i]).get(this);
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
