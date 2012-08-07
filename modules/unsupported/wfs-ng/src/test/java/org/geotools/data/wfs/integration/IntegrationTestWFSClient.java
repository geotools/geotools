package org.geotools.data.wfs.integration;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.namespace.QName;

import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.WfsFactory;

import org.geotools.data.DataUtilities;
import org.geotools.data.Diff;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.Response;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.impl.TestHttpResponse;
import org.geotools.data.wfs.internal.AbstractWFSStrategy;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.GetCapabilitiesRequest;
import org.geotools.data.wfs.internal.GetCapabilitiesResponse;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionRequest.Delete;
import org.geotools.data.wfs.internal.TransactionRequest.Insert;
import org.geotools.data.wfs.internal.TransactionRequest.TransactionElement;
import org.geotools.data.wfs.internal.TransactionRequest.Update;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.ows.ServiceException;
import org.geotools.wfs.WFS;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.GeometryFactory;

public class IntegrationTestWFSClient extends WFSClient {

    private URL baseDirectory;

    private Map<QName, Diff> diffs = new HashMap<QName, Diff>();

    private Map<QName, SimpleFeatureType> featureTypes = new HashMap<QName, SimpleFeatureType>();

    public IntegrationTestWFSClient(String baseDirectory, WFSConfig config)
            throws ServiceException, IOException {

        super(url(baseDirectory + "/GetCapabilities.xml"), new SimpleHttpClient(), config);

        this.baseDirectory = url(baseDirectory);
    }

    private static URL url(String resource) {

        String absoluteResouce = "/org/geotools/data/wfs/impl/test-data/" + resource;

        URL url = IntegrationTestWFSClient.class.getResource(absoluteResouce);

        if(null == url){
            throw new IllegalArgumentException("Resource not found: " + absoluteResouce);
        }
        return url;
    }

