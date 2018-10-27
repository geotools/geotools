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

import org.geotools.data.complex.feature.wrapper.FeatureWrapper;
import org.geotools.data.complex.feature.wrapper.XSDMapping;

@XSDMapping(namespace = "urn:org:example", separator = ":")
public class MineNameType extends FeatureWrapper {
    @XSDMapping(local = "isPreferred")
    public boolean isPreferred;

    @XSDMapping(local = "mineName")
    public String mineName;

    @Override
    public String toString() {
        return String.format("isPreferred: %s, mineName: %s", this.isPreferred, this.mineName);
    }
}
