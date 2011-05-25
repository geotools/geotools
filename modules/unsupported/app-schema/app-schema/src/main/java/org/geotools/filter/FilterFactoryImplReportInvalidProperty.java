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

package org.geotools.filter;

import org.opengis.filter.expression.PropertyName;

/**
 * 
 * @author Niels Charlier, Curtin University of Technology
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/app-schema/app-schema/src/main/java/org/geotools/filter/FilterFactoryImplReportInvalidProperty.java $
 */
public class FilterFactoryImplReportInvalidProperty extends FilterFactoryImpl {
    
    // @Override
    public PropertyName property(String name) {
        AttributeExpressionImpl att = new AttributeExpressionImpl(name);
        att.setLenient(false);
        return att;
    }

}
