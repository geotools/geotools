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

import java.util.Date;
import java.util.List;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Feature;

/**
 * The following is a placeholder simply to make a UML Diagram.
 *
 * @author jody
 */
public class TestSchema {
    /**
     * An abstract feature provides a set of common properties. A concrete feature type must derive
     * from this type and specify additional properties in an application schema. A feature may
     * optionally possess an identifying attribute ('fid').
     */
    public static interface TestFeatureType extends GML2Schema.AbstractFeatureType {
        public Point pointProperty();

        public int count();

        public Date date();
    }

    public static class TestFeature implements TestFeatureType {

        public String description() {
            return null;
        }

        public String name() {
            return null;
        }

        public Envelope boundedBy() {
            return null;
        }

        public Point pointProperty() {
            return null;
        }

        public int count() {
            return 0;
        }

        public Date date() {
            return null;
        }
    }

    /** A feature collection contains zero or more featureMember elements. */
    public static interface TestFeatureCollectionType
            extends GML2Schema.AbstractFeatureCollectionType {}

    public static class TestFeatureCollection implements TestFeatureCollectionType {

        public List<Feature> featureMemeber() {
            return null;
        }
    }
}
