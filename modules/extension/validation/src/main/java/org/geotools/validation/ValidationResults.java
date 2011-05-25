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


/**
 * Collates results of validation operations.
 * 
 * <p>
 * Following the lead the excelent design work in the JUnit testing framework
 * validation results are collected by a ValidationResults object. This
 * interface for the ValidationResults object also allows it to collect
 * warning information.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc
 *
 * @source $URL$
 */
public interface ValidationResults {
    /**
     * Called to configure ValidationResults according to the provided
     * FeatureValidation
     *
     * @param validation Provided FeatureValidation
     */
    public void setValidation(Validation validation);

    /**
     * Returns a validation error on against the provided feature, An optional
     * error message may be provided.  The validating web feature server will:
     * 
     * <ul>
     * <li>
     * Collate all errors for transaction result
     * </li>
     * <li>
     * Log all errors
     * </li>
     * <li>
     * Transaction may be canceled or have partial success depending on
     * transaction request
     * </li>
     * </ul>
     * 
     * Please note:<br>
     * The FeatureResults object has been provided with a Validation object
     * allowing it access to the user's name for the test, and the users
     * decription of the test. Use the message information only to provide
     * specific failure information.
     *
     * @param feature Feature found invalid
     * @param message Optional error message. Use a non null message to provide
     *        specific failure information.
     */
    public void error(SimpleFeature feature, String message);

    /**
     * Returns a validation warning against the provided feature. An optional
     * warning message may be provided  The validating web feature server
     * will:
     * 
     * <ul>
     * <li>
     * Collate all warnings for a comment in the transaction result
     * </li>
     * <li>
     * Log all warnings
     * </li>
     * </ul>
     * 
     * The FeatureResults object has been provided with a Validation object
     * allowing it access to the user's name for the test, and the users
     * decription of the test. Use the message information only to provide
     * specific failure information.
     *
     * @param feature Feature found to be in error
     * @param message Optional warning message. Use a non null message to
     *        provide specific warning information.
     */
    public void warning(SimpleFeature feature, String message);
}
