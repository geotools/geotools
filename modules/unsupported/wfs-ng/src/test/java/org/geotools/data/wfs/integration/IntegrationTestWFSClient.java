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
package org.geotools.data.wfs.integration;

import static org.geotools.data.wfs.WFSTestData.url;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfs20.CreatedOrModifiedFeatureType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.data.Diff;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.GetCapabilitiesRequest;
import org.geotools.data.wfs.internal.GetCapabilitiesResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionRequest.Delete;
import org.geotools.data.wfs.internal.TransactionRequest.Insert;
import org.geotools.data.wfs.internal.TransactionRequest.TransactionElement;
import org.geotools.data.wfs.internal.TransactionRequest.Update;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.data.wfs.internal.parsers.PullParserFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.wfs.v1_1.WFS;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.GeometryFactory;
import org.xml.sax.EntityResolver;

public class IntegrationTestWFSClient extends WFSClient {

    private boolean failOnTransaction;
    protected URL baseDirectory;

    private Map<QName, Diff> diffs = new HashMap<>();

    private Map<QName, SimpleFeatureType> featureTypes = new HashMap<>();

    public IntegrationTestWFSClient(final String baseDirectory, WFSConfig config) throws ServiceException, IOException {

        super(url(baseDirectory + "/GetCapabilities.xml"), HTTPClientFinder.createClient(), config);

        this.baseDirectory = url(baseDirectory);
    }

    @Override
    protected Response internalIssueRequest(Request request) throws IOException {
        try {
            if (request instanceof GetCapabilitiesRequest) {
                return mockCapabilities(request);
            }
            if (request instanceof DescribeFeatureTypeRequest) {
                return mockDFT((DescribeFeatureTypeRequest) request);
            }
            if (request instanceof GetFeatureRequest) {
                return mockGetFeature((GetFeatureRequest) request);
            }
            if (request instanceof TransactionRequest) {
                if (failOnTransaction) {
                    return mockTransactionFailure((TransactionRequest) request);
                } else {
                    return mockTransactionSuccess((TransactionRequest) request);
                }
            }
        } catch (ServiceException e) {
            throw new IOException(e);
        }

        throw new IllegalArgumentException("Unknown request : " + request);
    }

    protected Response mockCapabilities(Request request) throws IOException, ServiceException {
        HTTPResponse httpResp = new TestHttpResponse("text/xml", "UTF-8", super.serverURL);

        EntityResolver resolver = null;
        if (hints != null) {
            resolver = (EntityResolver) hints.get(XMLHandlerHints.ENTITY_RESOLVER);
        }
        return new GetCapabilitiesResponse(httpResp, resolver);
    }

    protected Response mockDFT(DescribeFeatureTypeRequest request) throws ServiceException, IOException {

        QName typeName = request.getTypeName();

        String resource = "DescribeFeatureType_" + typeName.getLocalPart() + ".xsd";
        URL contentUrl = new URL(baseDirectory, resource);

        String outputFormat = request.getOutputFormat();

        HTTPResponse response = new TestHttpResponse(outputFormat, "UTF-8", contentUrl);
        DescribeFeatureTypeResponse ret = new DescribeFeatureTypeResponse(request, response);
        FeatureType featureType = ret.getFeatureType();
        this.featureTypes.put(typeName, (SimpleFeatureType) featureType);
        return ret;
    }

