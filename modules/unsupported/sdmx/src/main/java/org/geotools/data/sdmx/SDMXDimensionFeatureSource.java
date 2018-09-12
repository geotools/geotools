/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.data.sdmx;

import it.bancaditalia.oss.sdmx.api.Dataflow;
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Source of features for SDMX Dimensions
 *
 * @author lmorandini
 */
public class SDMXDimensionFeatureSource extends SDMXFeatureSource {

    protected CoordinateReferenceSystem crs;

    /**
     * Inner class used to build the SDMX query expression
     *
     * @author lmorandini
     */
    protected final class VisitFilter extends DefaultFilterVisitor {

        public Object visit(PropertyIsEqualTo expr, Object data) {
            Map<String, String> map = (Map<String, String>) data;

            if (SDMXDataStore.DIMENSIONS_EXPR.equalsIgnoreCase(expr.getExpression1().toString())
                    && SDMXDataStore.DIMENSIONS_EXPR_ALL.equalsIgnoreCase(
                            expr.getExpression2().toString())) {
                map.put(SDMXDataStore.DIMENSIONS_EXPR, SDMXDataStore.DIMENSIONS_EXPR_ALL);
                return map;
            }

            if (SDMXDataStore.DIMENSIONS_EXPR.equalsIgnoreCase(expr.getExpression1().toString())
                    && !SDMXDataStore.DIMENSIONS_EXPR_ALL.equalsIgnoreCase(
                            expr.getExpression2().toString())) {
                map.put(
                        SDMXDataStore.DIMENSIONS_EXPR,
                        expr.getExpression2().toString().toUpperCase());
                return map;
            }

            throw new IllegalFilterException(
                    "Illegal filter for querying an SDMX dimension or list of dimensions");
        }
    }

    /**
     * Constructor
     *
     * @param entry ContentEntry of the feature type
     * @param dataflowIn SDMX Dataflow the query works on
     * @param query Query that defines the feature source
     * @throws IOException
     * @throws FactoryException
     */
    public SDMXDimensionFeatureSource(ContentEntry entry, Dataflow dataflowIn, Query query)
            throws IOException, FactoryException {

        super(entry, dataflowIn, query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {

        // Builds the feature type
        SimpleFeatureTypeBuilder builder = this.buildBuilder();
        builder.add(SDMXDataStore.CODE_KEY, java.lang.String.class);
        builder.add(SDMXDataStore.DESCRIPTION_KEY, java.lang.String.class);
        this.schema = builder.buildFeatureType();
        return this.schema;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {

        if (this.schema == null) {
            this.buildFeatureType();
        }

        Map<String, String> expressions =
                (Map<String, String>)
                        query.getFilter()
                                .accept(
                                        new SDMXDimensionFeatureSource.VisitFilter(),
                                        new HashMap<String, String>());

        try {
            return new SDMXDimensionFeatureReader(
                    this.dataStore.getSDMXClient(),
                    this.schema,
                    this.dataflow,
                    this.dataflowStructure,
                    expressions.get(SDMXDataStore.DIMENSIONS_EXPR),
                    this.dataStore.getLogger());
        } catch (SdmxException e) {
            // FIXME: re-hash the exception into an IOEXception
            this.dataStore.getLogger().log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }
    }
}
