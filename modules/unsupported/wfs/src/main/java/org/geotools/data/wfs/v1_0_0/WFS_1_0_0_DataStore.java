/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005-2006, David Zwiers
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
package org.geotools.data.wfs.v1_0_0;

import static org.geotools.data.wfs.protocol.http.HttpMethod.GET;
import static org.geotools.data.wfs.protocol.http.HttpMethod.POST;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.naming.OperationNotSupportedException;
import javax.swing.Icon;
import javax.xml.namespace.QName;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.ows.FeatureSetDescription;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSServiceInfo;
import org.geotools.data.wfs.protocol.http.HttpMethod;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.ExpressionType;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterType;
import org.geotools.filter.Filters;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.geotools.xml.DocumentWriter;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.filter.FilterSchema;
import org.geotools.xml.gml.GMLComplexTypes;
import org.geotools.xml.gml.WFSFeatureTypeTransformer;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.wfs.WFSSchema;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author dzwiers
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools
 *         /wfs/v_1_0_0/data/WFSDataStore.java $
 */
public class WFS_1_0_0_DataStore extends AbstractDataStore implements WFSDataStore {
    public static final Logger LOGGER = Logging.getLogger("org.geotools.data.wfs.1.1.0");

    protected WFSCapabilities capabilities = null;

    protected HttpMethod preferredProtocol = POST; // visible for transaction

    private final int bufferSize;

    private final int timeout;

    protected WFSStrategy strategy;

    private boolean lenient;

    WFS100ProtocolHandler protocolHandler;

    private String[] typeNames = null;

    private Map<String, SimpleFeatureType> featureTypeCache = new HashMap<String, SimpleFeatureType>();

    private Map fidMap = new HashMap();

    private Map xmlSchemaCache = new HashMap();

    /**
     * Construct <code>WFSDataStore</code>.
     * 
     * @param host
     *            - may not yet be a capabilities url
     * @param protocol
     *            - true,false,null (post,get,auto)
     * @param username
     *            - iff password
     * @param password
     *            - iff username
     * @param timeout
     *            - default 3000 (ms)
     * @param buffer
     *            - default 10 (features)
     * @param tryGZIP
     *            - indicates to use GZIP if server supports it.
     * @param lenient
     *            - if true the parsing will be very forgiving to bad data. Errors will be logged
     *            rather than exceptions.
     * 
     * @throws SAXException
     * @throws IOException
     */
    public WFS_1_0_0_DataStore(HttpMethod protocol, WFS100ProtocolHandler protocolHandler,
            int timeout, int buffer, boolean lenient) throws SAXException, IOException {
        super(true);
        this.capabilities = protocolHandler.getCapabilities();
        this.protocolHandler = protocolHandler;
        this.lenient = lenient;

        this.preferredProtocol = protocol;

        this.timeout = timeout;
        this.bufferSize = buffer;
        determineCorrectStrategy();
    }

