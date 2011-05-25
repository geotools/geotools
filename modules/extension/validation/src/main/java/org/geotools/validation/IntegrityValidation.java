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

import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;


/**
 * Used to check geospatial information for integrity.
 * 
 * <p>
 * Each ValidationPlugIn is very specific in nature: it performs one test
 * extermly well.  This simplifies design decisions, documenation
 * configuration and use.
 * </p>
 * 
 * <p>
 * Following the lead the excelent design work in the JUnit testing framework
 * validation results are collected by a ValidationResults object. This
 * interface for the ValidationResults object also allows it to collect
 * warning information.
 * </p>
 * 
 * <p>
 * The PlugIn is also required to supply some metadata to aid in its
 * deployment, scripting, logging and execution and error recovery:
 * 
 * <ul>
 * <li>
 * name: user's name of validation test
 * </li>
 * <li>
 * description: user's description of validation test
 * </li>
 * <li>
 * priority: used to schedule validation test
 * </li>
 * <li>
 * typeNames: used to connect validaiton test to transaction opperation
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Uses FeatureResults to allow environment to gather error/warning information
 * as required (transaction XML document, JTable, logging system, etc...)
 * </li>
 * <li>
 * Primiarly used as part of processing the Transaction opperation of a Web
 * Feature Server. Used to ensure that the DataStore is consistent before
 * commiting a Transaction.
 * </li>
 * </ul>
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public interface IntegrityValidation extends Validation {
    /**
     * Used to check features against this validation rule.
     * 
     * <p>
     * The layers Map is still under developement, current thinking involves
     * storing a SimpleFeatureSource of the correct typeName requested by
     * getTypeNames(), using the current geotools2 Transaction as the
     * opperation being validated.
     * </p>
     * 
     * <p>
     * We may need to extend this information to provide:
     * 
     * <ul>
     * <li>
     * FeatureTypeMetaData: we may with to configure against metadata
     * </li>
     * <li>
     * Networks: networks are expensive to produce, we may be able to have the
     * ValidationProcessor cache a network for later.
     * </li>
     * </ul>
     * </p>
     *
     * @param layers Map of SimpleFeatureSource by "dataStoreID:typeName"
     * @param envelope The bounding box that encloses the unvalidated data
     * @param results Used to coallate results information
     *
     * @return <code>true</code> if all the features pass this test.
     */
    public boolean validate(Map layers, ReferencedEnvelope envelope,
        ValidationResults results) throws Exception;
}
