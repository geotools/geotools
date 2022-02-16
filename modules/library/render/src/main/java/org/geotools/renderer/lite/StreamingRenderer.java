/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.abs;

import com.conversantmedia.util.concurrent.PushPullBlockingQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.ScreenMap;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.GeometryTransformationVisitor;
import org.geotools.filter.spatial.DefaultCRSFilterVisitor;
import org.geotools.filter.spatial.ReprojectingFilterVisitor;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.filter.visitor.SpatialFilterVisitor;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.OffsetCurveBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.map.DirectLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.renderer.crs.WrappingProjectionHandler;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.lite.gridcoverage2d.GridCoverageRenderer;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkAlongLine;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.StyleAttributeExtractor;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DpiRescaleStyleVisitor;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.styling.visitor.MapRenderingSelectorStyleVisitor;
import org.geotools.styling.visitor.RenderingSelectorStyleVisitor;
import org.geotools.styling.visitor.UomRescaleStyleVisitor;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.style.LineSymbolizer;
import org.opengis.style.PolygonSymbolizer;

/**
 * A streaming implementation of the GTRenderer interface.
 *
 * <ul>
 *   <li>Uses as little memory as possible by processing features as they come from the data source,
 *       instead of accumulating them up-front
 * </ul>
 *
 * Use this class if you need a stateless renderer that provides low memory footprint and decent
 * rendering performance on the first call but don't need good optimal performance on subsequent
 * calls on the same data.
 *
 * <p>The streaming renderer is not thread safe
 *
 * @author James Macgill
 * @author dblasby
 * @author jessie eichar
 * @author Simone Giannecchini
 * @author Andrea Aime
 * @author Alessio Fabiani
 * @version $Id$
 */
public class StreamingRenderer implements GTRenderer {

    private static final int REPROJECTION_RASTER_GUTTER = 10;

    private static final int defaultMaxFiltersToSendToDatastore = 5; // default

    /**
     * Computes the scale as the ratio between map distances and real world distances, assuming
     * 90dpi and taking into consideration projection deformations and actual earth shape. <br>
     * Use this method only when in need of accurate computation. Will break if the data extent is
     * outside of the currenct projection definition area.
     */
    public static final String SCALE_ACCURATE = "ACCURATE";

    /**
     * Very simple and lenient scale computation method that conforms to the OGC SLD specification
     * 1.0, page 26. <br>
     * This method is quite approximative, but should never break and ensure constant scale even on
     * lat/lon unprojected maps (because in that case scale is computed as if the area was along the
     * equator no matter what the real position is).
     */
    public static final String SCALE_OGC = "OGC";

    /**
     * The rendering buffer grows the query area to account for features that are contributing to
     * the requested area due to their large symbolizer, or long label
     */
    public static final String RENDERING_BUFFER = "renderingBuffer";