    public WFSServiceInfo getInfo() {
        return new WFSServiceInfo() {
            public String getDescription() {
                return capabilities.getService().get_abstract();
            }

            public Icon getIcon() {
                return null; // talk to Eclesia the icons are in renderer?
            }

            public Set<String> getKeywords() {
                String[] keywordList = capabilities.getService().getKeywordList();
                if (keywordList == null) {
                    return Collections.emptySet();
                }
                return new HashSet<String>(Arrays.asList(keywordList));
            }

            public URI getPublisher() {
                return null; // help?
            }

            public URI getSchema() {
                try {
                    return new URI("http://schemas.opengis.net/wfs/1.0.0/WFS-transaction.xsd");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            public URI getSource() {
                try {
                    return capabilities.getGetCapabilities().getGet().toURI();
                } catch (URISyntaxException e) {
                    return null;
                }
            }

            public String getTitle() {
                return capabilities.getService().getTitle();
            }

            public String getVersion() {
                return "1.0.0";
            }
        };
    }

    private void determineCorrectStrategy() {
        URL host = capabilities.getGetCapabilities().getGet();
        if (host == null) {
            host = capabilities.getGetCapabilities().getPost();
        }
        if (host.toString().indexOf("mapserv") != -1) {
            strategy = new MapServerWFSStrategy(this);
        } else if (host.toString().indexOf("geoserver") != -1) {
            strategy = new NonStrictWFSStrategy(this);
        } else if (lenient) {
            strategy = new NonStrictWFSStrategy(this);
        } else {
            strategy = new StrictWFSStrategy(this);
        }
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getTypeNames()
     */
    public String[] getTypeNames() {
        if (typeNames == null) {
            List l = capabilities.getFeatureTypes();
            typeNames = new String[l.size()];

            for (int i = 0; i < l.size(); i++) {
                typeNames[i] = ((FeatureSetDescription) l.get(i)).getName();
            }
        }
        // protect the cache against external modifications
        String[] retVal = new String[typeNames.length];
        System.arraycopy(typeNames, 0, retVal, 0, typeNames.length);
        return retVal;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param typeName
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws IOException
     * 
     * @see org.geotools.data.AbstractDataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        if (featureTypeCache.containsKey(typeName)) {
            return featureTypeCache.get(typeName);
        }

        // TODO sanity check for request with capabilities obj

        SimpleFeatureType featureType = null;
        SAXException sax = null;
        IOException io = null;
        if (preferredProtocol == POST) {
            try {
                featureType = getSchemaPost(typeName);
            } catch (SAXException e) {
                LOGGER.warning(e.toString());
                sax = e;
            } catch (IOException e) {
                LOGGER.warning(e.toString());
                io = e;
            }
        }
        // post either wasn't the prefferred protocol or it didn't work
        if (featureType == null) {
            try {
                featureType = getSchemaGet(typeName);
            } catch (SAXException e) {
                LOGGER.warning(e.toString());
                sax = e;
            } catch (IOException e) {
                LOGGER.warning(e.toString());
                io = e;
            }
        }

        if (featureType == null) {
            if (sax != null) {
                throw new DataSourceException(sax);
            }
            throw io;
        }

        // set crs?
        FeatureSetDescription fsd = WFSCapabilities
                .getFeatureSetDescription(capabilities, typeName);
        String crsName = null;
        String ftName = null;
        if (fsd != null) {
            crsName = fsd.getSRS();
            ftName = fsd.getName();

            CoordinateReferenceSystem crs;
            try {
                if (crsName != null) {
                    crs = CRS.decode(crsName);
                    featureType = WFSFeatureTypeTransformer.transform(featureType, crs);
                }
            } catch (FactoryException e) {
                LOGGER.warning(e.getMessage());
            } catch (SchemaException e) {
                LOGGER.warning(e.getMessage());
            }
        }

        if (ftName != null) {
            SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
            build.init(featureType);
            build.setName(ftName);

            featureType = build.buildFeatureType();
            // t = FeatureTypeBuilder.newFeatureType(
            // t.getAttributeTypes(),
            // ftName==null?typeName:ftName,
            // t.getNamespace(),
            // t.isAbstract(),
            // t.getAncestors(),
            // t.getDefaultGeometry());
        }
        if (featureType != null) {
            featureTypeCache.put(typeName, featureType);
        }
        return featureType;
    }

    // protected for testing
    protected SimpleFeatureType getSchemaGet(String typeName) throws SAXException, IOException {
        HttpURLConnection hc = protocolHandler.createDescribeFeatureTypeConnection(typeName, GET);
        if (hc == null) {
            return null;
        }

        InputStream is = protocolHandler.getConnectionFactory().getInputStream(hc);
        Schema schema;
        try {
            schema = SchemaFactory.getInstance(null, is);
        } finally {
            is.close();
        }
        return parseDescribeFeatureTypeResponse(typeName, schema);
    }

    static SimpleFeatureType parseDescribeFeatureTypeResponse(String typeName, Schema schema)
            throws SAXException {
        Element[] elements = schema.getElements();

        if (elements == null) {
            return null; // not found
        }

        Element element = null;

        String ttname = typeName.substring(typeName.indexOf(":") + 1);

        for (int i = 0; (i < elements.length) && (element == null); i++) {
            // HACK -- namspace related -- should be checking ns as opposed to
            // removing prefix
            if (typeName.equals(elements[i].getName()) || ttname.equals(elements[i].getName())) {
                element = elements[i];
            }
        }

        if (element == null) {
            return null;
        }

        SimpleFeatureType ft = GMLComplexTypes.createFeatureType(element);
        return ft;
    }

    // protected for testing
    protected SimpleFeatureType getSchemaPost(String typeName) throws IOException, SAXException {
        // getConnection(postUrl, tryGZIP, false, auth);
        HttpURLConnection hc;
        hc = protocolHandler.createDescribeFeatureTypeConnection(typeName, POST);

        // write request
        Writer osw = getOutputStream(hc);
        Map hints = new HashMap();
        hints.put(DocumentWriter.BASE_ELEMENT, WFSSchema.getInstance().getElements()[1]); // DescribeFeatureType
        List l = capabilities.getFeatureTypes();
        Iterator it = l.iterator();
        URI uri = null;
        while (it.hasNext() && uri == null) {
            FeatureSetDescription fsd = (FeatureSetDescription) it.next();
            if (typeName.equals(fsd.getName()))
                uri = fsd.getNamespace();
        }
        if (uri != null)
            hints.put(DocumentWriter.SCHEMA_ORDER, new String[] { WFSSchema.NAMESPACE.toString(),
                    uri.toString() });

        hints.put(DocumentWriter.ENCODING, protocolHandler.getEncoding());
        try {
            DocumentWriter.writeDocument(new String[] { typeName }, WFSSchema.getInstance(), osw,
                    hints);
        } catch (OperationNotSupportedException e) {
            LOGGER.warning(e.getMessage());
            throw new SAXException(e);
        }

        osw.flush();
        osw.close();
        InputStream is = protocolHandler.getConnectionFactory().getInputStream(hc);

        Schema schema;
        try {
            schema = SchemaFactory.getInstance(null, is);
        } finally {
            is.close();
        }

        return parseDescribeFeatureTypeResponse(typeName, schema);
    }

    // protected for testing
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReaderGet(Query request,
            Transaction transaction) throws UnsupportedEncodingException, IOException, SAXException {
        URL getUrl = capabilities.getGetFeature().getGet();

        if (getUrl == null) {
            return null;
        }

        String query = getUrl.getQuery();
        query = query == null ? null : query.toUpperCase();
        String url = getUrl.toString();

        if ((query == null) || "".equals(query)) {
            if ((url == null) || !url.endsWith("?")) {
                url += "?";
            }

            url += "SERVICE=WFS";
        } else {
            if (query.indexOf("SERVICE=WFS") == -1) {
                url += "&SERVICE=WFS";
            }
        }

        if ((query == null) || (query.indexOf("VERSION") == -1)) {
            url += "&VERSION=1.0.0";
        }

        if ((query == null) || (query.indexOf("REQUEST") == -1)) {
            url += "&REQUEST=GetFeature";
        }

        if (request != null) {
            if (request.getMaxFeatures() != Query.DEFAULT_MAX) {
                url += ("&MAXFEATURES=" + request.getMaxFeatures());
            }

            if (request.getFilter() != null) {
                if (Filters.getFilterType(request.getFilter()) == FilterType.GEOMETRY_BBOX) {
                    String bb = printBBoxGet(((GeometryFilter) request.getFilter()), request
                            .getTypeName());
                    if (bb != null)
                        url += ("&BBOX=" + URLEncoder.encode(bb, protocolHandler.getEncoding()));
                } else {
                    if (Filters.getFilterType(request.getFilter()) == FilterType.FID) {
                        FidFilter ff = (FidFilter) request.getFilter();

                        if ((ff.getFids() != null) && (ff.getFids().length > 0)) {
                            url += ("&FEATUREID=" + ff.getFids()[0]);

                            for (int i = 1; i < ff.getFids().length; i++) {
                                url += ("," + ff.getFids()[i]);
                            }
                        }
                    } else {
                        // rest
                        if (request.getFilter() != Filter.INCLUDE
                                && request.getFilter() != Filter.EXCLUDE) {
                            url += "&FILTER="
                                    + URLEncoder.encode(printFilter(request.getFilter()),
                                            protocolHandler.getEncoding());
                        }
                    }
                }
            }
        }

        url += ("&TYPENAME=" + URLEncoder.encode(request.getTypeName(), protocolHandler
                .getEncoding()));

        Logging.getLogger("org.geotools.data.wfs").fine(url);
        Logging.getLogger("org.geotools.data.communication").fine("Output: " + url);
        getUrl = new URL(url);
        HttpURLConnection hc = protocolHandler.getConnectionFactory().getConnection(getUrl, GET);
        InputStream is = protocolHandler.getConnectionFactory().getInputStream(hc);
        WFSTransactionState ts = null;

        if (!(transaction == Transaction.AUTO_COMMIT)) {
            ts = (WFSTransactionState) transaction.getState(this);

            if (ts == null) {
                ts = new WFSTransactionState(this);
                transaction.putState(this, ts);
            }
        }

        SimpleFeatureType schema = getSchema(request.getTypeName());

        SimpleFeatureType featureType;
        try {
            featureType = DataUtilities.createSubType(schema, request.getPropertyNames(), request
                    .getCoordinateSystem());
        } catch (SchemaException e) {
            featureType = schema;
        }
        schema.getUserData().put("lenient", true);
        WFSFeatureReader ft = WFSFeatureReader
                .getFeatureReader(is, bufferSize, timeout, ts, schema);

        if (!featureType.equals(ft.getFeatureType())) {
            LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
            return new ReTypeFeatureReader(ft, featureType, false);
        } else
            return ft;

    }

    Writer getOutputStream(HttpURLConnection hc) throws IOException {
        OutputStream os = hc.getOutputStream();

        Writer w = new OutputStreamWriter(os);
        // write request
        Logger logger = Logging.getLogger("org.geotools.data.wfs");
        if (logger.isLoggable(Level.FINE)) {
            w = new LogWriterDecorator(w, logger, Level.FINE);
        }
        // special logger for communication information only.
        logger = Logging.getLogger("org.geotools.data.communication");
        if (logger.isLoggable(Level.FINE)) {
            w = new LogWriterDecorator(w, logger, Level.FINE);
        }
        return w;
    }

    /**
     * If the field useGZIP is true Adds gzip to the connection accept-encoding property and creates
     * a gzip inputstream (if server supports it). Otherwise returns a normal buffered input stream.
     * 
     * @param hc
     *            the connection to use to create the stream
     * @return an input steam from the provided connection
     */
    static InputStream getInputStream(HttpURLConnection hc, final boolean tryGZIP)
            throws IOException {
        InputStream is = hc.getInputStream();

        if (tryGZIP) {
            if (hc.getContentEncoding() != null && hc.getContentEncoding().indexOf("gzip") != -1) {
                is = new GZIPInputStream(is);
            }
        }
        is = new BufferedInputStream(is);

        // special logger for communication information only.
        Logger logger = Logging.getLogger("org.geotools.data.communication");
        if (logger.isLoggable(Level.FINE)) {
            is = new LogInputStream(is, logger, Level.FINE);
        }
        return is;
    }

    private String printFilter(Filter f) throws IOException, SAXException {
        // ogc filter
        Map hints = new HashMap();
        hints.put(DocumentWriter.BASE_ELEMENT, FilterSchema.getInstance().getElements()[2]); // Filter

        StringWriter w = new StringWriter();

        try {
            DocumentWriter.writeFragment(f, FilterSchema.getInstance(), w, hints);
        } catch (OperationNotSupportedException e) {
            LOGGER.warning(e.toString());
            throw new SAXException(e);
        }

        return w.toString();
    }

    private String printBBoxGet(GeometryFilter gf, String typename) throws IOException {
        Envelope e = null;

        if (gf.getLeftGeometry().getType() == ExpressionType.LITERAL_GEOMETRY) {
            e = ((Geometry) ((LiteralExpression) gf.getLeftGeometry()).getLiteral())
                    .getEnvelopeInternal();
        } else {
            if (gf.getRightGeometry().getType() == ExpressionType.LITERAL_GEOMETRY) {
                LiteralExpression literal = (LiteralExpression) gf.getRightGeometry();
                Geometry geometry = (Geometry) literal.getLiteral();
                e = geometry.getEnvelopeInternal();
            } else {
                throw new IOException("Cannot encode BBOX:" + gf);
            }
        }

        if (e == null || e.isNull())
            return null;

        // Cannot check against layer bbounding box because they may be in
        // different CRS
        // We could insert ReferencedEnvelope fun here - note a check is already
        // performed
        // as part clipping the request bounding box.

        /*
         * // find layer's bbox Envelope lbb = null; if(capabilities != null &&
         * capabilities.getFeatureTypes() != null && typename!=null && !"".equals(typename)){ List
         * fts = capabilities.getFeatureTypes(); if(!fts.isEmpty()){ for(Iterator
         * i=fts.iterator();i.hasNext() && lbb == null;){ FeatureSetDescription fsd =
         * (FeatureSetDescription)i.next(); if(fsd!=null && typename.equals(fsd.getName())){ lbb =
         * fsd.getLatLongBoundingBox(); } } } } if(lbb == null || lbb.contains(e))
         */
        return e.getMinX() + "," + e.getMinY() + "," + e.getMaxX() + "," + e.getMaxY();
        // return null;
    }

    // protected for testing
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReaderPost(Query query,
            Transaction transaction) throws SAXException, IOException {
        URL postUrl = capabilities.getGetFeature().getPost();

        if (postUrl == null) {
            return null;
        }

        HttpURLConnection hc = protocolHandler.getConnectionFactory().getConnection(postUrl, POST);

        Writer w = getOutputStream(hc);

        Map hints = new HashMap();
        hints.put(DocumentWriter.BASE_ELEMENT, WFSSchema.getInstance().getElements()[2]); // GetFeature
        hints.put(DocumentWriter.ENCODING, protocolHandler.getEncoding());
        try {
            DocumentWriter.writeDocument(query, WFSSchema.getInstance(), w, hints);
        } catch (OperationNotSupportedException e) {
            LOGGER.warning(e.toString());
            throw new SAXException(e);
        } finally {
            w.flush();
            w.close();
        }

        // JE: permit possibility for GZipped data.
        InputStream is = protocolHandler.getConnectionFactory().getInputStream(hc);

        WFSTransactionState ts = null;

        if (!(transaction == Transaction.AUTO_COMMIT)) {
            ts = (WFSTransactionState) transaction.getState(this);

            if (ts == null) {
                ts = new WFSTransactionState(this);
                transaction.putState(this, ts);
            }
        }
        SimpleFeatureType schema = getSchema(query.getTypeName());

        SimpleFeatureType featureType;
        try {
            featureType = DataUtilities.createSubType(schema, query.getPropertyNames(), query
                    .getCoordinateSystem());
        } catch (SchemaException e) {
            featureType = schema;
        }
        schema.getUserData().put("lenient", true);
        WFSFeatureReader ft = WFSFeatureReader
                .getFeatureReader(is, bufferSize, timeout, ts, schema);

        if (!featureType.equals(ft.getFeatureType())) {
            LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
            return new ReTypeFeatureReader(ft, featureType, false);
        } else
            return ft;
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName)
            throws IOException {
        return getFeatureReader(typeName, new DefaultQuery(typeName));
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName,
            Query query) throws IOException {
        if ((query.getTypeName() == null) || !query.getTypeName().equals(typeName)) {
            Query q = new DefaultQuery(query);
            ((DefaultQuery) q).setTypeName(typeName);

            return getFeatureReader(q, Transaction.AUTO_COMMIT);
        }

        return getFeatureReader(query, Transaction.AUTO_COMMIT);
    }

    /**
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.data.Query,
     *      org.geotools.data.Transaction)
     */
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException {
        /**
         * @HACK: This is a hack to overcome the fact that WFS 1.0 support has not yet been ported
         *        to the new GeoAPI Filter interfaces and the renderer might be sending an
         *        org.geotools.renderer.lite.FastBBOX that makes Filters.accept(Filer) fail with a
         *        ClassCastException, and I don't want to pollute FastBBOX by implementing the
         *        deprecated geotools FilterVisitor interface, as this module is the one that should
         *        be upgraded. NOTE: it is good enough to check for the outer filter to be a BBOX
         *        because FastBBOX won't clone itself if the result of the visitor is not a BBOX
         */
        if (query.getFilter() instanceof BBOX) {
            DuplicatingFilterVisitor dfv = new DuplicatingFilterVisitor();
            Filter filter = (Filter) dfv.visit((BBOX)query.getFilter(), null);
            DefaultQuery q = new DefaultQuery(query);
            q.setFilter(filter);
            query = q;
        }
        return strategy.getFeatureReader(query, transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.data.AbstractDataStore#getBounds(org.geotools.data.Query)
     */
    protected ReferencedEnvelope getBounds(Query query) throws IOException {
        if ((query == null) || (query.getTypeName() == null)) {
            return super.getBounds(query);
        }

        List fts = capabilities.getFeatureTypes(); // FeatureSetDescription
        Iterator i = fts.iterator();
        String desiredType = query.getTypeName().substring(query.getTypeName().indexOf(":") + 1);

        while (i.hasNext()) {
            FeatureSetDescription fsd = (FeatureSetDescription) i.next();
            String fsdName = (fsd.getName() == null) ? null : fsd.getName().substring(
                    fsd.getName().indexOf(":") + 1);

            if (desiredType.equals(fsdName)) {
                Envelope env = fsd.getLatLongBoundingBox();

                ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(env,
                        DefaultGeographicCRS.WGS84);

                try {
                    return referencedEnvelope.transform(CRS.decode(fsd.getSRS()), true);
                } catch (NoSuchAuthorityCodeException e) {
                    return referencedEnvelope;
                } catch (TransformException e) {
                    return referencedEnvelope;
                } catch (FactoryException e) {
                    return referencedEnvelope;
                }
            }
        }

        return super.getBounds(query);
    }

    protected Filter[] splitFilters(Query q, Transaction t) throws IOException {
        // have to figure out which part of the request the server is capable of
        // after removing the parts in the update / delete actions
        // [server][post]
        if (q.getFilter() == null)
            return new Filter[] { Filter.INCLUDE, Filter.INCLUDE };
        if (q.getTypeName() == null || t == null)
            return new Filter[] { Filter.INCLUDE, q.getFilter() };

        SimpleFeatureType ft = getSchema(q.getTypeName());

        List fts = capabilities.getFeatureTypes(); // FeatureSetDescription
        boolean found = false;
        for (int i = 0; i < fts.size(); i++)
            if (fts.get(i) != null) {
                FeatureSetDescription fsd = (FeatureSetDescription) fts.get(i);
                if (ft.getTypeName().equals(fsd.getName())) {
                    found = true;
                } else {
                    String fsdName = (fsd.getName() == null) ? null : fsd.getName().substring(
                            fsd.getName().indexOf(":") + 1);
                    if (ft.getTypeName().equals(fsdName)) {
                        found = true;
                    }
                }
            }

        if (!found) {
            LOGGER.warning("Could not find typeName: " + ft.getTypeName());
            return new Filter[] { Filter.INCLUDE, q.getFilter() };
        }
        WFSTransactionState state = (t == Transaction.AUTO_COMMIT) ? null : (WFSTransactionState) t
                .getState(this);
        WFSTransactionAccessor transactionAccessor = null;
        if (state != null)
            transactionAccessor = new WFSTransactionAccessor(state.getActions(ft.getTypeName()));
        PostPreProcessFilterSplittingVisitor wfsfv = new PostPreProcessFilterSplittingVisitor(
                capabilities.getFilterCapabilities(), ft, transactionAccessor);

        q.getFilter().accept(wfsfv, null);

        Filter[] f = new Filter[2];
        f[0] = wfsfv.getFilterPre(); // server
        f[1] = wfsfv.getFilterPost();

        return f;
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getUnsupportedFilter(java.lang.String,
     *      org.geotools.filter.Filter)
     */
    protected Filter getUnsupportedFilter(String typeName, Filter filter) {
        try {
            return splitFilters(new DefaultQuery(typeName, filter), Transaction.AUTO_COMMIT)[1];
        } catch (IOException e) {
            return filter;
        }
    }

    /**
     * 
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public WFSFeatureSource getFeatureSource(String typeName) throws IOException {
        if (capabilities.getTransaction() != null) {
            // if(capabilities.getLockFeature()!=null){
            // return new WFSFeatureLocking(this,getSchema(typeName));
            // }
            return new WFSFeatureStore(this, typeName);
        }

        return new WFSFeatureSource(this, typeName);
    }

    /**
     * Runs {@link FidFilterVisitor} on the filter and returns the result as long as transaction is
     * not AUTO_COMMIT or null.
     * 
     * @param filter
     *            filter to process.
     * @return Runs {@link FidFilterVisitor} on the filter and returns the result as long as
     *         transaction is not AUTO_COMMIT or null.
     */
    public Filter processFilter(Filter filter) {
        FidFilterVisitor visitor = new FidFilterVisitor(fidMap);
        return (Filter) filter.accept(visitor, null);
    }

    /**
     * Adds a new fid mapping to the fid map.
     * 
     * @param original
     *            the before fid
     * @param finalFid
     *            the final fid;
     */
    public synchronized void addFidMapping(String original, String finalFid) {
        if (original == null)
            throw new NullPointerException();
        fidMap.put(original, finalFid);
    }

    public WFSCapabilities getCapabilities() {
        return capabilities;
    }

    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return null;
    }

    public List<Name> getNames() throws IOException {
        return null;
    }

    public SimpleFeatureType getSchema(Name name) throws IOException {
        return null;
    }

    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
    }

    /**
     * @see WFSDataStore#getDescribeFeatureTypeURL(String)
     */
    public URL getDescribeFeatureTypeURL(String typeName) {
        try {
            return protocolHandler.getDescribeFeatureTypeURLGet(typeName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#getFeatureTypeBounds(String)
     */
    public String getFeatureTypeAbstract(String typeName) {
        try {
            return getFeatureSource(typeName).getInfo().getDescription();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#getFeatureTypeBounds(String)
     */
    public ReferencedEnvelope getFeatureTypeBounds(String typeName) {
        try {
            return getFeatureSource(typeName).getInfo().getBounds();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#getFeatureTypeCRS(String)
     */
    public CoordinateReferenceSystem getFeatureTypeCRS(String typeName) {
        try {
            return getFeatureSource(typeName).getInfo().getCRS();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#
     */
    public Set<String> getFeatureTypeKeywords(String typeName) {
        try {
            Set<String> keywords = getFeatureSource(typeName).getInfo().getKeywords();
            return new HashSet<String>(keywords);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#getFeatureTypeTitle(String)
     */
    public String getFeatureTypeTitle(String typeName) {
        try {
            return getFeatureSource(typeName).getInfo().getTitle();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see WFSDataStore#getFeatureTypeWGS84Bounds(String)
     */
    public ReferencedEnvelope getFeatureTypeWGS84Bounds(String typeName) {
        FeatureSetDescription fsd = WFSCapabilities
                .getFeatureSetDescription(capabilities, typeName);
        Envelope latLongBoundingBox = fsd.getLatLongBoundingBox();
        return new ReferencedEnvelope(latLongBoundingBox, DefaultGeographicCRS.WGS84);
    }

    public void setMaxFeatures(Integer maxFeatures) {
        // ignored... this class needs to move to the new arch
    }

    public URL getCapabilitiesURL() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public QName getFeatureTypeName(String typeName) {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public Integer getMaxFeatures() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public String getServiceAbstract() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public Set<String> getServiceKeywords() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public URI getServiceProviderUri() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public String getServiceTitle() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public String getServiceVersion() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public boolean isPreferPostOverGet() {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }

    public void setPreferPostOverGet(Boolean booleanValue) {
        throw new UnsupportedOperationException(
                "Not used, this class needs to be adapted to the new architecture in the wfs.v_1_1_0 package");
    }
}
