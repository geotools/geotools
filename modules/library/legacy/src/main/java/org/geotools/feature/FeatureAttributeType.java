/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import org.opengis.filter.Filter;


/**
 * A FeatureType aware Feature AttributeType.
 *
 * <p>
 * While we could use a plain AttributeType to capture a Feature instance we
 * would miss out one one important aspect: the schema.
 * </p>
 *
 * <p>
 * By definition the schema of a Feature is not defined by java interface
 * alone, this interface allows access the the assocaited FeatureType.
 * </p>
 *
 * <p>
 * Suggestion: we can look at having this class extend ListFeatureType and
 * exactly specifying how delegation to the getSchema() should occur. (the
 * alternative is to make FeatureType extend ListAttributeType). Something
 * should be done as they have 90% the same API.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 *
 * @since 2.1.M5
 * @source $URL$
 */
public interface FeatureAttributeType extends AttributeType, PrimativeAttributeType {
    /**
     * Method should delegate responsibility to a FeatureFactory.
     *
     * @see org.geotools.feature.AttributeType#createDefaultValue()
     */
    Object createDefaultValue();

    /**
     * Method should return getSchema().duplicate( feature )
     *
     * <p>
     * Exampe implementation:
     * <pre><code>
     *  <b>return</b> getSchema().duplicate( (Feature) getValue() );
     * </code></pre>
     * It is understood that FeatureType.duplicate
     * </p>
     *
     * @see org.geotools.feature.AttributeType#duplicate(java.lang.Object)
     */
    Object duplicate(Object src) throws IllegalAttributeException;

    /**
     * Method must return type Feature.class.
     *
     * <p>
     * Exampe implementation:
     * <pre><code>
     *  <b>return</b> Feature.class;
     * </code></pre>
     * </p>
     *
     * @see org.geotools.feature.AttributeType#getBinding()
     */
    Class getBinding();

    /**
     * Retrieve the schema for this FeatureType
     *
     * @return CS_CoordinateSystem for this GeometryAttributeType
     */
    public FeatureType getSchema();

    /**
     * Must return <code>false</code>.
     *
     * @return DOCUMENT ME!
     *
     * @see org.geotools.feature.AttributeType#isGeometry()
     * @deprecated repalce with: <code>type instanceof
     *             GeometryAttributeType</code>
     */
    boolean isGeometry();

    /**
     * Filter must indicate value is required to be a member of getSchema().
     *
     * <p>
     * TODO: We need a Filter code snipit describing how to enforce the
     * indicated relationship.
     * </p>
     *
     * @see org.geotools.feature.AttributeType#getRestriction()
     */
    Filter getRestriction();
}
