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

import java.util.List;
import org.geotools.api.feature.Feature;
import org.locationtech.jts.geom.Envelope;

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
