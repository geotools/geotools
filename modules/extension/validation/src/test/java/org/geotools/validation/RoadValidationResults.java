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

import java.util.ArrayList;

import org.opengis.feature.simple.SimpleFeature;

/**
 * RoadValidationResults purpose.
 * <p>
 * Description of RoadValidationResults ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * RoadValidationResults x = new RoadValidationResults(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class RoadValidationResults implements ValidationResults {

	public ArrayList validationList;
	public ArrayList failedFeatures;
	public ArrayList warningFeatures;
	public ArrayList failureMessages;
	public ArrayList warningMessages;


	/**
	 * RoadValidationResults constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public RoadValidationResults() {
		validationList = new ArrayList();
		failedFeatures = new ArrayList();
		warningFeatures = new ArrayList();
		failureMessages = new ArrayList();
		warningMessages = new ArrayList();
	}

	/**
	 * Override setValidation.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.ValidationResults#setValidation(org.geotools.validation.Validation)
	 * 
	 * @param validation
	 */
	public void setValidation(Validation validation) {
		validationList.add(validation);
	}

	/**
	 * Override error.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.ValidationResults#error(org.geotools.feature.Feature, java.lang.String)
	 * 
	 * @param feature
	 * @param message
	 */
	public void error(SimpleFeature feature, String message) {
		failedFeatures.add(feature);
		failureMessages.add(feature.getID() + ": " + message);
	}

	/**
	 * Override warning.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.ValidationResults#warning(org.geotools.feature.Feature, java.lang.String)
	 * 
	 * @param feature
	 * @param message
	 */
	public void warning(SimpleFeature feature, String message) {
		warningFeatures.add(feature);
		warningMessages.add(feature.getID() + ": " + message);
	}

}
