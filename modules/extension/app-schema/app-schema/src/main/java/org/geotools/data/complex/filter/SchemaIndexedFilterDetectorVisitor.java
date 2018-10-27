/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.filter;

import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.data.complex.FeatureTypeMapping;
import org.opengis.filter.Filter;

/**
 * Detects which AND/OR filter (BinaryLogicOperator) is the parent operator of indexed filter(s).
 * Then collects the full-indexed subfilter(s). Xpath Filter implementation
 *
 * @author Fernando Miño - Geosolutions
 */
public class SchemaIndexedFilterDetectorVisitor extends IndexedFilterDetectorVisitor {

    public SchemaIndexedFilterDetectorVisitor(FeatureTypeMapping mapping) {
        super(mapping);
    }

    @Override
    protected boolean isFullyIndexed(Filter filter) {
        return IndexQueryUtils.checkAllPropertiesIndexed(
                IndexQueryUtils.getAttributesOnFilter(filter), mapping);
    }
}