    @SuppressWarnings("PMD.CloseResource") // readers are wrapped and returned in the response
    protected Response mockGetFeature(GetFeatureRequest request) throws ServiceException, IOException {

        final QName typeName = request.getTypeName();

        String resource = "GetFeature_" + typeName.getLocalPart() + ".xml";
        URL contentUrl = new URL(baseDirectory, resource);

        String outputFormat = request.getOutputFormat();

        HTTPResponse httpResponse = new TestHttpResponse(outputFormat, "UTF-8", contentUrl);

        WFSResponse response = request.createResponse(httpResponse);

        if (!(response instanceof GetFeatureResponse)) {
            return response;
        }

        final GetFeatureResponse gfr = (GetFeatureResponse) response;
        final GetParser<SimpleFeature> allFeatures = gfr.getFeatures();

        // register custom scheme
        if (allFeatures instanceof PullParserFeatureReader) {
            ((PullParserFeatureReader) allFeatures).setContextCustomizer(context -> {
                QName key = new QName("http://www.openplans.org/spearfish", "schemaLocationResolver");
                context.registerComponentInstance(
                        key, (XSDSchemaLocationResolver) (xsdSchema, namespaceURI, schemaLocationURI) -> {
                            if (schemaLocationURI.startsWith("DescribeFeatureType")) {
                                try {
                                    return new URL(baseDirectory, schemaLocationURI).toString();
                                } catch (MalformedURLException e) {
                                    return null;
                                }
                            }
                            return schemaLocationURI;
                        });
            });
        }

        final List<SimpleFeature> originalFeatures = new ArrayList<>();
        {
            SimpleFeature feature;
            while ((feature = allFeatures.parse()) != null) {
                originalFeatures.add(feature);
            }
        }

        WFSStrategy strategy = getStrategy();

        final Filter serverFiler = strategy.splitFilters(typeName, request.getFilter())[0];

        final Diff diff = diff(typeName);

        for (Iterator<SimpleFeature> it = originalFeatures.iterator(); it.hasNext(); ) {
            if (!serverFiler.evaluate(it.next())) {
                it.remove();
            }
        }

        FeatureReader<SimpleFeatureType, SimpleFeature> allFeaturesReader = null;
        if (!originalFeatures.isEmpty()) {
            allFeaturesReader = DataUtilities.reader(originalFeatures);
        }

        final DiffFeatureReader<SimpleFeatureType, SimpleFeature> serverFilteredReader =
                new DiffFeatureReader<>(allFeaturesReader, diff, serverFiler);
        final GetParser<SimpleFeature> filteredParser = new GetParser<>() {

            @Override
            public void setGeometryFactory(GeometryFactory geometryFactory) {
                // TODO Auto-generated method stub
            }

            @Override
            public SimpleFeature parse() throws IOException {
                if (!serverFilteredReader.hasNext()) {
                    return null;
                }
                return serverFilteredReader.next();
            }

            @Override
            public int getNumberOfFeatures() {
                if (-1 != allFeatures.getNumberOfFeatures()) {
                    // only if the original response included number of features (i.e. the
                    // server
                    // does advertise it)

                    FeatureReader<SimpleFeatureType, SimpleFeature> all = null;
                    try {
                        if (!originalFeatures.isEmpty()) {
                            all = DataUtilities.reader(originalFeatures);
                        }
                        try (DiffFeatureReader<SimpleFeatureType, SimpleFeature> serverFiltered =
                                new DiffFeatureReader<>(all, diff)) {
                            int count = 0;
                            while (serverFiltered.hasNext()) {
                                serverFiltered.next();
                                count++;
                            }
                            return count;
                        }
                    } catch (Exception e) {
                        java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                        throw new RuntimeException(e);
                    }
                }
                return -1;
            }

            @Override
            public FeatureType getFeatureType() {
                return allFeatures.getFeatureType();
            }

            @Override
            public void close() throws IOException {
                serverFilteredReader.close();
            }
        };

        try {
            return new GetFeatureResponse(request, httpResponse, filteredParser);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

    protected Response mockTransactionSuccess(TransactionRequest request) throws ServiceException, IOException {

        List<String> added = new ArrayList<>();
        int deleted = 0, updated = 0;

        for (TransactionElement e : request.getTransactionElements()) {
            QName typeName = e.getTypeName();
            if (e instanceof Insert) {
                Diff diff = diff(typeName);
                for (SimpleFeature f : ((Insert) e).getFeatures()) {
                    // String newId = "wfs-generated-" + idseq.incrementAndGet();
                    diff.add(f.getID(), f);
                    added.add(f.getID());
                }
            }
            if (e instanceof Delete) {
                Diff diff = diff(typeName);
                Filter filter = ((Delete) e).getFilter();
                List<SimpleFeature> features = features(typeName);
                for (SimpleFeature f : features) {
                    if (filter.evaluate(f)) {
                        diff.remove(f.getID());
                        deleted++;
                    }
                }
            }
            if (e instanceof Update) {
                Diff diff = diff(typeName);
                Update u = (Update) e;
                Filter filter = u.getFilter();
                List<SimpleFeature> features = features(typeName);
                List<QName> propertyNames = u.getPropertyNames();
                List<Object> newValues = u.getNewValues();

                for (SimpleFeature f : features) {
                    if (!filter.evaluate(f)) {
                        continue;
                    }
                    for (int i = 0; i < propertyNames.size(); i++) {
                        QName propName = propertyNames.get(i);
                        Object value = newValues.get(i);
                        String attName = propName.getLocalPart();
                        f.setAttribute(attName, value);
                    }
                    diff.modify(f.getID(), f);
                    updated++;
                }
            }
        }

        String outputFormat = request.getOutputFormat();
        String responseContents = createTransactionResponseXml(added, updated, deleted);
        HTTPResponse httpResponse = new TestHttpResponse(outputFormat, "UTF-8", responseContents);

        return request.createResponse(httpResponse);
    }

    protected Response mockTransactionFailure(TransactionRequest request) throws ServiceException, IOException {
        final QName typeName = request.getTypeNames().iterator().next();

        String resource = "TransactionFailure_" + typeName.getLocalPart() + ".xml";
        URL contentUrl = new URL(baseDirectory, resource);

        String outputFormat = request.getOutputFormat();

        HTTPResponse httpResponse = new TestHttpResponse(outputFormat, "UTF-8", contentUrl);

        WFSResponse response = request.createResponse(httpResponse);

        return response;
    }

    private Diff diff(QName typeName) {
        Diff diff = diffs.get(typeName);
        if (diff == null) {
            diff = new Diff();
            diffs.put(typeName, diff);
        }
        return diff;
    }

    private List<SimpleFeature> features(QName typeName) throws ServiceException, IOException {

        GetFeatureRequest gf = createGetFeatureRequest();
        gf.setTypeName(typeName);

        SimpleFeatureType featureType = featureTypes.get(typeName);
        if (featureType == null) {
            throw new IllegalStateException();
        }
        gf.setFullType(featureType);
        gf.setQueryType(featureType);
        gf.setFilter(Filter.INCLUDE);

        GetFeatureResponse response = (GetFeatureResponse) mockGetFeature(gf);
        GetParser<SimpleFeature> features = response.getFeatures();
        List<SimpleFeature> result = new ArrayList<>();
        SimpleFeature f;
        while ((f = features.parse()) != null) {
            result.add(f);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private String createTransactionResponseXml(List<String> added, int updated, int deleted) throws IOException {

        if ("2.0.0".equals(getStrategy().getVersion())) {

            Wfs20Factory factory = Wfs20Factory.eINSTANCE;

            net.opengis.wfs20.TransactionResponseType tr = factory.createTransactionResponseType();
            tr.setVersion(getStrategy().getVersion());

            tr.setTransactionSummary(factory.createTransactionSummaryType());
            tr.getTransactionSummary().setTotalInserted(BigInteger.valueOf(added.size()));
            tr.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
            tr.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(deleted));
            tr.setInsertResults(factory.createActionResultsType());
            tr.setUpdateResults(factory.createActionResultsType());

            if (!added.isEmpty()) {
                FilterFactory ff = CommonFactoryFinder.getFilterFactory();
                CreatedOrModifiedFeatureType inserted = factory.createCreatedOrModifiedFeatureType();
                for (String addedId : added) {
                    FeatureId featureId = ff.featureId(addedId);
                    inserted.getResourceId().add(featureId);
                }
                tr.getInsertResults().getFeature().add(inserted);
            }

            Configuration configuration = getStrategy().getWfsConfiguration();
            Encoder enc = new Encoder(configuration);
            enc.setEncoding(StandardCharsets.UTF_8);
            enc.setIndenting(true);
            enc.setIndentSize(1);

            String encodedTransactionResponse = enc.encodeAsString(tr, org.geotools.wfs.v2_0.WFS.TransactionResponse);
            return encodedTransactionResponse;

        } else {
            WfsFactory factory = WfsFactory.eINSTANCE;

            TransactionResponseType tr = factory.createTransactionResponseType();
            tr.setVersion(getStrategy().getVersion());

            tr.setTransactionSummary(factory.createTransactionSummaryType());
            tr.getTransactionSummary().setTotalInserted(BigInteger.valueOf(added.size()));
            tr.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
            tr.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(deleted));
            tr.setTransactionResults(factory.createTransactionResultsType());
            tr.setInsertResults(factory.createInsertResultsType());

            if (!added.isEmpty()) {
                FilterFactory ff = CommonFactoryFinder.getFilterFactory();
                InsertedFeatureType inserted = factory.createInsertedFeatureType();
                tr.getInsertResults().getFeature().add(inserted);
                for (String addedId : added) {
                    FeatureId featureId = ff.featureId(addedId);
                    inserted.getFeatureId().add(featureId);
                }
            }

            Configuration configuration = getStrategy().getWfsConfiguration();
            Encoder enc = new Encoder(configuration);
            enc.setEncoding(StandardCharsets.UTF_8);
            enc.setIndenting(true);
            enc.setIndentSize(1);

            String encodedTransactionResponse = enc.encodeAsString(
                    tr,
                    "1.0.0".equals(getStrategy().getVersion())
                            ? org.geotools.wfs.v1_0.WFS.WFS_TransactionResponse
                            : WFS.TransactionResponse);
            return encodedTransactionResponse;
        }
    }

    public void setFailOnTransaction(boolean failOnTransaction) {
        this.failOnTransaction = failOnTransaction;
    }

    @Override
    public boolean isUseProvidedFIDSupported() {
        WFSStrategy strategy = getStrategy();
        return Versions.v1_1_0.equals(strategy.getServiceVersion());
    }
}
