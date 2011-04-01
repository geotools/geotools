/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.RenderingHints.Key;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.data.memory.CollectionSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.GeometryTransformationVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.ScreenMap;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRenderer;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleAttributeExtractor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.styling.visitor.RescaleStyleVisitor;
import org.geotools.styling.visitor.UomRescaleStyleVisitor;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.processing.Operation;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.style.LineSymbolizer;
import org.opengis.style.PolygonSymbolizer;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * A streaming implementation of the GTRenderer interface.
 * <ul>
 * <li>The code is relatively simple to understand, so it can be used as a
 * simple example of an SLD compliant rendering code</li>
 * <li>Uses as little memory as possible</li>
 * </ul>
 * Use this class if you need a stateless renderer that provides low memory
 * footprint and decent rendering performance on the first call but don't need
 * good optimal performance on subsequent calls on the same data.
 * 
 * <p>
 * The streaming renderer is not thread safe
 * 
 * @author James Macgill
 * @author dblasby
 * @author jessie eichar
 * @author Simone Giannecchini
 * @author Andrea Aime
 * @author Alessio Fabiani
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/module/render/src/org/geotools/renderer/lite/StreamingRenderer.java $
 * @version $Id$
 */
public final class StreamingRenderer implements GTRenderer {

    

    private final static int defaultMaxFiltersToSendToDatastore = 5; // default

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

    /** Tolerance used to compare doubles for equality */
    private static final double TOLERANCE = 1e-6;

    /** The logger for the rendering module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.rendering");

    int error = 0;

    /** Filter factory for creating bounding box filters */
    private final static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    private final static PropertyName gridPropertyName = filterFactory.property("grid");

    private final static PropertyName paramsPropertyName = filterFactory.property("params");

    private final static PropertyName defaultGeometryPropertyName = filterFactory.property("");


    /**
     * Context which contains the layers and the bounding box which needs to be
     * rendered.
     */
    private MapContext context;

    /**
     * Flag which determines if the renderer is interactive or not. An
     * interactive renderer will return rather than waiting for time consuming
     * operations to complete (e.g. Image Loading). A non-interactive renderer
     * (e.g. a SVG or PDF renderer) will block for these operations.
     */
    private boolean interactive = true;

    /**
     * Flag which controls behaviour for applying affine transformation to the
     * graphics object. If true then the transform will be concatenated to the
     * existing transform. If false it will be replaced.
     */
    private boolean concatTransforms = false;

    /** Geographic map extent, eventually expanded to consider buffer area around the map */
    private ReferencedEnvelope mapExtent;

    /** Geographic map extent, as provided by the caller */
    private ReferencedEnvelope originalMapExtent;

    /**
     * The handler that will be called to process the geometries to deal with projections 
     * singularities and dateline wrapping
     */
    private ProjectionHandler projectionHandler;

    /** The size of the output area in output units. */
    private Rectangle screenSize;

    /**
     * This flag is set to false when starting rendering, and will be checked
     * during the rendering loop in order to make it stop forcefully
     */
    private boolean renderingStopRequested = false;

    /**
     * The ratio required to scale the features to be rendered so that they fit
     * into the output space.
     */
    private double scaleDenominator;

    /** Maximum displacement for generalization during rendering */
    private double generalizationDistance = 0.8;

    /** Factory that will resolve symbolizers into rendered styles */
    private SLDStyleFactory styleFactory = new SLDStyleFactory();

    protected LabelCache labelCache = new LabelCacheImpl();

    /** The painter class we use to depict shapes onto the screen */
    private StyledShapePainter painter = new StyledShapePainter();
    private BlockingQueue<RenderingRequest> requests;

    private IndexedFeatureResults indexedFeatureResults;

    private List<RenderListener> renderListeners = new CopyOnWriteArrayList<RenderListener>();

    private RenderingHints java2dHints;

    private boolean optimizedDataLoadingEnabledDEFAULT = true;

    private int renderingBufferDEFAULT = 0;

    private String scaleComputationMethodDEFAULT = SCALE_OGC;

    /**
     * Text will be rendered using the usual calls gc.drawString/drawGlyphVector.
     * This is a little faster, and more consistent with how the platform renders
     * the text in other applications. The downside is that on most platform the label
     * and its eventual halo are not properly centered.
     */
    public static final String TEXT_RENDERING_STRING = LabelCacheImpl.LabelRenderingMode.STRING.name();

    /**
     * Text will be rendered using the associated {@link GlyphVector} outline, that is, a {@link Shape}.
     * This ensures perfect centering between the text and the halo, but introduces more text aliasing.
     */
    public static final String TEXT_RENDERING_OUTLINE = LabelCacheImpl.LabelRenderingMode.OUTLINE.name();
    
    /**
     * Will use STRING mode for horizontal labels, OUTLINE mode for all other labels.
     * Works best when coupled with {@link RenderingHints#VALUE_FRACTIONALMETRICS_ON}
     */
    public static final String TEXT_RENDERING_ADAPTIVE = LabelCacheImpl.LabelRenderingMode.ADAPTIVE.name();

    /**
     * The text rendering method, either TEXT_RENDERING_OUTLINE or TEXT_RENDERING_STRING
     */
    public static final String TEXT_RENDERING_KEY = "textRenderingMethod";
    private String textRenderingModeDEFAULT = TEXT_RENDERING_STRING;

    /**
     * Whether the thin line width optimization should be used, or not.
     * <p>When rendering non antialiased lines adopting a width of 0 makes the
     * java2d renderer get into a fast path that generates the same output
     * as a 1 pixel wide line<p>
     * Unfortunately for antialiased rendering that optimization does not help,
     * and disallows controlling the width of thin lines. It is provided as
     * an explicit option as the optimization has been hard coded for years,
     * removing it when antialiasing is on by default will invalidate lots
     * of existing styles (making lines appear thicker).
     */
    public static final String LINE_WIDTH_OPTIMIZATION_KEY = "lineWidthOptimization";

    /**
     * Boolean flag controlling a memory/speed trade off related to how
     * multiple feature type styles are rendered.
     * <p>When enabled (by default) multiple feature type styles against the
     * same data source will be rendered in separate memory back buffers
     * in a way that allows the source to be scanned only once (each back buffer
     * is as big as the image being rendered).</p> 
     * <p>When disabled no memory back buffers will be used but the
     * feature source will be scanned once for every feature type style
     * declared against it</p>
     */
    public static final String OPTIMIZE_FTS_RENDERING_KEY = "optimizeFTSRendering";


    /**
     * Enables advanced reprojection handling. Geometries will be sliced to fit into the
     * area of definition of the rendering projection.
     */
    public static final String ADVANCED_PROJECTION_HANDLING_KEY = "advancedProjectionHandling";
    
    /**
     * Enabled continuous cartographic wrapping for projections that can wrap
     * around their edges (e.g., Mercator): this results in a continous horizontal map much
     * like Google Maps
     */
    public static final String CONTINUOUS_MAP_WRAPPING = "continuousMapWrapping";

    /**
     * Boolean flag indicating whether vector rendering should be preferred when
     * painting graphic fills. See {@link SLDStyleFactory#isVectorRenderingEnabled()}
     * for more details.  
     */
    public static final String VECTOR_RENDERING_KEY = "vectorRenderingEnabled";
    private static boolean VECTOR_RENDERING_ENABLED_DEFAULT = false;

    public static final String LABEL_CACHE_KEY = "labelCache";
    public static final String DPI_KEY = "dpi";
    public static final String DECLARED_SCALE_DENOM_KEY = "declaredScaleDenominator";
    public static final String OPTIMIZED_DATA_LOADING_KEY = "optimizedDataLoadingEnabled";
    public static final String SCALE_COMPUTATION_METHOD_KEY = "scaleComputationMethod";

    /**
     * "optimizedDataLoadingEnabled" - Boolean  yes/no (see default optimizedDataLoadingEnabledDEFAULT)
     * "memoryPreloadingEnabled"     - Boolean  yes/no (see default memoryPreloadingEnabledDEFAULT)
     * "vectorRenderingEnabled"      - Boolean  yes/no (see default vectorRenderingEnabledDEFAULT)
     * "declaredScaleDenominator"    - Double   the value of the scale denominator to use by the renderer.  
     *                                          by default the value is calculated based on the screen size 
     *                                          and the displayed area of the map.
     *  "dpi"                        - Integer  number of dots per inch of the display 90 DPI is the default (as declared by OGC)      
     *  "forceCRS"                   - CoordinateReferenceSystem declares to the renderer that all layers are of the CRS declared in this hint                               
     *  "labelCache"                 - Declares the label cache that will be used by the renderer.                               
     */
    private Map rendererHints = null;

    private AffineTransform worldToScreenTransform = null;

    private CoordinateReferenceSystem destinationCrs;

    private boolean canTransform;

    /**
     * Whether the renderer must perform generalization for the current set of features.
     * For each layer we will set this flag depending on whether the datastore can do full
     * generalization for us, or not
     */
    private boolean inMemoryGeneralization = true;
    
    /**
     * The thread pool used to submit the painter workers. 
     */
    private ExecutorService threadPool;

    /**
     * Creates a new instance of LiteRenderer without a context. Use it only to
     * gain access to utility methods of this class or if you want to render
     * random feature collections instead of using the map context interface
     */
    public StreamingRenderer() {

    }

    /**
     * Sets a thread pool to be used in parallel rendering
     * @param threadPool
     */
    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * Sets the flag which controls behaviour for applying affine transformation
     * to the graphics object.
     * 
     * @param flag
     *            If true then the transform will be concatenated to the
     *            existing transform. If false it will be replaced.
     */
    public void setConcatTransforms(boolean flag) {
        concatTransforms = flag;
    }

    /**
     * Flag which controls behaviour for applying affine transformation to the
     * graphics object.
     * 
     * @return a boolean flag. If true then the transform will be concatenated
     *         to the existing transform. If false it will be replaced.
     */
    public boolean getConcatTransforms() {
        return concatTransforms;
    }

    /**
     * adds a listener that responds to error events of feature rendered events.
     * 
     * @see RenderListener
     * 
     * @param listener
     *            the listener to add.
     */
    public void addRenderListener(RenderListener listener) {
        renderListeners.add(listener);
    }

    /**
     * Removes a render listener.
     * 
     * @see RenderListener
     * 
     * @param listener
     *            the listener to remove.
     */
    public void removeRenderListener(RenderListener listener) {
        renderListeners.remove(listener);
    }

    private void fireFeatureRenderedEvent(Object feature) {
        if( !(feature instanceof SimpleFeature)){
            if(feature instanceof Feature) {
                LOGGER.log(Level.FINE, "Skipping non simple feature rendering notification");
            }
            return;
        }
        if (renderListeners.size() > 0) {
            RenderListener listener;
            for (int i = 0; i < renderListeners.size(); i++) {
                listener = renderListeners.get(i);
                listener.featureRenderer((SimpleFeature) feature);
            }
        }
    }

