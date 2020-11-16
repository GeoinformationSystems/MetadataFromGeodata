/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.generateMetadata;

import org.locationtech.jts.geom.Point;

import java.util.List;

public final class FeatureDescriptor {
    // descriptor of features in polygons

    private final int number;
    private final Point centroidWGS84;
    private final Point centroidUTM;
    private final double areaKm2WGS84;
    private final double areaKm2UTM;
    private final List<String> attributeNames;
    private final List<String> attributeValues;

    public FeatureDescriptor(int number, Point centroidWGS84, Point centroidUTM, double areaKm2WGS84, double areaKm2UTM,
                             List<String> attributeNames, List<String> attributeValues) {
        this.number = number;
        this.centroidWGS84 = centroidWGS84;
        this.centroidUTM = centroidUTM;
        this.areaKm2WGS84 = areaKm2WGS84;
        this.areaKm2UTM = areaKm2UTM;
        this.attributeNames = attributeNames;
        this.attributeValues = attributeValues;
    }

    public int getNumber() {
        return number;
    }

    public Point getCentroidWGS84() {
        return centroidWGS84;
    }

    public Point getCentroidUTM() {
        return centroidUTM;
    }

    public double getAreaKm2WGS84() {
        return areaKm2WGS84;
    }

    public double getAreaKm2UTM() {
        return areaKm2UTM;
    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public List<String> getAttributeValues() {
        return attributeValues;
    }
}
