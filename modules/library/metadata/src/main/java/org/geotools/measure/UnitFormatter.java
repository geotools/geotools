/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import java.io.IOException;
import java.text.ParsePosition;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;

/**
 * An interface that is similar to {@link javax.measure.format.UnitFormat} but elides mutating
 * methods.
 *
 * <p>It is used to protect global or shared UnitFormat instances from being changed inadvertently.
 */
public interface UnitFormatter {

    Appendable format(Unit<?> unit, Appendable appendable) throws IOException;

    String format(Unit<?> unit);

    Unit<?> parse(CharSequence csq, ParsePosition pos)
            throws IllegalArgumentException, MeasurementParseException;

    Unit<?> parse(CharSequence csq) throws MeasurementParseException;
}
