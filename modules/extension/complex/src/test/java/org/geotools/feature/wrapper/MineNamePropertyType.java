/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.feature.wrapper;

/**
 * A sample implementation of FeatureWrapper.
 *
 * @author Adam Brown (Curtin University of Technology)
 */
@XSDMapping(namespace = "urn:org:example", separator = ":")
public class MineNamePropertyType extends FeatureWrapper {
    @XSDMapping(local = "MineName")
    public MineNameType MineName;

    @Override
    public String toString() {
        return String.format("MineName: %s", this.MineName);
    }
}
