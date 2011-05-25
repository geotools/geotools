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

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.util.InternationalString;

/**
 * Definition of one axis in a field for which we have some
 * measurements/observations/forecasts. The {@link Axis} data structure
 * describes the nature of each control variable for a certain {@link FieldType},
 * moreover it indicates as {@link Measurable}s the keys used to control each
 * Field subset.
 * 
 * <p>
 * Note that in order to comply with the WCS spec we need to have the
 * possibility to encode {@link Measurable}s as {@link String}s.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @param V Value being used to define this Axis
 * @param QA Quantity being represented by this Axis
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/range/Axis.java $
 */
public interface Axis<V,QA extends Quantity> {
    /**
     * Retrieves the {@link Axis} name
     * 
     * @return {@link org.opengis.feature.type.Name} of the {@link Axis}s
     */
    public Name getName();

    /**
     * Retrieves the description of the {@link Axis}
     * 
     * @return description of the {@link Axis}
     */
    public InternationalString getDescription();

    /**
     * Retrieves the list of keys for this {@link Axis}.
     * 
     * @return Retrieves the list of keys for this {@link Axis}.
     */
    public List<? extends Measure<V, QA>> getKeys();

    /**
     * Retrieves the number of keys for this {@link Axis}.
     * 
     * @return Retrieves the number of keys for this {@link Axis}.
     */
    public int getNumKeys();

    /**
     * Retrieves a specific key for this {@link Axis}.
     * 
     * @return Retrieves a specific key for this {@link Axis}.
     */
    public Measure<V, QA> getKey(final int keyIndex);

    /**
     * Retrieves the Unit of measure for the various keys of this axis.
     * 
     * In case this {@link Axis} is not made of measurable quantities
     * 
     * @return the Unit of measure for the various keys of this axis.
     */
    public Unit<QA> getUnitOfMeasure();

    /**
     * Retrieves the coordinate reference system for this {@link Axis}.
     * 
     * <p>
     * In case the coordinate reference system is present the Unit of measure
     * for its single coordinate axis should conform to the global {@link Unit}
     * for this {@link Axis}.
     * 
     * @return the coordinate reference system for this {@link Axis} or
     *         <code>null</code>, if no coordinate reference system is know
     *         or applicable.
     */
    public SingleCRS getCoordinateReferenceSystem();

}
