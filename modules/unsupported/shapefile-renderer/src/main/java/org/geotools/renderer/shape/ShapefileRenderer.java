/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import static org.geotools.data.shapefile.ShpFileType.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Diff;
import org.geotools.data.FIDReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.TransactionStateDiff;
import org.geotools.data.directory.DirectoryFeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileRendererUtil;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.IndexedDbaseFileReader;
import org.geotools.data.shapefile.indexed.IndexType;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.index.quadtree.StoreException;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.OpacityFinder;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleAttributeExtractor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.styling.visitor.UomRescaleStyleVisitor;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A LiteRenderer Implementations that is optimized for shapefiles.
 * 
 * @author jeichar
 * @since 2.1.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.2.x/ext/shaperenderer/src/org/geotools/renderer/shape/ShapefileRenderer.java $
 */
public class ShapefileRenderer implements GTRenderer {
    public static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.renderer.shape");

    /** Tolerance used to compare doubles for equality */
    private static final double TOLERANCE = 1e-6;
    private static final GeometryFactory geomFactory = new GeometryFactory(
            new LiteCoordinateSequenceFactory());
    private static final Coordinate[] COORDS;
    private static final MultiPolygon MULTI_POLYGON_GEOM;
    private static final Polygon POLYGON_GEOM;
    private static final LinearRing LINE_GEOM;
    private static final MultiLineString MULTI_LINE_GEOM;
    private static final Point POINT_GEOM;
    private static final MultiPoint MULTI_POINT_GEOM;
    
    /**
     * Computes the scale as the ratio between map distances and real world distances,
     * assuming 90dpi and taking into consideration projection deformations and actual
     * earth shape. <br>
     * Use this method only when in need of accurate computation. Will break if the
     * data extent is outside of the currenct projection definition area. 
     */
    public static final String SCALE_ACCURATE = "ACCURATE";
    
    /**
     * Very simple and lenient scale computation method that conforms to the OGC SLD 
     * specification 1.0, page 26. <br>This method is quite approximative, but should
     * never break and ensure constant scale even on lat/lon unprojected maps (because
     * in that case scale is computed as if the area was along the equator no matter
     * what the real position is).
     */
    public static final String SCALE_OGC = "OGC";
    
    private String scaleComputationMethodDEFAULT = SCALE_ACCURATE;
    static {
        COORDS = new Coordinate[5];
        COORDS[0] = new Coordinate(0.0, 0.0);
        COORDS[1] = new Coordinate(5.0, 0.0);
        COORDS[2] = new Coordinate(5.0, 5.0);
        COORDS[3] = new Coordinate(0.0, 5.0);
        COORDS[4] = new Coordinate(0.0, 0.0);
        LINE_GEOM = geomFactory.createLinearRing(COORDS);
        MULTI_LINE_GEOM = geomFactory.createMultiLineString(new LineString[]{LINE_GEOM});
        POLYGON_GEOM = geomFactory.createPolygon(LINE_GEOM, new LinearRing[0]);
        MULTI_POLYGON_GEOM = geomFactory.createMultiPolygon(new Polygon[]{POLYGON_GEOM});
        POINT_GEOM = geomFactory.createPoint(COORDS[2]);
        MULTI_POINT_GEOM = geomFactory.createMultiPoint(COORDS);
    }

    /**
     * This listener is added to the list of listeners automatically. It should be removed if the
     * default logging is not needed.
     */
    public static final DefaultRenderListener DEFAULT_LISTENER = new DefaultRenderListener();

    private static final IndexInfo STREAMING_RENDERER_INFO = new IndexInfo(IndexType.NONE,null);
    static int NUM_SAMPLES = 200;
    private RenderingHints hints;

    /** Factory that will resolve symbolizers into rendered styles */
    private SLDStyleFactory styleFactory = new SLDStyleFactory();
    private boolean renderingStopRequested;
    private boolean concatTransforms;
    private MapContext context;
    LabelCache labelCache = new LabelCacheImpl();
    private List<RenderListener> renderListeners = new CopyOnWriteArrayList<RenderListener>();
    /** If we are caching styles; by default this is false */
    boolean caching = false;
    private double scaleDenominator;
    private Object defaultGeom;
    IndexInfo[] layerIndexInfo;
    StreamingRenderer delegate;

    /**
     * Maps between the AttributeType index of the new generated FeatureType and the real
     * attributeType
     */
    int[] attributeIndexing;

    /** The painter class we use to depict shapes onto the screen */
    private StyledShapePainter painter = new StyledShapePainter(labelCache);
    private Map decimators = new HashMap();
    
    /**
     * Text will be rendered using the usual calls gc.drawString/drawGlyphVector.
     * This is a little faster, and more consistent with how the platform renders
     * the text in other applications. The downside is that on most platform the label
     * and its eventual halo are not properly centered.
     */
    public static final String TEXT_RENDERING_STRING = "STRING";
    
    /**
     * Text will be rendered using the associated {@link GlyphVector} outline, that is, a {@link Shape}.
     * This ensures perfect centering between the text and the halo, but introduces more text aliasing.
     */
    public static final String TEXT_RENDERING_OUTLINE = "OUTLINE";
    
    /**
     * The text rendering method, either TEXT_RENDERING_OUTLINE or TEXT_RENDERING_STRING
     */
    public static final String TEXT_RENDERING_KEY = "textRenderingMethod";
    private String textRenderingModeDEFAULT = TEXT_RENDERING_STRING;
    
	public static final String LABEL_CACHE_KEY = "labelCache";
	public static final String FORCE_CRS_KEY = "forceCRS";
	public static final String DPI_KEY = "dpi";
	public static final String DECLARED_SCALE_DENOM_KEY = "declaredScaleDenominator";
	public static final String MEMORY_PRE_LOADING_KEY = "memoryPreloadingEnabled";
	public static final String OPTIMIZED_DATA_LOADING_KEY = "optimizedDataLoadingEnabled";
	public static final String SCALE_COMPUTATION_METHOD_KEY = "scaleComputationMethod";
    
    /**
     * "optimizedDataLoadingEnabled" - Boolean  yes/no (see default optimizedDataLoadingEnabledDEFAULT)
     * "memoryPreloadingEnabled"     - Boolean  yes/no (see default memoryPreloadingEnabledDEFAULT)
     * "declaredScaleDenominator"    - Double   the value of the scale denominator to use by the renderer.  
     *                                          by default the value is calculated based on the screen size 
     *                                          and the displayed area of the map.
     *  "dpi"                        - Integer  number of dots per inch of the display 90 DPI is the default (as declared by OGC)      
     *  "forceCRS"                   - CoordinateReferenceSystem declares to the renderer that all layers are of the CRS declared in this hint                               
     *  "labelCache"                 - Declares the label cache that will be used by the renderer.                               
     */
    private Map rendererHints = null;

