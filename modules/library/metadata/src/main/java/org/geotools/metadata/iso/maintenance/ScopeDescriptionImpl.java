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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.maintenance;

import java.util.Set;
import org.opengis.metadata.maintenance.ScopeDescription;
import org.geotools.metadata.iso.MetadataEntity;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;


/**
 * Description of the class of information covered by the information.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ScopeDescriptionImpl extends MetadataEntity implements ScopeDescription {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5671299759930976286L;

    /**
     * The attributes to which the information applies.
     */
    private Set<AttributeType> attributes;

    /**
     * The features to which the information applies.
     */
    private Set<FeatureType> features;

    /**
     * The feature instances to which the information applies.
     */
    private Set<FeatureType> featureInstances;

    /**
     * The attribute instances to which the information applies.
     */
    private Set<AttributeType> attributeInstances;

    /**
     * Dataset to which the information applies.
     */
    private String dataset;

    /**
     * Class of information that does not fall into the other categories to
     * which the information applies.
     */
    private String other;

    /**
     * Creates an initially empty scope description.
     */
    public ScopeDescriptionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ScopeDescriptionImpl(final ScopeDescription source) {
        super(source);
    }

    /**
     * Returns the attributes to which the information applies.
     */
    public Set<AttributeType> getAttributes() {
        return attributes = nonNullSet(attributes, AttributeType.class);
    }

    /**
     * Set the attributes to which the information applies.
     *
     * @since 2.5
     */
    public synchronized void setAttributes(final Set<? extends AttributeType> newValues) {
        attributes = (Set<AttributeType>) copyCollection(newValues, attributes, AttributeType.class);
    }

    /**
     * Returns the features to which the information applies.
     */
    public Set<FeatureType> getFeatures() {
        return features = nonNullSet(features, FeatureType.class);
    }

    /**
     * Set the features to which the information applies.
     *
     * @since 2.5
     */
    public synchronized void setFeatures(final Set<? extends FeatureType> newValues) {
        features = (Set<FeatureType>) copyCollection(newValues, features, FeatureType.class);
    }

    /**
     * Returns the feature instances to which the information applies.
     */
    public Set<FeatureType> getFeatureInstances() {
        return featureInstances = nonNullSet(featureInstances, FeatureType.class);
    }

    /**
     * Set the feature instances to which the information applies.
     *
     * @since 2.5
     */
    public synchronized void setFeatureInstances(final Set<? extends FeatureType> newValues) {
        featureInstances = (Set<FeatureType>) copyCollection(newValues, featureInstances, FeatureType.class);
    }

    /**
     * Returns the attribute instances to which the information applies.
     *
     * @since 2.4
     */
    public Set<AttributeType> getAttributeInstances() {
        return attributeInstances = nonNullSet(attributeInstances, AttributeType.class);
    }

    /**
     * Set the attribute instances to which the information applies.
     *
     * @since 2.5
     */
    public synchronized void setAttributeInstances(final Set<? extends AttributeType> newValues) {
        attributeInstances = (Set<AttributeType>) copyCollection(newValues, attributeInstances, AttributeType.class);
    }

    /**
     * Returns the dataset to which the information applies.
     *
     * @since 2.4
     */
    public String getDataset() {
        return dataset;
    }

    /**
     * Set the dataset to which the information applies.
     *
     * @since 2.4
     */
    public synchronized void setDataset(final String newValue) {
        checkWritePermission();
        dataset = newValue;
    }

    /**
     * Returns the class of information that does not fall into the other categories to
     * which the information applies.
     *
     * @since 2.4
     */
    public String getOther() {
        return other;
    }

    /**
     * Set the class of information that does not fall into the other categories to
     * which the information applies.
     *
     * @since 2.4
     */
    public synchronized void setOther(final String newValue) {
        checkWritePermission();
        other = newValue;
    }
}
