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

import java.util.ArrayList;
import org.geotools.data.complex.feature.wrapper.FeatureWrapper;
import org.geotools.data.complex.feature.wrapper.XSDMapping;

// This is just like MineType but it's modified to cater for some tests. See
// FeatureWrapperTest.java.

// This demonstrates how to extend FeatureWrapper to get a strongly-typed object to represent a
// feature.
@XSDMapping(namespace = "urn:org:example", separator = ":")
public class MineType2 extends FeatureWrapper {
    @XSDMapping(local = "MineNamePropertyType")
    public ArrayList<MineNamePropertyType>
            MineNameProperties; // ArrayLists are allowed for multivalued types.
}