    public ShapefileRenderer( MapContext context ) {
        setContext(context);
    }

    public ShapefileRenderer() {
    }

    public void paint( Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea ) {
        if (mapArea == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        paint(graphics, paintArea, mapArea, RendererUtilities.worldToScreenTransform(mapArea,
                paintArea));
    }

    private void processStylers( Graphics2D graphics, ShapefileDataStore datastore,
            Query query, Envelope bbox, Rectangle screenSize, MathTransform mt, Style style, IndexInfo info,
            Transaction transaction, String layerId) throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("processing " + style.getFeatureTypeStyles().length + " stylers");
        }

        FeatureTypeStyle[] featureStylers = style.getFeatureTypeStyles();
        SimpleFeatureType type;

        try {
            type = createFeatureType(query, style, datastore);
        } catch (Exception e) {
            fireErrorEvent(e);
            LOGGER.logp(Level.WARNING, "org.geotools.renderer.shape.ShapefileRenderer", "processStylers", "Could not prep style for rendering", e);
            return;
        }

        for( int i = 0; i < featureStylers.length; i++ ) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("processing style " + i);
            }

            FeatureTypeStyle fts = featureStylers[i];
            String typeName = datastore.getSchema().getTypeName();

            if ((typeName != null) &&
                    ( FeatureTypes.isDecendedFrom(datastore.getSchema(), null, fts.getFeatureTypeName()) 
                    || typeName .equalsIgnoreCase(fts.getFeatureTypeName()))) {
                // get applicable rules at the current scale
                Rule[] rules = fts.getRules();
                List<Rule> ruleList = new ArrayList<Rule>();
                List<Rule> elseRuleList = new ArrayList<Rule>();
                
                // TODO process filter for geometry expressions and restrict bbox further based on 
                // the result
                
                for( int j = 0; j < rules.length; j++ ) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("processing rule " + j);
                    }

                    Rule r = rules[j];
                    Filter f = r.getFilter();
                    if(f != null) {
                    	GeometryFilterChecker checker = new GeometryFilterChecker();
                        f.accept(checker, null);
                        // geometry filters are quite unlikely in SLD, but if we have any,
                        // we need to reproject it to screen space since geometries are
                        // read directly in screen space
                        if(checker.isGeometryFilterPresent()) {
                        	// make copy so we don't modify the style
                        	DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
                            r.accept(duplicator);
                            r=(Rule) duplicator.getCopy();
                            
                            FilterTransformer transformer= new  FilterTransformer(mt);
                            r.setFilter((Filter) r.getFilter().accept(transformer, null));
                        }
                    }
                    if (isWithInScale(r)) {
                        if (r.hasElseFilter()) {
                            elseRuleList.add(r);
                        } else {
                            ruleList.add(r);
                        }
                    }
                }

                // apply uom rescaling
                double pixelsPerMeters = RendererUtilities.calculatePixelsPerMeterRatio(scaleDenominator, rendererHints);
                UomRescaleStyleVisitor rescaleVisitor = new UomRescaleStyleVisitor(pixelsPerMeters);
                for (int j = 0; j < ruleList.size(); j++) {
                    rescaleVisitor.visit(ruleList.get(j));
                    ruleList.set(j, (Rule) rescaleVisitor.getCopy());
                }
                for (int j = 0; j < elseRuleList.size(); j++) {
                    rescaleVisitor.visit(elseRuleList.get(j));
                    elseRuleList.set(j, (Rule) rescaleVisitor.getCopy());
                }
                
                // apply dpi rescale
                double dpi = RendererUtilities.getDpi(getRendererHints());
                double standardDpi = RendererUtilities.getDpi(Collections.emptyMap());
                if(dpi != standardDpi) {
                    double scaleFactor = dpi / standardDpi;
                    RescaleStyleVisitor dpiVisitor = new RescaleStyleVisitor(scaleFactor);
                    for (int j = 0; j < ruleList.size(); j++) {
                        dpiVisitor.visit(ruleList.get(j));
                        ruleList.set(j, (Rule) dpiVisitor.getCopy());
                    }
                    for (int j = 0; j < elseRuleList.size(); j++) {
                        dpiVisitor.visit(elseRuleList.get(j));
                        elseRuleList.set(j, (Rule) dpiVisitor.getCopy());
                    }
                }

                // process the features according to the rules
                // TODO: find a better way to declare the scale ranges so that
                // we
                // get style caching also between multiple rendering runs
                NumberRange scaleRange = new NumberRange(scaleDenominator, scaleDenominator);

                Set modifiedFIDs = processTransaction(graphics, bbox, mt, datastore, transaction,
                        typeName, query, ruleList, elseRuleList, scaleRange, layerId);

                // don't try to read the shapefile if there is nothing to draw
                if(ruleList.size() > 0 || elseRuleList.size() > 0)
                	processShapefile(graphics, datastore, bbox,screenSize, mt, info, type, query, ruleList,
                        elseRuleList, modifiedFIDs, scaleRange, layerId);
            }
        }
    }

    private Set processTransaction( Graphics2D graphics, Envelope bbox, MathTransform transform,
            DataStore ds, Transaction transaction, String typename, Query query, List ruleList,
            List elseRuleList, NumberRange scaleRange, String layerId ) {
        if (transaction == Transaction.AUTO_COMMIT) {
            return Collections.EMPTY_SET;
        }

        TransactionStateDiff state = (TransactionStateDiff) transaction.getState(ds);

        if (state == null) {
            return Collections.EMPTY_SET;
        }
        // set of fids that has been modified (ie updated or deleted)
        Set fids = new HashSet();
        Map modified = null;
        Map added = null;
        Diff diff=null;

        try {
            diff = state.diff(typename);
            modified = diff.modified2;
            added = diff.added;
            fids = new HashSet();
        } catch (IOException e) {
            fids = Collections.EMPTY_SET;
            return fids;
        }

        if (!diff.isEmpty()) {
            SimpleFeature feature;

            for( Iterator modifiedIter = modified.keySet().iterator(), 
            		addedIter=added.values().iterator(); 
            	modifiedIter.hasNext() || addedIter.hasNext(); ) {
                try {
                    if (renderingStopRequested) {
                        break;
                    }
                    boolean doElse = true;
                    if( modifiedIter.hasNext() ){
                    	String fid= (String) modifiedIter.next();
                    	feature = (SimpleFeature) modified.get(fid);
                        fids.add(fid);
                    } else {
                        feature = (SimpleFeature) addedIter.next();
                    }
                    if( feature == TransactionStateDiff.NULL){
                        continue; // skip this feature as it is removed
                    }
                    if (!query.getFilter().evaluate(feature)){
                        // currently this is failing for TransactionStateDiff.NULL
                        continue; 
                    }
    
                    // applicable rules
                    for( Iterator it = ruleList.iterator(); it.hasNext(); ) {
                        Rule r = (Rule) it.next();

                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("applying rule: " + r.toString());
                        }

                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("this rule applies ...");
                        }

                        Filter filter = r.getFilter();

                        if ((filter == null) || filter.evaluate(feature)) {
                            doElse = false;

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("processing Symobolizer ...");
                            }

                            Symbolizer[] symbolizers = r.getSymbolizers();

                            try {
                                processSymbolizers(graphics, feature, symbolizers, scaleRange,
                                        transform, layerId);
                            } catch (Exception e) {
                                fireErrorEvent(e);

                                continue;
                            }

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("... done!");
                            }
                        }
                    }

                    if (doElse) {
                        // rules with an else filter
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("rules with an else filter");
                        }

                        for( Iterator it = elseRuleList.iterator(); it.hasNext(); ) {
                            Rule r = (Rule) it.next();
                            Symbolizer[] symbolizers = r.getSymbolizers();

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("processing Symobolizer ...");
                            }

                            try {
                                processSymbolizers(graphics, feature, symbolizers, scaleRange,
                                        transform, layerId);
                            } catch (Exception e) {
                                fireErrorEvent(e);

                                continue;
                            }

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("... done!");
                            }
                        }
                    }

                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer("feature rendered event ...");
                    }
                }
                catch (RuntimeException e) {
                    fireErrorEvent(e);
                }
            }
        }
        return fids;
    }

    private void processShapefile( Graphics2D graphics, ShapefileDataStore datastore,
            Envelope bbox, Rectangle screenSize, MathTransform mt, IndexInfo info, SimpleFeatureType type, Query query,
            List ruleList, List elseRuleList, Set modifiedFIDs, NumberRange scaleRange, String layerId )
            throws IOException {
        IndexedDbaseFileReader dbfreader = null;
        IndexInfo.Reader shpreader = null;
        FIDReader fidReader = null;
        
        int hit = 0;
        int miss = 0;

        try {
    		// clips Graphics to current drawing area before painting
            graphics = (Graphics2D)graphics.create();
            graphics.clip(screenSize);
    
            // don't waste time processing the dbf file if the only attribute loades is the geometry
            if(type.getAttributeCount() > 1) {
                try {
                    dbfreader = ShapefileRendererUtil.getDBFReader(datastore);
                } catch (Exception e) {
                    fireErrorEvent(e);
                }
            }
    
            OpacityFinder opacityFinder = new OpacityFinder(getAcceptableSymbolizers(type
                    .getGeometryDescriptor()));
    
            for( Iterator iter = ruleList.iterator(); iter.hasNext(); ) {
                Rule rule = (Rule) iter.next();
                rule.accept(opacityFinder);
            }
            
            // create index as needed
            if(datastore instanceof IndexedShapefileDataStore) {
                ((IndexedShapefileDataStore) datastore).createSpatialIndex(false);
            }
            
            boolean useJTS=true;
            try {
                shpreader = new IndexInfo.Reader(info, ShapefileRendererUtil.getShpReader(datastore,
                        bbox, screenSize, mt, opacityFinder.hasOpacity, useJTS), bbox);
            } catch (Exception e) {
                fireErrorEvent(e);
                return;
            }
    
            
            try {
                fidReader = ShapefileRendererUtil.getFidReader(datastore,shpreader);
            } catch (Exception e) {
                fireErrorEvent(e);
                return;
            }
            
            SimpleFeatureBuilder fbuilder = new SimpleFeatureBuilder(type);
        
            while( true ) {
                try {
                    if (renderingStopRequested) {
                        break;
                    }

                    if (!shpreader.hasNext()) {
                        break;
                    }

                    boolean doElse = true;

                    String nextFid = null;
                    if( fidReader.hasNext() ){
                        try {
                            nextFid = fidReader.next();
                        }
                        catch( NoSuchElementException invalidIndex){
                            fireErrorEvent(new IllegalStateException("Skipping invalid FID; Please regenerate your index.", invalidIndex));
                            // TODO: mark index as needing regeneration
                        }
                    }
                    else {
                        fireErrorEvent(new IllegalStateException("Skipping invalid FID; shape and index are out of sync please regenerate index."));
                        // TODO: mark index as needing regeneration
                    }
                    if(LOGGER.isLoggable(Level.FINER))
                        LOGGER.finer("trying to read geometry ...");                    
                    if (nextFid == null || modifiedFIDs.contains(nextFid)) {
                        // this one is modified we will get it when we processTransaction
                        shpreader.next();
                        if( dbfreader != null && !dbfreader.IsRandomAccessEnabled() ){
                            dbfreader.skip();
                        }
                        continue;
                    }
                    
                    // store the current record number, we'll use it for reading the dbf
                    final int recno = shpreader.getRecordNumber();
                    ShapefileReader.Record record = shpreader.next();

                    Object geom = record.shape();
                    if (geom == null) {
                        miss++;
                        if(LOGGER.isLoggable(Level.FINEST))
                            LOGGER.finest("skipping geometry");
                        if( dbfreader != null && !dbfreader.IsRandomAccessEnabled() )
                            dbfreader.skip();
                        continue;
                    } else {
                        hit++;
                    }
                    
                    // read the dbf file only if we got a non null shape
                    if( dbfreader != null && dbfreader.IsRandomAccessEnabled() ){
                        dbfreader.goTo(recno);
                    }

                    SimpleFeature feature = createFeature(fbuilder, record, dbfreader, nextFid);
                    if (!query.getFilter().evaluate(feature))
                        continue;

                    if (renderingStopRequested) {
                        break;
                    }

                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("... done: " + geom.toString());
                    }

                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.fine("... done: " + type.getTypeName());
                    }

                    // applicable rules
                    for( Iterator it = ruleList.iterator(); it.hasNext(); ) {
                        Rule r = (Rule) it.next();

                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("applying rule: " + r.toString());
                        }

                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("this rule applies ...");
                        }

                        Filter filter = r.getFilter();

                        if ((filter == null) || filter.evaluate(feature)) {
                            doElse = false;

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("processing Symobolizer ...");
                            }

                            Symbolizer[] symbolizers = r.getSymbolizers();

                            processSymbolizers(graphics, feature, geom, symbolizers, scaleRange, useJTS, layerId, screenSize);

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("... done!");
                            }
                        }
                    }

                    if (doElse) {
                        // rules with an else filter
                        if (LOGGER.isLoggable(Level.FINER)) {
                            LOGGER.finer("rules with an else filter");
                        }

                        for( Iterator it = elseRuleList.iterator(); it.hasNext(); ) {
                            Rule r = (Rule) it.next();
                            Symbolizer[] symbolizers = r.getSymbolizers();

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("processing Symobolizer ...");
                            }

                            processSymbolizers(graphics, feature, geom, symbolizers, scaleRange, useJTS, layerId, screenSize);

                            if (LOGGER.isLoggable(Level.FINER)) {
                                LOGGER.finer("... done!");
                            }
                        }
                    }

                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer("feature rendered event ...");
                    }
                } catch (Exception e) {
                    fireErrorEvent(e);
                }
            }
        } finally {
            try {
                if (dbfreader != null) {
                    dbfreader.close();
                }
            } finally {
                try {
                    if (shpreader != null) {
                        shpreader.close();
                    }
                } finally {
                    if (fidReader != null)
                        fidReader.close();
                }
            }
        }
        if(LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, type.getTypeName() + "): hit " + hit + " miss " + miss);
        }
    }

    private Class[] getAcceptableSymbolizers( GeometryDescriptor defaultGeometry ) {
        Class binding = defaultGeometry.getType().getBinding();
        if (Polygon.class.isAssignableFrom(binding)
                || MultiPolygon.class.isAssignableFrom(binding)) {
            return new Class[]{PointSymbolizer.class, LineSymbolizer.class, PolygonSymbolizer.class};
        }

        return new Class[]{PointSymbolizer.class, LineSymbolizer.class};
    }

    SimpleFeature createFeature(SimpleFeatureBuilder builder, Record record, DbaseFileReader dbfreader, String id )
            throws Exception {
        SimpleFeatureType type = builder.getFeatureType();
        if (type.getAttributeCount() == 1) {
            builder.add(getGeom(record.shape(), type.getGeometryDescriptor()));
            return builder.buildFeature(id);
        } else {
            dbfreader.read();
            for( int i = 0; i < (type.getAttributeCount() - 1); i++ ) {
                builder.add(dbfreader.readField(attributeIndexing[i]));
            }
            builder.add(getGeom(record.shape(), type.getGeometryDescriptor()));
            return builder.buildFeature(id);
        }
    }

    /**
     * Return provided geom; or use a default value if null.
     * 
     * @param geom Provided Geometry as read from record.shape()
     * @param defaultGeometry GeometryDescriptor used to determine default value
     * @return provided geom or default value if null
     */
    private Object getGeom( Object geom, GeometryDescriptor defaultGeometry ) {
        if( geom instanceof Geometry){
            return geom;
        }
        return getGeom( defaultGeometry );
    }

    /**
     * This class keeps a couple of default geometries on hand to use
     * when making a feature with default values.
     * 
     * @param defaultGeometry
     * @return placeholder to use as a default while waiting for a real geometry.
     */
    private Object getGeom(GeometryDescriptor defaultGeometry) {
        Class binding = defaultGeometry.getType().getBinding();
        if (MultiPolygon.class.isAssignableFrom(binding)) {
            return MULTI_POLYGON_GEOM;
        }
        else if (MultiLineString.class.isAssignableFrom(binding)) {
            return MULTI_LINE_GEOM;
        }
        else if (Point.class.isAssignableFrom(binding)) {
            return POINT_GEOM;
        }
        else if (MultiPoint.class.isAssignableFrom(binding)) {
            return MULTI_POINT_GEOM;
        }
        return null; // we don't have a good default value - null will need to do
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @param query
     * @param style
     * @param schema DOCUMENT ME!
     * @return
     * @throws FactoryConfigurationError
     * @throws SchemaException
     */
    SimpleFeatureType createFeatureType( Query query, Style style, ShapefileDataStore ds)
            throws SchemaException, IOException {
        SimpleFeatureType schema = ds.getSchema();
        String[] attributes = findStyleAttributes((query == null) ? Query.ALL : query, style,
                schema);
        AttributeDescriptor[] types = new AttributeDescriptor[attributes.length];
        attributeIndexing = new int[attributes.length];
        
        if (attributes.length == 1
                && attributes[0].equals(schema.getGeometryDescriptor().getLocalName())) {
            types[0] = schema.getDescriptor(attributes[0]);
            
            // the symbolizer might be referring "THE_GEOM" for example
            if (types[0] == null)
                throw new IllegalArgumentException("Attribute " + attributes[0]
                        + " does not exist. Maybe it has just been spelled wrongly?");
        } else {
            for( int i = 0; i < types.length; i++ ) {
                types[i] = schema.getDescriptor(attributes[i]);
    
                if (types[i] == null)
                    throw new IllegalArgumentException("Attribute " + attributes[i]
                            + " does not exist. Maybe it has just been spelled wrongly?");
                for( int j = 0; j < schema.getAttributeCount(); j++ ) {
                    if (schema.getDescriptor(j).getLocalName().equals(attributes[i])) {
                        attributeIndexing[i] = j - 1;
    
                        break;
                    }
                    
                }
            }
        }

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( schema.getName() );
        tb.addAll( types );
        tb.setDefaultGeometry( schema.getGeometryDescriptor().getLocalName() );
        
        return tb.buildFeatureType();
    }

    /**
     * Inspects the <code>MapLayer</code>'s style and retrieves it's needed attribute names,
     * returning at least the default geometry attribute name.
     * 
     * @param query DOCUMENT ME!
     * @param style the <code>Style</code> to determine the needed attributes from
     * @param schema the SimpleFeatureSource schema
     * @return the minimun set of attribute names needed to render <code>layer</code>
     */
    private String[] findStyleAttributes( final Query query, Style style, SimpleFeatureType schema ) {
        StyleAttributeExtractor sae = new StyleAttributeExtractor();
        sae.visit(style);

        
        FilterAttributeExtractor qae = new FilterAttributeExtractor();
        query.getFilter().accept(qae,null);
        Set ftsAttributes = new LinkedHashSet(sae.getAttributeNameSet());
        ftsAttributes.addAll(qae.getAttributeNameSet());
        if (sae.getDefaultGeometryUsed()
				&& (!ftsAttributes.contains(schema.getGeometryDescriptor().getLocalName()))) {
        	ftsAttributes.add(schema.getGeometryDescriptor().getLocalName());
		} else {
	        // the code following assumes the geometry column is the last one
		    // make sure it's the last for good
	        ftsAttributes.remove(schema.getGeometryDescriptor().getLocalName());
	        ftsAttributes.add(schema.getGeometryDescriptor().getLocalName());
		}
        return (String[]) ftsAttributes.toArray(new String[0]);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param graphics
     * @param feature DOCUMENT ME!
     * @param geom
     * @param symbolizers
     * @param scaleRange
     * @param layerId 
     */
    private void processSymbolizers( Graphics2D graphics, SimpleFeature feature, Object geom,
            Symbolizer[] symbolizers, NumberRange scaleRange, boolean isJTS, String layerId, Rectangle screenSize) {
        for( int m = 0; m < symbolizers.length; m++ ) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("applying symbolizer " + symbolizers[m]);
            }

            if (renderingStopRequested) {
                break;
            }

            if (symbolizers[m] instanceof TextSymbolizer) {
                try {
                    labelCache.put(layerId,(TextSymbolizer) symbolizers[m], 
                            feature, 
                            new LiteShape2((Geometry)feature.getDefaultGeometry(), null, null, false, false),
                            scaleRange);
                } catch (Exception e) {
                    fireErrorEvent(e);
                }
            } else {
                Shape shape;
                try {
                    Style2D style = styleFactory.createStyle(feature, symbolizers[m], scaleRange);
                    if( isJTS ){
                        Geometry g;
                        if(symbolizers[m] instanceof PointSymbolizer) {
                            g = RendererUtilities.getCentroid((Geometry) geom);
                        } else {
                            g = (Geometry) geom;
                        }
                        
                        // clip to the visible area + the size of the symbolizer (with some extra 
                        // to make sure we get no artifacts from polygon new borders)
                        double size = RendererUtilities.getStyle2DSize(style) + 10;
                        Envelope env = new Envelope(screenSize.getMinX(), screenSize.getMaxX(), screenSize.getMinY(), screenSize.getMaxY());
                        env.expandBy(size);
                        final GeometryClipper clipper = new GeometryClipper(env);
                        Geometry clipped = clipper.clip(g, false);
                        if(clipped == null) 
                            continue;
                        shape = new LiteShape2(clipped, null, null, false);
                        
                        painter.paint(graphics, shape, style, scaleDenominator);
                    }else{
                        if(symbolizers[m] instanceof PointSymbolizer) {
                            shape = new LiteShape2(RendererUtilities.getCentroid((Geometry) feature.getDefaultGeometry()), null, null, false, false);
                        } else {
                            shape = getShape((SimpleGeometry) geom);
                        }
                            
                        painter.paint(graphics, shape, style, scaleDenominator);
                    }
                } catch (Exception e) {
                    fireErrorEvent(e);
                }            
            }

        }
        fireFeatureRenderedEvent(feature);
    }

    /**
     * Applies each of a set of symbolizers in turn to a given feature.
     * <p>
     * This is an internal method and should only be called by processStylers.
     * </p>
     * 
     * @param graphics
     * @param feature The feature to be rendered
     * @param symbolizers An array of symbolizers which actually perform the rendering.
     * @param scaleRange The scale range we are working on... provided in order to make the style
     *        factory happy
     * @param transform DOCUMENT ME!
     * @param layerId 
     * @throws TransformException
     * @throws FactoryException
     */
    private void processSymbolizers( final Graphics2D graphics, final SimpleFeature feature,
            final Symbolizer[] symbolizers, NumberRange scaleRange, MathTransform transform, String layerId )
            throws TransformException, FactoryException {
        LiteShape2 shape;

        for( int m = 0; m < symbolizers.length; m++ ) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("applying symbolizer " + symbolizers[m]);
            }

            Geometry g = (Geometry) feature.getDefaultGeometry();
            if(symbolizers[m] instanceof PointSymbolizer)
                g = RendererUtilities.getCentroid(g);
            shape = new LiteShape2(g, transform, getDecimator(transform), false);

            if (symbolizers[m] instanceof TextSymbolizer) {
                labelCache.put(layerId, (TextSymbolizer) symbolizers[m], feature, shape, scaleRange);
            } else {
                Style2D style = styleFactory.createStyle(feature, symbolizers[m], scaleRange);
                painter.paint(graphics, shape, style, scaleDenominator);
            }
        }

        fireFeatureRenderedEvent(feature);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param mathTransform DOCUMENT ME!
     * @return
     * @throws org.opengis.referencing.operation.NoninvertibleTransformException
     */
    private Decimator getDecimator( MathTransform mathTransform  )
            throws org.opengis.referencing.operation.NoninvertibleTransformException {
        Decimator decimator=null;
        
        if( mathTransform!=null )
            decimator = (Decimator) decimators.get(mathTransform);

        if (decimator == null) {
            decimator = new Decimator(mathTransform.inverse());

            decimators.put(mathTransform, decimator);
        }

        return decimator;
    }
