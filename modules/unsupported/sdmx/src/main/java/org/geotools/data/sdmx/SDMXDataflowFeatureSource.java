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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Source of features for SDMX Dataflows
 *
 * @author lmorandini
 */
public class SDMXDataflowFeatureSource extends SDMXFeatureSource {

    // FIXME:
    protected CoordinateReferenceSystem crs;

    /**
     * Inner class used to build the SDMX query expression
     *
     * @author lmorandini
     */
    protected final class VisitFilter extends DefaultFilterVisitor {

        public Object visit(Or expr, Object data) {
            Map<String, String> map = (Map<String, String>) data;
            List<String> ids = new ArrayList<String>();
            List<String> property = new ArrayList<String>();

            expr.getChildren()
                    .forEach(
                            eqExpr -> {
                                property.add(
                                        ((PropertyIsEqualTo) (eqExpr)).getExpression1().toString());
                                ids.add(((PropertyIsEqualTo) (eqExpr)).getExpression2().toString());
                            });

            map.put(property.get(0), String.join(SDMXDataStore.OR_EXP, ((List<String>) ids)));
            return map;
        }

        public Object visit(PropertyIsEqualTo expr, Object data) {
            Map<String, String> map = (Map<String, String>) data;

            map.put(expr.getExpression1().toString(), expr.getExpression2().toString());
            return map;
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
    public SDMXDataflowFeatureSource(ContentEntry entry, Dataflow dataflowIn, Query query)
            throws IOException, FactoryException {

        super(entry, dataflowIn, query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {

        // Builds the feature type
        SimpleFeatureTypeBuilder builder = this.buildBuilder();
        builder.add(SDMXDataStore.TIME_KEY, java.lang.String.class);
        builder.add(SDMXDataStore.MEASURE_KEY, java.lang.Double.class);

        this.dataflowStructure
                .getDimensions()
                .forEach(
                        dim -> {
                            if (!SDMXDataStore.MEASURE_KEY.equals(dim.getId().toUpperCase())) {
                                builder.add(dim.getId(), java.lang.String.class);
                            }
                        });

        this.schema = builder.buildFeatureType();
        return this.schema;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {

        if (this.schema == null) {
            this.buildFeatureType();
        }

        try {
            return new SDMXDataflowFeatureReader(
                    this.dataStore.getSDMXClient(),
                    this.schema,
                    this.dataflow,
                    this.dataflowStructure,
                    this.buildConstraints(query),
                    this.dataStore.getLogger());
        } catch (SdmxException e) {
            // FIXME: re-hash the exception into an IOEXception
            this.dataStore.getLogger().log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }
    }

    /**
     * Builds the SDMX expression to reflect the GeoTools query give as input
     *
     * @param query GeoTools query to transform into SDMX constraints
     * @return The SDMX expression
     */
    public String buildConstraints(Query query) throws SdmxException {

        // Check tha tMEASUREs are not in there, Add "All" for the MEASURE dimension
        Map<String, String> expressions;
        ArrayList<String> constraints =
                new ArrayList<String>(this.dataflowStructure.getDimensions().size());

        // All-in query
        if (Query.ALL.equals(query)) {
            this.dataflowStructure
                    .getDimensions()
                    .forEach(
                            dim -> {
                                constraints.add(SDMXDataStore.ALLCODES_EXP);
                            });
            // Builds a non-all-in query
        } else {
            expressions =
                    (Map<String, String>)
                            query.getFilter()
                                    .accept(
                                            new SDMXDataflowFeatureSource.VisitFilter(),
                                            new HashMap<String, String>());
            this.dataflowStructure
                    .getDimensions()
                    .forEach(
                            dim -> {
                                constraints.add(
                                        expressions.get(dim.getId()) == null
                                                ? SDMXDataStore.ALLCODES_EXP
                                                : (String) expressions.get(dim.getId()));
                            });
        }

        return String.join(SDMXDataStore.SEPARATOR_EXP, constraints);
    }
}