    private void fireErrorEvent(Throwable t) {
        LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);
        if (renderListeners.size() > 0) {
            Exception e;
            if(t instanceof Exception) {
                e = (Exception) t;
            } else {
                e = new Exception(t);
            }
            RenderListener listener;
            for (int i = 0; i < renderListeners.size(); i++) {
                listener = renderListeners.get(i);
                listener.errorOccurred(e);
            }
        }
    }

    /**
     * If you call this method from another thread than the one that called
     * <code>paint</code> or <code>render</code> the rendering will be
     * forcefully stopped before termination
     */
    public void stopRendering() {
        renderingStopRequested = true;
        labelCache.stop();
    }

    /**
     * Renders features based on the map layers and their styles as specified in
     * the map context using <code>setContext</code>. <p/> This version of
     * the method assumes that the size of the output area and the
     * transformation from coordinates to pixels are known. The latter
     * determines the map scale. The viewport (the visible part of the map) will
     * be calculated internally.
     * 
     * @param graphics
     *            The graphics object to draw to.
     * @param paintArea
     *            The size of the output area in output units (eg: pixels).
     * @param worldToScreen
     *            A transform which converts World coordinates to Screen
     *            coordinates.
     * @task Need to check if the Layer CoordinateSystem is different to the
     *       BoundingBox rendering CoordinateSystem and if so, then transform
     *       the coordinates.
     * @deprecated Use paint(Graphics2D graphics, Rectangle paintArea,
     *             ReferencedEnvelope mapArea) or paint(Graphics2D graphics,
     *             Rectangle paintArea, ReferencedEnvelope mapArea,
     *             AffineTransform worldToScreen) instead.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea,
            AffineTransform worldToScreen) {
        if (worldToScreen == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        // First, create the bbox in real world coordinates
        Envelope mapArea;
        try {
            mapArea = RendererUtilities.createMapEnvelope(paintArea,
                    worldToScreen);
            paint(graphics, paintArea, mapArea, worldToScreen);
        } catch (NoninvertibleTransformException e) {
            fireErrorEvent(e);
        }
    }

    /**
     * Renders features based on the map layers and their styles as specified in
     * the map context using <code>setContext</code>. <p/> This version of
     * the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is
     * calculated internally.
     * 
     * @param graphics
     *            The graphics object to draw to.
     * @param paintArea
     *            The size of the output area in output units (eg: pixels).
     * @param mapArea
     *            the map's visible area (viewport) in map coordinates.
     * @deprecated Use paint(Graphics2D graphics, Rectangle paintArea,
     *             ReferencedEnvelope mapArea) or paint(Graphics2D graphics,
     *             Rectangle paintArea, ReferencedEnvelope mapArea,
     *             AffineTransform worldToScreen) instead.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
        if (mapArea == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        paint(graphics, paintArea, mapArea, RendererUtilities
                .worldToScreenTransform(mapArea, paintArea));
    }

    /**
     * Renders features based on the map layers and their styles as specified in
     * the map context using <code>setContext</code>. <p/> This version of
     * the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is
     * calculated internally.
     * 
     * @param graphics
     *            The graphics object to draw to.
     * @param paintArea
     *            The size of the output area in output units (eg: pixels).
     * @param mapArea
     *            the map's visible area (viewport) in map coordinates.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea,
            ReferencedEnvelope mapArea) {
        if (mapArea == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        paint(graphics, paintArea, mapArea, RendererUtilities
                .worldToScreenTransform(mapArea, paintArea));
    }

    /**
     * Renders features based on the map layers and their styles as specified in
     * the map context using <code>setContext</code>. <p/> This version of
     * the method assumes that paint area, envelope and worldToScreen transform
     * are already computed. Use this method to avoid recomputation. <b>Note
     * however that no check is performed that they are really in sync!<b/>
     * 
     * @param graphics
     *            The graphics object to draw to.
     * @param paintArea
     *            The size of the output area in output units (eg: pixels).
     * @param mapArea
     *            the map's visible area (viewport) in map coordinates.
     * @param worldToScreen
     *            A transform which converts World coordinates to Screen
     *            coordinates.
     * @deprecated Use paint(Graphics2D graphics, Rectangle paintArea,
     *             ReferencedEnvelope mapArea) or paint(Graphics2D graphics,
     *             Rectangle paintArea, ReferencedEnvelope mapArea,
     *             AffineTransform worldToScreen) instead.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea,
            Envelope mapArea, AffineTransform worldToScreen) {
        paint( graphics, paintArea, new ReferencedEnvelope(mapArea, context.getCoordinateReferenceSystem()),
                worldToScreen);
    }

    private double computeScale(ReferencedEnvelope envelope, Rectangle paintArea, 
            AffineTransform worldToScreen, Map hints) {
        if(getScaleComputationMethod().equals(SCALE_ACCURATE)) {
            try {
                return RendererUtilities.calculateScale(envelope,
                        paintArea.width, paintArea.height, hints);
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

    /**
     * Renders features based on the map layers and their styles as specified in
     * the map context using <code>setContext</code>. <p/> This version of
     * the method assumes that paint area, envelope and worldToScreen transform
     * are already computed. Use this method to avoid recomputation. <b>Note
     * however that no check is performed that they are really in sync!<b/>
     * 
     * @param graphics
     *            The graphics object to draw to.
     * @param paintArea
     *            The size of the output area in output units (eg: pixels).
     * @param mapArea
     *            the map's visible area (viewport) in map coordinates. Its
     *            associate CRS is ALWAYS 2D
     * @param worldToScreen
     *            A transform which converts World coordinates to Screen
     *            coordinates.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea,
            ReferencedEnvelope mapArea, AffineTransform worldToScreen) {
        // ////////////////////////////////////////////////////////////////////
        // 
        // Check for null arguments, recompute missing ones if possible
        //
        // ////////////////////////////////////////////////////////////////////
        if (graphics == null || paintArea == null) {
            LOGGER.severe("renderer passed null arguments");
            throw new NullPointerException("renderer passed null arguments");
        } else if (mapArea == null && paintArea == null) {
            LOGGER.severe("renderer passed null arguments");
            throw new NullPointerException("renderer passed null arguments");
        } else if (mapArea == null) {

            LOGGER.severe("renderer passed null arguments");
            throw new NullPointerException("renderer passed null arguments");
        } else if (worldToScreen == null) {
            worldToScreen = RendererUtilities.worldToScreenTransform(mapArea,
                    paintArea);
            if (worldToScreen == null)
                return;
        }
        
        // ////////////////////////////////////////////////////////////////////
        // 
        // Setting base information
        //
        // TODO the way this thing is built is a mess if you try to use it in a
        // multithreaded environment. I will fix this at the end.
        //
        // ////////////////////////////////////////////////////////////////////
        destinationCrs = mapArea.getCoordinateReferenceSystem();
        mapExtent = new ReferencedEnvelope(mapArea);
        this.screenSize = paintArea;
        this.worldToScreenTransform = worldToScreen;
        error = 0;
        if (java2dHints != null)
            graphics.setRenderingHints(java2dHints);
        // reset the abort flag
        renderingStopRequested = false;
        
        // setup the graphic clip
        graphics.setClip(paintArea);

        // ////////////////////////////////////////////////////////////////////
        //
        // Managing transformations , CRSs and scales
        //
        // If we are rendering to a component which has already set up some form
        // of transformation then we can concatenate our transformation to it.
        // An example of this is the ZoomPane component of the swinggui module.
        // ////////////////////////////////////////////////////////////////////
        if (concatTransforms) {
            AffineTransform atg = graphics.getTransform();
            atg.concatenate(worldToScreenTransform);
            worldToScreenTransform = atg;
            graphics.setTransform(worldToScreenTransform);
        }

        // compute scale according to the user specified method
        scaleDenominator = computeScale(mapArea, paintArea,worldToScreenTransform, rendererHints);
        if(LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Computed scale denominator: " + scaleDenominator);
        //////////////////////////////////////////////////////////////////////
        //
        // Consider expanding the map extent so that a few more geometries
        // will be considered, in order to catch those outside of the rendering
        // bounds whose stroke is so thick that it countributes rendered area
        //
        //////////////////////////////////////////////////////////////////////
        int buffer = getRenderingBuffer();
        originalMapExtent = mapExtent;
        if(buffer > 0) {
            mapExtent = new ReferencedEnvelope(expandEnvelope(mapExtent, worldToScreen, buffer), 
                    mapExtent.getCoordinateReferenceSystem()); 
        }

        // enable advanced projection handling with the updated map extent
        if(isAdvancedProjectionHandlingEnabled()) {
            // get the projection handler and set a tentative envelope
            projectionHandler = ProjectionHandlerFinder.getHandler(mapExtent, isMapWrappingEnabled());
        }
        
        // Setup the secondary painting thread
        requests = new ArrayBlockingQueue<RenderingRequest>(10000);
        PainterThread painterThread = new PainterThread(requests);
        ExecutorService localThreadPool = threadPool;
        boolean localPool = false;
        if(localThreadPool == null) {
            localThreadPool = Executors.newSingleThreadExecutor();
            localPool = true;
        }
        Future painterFuture = localThreadPool.submit(painterThread);
        try {
            // ////////////////////////////////////////////////////////////////////
            //
            // Processing all the map layers in the context using the accompaining
            // styles
            //
            // ////////////////////////////////////////////////////////////////////
            final MapLayer[] layers = context.getLayers();
            labelCache.start();
            if(labelCache instanceof LabelCacheImpl) {
                ((LabelCacheImpl) labelCache).setLabelRenderingMode(LabelRenderingMode.valueOf(getTextRenderingMethod()));
            }
            final int layersNumber = layers.length;
            MapLayer currLayer;
            for (int i = 0; i < layersNumber; i++) // DJB: for each layer (ie. one
            {
                currLayer = layers[i];
    
                if (!currLayer.isVisible()) {
                    // Only render layer when layer is visible
                    continue;
                }
    
                if (renderingStopRequested) {
                    return;
                }
                labelCache.startLayer(i+"");
                try {
    
                    // extract the feature type stylers from the style object
                    // and process them
                    processStylers(graphics, currLayer, worldToScreenTransform,
                            destinationCrs, mapExtent, screenSize, i+"");
                } catch (Throwable t) {
                    fireErrorEvent(t);
                }
    
                labelCache.endLayer(i+"", graphics, screenSize);
            }
        } finally {
            try {
                requests.put(new EndRequest());
                painterFuture.get();
            } catch(Exception e) {
                painterFuture.cancel(true);
                fireErrorEvent(e);
            } finally {
                if(localPool) {
                    localThreadPool.shutdown();
                }
            }
        }
        
        labelCache.end(graphics, paintArea);
    
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuffer("Style cache hit ratio: ").append(
                    styleFactory.getHitRatio()).append(" , hits ").append(
                            styleFactory.getHits()).append(", requests ").append(
                                    styleFactory.getRequests()).toString());
        if (error > 0) {
            LOGGER
            .warning(new StringBuffer(
            "Number of Errors during paint(Graphics2D, AffineTransform) = ")
            .append(error).toString());
        }
        
    }

    /**
     * Extends the provided {@link Envelope} in order to add the number of pixels
     * specified by <code>buffer</code> in every direction.
     * 
     * @param envelope to extend.
     * @param worldToScreen by means  of which doing the extension.
     * @param buffer to use for the extension.
     * @return an extended version of the provided {@link Envelope}.
     */
    private Envelope expandEnvelope(Envelope envelope, AffineTransform worldToScreen, int buffer) {
        assert buffer>0;
        double bufferX =  Math.abs(buffer * 1.0 /  XAffineTransform.getScaleX0(worldToScreen));
        double bufferY =  Math.abs(buffer * 1.0 /  XAffineTransform.getScaleY0(worldToScreen));
        return new Envelope(envelope.getMinX() - bufferX, 
                envelope.getMaxX() + bufferX, envelope.getMinY() - bufferY, 
                envelope.getMaxY() + bufferY);
    }

    /**
     * Queries a given layer's <code>Source</code> instance to be rendered. 
     * <p>
     * <em><strong>Note: This is proof-of-concept quality only!</strong> At 
     * the moment the query is not filtered, that means all objects with all 
     * fields are read from the datastore for every call to this method. This 
     * method should work like 
     * {@link #queryLayer(MapLayer, FeatureSource, SimpleFeatureType, LiteFeatureTypeStyle[], Envelope, CoordinateReferenceSystem, CoordinateReferenceSystem, Rectangle, GeometryAttributeType)} 
     * and eventually replace it.</em>
     * </p>
     * 
     * @param currLayer The actually processed layer for rendering
     * @param source Source to read data from
     */
    //TODO: Implement filtering for bbox and read in only the need attributes 
    Collection queryLayer(MapLayer currLayer, CollectionSource source) {
        //REVISIT: this method does not make sense. Always compares
        //new Query(Query.ALL) for reference equality with Query.All. GR.

        Collection results = null;
        Query query = new Query(Query.ALL);
        Query definitionQuery;

        definitionQuery = currLayer.getQuery();

        if (definitionQuery != Query.ALL) {
            if (query == Query.ALL) {
                query = new Query(definitionQuery);
            } else {
                query = new Query(DataUtilities.mixQueries(definitionQuery, query, "liteRenderer"));
            }
        }

        results = source.content(query.getFilter());

        return results;
    }


    /**
     * Queries a given layer's features to be rendered based on the target
     * rendering bounding box.
     * <p>
     * If <code>optimizedDataLoadingEnabled</code> attribute has been set to
     * <code>true</code>, the following optimization will be performed in
     * order to limit the number of features returned:
     * <ul>
     * <li>Just the features whose geometric attributes lies within
     * <code>envelope</code> will be queried</li>
     * <li>The queried attributes will be limited to just those needed to
     * perform the rendering, based on the required geometric and non geometric
     * attributes found in the Layer's style rules</li>
     * <li>If a <code>Query</code> has been set to limit the resulting
     * layer's features, the final filter to obtain them will respect it. This
     * means that the bounding box filter and the Query filter will be combined,
     * also including maxFeatures from Query</li>
     * <li>At least that the layer's definition query explicitly says to
     * retrieve some attribute, no attributes will be requested from it, for
     * performance reasons. So it is desirable to not use a Query for filtering
     * a layer which includes attributes. Note that including the attributes in
     * the result is not necessary for the query's filter to get properly
     * processed. </li>
     * </ul>
     * </p>
     * <p>
     * <b>NOTE </b>: This is an internal method and should only be called by
     * <code>paint(Graphics2D, Rectangle, AffineTransform)</code>. It is
     * package protected just to allow unit testing it.
     * </p>
     * 
     * @param currLayer
     *            the actually processing layer for renderition
     * @param schema
     * @param source
     * @param envelope
     *            the spatial extent which is the target area of the rendering
     *            process
     * @param destinationCRS
     *            DOCUMENT ME!
     * @param sourceCrs
     * @param screenSize
     * @param geometryAttribute
     * @return the set of features resulting from <code>currLayer</code> after
     *         querying its feature source
     * @throws IllegalFilterException
     *             if something goes wrong constructing the bbox filter
     * @throws IOException
     * @see MapLayer#setQuery(org.geotools.data.Query)
     */
    /*
     * Default visibility for testing purposes
     */

    FeatureCollection<FeatureType, Feature> queryLayer(MapLayer currLayer, FeatureSource<FeatureType, Feature> source,
            FeatureType schema, LiteFeatureTypeStyle[] styles,
            Envelope mapArea, CoordinateReferenceSystem mapCRS,
            CoordinateReferenceSystem featCrs, Rectangle screenSize,
            GeometryDescriptor geometryAttribute,
            AffineTransform worldToScreenTransform)
            throws IllegalFilterException, IOException {
        FeatureCollection<FeatureType, Feature> results = null;
        Query query = new Query(Query.ALL);
        Query definitionQuery;
        final int length;
        Filter filter = null;

        // if map extent are not already expanded by a constant buffer, try to compute a layer
        // specific one based on stroke widths
        if(getRenderingBuffer() == 0) {
            int buffer = findRenderingBuffer(styles);
            if (buffer > 0) {
                mapArea = expandEnvelope(mapArea, worldToScreenTransform,
                        buffer);
                LOGGER.fine("Expanding rendering area by " + buffer 
                        + " pixels to consider stroke width");
            }
        }

        // build a list of attributes used in the rendering
        String[] attributes;
        if (styles == null) {
//            List<AttributeDescriptor> ats = schema.getAttributeDescriptors();
//            length = ats.size();
//            attributes = new String[length];
//            for (int t = 0; t < length; t++) {
//                attributes[t] = ats.get(t).getLocalName();
//            }
            attributes = null;
        } else {
            attributes = findStyleAttributes(styles, schema);
        }

        ReferencedEnvelope envelope = new ReferencedEnvelope(mapArea, mapCRS);
        if (isOptimizedDataLoadingEnabled()) {
            // see what attributes we really need by exploring the styles
            // for testing purposes we have a null case -->

            try {
                // Then create the geometry filters. We have to create one for
                // each geometric attribute used during the rendering as the
                // feature may have more than one and the styles could use non
                // default geometric ones
                List<ReferencedEnvelope> envelopes;
                if (projectionHandler != null) {
                    // update the envelope with the one eventually grown by the rendering buffer
                    projectionHandler.setRenderingEnvelope(envelope);
                    envelopes = projectionHandler.getQueryEnvelopes(featCrs);
                } else {
                    if (mapCRS != null && featCrs != null && !CRS.equalsIgnoreMetadata(featCrs, mapCRS)) {
                        envelopes = Collections.singletonList(envelope.transform(featCrs, true, 10));
                    } else {
                        envelopes = Collections.singletonList(envelope);
                    }
                }

                if(LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("Querying layer " + schema.getName() +  " with bbox: " + envelope);
                filter = createBBoxFilters(schema, attributes, envelopes);

                // now build the query using only the attributes and the
                // bounding box needed
                query = new Query(schema.getName().getLocalPart());
                query.setFilter(filter);
                query.setPropertyNames(attributes);
                processRuleForQuery(styles, query);

            } catch (Exception e) {
                fireErrorEvent(new Exception("Error transforming bbox", e));
                canTransform = false;
                query = new Query(schema.getName().getLocalPart());
                query.setPropertyNames(attributes);
                Envelope bounds = source.getBounds();
                if (bounds != null && envelope.intersects(bounds)) {
                    LOGGER.log(Level.WARNING, "Got a tranform exception while trying to de-project the current " +
                            "envelope, bboxs intersect therefore using envelope)", e);
                    filter = null;					
                    filter = createBBoxFilters(schema, attributes, Collections.singletonList(envelope));
                    query.setFilter(filter);
                } else {
                    LOGGER.log(Level.WARNING, "Got a tranform exception while trying to de-project the current " +
                            "envelope, falling back on full data loading (no bbox query)", e);
                    query.setFilter(Filter.INCLUDE);
                }
                processRuleForQuery(styles, query);

            }
        }

        // now, if a definition query has been established for this layer, be
        // sure to respect it by combining it with the bounding box one.
        // Currently this definition query is being set dynamically in geoserver
        // as per the user's filter, maxFeatures and startIndex WMS GetMap custom parameters
        definitionQuery = currLayer.getQuery();

        if (definitionQuery != Query.ALL) {
            if (query == Query.ALL) {
                query = new Query(definitionQuery);
            } else {
                query = new Query(DataUtilities.mixQueries(
                        definitionQuery, query, "liteRenderer"));
            }
        }
        query.setCoordinateSystem(featCrs);

        // prepare hints
        // ... basic one, we want fast and compact coordinate sequences and geometries optimized
        // for the collection of one item case (typical in shapefiles)
        LiteCoordinateSequenceFactory csFactory = new LiteCoordinateSequenceFactory();
        GeometryFactory gFactory = new SimpleGeometryFactory(csFactory);
        Hints hints = new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, csFactory);
        hints.put(Hints.JTS_GEOMETRY_FACTORY, gFactory);
        hints.put(Hints.FEATURE_2D, Boolean.TRUE);
        
        // update the screenmaps
        try {
            CoordinateReferenceSystem crs = getNativeCRS(schema, Arrays.asList(attributes));
            if(crs != null) {
                Set<RenderingHints.Key> fsHints = source.getSupportedHints();
                
                MathTransform mt = buildFullTransform(crs, mapCRS, worldToScreenTransform);
                double[] spans = Decimator.computeGeneralizationDistances(mt.inverse(), screenSize, generalizationDistance);
                double distance = spans[0] < spans[1] ? spans[0] : spans[1];
                for (LiteFeatureTypeStyle fts : styles) {
                    if(fts.screenMap != null) {
                        fts.screenMap.setTransform(mt);
                        fts.screenMap.setSpans(spans[0], spans[1]);
                        if(fsHints.contains(Hints.SCREENMAP)) {
                            // replace the renderer screenmap with the hint, and avoid doing
                            // the work twice
                            hints.put(Hints.SCREENMAP, fts.screenMap);
                            fts.screenMap = null;
                        }
                    }
                }
            
                // ... if possible we let the datastore do the generalization
                if(fsHints.contains(Hints.GEOMETRY_SIMPLIFICATION)) {
                    // good, we don't need to perform in memory generalization, the datastore
                    // does it all for us
                    hints.put(Hints.GEOMETRY_SIMPLIFICATION, distance);
                    inMemoryGeneralization = false;
                } else if(fsHints.contains(Hints.GEOMETRY_DISTANCE)) {
                    // in this case the datastore can get us close, but we can still
                    // perform some in memory generalization
                    hints.put(Hints.GEOMETRY_DISTANCE, distance);
                }
            }
            
        } catch(Exception e) {
            LOGGER.log(Level.INFO, "Error computing the generalization hints", e);
        }

        if(query.getHints() == null) {
            query.setHints(hints);
        } else {
            query.getHints().putAll(hints);
        }

        // simplify the filter
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        Filter simplifiedFilter = (Filter) query.getFilter().accept(simplifier, null);
        query.setFilter(simplifiedFilter);

        return source.getFeatures(query);
    }


    /**
     * Takes care of eventual geometric transformations
     * @param styles
     * @param envelope
     * @return
     */
    ReferencedEnvelope expandEnvelopeByTransformations(LiteFeatureTypeStyle[] styles,
            ReferencedEnvelope envelope) {
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = new ReferencedEnvelope(envelope);
        for (LiteFeatureTypeStyle lts : styles) {
            List<Rule> rules = new ArrayList<Rule>();
            rules.addAll(Arrays.asList(lts.ruleList));
            rules.addAll(Arrays.asList(lts.elseRules));
            for (Rule r : rules) {
                for (Symbolizer s : r.symbolizers()) {
                    if(s.getGeometry() != null)
                        result.expandToInclude((ReferencedEnvelope) s.getGeometry().accept(visitor, envelope));
                }
            }
        }

        return result;
    }

    /**
     * Builds a full transform going from the source CRS to the denstionan CRS
     * and from there to the screen
     */
    private MathTransform2D buildFullTransform(CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem destCRS, AffineTransform worldToScreenTransform)
    throws FactoryException {
        MathTransform2D mt = buildTransform(sourceCRS, destCRS);

        // concatenate from world to screen
        if (mt != null && !mt.isIdentity()) {
            mt = (MathTransform2D) ConcatenatedTransform
            .create(mt, ProjectiveTransform.create(worldToScreenTransform));
        } else {
            mt = (MathTransform2D) ProjectiveTransform.create(worldToScreenTransform);
        }

        return mt;
    }

    /**
     * Builds the transform from sourceCRS to destCRS
     * @param sourceCRS
     * @param destCRS
     * @return the transform, or null if any of the crs is null, or if the the two crs are equal
     * @throws FactoryException
     */
    private MathTransform2D buildTransform(CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem destCRS) throws FactoryException {
        // the basic crs transformation, if any
        MathTransform2D mt;
        if (sourceCRS == null || destCRS == null || CRS.equalsIgnoreMetadata(sourceCRS,
                destCRS))
            mt = null;
        else
            mt = (MathTransform2D) CRS.findMathTransform(sourceCRS, destCRS, true);
        return mt;
    }

    /**
     * Scans the schema for the specified attributes are returns a single CRS
     * if all the geometric attributes in the lot share one CRS, null if
     * there are different ones
     * @param schema
     * @return
     */
    private CoordinateReferenceSystem getNativeCRS(FeatureType schema, List<String> attNames) {
        // first off, check how many crs we have, this hint works only
        // if we have just one native CRS at hand (and the native CRS is known
        CoordinateReferenceSystem crs = null;
        for (PropertyDescriptor att : schema.getDescriptors()) {
            if(!attNames.contains(att.getName().getLocalPart()))
                continue;

            if(att instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) att;
                CoordinateReferenceSystem gdCrs = gd.getCoordinateReferenceSystem();
                if(crs == null) {
                    crs = gdCrs;
                } else if(gdCrs == null) {
                    crs = null;
                    break;
                } else if(!CRS.equalsIgnoreMetadata(crs, gdCrs)) {
                    crs = null;
                    break;
                }
            }
        }
        return crs;
    }

    /**
     * JE: If there is a single rule "and" its filter together with the query's
     * filter and send it off to datastore. This will allow as more processing
     * to be done on the back end... Very useful if DataStore is a database.
     * Problem is that worst case each filter is ran twice. Next we will modify
     * it to find a "Common" filter between all rules and send that to the
     * datastore.
     * 
     * DJB: trying to be smarter. If there are no "elseRules" and no rules w/o a
     * filter, then it makes sense to send them off to the Datastore We limit
     * the number of Filters sent off to the datastore, just because it could
     * get a bit rediculous. In general, for a database, if you can limit 10% of
     * the rows being returned you're probably doing quite well. The main
     * problem is when your filters really mean you're secretly asking for all
     * the data in which case sending the filters to the Datastore actually
     * costs you. But, databases are *much* faster at processing the Filters
     * than JAVA is and can use statistical analysis to do it.
     * 
     * @param styles
     * @param q
     */

    private void processRuleForQuery(LiteFeatureTypeStyle[] styles, Query q) {
        try {

            // first we check to see if there are >
            // "getMaxFiltersToSendToDatastore" rules
            // if so, then we dont do anything since no matter what there's too
            // many to send down.
            // next we check for any else rules. If we find any --> dont send
            // anything to Datastore
            // next we check for rules w/o filters. If we find any --> dont send
            // anything to Datastore
            //
            // otherwise, we're gold and can "or" together all the filters then
            // AND it with the original filter.
            // ie. SELECT * FROM ... WHERE (the_geom && BBOX) AND (filter1 OR
            // filter2 OR filter3);

            final int maxFilters = getMaxFiltersToSendToDatastore();
            final List<Filter> filtersToDS = new ArrayList<Filter>();
            // look at each featuretypestyle
            for(LiteFeatureTypeStyle style : styles) {
                if (style.elseRules.length > 0) // uh-oh has elseRule
                    return;
                // look at each rule in the featuretypestyle
                for(Rule r : style.ruleList) {
                    if (r.getFilter() == null)
                        return; // uh-oh has no filter (want all rows)
                    filtersToDS.add(r.getFilter());
                }
            }

            // if too many bail out
            if (filtersToDS.size() > maxFilters)
                return;

            // or together all the filters
            org.opengis.filter.Filter ruleFiltersCombined;
            if (filtersToDS.size() == 1) {
                ruleFiltersCombined = (Filter) filtersToDS.get(0);
            } else {
                ruleFiltersCombined = filterFactory.or(filtersToDS); 
            }

            // combine with the pre-existing filter
            ruleFiltersCombined = filterFactory.and(
                    q.getFilter(), ruleFiltersCombined);
            q.setFilter(ruleFiltersCombined);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.SEVERE,
                        "Could not send rules to datastore due to: "
                        + e.getLocalizedMessage(), e);
        }
    }

    /**
     * find out the maximum number of filters we're going to send off to the
     * datastore. See processRuleForQuery() for details.
     * 
     */
    private int getMaxFiltersToSendToDatastore() {
        try {
            if (rendererHints == null)
                return defaultMaxFiltersToSendToDatastore;
            
            Integer result = (Integer) rendererHints.get("maxFiltersToSendToDatastore");
            if (result == null)
                return defaultMaxFiltersToSendToDatastore; // default if not
            // present in hints
            return result.intValue();

        } catch (Exception e) {
            return defaultMaxFiltersToSendToDatastore;
        }
    }

    /**
     * Checks if optimized feature type style rendering is enabled, or not.
     * See {@link #OPTIMIZE_FTS_RENDERING_KEY} description for a full explanation.
     */
    private boolean isOptimizedFTSRenderingEnabled() {
        if (rendererHints == null)
            return true;
        Object result = rendererHints.get(OPTIMIZE_FTS_RENDERING_KEY);
        if (result == null)
            return true;
        return Boolean.TRUE.equals(result);
    }

    /**
     * Checks if the advanced projection handling is enabled
     * @return
     */
    private boolean isAdvancedProjectionHandlingEnabled() {
        if (rendererHints == null)
            return false;
        Object result = rendererHints.get(ADVANCED_PROJECTION_HANDLING_KEY);
        if (result == null)
            return false;
        return Boolean.TRUE.equals(result);
    }
    
    /**
     * Checks if continuous map wrapping is enabled
     * @return
     */
    private boolean isMapWrappingEnabled() {
        if (rendererHints == null)
            return false;
        Object result = rendererHints.get(CONTINUOUS_MAP_WRAPPING);
        if (result == null)
            return false;
        return Boolean.TRUE.equals(result);
    }

    /**
     * Checks if vector rendering is enabled or not.
     * See {@link SLDStyleFactory#isVectorRenderingEnabled()} for a full explanation.
     */
    private boolean isVectorRenderingEnabled() {
        if (rendererHints == null)
            return true;
        Object result = rendererHints.get(VECTOR_RENDERING_KEY);
        if (result == null)
            return VECTOR_RENDERING_ENABLED_DEFAULT;
        return ((Boolean)result).booleanValue();
    }

    /**
     * Returns an estimate of the rendering buffer needed to properly display this
     * layer taking into consideration the constant stroke sizes in the feature type
     * styles.
     * 
     * @param styles
     *            the feature type styles to be applied to the layer
     * @return an estimate of the buffer that should be used to properly display a layer
     *         rendered with the specified styles 
     */
    private int findRenderingBuffer(LiteFeatureTypeStyle[] styles) {
        final MetaBufferEstimator rbe = new MetaBufferEstimator();

        for (int t = 0; t < styles.length; t++) {
            final LiteFeatureTypeStyle lfts = styles[t];
            Rule[] rules = lfts.elseRules;
            for (int j = 0; j < rules.length; j++) {
                rbe.visit(rules[j]);
            }
            rules = lfts.ruleList;
            for (int j = 0; j <  rules.length; j++) {
                rbe.visit(rules[j]);
            }
        }

        if(!rbe.isEstimateAccurate())
            LOGGER.warning("Assuming rendering buffer = " + rbe.getBuffer() 
                    + ", but estimation is not accurate, you may want to set a buffer manually");
        
        // the actual amount we have to grow the rendering area by is half of the stroke/symbol sizes
        // plus one extra pixel for antialiasing effects
        return (int) Math.round(rbe.getBuffer() / 2.0 + 1);
    }

    /**
     * Inspects the <code>MapLayer</code>'s style and retrieves it's needed
     * attribute names, returning at least the default geometry attribute name.
     * 
     * @param layer
     *            the <code>MapLayer</code> to determine the needed attributes
     *            from
     * @param schema
     *            the <code>layer</code>'s FeatureSource<SimpleFeatureType, SimpleFeature> schema
     * @return the minimum set of attribute names needed to render
     *         <code>layer</code>
     */
    private String[] findStyleAttributes(LiteFeatureTypeStyle[] styles,
            FeatureType schema) {
        final StyleAttributeExtractor sae = new StyleAttributeExtractor();

        LiteFeatureTypeStyle lfts;
        Rule[] rules;
        int rulesLength;
        final int length = styles.length;
        for (int t = 0; t < length; t++) {
            lfts = styles[t];
            rules = lfts.elseRules;
            rulesLength = rules.length;
            for (int j = 0; j < rulesLength; j++) {
                sae.visit(rules[j]);
            }
            rules = lfts.ruleList;
            rulesLength = rules.length;
            for (int j = 0; j < rulesLength; j++) {
                sae.visit(rules[j]);
            }
        }

        String[] ftsAttributes = sae.getAttributeNames();

        /*
         * DJB: this is an old comment - erase it soon (see geos-469 and below) -
         * we only add the default geometry if it was used.
         * 
         * GR: if as result of sae.getAttributeNames() ftsAttributes already
         * contains geometry attribute names, they gets duplicated, which produces
         * an error in AbstracDatastore when trying to create a derivate
         * SimpleFeatureType. So I'll add the default geometry only if it is not
         * already present, but: should all the geometric attributes be added by
         * default? I will add them, but don't really know what's the expected
         * behavior
         */
        List atts = new LinkedList(Arrays.asList(ftsAttributes));
        Collection<PropertyDescriptor> attTypes = schema.getDescriptors();
        String attName;

        final int attTypesLength = attTypes.size();
        for (PropertyDescriptor pd : attTypes) {
            attName = pd.getName().getLocalPart();

            // DJB: This geometry check was commented out. I think it should
            // actually be back in or
            // you get ALL the attributes back, which isn't what you want.
            // ALX: For rasters I need even the "grid" attribute.

            // DJB:geos-469, we do not grab all the geometry columns.
            // for symbolizers, if a geometry is required it is either
            // explicitly named
            // ("<Geometry><PropertyName>the_geom</PropertyName></Geometry>")
            // or the default geometry is assumed (no <Geometry> element).
            // I've modified the style attribute extractor so it tracks if the
            // default geometry is used. So, we no longer add EVERY geometry
            // column to the query!!

            if ((attName.equalsIgnoreCase("grid"))
                    && !atts.contains(attName)||
                    (attName.equalsIgnoreCase("params"))
                    && !atts.contains(attName)) {
                atts.add(attName);
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("added attribute " + attName);
            }
        }

        try {
            // DJB:geos-469 if the default geometry was used in the style, we
            // need to grab it.
            if (sae.getDefaultGeometryUsed()
                    && (!atts.contains(schema.getGeometryDescriptor().getLocalName()))) {
                atts.add(schema.getGeometryDescriptor().getLocalName());
            }
        } catch (Exception e) {
            // might not be a geometry column. That will cause problems down the
            // road (why render a non-geometry layer)
        }

        ftsAttributes = new String[atts.size()];
        atts.toArray(ftsAttributes);

        return ftsAttributes;
    }

    /**
     * Creates the bounding box filters (one for each geometric attribute)
     * needed to query a <code>MapLayer</code>'s feature source to return
     * just the features for the target rendering extent
     * 
     * @param schema
     *            the layer's feature source schema
     * @param attributes
     *            set of needed attributes
     * @param bbox
     *            the expression holding the target rendering bounding box
     * @return an or'ed list of bbox filters, one for each geometric attribute
     *         in <code>attributes</code>. If there are just one geometric
     *         attribute, just returns its corresponding
     *         <code>GeometryFilter</code>.
     * @throws IllegalFilterException
     *             if something goes wrong creating the filter
     */
    private Filter createBBoxFilters(FeatureType schema, String[] attributes,
            List<ReferencedEnvelope> bboxes) throws IllegalFilterException {
        Filter filter = Filter.INCLUDE;
        final int length = attributes.length;
        PropertyDescriptor attType;

        for (int j = 0; j < length; j++) {
            attType = schema.getDescriptor(attributes[j]);

            // DJB: added this for better error messages!
            if (attType == null) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine(new StringBuffer("Could not find '").append(
                            attributes[j]).append("' in the FeatureType (")
                            .append(schema.getName()).append(")")
                            .toString());
                throw new IllegalFilterException(new StringBuffer(
                "Could not find '").append(
                        attributes[j] + "' in the FeatureType (").append(
                                schema.getName()).append(")").toString());
            }

            if (attType instanceof GeometryDescriptor) {
                String localName = ((GeometryDescriptor) attType).getLocalName();
                Filter gfilter = new FastBBOX(localName, bboxes.get(0), filterFactory);

                if (filter == Filter.INCLUDE) {
                    filter = gfilter;
                } else {
                    filter = filterFactory.or( filter, gfilter );
                }

                if(bboxes.size() > 0) {
                    for (int k = 1; k < bboxes.size(); k++) {
                        filter = filterFactory.or( filter, new FastBBOX(localName, bboxes.get(k), filterFactory) );
                    }
                }
            }
        }

        return filter;
    }

    /**
     * Checks if a rule can be triggered at the current scale level
     * 
     * @param r
     *            The rule
     * @return true if the scale is compatible with the rule settings
     */
    private boolean isWithInScale(Rule r) {
        return ((r.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
        && ((r.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
    }

    /**
     * <p>Creates a list of <code>LiteFeatureTypeStyle</code>s with:
     * <ol type="a">
     * <li>out-of-scale rules removed</li>
     * <li>incompatible FeatureTypeStyles removed</li>
     * </ol>
     * </p>
     * 
     * <p><em><strong>Note:</strong> This method has a lot of duplication with 
     * {@link #createLiteFeatureTypeStyles(FeatureTypeStyle[], SimpleFeatureType, Graphics2D)}. 
     * </em></p>
     * 
     * @param featureStyles Styles to process
     * @param typeDescription The type description that has to be matched
     * @return ArrayList<LiteFeatureTypeStyle>
     */
    private ArrayList<LiteFeatureTypeStyle> createLiteFeatureTypeStyles(FeatureTypeStyle[] featureStyles, 
            Object typeDescription, Graphics2D graphics) throws IOException {
        ArrayList<LiteFeatureTypeStyle> result = new ArrayList<LiteFeatureTypeStyle>();

        List<Rule> rules;
        List<Rule> ruleList;
        List<Rule> elseRuleList;
        LiteFeatureTypeStyle lfts;
        BufferedImage image;

        final int length = featureStyles.length;
        for (int i = 0; i < length; i++) {
            FeatureTypeStyle fts = featureStyles[i];

            if (typeDescription == null || typeDescription.toString().indexOf( fts.getFeatureTypeName() ) == -1) 
                continue; 

            // get applicable rules at the current scale
            rules = fts.rules();
            ruleList = new ArrayList<Rule>();
            elseRuleList = new ArrayList<Rule>();

            // gather the active rules
            for(Rule r : rules) {
                if (isWithInScale(r)) {
                    if (r.isElseFilter()) {
                        elseRuleList.add(r);
                    } else {
                        ruleList.add(r);
                    }
                }
            }
            
            // nothing to render, don't do anything!!
            if ((ruleList.size() == 0) && (elseRuleList.size() == 0))
                continue; 

            // first fts, we can reuse the graphics directly
            if (result.size() == 0 || !isOptimizedFTSRenderingEnabled()) {
                lfts = new LiteFeatureTypeStyle(graphics, ruleList, elseRuleList);
            } else {
                lfts = new LiteFeatureTypeStyle(new DelayedBackbufferGraphic(graphics, screenSize), ruleList, elseRuleList);
            }
            result.add(lfts);
        }

        return result;
    }

    /**
     * creates a list of LiteFeatureTypeStyles a) out-of-scale rules removed b)
     * incompatible FeatureTypeStyles removed
     * 
     * 
     * @param featureStylers
     * @param features
     * @throws Exception
     * @return ArrayList<LiteFeatureTypeStyle>
     */
    private ArrayList<LiteFeatureTypeStyle> createLiteFeatureTypeStyles(
            FeatureTypeStyle[] featureStyles, FeatureType ftype,
            Graphics2D graphics) throws IOException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("creating rules for scale denominator - "
                    + NumberFormat.getNumberInstance().format(scaleDenominator));
        ArrayList<LiteFeatureTypeStyle> result = new ArrayList<LiteFeatureTypeStyle>();

        LiteFeatureTypeStyle lfts;
        for (FeatureTypeStyle fts : featureStyles) {
            if (isFeatureTypeStyleActive(ftype, fts)) {
                // DJB: this FTS is compatible with this FT.

                // get applicable rules at the current scale
                List[] splittedRules = splitRules(fts);
                List ruleList = splittedRules[0];
                List elseRuleList = splittedRules[1];

                // if none, skip it
                if ((ruleList.size() == 0) && (elseRuleList.size() == 0))
                    continue; 

                // we can optimize this one!
                if (result.size() == 0 || !isOptimizedFTSRenderingEnabled()) {
                    lfts = new LiteFeatureTypeStyle(graphics, ruleList,
                            elseRuleList);
                } else {
//                    BufferedImage image = graphics.getDeviceConfiguration().createCompatibleImage(screenSize.width,
//                            screenSize.height, Transparency.TRANSLUCENT);
//                    lfts = new LiteFeatureTypeStyle(image, graphics
//                            .getTransform(), ruleList, elseRuleList,
//                            java2dHints);
                    lfts = new LiteFeatureTypeStyle(new DelayedBackbufferGraphic(graphics, screenSize), ruleList, elseRuleList);
                }
                if (screenMapEnabled(lfts)) {
                    lfts.screenMap = new ScreenMap(screenSize.x, screenSize.y, screenSize.width,
                            screenSize.height);
                }
                                                   
                result.add(lfts);
            }
        }

        return result;
    }

    
    /**
     * Returns true if the ScreenMap optimization can be applied given the current renderer and
     * configuration and the style to be applied
     * 
     * @param lfts
     * @return
     */
    boolean screenMapEnabled(LiteFeatureTypeStyle lfts) {
        if (generalizationDistance == 0.0) {
            return false;
        }

        OpacityFinder finder = new OpacityFinder(new Class[] { PointSymbolizer.class,
                LineSymbolizer.class, PolygonSymbolizer.class });
        for (Rule r : lfts.ruleList) {
            r.accept(finder);
        }
        for (Rule r : lfts.elseRules) {
            r.accept(finder);
        }

        return !finder.hasOpacity;
    }


    private boolean isFeatureTypeStyleActive(FeatureType ftype, FeatureTypeStyle fts) {
        // TODO: find a complex feature equivalent for this check
        return fts.featureTypeNames().isEmpty() || ((ftype.getName().getLocalPart() != null)
                && (ftype.getName().getLocalPart().equalsIgnoreCase(fts.getFeatureTypeName()) || 
                        FeatureTypes.isDecendedFrom(ftype, null, fts.getFeatureTypeName())));
    }

    private List[] splitRules(FeatureTypeStyle fts) {
        Rule[] rules;
        List<Rule> ruleList = new ArrayList<Rule>();
        List<Rule> elseRuleList = new ArrayList<Rule>();

        rules = fts.getRules();
        ruleList = new ArrayList();
        elseRuleList = new ArrayList();

        for (int j = 0; j < rules.length; j++) {
            // getting rule
            Rule r = rules[j];

            if (isWithInScale(r)) {
                if (r.isElseFilter()) {
                    elseRuleList.add(r);
                } else {
                    ruleList.add(r);
                }
            }
        }

        return new List[] {ruleList, elseRuleList};
    }

    /**
     * When drawing in optimized mode a 32bit surface is created for each FeatureTypeStyle
     * other than the first in order to draw features in parallel while respecting the
     * feature draw ordering multiple FTS impose. This method allows to estimate how many
     * megabytes will be needed, in terms of back buffers, to draw the current {@link MapContext},
     * assuming the feature type style optimizations are turned on (in the case they are off,
     * no extra memory will be used).
     * @param width the image width
     * @param height the image height
     */
    public int getMaxBackBufferMemory(int width, int height) {
        int maxBuffers = 0;
        for (MapLayer layer : context.getLayers()) {
            if (!layer.isVisible()) {
                // Only render layer when layer is visible
                continue;
            }

            // skip layers that do have only one fts
            if(layer.getStyle().getFeatureTypeStyles().length < 2)
                continue;

            // count how many lite feature type styles are active
            int currCount = 0;
            FeatureType ftype = layer.getFeatureSource().getSchema();
            for (FeatureTypeStyle fts : layer.getStyle().getFeatureTypeStyles()) {
                if (isFeatureTypeStyleActive(ftype, fts)) {
                    // get applicable rules at the current scale
                    List[] splittedRules = splitRules(fts);
                    List ruleList = splittedRules[0];
                    List elseRuleList = splittedRules[1];

                    // if none, skip this fts
                    if ((ruleList.size() == 0) && (elseRuleList.size() == 0))
                        continue; 

                    currCount++;
                }
            }
            // consider the first fts does not allocate a buffer
            currCount--;

            if(currCount > maxBuffers)
                maxBuffers = currCount;
        }

        return maxBuffers * width * height * 4;
    }

    /**
     * Prepair a FeatureCollection<SimpleFeatureType, SimpleFeature> for display, this method formally ensured that a FeatureReader
     * produced the correct CRS and has now been updated to work with FeatureCollection.
     * <p>
     * What is really going on is the need to set up for reprojection; but *after* decimation has
     * occured.
     * </p>
     *  
     * @param features
     * @param sourceCrs
     * @return FeatureCollection<SimpleFeatureType, SimpleFeature> that produces results with the correct CRS
     */
    private FeatureCollection prepFeatureCollection(FeatureCollection features, CoordinateReferenceSystem sourceCrs ) {
        // DJB: dont do reprojection here - do it after decimation
        // but we ensure that the reader is producing geometries with
        // the correct CRS
        // NOTE: it, by default, produces ones that are are tagged with
        // the CRS of the datastore, which
        // maybe incorrect.
        // The correct value is in sourceCrs.

        // this is the reader's CRS
        CoordinateReferenceSystem rCS = null;
        try {
            rCS = features.getSchema().getGeometryDescriptor().getType().getCoordinateReferenceSystem();
        } catch(NullPointerException e) {
            // life sucks sometimes
        }

        // sourceCrs == source's real SRS
        // if we need to recode the incoming geometries

        if (rCS != sourceCrs) // not both null or both EXACTLY the
            // same CRS object
        {
            if (sourceCrs != null) // dont re-tag to null, keep the
                // DataStore's CRS (this shouldnt
                // really happen)
            {
                // if the datastore is producing null CRS, we recode.
                // if the datastore's CRS != real CRS, then we recode
                if ((rCS == null) || !CRS.equalsIgnoreMetadata(rCS, sourceCrs)) {
                    // need to retag the features
                    try {
                        if( features instanceof SimpleFeatureCollection){
                            return new ForceCoordinateSystemFeatureResults( (SimpleFeatureCollection) features, sourceCrs );
                        }
                    } catch (Exception ee) {
                        LOGGER.log(Level.WARNING, ee.getLocalizedMessage(), ee);
                    }
                }
            }
        }
        return features;
    }

    /**
     * Applies each feature type styler in turn to all of the features. This
     * perhaps needs some explanation to make it absolutely clear.
     * featureStylers[0] is applied to all features before featureStylers[1] is
     * applied. This can have important consequences as regards the painting
     * order.
     * <p>
     * In most cases, this is the desired effect. For example, all line features
     * may be rendered with a fat line and then a thin line. This produces a
     * 'cased' effect without any strange overlaps.
     * </p>
     * <p>
     * This method is internal and should only be called by render.
     * </p>
     * <p>
     * </p>
     * 
     * @param graphics
     *            DOCUMENT ME!
     * @param features
     *            An array of features to be rendered
     * @param featureStylers
     *            An array of feature stylers to be applied
     * @param at
     *            DOCUMENT ME!
     * @param destinationCrs -
     *            The destination CRS, or null if no reprojection is required
     * @param screenSize
     * @param layerId 
     * @throws IOException
     * @throws IllegalFilterException
     */
    final private void processStylers(final Graphics2D graphics,
            MapLayer currLayer, AffineTransform at,
            CoordinateReferenceSystem destinationCrs, Envelope mapArea,
            Rectangle screenSize, String layerId) throws IllegalFilterException, IOException {

        /*
         * DJB: changed this a wee bit so that it now does the layer query AFTER
         * it has evaluated the rules for scale inclusion. This makes it so that
         * geometry columns (and other columns) will not be queried unless they
         * are actually going to be required. see geos-469
         */
        // /////////////////////////////////////////////////////////////////////
        //
        // Preparing feature information and styles
        //
        // /////////////////////////////////////////////////////////////////////
        final FeatureTypeStyle[] featureStylers = currLayer.getStyle().getFeatureTypeStyles();

        final FeatureSource featureSource =  currLayer.getFeatureSource();

        Collection collection = null;
        FeatureCollection features = null;

        final CoordinateReferenceSystem sourceCrs;
        final NumberRange scaleRange = new NumberRange(scaleDenominator,scaleDenominator);
        final ArrayList<LiteFeatureTypeStyle> lfts ;

        if ( featureSource != null ) {
            final FeatureType schema = featureSource.getSchema();

            final GeometryDescriptor geometryAttribute = schema.getGeometryDescriptor();
            sourceCrs = geometryAttribute.getType().getCoordinateReferenceSystem();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(new StringBuffer("processing ").append(
                        featureStylers.length).append(" stylers for ").append(
                                currLayer.getFeatureSource().getSchema().getName())
                                .toString());
            }
            // transformMap = new HashMap();
            lfts = createLiteFeatureTypeStyles(featureStylers,schema, graphics);
            if(lfts.size() == 0)
                return;
            
            applyUnitRescale(lfts);

            LiteFeatureTypeStyle[] featureTypeStyleArray = (LiteFeatureTypeStyle[]) lfts.toArray(new LiteFeatureTypeStyle[lfts.size()]);
            // /////////////////////////////////////////////////////////////////////
            //
            // DJB: get a featureresults (so you can get a feature reader) for the
            // data
            //
            // /////////////////////////////////////////////////////////////////////

            // ... assume we have to do the generalization, the query layer process will
            // turn down the flag if we don't 
            inMemoryGeneralization = true;
            features = queryLayer(currLayer, featureSource, schema,
                    featureTypeStyleArray,
                    mapArea, destinationCrs, sourceCrs, screenSize,
                    geometryAttribute, at);

            features = prepFeatureCollection( features, sourceCrs );        	
        } else {
            CollectionSource source = currLayer.getSource();
            collection = queryLayer( currLayer, currLayer.getSource() );

            sourceCrs = null;
            lfts = createLiteFeatureTypeStyles( featureStylers, source.describe(), graphics );
            applyUnitRescale(lfts);
        }

        if (lfts.size() == 0) return; // nothing to do

        // finally, perform rendering
        if(isOptimizedFTSRenderingEnabled() && lfts.size() > 1) {
            drawOptimized(graphics, currLayer, at, destinationCrs, layerId, collection, features,
                    scaleRange, lfts);
        } else {
            drawPlain(graphics, currLayer, at, destinationCrs, layerId, collection, features,
                    scaleRange, lfts);
        }
    }

    /**
     * Applies Unit Of Measure rescaling against all symbolizers, the result will be symbolizers
     * that operate purely in pixels
     * @param lfts
     */
    void applyUnitRescale(final ArrayList<LiteFeatureTypeStyle> lfts) {
        // apply UOM rescaling
        double pixelsPerMeters = RendererUtilities.calculatePixelsPerMeterRatio(scaleDenominator, rendererHints);
        UomRescaleStyleVisitor rescaleVisitor = new UomRescaleStyleVisitor(pixelsPerMeters);
        for(LiteFeatureTypeStyle fts : lfts) {
            rescaleFeatureTypeStyle(fts, rescaleVisitor);
        }
        
        // apply dpi rescale
        double dpi = RendererUtilities.getDpi(getRendererHints());
        double standardDpi = RendererUtilities.getDpi(Collections.emptyMap());
        if(dpi != standardDpi) {
            double scaleFactor = dpi / standardDpi;
            RescaleStyleVisitor dpiVisitor = new RescaleStyleVisitor(scaleFactor);
            for(LiteFeatureTypeStyle fts : lfts) {
                rescaleFeatureTypeStyle(fts, dpiVisitor);
            }
        }
    }
    
    /**
     * Utility method to apply the two rescale visitors without duplicating code
     * @param fts
     * @param visitor
     */
    void rescaleFeatureTypeStyle(LiteFeatureTypeStyle fts, DuplicatingStyleVisitor visitor) {
        for (int i = 0; i < fts.ruleList.length; i++) {
            visitor.visit(fts.ruleList[i]);
            fts.ruleList[i] = (Rule) visitor.getCopy();
        }
        if(fts.elseRules != null) {
            for (int i = 0; i < fts.elseRules.length; i++) {
                visitor.visit(fts.elseRules[i]);
                fts.elseRules[i] = (Rule) visitor.getCopy();
            }
        }
    }

    /**
     * Performs all rendering on the user provided graphics object by scanning
     * the collection multiple times, one for each feature type style provided
     */
    private void drawPlain(final Graphics2D graphics, MapLayer currLayer, AffineTransform at,
            CoordinateReferenceSystem destinationCrs, String layerId, Collection collection,
            FeatureCollection features, final NumberRange scaleRange, final ArrayList lfts) {
        final LiteFeatureTypeStyle[] fts_array = (LiteFeatureTypeStyle[]) lfts
        .toArray(new LiteFeatureTypeStyle[lfts.size()]);

        // for each lite feature type style, scan the whole collection and draw
        for (LiteFeatureTypeStyle liteFeatureTypeStyle : fts_array) {
            Iterator iterator = null;
            if (collection != null)
                iterator = collection.iterator();
            if (features != null)
                iterator = features.iterator();

            if (iterator == null)
                return; // nothing to do

            try {
                boolean clone = isCloningRequired(currLayer, fts_array);
                RenderableFeature rf = new RenderableFeature(currLayer, clone);
                // loop exit condition tested inside try catch
                // make sure we test hasNext() outside of the try/cath that follows, as that
                // one is there to make sure a single feature error does not ruin the rendering
                // (best effort) whilst an exception in hasNext() + ignoring catch results in
                // an infinite loop
                while (iterator.hasNext() && !renderingStopRequested) {
                    try {
                        rf.setFeature(iterator.next());
                        process(rf, liteFeatureTypeStyle, scaleRange, at, destinationCrs, layerId);
                    } catch (Throwable tr) {
                        fireErrorEvent(tr);
                    }
                }
            } finally {
                if (collection instanceof FeatureCollection) {
                    FeatureCollection resource = (FeatureCollection) collection;
                    resource.close(iterator);
                } else if (features != null) {
                    features.close(iterator);
                }
            }
        }
    }

    /**
     * Performs rendering so that the collection is scanned only once even in presence
     * of multiple feature type styles, using the in memory buffer for each feature type
     * style other than the first one (that uses the graphics provided by the user)s 
     */
    private void drawOptimized(final Graphics2D graphics, MapLayer currLayer, AffineTransform at,
            CoordinateReferenceSystem destinationCrs, String layerId, Collection collection,
            FeatureCollection features, final NumberRange scaleRange, final ArrayList lfts) {
        Iterator iterator = null;
        if( collection != null ) iterator = collection.iterator();        
        if( features != null ) iterator = features.iterator();

        if( iterator == null ) return; // nothing to do

        final LiteFeatureTypeStyle[] fts_array = (LiteFeatureTypeStyle[]) lfts
        .toArray(new LiteFeatureTypeStyle[lfts.size()]);

        try {
            boolean clone = isCloningRequired(currLayer, fts_array);
            RenderableFeature rf = new RenderableFeature(currLayer, clone);
            // loop exit condition tested inside try catch
            // make sure we test hasNext() outside of the try/cath that follows, as that
            // one is there to make sure a single feature error does not ruin the rendering
            // (best effort) whilst an exception in hasNext() + ignoring catch results in
            // an infinite loop
            while (iterator.hasNext() && !renderingStopRequested) { 
                try {
                    rf.setFeature(iterator.next());
                    // draw the feature on the main graphics and on the eventual extra image buffers
                    for (LiteFeatureTypeStyle liteFeatureTypeStyle : fts_array) {
                        rf.setScreenMap(liteFeatureTypeStyle.screenMap);
                        process(rf, liteFeatureTypeStyle, scaleRange, at, destinationCrs, layerId);

                    }
                } catch (Throwable tr) {
                    fireErrorEvent(tr);
                }
            }
            
            // submit the merge request
            requests.put(new MergeLayersRequest(graphics, fts_array));
        }catch(InterruptedException e) {
            fireErrorEvent(e);
        } finally {
            if( collection instanceof FeatureCollection ){
                FeatureCollection resource = (FeatureCollection ) collection;
                resource.close( iterator );
            } else if(features != null) {
                features.close( iterator );
            }
        } 
    }

    /**
     * Tells if geometry cloning is required or not
     */
    private boolean isCloningRequired(MapLayer layer, LiteFeatureTypeStyle[] lfts) {
        // check if the features are detached, we can thus modify the geometries in place
        final Set<Key> hints = layer.getFeatureSource().getSupportedHints();
        if(!hints.contains(Hints.FEATURE_DETACHED))
            return true;

        // check if there is any conflicting geometry transformation.
        // No geometry transformations -> we can modify geometries in place
        // Just one geometry transformation over an attribute -> we can modify geometries in place
        // Two tx over the same attribute, or straight usage and a tx -> we have to preserve the 
        // original geometry as well, thus we need cloning
        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        FeatureType featureType = layer.getFeatureSource().getSchema();
        Set<String> plainGeometries = new java.util.HashSet<String>();
        Set<String> txGeometries = new java.util.HashSet<String>();
        for (LiteFeatureTypeStyle lft : lfts) {
            for(Rule r: lft.ruleList) {
                for(Symbolizer s: r.symbolizers()) {
                    if(s.getGeometry() == null) {
                        String attribute = featureType.getGeometryDescriptor().getName().getLocalPart();
                        if(txGeometries.contains(attribute))
                            return true;
                        plainGeometries.add(attribute);
                    } else if(s.getGeometry() instanceof PropertyName) {
                        String attribute = ((PropertyName) s.getGeometry()).getPropertyName();
                        if(txGeometries.contains(attribute))
                            return true;
                        plainGeometries.add(attribute);
                    } else {
                        Expression g = s.getGeometry();
                        extractor.clear();
                        g.accept(extractor, null);
                        Set<String> attributes = extractor.getAttributeNameSet();
                        for (String attribute : attributes) {
                            if(plainGeometries.contains(attribute))
                                return true;
                            if(txGeometries.contains(attribute))
                                return true;
                            txGeometries.add(attribute);
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param rf
     * @param feature 
     * @param fts
     * @param layerId 
     */
    final private void process(RenderableFeature rf, LiteFeatureTypeStyle fts,
            NumberRange scaleRange, AffineTransform at,
            CoordinateReferenceSystem destinationCrs, String layerId)
            throws Exception {
        boolean doElse = true;
        Rule[] elseRuleList = fts.elseRules;
        Rule[] ruleList = fts.ruleList;
        Rule r;
        Filter filter;
        Graphics2D graphics = fts.graphics;
        // applicable rules
        final int length = ruleList.length;
        for (int t = 0; t < length; t++) {
            r = ruleList[t];
            filter = r.getFilter();

            if (filter == null || filter.evaluate(rf.content)) {
                doElse = false;
                processSymbolizers(graphics, rf, r.symbolizers(), scaleRange, at, destinationCrs, layerId);
            }
        }

        if (doElse) {
            final int elseLength = elseRuleList.length;
            for (int tt = 0; tt < elseLength; tt++) {
                r = elseRuleList[tt];

                processSymbolizers(graphics, rf, r.symbolizers(), scaleRange,
                        at, destinationCrs, layerId);

            }
        }
    }

    /**
     * Applies each of a set of symbolizers in turn to a given feature.
     * <p>
     * This is an internal method and should only be called by processStylers.
     * </p>
     * @param currLayer 
     * 
     * @param graphics
     * @param drawMe
     *            The feature to be rendered
     * @param symbolizers
     *            An array of symbolizers which actually perform the rendering.
     * @param scaleRange
     *            The scale range we are working on... provided in order to make
     *            the style factory happy
     * @param shape
     * @param destinationCrs
     * @param layerId 
     * @throws TransformException
     * @throws FactoryException
     */
    final private void processSymbolizers(final Graphics2D graphics,
            final RenderableFeature drawMe, final List<Symbolizer> symbolizers,
            NumberRange scaleRange, AffineTransform at,
            CoordinateReferenceSystem destinationCrs, String layerId)
            throws Exception {
        
        for (Symbolizer symbolizer : symbolizers) {

            // /////////////////////////////////////////////////////////////////
            //
            // RASTER
            //
            // /////////////////////////////////////////////////////////////////
            if (symbolizer instanceof RasterSymbolizer) {
                // grab the grid coverage
                GridCoverage2D coverage = null;
                boolean disposeCoverage = false;
                
                try {
                    // //
                    // It is a grid coverage
                    // //
                    final Object grid = gridPropertyName.evaluate(drawMe.content);
                    if (grid instanceof GridCoverage2D) {
                        coverage = (GridCoverage2D) drawMe.content;
                    } else if (grid instanceof AbstractGridCoverage2DReader) {
                        final Object params = paramsPropertyName.evaluate(drawMe.content);
                        GridGeometry2D readGG = new GridGeometry2D(new GridEnvelope2D(screenSize), mapExtent);
                        AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) grid;
                        coverage = readCoverage(reader, params, readGG);
                        disposeCoverage = true;
                    }
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    fireErrorEvent(e);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    fireErrorEvent(e);
                }
                
                if(coverage != null) {
                    requests.put(new RenderRasterRequest(graphics, coverage, disposeCoverage,
                            (RasterSymbolizer) symbolizer, destinationCrs, at));
                }
            } else {

                // /////////////////////////////////////////////////////////////////
                //
                // FEATURE
                //
                // /////////////////////////////////////////////////////////////////
                LiteShape2 shape = drawMe.getShape(symbolizer, at);
                if(shape == null) {
                    continue;
                }
                
                if (symbolizer instanceof TextSymbolizer && drawMe.content instanceof Feature) {
                    labelCache.put(layerId, (TextSymbolizer) symbolizer, (Feature) drawMe.content,
                            shape, scaleRange);
                } else {
                    Style2D style = styleFactory.createStyle(drawMe.content,
                            symbolizer, scaleRange);
                    
                    // clip to the visible area + the size of the symbolizer (with some extra 
                    // to make sure we get no artefacts from polygon new borders)
                    double size = RendererUtilities.getStyle2DSize(style) + 10;
                    Envelope env = new Envelope(screenSize.getMinX(), screenSize.getMaxX(), screenSize.getMinY(), screenSize.getMaxY());
                    env.expandBy(size);
                    final GeometryClipper clipper = new GeometryClipper(env);
                    Geometry g = clipper.clip(shape.getGeometry(), false);
                    //System.out.println(g);
                    if(g == null) 
                        continue;
                    if(g != shape.getGeometry()) {
                        shape = new LiteShape2(g, null, null, false);
                    }
                    
                    requests.put(new PaintShapeRequest(graphics, shape, style, scaleDenominator));
                }

            }
        }
        fireFeatureRenderedEvent(drawMe.content);
    }





    /**
     * Finds the geometric attribute requested by the symbolizer
     * 
     * @param drawMe
     *            The feature
     * @param s
     * 
     * /** Finds the geometric attribute requested by the symbolizer
     * 
     * @param drawMe
     *            The feature
     * @param s
     *            The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry
     *         if none is specified
     */
    private com.vividsolutions.jts.geom.Geometry findGeometry(Object drawMe,
            Symbolizer s) {
        Expression geomExpr = s.getGeometry();

        // get the geometry
        Geometry geom;
        if(geomExpr == null) {
            if(drawMe instanceof SimpleFeature) {
                geom = (Geometry) ((SimpleFeature) drawMe).getDefaultGeometry();
            } else if (drawMe instanceof Feature) {
                geom = (Geometry) ((Feature) drawMe).getDefaultGeometryProperty().getValue();
            } else {
                geom = (Geometry) defaultGeometryPropertyName.evaluate(drawMe, Geometry.class);
            }
        } else {
            geom = (Geometry) geomExpr.evaluate(drawMe, Geometry.class);
        }

        return geom;    
    }

    /**
     * Finds the geometric attribute coordinate reference system.
     * @param drawMe2 
     * 
     * @param f The feature
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is specified
     */
    private org.opengis.referencing.crs.CoordinateReferenceSystem findGeometryCS(
            MapLayer currLayer, Object drawMe, Symbolizer s) {


        if( drawMe instanceof Feature) {
            Feature f = (Feature) drawMe;
            FeatureType schema = f.getType();

            Expression geometry = s.getGeometry();

            String geomName = null;
            if(geometry instanceof PropertyName) {
                geomName = ((PropertyName) geometry).getPropertyName();
                return getAttributeCRS(geomName, schema);
            } else if(geometry == null) {
                return getAttributeCRS(null, schema);
            } else {
                StyleAttributeExtractor attExtractor = new StyleAttributeExtractor();
                geometry.accept(attExtractor, null);
                for(String name : attExtractor.getAttributeNameSet()) {
                    if(schema.getDescriptor(name) instanceof GeometryDescriptor) {
                        return getAttributeCRS(name, schema);
                    }
                }
            }


        } else if ( currLayer.getSource() != null ) {
            return currLayer.getSource().getCRS();
        }

        return null;
    }

    /**
     * Finds the CRS of the specified attribute (or uses the default geometry instead)
     * @param geomName
     * @param schema
     * @return
     */
    org.opengis.referencing.crs.CoordinateReferenceSystem getAttributeCRS(String geomName,
            FeatureType schema) {
        if (geomName == null || "".equals(geomName)) {
            GeometryDescriptor geom = schema.getGeometryDescriptor();
            return geom.getType().getCoordinateReferenceSystem();
        } else {
            GeometryDescriptor geom = (GeometryDescriptor) schema.getDescriptor( geomName );
            return geom.getType().getCoordinateReferenceSystem();
        }
    }

    /**
     * Getter for property interactive.
     * 
     * @return Value of property interactive.
     */
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * Sets the interactive status of the renderer. An interactive renderer
     * won't wait for long image loading, preferring an alternative mark instead
     * 
     * @param interactive
     *            new value for the interactive property
     */
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * <p>
     * Returns true if the optimized data loading is enabled, false otherwise.
     * </p>
     * <p>
     * When optimized data loading is enabled, lite renderer will try to load
     * only the needed feature attributes (according to styles) and to load only
     * the features that are in (or overlaps with)the bounding box requested for
     * painting
     * </p>
     * 
     */
    private boolean isOptimizedDataLoadingEnabled() {
        if (rendererHints == null)
            return optimizedDataLoadingEnabledDEFAULT;
        Object result = null;
        try{
            result=rendererHints
            .get("optimizedDataLoadingEnabled");
        }catch (ClassCastException e) {

        }
        if (result == null)
            return optimizedDataLoadingEnabledDEFAULT;
        return ((Boolean)result).booleanValue();
    }

    /**
     * <p>
     * Returns the rendering buffer, a measure in pixels used to expand the geometry search area
     * enough to capture the geometries that do stay outside of the current rendering bounds but
     * do affect them because of their large strokes (labels and graphic symbols are handled 
     * differently, see the label chache).
     * </p>
     * 
     */
    private int getRenderingBuffer() {
        if (rendererHints == null)
            return renderingBufferDEFAULT;
        Number result = (Number) rendererHints.get("renderingBuffer");
        if (result == null)
            return renderingBufferDEFAULT;
        return result.intValue();
    }

    /**
     * <p>
     * Returns scale computation algorithm to be used. 
     * </p>
     * 
     */
    private String getScaleComputationMethod() {
        if (rendererHints == null)
            return scaleComputationMethodDEFAULT;
        String result = (String) rendererHints.get("scaleComputationMethod");
        if (result == null)
            return scaleComputationMethodDEFAULT;
        return result;
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
     * Returns the generalization distance in the screen space.
     * 
     */
    public double getGeneralizationDistance() {
        return generalizationDistance;
    }

    /**
     * <p>
     * Sets the generalizazion distance in the screen space.
     * </p>
     * <p>
     * Default value is 0.8, meaning that two subsequent points are collapsed to
     * one if their on screen distance is less than one pixel
     * </p>
     * <p>
     * Set the distance to 0 if you don't want any kind of generalization
     * </p>
     * 
     * @param d
     */
    public void setGeneralizationDistance(double d) {
        generalizationDistance = d;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.GTRenderer#setJava2DHints(java.awt.RenderingHints)
     */
    public void setJava2DHints(RenderingHints hints) {
        this.java2dHints = hints;
        styleFactory.setRenderingHints(hints);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.GTRenderer#getJava2DHints()
     */
    public RenderingHints getJava2DHints() {
        return java2dHints;
    }

    public void setRendererHints(Map hints) {
        if( hints!=null && hints.containsKey(LABEL_CACHE_KEY) ){
            LabelCache cache=(LabelCache) hints.get(LABEL_CACHE_KEY);
            if( cache==null )
                throw new NullPointerException("Label_Cache_Hint has a null value for the labelcache");

            this.labelCache=cache;
        }
        if(hints != null && hints.containsKey(LINE_WIDTH_OPTIMIZATION_KEY)) {
            styleFactory.setLineOptimizationEnabled(Boolean.TRUE.equals(hints.get(LINE_WIDTH_OPTIMIZATION_KEY)));
        }
        rendererHints = hints;

        // sets whether vector rendering is enabled in the SLDStyleFactory
        styleFactory.setVectorRenderingEnabled(isVectorRenderingEnabled());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.GTRenderer#getRendererHints()
     */
    public Map getRendererHints() {
        return rendererHints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.GTRenderer#setContext(org.geotools.map.MapContext)
     */
    public void setContext(MapContext context) {
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.GTRenderer#getContext()
     */
    public MapContext getContext() {
        return context;
    }

    public boolean isCanTransform() {
        return canTransform;
    }

    public static MathTransform getMathTransform(
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem destCRS) {
        try {
            return CRS.findMathTransform(sourceCRS, destCRS, true);
        } catch (OperationNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);

        } catch (FactoryException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return null;
    }

    GridCoverage2D readCoverage(final AbstractGridCoverage2DReader reader, final Object params, GridGeometry2D readGG) throws IOException {
        GridCoverage2D coverage;
        // read the coverage with the proper target geometry (will trigger cropping and resolution reduction)
        final Parameter<GridGeometry2D> readGGParam = new Parameter<GridGeometry2D>(
                AbstractGridFormat.READ_GRIDGEOMETRY2D);
        readGGParam.setValue(readGG);
        // then I try to get read parameters associated with this
        // coverage if there are any.
        if (params != null) {
            // //
            //
            // Getting parameters to control how to read this coverage.
            // Remember to check to actually have them before forwarding
            // them to the reader.
            //
            // //
            GeneralParameterValue[] readParams = (GeneralParameterValue[]) params;
            final int length = readParams.length;
            if (length > 0) {
                // we have a valid number of parameters, let's check if
                // also have a READ_GRIDGEOMETRY2D. In such case we just
                // override it with the one we just build for this
                // request.
                final String name = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();
                int i = 0;
                for (; i < length; i++)
                    if (readParams[i].getDescriptor().getName().toString().equalsIgnoreCase(name))
                        break;
                // did we find anything?
                if (i < length) {
                    // we found another READ_GRIDGEOMETRY2D, let's override it.
                    ((Parameter) readParams[i]).setValue(readGGParam);
                    coverage = (GridCoverage2D) reader.read(readParams);
                } else {
                    // add the correct read geometry to the supplied
                    // params since we did not find anything
                    GeneralParameterValue[] readParams2 = new GeneralParameterValue[length + 1];
                    System.arraycopy(readParams, 0, readParams2, 0, length);
                    readParams2[length] = readGGParam;
                    coverage = (GridCoverage2D) reader.read(readParams2);
                }
            } else
                // we have no parameters hence we just use the read grid
                // geometry to get a coverage
                coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { readGGParam });
        } else {
            coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { readGGParam });
        }
        return coverage;
    }

    /**
     * A decimator that will just transform coordinates
     */
    private static final Decimator NULL_DECIMATOR = new Decimator(-1, -1);

    /**
     * A class transforming (and caching) feature's geometries to shapes
     **/
    private class RenderableFeature {
        Object content;
        private MapLayer layer;
        private IdentityHashMap symbolizerAssociationHT = new IdentityHashMap(); // associate a value
        private List geometries = new ArrayList();
        private List shapes = new ArrayList();
        private boolean clone;
        private IdentityHashMap decimators = new IdentityHashMap();
        private ScreenMap screenMap;


        public RenderableFeature(MapLayer layer, boolean clone) {
            this.layer = layer;
            this.clone = clone;
        }

        public void setScreenMap(ScreenMap screenMap) {
            this.screenMap = screenMap;
        }

        public void setFeature(Object feature) {
            this.content = feature;
            geometries.clear();
            shapes.clear();
        }

        public LiteShape2 getShape(Symbolizer symbolizer, AffineTransform at) throws FactoryException {
            Geometry g = findGeometry(content, symbolizer); // pulls the geometry

            if ( g == null )
                return null;
            
            try {
                // process screenmap if necessary (only do it once, 
                // the geometry will be transformed simplified in place and the screenmap 
                // really needs to play against the original coordinates, plus, once we start
                // drawing a geometry we want to apply all symbolizers on it)
                if (screenMap != null // 
                        && !(symbolizer instanceof PointSymbolizer) //
                        && !(g instanceof Point)
                        && getGeometryIndex(g) == -1) {
                    Envelope env = g.getEnvelopeInternal();
                    if(screenMap.canSimplify(env))
                        if (screenMap.checkAndSet(env)) {
                            return null;
                        } else {
                            g = screenMap.getSimplifiedShape(env.getMinX(), env.getMinY(), 
                                    env.getMaxX(), env.getMaxY(), g.getFactory(), g.getClass());
                        }
                }
    
                SymbolizerAssociation sa = (SymbolizerAssociation) symbolizerAssociationHT
                .get(symbolizer);
                MathTransform2D crsTransform = null;
                MathTransform2D atTransform = null;
                MathTransform2D fullTransform = null;
                if (sa == null) {
                    sa = new SymbolizerAssociation();
                    sa.crs = (findGeometryCS(layer, content, symbolizer));
                    try {
                        crsTransform = buildTransform(sa.crs, destinationCrs);
                        atTransform = (MathTransform2D) ProjectiveTransform.create(worldToScreenTransform);
                        fullTransform = buildFullTransform(sa.crs, destinationCrs, at);
                    } catch (Exception e) {
                        // fall through
                        LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    }
                    sa.xform = fullTransform;
                    sa.crsxform = crsTransform;
                    sa.axform = atTransform;
    
                    symbolizerAssociationHT.put(symbolizer, sa);
                }

                // some shapes may be too close to projection boundaries to
                // get transformed, try to be lenient
                if (symbolizer instanceof PointSymbolizer) {
                    // if the coordinate transformation will occurr in place on the coordinate sequence
                    if(!clone && g.getFactory().getCoordinateSequenceFactory() instanceof LiteCoordinateSequenceFactory) {
                        // if the symbolizer is a point symbolizer we first get the transformed
                        // geometry to make sure the coordinates have been modified once, and then
                        // compute the centroid in the screen space. This is a side effect of the
                        // fact we're modifing the geometry coordinates directly, if we don't get
                        // the reprojected and decimated geometry we risk of transforming it twice
                        // when computing the centroid
                        Shape first = getTransformedShape(g, sa);
                        if(first != null) {
                        	return getTransformedShape(RendererUtilities.getCentroid(g), null);
                        } else {
                        	return null;
                        }
                    } else {
                        return getTransformedShape(RendererUtilities.getCentroid(g), sa);
                    }
                } else {
                    return getTransformedShape(g, sa);
                }
            } catch (TransformException te) {
                LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
                fireErrorEvent(te);
                return null;
            } catch (AssertionError ae) {
                LOGGER.log(Level.FINE, ae.getLocalizedMessage(), ae);
                fireErrorEvent(ae);
                return null;
            }
        }
        
        private int getGeometryIndex(Geometry g) {
            for (int i = 0; i < geometries.size(); i++) {
                if(geometries.get(i) == g) {
                    return i;
                }
            }
            return -1;
        }

        private final LiteShape2 getTransformedShape(Geometry originalGeom, SymbolizerAssociation sa) throws TransformException,
        FactoryException {
            int idx = getGeometryIndex(originalGeom);
            if(idx != -1) {
                return (LiteShape2) shapes.get(idx);
            }

            // we need to clone if the clone flag is high or if the coordinate sequence is not the one we asked for
            Geometry geom = originalGeom;
            if(clone || !(geom.getFactory().getCoordinateSequenceFactory() instanceof LiteCoordinateSequenceFactory)) {
                geom = LiteCoordinateSequence.cloneGeometry(geom);
            }

            LiteShape2 shape;
            if(projectionHandler != null && sa != null) {
                // first generalize and transform the geometry into the rendering CRS
                geom = projectionHandler.preProcess(sa.crs, geom);
                if(geom == null) {
                    shape = null;
                } else {
                    // first generalize and transform the geometry into the rendering CRS
                    Decimator d = getDecimator(sa.xform);
                    d.decimateTransformGeneralize(geom, sa.crsxform);
                    geom.geometryChanged();

                    // then post process it					
                    geom = projectionHandler.postProcess(geom);
                    if(geom == null) {
                        shape = null;
                    } else {
                        // apply the affine transform turning the coordinates into pixels
                        d = new Decimator(-1, -1);
                        d.decimateTransformGeneralize(geom, sa.axform);
    
                        // wrap into a lite shape
                        geom.geometryChanged();
                        shape = new LiteShape2(geom, null, null, false, false);
                    }
                } 
            } else {
                MathTransform2D xform = null;
                if(sa != null)
                    xform = sa.xform;
                shape = new LiteShape2(geom, xform, getDecimator(xform), false, false);
            }

            // cache the result
            geometries.add(originalGeom);
            shapes.add(shape);
            return shape;
        }



        /**
         * @throws org.opengis.referencing.operation.NoninvertibleTransformException
         */
        private Decimator getDecimator(MathTransform2D mathTransform) {
            // returns a decimator that does nothing if the currently set generalization
            // distance is zero (no generalization desired) or if the datastore has
            // already done full generalization at the desired level
            if (generalizationDistance == 0 || !inMemoryGeneralization)
                return NULL_DECIMATOR;

            Decimator decimator = (Decimator) decimators.get(mathTransform);
            if (decimator == null) {
                try {
                    if (mathTransform != null && !mathTransform.isIdentity())
                        decimator = new Decimator(mathTransform.inverse(), screenSize, generalizationDistance);
                    else
                        decimator = new Decimator(null, screenSize, generalizationDistance);
                } catch(org.opengis.referencing.operation.NoninvertibleTransformException e) {
                    decimator = new Decimator(null, screenSize, generalizationDistance);
                }

                decimators.put(mathTransform, decimator);
            }
            return decimator;
        }
    }
    
    /**
     * A request sent to the painting thread 
     * @author aaime
     */
    abstract class RenderingRequest {
        abstract void execute();
    }
    
    /**
     * A request to paint a shape with a specific Style2D
     * @author aaime
     *
     */
    class PaintShapeRequest extends RenderingRequest {
        Graphics2D graphic;
        
        LiteShape2 shape;

        Style2D style;

        double scale;

        public PaintShapeRequest(Graphics2D graphic, LiteShape2 shape, Style2D style, double scale) {
            this.graphic = graphic;
            this.shape = shape;
            this.style = style;
            this.scale = scale;
        }

        @Override
        void execute() {
            if(graphic instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphic).init();
            }
            
            try {
                painter.paint(graphic, shape, style, scale);
            } catch(Throwable t) {
                fireErrorEvent(t);
            }
        }
    }
    
    /**
     * A request to merge multiple back buffers to the main graphics
     * @author aaime
     *
     */
    class MergeLayersRequest extends RenderingRequest {
        Graphics2D graphics;
        LiteFeatureTypeStyle fts_array[];
        
        

        public MergeLayersRequest(Graphics2D graphics, LiteFeatureTypeStyle[] ftsArray) {
            this.graphics = graphics;
            fts_array = ftsArray;
        }

        @Override
        void execute() {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            for (int t = 0; t < fts_array.length; t++) {
                // first fts won't have an image, it's using the user provided graphics
                // straight, so we don't need to compose it back in.
                final Graphics2D ftsGraphics = fts_array[t].graphics;
                if (ftsGraphics instanceof DelayedBackbufferGraphic) {
                    final BufferedImage image = ((DelayedBackbufferGraphic) ftsGraphics).image;
                    // we may have not found anything to paint, in that case the delegate
                    // has not been initialized
                    if(image != null) {
                        graphics.drawImage(image, 0, 0, null);
                        ftsGraphics.dispose();
                    }
                }
            }
            
        }
    }
    
    /**
     * A request to render a raster
     * @author aaime
     *
     */
    public class RenderRasterRequest extends RenderingRequest {

        private Graphics2D graphics;
        private boolean disposeCoverage;
        private GridCoverage2D coverage;
        private RasterSymbolizer symbolizer;
        private CoordinateReferenceSystem destinationCRS;
        private AffineTransform worldToScreen;

        public RenderRasterRequest(Graphics2D graphics, GridCoverage2D coverage, boolean disposeCoverage,
                RasterSymbolizer symbolizer, CoordinateReferenceSystem destinationCRS,
                AffineTransform worldToScreen) {
            this.graphics = graphics;
            this.coverage = coverage;
            this.disposeCoverage = disposeCoverage;
            this.symbolizer = symbolizer;
            this.destinationCRS = destinationCRS;
            this.worldToScreen = worldToScreen;
        }

        @Override
        void execute() {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rendering Raster " + coverage);
            }

            try {
                // /////////////////////////////////////////////////////////////////
                //
                // If the grid object is a reader we ask him to do its best for the
                // requested resolution, if it is a gridcoverage instead we have to
                // rely on the gridocerage renderer itself.
                //
                // /////////////////////////////////////////////////////////////////

                final GridCoverageRenderer gcr = new GridCoverageRenderer(destinationCRS,
                        originalMapExtent, screenSize, worldToScreen, java2dHints);
                try {
                    gcr.paint(graphics, coverage, symbolizer);
                } finally {
                    // we need to try and dispose this coverage if was created on purpose for
                    // rendering
                    if (coverage != null && disposeCoverage)
                        coverage.dispose(true);
                }
                
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Raster rendered");
                }

            } catch (FactoryException e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            } catch (TransformException e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            } catch (NoninvertibleTransformException e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            }
        }

    }
    
    /**
     * Marks the end of the request flow, instructs the painting thread to exit
     * @author Andrea Aime - OpenGeo
     */
    class EndRequest extends RenderingRequest {

        @Override
        void execute() {
            // nothing to do here
        }
        
    }
    
    /**
     * The secondary thread that actually issues the paint requests against the graphic object
     * @author aaime
     *
     */
    class PainterThread implements Runnable {
        BlockingQueue<RenderingRequest> requests;
        
        public PainterThread(BlockingQueue<RenderingRequest> requests) {
            this.requests = requests;
        }

        public void run() {
            boolean done = false;
            while(!done) {
                try {
                    RenderingRequest request = requests.take();
                    if(request instanceof EndRequest || renderingStopRequested) {
                        done = true;
                    } else {
                        request.execute();
                    }
                } catch(InterruptedException e) {
                    // ok, we might have been interupped to stop processing
                    if(renderingStopRequested) {
                        done = true;
                    }
                }
                
            }
            
        }
        
    }
}