//
//    /**
//     * Creates a JTS shape that is an approximation of the SImpleGeometry. This is ONLY use for
//     * labelling and is only created if a text symbolizer is part of the current style.
//     * 
//     * @param geom the geometry to wrap
//     * @return
//     * @throws TransformException
//     * @throws FactoryException
//     * @throws RuntimeException DOCUMENT ME!
//     */
//    LiteShape2 getLiteShape2( SimpleGeometry geom ) throws TransformException, FactoryException {
//        Geometry jtsGeom;
//        if ((geom.type == ShapeType.POLYGON) || (geom.type == ShapeType.POLYGONM)
//                || (geom.type == ShapeType.POLYGONZ)) {
//            double[] points = getPointSample(geom, true);
//            CoordinateSequence seq = new LiteCoordinateSequence(points);
//            Polygon poly;
//
//            try {
//                poly = geomFactory.createPolygon(geomFactory.createLinearRing(seq),
//                        new LinearRing[]{});
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            jtsGeom = geomFactory.createMultiPolygon(new Polygon[]{poly});
//        } else if ((geom.type == ShapeType.ARC) || (geom.type == ShapeType.ARCM)
//                || (geom.type == ShapeType.ARCZ)) {
//            double[] points = getPointSample(geom, false);
//            CoordinateSequence seq = new LiteCoordinateSequence(points);
//            jtsGeom = geomFactory.createMultiLineString(new LineString[]{geomFactory
//                    .createLineString(seq)});
//        } else if ((geom.type == ShapeType.MULTIPOINT) || (geom.type == ShapeType.MULTIPOINTM)
//                || (geom.type == ShapeType.MULTIPOINTZ)) {
//            double[] points = getPointSample(geom, false);
//            CoordinateSequence seq = new LiteCoordinateSequence(points);
//            jtsGeom = geomFactory.createMultiPoint(seq);
//        } else {
//            jtsGeom = geomFactory.createPoint(new Coordinate(geom.coords[0][0], geom.coords[0][1]));
//        }
//
//        LiteShape2 shape = new LiteShape2(jtsGeom, null, null, false);
//
//        return shape;
//    }