    @Override
    protected Response internalIssueRequest(Request request) throws IOException {
        try {
            if (request instanceof GetCapabilitiesRequest) {
                return mockCapabilities();
            }
            if (request instanceof DescribeFeatureTypeRequest) {
                return mockDFT((DescribeFeatureTypeRequest) request);
            }
            if (request instanceof GetFeatureRequest) {
                return mockGetFeature((GetFeatureRequest) request);
            }
            if (request instanceof TransactionRequest) {
                return mockTransaction((TransactionRequest) request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getCause());
        }

        throw new IllegalArgumentException("Unknown request : " + request);
    }

    private Response mockCapabilities() throws IOException, ServiceException {
        HTTPResponse httpResp = new TestHttpResponse("text/xml", "UTF-8", super.serverURL);

        return new GetCapabilitiesResponse(httpResp);
    }

    private Response mockDFT(DescribeFeatureTypeRequest request) throws ServiceException,
            IOException {

        QName typeName = request.getTypeName();
        String simpleName = typeName.getPrefix() + "_" + typeName.getLocalPart();

        String resource = "DescribeFeatureType_" + simpleName + ".xsd";
        URL contentUrl = new URL(baseDirectory, resource);

        String outputFormat = request.getOutputFormat();

        HTTPResponse response = new TestHttpResponse(outputFormat, "UTF-8", contentUrl);
        DescribeFeatureTypeResponse ret = new DescribeFeatureTypeResponse(request, response);
        FeatureType featureType = ret.getFeatureType();
        this.featureTypes.put(typeName, (SimpleFeatureType) featureType);
        return ret;
    }

    private Response mockGetFeature(GetFeatureRequest request) throws IOException {

        final QName typeName = request.getTypeName();
        String simpleName = typeName.getPrefix() + "_" + typeName.getLocalPart();

        String resource = "GetFeature_" + simpleName + ".xml";
        URL contentUrl = new URL(baseDirectory, resource);

        String outputFormat = request.getOutputFormat();

        HTTPResponse httpResponse = new TestHttpResponse(outputFormat, "UTF-8", contentUrl);

        WFSResponse response = request.createResponse(httpResponse);

        if (!(response instanceof GetFeatureResponse)) {
            return response;
        }

        final GetFeatureResponse gfr = (GetFeatureResponse) response;
        final GetFeatureParser allFeatures = gfr.getFeatures();

        final List<SimpleFeature> originalFeatures = new ArrayList<SimpleFeature>();
        {
            SimpleFeature feature;
            while ((feature = allFeatures.parse()) != null) {
                originalFeatures.add(feature);
            }
        }

        WFSStrategy strategy = getStrategy();

        final Filter serverFiler = ((AbstractWFSStrategy) strategy).splitFilters(typeName,
                request.getFilter())[0];

        final Diff diff = diff(typeName);

        for (Iterator<SimpleFeature> it = originalFeatures.iterator(); it.hasNext();) {
            if (!serverFiler.evaluate(it.next())) {
                it.remove();
            }
        }

        FeatureReader<SimpleFeatureType, SimpleFeature> allFeaturesReader;
        allFeaturesReader = DataUtilities.reader(originalFeatures);

        final DiffFeatureReader<SimpleFeatureType, SimpleFeature> serverFilteredReader;
        serverFilteredReader = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(
                allFeaturesReader, diff);

        final GetFeatureParser filteredParser = new GetFeatureParser() {

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
                    // only if the original response included number of features (i.e. the server
                    // does advertise it)

                    FeatureReader<SimpleFeatureType, SimpleFeature> all;
                    try {
                        all = DataUtilities.reader(originalFeatures);
                        final DiffFeatureReader<SimpleFeatureType, SimpleFeature> serverFiltered;
                        serverFiltered = new DiffFeatureReader<SimpleFeatureType, SimpleFeature>(
                                all, diff);

                        int count = 0;
                        while (serverFiltered.hasNext()) {
                            serverFiltered.next();
                            count++;
                        }
                        return count;
                    } catch (Exception e) {
                        e.printStackTrace();
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
                //
            }
        };

        try {
            return new GetFeatureResponse(request, httpResponse, filteredParser);
        } catch (ServiceException e) {
            throw new IOException(e);
        }
    }

    private AtomicInteger idseq = new AtomicInteger();

    private Response mockTransaction(TransactionRequest request) throws Exception {

        List<String> added = new ArrayList<String>();
        int deleted = 0, updated = 0;

        for (TransactionElement e : request.getTransactionElements()) {
            QName typeName = e.getTypeName();
            if (e instanceof Insert) {
                Diff diff = diff(typeName);
                for (SimpleFeature f : ((Insert) e).getFeatures()) {
                    String newId = "wfs-generated-" + idseq.incrementAndGet();
                    diff.add(f.getID(), f);
                    added.add(newId);
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

    private Diff diff(QName typeName) {
        Diff diff = diffs.get(typeName);
        if (diff == null) {
            diff = new Diff();
            diffs.put(typeName, diff);
        }
        return diff;
    }

    private List<SimpleFeature> features(QName typeName) throws IOException {

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
        GetFeatureParser features = response.getFeatures();
        List<SimpleFeature> result = new ArrayList<SimpleFeature>();
        SimpleFeature f;
        while ((f = features.parse()) != null) {
            result.add(f);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private String createTransactionResponseXml(List<String> added, int updated, int deleted)
            throws IOException {
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
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            InsertedFeatureType inserted = factory.createInsertedFeatureType();
            tr.getInsertResults().getFeature().add(inserted);
            for (String addedId : added) {
                FeatureId featureId = ff.featureId(addedId);
                inserted.getFeatureId().add(featureId);
            }
        }

        Configuration configuration = getStrategy().getWfsConfiguration();
        Encoder enc = new Encoder(configuration);
        enc.setEncoding(Charset.forName("UTF-8"));
        enc.setIndenting(true);
        enc.setIndentSize(1);

        String encodedTransactionResponse = enc.encodeAsString(tr, WFS.TransactionResponse);
        System.err.println(encodedTransactionResponse);
        return encodedTransactionResponse;
    }
}
