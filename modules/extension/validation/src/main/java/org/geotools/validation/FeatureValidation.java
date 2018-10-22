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
package org.geotools.validation;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Defined a per Feature validation test.
 *
 * <p>Each ValidationPlugIn is very specific in nature: it performs one test extermly well. This
 * simplifies design decisions, documenation configuration and use.
 *
 * <p>Following the lead the excelent design work in the JUnit testing framework validation results
 * are collected by a ValidationResults object. This interface for the ValidationResults object also
 * allows it to collect warning information.
 *
 * <p>The PlugIn is also required to supply some metadata to aid in its deployment, scripting,
 * logging and execution and error recovery:
 *
 * <ul>
 *   <li>name: user's name of validation test
 *   <li>description: user's description of validation test
 *   <li>priority: used to schedule validation test
 *   <li>typeNames: used to connect validaiton test to transaction opperation
 * </ul>
 *
 * <p>Capabilities:
 *
 * <ul>
 *   <li>Uses FeatureResults to allow environment to gather error/warning information as required
 *       (transaction XML document, JTable, logging system, etc...)
 *   <li>Primiarly used as part of processing an Insert Element in the Transaction opperation of a
 *       Web Feature Server. (Allows us to fail a Feature without bothering the Database)
 * </ul>
 *
 * <p>Example Use (feature: id=1, name="foo", geom=linestring):
 *
 * <pre><code>
 * RangeFeatureValidation test = new RangeFeatureValidation();
 *
 * results.setValidation( test );
 * test.setMin(0);
 * test.validate( feature, featureType, results ); // true
 * test.setMin(2);
 * test.validate( feature, featureType, results ); // false
 * </code></pre>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @version $Id$
 */
public interface FeatureValidation extends Validation {
    /**
     * Used to check features against this validation rule.
     *
     * @param feature Feature to be Validated
     * @param type FeatureTypeInfo schema of feature
     * @param results coallate results information
     * @return True if feature passes this test.
     */
    public boolean validate(
            SimpleFeature feature, SimpleFeatureType type, ValidationResults results)
            throws Exception;
}
