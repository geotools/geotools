/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson;

import org.geotools.filter.text.commons.ICompiler;
import org.opengis.filter.FilterFactory2;

final class CQLJsonCompilerFactory {

    /* (non-Javadoc)
     * @see org.geotools.filter.text.commons.AbstractCompilerFactory#createCompiler(java.lang.String, org.opengis.filter.FilterFactory)
     */
    protected ICompiler createCompiler(final String predicate, final FilterFactory2 filterFactory) {

        return new CQLJsonCompiler(predicate, filterFactory);
    }
}