    /** Tolerance used to compare doubles for equality */
    private static final double TOLERANCE = 1e-6;

    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StreamingRenderer.class);

    int error = 0;

    /** Filter factory for creating bounding box filters */
    protected static final FilterFactory2 filterFactory =
            CommonFactoryFinder.getFilterFactory2(null);

    protected static final StyleFactory STYLE_FACTORY = CommonFactoryFinder.getStyleFactory();

    private static final PropertyName gridPropertyName = filterFactory.property("grid");

    private static final PropertyName paramsPropertyName = filterFactory.property("params");

    private static final PropertyName defaultGeometryPropertyName = filterFactory.property("");

    /**
     * The MapContent instance which contains the layers and the bounding box which needs to be
     * rendered.
     */
    private MapContent mapContent;

    /**
     * Flag which determines if the renderer is interactive or not. An interactive renderer will
     * return rather than waiting for time consuming operations to complete (e.g. Image Loading). A
     * non-interactive renderer (e.g. a SVG or PDF renderer) will block for these operations.
     */
    private boolean interactive = true;

    /**
     * Flag which controls behaviour for applying affine transformation to the graphics object. If
     * true then the transform will be concatenated to the existing transform. If false it will be
     * replaced.
     */
    private boolean concatTransforms = false;

    /** Geographic map extent, eventually expanded to consider buffer area around the map */
    private ReferencedEnvelope mapExtent;

    /** Geographic map extent, as provided by the caller */
    private ReferencedEnvelope originalMapExtent;

    /** The size of the output area in output units. */
    private Rectangle screenSize;

    /**
     * This flag is set to false when starting rendering, and will be checked during the rendering
     * loop in order to make it stop forcefully
     */
    boolean renderingStopRequested = false;

    /**
     * The ratio required to scale the features to be rendered so that they fit into the output
     * space.
     */
    protected double scaleDenominator;

    /** Maximum displacement for generalization during rendering */
    private double generalizationDistance = 0.8;

    /** Factory that will resolve symbolizers into rendered styles */
    private SLDStyleFactory styleFactory = new SLDStyleFactory();

    protected LabelCache labelCache = new LabelCacheImpl();

    /** The painter class we use to depict shapes onto the screen */
    protected StyledShapePainter painter = new StyledShapePainter(labelCache);

    private BlockingQueue<RenderingRequest> requests;

    private List<RenderListener> renderListeners = new CopyOnWriteArrayList<>();

    private RenderingHints java2dHints;

    private int renderingBufferDEFAULT = 0;

    private String scaleComputationMethodDEFAULT = SCALE_OGC;

    /**
     * Text will be rendered using the usual calls gc.drawString/drawGlyphVector. This is a little
     * faster, and more consistent with how the platform renders the text in other applications. The
     * downside is that on most platform the label and its eventual halo are not properly centered.
     */
    public static final String TEXT_RENDERING_STRING =
            LabelCacheImpl.LabelRenderingMode.STRING.name();

    /**
     * Text will be rendered using the associated {@link GlyphVector} outline, that is, a {@link
     * Shape}. This ensures perfect centering between the text and the halo, but introduces more
     * text aliasing.
     */
    public static final String TEXT_RENDERING_OUTLINE =
            LabelCacheImpl.LabelRenderingMode.OUTLINE.name();

    /**
     * Will use STRING mode for horizontal labels, OUTLINE mode for all other labels. Works best
     * when coupled with {@link RenderingHints#VALUE_FRACTIONALMETRICS_ON}
     */
    public static final String TEXT_RENDERING_ADAPTIVE =
            LabelCacheImpl.LabelRenderingMode.ADAPTIVE.name();

    /** The text rendering method, either TEXT_RENDERING_OUTLINE or TEXT_RENDERING_STRING */
    public static final String TEXT_RENDERING_KEY = "textRenderingMethod";

    private String textRenderingModeDEFAULT = TEXT_RENDERING_STRING;

    /**
     * Whether the thin line width optimization should be used, or not.
     *
     * <p>When rendering non antialiased lines adopting a width of 0 makes the java2d renderer get
     * into a fast path that generates the same output as a 1 pixel wide line
     *
     * <p>Unfortunately for antialiased rendering that optimization does not help, and disallows
     * controlling the width of thin lines. It is provided as an explicit option as the optimization
     * has been hard coded for years, removing it when antialiasing is on by default will invalidate
     * lots of existing styles (making lines appear thicker).
     */
    public static final String LINE_WIDTH_OPTIMIZATION_KEY = "lineWidthOptimization";

    /**
     * Boolean flag controlling a memory/speed trade off related to how multiple feature type styles
     * are rendered.
     *
     * <p>When enabled (by default) multiple feature type styles against the same data source will
     * be rendered in separate memory back buffers in a way that allows the source to be scanned
     * only once (each back buffer is as big as the image being rendered).
     *
     * <p>When disabled no memory back buffers will be used but the feature source will be scanned
     * once for every feature type style declared against it
     */
    public static final String OPTIMIZE_FTS_RENDERING_KEY = "optimizeFTSRendering";

    /**
     * Enables advanced reprojection handling. Geometries will be sliced to fit into the area of
     * definition of the rendering projection.
     */
    public static final String ADVANCED_PROJECTION_HANDLING_KEY = "advancedProjectionHandling";

    /**
     * Enabled continuous cartographic wrapping for projections that can wrap around their edges
     * (e.g., Mercator): this results in a continous horizontal map much like Google Maps
     */
    public static final String CONTINUOUS_MAP_WRAPPING = "continuousMapWrapping";

    /**
     * Boolean flag indicating whether vector rendering should be preferred when painting graphic
     * fills. See {@link SLDStyleFactory#isVectorRenderingEnabled()} for more details.
     */
    public static final String VECTOR_RENDERING_KEY = "vectorRenderingEnabled";

    private static boolean VECTOR_RENDERING_ENABLED_DEFAULT = false;

    /**
     * Boolean flag indicating whether advanced projection densification should be used when needed.
     */
    public static final String ADVANCED_PROJECTION_DENSIFICATION_KEY =
            "advancedProjectionDensificationEnabled";

    private static boolean ADVANCED_PROJECTION_DENSIFICATION_DEFAULT = false;

    /** Densification Tolerance, to be used when densification is enabled. */
    public static final String ADVANCED_PROJECTION_DENSIFICATION_TOLERANCE_KEY =
            "advancedProjectionDensificationTolerance";

    private static double ADVANCED_PROJECTION_DENSIFICATION_TOLERANCE_DEFAULT = 0.8;

    /**
     * Boolean flag indicating whether advanced projection wrapping heuristic should be used or nto.
     */
    public static final String DATELINE_WRAPPING_HEURISTIC_KEY = "datelineWrappingCheckEnabled";

    private static boolean DATELINE_WRAPPING_HEURISTIC_DEFAULT = true;

    public static final String LABEL_CACHE_KEY = "labelCache";
    public static final String FORCE_EPSG_AXIS_ORDER_KEY = "ForceEPSGAxisOrder";
    public static final String DPI_KEY = "dpi";
    public static final String DECLARED_SCALE_DENOM_KEY = "declaredScaleDenominator";
    public static final String SCALE_COMPUTATION_METHOD_KEY = "scaleComputationMethod";
    public static final String BYLAYER_INTERPOLATION = "byLayerInterpolation";

    /**
     * "vectorRenderingEnabled" - Boolean yes/no (see default vectorRenderingEnabledDEFAULT)
     * "declaredScaleDenominator" - Double the value of the scale denominator to use by the
     * renderer. by default the value is calculated based on the screen size and the displayed area
     * of the map. "dpi" - Integer number of dots per inch of the display 90 DPI is the default (as
     * declared by OGC) "forceCRS" - CoordinateReferenceSystem declares to the renderer that all
     * layers are of the CRS declared in this hint "labelCache" - Declares the label cache that will
     * be used by the renderer. "forceEPSGAxisOrder" - When doing spatial filter reprojection (from
     * the SLD towards the native CRS) assume the geometries are expressed with the axis order
     * suggested by the official EPSG database, regardless of how the CRS system might be configured
     */
    private Map<?, ?> rendererHints = null;

    private AffineTransform worldToScreenTransform = null;

    private CoordinateReferenceSystem destinationCrs;

    private boolean canTransform;

    /**
     * Whether the renderer must perform generalization for the current set of features. For each
     * layer we will set this flag depending on whether the datastore can do full generalization for
     * us, or not
     */
    // private boolean inMemoryGeneralization = true;

    /** The thread pool used to submit the painter workers. */
    private ExecutorService threadPool;

    private PainterThread painterThread;

    private static int MAX_PIXELS_DENSIFY =
            Integer.valueOf(System.getProperty("ADVANCED_PROJECTION_DENSIFY_MAX_PIXELS", "5"));

    /**
     * Creates a new instance of LiteRenderer without a context. Use it only to gain access to
     * utility methods of this class or if you want to render random feature collections instead of
     * using the map context interface
     */
    public StreamingRenderer() {}

    /** Sets a thread pool to be used in parallel rendering */
    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * Sets the flag which controls behaviour for applying affine transformation to the graphics
     * object.
     *
     * @param flag If true then the transform will be concatenated to the existing transform. If
     *     false it will be replaced.
     */
    public void setConcatTransforms(boolean flag) {
        concatTransforms = flag;
    }

    /**
     * Flag which controls behaviour for applying affine transformation to the graphics object.
     *
     * @return a boolean flag. If true then the transform will be concatenated to the existing
     *     transform. If false it will be replaced.
     */
    public boolean getConcatTransforms() {
        return concatTransforms;
    }

    /**
     * adds a listener that responds to error events of feature rendered events.
     *
     * @see RenderListener
     * @param listener the listener to add.
     */
    @Override
    public void addRenderListener(RenderListener listener) {
        renderListeners.add(listener);
        if (labelCache instanceof LabelCacheImpl) {
            ((LabelCacheImpl) labelCache).addRenderListener(listener);
        }
    }

    /**
     * Removes a render listener.
     *
     * @see RenderListener
     * @param listener the listener to remove.
     */
    @Override
    public void removeRenderListener(RenderListener listener) {
        renderListeners.remove(listener);
    }

    private void fireFeatureRenderedEvent(Object feature) {
        if (!(feature instanceof SimpleFeature)) {
            if (feature instanceof Feature) {
                LOGGER.log(Level.FINE, "Skipping non simple feature rendering notification");
            }
            return;
        }
        if (!renderListeners.isEmpty()) {
            RenderListener listener;
            for (RenderListener renderListener : renderListeners) {
                listener = renderListener;
                listener.featureRenderer((SimpleFeature) feature);
            }
        }
    }

    private void fireErrorEvent(Throwable t) {
        LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);
        if (!renderListeners.isEmpty()) {
            Exception e;
            if (t instanceof Exception) {
                e = (Exception) t;
            } else {
                e = new Exception(t);
            }
            RenderListener listener;
            for (RenderListener renderListener : renderListeners) {
                listener = renderListener;
                listener.errorOccurred(e);
            }
        }
    }

    /**
     * If you call this method from another thread than the one that called <code>paint</code> or
     * <code>render</code> the rendering will be forcefully stopped before termination
     */
    @Override
    public void stopRendering() {
        renderingStopRequested = true;
        // un-block the queue in case it was filled with requests and the main
        // thread got blocked on it
        requests.clear();
        // wake up the painter and put a death pill in the queue
        painterThread.interrupt();
        try {
            requests.put(new EndRequest());
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "Interrupted while trying to put the end "
                            + "request in the requests queue, this should never happen",
                    e);
        }

        labelCache.stop();
    }

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the size of the output area and the transformation
     * from coordinates to pixels are known. The latter determines the map scale. The viewport (the
     * visible part of the map) will be calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     * @task Need to check if the Layer CoordinateSystem is different to the BoundingBox rendering
     *     CoordinateSystem and if so, then transform the coordinates.
     */
    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen) {
        if (worldToScreen == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        // First, create the bbox in real world coordinates
        Envelope mapArea;
        try {
            mapArea = RendererUtilities.createMapEnvelope(paintArea, worldToScreen);
            paint(graphics, paintArea, mapArea, worldToScreen);
        } catch (NoninvertibleTransformException e) {
            fireErrorEvent(e);
        }
    }

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     */
    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
        if (mapArea == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        paint(graphics, paintArea, mapArea, worldToScreenTransform(mapArea, paintArea));
    }

    private static AffineTransform worldToScreenTransform(Envelope mapExtent, Rectangle paintArea) {
        double scaleX = paintArea.getWidth() / mapExtent.getWidth();
        double scaleY = paintArea.getHeight() / mapExtent.getHeight();

        double tx = -mapExtent.getMinX() * scaleX;
        double ty = (mapExtent.getMinY() * scaleY) + paintArea.getHeight();

        AffineTransform at = new AffineTransform(scaleX, 0.0d, 0.0d, -scaleY, tx, ty);
        AffineTransform originTranslation =
                AffineTransform.getTranslateInstance(paintArea.x, paintArea.y);
        originTranslation.concatenate(at);

        return originTranslation != null ? originTranslation : at;
    }

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     */
    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea) {
        if (mapArea == null || paintArea == null) {
            LOGGER.info("renderer passed null arguments");
            return;
        } // Other arguments get checked later
        paint(
                graphics,
                paintArea,
                mapArea,
                RendererUtilities.worldToScreenTransform(mapArea, paintArea));
    }

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that paint area, envelope and worldToScreen transform
     * are already computed. Use this method to avoid recomputation. <b>Note however that no check
     * is performed that they are really in sync!<b/>
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     */
    @Override
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            Envelope mapArea,
            AffineTransform worldToScreen) {
        paint(
                graphics,
                paintArea,
                new ReferencedEnvelope(mapArea, mapContent.getCoordinateReferenceSystem()),
                worldToScreen);
    }

    private double computeScale(
            ReferencedEnvelope envelope,
            Rectangle paintArea,
            AffineTransform worldToScreen,
            Map<?, ?> hints) {
        if (getScaleComputationMethod().equals(SCALE_ACCURATE)) {
            try {
                return RendererUtilities.calculateScale(
                        envelope, paintArea.width, paintArea.height, hints);
            } catch (Exception e) // probably either (1) no CRS (2) error xforming
            {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }
        if (XAffineTransform.getRotation(worldToScreen) != 0.0) {
            return RendererUtilities.calculateOGCScaleAffine(
                    envelope.getCoordinateReferenceSystem(), worldToScreen, hints);
        }
        return RendererUtilities.calculateOGCScale(envelope, paintArea.width, hints);
    }

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that paint area, envelope and worldToScreen transform
     * are already computed. Use this method to avoid recomputation. <b>Note however that no check
     * is performed that they are really in sync!<b/>
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates. Its associate CRS is
     *     ALWAYS 2D
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     */
    @Override
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            ReferencedEnvelope mapArea,
            AffineTransform worldToScreen) {
        // ////////////////////////////////////////////////////////////////////
        //
        // Check for null arguments, recompute missing ones if possible
        //
        // ////////////////////////////////////////////////////////////////////
        if (graphics == null) {
            LOGGER.severe("renderer passed null graphics argument");
            throw new NullPointerException("renderer requires graphics");
        } else if (paintArea == null) {
            LOGGER.severe("renderer passed null paintArea argument");
            throw new NullPointerException("renderer requires paintArea");
        } else if (mapArea == null) {
            LOGGER.severe("renderer passed null mapArea argument");
            throw new NullPointerException("renderer requires mapArea");
        } else if (worldToScreen == null) {
            worldToScreen = RendererUtilities.worldToScreenTransform(mapArea, paintArea);
            if (worldToScreen == null) return;
        }

        CoordinateReferenceSystem mapCRS = mapArea.getCoordinateReferenceSystem();
        if (CRS.getAxisOrder(mapCRS) == CRS.AxisOrder.NORTH_EAST) {
            try {
                // sanitize, having flipped axis causes slowdowns, the rendering
                // subsystem has to go from data to rendering to screen flipping axis order
                // twice when advanced projection handling is enabled
                Integer code = CRS.lookupEpsgCode(mapCRS, false);
                if (code != null) {
                    String srs = "EPSG:" + code;
                    CoordinateReferenceSystem earthNorthCRS = CRS.decode(srs, true);
                    mapArea =
                            new ReferencedEnvelope(
                                    mapArea.getMinY(),
                                    mapArea.getMaxY(),
                                    mapArea.getMinX(),
                                    mapArea.getMaxX(),
                                    earthNorthCRS);
                }

                // flip world to screen too
                worldToScreen =
                        new AffineTransform(
                                worldToScreen.getShearX(),
                                worldToScreen.getScaleX(),
                                worldToScreen.getScaleY(),
                                worldToScreen.getShearY(),
                                worldToScreen.getTranslateX(),
                                worldToScreen.getTranslateY());
            } catch (Exception e) {
                LOGGER.log(
                        Level.FINER,
                        "Failed to turn the requested bbox in east/north order, map rendering "
                                + "should work anyways, but pay a performance price");
            }
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
        if (java2dHints != null) graphics.setRenderingHints(java2dHints);
        // add the anchor for graphic fills
        Point2D textureAnchor =
                new Point2D.Double(
                        worldToScreenTransform.getTranslateX(),
                        worldToScreenTransform.getTranslateY());
        graphics.setRenderingHint(StyledShapePainter.TEXTURE_ANCHOR_HINT_KEY, textureAnchor);
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
        scaleDenominator = computeScale(mapArea, paintArea, worldToScreenTransform, rendererHints);
        if (LOGGER.isLoggable(Level.FINE))
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
        if (buffer > 0) {
            mapExtent =
                    new ReferencedEnvelope(
                            expandEnvelope(mapExtent, worldToScreen, buffer),
                            mapExtent.getCoordinateReferenceSystem());
        }

        // Setup the secondary painting thread
        requests = getRequestsQueue();
        painterThread = new PainterThread(requests);
        ExecutorService localThreadPool = threadPool;
        boolean localPool = false;
        if (localThreadPool == null) {
            localThreadPool = Executors.newSingleThreadExecutor();
            localPool = true;
        }
        Future painterFuture = localThreadPool.submit(painterThread);
        List<CompositingGroup> compositingGroups = null;
        try {
            if (mapContent == null) {
                throw new IllegalStateException(
                        "Cannot call paint, you did not set a MapContent in this renderer");
            }

            // re-organize the map content and generate the z group layers
            MapContent zGroupedMapContent = ZGroupLayerFactory.filter(mapContent);

            // split over multiple map contents, one per composition base
            compositingGroups =
                    CompositingGroup.splitOnCompositingBase(
                            graphics, paintArea, zGroupedMapContent);

            int layerCounter = 0;

            for (CompositingGroup compositingGroup : compositingGroups) {
                MapContent currentMapContent = compositingGroup.mapContent;
                Graphics2D compositingGraphic = compositingGroup.graphics;

                // ////////////////////////////////////////////////////////////////////
                //
                // Processing all the map layers in the context using the accompaining
                // styles
                //
                // ////////////////////////////////////////////////////////////////////

                labelCache.start();
                if (labelCache instanceof LabelCacheImpl) {
                    ((LabelCacheImpl) labelCache)
                            .setLabelRenderingMode(
                                    LabelRenderingMode.valueOf(getTextRenderingMethod()));
                }

                for (Layer layer : currentMapContent.layers()) {
                    try {
                        renderListeners.forEach(l -> l.layerStart(layer));
                    } catch (Exception e) {
                        fireErrorEvent(e);
                    }
                    layerCounter++;
                    String layerId = String.valueOf(layerCounter);
                    if (!layer.isVisible()) {
                        // Only render layer when layer is visible
                        continue;
                    }

                    if (renderingStopRequested) {
                        return;
                    }

                    // handle the background color specification, if any
                    Style style = layer.getStyle();
                    if (style != null && style.getBackground() != null) {
                        fillBackground(graphics, paintArea, style);
                    }

                    labelCache.startLayer(layerId);
                    if (layer instanceof DirectLayer) {
                        RenderingRequest request =
                                new RenderDirectLayerRequest(
                                        compositingGraphic, (DirectLayer) layer);
                        try {
                            requests.put(request);
                        } catch (InterruptedException e) {
                            fireErrorEvent(e);
                        }
                    } else if (layer instanceof ZGroupLayer) {
                        try {
                            ZGroupLayer zGroup = (ZGroupLayer) layer;
                            zGroup.drawFeatures(compositingGraphic, this, layerId);
                        } catch (Throwable t) {
                            fireErrorEvent(t);
                        }
                    } else {
                        try {
                            // extract the feature type stylers from the style object
                            // and process them
                            processStylers(compositingGraphic, layer, layerId);
                        } catch (Throwable t) {
                            fireErrorEvent(t);
                        }
                    }

                    labelCache.endLayer(layerId, graphics, screenSize);
                    try {
                        requests.put(new RenderTimeStatisticsRequest(renderListeners, layer));
                    } catch (InterruptedException ex) {
                        fireErrorEvent(ex);
                    }
                }

                // have we been painting on a back buffer? If so, merge on the main graphic
                if (compositingGraphic instanceof DelayedBackbufferGraphic) {
                    RenderingRequest request =
                            new MargeCompositingGroupRequest(graphics, compositingGroup);
                    try {
                        requests.put(request);
                    } catch (InterruptedException e) {
                        fireErrorEvent(e);
                    }
                }
            }
        } finally {
            try {
                // clean up generated map contents (in finally block to ensure it's done regardless
                // of how we got here
                if (compositingGroups != null) {
                    for (CompositingGroup group : compositingGroups) {
                        MapContent groupContent = group.getMapContent();
                        if (groupContent != mapContent) {
                            groupContent.dispose();
                        }
                    }
                }
            } finally {
                try {
                    if (!renderingStopRequested) {
                        requests.put(new EndRequest());
                        painterFuture.get();
                    }
                } catch (Exception e) {
                    painterFuture.cancel(true);
                    fireErrorEvent(e);
                } finally {
                    if (localPool) {
                        localThreadPool.shutdown();
                    }
                }
            }
        }

        if (!renderingStopRequested) {
            renderListeners.forEach(l -> l.labellingStart());
            labelCache.end(graphics, paintArea);
            renderListeners.forEach(l -> l.labellingEnd());
        } else {
            labelCache.clear();
        }

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(
                    new StringBuffer("Style cache hit ratio: ")
                            .append(styleFactory.getHitRatio())
                            .append(" , hits ")
                            .append(styleFactory.getHits())
                            .append(", requests ")
                            .append(styleFactory.getRequests())
                            .toString());
        if (error > 0) {
            LOGGER.warning(
                    new StringBuffer(
                                    "Number of Errors during paint(Graphics2D, AffineTransform) = ")
                            .append(error)
                            .toString());
        }
    }

    protected void fillBackground(Graphics2D graphics, Rectangle paintArea, Style style) {
        // get the paint, could be a repeated image too (TexturePaint)
        Paint background = styleFactory.getPaint(style.getBackground(), null, null);
        graphics.setPaint(background);
        // the opacity we need only if the paint is solid, otherwise the graphic fill
        // machinery has already given us the right composite for it
        if (background instanceof Color) {
            Composite composite = styleFactory.getComposite(style.getBackground(), null);
            graphics.setComposite(composite);
        }
        // and fill it
        graphics.fill(paintArea);
    }

    /**
     * Builds the blocking queue used to bridge between the data loading thread and the painting one
     */
    protected BlockingQueue<RenderingRequest> getRequestsQueue() {
        return new RenderingBlockingQueue(10000);
    }

    /**
     * Extends the provided {@link Envelope} in order to add the number of pixels specified by
     * <code>buffer</code> in every direction.
     *
     * @param envelope to extend.
     * @param worldToScreen by means of which doing the extension.
     * @param buffer to use for the extension.
     * @return an extended version of the provided {@link Envelope}.
     */
    private Envelope expandEnvelope(Envelope envelope, AffineTransform worldToScreen, int buffer) {
        assert buffer > 0;
        double bufferX = Math.abs(buffer * 1.0 / XAffineTransform.getScaleX0(worldToScreen));
        double bufferY = Math.abs(buffer * 1.0 / XAffineTransform.getScaleY0(worldToScreen));
        return new Envelope(
                envelope.getMinX() - bufferX,
                envelope.getMaxX() + bufferX,
                envelope.getMinY() - bufferY,
                envelope.getMaxY() + bufferY);
    }

    /**
     * Queries a given layer's features to be rendered based on the target rendering bounding box.
     *
     * <p>The following optimization will be performed in order to limit the number of features
     * returned:
     *
     * <ul>
     *   <li>Just the features whose geometric attributes lies within <code>envelope</code> will be
     *       queried
     *   <li>The queried attributes will be limited to just those needed to perform the rendering,
     *       based on the required geometric and non geometric attributes found in the Layer's style
     *       rules
     *   <li>If a <code>Query</code> has been set to limit the resulting layer's features, the final
     *       filter to obtain them will respect it. This means that the bounding box filter and the
     *       Query filter will be combined, also including maxFeatures from Query
     *   <li>At least that the layer's definition query explicitly says to retrieve some attribute,
     *       no attributes will be requested from it, for performance reasons. So it is desirable to
     *       not use a Query for filtering a layer which includes attributes. Note that including
     *       the attributes in the result is not necessary for the query's filter to get properly
     *       processed.
     * </ul>
     *
     * <p><b>NOTE </b>: This is an internal method and should only be called by <code>
     * paint(Graphics2D, Rectangle, AffineTransform)</code>. It is package protected just to allow
     * unit testing it.
     *
     * @param envelope the spatial extent which is the target area of the rendering process
     * @return the set of features resulting from <code>currLayer</code> after querying its feature
     *     source
     * @throws IllegalFilterException if something goes wrong constructing the bbox filter
     */
    /*
     * Default visibility for testing purposes
     */

    Query getStyleQuery(
            Layer layer,
            List<LiteFeatureTypeStyle> styleList,
            Envelope mapArea,
            CoordinateReferenceSystem mapCRS,
            CoordinateReferenceSystem featCrs,
            Rectangle screenSize,
            GeometryDescriptor geometryAttribute,
            AffineTransform worldToScreenTransform,
            boolean hasRenderingTransformation)
            throws IllegalFilterException, IOException, FactoryException {
        @SuppressWarnings("unchecked")
        FeatureSource<FeatureType, Feature> source =
                (FeatureSource<FeatureType, Feature>) layer.getFeatureSource();
        FeatureType schema = source.getSchema();
        Query query = new Query(Query.ALL);
        Filter filter = null;

        // if map extent are not already expanded by a constant buffer, try to compute a layer
        // specific one based on stroke widths
        if (getRenderingBuffer() == 0) {
            int metaBuffer = findRenderingBuffer(styleList);
            if (metaBuffer > 0) {
                mapArea = expandEnvelope(mapArea, worldToScreenTransform, metaBuffer);
                LOGGER.fine(
                        "Expanding rendering area by "
                                + metaBuffer
                                + " pixels to consider stroke width");

                // expand the screenmaps by the meta buffer, otherwise we'll throw away geomtries
                // that sit outside of the map, but whose symbolizer may contribute to it
                for (LiteFeatureTypeStyle lfts : styleList) {
                    if (lfts.screenMap != null) {
                        lfts.screenMap = new ScreenMap(lfts.screenMap, metaBuffer);
                    }
                }
            }
            setMetaBuffer(styleList, metaBuffer);
        }

        // take care of rendering transforms
        mapArea =
                expandEnvelopeByTransformations(styleList, new ReferencedEnvelope(mapArea, mapCRS));

        // build a list of attributes used in the rendering
        List<PropertyName> attributes;
        if (styleList == null) {
            attributes = null;
        } else {
            attributes = findStyleAttributes(styleList, schema);
        }

        ReferencedEnvelope envelope = new ReferencedEnvelope(mapArea, mapCRS);
        // see what attributes we really need by exploring the styles
        // for testing purposes we have a null case -->
        try {
            // Then create the geometry filters. We have to create one for
            // each geometric attribute used during the rendering as the
            // feature may have more than one and the styles could use non
            // default geometric ones
            List<ReferencedEnvelope> envelopes = null;
            // enable advanced projection handling with the updated map extent
            if (isAdvancedProjectionHandlingEnabled()) {
                Map<String, Object> projectionHints = new HashMap<>();
                if (isAdvancedProjectionDensificationEnabled()
                        && !CRS.equalsIgnoreMetadata(featCrs, mapCRS)) {
                    double tolerance = getAdvancedProjectionDensificationTolerance();
                    if (tolerance > 0.0) {
                        ReferencedEnvelope targetEnvelope = envelope;
                        ReferencedEnvelope sourceEnvelope =
                                transformEnvelope(targetEnvelope, featCrs);
                        if (sourceEnvelope != null
                                && !sourceEnvelope.isEmpty()
                                && !sourceEnvelope.isNull()) {
                            setupDensificationHints(
                                    mapCRS,
                                    featCrs,
                                    screenSize,
                                    worldToScreenTransform,
                                    projectionHints,
                                    tolerance,
                                    sourceEnvelope);
                        }
                    }
                }
                if (!isWrappingHeuristicEnabled()) {
                    projectionHints.put(
                            WrappingProjectionHandler.DATELINE_WRAPPING_CHECK_ENABLED, false);
                }
                // get the projection handler and set a tentative envelope
                ProjectionHandler projectionHandler =
                        ProjectionHandlerFinder.getHandler(
                                envelope, featCrs, isMapWrappingEnabled(), projectionHints);
                if (projectionHandler != null) {
                    setProjectionHandler(styleList, projectionHandler);
                    envelopes = projectionHandler.getQueryEnvelopes();
                }
            }
            if (envelopes == null) {
                if (mapCRS != null
                        && featCrs != null
                        && !CRS.equalsIgnoreMetadata(featCrs, mapCRS)) {
                    envelopes = Collections.singletonList(envelope.transform(featCrs, true, 10));
                } else {
                    envelopes = Collections.singletonList(envelope);
                }
            }

            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Querying layer " + schema.getName() + " with bbox: " + envelope);
            filter = createBBoxFilters(schema, attributes, envelopes);

            // now build the query using only the attributes and the
            // bounding box needed
            query = new Query(schema.getName().getLocalPart());
            query.setFilter(filter);
            query.setProperties(attributes);
            processRuleForQuery(styleList, query);

        } catch (Exception e) {
            final Exception txException = new Exception("Error transforming bbox", e);
            LOGGER.log(Level.SEVERE, "Error querying layer", txException);
            fireErrorEvent(txException);

            canTransform = false;
            query = new Query(schema.getName().getLocalPart());
            query.setProperties(attributes);
            Envelope bounds = source.getBounds();
            if (bounds != null && envelope.intersects(bounds)) {
                LOGGER.log(
                        Level.WARNING,
                        "Got a tranform exception while trying to de-project the current "
                                + "envelope, bboxs intersect therefore using envelope)",
                        e);
                filter = null;
                filter = createBBoxFilters(schema, attributes, Collections.singletonList(envelope));
                query.setFilter(filter);
            } else {
                LOGGER.log(
                        Level.WARNING,
                        "Got a tranform exception while trying to de-project the current "
                                + "envelope, falling back on full data loading (no bbox query)",
                        e);
                query.setFilter(Filter.INCLUDE);
            }
            processRuleForQuery(styleList, query);
        }

        // get the eventual sort-by from the styles
        SortBy[] sortBy = getSortByFromLiteStyles(styleList);
        if (sortBy != null) {
            QueryCapabilities qc = source.getQueryCapabilities();
            if (qc != null && !qc.supportsSorting(sortBy)) {
                throw new IllegalArgumentException(
                        "The feature source in layer "
                                + layer.getTitle()
                                + " cannot sort on "
                                + Arrays.toString(sortBy));
            }
            query.setSortBy(sortBy);
        }

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
            CoordinateReferenceSystem crs = getNativeCRS(schema, attributes);
            if (crs != null) {
                Set<RenderingHints.Key> fsHints = source.getSupportedHints();

                SingleCRS crs2D = crs == null ? null : CRS.getHorizontalCRS(crs);
                MathTransform sourceToScreen =
                        buildFullTransform(crs2D, mapCRS, worldToScreenTransform);
                double[] spans =
                        getGeneralizationSpans(
                                envelope,
                                sourceToScreen,
                                worldToScreenTransform,
                                featCrs,
                                screenSize);
                for (LiteFeatureTypeStyle fts : styleList) {
                    if (fts.screenMap != null) {
                        fts.screenMap.setTransform(sourceToScreen);
                        fts.screenMap.setSpans(spans[0], spans[1]);
                        if (fsHints.contains(Hints.SCREENMAP)) {
                            // replace the renderer screenmap with the hint, and avoid doing
                            // the work twice
                            hints.put(Hints.SCREENMAP, fts.screenMap);
                            fts.screenMap = null;
                        }
                    }
                }

                double distance = spans[0] < spans[1] ? spans[0] : spans[1];
                if (hasRenderingTransformation) {
                    // the RT might need valid geometries, we can at most apply a topology
                    // preserving generalization
                    if (fsHints.contains(Hints.GEOMETRY_GENERALIZATION)) {
                        hints.put(Hints.GEOMETRY_GENERALIZATION, distance);
                        disableInMemoryGeneralization(styleList);
                    }
                } else {
                    // ... if possible we let the datastore do the generalization
                    if (fsHints.contains(Hints.GEOMETRY_SIMPLIFICATION)) {
                        // good, we don't need to perform in memory generalization, the datastore
                        // does it all for us
                        hints.put(Hints.GEOMETRY_SIMPLIFICATION, distance);
                        disableInMemoryGeneralization(styleList);
                    } else if (fsHints.contains(Hints.GEOMETRY_DISTANCE)) {
                        // in this case the datastore can get us close, but we can still
                        // perform some in memory generalization
                        hints.put(Hints.GEOMETRY_DISTANCE, distance);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Error computing the generalization hints", e);
        }

        if (query.getHints() == null) {
            query.setHints(hints);
        } else {
            query.getHints().putAll(hints);
        }

        // simplify the filter
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        simplifier.setFeatureType(source.getSchema());
        Filter simplifiedFilter = (Filter) query.getFilter().accept(simplifier, null);
        query.setFilter(simplifiedFilter);

        return query;
    }

    private void setupDensificationHints(
            CoordinateReferenceSystem mapCRS,
            CoordinateReferenceSystem featCrs,
            Rectangle screenSize,
            AffineTransform worldToScreenTransform,
            Map<String, Object> projectionHints,
            double tolerance,
            ReferencedEnvelope sourceEnvelope)
            throws NoninvertibleTransformException, FactoryException {
        AffineTransform at = worldToScreenTransform;
        AffineTransform screenToWorldTransform = new AffineTransform(at);
        screenToWorldTransform.invert();
        MathTransform2D crsTransform =
                (MathTransform2D)
                        CRS.findMathTransform(
                                CRS.getHorizontalCRS(featCrs), CRS.getHorizontalCRS(mapCRS));
        MathTransform2D screenTransform = new AffineTransform2D(at);
        MathTransform2D fullTranform =
                (MathTransform2D) ConcatenatedTransform.create(crsTransform, screenTransform);
        Rectangle2D.Double sourceDomain =
                new Rectangle2D.Double(
                        sourceEnvelope.getMinX(),
                        sourceEnvelope.getMinY(),
                        sourceEnvelope.getWidth(),
                        sourceEnvelope.getHeight());
        WarpBuilder wb = new WarpBuilder(tolerance);
        double densifyDistance = 0.0;
        int[] actualSplit =
                wb.isValidDomain(sourceDomain)
                        ? wb.getRowColsSplit(fullTranform, sourceDomain)
                        : null;
        double minDistance =
                Math.min(
                        MAX_PIXELS_DENSIFY * sourceEnvelope.getWidth() / screenSize.getWidth(),
                        MAX_PIXELS_DENSIFY * sourceEnvelope.getHeight() / screenSize.getHeight());
        if (actualSplit == null) {
            // alghoritm gave up, we decide to use a fixed distance value
            densifyDistance = minDistance;
        } else if (actualSplit[0] != 1 || actualSplit[1] != 1) {

            densifyDistance =
                    Math.max(
                            Math.min(
                                    sourceEnvelope.getWidth() / actualSplit[0],
                                    sourceEnvelope.getHeight() / actualSplit[1]),
                            minDistance);
        }
        if (densifyDistance > 0.0) {
            projectionHints.put(ProjectionHandler.ADVANCED_PROJECTION_DENSIFY, densifyDistance);
        }
    }

    private double[] getGeneralizationSpans(
            ReferencedEnvelope envelope,
            MathTransform sourceToScreen,
            AffineTransform worldToScreenTransform,
            CoordinateReferenceSystem featureCRS,
            Rectangle screen)
            throws TransformException {
        // can we cut on the valid area? No? well, let's hope for the best
        try {
            ProjectionHandler ph =
                    ProjectionHandlerFinder.getHandler(
                            new ReferencedEnvelope(featureCRS),
                            envelope.getCoordinateReferenceSystem(),
                            false);
            if (isAdvancedProjectionHandlingEnabled() && ph != null) {

                Polygon renderPolygon = JTS.toGeometry(envelope);
                // make it cut to valid area
                Geometry preProcessed = ph.preProcess(renderPolygon);
                if (preProcessed != null && !preProcessed.isEmpty()) {
                    LinearTransform w2s = ProjectiveTransform.create(worldToScreenTransform);
                    Geometry screenGeometry = JTS.transform(preProcessed, w2s);
                    Envelope screenEnvelope = screenGeometry.getEnvelopeInternal();
                    int minX, minY, maxX, maxY;
                    if (screenEnvelope.getWidth() > 1) {
                        // ensure expansion does not bring it outside of the valid area
                        minX = (int) Math.ceil(screenEnvelope.getMinX());
                        maxX = (int) Math.floor(screenEnvelope.getMaxX());
                    } else {
                        double midPoint = (screenEnvelope.getMinX() + screenEnvelope.getMaxX()) / 2;
                        minX = maxX = (int) Math.round(midPoint);
                    }
                    if (screenEnvelope.getHeight() > 1) {
                        // ensure expansion does not bring it outside of the valid area
                        minY = (int) Math.ceil(screenEnvelope.getMinY());
                        maxY = (int) Math.floor(screenEnvelope.getMaxY());
                    } else {
                        double midPoint = (screenEnvelope.getMinY() + screenEnvelope.getMaxY()) / 2;
                        minY = maxY = (int) Math.round(midPoint);
                    }

                    screen = new Rectangle(minX, minY, maxX - minX, maxY - minY);
                }
            }

        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(
                        Level.INFO,
                        "Failed to compute the generalization spans with projection handlers, falling back to full area evaluation",
                        e);
        }

        // fallback, use the entire rendering area
        return Decimator.computeGeneralizationDistances(
                sourceToScreen.inverse(), screen, generalizationDistance);
    }

    protected ReferencedEnvelope transformEnvelope(
            ReferencedEnvelope envelope, CoordinateReferenceSystem crs)
            throws TransformException, FactoryException {
        try {
            ProjectionHandler projectionHandler =
                    ProjectionHandlerFinder.getHandler(envelope, crs, isMapWrappingEnabled());
            if (projectionHandler != null) {
                return projectionHandler.getProjectedEnvelope(envelope, crs);
            } else {
                return envelope.transform(crs, true);
            }
        } catch (FactoryException e) {
            return envelope.transform(crs, true);
        }
    }

    private void setMetaBuffer(List<LiteFeatureTypeStyle> styleList, int metaBuffer) {
        for (LiteFeatureTypeStyle fts : styleList) {
            fts.metaBuffer = metaBuffer;
        }
    }

    private void setProjectionHandler(
            List<LiteFeatureTypeStyle> styleList, ProjectionHandler projectionHandler) {
        for (LiteFeatureTypeStyle fts : styleList) {
            fts.projectionHandler = projectionHandler;
        }
    }

    private void disableInMemoryGeneralization(List<LiteFeatureTypeStyle> styleList) {
        for (LiteFeatureTypeStyle fts : styleList) {
            fts.inMemoryGeneralization = false;
        }
    }

    /**
     * Returns the sort-by from the list of feature type styles for a given layer. The code assumes
     * the styles have already been classified and are uniform in sorting clauses
     */
    private SortBy[] getSortByFromLiteStyles(List<LiteFeatureTypeStyle> styles) {
        if (styles != null) {
            for (LiteFeatureTypeStyle fts : styles) {
                if (fts.sortBy != null) {
                    return fts.sortBy;
                }
            }
        }

        return null;
    }

    Query getDefinitionQuery(
            Layer currLayer,
            FeatureSource<FeatureType, Feature> source,
            CoordinateReferenceSystem featCrs)
            throws FactoryException {
        // now, if a definition query has been established for this layer, be
        // sure to respect it by combining it with the bounding box one.
        Query definitionQuery = new Query(reprojectQuery(currLayer.getQuery(), source));
        definitionQuery.setCoordinateSystem(featCrs);

        return definitionQuery;
    }

    /** Takes care of eventual geometric transformations */
    ReferencedEnvelope expandEnvelopeByTransformations(
            List<LiteFeatureTypeStyle> styles, ReferencedEnvelope envelope) {
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = new ReferencedEnvelope(envelope);
        if (styles != null) {
            for (LiteFeatureTypeStyle lts : styles) {
                List<Rule> rules = new ArrayList<>();
                rules.addAll(Arrays.asList(lts.ruleList));
                rules.addAll(Arrays.asList(lts.elseRules));
                for (Rule r : rules) {
                    for (Symbolizer s : r.symbolizers()) {
                        if (s.getGeometry() != null) {
                            ReferencedEnvelope re =
                                    (ReferencedEnvelope) s.getGeometry().accept(visitor, envelope);
                            if (re != null) {
                                result.expandToInclude(re);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Builds a full transform going from the source CRS to the destination CRS and from there to
     * the screen.
     *
     * <p>Although we ask for 2D content (via {@link Hints#FEATURE_2D} ) not all DataStore
     * implementations are capable. In this event we will manually stage the information into {@link
     * DefaultGeographicCRS#WGS84}) and before using this transform.
     */
    private MathTransform buildFullTransform(
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem destCRS,
            AffineTransform worldToScreenTransform)
            throws FactoryException {
        MathTransform mt = buildTransform(sourceCRS, destCRS);

        // concatenate from world to screen
        if (mt != null && !mt.isIdentity()) {
            mt =
                    ConcatenatedTransform.create(
                            mt, ProjectiveTransform.create(worldToScreenTransform));
        } else {
            mt = ProjectiveTransform.create(worldToScreenTransform);
        }

        return mt;
    }

    /**
     * Builds the transform from sourceCRS to destCRS/
     *
     * <p>Although we ask for 2D content (via {@link Hints#FEATURE_2D} ) not all DataStore
     * implementations are capable. With that in mind if the provided soruceCRS is not 2D we are
     * going to manually post-process the Geomtries into {@link DefaultGeographicCRS#WGS84} - and
     * the {@link MathTransform2D} returned here will transition from WGS84 to the requested
     * destCRS.
     *
     * @return the transform, or null if any of the crs is null, or if the the two crs are equal
     * @throws FactoryException If no transform is available to the destCRS
     */
    private MathTransform buildTransform(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem destCRS)
            throws FactoryException {
        MathTransform transform = null;
        if (sourceCRS != null && sourceCRS.getCoordinateSystem().getDimension() >= 3) {
            // We are going to transform over to DefaultGeographic.WGS84 on the fly
            // so we will set up our math transform to take it from there
            MathTransform toWgs84_3d =
                    CRS.findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84_3D);
            MathTransform toWgs84_2d =
                    CRS.findMathTransform(
                            DefaultGeographicCRS.WGS84_3D, DefaultGeographicCRS.WGS84);
            transform = ConcatenatedTransform.create(toWgs84_3d, toWgs84_2d);
            sourceCRS = DefaultGeographicCRS.WGS84;
        }
        // the basic crs transformation, if any
        MathTransform2D mt;
        if (sourceCRS == null || destCRS == null || CRS.equalsIgnoreMetadata(sourceCRS, destCRS)) {
            mt = null;
        } else {
            mt = (MathTransform2D) CRS.findMathTransform(sourceCRS, destCRS, true);
        }

        if (transform != null) {
            if (mt == null) {
                return transform;
            } else {
                return ConcatenatedTransform.create(transform, mt);
            }
        } else {
            return mt;
        }
    }

    /**
     * Scans the schema for the specified attributes are returns a single CRS if all the geometric
     * attributes in the lot share one CRS, null if there are different ones
     */
    private CoordinateReferenceSystem getNativeCRS(
            FeatureType schema, List<PropertyName> attNames) {
        // first off, check how many crs we have, this hint works only
        // if we have just one native CRS at hand (and the native CRS is known
        CoordinateReferenceSystem crs = null;
        // NC - property (namespace) support
        for (PropertyName name : attNames) {
            Object att = name.evaluate(schema);

            if (att instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) att;
                CoordinateReferenceSystem gdCrs = gd.getCoordinateReferenceSystem();
                if (crs == null) {
                    crs = gdCrs;
                } else if (gdCrs == null) {
                    crs = null;
                    break;
                } else if (!CRS.equalsIgnoreMetadata(crs, gdCrs)) {
                    crs = null;
                    break;
                }
            }
        }
        return crs;
    }

    /**
     * JE: If there is a single rule "and" its filter together with the query's filter and send it
     * off to datastore. This will allow as more processing to be done on the back end... Very
     * useful if DataStore is a database. Problem is that worst case each filter is ran twice. Next
     * we will modify it to find a "Common" filter between all rules and send that to the datastore.
     *
     * <p>DJB: trying to be smarter. If there are no "elseRules" and no rules w/o a filter, then it
     * makes sense to send them off to the Datastore We limit the number of Filters sent off to the
     * datastore, just because it could get a bit rediculous. In general, for a database, if you can
     * limit 10% of the rows being returned you're probably doing quite well. The main problem is
     * when your filters really mean you're secretly asking for all the data in which case sending
     * the filters to the Datastore actually costs you. But, databases are *much* faster at
     * processing the Filters than JAVA is and can use statistical analysis to do it.
     */
    private void processRuleForQuery(List<LiteFeatureTypeStyle> styles, Query q) {
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
            final List<Filter> filtersToDS = new ArrayList<>();
            // look at each featuretypestyle
            for (LiteFeatureTypeStyle style : styles) {
                if (style.elseRules.length > 0) // uh-oh has elseRule
                return;
                // look at each rule in the featuretypestyle
                for (Rule r : style.ruleList) {
                    if (r.getFilter() == null) return; // uh-oh has no filter (want all rows)
                    filtersToDS.add(r.getFilter());
                }
            }

            // if too many bail out
            if (filtersToDS.size() > maxFilters) return;

            // or together all the filters
            org.opengis.filter.Filter ruleFiltersCombined;
            if (filtersToDS.size() == 1) {
                ruleFiltersCombined = filtersToDS.get(0);
            } else {
                ruleFiltersCombined = filterFactory.or(filtersToDS);
            }

            // combine with the pre-existing filter
            ruleFiltersCombined = filterFactory.and(q.getFilter(), ruleFiltersCombined);
            q.setFilter(ruleFiltersCombined);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(
                        Level.SEVERE,
                        "Could not send rules to datastore due to: " + e.getLocalizedMessage(),
                        e);
        }
    }

    /**
     * find out the maximum number of filters we're going to send off to the datastore. See
     * processRuleForQuery() for details.
     */
    private int getMaxFiltersToSendToDatastore() {
        try {
            if (rendererHints == null) return defaultMaxFiltersToSendToDatastore;

            Integer result = (Integer) rendererHints.get("maxFiltersToSendToDatastore");
            if (result == null) return defaultMaxFiltersToSendToDatastore; // default if not
            // present in hints
            return result.intValue();

        } catch (Exception e) {
            return defaultMaxFiltersToSendToDatastore;
        }
    }

    /**
     * Checks if optimized feature type style rendering is enabled, or not. See {@link
     * #OPTIMIZE_FTS_RENDERING_KEY} description for a full explanation.
     */
    private boolean isOptimizedFTSRenderingEnabled() {
        if (rendererHints == null) return true;
        Object result = rendererHints.get(OPTIMIZE_FTS_RENDERING_KEY);
        if (result == null) return true;
        return Boolean.TRUE.equals(result);
    }

    /** Checks if the advanced projection handling is enabled */
    private boolean isAdvancedProjectionHandlingEnabled() {
        if (rendererHints == null) return false;
        Object result = rendererHints.get(ADVANCED_PROJECTION_HANDLING_KEY);
        if (result == null) return false;
        return Boolean.TRUE.equals(result);
    }

    /** Checks if continuous map wrapping is enabled */
    private boolean isMapWrappingEnabled() {
        if (rendererHints == null) return false;
        Object result = rendererHints.get(CONTINUOUS_MAP_WRAPPING);
        if (result == null) return false;
        return Boolean.TRUE.equals(result);
    }

    /**
     * Checks if the geometries in spatial filters in the SLD must be assumed to be expressed in the
     * official EPSG axis order, regardless of how the referencing subsystem is configured (this is
     * required to support filter reprojection in WMS 1.3+)
     */
    private boolean isEPSGAxisOrderForced() {
        if (rendererHints == null) return false;
        Object result = rendererHints.get(FORCE_EPSG_AXIS_ORDER_KEY);
        if (result == null) return false;
        return Boolean.TRUE.equals(result);
    }

    /**
     * Checks if vector rendering is enabled or not. See {@link
     * SLDStyleFactory#isVectorRenderingEnabled()} for a full explanation.
     */
    private boolean isVectorRenderingEnabled() {
        if (rendererHints == null) return true;
        Object result = rendererHints.get(VECTOR_RENDERING_KEY);
        if (result == null) return VECTOR_RENDERING_ENABLED_DEFAULT;
        return ((Boolean) result).booleanValue();
    }

    /** Checks if advanced projection densification is enabled or not. */
    private boolean isAdvancedProjectionDensificationEnabled() {
        if (rendererHints == null) return false;
        Object result = rendererHints.get(ADVANCED_PROJECTION_DENSIFICATION_KEY);
        if (result == null) return ADVANCED_PROJECTION_DENSIFICATION_DEFAULT;
        return ((Boolean) result).booleanValue();
    }

    private double getAdvancedProjectionDensificationTolerance() {
        if (rendererHints == null) return ADVANCED_PROJECTION_DENSIFICATION_TOLERANCE_DEFAULT;
        Object result = rendererHints.get(ADVANCED_PROJECTION_DENSIFICATION_TOLERANCE_KEY);
        if (result == null) return ADVANCED_PROJECTION_DENSIFICATION_TOLERANCE_DEFAULT;
        return ((Double) result).doubleValue();
    }

    /** Checks if advanced projection wrapping heuristic should be enabled. */
    private boolean isWrappingHeuristicEnabled() {
        if (rendererHints == null) return true;
        Object result = rendererHints.get(DATELINE_WRAPPING_HEURISTIC_KEY);
        if (result == null) return DATELINE_WRAPPING_HEURISTIC_DEFAULT;
        return ((Boolean) result).booleanValue();
    }

    /**
     * Returns an estimate of the rendering buffer needed to properly display this layer taking into
     * consideration the constant stroke sizes in the feature type styles.
     *
     * @param styles the feature type styles to be applied to the layer
     * @return an estimate of the buffer that should be used to properly display a layer rendered
     *     with the specified styles
     */
    private int findRenderingBuffer(List<LiteFeatureTypeStyle> styles) {
        final MetaBufferEstimator rbe = new MetaBufferEstimator();

        for (LiteFeatureTypeStyle lfts : styles) {
            Rule[] rules = lfts.elseRules;
            for (Rule value : rules) {
                rbe.visit(value);
            }
            rules = lfts.ruleList;
            for (Rule rule : rules) {
                rbe.visit(rule);
            }
        }

        if (!rbe.isEstimateAccurate())
            LOGGER.fine(
                    "Assuming rendering buffer = "
                            + rbe.getBuffer()
                            + ", but estimation is not accurate, you may want to set a buffer manually");

        // the actual amount we have to grow the rendering area by is half of the stroke/symbol
        // sizes
        // plus one extra pixel for antialiasing effects
        return (int) Math.round(rbe.getBuffer() / 2.0 + 1);
    }

    /**
     * Inspects the <code>Layer</code>'s style and retrieves it's needed attribute names, returning
     * at least the default geometry attribute name.
     *
     * @param styles the <code>styles</code> to determine the needed attributes from
     * @param schema the <code>layer</code>'s FeatureSource<SimpleFeatureType, SimpleFeature> schema
     * @return the minimum set of attribute names needed to render <code>layer</code>
     */
    private List<PropertyName> findStyleAttributes(
            List<LiteFeatureTypeStyle> styles, FeatureType schema) {
        final StyleAttributeExtractor sae = new StyleAttributeExtractor();

        for (LiteFeatureTypeStyle lfts : styles) {
            for (Rule rule : lfts.elseRules) {
                sae.visit(rule);
            }
            for (Rule rule : lfts.ruleList) {
                sae.visit(rule);
            }
        }

        if (sae.isUsingDynamincProperties()) {
            return null;
        }
        Set<PropertyName> attributes = sae.getAttributes();
        Set<String> attributeNames = sae.getAttributeNameSet();

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
        List<PropertyName> atts = new ArrayList<>(attributes);
        Collection<PropertyDescriptor> attTypes = schema.getDescriptors();
        Name attName;

        for (PropertyDescriptor pd : attTypes) {
            // attName = pd.getName().getLocalPart();
            attName = pd.getName();

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

            if ((attName.getLocalPart().equalsIgnoreCase("grid"))
                            && !attributeNames.contains(attName.getLocalPart())
                    || (attName.getLocalPart().equalsIgnoreCase("params"))
                            && !attributeNames.contains(attName.getLocalPart())) {
                atts.add(filterFactory.property(attName));
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("added attribute " + attName);
            }
        }

        try {
            // DJB:geos-469 if the default geometry was used in the style, we need to grab it.
            // we substitute the default geometry attribute "" with the proper geometry attribute
            // name (this will help us avoid
            // situations were the geometry is not read because of the default geometry attribute ""
            // not being taken in account)
            if (sae.getDefaultGeometryUsed()
                    && !attributeNames.contains(
                            schema.getGeometryDescriptor().getName().toString())) {
                atts.add(filterFactory.property(schema.getGeometryDescriptor().getName()));
            }
            // the geometry attribute name was added above, we need to remove the default geometry
            // attribute "" if present
            for (Iterator<PropertyName> it = atts.iterator(); it.hasNext(); ) {
                PropertyName propertyName = it.next();
                if (propertyName.getPropertyName().equals("")) {
                    // well the default geometry attribute name "" is present, so let's remove it
                    it.remove();
                    break;
                }
            }
        } catch (Exception e) {
            // might not be a geometry column. That will cause problems down the
            // road (why render a non-geometry layer)
        }

        return atts;
    }

    /**
     * Creates the bounding box filters (one for each geometric attribute) needed to query a <code>
     * MapLayer</code>'s feature source to return just the features for the target rendering extent
     *
     * @param schema the layer's feature source schema
     * @param attributes set of needed attributes
     * @param bboxes the rendering bounding boxes
     * @return an or'ed list of bbox filters, one for each geometric attribute in <code>attributes
     *     </code>. If there are just one geometric attribute, just returns its corresponding <code>
     *     GeometryFilter</code>.
     * @throws IllegalFilterException if something goes wrong creating the filter
     */
    private Filter createBBoxFilters(
            FeatureType schema, List<PropertyName> attributes, List<ReferencedEnvelope> bboxes)
            throws IllegalFilterException {
        // if there are no bboxes to render then use Filter.EXCLUDE as there is no clear way
        // to return
        if (bboxes.isEmpty()) {
            return Filter.EXCLUDE;
        }

        Filter filter = Filter.INCLUDE;
        Object attType;

        for (PropertyName attribute : attributes) {
            // NC - support nested attributes -> use evaluation for getting descriptor
            // result is not necessary a descriptor, is Name in case of @attribute
            attType = attribute.evaluate(schema);

            // the attribute type might be missing because of rendering transformations, skip it
            if (attType == null) {
                continue;
            }

            if (attType instanceof GeometryDescriptor) {
                Filter gfilter = new FastBBOX(attribute, bboxes.get(0), filterFactory);

                if (filter == Filter.INCLUDE) {
                    filter = gfilter;
                } else {
                    filter = filterFactory.or(filter, gfilter);
                }

                if (!bboxes.isEmpty()) {
                    for (int k = 1; k < bboxes.size(); k++) {
                        // filter = filterFactory.or( filter, new FastBBOX(localName, bboxes.get(k),
                        // filterFactory) );
                        filter =
                                filterFactory.or(
                                        filter,
                                        new FastBBOX(attribute, bboxes.get(k), filterFactory));
                    }
                }
            }
        }

        return filter;
    }

    /**
     * Checks if a rule can be triggered at the current scale level
     *
     * @param r The rule
     * @return true if the scale is compatible with the rule settings
     */
    private boolean isWithInScale(Rule r) {
        return ((r.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
                && ((r.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
    }

    /**
     * creates a list of LiteFeatureTypeStyles a) out-of-scale rules removed b) incompatible
     * FeatureTypeStyles removed
     *
     * @return ArrayList<LiteFeatureTypeStyle>
     */
    ArrayList<LiteFeatureTypeStyle> createLiteFeatureTypeStyles(
            Layer layer, Graphics2D graphics, boolean optimizedFTSRendering)
            throws IOException, FactoryException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(
                    "creating rules for scale denominator - "
                            + NumberFormat.getNumberInstance().format(scaleDenominator));
        ArrayList<LiteFeatureTypeStyle> result = new ArrayList<>();

        LiteFeatureTypeStyle lfts;
        boolean foundComposite = false;

        // check if any <VendorOption name="renderingMap">false</VendorOption>
        // is present in the style removing style's elements not meant to be applied
        // to the data
        RenderingSelectorStyleVisitor selectorStyleVisitor = new MapRenderingSelectorStyleVisitor();
        layer.getStyle().accept(selectorStyleVisitor);
        Style style = (Style) selectorStyleVisitor.getCopy();

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (isFeatureTypeStyleActive(layer.getFeatureSource().getSchema(), fts)) {
                // DJB: this FTS is compatible with this FT.

                // get applicable rules at the current scale
                List<List<Rule>> splittedRules = splitRules(fts);
                List<Rule> ruleList = splittedRules.get(0);
                List<Rule> elseRuleList = splittedRules.get(1);

                // if none, skip it
                if ((ruleList.isEmpty()) && (elseRuleList.isEmpty())) continue;

                // get the fts level composition, if any
                Composite composite = styleFactory.getComposite(fts.getOptions());
                foundComposite |= composite != null;
                // we can optimize this one and draw directly on the graphics, assuming
                // there is no composition
                if (!foundComposite && (result.isEmpty() || !optimizedFTSRendering)) {
                    lfts =
                            new LiteFeatureTypeStyle(
                                    layer,
                                    graphics,
                                    ruleList,
                                    elseRuleList,
                                    fts.getTransformation());
                } else {
                    lfts =
                            new LiteFeatureTypeStyle(
                                    layer,
                                    new DelayedBackbufferGraphic(graphics, screenSize),
                                    ruleList,
                                    elseRuleList,
                                    fts.getTransformation());
                }
                lfts.composite = composite;
                if (FeatureTypeStyle.VALUE_EVALUATION_MODE_FIRST.equals(
                        fts.getOptions().get(FeatureTypeStyle.KEY_EVALUATION_MODE))) {
                    lfts.matchFirst = true;
                }

                // get the sort by, if any
                SortBy[] sortBy = styleFactory.getSortBy(fts.getOptions());
                lfts.sortBy = sortBy;

                if (screenMapEnabled(lfts)) {
                    int renderingBuffer = getRenderingBuffer();
                    lfts.screenMap =
                            new ScreenMap(
                                    screenSize.x - renderingBuffer,
                                    screenSize.y - renderingBuffer,
                                    screenSize.width + renderingBuffer * 2,
                                    screenSize.height + renderingBuffer * 2);
                }

                result.add(lfts);
            }
        }

        if (!result.isEmpty()) {
            // make sure all spatial filters in the feature source native SRS
            reprojectSpatialFilters(result, layer.getFeatureSource().getSchema());

            // apply the uom and dpi rescale
            applyUnitRescale(result);
        }

        return result;
    }

    /**
     * Returns true if the ScreenMap optimization can be applied given the current renderer and
     * configuration and the style to be applied
     */
    boolean screenMapEnabled(LiteFeatureTypeStyle lfts) {
        if (generalizationDistance == 0.0) {
            return false;
        }

        OpacityFinder finder =
                new OpacityFinder(
                        new Class[] {
                            PointSymbolizer.class, LineSymbolizer.class, PolygonSymbolizer.class
                        });
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
        return fts.featureTypeNames().isEmpty()
                || ((ftype.getName().getLocalPart() != null)
                        && (fts.featureTypeNames().isEmpty()
                                || fts.featureTypeNames().stream()
                                        .anyMatch(tn -> FeatureTypes.matches(ftype, tn))));
    }

    private List<List<Rule>> splitRules(FeatureTypeStyle fts) {
        List<Rule> ruleList = new ArrayList<>();
        List<Rule> elseRuleList = new ArrayList<>();

        for (Rule r : fts.rules())
            if (isWithInScale(r)) {
                if (r.isElseFilter()) {
                    elseRuleList.add(r);
                } else {
                    // rules can have dynamic bits related to env variables that we evaluate and
                    // skip at this this time
                    if (!Filter.INCLUDE.equals(r.getFilter()) && hasEnvVariables(r.getFilter())) {
                        DuplicatingStyleVisitor cloner =
                                new DuplicatingStyleVisitor() {
                                    SimplifyingFilterVisitor simplifier =
                                            new SimplifyingFilterVisitor();

                                    @Override
                                    protected Filter copy(Filter filter) {
                                        if (filter == null) return null;
                                        return (Filter) filter.accept(simplifier, ff);
                                    }
                                };
                        r.accept(cloner);
                        Rule copy = (Rule) cloner.getCopy();
                        if (!Filter.EXCLUDE.equals(copy.getFilter())) {
                            ruleList.add(copy);
                        }
                    } else {
                        ruleList.add(r);
                    }
                }
            }

        return Arrays.asList(ruleList, elseRuleList);
    }

    private boolean hasEnvVariables(Filter filter) {
        if (filter == null) {
            return false;
        }
        DefaultFilterVisitor envFunctionChecker =
                new DefaultFilterVisitor() {
                    @Override
                    public Object visit(Function expression, Object data) {
                        if (Boolean.TRUE.equals(super.visit(expression, data))) {
                            return true;
                        } else {
                            return expression instanceof EnvFunction;
                        }
                    }
                };
        return Boolean.TRUE.equals(filter.accept(envFunctionChecker, null));
    }

    /**
     * When drawing in optimized mode a 32bit surface is created for each FeatureTypeStyle other
     * than the first in order to draw features in parallel while respecting the feature draw
     * ordering multiple FTS impose. This method allows to estimate how many megabytes will be
     * needed, in terms of back buffers, to draw the current {@link MapContent}, assuming the
     * feature type style optimizations are turned on (in the case they are off, no extra memory
     * will be used).
     *
     * @param width the image width
     * @param height the image height
     */
    public int getMaxBackBufferMemory(int width, int height) {
        int maxBuffers = 0;
        for (Layer layer : mapContent.layers()) {
            if (!layer.isVisible()) {
                // Only render layer when layer is visible
                continue;
            }

            // Skip layers that do not have multiple FeatureTypeStyles
            if (!(layer instanceof StyleLayer)) {
                continue;
            }

            StyleLayer styleLayer = (StyleLayer) layer;

            if (styleLayer.getStyle().featureTypeStyles().size() < 2) continue;

            // count how many lite feature type styles are active
            int currCount = 0;
            FeatureType ftype = layer.getFeatureSource().getSchema();
            for (FeatureTypeStyle fts : styleLayer.getStyle().featureTypeStyles()) {
                if (isFeatureTypeStyleActive(ftype, fts)) {
                    // get applicable rules at the current scale
                    List<List<Rule>> splittedRules = splitRules(fts);
                    List<Rule> ruleList = splittedRules.get(0);
                    List<Rule> elseRuleList = splittedRules.get(1);

                    // if none, skip this fts
                    if ((ruleList.isEmpty()) && (elseRuleList.isEmpty())) continue;

                    currCount++;
                }
            }
            // consider the first fts does not allocate a buffer
            currCount--;

            if (currCount > maxBuffers) maxBuffers = currCount;
        }

        return maxBuffers * width * height * 4;
    }

    /**
     * Applies all the styles to the features/coverages contained in the given layer.
     *
     * @param graphics Target graphics for rendering
     * @param layer The layer being styled
     * @param layerId Handle used to identify the layer in the {@link LabelCache}
     */
    private void processStylers(final Graphics2D graphics, final Layer layer, String layerId)
            throws Exception {
        // /////////////////////////////////////////////////////////////////////
        //
        // Preparing feature information and styles
        //
        // /////////////////////////////////////////////////////////////////////
        final FeatureSource featureSource = layer.getFeatureSource();
        if (featureSource == null) {
            throw new IllegalArgumentException(
                    "The layer does not contain a feature source: " + layer.getTitle());
        }
        final FeatureType schema = featureSource.getSchema();

        final ArrayList<LiteFeatureTypeStyle> lfts =
                createLiteFeatureTypeStyles(layer, graphics, isOptimizedFTSRenderingEnabled());
        if (lfts.isEmpty()) {
            return;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Processing " + lfts.size() + " stylers for " + schema.getName());
            }
        }

        // classify by sortby and transformation (aka how we produce the features to
        // be rendered)
        List<List<LiteFeatureTypeStyle>> txClassified = classifyByFeatureProduction(lfts);

        // render groups by uniform transformation
        for (List<LiteFeatureTypeStyle> uniformLfts : txClassified) {
            FeatureCollection features = getFeatures(layer, schema, uniformLfts);
            if (features == null) {
                continue;
            }

            // optimize filters for in memory sequential execution
            // step one, collect duplicated filters and expressions
            RepeatedFilterVisitor repeatedVisitor = new RepeatedFilterVisitor();
            uniformLfts.stream()
                    .flatMap(fts -> Arrays.stream(fts.ruleList))
                    .filter(r -> !r.isElseFilter() && r.getFilter() != null)
                    .forEach(r -> r.getFilter().accept(repeatedVisitor, null));
            Set<Object> repeatedObjects = repeatedVisitor.getRepeatedObjects();
            // step two, memoize the repeated ones and convert simple features access to indexed
            if (schema instanceof SimpleFeatureType || !repeatedObjects.isEmpty()) {
                MemoryFilterOptimizer filterOptimizer =
                        new MemoryFilterOptimizer(features.getSchema(), repeatedObjects);
                for (LiteFeatureTypeStyle fts : uniformLfts) {
                    for (int i = 0; i < fts.ruleList.length; i++) {
                        Rule rule = fts.ruleList[i];
                        DuplicatingStyleVisitor optimizingStyleVisitor =
                                new DuplicatingStyleVisitor(
                                        STYLE_FACTORY, filterFactory, filterOptimizer);
                        rule.accept(optimizingStyleVisitor);
                        fts.ruleList[i] = (Rule) optimizingStyleVisitor.getCopy();
                    }
                }
            }

            // finally, perform rendering
            if (isOptimizedFTSRenderingEnabled() && lfts.size() > 1) {
                drawOptimized(graphics, layerId, features, uniformLfts);
            } else {
                drawPlain(graphics, layerId, features, uniformLfts);
            }
        }
    }

    FeatureCollection getFeatures(
            final Layer layer,
            final FeatureType schema,
            List<LiteFeatureTypeStyle> featureTypeStyles)
            throws IOException, FactoryException, NoninvertibleTransformException, SchemaException,
                    TransformException {
        @SuppressWarnings("unchecked")
        final FeatureSource<FeatureType, Feature> featureSource =
                (FeatureSource<FeatureType, Feature>) layer.getFeatureSource();
        Expression transform = featureTypeStyles.get(0).transformation;

        // grab the source crs and geometry attribute
        final CoordinateReferenceSystem sourceCrs;
        final GeometryDescriptor geometryAttribute = schema.getGeometryDescriptor();
        if (geometryAttribute != null && geometryAttribute.getType() != null) {
            sourceCrs = geometryAttribute.getType().getCoordinateReferenceSystem();
        } else {
            sourceCrs = null;
        }

        // ... assume we have to do the generalization, the query layer process will
        // turn down the flag if we don't
        boolean hasTransformation = transform != null;
        Query styleQuery =
                getStyleQuery(
                        layer,
                        featureTypeStyles,
                        mapExtent,
                        destinationCrs,
                        sourceCrs,
                        screenSize,
                        geometryAttribute,
                        worldToScreenTransform,
                        hasTransformation);
        Query definitionQuery = getDefinitionQuery(layer, featureSource, sourceCrs);
        FeatureCollection features = null;
        if (hasTransformation) {
            // prepare the stage for the raster transformations
            GridGeometry2D gridGeometry = getRasterGridGeometry(destinationCrs, sourceCrs);
            // vector transformation wise, we have to account for two separate queries,
            // the one attached to the layer and then one coming from SLD.
            // The first source attributes, the latter talks tx output attributes
            // so they have to be applied before and after the transformation respectively
            RenderingTransformationHelper helper = new GCRRenderingTransformationHelper(layer);

            Object result =
                    helper.applyRenderingTransformation(
                            transform,
                            featureSource,
                            definitionQuery,
                            styleQuery,
                            gridGeometry,
                            sourceCrs,
                            java2dHints);
            if (result == null) {
                return null;
            } else if (result instanceof FeatureCollection) {
                features = (FeatureCollection) result;
            } else if (result instanceof GridCoverage2D) {
                GridCoverage2D coverage = (GridCoverage2D) result;
                // we only avoid disposing if the input was a in memory GridCovereage2D
                if ((schema instanceof SimpleFeatureType
                        && !FeatureUtilities.isWrappedCoverage((SimpleFeatureType) schema))) {
                    coverage = new DisposableGridCoverage(coverage);
                }
                features = FeatureUtilities.wrapGridCoverage(coverage);
            } else if (result instanceof GridCoverage2DReader) {
                features =
                        FeatureUtilities.wrapGridCoverageReader(
                                (GridCoverage2DReader) result, null);
            } else {
                throw new IllegalArgumentException(
                        "Don't know how to handle the results of the transformation, "
                                + "the supported result types are FeatureCollection, GridCoverage2D "
                                + "and GridCoverage2DReader, but we got: "
                                + result.getClass());
            }
        } else {
            Query mixed = DataUtilities.mixQueries(definitionQuery, styleQuery, null);
            // mix the sort by, the style query takes precedence
            if (styleQuery.getSortBy() != null) {
                mixed.setSortBy(styleQuery.getSortBy());
            } else {
                mixed.setSortBy(definitionQuery.getSortBy());
            }
            checkAttributeExistence(featureSource.getSchema(), mixed);
            features = featureSource.getFeatures(mixed);
            features = RendererUtilities.fixFeatureCollectionReferencing(features, sourceCrs);
        }

        // HACK HACK HACK
        // For complex features, we need the targetCrs and version in scenario where we have
        // a top level feature that does not contain a geometry(therefore no crs) and has a
        // nested feature that contains geometry as its property.Furthermore it is possible
        // for each nested feature to have different crs hence we need to reproject on each
        // feature accordingly.
        // This is a Hack, this information should not be passed through feature type
        // appschema will need to remove this information from the feature type again
        if (!(features instanceof SimpleFeatureCollection)) {
            features.getSchema().getUserData().put("targetCrs", destinationCrs);
            features.getSchema().getUserData().put("targetVersion", "wms:getmap");
        }

        return features;
    }

    /**
     * Classify a List of LiteFeatureTypeStyle objects by Transformation.
     *
     * @param lfts A List of LiteFeatureTypeStyles
     * @return A List of List of LiteFeatureTypeStyles
     */
    List<List<LiteFeatureTypeStyle>> classifyByFeatureProduction(List<LiteFeatureTypeStyle> lfts) {
        List<List<LiteFeatureTypeStyle>> txClassified = new ArrayList<>();
        txClassified.add(new ArrayList<>());
        Expression transformation = null;
        SortBy[] sortBy = null;
        for (int i = 0; i < lfts.size(); i++) {
            LiteFeatureTypeStyle curr = lfts.get(i);
            if (i == 0) {
                transformation = curr.transformation;
                sortBy = curr.sortBy;
            } else {
                // do they have the same transformation?
                boolean differentTransformation =
                        (transformation != curr.transformation)
                                || (transformation != null
                                        && curr.transformation != null
                                        && !curr.transformation.equals(transformation));

                // is sorting incompatible, that is, different from the one
                // we are working against? "null" means not caring about sorting,
                // it's thus compatible with whatever other sort
                boolean incompatibleSort = false;
                if (curr.sortBy != null) {
                    if (sortBy == null) {
                        // we started with "whatever sorting", from here on we have one
                        sortBy = curr.sortBy;
                    } else {
                        incompatibleSort = true;
                    }
                }
                if (differentTransformation || incompatibleSort) {
                    // create a new slot (we always add the lfts into the last one)
                    txClassified.add(new ArrayList<>());
                    transformation = curr.transformation;
                    sortBy = curr.sortBy;
                }
            }
            txClassified.get(txClassified.size() - 1).add(curr);
        }
        return txClassified;
    }

    /**
     * Checks the attributes in the query (which we got from the SLD) match the schema, throws an
     * {@link IllegalFilterException} otherwise
     */
    void checkAttributeExistence(FeatureType schema, Query query) {
        if (query.getProperties() == null) {
            return;
        }

        for (PropertyName attribute : query.getProperties()) {
            if (attribute.evaluate(schema) == null) {
                if (schema instanceof SimpleFeatureType) {
                    List<Name> allNames = new ArrayList<>();
                    for (PropertyDescriptor pd : schema.getDescriptors()) {
                        allNames.add(pd.getName());
                    }
                    throw new IllegalFilterException(
                            "Could not find '"
                                    + attribute
                                    + "' in the FeatureType ("
                                    + schema.getName()
                                    + "), available attributes are: "
                                    + allNames);
                } else {
                    throw new IllegalFilterException(
                            "Could not find '"
                                    + attribute
                                    + "' in the FeatureType ("
                                    + schema.getName()
                                    + ")");
                }
            }
        }
    }

    /**
     * Applies Unit Of Measure rescaling against all symbolizers, the result will be symbolizers
     * that operate purely in pixels
     */
    void applyUnitRescale(final ArrayList<LiteFeatureTypeStyle> lfts) {
        // apply dpi rescale
        double dpi = RendererUtilities.getDpi(getRendererHints());
        double standardDpi = RendererUtilities.getDpi(Collections.emptyMap());
        if (dpi != standardDpi) {
            double scaleFactor = dpi / standardDpi;
            DpiRescaleStyleVisitor dpiVisitor =
                    new GraphicsAwareDpiRescaleStyleVisitor(scaleFactor);
            for (LiteFeatureTypeStyle fts : lfts) {
                rescaleFeatureTypeStyle(fts, dpiVisitor);
            }
        }

        // apply UOM rescaling
        double pixelsPerMeters =
                RendererUtilities.calculatePixelsPerMeterRatio(scaleDenominator, rendererHints);
        UomRescaleStyleVisitor rescaleVisitor = new UomRescaleStyleVisitor(pixelsPerMeters);
        for (LiteFeatureTypeStyle fts : lfts) {
            rescaleFeatureTypeStyle(fts, rescaleVisitor);
        }
    }

    /**
     * Reprojects the spatial filters in each {@link LiteFeatureTypeStyle} so that they match the
     * feature source native coordinate system
     */
    void reprojectSpatialFilters(final ArrayList<LiteFeatureTypeStyle> lfts, FeatureType schema)
            throws FactoryException {
        CoordinateReferenceSystem declaredCRS = getDeclaredSRS(schema);

        // reproject spatial filters in each fts
        for (LiteFeatureTypeStyle fts : lfts) {
            reprojectSpatialFilters(fts, declaredCRS, schema);
        }
    }

    /** Computes the declared SRS of a layer based on the layer schema and the EPSG forcing flag */
    private CoordinateReferenceSystem getDeclaredSRS(FeatureType schema) throws FactoryException {
        // compute the default SRS of the feature source
        CoordinateReferenceSystem declaredCRS = schema.getCoordinateReferenceSystem();
        if (isEPSGAxisOrderForced()) {
            Integer code = CRS.lookupEpsgCode(declaredCRS, false);
            if (code != null) {
                declaredCRS = CRS.decode("urn:ogc:def:crs:EPSG::" + code);
            }
        }
        return declaredCRS;
    }

    /**
     * Reprojects all spatial filters in the specified Query so that they match the native srs of
     * the specified feature source
     */
    private Query reprojectQuery(Query query, FeatureSource<FeatureType, Feature> source)
            throws FactoryException {
        if (query == null || query.getFilter() == null) {
            return query;
        }

        // compute the declared CRS
        Filter original = query.getFilter();
        CoordinateReferenceSystem declaredCRS = getDeclaredSRS(source.getSchema());
        Filter reprojected = reprojectSpatialFilter(declaredCRS, source.getSchema(), original);
        if (reprojected == original) {
            return query;
        } else {
            Query rq = new Query(query);
            rq.setFilter(reprojected);
            return rq;
        }
    }

    /**
     * Reprojects spatial filters so that they match the feature source native CRS, and assuming all
     * literal geometries are specified in the specified declaredCRS
     */
    void reprojectSpatialFilters(
            LiteFeatureTypeStyle fts, CoordinateReferenceSystem declaredCRS, FeatureType schema) {
        for (int i = 0; i < fts.ruleList.length; i++) {
            fts.ruleList[i] = reprojectSpatialFilters(fts.ruleList[i], declaredCRS, schema);
        }
        if (fts.elseRules != null) {
            for (int i = 0; i < fts.elseRules.length; i++) {
                fts.elseRules[i] = reprojectSpatialFilters(fts.elseRules[i], declaredCRS, schema);
            }
        }
    }

    /**
     * Reprojects spatial filters so that they match the feature source native CRS, and assuming all
     * literal geometries are specified in the specified declaredCRS
     */
    private Rule reprojectSpatialFilters(
            Rule rule, CoordinateReferenceSystem declaredCRS, FeatureType schema) {
        // NPE avoidance
        Filter filter = rule.getFilter();
        if (filter == null) {
            return rule;
        }

        // try to reproject the filter
        Filter reprojected = reprojectSpatialFilter(declaredCRS, schema, filter);
        if (reprojected == filter) {
            return rule;
        }

        // clone the rule (the style can be reused over and over, we cannot alter it) and set the
        // new filter
        Rule rr = new RuleImpl(rule);
        rr.setFilter(reprojected);
        return rr;
    }

    /**
     * Reprojects spatial filters so that they match the feature source native CRS, and assuming all
     * literal geometries are specified in the specified declaredCRS
     */
    private Filter reprojectSpatialFilter(
            CoordinateReferenceSystem declaredCRS, FeatureType schema, Filter filter) {
        // NPE avoidance
        if (filter == null) {
            return null;
        }

        // do we have any spatial filter?
        SpatialFilterVisitor sfv = new SpatialFilterVisitor();
        filter.accept(sfv, null);
        if (!sfv.hasSpatialFilter()) {
            return filter;
        }

        // all right, we need to default the literals to the declaredCRS and then reproject to
        // the native one
        DefaultCRSFilterVisitor defaulter = new DefaultCRSFilterVisitor(filterFactory, declaredCRS);
        Filter defaulted = (Filter) filter.accept(defaulter, null);
        ReprojectingFilterVisitor reprojector =
                new ReprojectingFilterVisitor(filterFactory, schema);
        Filter reprojected = (Filter) defaulted.accept(reprojector, null);
        return reprojected;
    }

    /** Utility method to apply the two rescale visitors without duplicating code */
    void rescaleFeatureTypeStyle(LiteFeatureTypeStyle fts, DuplicatingStyleVisitor visitor) {
        for (int i = 0; i < fts.ruleList.length; i++) {
            visitor.visit(fts.ruleList[i]);
            fts.ruleList[i] = (Rule) visitor.getCopy();
        }
        if (fts.elseRules != null) {
            for (int i = 0; i < fts.elseRules.length; i++) {
                visitor.visit(fts.elseRules[i]);
                fts.elseRules[i] = (Rule) visitor.getCopy();
            }
        }
    }

    /**
     * Performs all rendering on the user provided graphics object by scanning the collection
     * multiple times, one for each feature type style provided
     */
    private void drawPlain(
            final Graphics2D graphics,
            String layerId,
            FeatureCollection<?, ?> features,
            final List<LiteFeatureTypeStyle> lfts) {

        // for each lite feature type style, scan the whole collection and draw
        for (LiteFeatureTypeStyle liteFeatureTypeStyle : lfts) {
            try (FeatureIterator<?> featureIterator = features.features()) {
                if (featureIterator == null) {
                    return; // nothing to do
                }
                boolean cloningRequired = isCloningRequired(lfts);
                RenderableFeature rf = createRenderableFeature(layerId, cloningRequired);
                ProjectionHandler handler =
                        checkForReprojection(features, rf, lfts, liteFeatureTypeStyle);
                // loop exit condition tested inside try catch
                // make sure we test hasNext() outside of the try/cath that follows, as that
                // one is there to make sure a single feature error does not ruin the rendering
                // (best effort) whilst an exception in hasNext() + ignoring catch results in
                // an infinite loop
                while (featureIterator.hasNext() && !renderingStopRequested) {
                    rf.setFeature(featureIterator.next());
                    processFeature(rf, liteFeatureTypeStyle, handler);
                }
            }

            if (liteFeatureTypeStyle.composite != null) {
                try {
                    requests.put(
                            new MergeLayersRequest(
                                    graphics, Collections.singletonList(liteFeatureTypeStyle)));
                } catch (InterruptedException e) {
                    fireErrorEvent(e);
                }
            }
        }
    }

    /**
     * Builds a new renderable feature for the given layerId and set of lite feature type styles
     *
     * @param cloningRequired TODO
     */
    RenderableFeature createRenderableFeature(String layerId, boolean cloningRequired) {
        RenderableFeature rf = new RenderableFeature(layerId, cloningRequired);
        return rf;
    }

    /**
     * Performs rendering so that the collection is scanned only once even in presence of multiple
     * feature type styles, using the in memory buffer for each feature type style other than the
     * first one (that uses the graphics provided by the user)s
     */
    private void drawOptimized(
            final Graphics2D graphics,
            String layerId,
            FeatureCollection features,
            final List<LiteFeatureTypeStyle> lfts) {

        try (FeatureIterator<?> iterator = features.features()) {
            if (iterator == null) return; // nothing to do

            boolean cloningRequired = isCloningRequired(lfts);
            RenderableFeature rf = createRenderableFeature(layerId, cloningRequired);
            // loop exit condition tested inside try catch
            // make sure we test hasNext() outside of the try/cath that follows, as that
            // one is there to make sure a single feature error does not ruin the rendering
            // (best effort) whilst an exception in hasNext() + ignoring catch results in
            // an infinite loop
            // the projection handlers and screen maps are calculated only for the first
            // feature in the collection and are cached for reuse by subsequent features
            boolean firstFeature = true;
            ProjectionHandler[] handlers = new ProjectionHandler[lfts.size()];
            ScreenMap[] screenMaps = new ScreenMap[lfts.size()];
            while (iterator.hasNext() && !renderingStopRequested) {
                rf.setFeature(iterator.next());
                // draw the feature on the main graphics and on the eventual extra image buffers
                for (int i = 0; i < lfts.size(); i++) {
                    LiteFeatureTypeStyle liteFeatureTypeStyle = lfts.get(i);
                    if (firstFeature) {
                        handlers[i] =
                                checkForReprojection(features, rf, lfts, liteFeatureTypeStyle);
                        screenMaps[i] = rf.screenMap;
                    } else {
                        rf.layer = liteFeatureTypeStyle.layer;
                        rf.setScreenMap(screenMaps[i]);
                    }
                    processFeature(rf, liteFeatureTypeStyle, handlers[i]);
                }
                firstFeature = false;
            }
            // submit the merge request
            requests.put(new MergeLayersRequest(graphics, lfts));
        } catch (InterruptedException e) {
            fireErrorEvent(e);
        }
    }

    private ProjectionHandler checkForReprojection(
            FeatureCollection features,
            RenderableFeature rf,
            List<LiteFeatureTypeStyle> lfts,
            LiteFeatureTypeStyle liteFeatureTypeStyle) {
        rf.layer = liteFeatureTypeStyle.layer;
        ProjectionHandler handler = liteFeatureTypeStyle.projectionHandler;
        // Check if a reprojection has been made, in that case, let's update the
        // projection handler
        CoordinateReferenceSystem featureCrs = features.getSchema().getCoordinateReferenceSystem();
        ScreenMap screenMap = liteFeatureTypeStyle.screenMap;
        if (handler != null
                && featureCrs != null
                && !CRS.equalsIgnoreMetadata(handler.getSourceCRS(), featureCrs)) {
            try {
                handler =
                        ProjectionHandlerFinder.getHandler(
                                mapExtent, featureCrs, isMapWrappingEnabled());
                if (screenMap != null) {
                    Envelope mapArea = mapExtent;
                    if (getRenderingBuffer() == 0) {
                        int metaBuffer = findRenderingBuffer(lfts);
                        if (metaBuffer > 0) {
                            mapArea = expandEnvelope(mapArea, worldToScreenTransform, metaBuffer);
                        }
                    }
                    ReferencedEnvelope envelope =
                            expandEnvelopeByTransformations(
                                    lfts, new ReferencedEnvelope(mapArea, destinationCrs));
                    envelope = new ReferencedEnvelope(envelope, destinationCrs);
                    SingleCRS crs2D = CRS.getHorizontalCRS(featureCrs);
                    MathTransform sourceToScreen =
                            buildFullTransform(crs2D, destinationCrs, worldToScreenTransform);
                    double[] spans =
                            getGeneralizationSpans(
                                    envelope,
                                    sourceToScreen,
                                    worldToScreenTransform,
                                    featureCrs,
                                    screenSize);
                    screenMap.setTransform(sourceToScreen);
                    screenMap.setSpans(spans[0], spans[1]);
                }
            } catch (FactoryException | TransformException e) {
                fireErrorEvent(e);
            }
        }
        rf.setScreenMap(screenMap);
        return handler;
    }

    /** Tells if geometry cloning is required or not */
    private boolean isCloningRequired(List<LiteFeatureTypeStyle> lfts) {
        // check if the features are detached, we can thus modify the geometries in place
        Layer layer = lfts.get(0).layer;

        final Set<Key> hints = layer.getFeatureSource().getSupportedHints();
        if (!hints.contains(Hints.FEATURE_DETACHED)) return true;

        // check if there is any conflicting geometry transformation.
        // No geometry transformations -> we can modify geometries in place
        // Just one geometry transformation over an attribute -> we can modify geometries in place
        // Two tx over the same attribute, or straight usage and a tx -> we have to preserve the
        // original geometry as well, thus we need cloning
        StyleAttributeExtractor extractor = new StyleAttributeExtractor();
        FeatureType featureType = layer.getFeatureSource().getSchema();
        Set<String> plainGeometries = new java.util.HashSet<>();
        Set<String> txGeometries = new java.util.HashSet<>();
        for (LiteFeatureTypeStyle lft : lfts) {
            for (Rule r : lft.ruleList) {
                for (Symbolizer s : r.symbolizers()) {
                    if (s.getGeometry() == null) {
                        String attribute =
                                featureType.getGeometryDescriptor().getName().getLocalPart();
                        if (txGeometries.contains(attribute)) return true;
                        plainGeometries.add(attribute);
                    } else if (s.getGeometry() instanceof PropertyName) {
                        String attribute = ((PropertyName) s.getGeometry()).getPropertyName();
                        if (txGeometries.contains(attribute)) return true;
                        plainGeometries.add(attribute);
                    } else {
                        Expression g = s.getGeometry();
                        extractor.clear();
                        g.accept(extractor, null);
                        Set<String> attributes = extractor.getAttributeNameSet();
                        for (String attribute : attributes) {
                            if (plainGeometries.contains(attribute)) return true;
                            if (txGeometries.contains(attribute)) return true;
                            txGeometries.add(attribute);
                        }
                    }
                }
            }
        }

        // check also that the rendered geometry is not used in any other filter or property of the
        // LFTS
        StyleAttributeExtractor extractorOther = new StyleAttributeExtractor();
        extractorOther.setSymbolizerGeometriesVisitEnabled(false);
        for (LiteFeatureTypeStyle lft : lfts) {
            for (Rule r : lft.ruleList) {
                if (r.getFilter() != null) {
                    r.getFilter().accept(extractorOther, null);
                }
                for (Symbolizer s : r.symbolizers()) {
                    s.accept(extractorOther);
                }
            }
        }
        Set<String> filterAndSymbolizerProperties =
                extractorOther.getAttributes().stream()
                        .map(pn -> pn.getPropertyName())
                        .collect(Collectors.toSet());
        if (extractorOther.getDefaultGeometryUsed()
                && featureType.getGeometryDescriptor() != null) {
            String defaultGeometryName =
                    featureType.getGeometryDescriptor().getName().getLocalPart();
            filterAndSymbolizerProperties.add(defaultGeometryName);
        }
        return !Collections.disjoint(filterAndSymbolizerProperties, plainGeometries)
                || !Collections.disjoint(filterAndSymbolizerProperties, txGeometries);
    }

    /** */
    void processFeature(
            RenderableFeature rf, LiteFeatureTypeStyle fts, ProjectionHandler projectionHandler) {
        try {
            // init the renderable feature for this fts
            rf.inMemoryGeneralization = fts.inMemoryGeneralization;
            rf.projectionHandler = projectionHandler;
            rf.setScreenMap(fts.screenMap);
            rf.layer = fts.layer;
            rf.metaBuffer = fts.metaBuffer;

            // can the rules
            boolean doElse = true;
            Rule[] elseRuleList = fts.elseRules;
            Rule[] ruleList = fts.ruleList;
            Rule r;
            Filter filter;
            Graphics2D graphics = fts.graphics;
            // applicable rules
            int paintCommands = 0;
            for (Rule value : ruleList) {
                r = value;
                filter = r.getFilter();

                if (filter == null || filter.evaluate(rf.feature)) {
                    doElse = false;
                    paintCommands += processSymbolizers(graphics, rf, r.symbolizers());

                    // bail out if we are in match first mode
                    if (fts.matchFirst) {
                        break;
                    }
                }
            }

            if (doElse) {
                for (Rule rule : elseRuleList) {
                    r = rule;

                    paintCommands += processSymbolizers(graphics, rf, r.symbolizers());
                }
            }

            // only emit a feature drawn event if we actually painted something with it,
            // if it has been clipped out or eliminated by the screenmap we won't emit the event
            // instead
            if (paintCommands > 0) {
                requests.put(new FeatureRenderedRequest(rf.feature));
            }

        } catch (Throwable tr) {
            fireErrorEvent(tr);
        }
    }

    /**
     * Applies each of a set of symbolizers in turn to a given feature.
     *
     * <p>This is an internal method and should only be called by processStylers.
     *
     * @param drawMe The feature to be rendered
     * @param symbolizers An array of symbolizers which actually perform the rendering. The scale
     *     range we are working on... provided in order to make the style factory happy
     */
    private int processSymbolizers(
            final Graphics2D graphics,
            final RenderableFeature drawMe,
            final List<Symbolizer> symbolizers)
            throws Exception {
        int paintCommands = 0;

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
                    final Object grid = gridPropertyName.evaluate(drawMe.feature);

                    if (grid instanceof GridCoverage2D) {
                        coverage = (GridCoverage2D) grid;
                        if (coverage != null) {
                            disposeCoverage = grid instanceof DisposableGridCoverage;
                            requests.put(
                                    new RenderRasterRequest(
                                            graphics,
                                            coverage,
                                            disposeCoverage,
                                            (RasterSymbolizer) symbolizer,
                                            destinationCrs,
                                            worldToScreenTransform));
                            paintCommands++;
                        }
                    } else if (grid instanceof GridCoverage2DReader) {
                        final GeneralParameterValue[] params =
                                (GeneralParameterValue[])
                                        paramsPropertyName.evaluate(drawMe.feature);
                        GridCoverage2DReader reader = (GridCoverage2DReader) grid;
                        requests.put(
                                new RenderCoverageReaderRequest(
                                        graphics,
                                        reader,
                                        params,
                                        (RasterSymbolizer) symbolizer,
                                        destinationCrs,
                                        worldToScreenTransform,
                                        getRenderingInterpolation(drawMe.layer)));
                    }
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    fireErrorEvent(e);
                }
            } else {

                // /////////////////////////////////////////////////////////////////
                //
                // FEATURE
                //
                // /////////////////////////////////////////////////////////////////
                LiteShape2 shape = drawMe.getShape(symbolizer, worldToScreenTransform);
                if (shape == null) {
                    continue;
                }

                if (symbolizer instanceof TextSymbolizer && drawMe.feature instanceof Feature) {
                    labelCache.put(
                            drawMe.layerId,
                            (TextSymbolizer) symbolizer,
                            drawMe.feature,
                            shape,
                            null);
                    paintCommands++;
                } else {
                    Style2D style = styleFactory.createStyle(drawMe.feature, symbolizer);

                    // clip to the visible area + the size of the symbolizer (with some extra
                    // to make sure we get no artifacts from polygon new borders)
                    double size = RendererUtilities.getStyle2DSize(style);
                    // take into account the meta buffer to try and clip all geometries by the same
                    // amount
                    double clipBuffer = Math.max(size / 2, drawMe.metaBuffer) + 10;
                    Envelope env =
                            new Envelope(
                                    screenSize.getMinX(),
                                    screenSize.getMaxX(),
                                    screenSize.getMinY(),
                                    screenSize.getMaxY());
                    env.expandBy(clipBuffer);
                    final GeometryClipper clipper = new GeometryClipper(env);
                    Geometry source = shape.getGeometry();
                    // we need to preserve the topology if we end up applying buffer for perp.
                    // offset
                    boolean preserveTopology =
                            style instanceof LineStyle2D
                                    && ((LineStyle2D) style).getPerpendicularOffset() != 0
                                    && (source instanceof Polygon
                                            || source instanceof MultiPolygon);

                    Geometry g = clipper.clipSafe(shape.getGeometry(), preserveTopology, 1);

                    // handle perpendincular offset as needed
                    if (style instanceof LineStyle2D
                            && ((LineStyle2D) style).getPerpendicularOffset() != 0
                            && g != null
                            && !g.isEmpty()) {
                        LineStyle2D ls = (LineStyle2D) style;
                        double offset = ls.getPerpendicularOffset();
                        // people applying an offset on a polygon really expect a buffer instead,
                        // do so... however buffering is damn expensive, so let's apply some
                        // heuristics
                        // to still run the offset curve builder for the simplest cases
                        if ((source instanceof Polygon || source instanceof MultiPolygon)
                                && abs(offset) > 3) {
                            // buffering is expensive, we can be a bit off with the
                            // result, do simplify the geometry first
                            Geometry simplified =
                                    TopologyPreservingSimplifier.simplify(
                                            source, Math.max(abs(offset) / 10, 1));
                            try {
                                g = simplified.buffer(offset);
                            } catch (Exception e) {
                                LOGGER.log(
                                        Level.FINE,
                                        "Failed to apply JTS buffer to the geometry, falling back on the offset curve builder",
                                        e);
                                OffsetCurveBuilder offseter = new OffsetCurveBuilder(offset);
                                g = offseter.offset(g);
                            }
                        } else {
                            OffsetCurveBuilder offseter = new OffsetCurveBuilder(offset);
                            g = offseter.offset(g);
                        }
                    } else if (style instanceof LineStyle2D) {
                        if (((LineStyle2D) style).getStroke() instanceof MarkAlongLine) {
                            double simplificationFactor =
                                    ((MarkAlongLine) ((LineStyle2D) style).getStroke())
                                            .getSimplificatorFactor();
                            if (simplificationFactor != 0) {
                                Decimator d =
                                        new Decimator(simplificationFactor, simplificationFactor);
                                g = source;
                                d.decimate(g);
                            }
                        }
                    }
                    if (g == null) {
                        continue;
                    } else {
                        shape = new LiteShape2(g, null, null, false);
                    }

                    PaintShapeRequest paintShapeRequest =
                            new PaintShapeRequest(graphics, shape, style, scaleDenominator);
                    if (symbolizer.hasOption("labelObstacle")) {
                        paintShapeRequest.setLabelObstacle(true);
                    }
                    Polygon clip = getClip(drawMe);
                    if (clip != null) {
                        LiteShape2 clipShape =
                                drawMe.getShape(null, worldToScreenTransform, clip, true);
                        paintShapeRequest.setClipShape(clipShape);
                    }
                    requests.put(paintShapeRequest);
                    paintCommands++;
                }
            }
        }

        return paintCommands;
    }

    private Polygon getClip(RenderableFeature drawMe) {
        if (drawMe.feature.hasUserData()) {
            Object clipCandidate = drawMe.feature.getUserData().get(Hints.GEOMETRY_CLIP);
            if (clipCandidate instanceof Polygon) {
                return (Polygon) clipCandidate;
            }
        }
        return null;
    }

    /**
     * Builds a raster grid geometry that will be used for reading, taking into account the original
     * map extent and target paint area, and expanding the target raster area by {@link
     * #REPROJECTION_RASTER_GUTTER}
     */
    GridGeometry2D getRasterGridGeometry(
            CoordinateReferenceSystem destinationCrs, CoordinateReferenceSystem sourceCRS)
            throws NoninvertibleTransformException {
        GridGeometry2D readGG;
        if (sourceCRS == null
                || destinationCrs == null
                || CRS.equalsIgnoreMetadata(destinationCrs, sourceCRS)) {
            readGG = new GridGeometry2D(new GridEnvelope2D(screenSize), originalMapExtent);
        } else {
            // reprojection involved, read a bit more pixels to account for rotation
            Rectangle bufferedTargetArea = (Rectangle) screenSize.clone();
            bufferedTargetArea.add( // exand top/right
                    screenSize.x + screenSize.width + REPROJECTION_RASTER_GUTTER,
                    screenSize.y + screenSize.height + REPROJECTION_RASTER_GUTTER);
            bufferedTargetArea.add( // exand bottom/left
                    screenSize.x - REPROJECTION_RASTER_GUTTER,
                    screenSize.y - REPROJECTION_RASTER_GUTTER);

            // now create the final envelope accordingly
            readGG =
                    new GridGeometry2D(
                            new GridEnvelope2D(bufferedTargetArea),
                            PixelInCell.CELL_CORNER,
                            new AffineTransform2D(worldToScreenTransform.createInverse()),
                            originalMapExtent.getCoordinateReferenceSystem(),
                            null);
        }
        return readGG;
    }

    /**
     * Finds the geometric attribute requested by the symbolizer
     *
     * @param drawMe The feature
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is
     *     specified
     */
    private org.locationtech.jts.geom.Geometry findGeometry(Object drawMe, Symbolizer s) {
        Expression geomExpr = s.getGeometry();

        // get the geometry
        Geometry geom;
        if (geomExpr == null) {
            if (drawMe instanceof SimpleFeature) {
                geom = (Geometry) ((SimpleFeature) drawMe).getDefaultGeometry();
            } else if (drawMe instanceof Feature) {
                geom = (Geometry) ((Feature) drawMe).getDefaultGeometryProperty().getValue();
            } else {
                geom = defaultGeometryPropertyName.evaluate(drawMe, Geometry.class);
            }
        } else {
            geom = geomExpr.evaluate(drawMe, Geometry.class);
        }

        return geom;
    }

    /**
     * Finds the geometric attribute coordinate reference system.
     *
     * @param f The feature
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is
     *     specified
     */
    private org.opengis.referencing.crs.CoordinateReferenceSystem findGeometryCS(
            Feature f, Symbolizer s) {
        if (s == null) {
            if (f != null) {
                return f.getType().getCoordinateReferenceSystem();
            } else {
                return null;
            }
        }
        FeatureType schema = f.getType();

        Expression geometry = s.getGeometry();

        if (geometry instanceof PropertyName) {
            return getAttributeCRS((PropertyName) geometry, schema);
        } else if (geometry == null) {
            return getAttributeCRS(null, schema);
        } else {
            StyleAttributeExtractor attExtractor = new StyleAttributeExtractor();
            geometry.accept(attExtractor, null);
            for (PropertyName name : attExtractor.getAttributes()) {
                if (name.evaluate(schema) instanceof GeometryDescriptor) {
                    return getAttributeCRS(name, schema);
                }
            }
        }

        return null;
    }

    /** Finds the CRS of the specified attribute (or uses the default geometry instead) */
    org.opengis.referencing.crs.CoordinateReferenceSystem getAttributeCRS(
            PropertyName geomName, FeatureType schema) {
        if (geomName == null || "".equals(geomName.getPropertyName())) {
            GeometryDescriptor geom = schema.getGeometryDescriptor();
            return geom.getType().getCoordinateReferenceSystem();
        } else {
            GeometryDescriptor geom = (GeometryDescriptor) geomName.evaluate(schema);
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
     * Sets the interactive status of the renderer. An interactive renderer won't wait for long
     * image loading, preferring an alternative mark instead
     *
     * @param interactive new value for the interactive property
     */
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * Returns the rendering buffer, a measure in pixels used to expand the geometry search area
     * enough to capture the geometries that do stay outside of the current rendering bounds but do
     * affect them because of their large strokes (labels and graphic symbols are handled
     * differently, see the label chache).
     */
    private int getRenderingBuffer() {
        if (rendererHints == null) return renderingBufferDEFAULT;
        Number result = (Number) rendererHints.get(RENDERING_BUFFER);
        if (result == null) return renderingBufferDEFAULT;
        return result.intValue();
    }

    /** Returns scale computation algorithm to be used. */
    private String getScaleComputationMethod() {
        if (rendererHints == null) return scaleComputationMethodDEFAULT;
        String result = (String) rendererHints.get("scaleComputationMethod");
        if (result == null) return scaleComputationMethodDEFAULT;
        return result;
    }

    /** Returns the text rendering method */
    private String getTextRenderingMethod() {
        if (rendererHints == null) return textRenderingModeDEFAULT;
        String result = (String) rendererHints.get(TEXT_RENDERING_KEY);
        if (result == null) return textRenderingModeDEFAULT;
        return result;
    }

    /** Returns the generalization distance in the screen space. */
    public double getGeneralizationDistance() {
        return generalizationDistance;
    }

    /**
     * Sets the generalizazion distance in the screen space.
     *
     * <p>Default value is 0.8, meaning that two subsequent points are collapsed to one if their on
     * screen distance is less than one pixel
     *
     * <p>Set the distance to 0 if you don't want any kind of generalization
     */
    public void setGeneralizationDistance(double d) {
        generalizationDistance = d;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.renderer.GTRenderer#setJava2DHints(java.awt.RenderingHints)
     */
    @Override
    public void setJava2DHints(RenderingHints hints) {
        this.java2dHints = hints;
        styleFactory.setRenderingHints(hints);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.renderer.GTRenderer#getJava2DHints()
     */
    @Override
    public RenderingHints getJava2DHints() {
        return java2dHints;
    }

    @Override
    public void setRendererHints(Map<?, ?> hints) {
        if (hints != null && hints.containsKey(LABEL_CACHE_KEY)) {
            LabelCache cache = (LabelCache) hints.get(LABEL_CACHE_KEY);
            if (cache == null)
                throw new NullPointerException(
                        "Label_Cache_Hint has a null value for the labelcache");

            this.labelCache = cache;
            painter = new StyledShapePainter(cache);
        }
        if (hints != null && hints.containsKey(LINE_WIDTH_OPTIMIZATION_KEY)) {
            styleFactory.setLineOptimizationEnabled(
                    Boolean.TRUE.equals(hints.get(LINE_WIDTH_OPTIMIZATION_KEY)));
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
    @Override
    @SuppressWarnings("unchecked")
    public Map<Object, Object> getRendererHints() {
        return (Map<Object, Object>) rendererHints;
    }

    @Override
    public void setMapContent(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    public boolean isCanTransform() {
        return canTransform;
    }

    public static MathTransform getMathTransform(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem destCRS) {
        try {
            return CRS.findMathTransform(sourceCRS, destCRS, true);
        } catch (OperationNotFoundException | FactoryException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return null;
    }

    Interpolation getRenderingInterpolation(Layer currLayer) {
        if (currLayer != null && currLayer.getUserData().containsKey(BYLAYER_INTERPOLATION)) {
            return (Interpolation) currLayer.getUserData().get(BYLAYER_INTERPOLATION);
        }
        if (java2dHints == null) {
            return Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        }
        Object interpolationHint = java2dHints.get(RenderingHints.KEY_INTERPOLATION);
        if (interpolationHint == null
                || interpolationHint == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
            return Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        } else if (interpolationHint == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
            return Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        } else {
            return Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
        }
    }

    /** A decimator that will just transform coordinates */
    private static final Decimator NULL_DECIMATOR = new Decimator(-1, -1);

    /** A class transforming (and caching) feature's geometries to shapes */
    class RenderableFeature {
        Feature feature;
        Layer layer;
        boolean inMemoryGeneralization;
        ProjectionHandler projectionHandler;
        int metaBuffer;
        private IdentityHashMap<Symbolizer, SymbolizerAssociation> symbolizerAssociationHT =
                new IdentityHashMap<>(); // associate a value
        private List<Geometry> geometries = new ArrayList<>();
        private List<Shape> shapes = new ArrayList<>();
        private boolean clone;
        private IdentityHashMap<MathTransform, Decimator> decimators = new IdentityHashMap<>();
        private ScreenMap screenMap;
        private String layerId;

        public RenderableFeature(String layerId, boolean clone) {
            this.layerId = layerId;
            this.clone = clone;
        }

        public void setScreenMap(ScreenMap screenMap) {
            this.screenMap = screenMap;
        }

        public void setFeature(Feature feature) {
            this.feature = feature;
            geometries.clear();
            shapes.clear();
        }

        public LiteShape2 getShape(Symbolizer symbolizer, AffineTransform at)
                throws FactoryException {
            Geometry g = findGeometry(feature, symbolizer); // pulls the geometry

            if (g == null || g.isEmpty()) return null;

            return getShape(symbolizer, at, g, clone);
        }

        public LiteShape2 getShape(
                Symbolizer symbolizer, AffineTransform at, Geometry g, boolean clone)
                throws FactoryException {
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
                    if (screenMap.canSimplify(env))
                        if (screenMap.checkAndSet(env)) {
                            return null;
                        } else {
                            g =
                                    screenMap.getSimplifiedShape(
                                            env.getMinX(),
                                            env.getMinY(),
                                            env.getMaxX(),
                                            env.getMaxY(),
                                            g.getFactory(),
                                            g.getClass());
                        }
                }

                SymbolizerAssociation sa = symbolizerAssociationHT.get(symbolizer);
                MathTransform crsTransform = null;
                MathTransform atTransform = null;
                MathTransform fullTransform = null;
                if (sa == null) {
                    sa = new SymbolizerAssociation();
                    sa.crs = (findGeometryCS(feature, symbolizer));
                    try {
                        crsTransform = buildTransform(sa.crs, destinationCrs);
                        atTransform = ProjectiveTransform.create(worldToScreenTransform);
                        fullTransform = buildFullTransform(sa.crs, destinationCrs, at);
                    } catch (Exception e) {
                        // fall through
                        LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    }
                    sa.xform = fullTransform;
                    sa.crsxform = crsTransform;
                    sa.axform = atTransform;
                    if (projectionHandler != null) {
                        sa.rxform = projectionHandler.getRenderingTransform(sa.crsxform);
                    } else {
                        sa.rxform = sa.crsxform;
                    }

                    symbolizerAssociationHT.put(symbolizer, sa);
                }

                // some shapes may be too close to projection boundaries to
                // get transformed, try to be lenient
                if (symbolizer instanceof PointSymbolizer) {
                    // if the coordinate transformation will occurr in place on the coordinate
                    // sequence
                    if (!clone
                            && g.getFactory().getCoordinateSequenceFactory()
                                    instanceof LiteCoordinateSequenceFactory) {
                        // if the symbolizer is a point symbolizer we first get the transformed
                        // geometry to make sure the coordinates have been modified once, and then
                        // compute the centroid in the screen space. This is a side effect of the
                        // fact we're modifing the geometry coordinates directly, if we don't get
                        // the reprojected and decimated geometry we risk of transforming it twice
                        // when computing the centroid
                        LiteShape2 first = getTransformedShape(g, sa, clone);
                        if (first != null) {
                            if (projectionHandler != null) {
                                // at the same time, we cannot keep the geometry in screen space
                                // because
                                // that would prevent the advanced projection handling to do its
                                // work,
                                // to replicate the geometries across the datelines, so we transform
                                // it back to the original
                                Geometry tx =
                                        JTS.transform(first.getGeometry(), sa.xform.inverse());
                                return getTransformedShape(
                                        RendererUtilities.getCentroid(tx), sa, clone);
                            } else {
                                return getTransformedShape(
                                        RendererUtilities.getCentroid(g), null, clone);
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return getTransformedShape(RendererUtilities.getCentroid(g), sa, clone);
                    }
                } else {
                    return getTransformedShape(g, sa, clone);
                }
            } catch (TransformException | AssertionError te) {
                LOGGER.log(Level.FINE, te.getLocalizedMessage(), te);
                fireErrorEvent(te);
                return null;
            }
        }

        private int getGeometryIndex(Geometry g) {
            for (int i = 0; i < geometries.size(); i++) {
                if (geometries.get(i) == g) {
                    return i;
                }
            }
            return -1;
        }

        private LiteShape2 getTransformedShape(
                Geometry originalGeom, SymbolizerAssociation sa, boolean clone)
                throws TransformException, FactoryException {
            int idx = getGeometryIndex(originalGeom);
            if (idx != -1) {
                return (LiteShape2) shapes.get(idx);
            }

            // we need to clone if the clone flag is high or if the coordinate sequence is not the
            // one we asked for
            Geometry geom = originalGeom;
            if (clone
                    || !(geom.getFactory().getCoordinateSequenceFactory()
                            instanceof LiteCoordinateSequenceFactory)) {
                int dim = sa.crs != null ? sa.crs.getCoordinateSystem().getDimension() : 2;
                geom = LiteCoordinateSequence.cloneGeometry(geom, dim);
            }

            LiteShape2 shape;
            if (projectionHandler != null && sa != null) {
                // first generalize and transform the geometry into the rendering CRS
                geom = projectionHandler.preProcess(geom);
                if (geom == null) {
                    shape = null;
                } else {
                    // first generalize and transform the geometry into the rendering CRS
                    Decimator d = getDecimator(sa.xform);
                    geom = d.decimateTransformGeneralize(geom, sa.rxform);
                    geom.geometryChanged();
                    // then post process it (provide reverse transform if available)
                    MathTransform reverse = null;
                    if (sa.crsxform != null) {
                        if (sa.crsxform instanceof ConcatenatedTransform
                                && ((ConcatenatedTransform) sa.crsxform)
                                                .transform1.getTargetDimensions()
                                        >= 3
                                && ((ConcatenatedTransform) sa.crsxform)
                                                .transform2.getTargetDimensions()
                                        == 2) {
                            reverse =
                                    null; // We are downcasting 3D data to 2D data so no inverse is
                            // available
                        } else {
                            try {
                                reverse = sa.crsxform.inverse();
                            } catch (Exception cannotReverse) {
                                reverse = null; // reverse transform not available
                            }
                        }
                    }
                    geom = projectionHandler.postProcess(reverse, geom);
                    if (geom == null) {
                        shape = null;
                    } else {
                        // apply the affine transform turning the coordinates into pixels
                        d = new Decimator(-1, -1);
                        geom = d.decimateTransformGeneralize(geom, sa.axform);

                        // wrap into a lite shape
                        geom.geometryChanged();
                        shape = new LiteShape2(geom, null, null, false, false);
                    }
                }
            } else {
                MathTransform xform = null;
                if (sa != null) xform = sa.xform;
                shape = new LiteShape2(geom, xform, getDecimator(xform), false, false);
            }

            // cache the result
            geometries.add(originalGeom);
            shapes.add(shape);
            return shape;
        }

        /** @throws org.opengis.referencing.operation.NoninvertibleTransformException */
        private Decimator getDecimator(MathTransform mathTransform) {
            // returns a decimator that does nothing if the currently set generalization
            // distance is zero (no generalization desired) or if the datastore has
            // already done full generalization at the desired level
            if (generalizationDistance == 0 || !inMemoryGeneralization) return NULL_DECIMATOR;

            Decimator decimator = decimators.get(mathTransform);
            if (decimator == null) {
                try {
                    if (mathTransform != null && !mathTransform.isIdentity())
                        decimator =
                                new Decimator(
                                        mathTransform.inverse(),
                                        screenSize,
                                        generalizationDistance);
                    else decimator = new Decimator(null, screenSize, generalizationDistance);
                } catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
                    decimator = new Decimator(null, screenSize, generalizationDistance);
                }

                decimators.put(mathTransform, decimator);
            }
            return decimator;
        }
    }

    /**
     * A request sent to the painting thread
     *
     * @author aaime
     */
    protected abstract class RenderingRequest {
        abstract void execute();
    }

    /**
     * A request to paint a shape with a specific Style2D
     *
     * @author aaime
     */
    protected class PaintShapeRequest extends RenderingRequest {
        Graphics2D graphic;

        LiteShape2 shape;

        Style2D style;

        double scale;

        boolean labelObstacle = false;

        Shape clipShape;

        public PaintShapeRequest(Shape clipShape) {
            this.clipShape = clipShape;
        }

        public PaintShapeRequest(
                Graphics2D graphic, LiteShape2 shape, Style2D style, double scale) {
            this.graphic = graphic;
            this.shape = shape;
            this.style = style;
            this.scale = scale;
        }

        public void setLabelObstacle(boolean labelObstacle) {
            this.labelObstacle = labelObstacle;
        }

        public void setClipShape(Shape clipShape) {
            this.clipShape = clipShape;
        }

        @Override
        void execute() {
            if (graphic instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphic).init();
            }

            try {
                Shape oldClip = null;
                if (clipShape != null) {
                    oldClip = graphic.getClip();
                    graphic.setClip(clipShape);
                }
                painter.paint(graphic, shape, style, scale, labelObstacle);
                if (clipShape != null) {
                    graphic.setClip(oldClip);
                }
            } catch (Throwable t) {
                fireErrorEvent(t);
            }
        }
    }

    /**
     * A request to paint a shape with a specific Style2D
     *
     * @author aaime
     */
    protected class FeatureRenderedRequest extends RenderingRequest {
        Object content;

        public FeatureRenderedRequest(Object content) {
            this.content = content;
        }

        @Override
        void execute() {
            fireFeatureRenderedEvent(content);
        }
    }

    /**
     * A request to merge multiple back buffers to the main graphics
     *
     * @author aaime
     */
    protected class MergeLayersRequest extends RenderingRequest {
        Graphics2D graphics;
        List<LiteFeatureTypeStyle> lfts;

        public MergeLayersRequest(Graphics2D graphics, List<LiteFeatureTypeStyle> lfts) {
            this.graphics = graphics;
            this.lfts = lfts;
        }

        @Override
        void execute() {
            if (graphics instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphics).init();
            }

            for (LiteFeatureTypeStyle currentLayer : lfts) {
                // first fts won't have an image, it's using the user provided graphics
                // straight, so we don't need to compose it back in.
                final Graphics2D ftsGraphics = currentLayer.graphics;
                if (ftsGraphics instanceof DelayedBackbufferGraphic && !(ftsGraphics == graphics)) {
                    BufferedImage image = ((DelayedBackbufferGraphic) ftsGraphics).image;
                    if (currentLayer.composite == null) {
                        graphics.setComposite(AlphaComposite.SrcOver);
                    } else {
                        // we may have not found anything to paint, in that case the delegate
                        // has not been initialized
                        if (image == null) {
                            Rectangle size = ((DelayedBackbufferGraphic) ftsGraphics).screenSize;
                            image =
                                    new BufferedImage(
                                            size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
                        }
                        graphics.setComposite(currentLayer.composite);
                    }
                    if (image != null) {
                        graphics.drawImage(image, 0, 0, null);
                        ftsGraphics.dispose();
                    }
                }
            }
        }
    }

    protected class MargeCompositingGroupRequest extends RenderingRequest {
        Graphics2D graphics;

        CompositingGroup compositingGroup;

        public MargeCompositingGroupRequest(
                Graphics2D graphics, CompositingGroup compositingGroup) {
            this.graphics = graphics;
            this.compositingGroup = compositingGroup;
        }

        @Override
        void execute() {
            if (graphics instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphics).init();
            }
            final BufferedImage image =
                    ((DelayedBackbufferGraphic) compositingGroup.graphics).image;
            // we may have not found anything to paint, in that case the delegate
            // has not been initialized
            if (image != null) {
                compositingGroup.graphics.dispose();
                Composite composite = compositingGroup.composite;
                if (composite == null) {
                    graphics.setComposite(AlphaComposite.SrcOver);
                } else {
                    graphics.setComposite(composite);
                }
                graphics.drawImage(image, 0, 0, null);
            }
        }
    }

    /**
     * A request to render a raster
     *
     * @author aaime
     */
    protected class RenderRasterRequest extends RenderingRequest {

        private Graphics2D graphics;
        private boolean disposeCoverage;
        private GridCoverage2D coverage;
        private RasterSymbolizer symbolizer;
        private CoordinateReferenceSystem destinationCRS;
        private AffineTransform worldToScreen;

        public RenderRasterRequest(
                Graphics2D graphics,
                GridCoverage2D coverage,
                boolean disposeCoverage,
                RasterSymbolizer symbolizer,
                CoordinateReferenceSystem destinationCRS,
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

            if (graphics instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphics).init();
            }

            try {
                // /////////////////////////////////////////////////////////////////
                //
                // If the grid object is a reader we ask him to do its best for the
                // requested resolution, if it is a gridcoverage instead we have to
                // rely on the gridocerage renderer itself.
                //
                // /////////////////////////////////////////////////////////////////
                final GridCoverageRenderer gcr =
                        new GridCoverageRenderer(
                                destinationCRS,
                                originalMapExtent,
                                screenSize,
                                worldToScreen,
                                java2dHints);

                try {
                    gcr.paint(graphics, coverage, symbolizer);
                } finally {
                    // we need to try and dispose this coverage if was created on purpose for
                    // rendering
                    if (coverage != null && disposeCoverage) {
                        coverage.dispose(true);
                        final RenderedImage image = coverage.getRenderedImage();
                        if (image instanceof PlanarImage) {
                            ImageUtilities.disposePlanarImageChain((PlanarImage) image);
                        }
                    }
                }

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Raster rendered");
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            }
        }
    }

    /**
     * A request to render a raster
     *
     * @author aaime
     */
    protected class RenderCoverageReaderRequest extends RenderingRequest {

        private Graphics2D graphics;

        private GridCoverage2DReader reader;

        private RasterSymbolizer symbolizer;

        private CoordinateReferenceSystem destinationCRS;

        private AffineTransform worldToScreen;

        private GeneralParameterValue[] readParams;

        private Interpolation interpolation;

        public RenderCoverageReaderRequest(
                Graphics2D graphics,
                GridCoverage2DReader reader,
                GeneralParameterValue[] readParams,
                RasterSymbolizer symbolizer,
                CoordinateReferenceSystem destinationCRS,
                AffineTransform worldToScreen,
                Interpolation interpolation) {
            this.graphics = graphics;
            this.reader = reader;
            this.readParams = readParams;
            this.symbolizer = symbolizer;
            this.destinationCRS = destinationCRS;
            this.worldToScreen = worldToScreen;
            this.interpolation = interpolation;
        }

        @Override
        void execute() {

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rendering reader " + reader);
            }

            if (graphics instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphics).init();
            }

            try {
                // /////////////////////////////////////////////////////////////////
                //
                // If the grid object is a reader we ask him to do its best for the
                // requested resolution, if it is a gridcoverage instead we have to
                // rely on the gridocerage renderer itself.
                //
                // /////////////////////////////////////////////////////////////////
                final GridCoverageRenderer gcr =
                        new GridCoverageRenderer(
                                destinationCRS,
                                originalMapExtent,
                                screenSize,
                                worldToScreen,
                                java2dHints);

                // Checks on the Reprojection parameters
                gcr.setAdvancedProjectionHandlingEnabled(isAdvancedProjectionHandlingEnabled());
                gcr.setWrapEnabled(isMapWrappingEnabled());
                gcr.paint(graphics, reader, readParams, symbolizer, interpolation, null);

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Raster rendered");
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            }
        }
    }

    protected class RenderDirectLayerRequest extends RenderingRequest {
        private final Graphics2D graphics;
        private final DirectLayer layer;

        public RenderDirectLayerRequest(Graphics2D graphics, DirectLayer layer) {
            this.graphics = graphics;
            this.layer = layer;
        }

        @Override
        void execute() {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Rendering DirectLayer: " + layer);
            }

            if (graphics instanceof DelayedBackbufferGraphic) {
                ((DelayedBackbufferGraphic) graphics).init();
            }

            try {
                layer.draw(graphics, mapContent, mapContent.getViewport());

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Layer rendered");
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                fireErrorEvent(e);
            }
        }
    }

    public class RenderTimeStatisticsRequest extends RenderingRequest {

        private List<RenderListener> listeners;

        private Layer currentLayer;

        public RenderTimeStatisticsRequest(List<RenderListener> listeners, Layer currentLayer) {
            this.listeners = listeners;
            this.currentLayer = currentLayer;
        }

        @Override
        void execute() {
            listeners.forEach(l -> l.layerEnd(currentLayer));
        }
    }

    /**
     * Marks the end of the request flow, instructs the painting thread to exit
     *
     * @author Andrea Aime - OpenGeo
     */
    protected class EndRequest extends RenderingRequest {

        @Override
        void execute() {
            // nothing to do here
        }
    }

    /**
     * The secondary thread that actually issues the paint requests against the graphic object
     *
     * @author aaime
     */
    class PainterThread implements Runnable {
        BlockingQueue<RenderingRequest> requests;
        Thread thread;

        public PainterThread(BlockingQueue<RenderingRequest> requests) {
            this.requests = requests;
        }

        public void interrupt() {
            if (thread != null) {
                thread.interrupt();
            }
        }

        @Override
        public void run() {
            thread = Thread.currentThread();
            boolean done = false;
            while (!done) {
                try {
                    List<RenderingRequest> localRequests = new ArrayList<>();
                    RenderingRequest request = requests.take();

                    requests.drainTo(localRequests);
                    localRequests.add(0, request);

                    for (RenderingRequest r : localRequests) {
                        if (r instanceof EndRequest || renderingStopRequested) {
                            done = true;
                            break;
                        } else {
                            r.execute();
                        }
                    }
                } catch (InterruptedException e) {
                    // ok, we might have been interrupted to stop processing
                    if (renderingStopRequested) {
                        done = true;
                    }
                } catch (Throwable t) {
                    fireErrorEvent(t);
                }
            }
        }
    }

    /**
     * A blocking queue subclass with a special behavior for the occasion when the rendering stop
     * has been requested: puts are getting ignored, and take always returns an EndRequest
     *
     * @author Andrea Aime - GeoSolutions
     */
    public class RenderingBlockingQueue implements BlockingQueue<RenderingRequest> {
        private static final long serialVersionUID = 4908029658595573833L;

        PushPullBlockingQueue<RenderingRequest> delegate;

        public RenderingBlockingQueue(int capacity) {
            this.delegate = new PushPullBlockingQueue<>(capacity, SpinPolicy.BLOCKING);
        }

        @Override
        public boolean add(RenderingRequest renderingRequest) {
            return delegate.add(renderingRequest);
        }

        @Override
        public boolean offer(RenderingRequest renderingRequest) {
            return delegate.offer(renderingRequest);
        }

        @Override
        public RenderingRequest remove() {
            return delegate.remove();
        }

        @Override
        public RenderingRequest poll() {
            return delegate.poll();
        }

        @Override
        public RenderingRequest element() {
            return delegate.element();
        }

        @Override
        public RenderingRequest peek() {
            return delegate.peek();
        }

        @Override
        public void put(RenderingRequest e) throws InterruptedException {
            if (!renderingStopRequested) {
                delegate.put(e);
                if (renderingStopRequested) {
                    this.clear();
                }
            }
        }

        @Override
        public boolean offer(RenderingRequest renderingRequest, long timeout, TimeUnit unit)
                throws InterruptedException {
            return delegate.offer(renderingRequest, timeout, unit);
        }

        @Override
        public RenderingRequest take() throws InterruptedException {
            if (!renderingStopRequested) {
                return delegate.take();
            } else {
                return new EndRequest();
            }
        }

        @Override
        public RenderingRequest poll(long timeout, TimeUnit unit) throws InterruptedException {
            return delegate.poll(timeout, unit);
        }

        @Override
        public int remainingCapacity() {
            return delegate.remainingCapacity();
        }

        @Override
        public boolean remove(Object o) {
            return delegate.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends RenderingRequest> c) {
            return delegate.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return delegate.removeAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super RenderingRequest> filter) {
            return delegate.removeIf(filter);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return delegate.retainAll(c);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public Spliterator<RenderingRequest> spliterator() {
            return delegate.spliterator();
        }

        @Override
        public Stream<RenderingRequest> stream() {
            return delegate.stream();
        }

        @Override
        public Stream<RenderingRequest> parallelStream() {
            return delegate.parallelStream();
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        @Override
        public Iterator<RenderingRequest> iterator() {
            return delegate.iterator();
        }

        @Override
        public Object[] toArray() {
            return delegate.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return delegate.toArray(a);
        }

        @Override
        public int drainTo(Collection<? super RenderingRequest> list) {
            if (!renderingStopRequested) {
                return delegate.drainTo(list);
            } else {
                list.clear();
                list.add(new EndRequest());
                return 1;
            }
        }

        @Override
        public int drainTo(Collection<? super RenderingRequest> c, int maxElements) {
            return delegate.drainTo(c, maxElements);
        }
    }

    private class GCRRenderingTransformationHelper extends RenderingTransformationHelper {

        private final Layer layer;

        public GCRRenderingTransformationHelper(Layer layer) {
            this.layer = layer;
        }

        @Override
        protected GridCoverage2D readCoverage(
                GridCoverage2DReader reader, Object readParams, GridGeometry2D readGG)
                throws IOException {
            Interpolation interpolation = getRenderingInterpolation(layer);
            RenderingHints interpolationHints =
                    new RenderingHints(JAI.KEY_INTERPOLATION, interpolation);
            final GridCoverageRenderer gcr;

            try {
                Rectangle mapRasterArea = readGG.getGridRange2D();
                final AffineTransform worldToScreen =
                        RendererUtilities.worldToScreenTransform(mapExtent, mapRasterArea);
                gcr =
                        new GridCoverageRenderer(
                                mapExtent.getCoordinateReferenceSystem(),
                                mapExtent,
                                mapRasterArea,
                                worldToScreen,
                                interpolationHints);
                gcr.setAdvancedProjectionHandlingEnabled(isAdvancedProjectionHandlingEnabled());
                gcr.setWrapEnabled(isMapWrappingEnabled());
                RenderedImage ri =
                        gcr.renderImage(
                                reader,
                                (GeneralParameterValue[]) readParams,
                                null,
                                interpolation,
                                null,
                                256,
                                256);
                if (ri != null) {
                    PlanarImage pi = PlanarImage.wrapRenderedImage(ri);
                    GridCoverage2D gc2d =
                            (GridCoverage2D)
                                    pi.getProperty(GridCoverageRenderer.PARENT_COVERAGE_PROPERTY);
                    return gc2d;
                }
                return null;
            } catch (TransformException | NoninvertibleTransformException | FactoryException e) {
                throw new IOException("Failure rendering the coverage", e);
            }
        }
    }
}
