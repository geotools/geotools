package org.geotools.xml;

import java.util.List;

import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Envelope;

/**
 * The following is a placeholder simply to make a UML Diagram.
 * @author jody
 *
 */
public class GML2Schema {
    /**
     * An abstract feature provides a set of common properties. A concrete feature type must derive from
     * this type and specify additional properties in an application schema. A feature may optionally
     * possess an identifying attribute ('fid').
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