/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wfs20.ActionResultsType;
import net.opengis.wfs20.CreatedOrModifiedFeatureType;
import net.opengis.wfs20.TransactionResponseType;
import net.opengis.wfs20.TransactionSummaryType;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.TransactionResponse;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.ows.ServiceException;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.opengis.filter.identity.FeatureId;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

public class TransactionResponseImpl extends WFSResponse implements TransactionResponse {

    private List<FeatureId> inserted;

    private int deletedCount;

    private int updatedCount;

    private int insertCount;

    public TransactionResponseImpl(
            WFSRequest originatingRequest, HTTPResponse response, InputStream in)
            throws ServiceException, IOException {

        super(originatingRequest, response);

        inserted = new ArrayList<FeatureId>();

        Object parsed;
        try {
            WFSStrategy strategy = originatingRequest.getStrategy();
            Configuration wfsConfiguration = strategy.getWfsConfiguration();
            Parser parser = new Parser(wfsConfiguration);
            EntityResolver resolver = strategy.getConfig().getEntityResolver();
            if (resolver != null) {
                parser.setEntityResolver(resolver);
            }
            parsed = parser.parse(in);
        } catch (SAXException e) {
            throw new IOException(e);
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } finally {
            response.dispose();
        }

        if (parsed instanceof TransactionResponseType) {
            TransactionResponseType tr = (TransactionResponseType) parsed;
            ActionResultsType insertResults = tr.getInsertResults();
            if (insertResults != null) {
                @SuppressWarnings("unchecked")
                List<CreatedOrModifiedFeatureType> inserted = insertResults.getFeature();
                for (CreatedOrModifiedFeatureType i : inserted) {
                    @SuppressWarnings("unchecked")
                    List<FeatureId> featureIds = i.getResourceId();
                    if (null != featureIds) {
                        this.inserted.addAll(featureIds);
                    }
                }
            }
            TransactionSummaryType ts = tr.getTransactionSummary();
            if (ts != null) {
                BigInteger totalInserted = ts.getTotalInserted();
                BigInteger totalDeleted = ts.getTotalDeleted();
                BigInteger totalUpdated = ts.getTotalUpdated();
                this.updatedCount = totalUpdated == null ? -1 : totalUpdated.intValue();
                this.deletedCount = totalDeleted == null ? -1 : totalDeleted.intValue();
                this.insertCount = totalInserted == null ? -1 : totalInserted.intValue();
            }
        } else {
            throw new IOException(); // TODO: response parser factory should do this
        }
    }

    @Override
    public List<FeatureId> getInsertedFids() {
        return Collections.unmodifiableList(inserted);
    }

    @Override
    public int getUpdatedCount() {
        return updatedCount;
    }

    @Override
    public int getDeleteCount() {
        return deletedCount;
    }

    @Override
    public int getInsertCount() {
        return insertCount;
    }
}
