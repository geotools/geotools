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
package org.geotools.validation.relate;

import java.util.ArrayList;
import java.util.List;

import org.geotools.validation.Validation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @source $URL$
 */
final class TempFeatureResults implements ValidationResults {
	Validation trial;
	List error = new ArrayList();
	List warning = new ArrayList();
	public void setValidation(Validation validation) {
		trial = validation;									
	}
	public void error(SimpleFeature feature, String message) {
		String where = feature != null ? feature.getID() : "all"; 
		error.add( where + ":"+ message );
		System.err.println( where + ":"+ message );
	}
	public void warning(SimpleFeature feature, String message) {
		String where = feature != null ? feature.getID() : "all";
		warning.add( where + ":"+ message );
		System.out.println( where + ":"+ message );
	}
}