//    /**
//     * takes a random sampling from the geometry. Only uses the larges part of the geometry.
//     * 
//     * @param geom
//     * @param isPolygon DOCUMENT ME!
//     * @return
//     */
//    private double[] getPointSample( SimpleGeometry geom, boolean isPolygon ) {
//        int largestPart = 0;
//
//        for( int i = 0; i < geom.coords.length; i++ ) {
//            if (geom.coords[i].length > geom.coords[largestPart].length) {
//                largestPart = i;
//            }
//        }
//
//        return geom.coords[largestPart];
//    }

    /**
     * DOCUMENT ME!
     * 
     * @param geom
     * @return
     */
    private Shape getShape( SimpleGeometry geom ) {
        if ((geom.type == ShapeType.ARC) || (geom.type == ShapeType.ARCM)
                || (geom.type == ShapeType.ARCZ)) {
            return new MultiLineShape(geom);
        }

        if ((geom.type == ShapeType.POLYGON) || (geom.type == ShapeType.POLYGONM)
                || (geom.type == ShapeType.POLYGONZ)) {
            return new PolygonShape(geom);
        }

        if ((geom.type == ShapeType.POINT) || (geom.type == ShapeType.POINTM)
                || (geom.type == ShapeType.POINTZ) || (geom.type == ShapeType.MULTIPOINT)
                || (geom.type == ShapeType.MULTIPOINTM) || (geom.type == ShapeType.MULTIPOINTZ)) {
            return new MultiPointShape(geom);
        }
        
        

        return null;
    }

    /**
     * Checks if a rule can be triggered at the current scale level
     * 
     * @param r The rule
     * @return true if the scale is compatible with the rule settings
     */
    private boolean isWithInScale( Rule r ) {
        return ((r.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
                && ((r.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
    }

    /**
     * adds a listener that responds to error events of feature rendered events.
     * 
     * @param listener the listener to add.
     * @see RenderListener
     */
    public void addRenderListener( RenderListener listener ) {
        renderListeners.add(listener);
    }

    /**
     * Removes a render listener.
     * 
     * @param listener the listener to remove.
     * @see RenderListener
     */
    public void removeRenderListener( RenderListener listener ) {
        renderListeners.remove(listener);
    }

    private void fireFeatureRenderedEvent( SimpleFeature feature ) {
        if (renderListeners.size() > 0) {
            RenderListener listener;
            for (int i = 0; i < renderListeners.size(); i++) {
                listener = renderListeners.get(i);
                listener.featureRenderer((SimpleFeature) feature);
            }
        }
    }

    private void fireErrorEvent(Exception e) {
        if (renderListeners.size() > 0) {
            RenderListener listener;
            for (int i = 0; i < renderListeners.size(); i++) {
                try {
                    listener = renderListeners.get(i);
                    listener.errorOccurred(e);
                } catch (RuntimeException ignore) {
                    LOGGER.fine("Provided RenderListener could not handle error message:" + ignore);
                    LOGGER.throwing(getClass().getName(), "fireErrorEvent", ignore);
                }
            }
        }
    }

    /**
     * Setter for property scaleDenominator.
     * 
     * @param scaleDenominator New value of property scaleDenominator.
     */
    protected void setScaleDenominator( double scaleDenominator ) {
        this.scaleDenominator = scaleDenominator;
    }

    /**
     * If you call this method from another thread than the one that called <code>paint</code> or
     * <code>render</code> the rendering will be forcefully stopped before termination
     */
    public void stopRendering() {
        try {
            if(delegate != null)
                delegate.stopRendering();
        } catch(NullPointerException e) {
            // Since stopRendering is called by another thread the null check may
            // pass, and the method call can NPE nevertheless. It's ok, in that
            // case rendering is done anyways
        }
        renderingStopRequested = true;
        labelCache.stop();
    }

    /**
     * True if we are caching styles.
     * 
     * @return <code>ture </code>if caching
     */
    public boolean isCaching() {
        return caching;
    }

    /**
     * Set to true to cache styles.
     * 
     * @param caching The caching to set.
     */
    public void setCaching( boolean caching ) {
        this.caching = caching;
    }

    public MapContext getContext() {
        return context;
    }

    public boolean isConcatTransforms() {
        return concatTransforms;
    }

    public void setConcatTransforms( boolean concatTransforms ) {
        this.concatTransforms = concatTransforms;
    }

    public IndexInfo useIndex( ShapefileDataStore ds ) throws IOException, StoreException {
        IndexInfo info;

        ShpFiles shpFiles = ShapefileRendererUtil.getShpFiles(ds);
        if (ds.isLocal()) {

            if (!shpFiles.exists(SHX)) {
                info = new IndexInfo(IndexType.NONE, shpFiles);
                LOGGER.fine("No indexing");
            } else if (shpFiles.exists(QIX)) {
                info = new IndexInfo(IndexType.QIX, shpFiles);
                LOGGER.fine("Using quad tree");
            } else {
                info = new IndexInfo(IndexType.NONE, shpFiles);
                LOGGER.fine("No indexing");
            }
        } else {
            info = new IndexInfo(IndexType.NONE, shpFiles);
            LOGGER.fine("No indexing");
        }

        return info;
    }

    /**
     * By default ignores all feature renderered events and logs all exceptions as severe.
     */
    private static class DefaultRenderListener implements RenderListener {
        /**
         * @see org.geotools.renderer.lite.RenderListener#featureRenderer(org.geotools.feature.Feature)
         */
        public void featureRenderer( SimpleFeature feature ) {
            // do nothing.
        }

        /**
         * @see org.geotools.renderer.lite.RenderListener#errorOccurred(java.lang.Exception)
         */
        public void errorOccurred( Exception e ) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void setJava2DHints( RenderingHints hints ) {
        this.hints = hints;
    }

    public RenderingHints getJava2DHints() {
        return hints;
    }

    public void setRendererHints(Map hints) {
    	if( hints!=null && hints.containsKey(LABEL_CACHE_KEY) ){
    		LabelCache cache=(LabelCache) hints.get(LABEL_CACHE_KEY);
    		if( cache==null )
    			throw new NullPointerException("Label_Cache_Hint has a null value for the labelcache");
    		
    		this.labelCache=cache;
    		this.painter=new StyledShapePainter(cache);
    	}
    	if(hints != null && hints.containsKey(StreamingRenderer.LINE_WIDTH_OPTIMIZATION_KEY)) {
            styleFactory.setLineOptimizationEnabled(Boolean.TRUE.equals(hints.get(StreamingRenderer.LINE_WIDTH_OPTIMIZATION_KEY)));
        }
    	if(hints != null && hints.containsKey(StreamingRenderer.VECTOR_RENDERING_KEY)) {
            styleFactory.setVectorRenderingEnabled(Boolean.TRUE.equals(hints.get(StreamingRenderer.VECTOR_RENDERING_KEY)));
        }
        rendererHints = hints;
    }
    
    public Map getRendererHints() {
        return rendererHints;
    }

    public void setContext( MapContext context ) {
        if (context == null) {
            context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        }

        this.context = context;

        MapLayer[] layers = context.getLayers();
        layerIndexInfo = new IndexInfo[layers.length];

        int i=0;
        for(MapLayer layer:layers ) {
            final FeatureSource fs = layer.getFeatureSource();
            DataStore ds = (DataStore) fs.getDataStore();
            
            if( ds instanceof ShapefileDataStore ){
	            ShapefileDataStore sds = (ShapefileDataStore) ds;
	            try {
	                layerIndexInfo[i] = useIndex(sds);
	            } catch (Exception e) {
	                layerIndexInfo[i] = new IndexInfo(IndexType.NONE, ShapefileRendererUtil.getShpFiles(sds));
	                if(LOGGER.isLoggable(Level.FINE))
	                    LOGGER.fine("Exception while trying to use index" + e.getLocalizedMessage());
	            }
	        }else{
	        	layerIndexInfo[i]=STREAMING_RENDERER_INFO;
	        }
            i++;
        }
    }

    public void paint( Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen ) {
        if (worldToScreen == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        // First, create the bbox in real world coordinates
        ReferencedEnvelope mapArea;
        try {
            mapArea = RendererUtilities.createMapEnvelope(paintArea, worldToScreen, getContext().getCoordinateReferenceSystem());
            paint(graphics, paintArea, mapArea, worldToScreen);
        } catch (NoninvertibleTransformException e) {
            fireErrorEvent(new Exception("Can't create pixel to world transform", e));
        }
    }

    public void paint( Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope envelope,
            AffineTransform transform ) {
        if( transform == null ){
            throw new NullPointerException("Transform is required");
        }
        if (hints != null) {
            graphics.setRenderingHints(hints);
        }

        if ((graphics == null) || (paintArea == null)) {
            LOGGER.info("renderer passed null arguments");

            return;
        }

        // reset the abort flag
        renderingStopRequested = false;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Affine Transform is " + transform);
        }

        /*
         * If we are rendering to a component which has already set up some form of transformation
         * then we can concatenate our transformation to it. An example of this is the ZoomPane
         * component of the swinggui module.
         */
        if (concatTransforms) {
            AffineTransform atg = graphics.getTransform();

            // graphics.setTransform(new AffineTransform());
            atg.concatenate(transform);
            transform = atg;
        }

        try {
            setScaleDenominator(  
                    computeScale(
                            envelope,
                            context.getCoordinateReferenceSystem(),
                            paintArea, 
                            transform,
                            this.rendererHints));
        } catch (Exception e) // probably either (1) no CRS (2) error xforming
        {
            LOGGER.throwing("RendererUtilities", "calculateScale(envelope, coordinateReferenceSystem, imageWidth, imageHeight, hints)", e);
            setScaleDenominator(1 / transform.getScaleX()); // DJB old method - the best we can do            
        }

        MapLayer[] layers = context.getLayers();

        // get detstination CRS
        CoordinateReferenceSystem destinationCrs = context.getCoordinateReferenceSystem();
        labelCache.start();
        labelCache.clear();
        if(labelCache instanceof LabelCacheImpl) {
            ((LabelCacheImpl) labelCache).setLabelRenderingMode(LabelRenderingMode.valueOf(getTextRenderingMethod()));
        }
        for( int i = 0; i < layers.length; i++ ) {
            MapLayer currLayer = layers[i];

            if (!currLayer.isVisible()) {
                // Only render layer when layer is visible
                continue;
            }

            if (renderingStopRequested) {
                return;
            }
            
            if( layerIndexInfo[i]==STREAMING_RENDERER_INFO ){
            	renderWithStreamingRenderer(currLayer, graphics, paintArea, envelope, transform);
                continue;
            }
            labelCache.startLayer(""+i);

            ReferencedEnvelope bbox = envelope;

            try {
                FeatureSource featureSource = currLayer.getFeatureSource();
                if(featureSource instanceof DirectoryFeatureSource) {
                    featureSource = ((DirectoryFeatureSource) featureSource).unwrap();
                }
                
                GeometryDescriptor geom = featureSource.getSchema().getGeometryDescriptor();
                
                CoordinateReferenceSystem dataCRS;
                if (getForceCRSHint() == null) {
                    dataCRS = geom.getCoordinateReferenceSystem();
                } else {
                    dataCRS = getForceCRSHint();
                }
                MathTransform mt = null;
                CoordinateOperation op = null;
                if( dataCRS != null ){
                    try {
                        if( dataCRS != null ){
                            op = CRS.getCoordinateOperationFactory(true).createOperation(dataCRS, destinationCrs);
                            mt = op.getMathTransform();
                            bbox = bbox.transform(dataCRS, true, 10);
                        }
                        else {
                            LOGGER.log(Level.WARNING, "Could not reproject the bounding boxes as data CRS was null, proceeding in non reprojecting mode");
                            op = null;
                            mt = null;
                        }
                    } catch (Exception e) {
                        fireErrorEvent(e);
                        LOGGER.log(Level.WARNING, "Could not reproject the bounding boxes, proceeding in non reprojecting mode", e);
                        op = null;
                        mt = null;
                    }
                }
                else {
                    LOGGER.log(Level.WARNING, "Data CRS is unknown, proceeding in non reprojecting mode");
                }
                
                MathTransform at = ReferencingFactoryFinder.getMathTransformFactory(null)
                        .createAffineTransform(new GeneralMatrix(transform));

                if (mt == null) {
                    mt = at;
                } else {
                    mt = ReferencingFactoryFinder.getMathTransformFactory(null).createConcatenatedTransform(
                            mt, at);
                }

                // dbfheader must be set so that the attributes required for theming can be read in.
                ShapefileDataStore ds = (ShapefileDataStore) featureSource.getDataStore();

                // graphics.setTransform(transform);
                // extract the feature type stylers from the style object
                // and process them

                Transaction transaction = Transaction.AUTO_COMMIT;

                if (featureSource instanceof FeatureStore) {
                    transaction = ((SimpleFeatureStore) featureSource).getTransaction();
                }

                DefaultQuery query = new DefaultQuery(currLayer.getQuery());
                if( query.getFilter() !=null ){
                    // now reproject the geometries in filter because geoms are retrieved projected to screen space
                    FilterTransformer transformer= new  FilterTransformer(mt);
                    Filter transformedFilter = (Filter) query.getFilter().accept(transformer, null);
                    query.setFilter(transformedFilter);
                }
                
                // by processing the filter we can further restrict the maximum bounds that are
                // required.  For example if a filter 
                //BoundsExtractor extractor=new BoundsExtractor(bbox);
                //if( query.getFilter()!=null )
                //    query.getFilter().accept(extractor);
                //
                //processStylers(graphics, ds, query, extractor.getIntersection(), paintArea,
                //        mt, currLayer.getStyle(), layerIndexInfo[i], transaction);
                processStylers(graphics, ds, query, bbox, paintArea,
                        mt, currLayer.getStyle(), layerIndexInfo[i], transaction, ""+i);
            } catch (Exception exception) {
                Exception e = new Exception("Exception rendering layer " + currLayer, exception);
                fireErrorEvent(e);
            }

            labelCache.endLayer(""+i, graphics, paintArea);
        }

        labelCache.end(graphics, paintArea);
        if(LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Style cache hit ratio: " + styleFactory.getHitRatio() + " , hits "
                + styleFactory.getHits() + ", requests " + styleFactory.getRequests());
    }
    
    /**
     * Returns the text rendering method
     */
    private String getTextRenderingMethod() {
        if (rendererHints == null)
            return textRenderingModeDEFAULT;
        String result = (String) rendererHints.get(TEXT_RENDERING_KEY);
        if (result == null)
            return textRenderingModeDEFAULT;
        return result;
    }
    
    /**
     * <p>
     * Returns scale computation algorithm to be used. 
     * </p>
     */
    private String getScaleComputationMethod() {
        if (rendererHints == null)
            return scaleComputationMethodDEFAULT;
        String result = (String) rendererHints.get(SCALE_COMPUTATION_METHOD_KEY);
        if (result == null)
            return scaleComputationMethodDEFAULT;
        return result;
    }
    
    private double computeScale(ReferencedEnvelope envelope, CoordinateReferenceSystem crs, Rectangle paintArea,
            AffineTransform worldToScreen, Map hints) {
        if(getScaleComputationMethod().equals(SCALE_ACCURATE)) {
            try {
               return RendererUtilities.calculateScale(envelope, paintArea.width, paintArea.height, hints);
            } catch (Exception e) // probably either (1) no CRS (2) error xforming
            {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } 
        if (XAffineTransform.getRotation(worldToScreen) != 0.0) {
            return RendererUtilities.calculateOGCScaleAffine(envelope.getCoordinateReferenceSystem(),
                    worldToScreen, hints);
        } 
        return RendererUtilities.calculateOGCScale(envelope, paintArea.width, hints);
    }
    
    private void renderWithStreamingRenderer(MapLayer layer, Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope envelope, AffineTransform transform) {
		MapContext context = null;
		RenderListener listener = null;;
		try {
		    context = new DefaultMapContext(new MapLayer[]{layer}, envelope.getCoordinateReferenceSystem());
    		delegate = new StreamingRenderer();
    		delegate.setContext(context);
    		delegate.setJava2DHints(getJava2DHints());
    		Map rendererHints2 = new HashMap(getRendererHints() != null ? getRendererHints() : Collections.EMPTY_MAP);
    		rendererHints2.put(LABEL_CACHE_KEY, new IntegratingLabelCache(labelCache));
    		delegate.setRendererHints(rendererHints2);
    		
    		// cascade events, provided there is anyone listening
    		listener = new RenderListener() {
            
                public void featureRenderer(SimpleFeature feature) {
                    fireFeatureRenderedEvent(feature);
                }
            
                public void errorOccurred(Exception e) {
                    fireErrorEvent(e);
                }
            };
            delegate.addRenderListener(listener);
    		
    		delegate.paint(graphics, paintArea, envelope, transform);
		} finally {
		    // cleanups to avoid circular references
		    if(context != null)
		        context.clearLayerList();
		    if(listener != null && delegate != null)
		        delegate.removeRenderListener(listener);
		 
		    
		    delegate = null;
		}
	}

	/**
     * If the forceCRS hint is set then return the value.
     * @return the value of the forceCRS hint or null
     */
    private CoordinateReferenceSystem getForceCRSHint() {
    	if ( rendererHints==null )
    		return null;
    	Object crs=this.rendererHints.get("forceCRS");
    	if( crs instanceof CoordinateReferenceSystem )
    		return (CoordinateReferenceSystem) crs;
    	
    	return null;
	}

    /**
     * @deprecated
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
        paint(graphics, paintArea, new ReferencedEnvelope(mapArea, context.getCoordinateReferenceSystem()));
    }

    /**
     * @deprecated
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea, AffineTransform worldToScreen) {
        paint(graphics, paintArea, new ReferencedEnvelope(mapArea, context.getCoordinateReferenceSystem()), worldToScreen);
    }
}
