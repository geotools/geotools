/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.gml3.GMLSchema;
import org.geotools.xs.XSSchema;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Constants and methods to create sample features for {@link SampleDataAccess}.
 * 
 * <p>
 * 
 * The features are similar to MappedFeature found in GeoSciML.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/sample-data-access
 *         /src/main/java/org/geotools/data/SampleDataAccessData.java $
 * @since 2.6
 */
@SuppressWarnings("serial")
public class SampleDataAccessData {

    /**
     * Namespace prefix for the sample feature type.
     */
    public static final String NAMESPACE_PREFIX = "gsml";

    /**
     * Namespace URI for the sample feature type.
     */
    public static final String NAMESPACE_URI = "http://www.example.org/sample-data-access/GeoSciML-lite";

    public static final Name GEOLOGICUNIT_TYPE_NAME = new NameImpl(NAMESPACE_URI, "GeologicUnit");

    public static final FeatureType GEOLOGICUNIT_TYPE = new FeatureTypeImpl(GEOLOGICUNIT_TYPE_NAME,
            Collections.<PropertyDescriptor> emptyList(), null, false, Collections
                    .<Filter> emptyList(), GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);

    public static final AttributeDescriptor SPECIFICATION_DESCRIPTOR = new AttributeDescriptorImpl(
            GMLSchema.FEATUREPROPERTYTYPE_TYPE, new NameImpl(NAMESPACE_URI, "specification"), 0, 1,
            false, null);

    // FIXME should be Geometry*
    public static final AttributeDescriptor SHAPE_DESCRIPTOR = new AttributeDescriptorImpl(
            GMLSchema.GEOMETRYPROPERTYTYPE_TYPE, new NameImpl(NAMESPACE_URI, "shape"), 1, 1, false,
            null);

    /**
     * The schema of the sample feature type.
     */
    private static final List<PropertyDescriptor> MAPPEDFEATURE_TYPE_SCHEMA = new ArrayList<PropertyDescriptor>() {
        {
            add(SPECIFICATION_DESCRIPTOR);
            add(SHAPE_DESCRIPTOR);
        }
    };

    /**
     * The qualified name of the sample feature type.
     */
    public static final Name MAPPEDFEATURE_TYPE_NAME = new NameImpl(NAMESPACE_URI, "MappedFeature");

    /**
     * The type of the sample feature.
     */
    public static final FeatureType MAPPEDFEATURE_TYPE = new FeatureTypeImpl(
            MAPPEDFEATURE_TYPE_NAME, MAPPEDFEATURE_TYPE_SCHEMA, null, false, Collections
                    .<Filter> emptyList(), GMLSchema.ABSTRACTFEATURETYPE_TYPE, null);

    /**
     * Create a sample feature from primitive components.
     * 
     * @param id
     *            feature id
     * @param bgsid
     *            an alternate id (not yet used)
     * @param description
     *            name of the feature
     * @param shape
     *            the shape of the feature
     * @return
     */
    public static Feature createMappedFeature(String id, String bgsid, final String description,
            final String specificationDescription, final Geometry shape) {

        final Collection<Property> specificationFeatureProperties = new ArrayList<Property>() {
            {
                add(new AttributeImpl(specificationDescription, new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml",
                                "description"), 0, 1, false, null), null));
            }
        };
        final Feature specificationFeature = new FeatureImpl(specificationFeatureProperties,
                GEOLOGICUNIT_TYPE, new FeatureIdImpl(id + ".spec"));

        Collection<Property> properties = new ArrayList<Property>() {
            {
                // FIXME: should be GMLSchema.STRINGORREFTYPE_TYPE, but that is a complexType with
                // simpleContent, not currently supported
                add(new AttributeImpl(description, new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml",
                                "description"), 0, 1, false, null), null));
                add(new ComplexAttributeImpl(new ArrayList<Property>() {
                    {
                        add(specificationFeature);
                    }
                }, SPECIFICATION_DESCRIPTOR, null));
                add(new AttributeImpl(shape, SHAPE_DESCRIPTOR, null)); // FIXME should be Geometry*
            }
        };
        return new FeatureImpl(properties, MAPPEDFEATURE_TYPE, new FeatureIdImpl(id));
    }

    /**
     * Create a list of two sample features.
     * 
     * @return list of sample features
     */
    public static List<Feature> createMappedFeatures() {
        return new ArrayList<Feature>() {
            {
                // Two sample MappedFeature from an old British Geological Survey test suite.
                // See also mappedPolygons.properties and GeoSciMLTest.java in app-schema module.
                add(createMappedFeature(
                        "mf1",
                        "651",
                        "GUNTHORPE FORMATION",
                        "Gunthorpe specification description",
                        readGeometry("POLYGON((-1.2 52.5,-1.2 52.6,-1.1 52.6,-1.1 52.5,-1.2 52.5))")));
                add(createMappedFeature(
                        "mf2",
                        "269",
                        "MERCIA MUDSTONE GROUP",
                        "Mercia specification description",
                        readGeometry("POLYGON((-1.3 52.5,-1.3 52.6,-1.2 52.6,-1.2 52.5,-1.3 52.5))")));
            }
        };
    }

    /**
     * Convert a shape in Well-Known Text into a {@link Geometry}.
     * 
     * @param wellKnownText
     *            a shape in Well-Known Text
     * @return the corresponding geometry
     */
    public static Geometry readGeometry(String wellKnownText) {
        try {
            return (new WKTReader()).read(wellKnownText);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
