/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.commons;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLCompiler;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQLCompiler;
import org.geotools.util.factory.Hints;
import org.opengis.filter.FilterFactory;

/**
 * Creates the compiler required for the specific language.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class CompilerFactory {

    private CompilerFactory() {}

    /**
     * Initializes and create the new compiler
     *
     * @return CQLCompiler
     */
    public static ICompiler makeCompiler(
            final Language language, final String predicate, final FilterFactory filterFactory)
            throws CQLException {

        FilterFactory ff = filterFactory;

        if (filterFactory == null) {
            ff = CommonFactoryFinder.getFilterFactory((Hints) null);
        }
        ICompiler compiler;
        if (language == Language.ECQL) {
            compiler = new ECQLCompiler(predicate, ff);
        } else {
            compiler = new CQLCompiler(predicate, ff);
        }
        return compiler;
    }
}
