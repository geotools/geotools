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

import it.bancaditalia.oss.sdmx.api.*;
import it.bancaditalia.oss.sdmx.exceptions.SdmxException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Feature reader of SDMX tuples
 *
 * @author lmorandini
 */
public class SDMXDimensionFeatureReader extends SDMXFeatureReader {

    protected Iterator<Entry<String, String>> dimIter;

    public SDMXDimensionFeatureReader(
            GenericSDMXClient clientIn,
            SimpleFeatureType featureTypeIn,
            Dataflow dataflowIn,
            DataFlowStructure dfStructureIn,
            String expression,
            Logger logger)
            throws IOException, SdmxException {

        super(clientIn, featureTypeIn, dataflowIn, dfStructureIn, logger);

        try {
            // If the list of dimensions has to be returned, returns only those
            if (SDMXDataStore.DIMENSIONS_EXPR_ALL.equals(expression.toUpperCase())) {
                Map<String, String> dimensions = new HashMap<String, String>();
                Iterator<Dimension> iter = dfStructureIn.getDimensions().iterator();
                iter.forEachRemaining(
                        dim -> {
                            dimensions.put(dim.getId(), dim.getName());
                        });

                if (dfStructureIn.getTimeDimension() != null) {
                    dimensions.put(
                            dfStructureIn.getTimeDimension(), dfStructureIn.getTimeDimension());
                }

                this.dimIter = dimensions.entrySet().iterator();
                // If all the codes of the given dimension has to be returned, returns the codes
            } else {
                // FIXME:
                /* 12-Feb-2020 06:45:43.222 INFO [http-apr-8080-exec-7] it.bancaditalia.oss.sdmx.client.RestSdmxClient.runQuery Contacting web service with query: http://stat.data.abs.gov.au/restsdmx/sdmx.ashx/GetData/ABS_ANNUAL_ERP_LGA2016/ABS_ANNUAL_ERP_LGA2016/ABS?format=compact_v2
12-Feb-2020 06:45:43.412 SEVERE [http-apr-8080-exec-7] it.bancaditalia.oss.sdmx.client.RestSdmxClient.runQuery
12 Feb 06:45:43 ERROR [org.geotools.data.sdmx] - (140): There is a problem with the syntax of the query.
it.bancaditalia.oss.sdmx.exceptions.SdmxResponseException: (140): There is a problem with the syntax of the query.
        at it.bancaditalia.oss.sdmx.exceptions.SdmxExceptionFactory.createRestException(SdmxExceptionFactory.java:77)
*/
                Map<String, String> times = new HashMap<String, String>();
                if (dfStructureIn.getTimeDimension().equalsIgnoreCase(expression)) {
                    List<PortableTimeSeries<Double>> tsList =
                            clientIn.getTimeSeries(
                                    dataflowIn,
                                    dfStructureIn,
                                    dfStructureIn.getId(),
                                    null,
                                    null,
                                    true,
                                    null,
                                    false);
                    tsList.iterator()
                            .forEachRemaining(
                                    (ts) -> {
                                        ts.forEach(
                                                (tsValue) -> {
                                                    times.put(
                                                            tsValue.getValueAsString(),
                                                            tsValue.getValueAsString());
                                                });
                                    });
                    this.dimIter = times.entrySet().iterator();
                } else {
                    this.dimIter =
                            dfStructureIn
                                    .getDimension(expression.toUpperCase())
                                    .getCodeList()
                                    .entrySet()
                                    .iterator();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new IOException(e);
        }
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (this.featureType == null) {
            throw new IllegalStateException(
                    "No dimension codes were retrieved, shouldn't be calling getFeatureType()");
        }
        return this.featureType;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() {
        return dimIter.hasNext();
    }

    /**
     * @throws IOException
     * @see FeatureReader#next()
     */
    @Override
    public SimpleFeature next() throws NoSuchElementException, IOException {

        if (this.hasNext() == false) {
            return null;
        }

        Entry dim = this.dimIter.next();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.featureType);
        builder.set(SDMXDataStore.GEOMETRY_ATTR, null);
        builder.set(SDMXDataStore.CODE_KEY, dim.getKey().toString());
        builder.set(SDMXDataStore.DESCRIPTION_KEY, dim.getValue().toString());

        return builder.buildFeature((new FeatureIdImpl(dim.getKey().toString())).toString());
    }

    @Override
    public void close() {
        // TODO
    }
}
