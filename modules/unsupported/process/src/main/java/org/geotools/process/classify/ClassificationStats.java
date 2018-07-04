/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.classify;

import java.util.Set;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Statistic;

/** A classification of data into classes, with a count and statistics for each class. */
public interface ClassificationStats {

    /** Number of classes. */
    int size();

    /** The statistics maintained for each class. */
    Set<Statistic> getStats();

    /** The value range for the specified class. */
    Range range(int i);

    /** The stat value for the specified class and statistic type. */
    Double value(int i, Statistic stat);

    /** The count of values for the speciifed class. */
    Long count(int i);
}
