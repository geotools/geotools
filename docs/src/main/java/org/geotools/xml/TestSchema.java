/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.xml;

import java.util.Date;
import java.util.List;
import org.geotools.api.feature.Feature;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;

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
    public static interface TestFeatureCollectionType extends GML2Schema.AbstractFeatureCollectionType {}

    public static class TestFeatureCollection implements TestFeatureCollectionType {

        public List<Feature> featureMemeber() {
            return null;
        }
    }
}
