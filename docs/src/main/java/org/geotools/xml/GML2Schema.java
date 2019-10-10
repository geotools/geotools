/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.xml;

import java.util.List;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.Feature;

/**
 * The following is a placeholder simply to make a UML Diagram.
 *
 * @author jody
 */
public class GML2Schema {
    /**
     * An abstract feature provides a set of common properties. A concrete feature type must derive
     * from this type and specify additional properties in an application schema. A feature may
     * optionally possess an identifying attribute ('fid').
     */
    public static interface AbstractFeatureType {
        /** optional */
        public String description();

        /** optional */
        public String name();

        /** optional */
        public Envelope boundedBy();
    }
    /** A feature collection contains zero or more featureMember elements. */
    public static interface AbstractFeatureCollectionType {
        List<Feature> featureMemeber();
    }
}
