package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionSummaryType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.opengis.filter.identity.FeatureId;
import org.xml.sax.SAXException;

public class TransactionResponse extends WFSResponse {

    private List<FeatureId> inserted;

    private int deletedCount;

    private int updatedCount;

    private int insertCount;

    public TransactionResponse(WFSRequest originatingRequest, HTTPResponse response)
            throws ServiceException, IOException {

        super(originatingRequest, response);

        inserted = new ArrayList<FeatureId>();

        Object parsed;
        try {
            WFSStrategy strategy = originatingRequest.getStrategy();
            Configuration wfsConfiguration = strategy.getWfsConfiguration();
            Parser parser = new Parser(wfsConfiguration);
            InputStream input = response.getResponseStream();
            parsed = parser.parse(input);
        } catch (SAXException e) {
            throw new IOException(e);
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } finally {
            response.dispose();
        }

        if (parsed instanceof TransactionResponseType) {
            TransactionResponseType tr = (TransactionResponseType) parsed;
            InsertResultsType insertResults = tr.getInsertResults();
            if (insertResults != null) {
                @SuppressWarnings("unchecked")
                List<InsertedFeatureType> inserted = insertResults.getFeature();
                for (InsertedFeatureType i : inserted) {
                    @SuppressWarnings("unchecked")
                    List<FeatureId> featureIds = i.getFeatureId();
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
            throw new IOException();// TODO: response parser factory should do this
        }
    }

    public List<FeatureId> getInsertedFids() {
        return Collections.unmodifiableList(inserted);
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public int getDeleteCount() {
        return deletedCount;
    }

    public int getInsertCount() {
        return insertCount;
    }
}
