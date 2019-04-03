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

// This demonstrates how to extend FeatureWrapper to get a strongly-typed object to represent a
// feature.
@XSDMapping(namespace = "urn:org:example", separator = ":")
public class MineType extends FeatureWrapper {
    @XSDMapping(local = "MineNamePropertyType")
    public ArrayList<MineNamePropertyType>
            MineNameProperties; // ArrayLists are allowed for multi-valued types.

    // You can use path to allow a lower-level value to be set in the current class. This might be
    // useful if you don't want to have
    // to create the whole class tree.
    @XSDMapping(path = "MineNamePropertyType/MineName/MineNameType", local = "mineName")
    public String firstName;

    public String getPreferredName() { // You can add extra methods.
        for (MineNamePropertyType mineNameProperty : MineNameProperties) {
            if (mineNameProperty.MineName.isPreferred) {
                return mineNameProperty.MineName.mineName;
            }
        }

        return "";
    }
}
