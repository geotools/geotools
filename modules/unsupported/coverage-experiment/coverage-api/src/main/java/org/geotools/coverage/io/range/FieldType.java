/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.range;

import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * A {@link net.opengis.wcs11.FieldType} describes a
 * measure/observation/forecast of a certain quantity. A {@link FieldType} may
 * be a scalar (numeric or text) value, such as population density, or a vector
 * of many similar values, such as incomes by race, or radiance by wavelength.
 * 
 * <p>
 * A {@link FieldType} has an associated quantity from the JScience project
 * since the goal of a {@link FieldType} is to describe a {@link Quantity}.
 * Note that I am referring to quantity in the broader term here. As an instance
 * a {@link FieldType} could describe the bands of a synthetic RGB image. Now,
 * there might not be a real physical quantity (like Temperature or Pressure)
 * associated to such a quantity, but still we want to be able to capture
 * somehow the concept of digital number as the represented quantity as well as
 * the concept of the bands index or textual representation for the bands.
 * 
 * <p>
 * Note that in our proposal a {@link FieldType} shall always contain at least
 * one {@link Axis}.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/range/FieldType.java $
 */
public interface FieldType {

    /**
     * Get the {@link FieldType} {@link org.opengis.feature.type.Name}
     * 
     * @return {@link org.opengis.feature.type.Name} of the {@link FieldType}
     */
    public Name getName();

    /**
     * Get the description of the {@link FieldType}
     * 
     * @return description of the {@link FieldType}
     */
    public InternationalString getDescription();

    /**
     * {@link List} of all the axes of the {@link FieldType}
     * 
     * @return a {@link List} of all the {@link Axis} instances for this
     *         {@link FieldType}
     */
    public List<Axis<?,?>> getAxes();

    /**
     * {@link List} of all the {@link Axis} instances
     * {@link org.opengis.feature.type.Name}s.
     * 
     * @return a {@link List} of all the {@link Axis} instances
     *         {@link org.opengis.feature.type.Name}s.
     */
    public List<Name> getAxesNames();

    /**
     * Get the Axis by name
     * 
     * @param name
     *                name of the Axis
     * @return Axis instance or null if not found
     */
    public Axis<?,?> getAxis(Name name);

    /**
     * List the SampleDimensions of the measure.
     * 
     * @return Set of {@link SampleDimension} instances
     */
    public Set<SampleDimension> getSampleDimensions();

    /**
     * Look up the SampleDimension by key (as described by Axis)
     * 
     * @param key
     *                key of the SampleDimension
     * @return {@link SampleDimension} instance or null if not found
     */
    public SampleDimension getSampleDimension(Measure<?,?> key);

    /**
     * Retrieves the Unit of measure for the values described by this field.
     *  
     * <p>
     * In case this {@link FieldType} is not made of measurable quantities we
     * return <code>null</code>
     * 
     * @return the Unit of measure for the values described by this field or
     *         <code>null</code> in case this {@link FieldType} is not made of
     *         measurable quantities
     */
    public Unit<?> getUnitOfMeasure();

}
