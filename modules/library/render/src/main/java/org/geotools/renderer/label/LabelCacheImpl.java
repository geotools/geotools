/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.TextSymbolizer.GraphicPlacement;
import org.geotools.api.style.TextSymbolizer.PolygonAlignOptions;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.OffsetCurveBuilder;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.VendorOptionParser;
import org.geotools.renderer.label.LabelCacheItem.GraphicResize;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.util.NumberRange;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.locationtech.jts.operation.linemerge.LineMerger;

/**
 * Default LabelCache Implementation.
 *
 * <p>The label cache sports a number of features that are enabled depending on the programmatic configuration and the
 * TextSymbolizer options.
 *
 * <p>The basic functionality of the label cache consist in finding the best label position for each Feature according
 * to the {@link TextSymbolizer} specifications, and drawing it, provided it does not overlap with other labels.
 *
 * <p>This basic behaviour can be customised in a number of ways.
 *
 * <h2>Priority</h2>
 *
 * <p>{@link TextSymbolizer#getPriority()} OGC Expression controls a label priority.
 *
 * <p>A label with high priority will be drawn before others, increasing its likeliness to appear on the screen
 *
 * @author jeichar
 * @author dblasby
 * @author Andrea Aime - OpenGeo
 */
public class LabelCacheImpl implements LabelCache {

    static FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    static StyleFactory SF = CommonFactoryFinder.getStyleFactory();

    static final boolean DEBUG_CACHE_BOUNDS = Boolean.getBoolean("org.geotools.labelcache.showbounds");

    public static boolean DISABLE_LETTER_LEVEL_CONFLICT =
            Boolean.getBoolean("org.geotools.labelcache.disableLetterLevelConflict");

    public enum LabelRenderingMode {
        /**
         * Always uses {@link Graphics2D#drawGlyphVector(java.awt.font.GlyphVector, float, float)} to draw the straight
         * labels. It's faster, straight and horizontal labels look better, diagonal labels look worse, labels and halos
         * are not perfectly centered
         */
        STRING,
        /**
         * Always extracts the outline from the {@link GlyphVector} and paints it as a shape. It's a bit slower,
         * generates more antialiasing, ensures labels and halos are perfectly Centred
         */
        OUTLINE,
        /**
         * Draws all diagonal lines in OUTLINE model, but horizontal ones in STRING mode. Gives the best results when
         * coupled with {@link RenderingHints#VALUE_FRACTIONALMETRICS_ON} for good label/halo centering
         */
        ADAPTIVE
    }

    static final Logger LOGGER = Logging.getLogger(LabelCacheImpl.class);

    public double DEFAULT_PRIORITY = 1000.0;

    // list of displacement angles to be used for polys
    public static final int[] DEFAULT_DISPLACEMENT_ANGLES = {0, 45, 90, 135, 180, 225, 270, 315};

    /** The angle delta at which we switch from curved rendering to straight rendering */
    public static double MIN_CURVED_DELTA = Math.PI / 60;

    /** Used to locate grouped labels quickly */
    protected Map<LabelCacheItem, LabelCacheItem> groupedLabelsLookup = new HashMap<>();

    /** labels get thrown in here, in insertion order */
    protected ArrayList<LabelCacheItem> labelCache = new ArrayList<>();

    /** List of reserved areas of the screen for which labels should fear to tread */
    private List<Rectangle2D> reserved = new ArrayList<>();

    // Anchor candidate values used when looping to find a point label that can be drawn
    static final double[] RIGHT_ANCHOR_CANDIDATES = {0, 0.5, 0, 0, 0, 1};
    static final double[] MID_ANCHOR_CANDIDATES = {0.5, 0.5, 0, 0.5, 1, 0.5};
    static final double[] LEFT_ANCHOR_CANDIDATES = {1, 0.5, 1, 0, 1, 1};

    // Anchor candidate values used when looping to find a point label that can be drawn
    // applicable only in case displacementMode is set
    static final double[] RIGHT_UP_ANCHOR_CANDIDATES = {0, 0, 0, 0.5};
    static final double[] RIGHT_DOWN_ANCHOR_CANDIDATES = {0, 1, 0, 0.5};
    static final double[] VERTICAL_UP_ANCHOR_CANDIDATES = {0.5, 0.5, 0.5, 0.0};
    static final double[] VERTICAL_DOWN_ANCHOR_CANDIDATES = {0.5, 0.5, 0.5, 1};
    static final double[] HORIZONTAL_LEFT_ANCHOR_CANDIDATES = {0.5, 0.5, 1.0, 0.5};
    static final double[] HORIZONTAL_RIGHT_ANCHOR_CANDIDATES = {0.5, 0.5, 0, 0.5};
    static final double[] LEFT_UP_ANCHOR_CANDIDATES = {1, 0, 1, 0.5};
    static final double[] LEFT_DOWN_ANCHOR_CANDIDATES = {1, 1, 1, 0.5};

    protected LabelRenderingMode labelRenderingMode = LabelRenderingMode.STRING;

    // SLDStyleFactory caches by identity, we have to re-use the same labellels equivalent
    LoadingCache<TextSymbolizer, TextSymbolizer> labellessCache = CacheBuilder.newBuilder()
            .softValues()
            .build(new CacheLoader<>() {

                @Override
                public TextSymbolizer load(TextSymbolizer orig) throws Exception {
                    return labelless(orig);
                }
            });

    /**
     * Makes a shallow copy of the TextSymbolizer with the parts that are managed by the LabelItem anyways, making the
     * result easier to cache as "static" in SLDStyleFactory
     */
    private static TextSymbolizer labelless(TextSymbolizer orig) {
        TextSymbolizer copy = SF.createTextSymbolizer();
        copy.setDescription(orig.getDescription());
        copy.setFill(orig.getFill());
        if (orig.fonts() != null) copy.fonts().addAll(orig.fonts());
        copy.setGeometryPropertyName(orig.getGeometryPropertyName());
        copy.setHalo(orig.getHalo());
        // label skipped intentionally
        copy.setLabelPlacement(orig.getLabelPlacement());
        copy.setName(orig.getName());
        copy.setUnitOfMeasure(orig.getUnitOfMeasure());
        // priority skipped intentionally
        copy.setGraphic(orig.getGraphic());
        copy.setOtherText(orig.getOtherText());
        copy.setFeatureDescription(orig.getFeatureDescription());
        copy.setSnippet(orig.getSnippet());
        if (orig.getOptions() != null) copy.getOptions().putAll(orig.getOptions());

        return copy;
    }

    protected SLDStyleFactory styleFactory = new SLDStyleFactory();

    boolean stop = false;

    Set<String> enabledLayers = new HashSet<>();

    Set<String> activeLayers = new HashSet<>();

    LineLengthComparator lineLengthComparator = new LineLengthComparator();

    GeometryFactory gf = new GeometryFactory();

    GeometryClipper clipper;

    private boolean needsOrdering = false;

    private VendorOptionParser voParser = new VendorOptionParser();

    private List<RenderListener> renderListeners = new CopyOnWriteArrayList<>();

    private BiFunction<Graphics2D, LabelRenderingMode, LabelPainter> constructPainter = LabelPainter::new;

    @Override
    public void enableLayer(String layerId) {
        needsOrdering = true;
        enabledLayers.add(layerId);
    }

    public LabelRenderingMode getLabelRenderingMode() {
        return labelRenderingMode;
    }

    /** Sets the text rendering mode. */
    public void setLabelRenderingMode(LabelRenderingMode mode) {
        this.labelRenderingMode = mode;
    }

    /** Change the method used to construct LabelPainters. Defaults to {@link LabelPainter#LabelPainter}. */
    public void setConstructPainter(BiFunction<Graphics2D, LabelRenderingMode, LabelPainter> constructPainter) {
        this.constructPainter = constructPainter;
    }

    @Override
    public void stop() {
        stop = true;
        activeLayers.clear();
    }

    /** @see org.geotools.renderer.lite.LabelCache#start() */
    @Override
    public void start() {
        stop = false;
    }

    @Override
    public void clear() {
        if (!activeLayers.isEmpty()) {
            throw new IllegalStateException(activeLayers
                    + " are layers that started rendering but have not completed,"
                    + " stop() or endLayer() must be called before clear is called");
        }
        needsOrdering = true;
        labelCache.clear();
        groupedLabelsLookup.clear();
        enabledLayers.clear();
    }

    @Override
    public void clear(String layerId) {
        if (activeLayers.contains(layerId)) {
            throw new IllegalStateException(layerId + " is still rendering, end the layer before calling clear.");
        }
        needsOrdering = true;

        for (Iterator<LabelCacheItem> iter = labelCache.iterator(); iter.hasNext(); ) {
            LabelCacheItem item = iter.next();
            if (item.getLayerIds().contains(layerId)) {
                iter.remove();
                groupedLabelsLookup.remove(item);
            }
        }

        enabledLayers.remove(layerId);
    }

    @Override
    public void disableLayer(String layerId) {
        needsOrdering = true;
        enabledLayers.remove(layerId);
    }

    /** @see org.geotools.renderer.lite.LabelCache#startLayer(String) */
    @Override
    public void startLayer(String layerId) {
        enabledLayers.add(layerId);
        activeLayers.add(layerId);
    }

    /**
     * get the priority from the symbolizer its an expression, so it will try to evaluate it: 1. if its missing -->
     * DEFAULT_PRIORITY 2. if its a number, return that number 3. if its not a number, convert to string and try to
     * parse the number; return the number 4. otherwise, return DEFAULT_PRIORITY
     */
    public double getPriority(TextSymbolizer symbolizer, Feature feature) {
        if (symbolizer.getPriority() == null) return DEFAULT_PRIORITY;

        // evaluate
        try {
            Double number = symbolizer.getPriority().evaluate(feature, Double.class);
            return number.doubleValue();
        } catch (Exception e) {
            return DEFAULT_PRIORITY;
        }
    }

    /** @see org.geotools.renderer.lite.LabelCache#put(String,TextSymbolizer,Feature, LiteShape2,NumberRange) */
    @Override
    public void put(
            String layerId, TextSymbolizer symbolizer, Feature feature, LiteShape2 shape, NumberRange scaleRange) {
        needsOrdering = true;
        try {
            // get label and geometry
            if (symbolizer.getLabel() == null) {
                return;
            }

            String label = symbolizer.getLabel().evaluate(feature, String.class);

            if (label == null) return;

            if (label.length() == 0) {
                return; // dont label something with nothing!
            }
            double priorityValue = getPriority(symbolizer, feature);
            boolean group =
                    voParser.getBooleanOption(symbolizer, org.geotools.api.style.TextSymbolizer.GROUP_KEY, false);
            LabelCacheItem item =
                    buildLabelCacheItem(layerId, symbolizer, feature, shape, scaleRange, label, priorityValue);
            if (!group) {
                labelCache.add(item);
            } else { // / --------- grouping case ----------------
                // LabelCacheItem equals and hashcode work based on the label equality and the
                // TextSymbolizer identity
                LabelCacheItem groupItem = groupedLabelsLookup.get(item);
                if (groupItem == null) {
                    labelCache.add(item);
                    groupedLabelsLookup.put(item, item);
                } else {
                    // add to the priority only in the non-default case or non-literal. Ie.
                    // area()
                    Expression priority = symbolizer.getPriority();
                    if (priority != null && !(priority instanceof Literal)) {
                        groupItem.setPriority(groupItem.getPriority() + priorityValue);
                    }

                    groupItem.getGeoms().add(shape.getGeometry());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding label to the label cache", e);
        }
    }

    @Override
    public void put(Rectangle2D area) {
        reserved.add(area);
    }

    private LabelCacheItem buildLabelCacheItem(
            String layerId,
            TextSymbolizer symbolizer,
            Feature feature,
            LiteShape2 shape,
            NumberRange scaleRange,
            String label,
            double priorityValue) {
        TextStyle2D textStyle =
                (TextStyle2D) styleFactory.createStyle(feature, labellessCache.getUnchecked(symbolizer), scaleRange);

        LabelCacheItem item = new LabelCacheItem(layerId, textStyle, shape, label, symbolizer);
        item.setPriority(priorityValue);
        item.setSpaceAround(voParser.getIntOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.SPACE_AROUND_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_SPACE_AROUND));
        item.setMaxDisplacement(voParser.getIntOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.MAX_DISPLACEMENT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_MAX_DISPLACEMENT));
        item.setMinGroupDistance(voParser.getIntOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.MIN_GROUP_DISTANCE_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_MIN_GROUP_DISTANCE));
        item.setRepeat(voParser.getIntOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.LABEL_REPEAT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_LABEL_REPEAT));
        item.setLabelAllGroup(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.LABEL_ALL_GROUP_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_LABEL_ALL_GROUP));
        item.setRemoveGroupOverlaps(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.REMOVE_OVERLAPS_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_REMOVE_OVERLAPS));
        item.setAllowOverruns(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.ALLOW_OVERRUNS_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_ALLOW_OVERRUNS));
        item.setFollowLineEnabled(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.FOLLOW_LINE_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_FOLLOW_LINE));
        double maxAngleDelta = voParser.getDoubleOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.MAX_ANGLE_DELTA_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_MAX_ANGLE_DELTA);
        item.setMaxAngleDelta(Math.toRadians(maxAngleDelta));
        // follow line and write don't work toghether, disable it while we wait for a fix
        if (!item.isFollowLineEnabled()) {
            item.setAutoWrap(voParser.getIntOption(
                    symbolizer,
                    org.geotools.api.style.TextSymbolizer.AUTO_WRAP_KEY,
                    org.geotools.api.style.TextSymbolizer.DEFAULT_AUTO_WRAP));
        } else {
            // at fine level cause it would show up for every label with this setup
            LOGGER.log(Level.FINE, "Disabling auto-wrap, it's not supported along with followLine yet");
            item.setAutoWrap(0);
        }
        item.setForceLeftToRightEnabled(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.FORCE_LEFT_TO_RIGHT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_FORCE_LEFT_TO_RIGHT));
        item.setConflictResolutionEnabled(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.CONFLICT_RESOLUTION_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_CONFLICT_RESOLUTION));
        item.setGoodnessOfFit(voParser.getDoubleOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.GOODNESS_OF_FIT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_GOODNESS_OF_FIT));
        item.setPolygonAlign((PolygonAlignOptions) voParser.getEnumOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.POLYGONALIGN_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_POLYGONALIGN));
        item.setGraphicsResize(
                (GraphicResize) voParser.getEnumOption(symbolizer, "graphic-resize", GraphicResize.NONE));
        item.setGraphicMargin(voParser.getGraphicMargin(symbolizer, "graphic-margin"));
        item.setPartialsEnabled(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.PARTIALS_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_PARTIALS));
        item.setTextUnderlined(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.UNDERLINE_TEXT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_UNDERLINE_TEXT));
        item.setTextStrikethrough(voParser.getBooleanOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.STRIKETHROUGH_TEXT_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_STRIKETHROUGH_TEXT));
        item.setWordSpacing(voParser.getDoubleOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.WORD_SPACING_KEY,
                org.geotools.api.style.TextSymbolizer.DEFAULT_WORD_SPACING));
        item.setDisplacementAngles(voParser.getDisplacementAngles(
                symbolizer, org.geotools.api.style.TextSymbolizer.DISPLACEMENT_MODE_KEY));

        item.setFontShrinkSizeMin(voParser.getIntOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.FONT_SHRINK_SIZE_MIN,
                org.geotools.api.style.TextSymbolizer.DEFAULT_FONT_SHRINK_SIZE_MIN));
        item.setGraphicPlacement((GraphicPlacement) voParser.getEnumOption(
                symbolizer,
                org.geotools.api.style.TextSymbolizer.GRAPHIC_PLACEMENT_KEY,
                org.geotools.api.style.TextSymbolizer.GraphicPlacement.LABEL));
        return item;
    }

    /** @see org.geotools.renderer.lite.LabelCache#endLayer(String,Graphics2D,Rectangle) */
    @Override
    public void endLayer(String layerId, Graphics2D graphics, Rectangle displayArea) {
        activeLayers.remove(layerId);
    }

    /** Return a list with all the values in priority order. Both grouped and non-grouped */
    @Override
    public List<LabelCacheItem> orderedLabels() {
        List<LabelCacheItem> al = getActiveLabels();
        Collections.sort(al);
        Collections.reverse(al);
        return al;
    }

    /** Returns a list of all active labels */
    public List<LabelCacheItem> getActiveLabels() {
        // fill a list with the active labels
        List<LabelCacheItem> al = new ArrayList<>();
        for (LabelCacheItem item : labelCache) {
            if (isActive(item.getLayerIds())) al.add(item);
        }
        return al;
    }

    /** Is the label part of an active layer? */
    private boolean isActive(Set<String> layerIds) {
        for (String layerName : layerIds) {
            if (enabledLayers.contains(layerName)) return true;
        }
        return false;
    }

    /** @see org.geotools.renderer.lite.LabelCache#end(java.awt.Graphics2D, java.awt.Rectangle) */
    @Override
    public void end(Graphics2D graphics, Rectangle displayArea) {
        final Object antialiasing = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        final Object textAntialiasing = graphics.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        try {
            // if we are asked to antialias only text but we're drawing using
            // the outline
            // method, we need to re-enable graphics antialiasing during label
            // painting
            if (labelRenderingMode != LabelRenderingMode.STRING
                    && antialiasing == RenderingHints.VALUE_ANTIALIAS_OFF
                    && textAntialiasing == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            paintLabels(graphics, displayArea);
        } finally {
            if (antialiasing != null) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);
            }
        }
    }

    void paintLabels(Graphics2D graphics, Rectangle displayArea) {
        if (!activeLayers.isEmpty()) {
            throw new IllegalStateException(activeLayers
                    + " are layers that started rendering but have not completed,"
                    + " stop() or endLayer() must be called before end() is called");
        }
        LabelIndex glyphs = new LabelIndex();
        glyphs.reserveArea(reserved);

        // Used to check the paintLineLabel function
        int paintedLineLabels = 0;

        // Hack: let's reduce the display area width and height by one pixel.
        // If the rendered image is 256x256, proper rendering of polygons and
        // lines occurr only if the display area is [0,0; 256,256], yet if you
        // try to render anything at [x,256] or [256,y] it won't show.
        // So, to avoid labels that happen to touch the border being cut
        // by one pixel, we reduce the display area.
        // Feels hackish, don't have a better solution at the moment thought
        displayArea = new Rectangle(displayArea);
        displayArea.width -= 1;
        displayArea.height -= 1;

        // prepare the geometry clipper
        clipper = new GeometryClipper(new Envelope(
                displayArea.getMinX(), displayArea.getMaxX(), displayArea.getMinY(), displayArea.getMaxY()));

        List<LabelCacheItem> items; // both grouped and non-grouped
        if (needsOrdering) {
            items = orderedLabels();
        } else {
            items = getActiveLabels();
        }
        LabelPainter painter = constructPainter.apply(graphics, labelRenderingMode);
        for (LabelCacheItem labelItem : items) {
            if (stop) return;

            paintedLineLabels = paintLabel(graphics, displayArea, glyphs, paintedLineLabels, painter, labelItem);
        }
        // Output for line labels
        LOGGER.log(Level.FINE, "TOTAL LINE LABELS : {0}", items.size());
        LOGGER.log(Level.FINE, "PAINTED LINE LABELS : {0}", paintedLineLabels);
        LOGGER.log(Level.FINE, "REMAINING LINE LABELS : {0}", items.size() - paintedLineLabels);
    }

    int paintLabel(
            Graphics2D graphics,
            Rectangle displayArea,
            LabelIndex glyphs,
            int paintedLineLabels,
            LabelPainter painter,
            LabelCacheItem labelItem) {
        boolean painted;
        try {
            painter.setLabel(labelItem);
            // LabelCacheItem labelItem = (LabelCacheItem)
            // labelCache.get(labelIter.next());

            // DJB: simplified this. Just send off to the point,line,or
            // polygon routine
            // NOTE: labelItem.getGeometry() returns the FIRST geometry, so
            // we're assuming that lines & points arent mixed
            // If they are, then the FIRST geometry determines how its
            // rendered (which is probably bad since it should be in
            // area,line,point order
            // TOD: as in NOTE above

            /*
             * Just use identity for tempTransform because display area is 0,0,width,height and oldTransform may have a different origin. OldTransform
             * will be used later for drawing. -rg & je
             */
            AffineTransform tempTransform = new AffineTransform();

            Geometry geom = labelItem.getGeometry();
            if (geom instanceof Point || geom instanceof MultiPoint)
                paintPointLabel(painter, tempTransform, displayArea, glyphs);
            else if (geom instanceof LineString && !(geom instanceof LinearRing) || geom instanceof MultiLineString) {
                // letter conflict should be used if not disabled, and if there is any letter to
                // paint (not laying out a symbol with conflict resolution, with an empty label)
                if (!DISABLE_LETTER_LEVEL_CONFLICT
                        && !painter.getLabel().getLabel().trim().isEmpty())
                    painted = paintLineLabelsWithLetterConflict(painter, tempTransform, displayArea, glyphs);
                else painted = paintLineLabels(painter, tempTransform, displayArea, glyphs);
                if (painted) {
                    paintedLineLabels++;
                }
            } else if (geom instanceof Polygon || geom instanceof MultiPolygon || geom instanceof LinearRing)
                if (labelItem.getTextStyle().isPointPlacement() && !labelItem.isFollowLineEnabled()) {
                    // labelling the polygon centroid/label point
                    paintPolygonLabel(painter, tempTransform, displayArea, glyphs);
                } else {
                    // labelling the polygon border(s)
                    paintPolygonBorder(painter, tempTransform, displayArea, glyphs);
                }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Failure while painting labels", e);
            }
            for (RenderListener listener : renderListeners) {
                listener.errorOccurred(e);
            }
        }
        return paintedLineLabels;
    }

    private Envelope toEnvelope(Rectangle2D bounds) {
        return new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
    }

    /**
     * how well does the label "fit" with the geometry. 1. points ALWAYS RETURNS 1.0 2. lines ALWAYS RETURNS 1.0 (modify
     * polygon method to handle rotated labels) 3. polygon + assume: polylabels are unrotated + assume: polygon could be
     * invalid + dont worry about holes
     *
     * <p>like to RETURN area of intersection between polygon and label bounds, but thats expensive and likely to give
     * us problems due to invalid polygons SO, use a sample method - make a few points inside the label and see if
     * they're "close to" the polygon The method sucks, but works well...
     */
    private double goodnessOfFit(LabelPainter painter, AffineTransform transform, PreparedGeometry representativeGeom) {
        if (representativeGeom.getGeometry() instanceof Point) {
            return 1.0;
        }
        if (representativeGeom.getGeometry() instanceof LineString) {
            return 1.0;
        }
        if (representativeGeom.getGeometry() instanceof Polygon) {
            Rectangle2D glyphBounds = painter.getFullLabelBounds();
            try {
                // do a sampling, how many points sitting on the labels are also
                // within a certain distance of the polygon?
                int count = 0;
                int n = 10;
                Coordinate c = new Coordinate();
                Point pp = gf.createPoint(c);
                double[] gp = new double[2];
                double[] tp = new double[2];
                for (int i = 1; i < painter.getLineCount() + 1; i++) {
                    gp[1] = glyphBounds.getY() + glyphBounds.getHeight() * ((double) i / (painter.getLineCount() + 1));
                    for (int j = 1; j < n + 1; j++) {
                        gp[0] = glyphBounds.getX() + glyphBounds.getWidth() * ((double) j / (n + 1));
                        transform.transform(gp, 0, tp, 0, 1);
                        c.x = tp[0];
                        c.y = tp[1];
                        pp.geometryChanged();

                        // useful to debug the sampling point positions
                        // painter.graphics.setColor(Color.CYAN);
                        // painter.graphics.drawRect((int) (c.x - 1), (int) (c.y - 1), 2, 2);

                        if (representativeGeom.contains(pp)) {
                            count++;
                        }
                    }
                }
                return (double) count / (n * painter.getLineCount());
            } catch (Exception e) {
                Geometry g = representativeGeom.getGeometry();
                g.geometryChanged();
                Envelope ePoly = g.getEnvelopeInternal();
                Envelope eglyph =
                        toEnvelope(transform.createTransformedShape(glyphBounds).getBounds2D());
                Envelope inter = intersection(ePoly, eglyph);
                if (inter != null) {
                    return inter.getWidth() * inter.getHeight() / (eglyph.getWidth() * eglyph.getHeight());
                } else {
                    return 0.0;
                }
            }
        }
        return 0.0;
    }

    // Modified version of paintLineLabels
    // We compute the Bounding box for each letters instead of the whole label
    // then we check each letters for collisions
    private boolean paintLineLabelsWithLetterConflict(
            LabelPainter painter, AffineTransform originalTransform, Rectangle displayArea, LabelIndex paintedBounds)
            throws Exception {
        final LabelCacheItem labelItem = painter.getLabel();
        List<LineString> lines = getLineSetRepresentativeLocation(
                labelItem.getGeoms(), displayArea, labelItem.removeGroupOverlaps(), labelItem.isPartialsEnabled());

        if (lines == null || lines.isEmpty()) return false;

        // if we just want to label the longest line, remove the others
        if (!labelItem.labelAllGroup() && lines.size() > 1) {
            lines = Collections.singletonList(lines.get(0));
        }

        // pre compute some labelling params
        final Rectangle2D textBounds = painter.getFullLabelBounds();
        // ... use at least a 8 pixel step (curved processing is quite expensive), no matter what
        // the label length is
        final double step = painter.getLineHeight() > 8 ? painter.getLineHeight() : 8;
        // repetition distance, if any
        int labelDistance = labelItem.getRepeat();
        if (labelDistance > 0 && labelItem.isFollowLineEnabled()) {
            labelDistance += textBounds.getWidth();
        }
        // min distance, if any
        LabelIndex groupLabels = new LabelIndex();
        // Max displacement for the current label
        double labelOffset = labelItem.getMaxDisplacement();
        boolean allowOverruns = labelItem.allowOverruns();
        double maxAngleDelta = labelItem.getMaxAngleDelta();

        final int perpendicularOffset = painter.getLabel().getTextStyle().getPerpendicularOffset();
        OffsetCurveBuilder offsetBuilder = null;
        if (perpendicularOffset != 0) {
            offsetBuilder = new OffsetCurveBuilder(perpendicularOffset, 2);
        }
        int labelCount = 0;
        for (LineString line : lines) {
            // if we are following lines, use a simplified version of the line,
            // we don't want very small segments to influence the character
            // orientation
            if (labelItem.isFollowLineEnabled()) {
                line = decimateLineString(line, step);
                if (offsetBuilder != null) {
                    // offset the line and replace it
                    line = (LineString) offsetBuilder.offset(line);
                }
            }

            // max distance between candidate label points, if any
            final double lineStringLength = line.getLength();

            // if the line is too small compared to the label, don't label it
            // and exit right away, since the lines are sorted from longest to
            // shortest
            if ((!allowOverruns || labelItem.isFollowLineEnabled()) && line.getLength() < textBounds.getWidth()) {
                return labelCount > 0;
            }

            double[] labelPositions = buildLabelPositions(labelDistance, lineStringLength);

            // Ok, now we try to paint each of the labels in each position, and
            // we take into
            // account that we might have to displace the labels
            LineStringCursor cursor = new LineStringCursor(line);
            AffineTransform tx = new AffineTransform();
            boolean mightSkipLastLabel =
                    line.isClosed() && lineStringLength - (labelPositions.length - 1) * labelDistance < labelDistance;
            for (int i = 0; i < labelPositions.length; i++) {
                cursor.moveTo(labelPositions[i]);
                Coordinate centroid = cursor.getCurrentPosition();
                double currOffset = 0;

                // label displacement loop
                boolean painted = false;
                while (Math.abs(currOffset) <= labelOffset * 2 && !painted) {
                    // reset transform and other computation parameters
                    tx.setToIdentity();
                    double maxAngleChange = 0;
                    boolean curved = false;

                    // the line ordinates where we presume the label will start
                    // and end (using full bounds,
                    // thus taking into account shield and halo)
                    double startOrdinate = cursor.getCurrentOrdinate() - textBounds.getWidth() / 2;
                    double endOrdinate = cursor.getCurrentOrdinate() + textBounds.getWidth() / 2;

                    // compute label bounds
                    if (labelItem.followLineEnabled) {
                        // curved label, but we might end up drawing a straight
                        // one as an optimization
                        maxAngleChange = cursor.getMaxAngleChange(startOrdinate, endOrdinate);
                        setupLineTransform(painter, cursor, centroid, tx, true);
                        curved = maxAngleChange >= MIN_CURVED_DELTA;
                    } else {
                        setupLineTransform(painter, cursor, centroid, tx, false);
                    }

                    GlyphVectorProcessor glyphVectorProcessor = null;
                    // List<AffineTransform> transforms = null;
                    // For curved labels, we want the glyph's bounding box
                    // to follow the line
                    if (curved) {
                        LineStringCursor oldCursor = new LineStringCursor(cursor);
                        glyphVectorProcessor = new GlyphVectorProcessor.Curved(painter, oldCursor);
                    } else {
                        glyphVectorProcessor = new GlyphVectorProcessor.Straight(painter, tx);
                    }

                    // We check each letters for collision
                    boolean collision = glyphVectorProcessor.process(
                            new GlyphProcessor.ConflictDetector(painter, displayArea, paintedBounds, groupLabels),
                            true);

                    // If none of the glyphs intersects a bounding box,
                    // we paint the label
                    if (!collision) {
                        if (labelItem.isFollowLineEnabled()) {
                            // for curved labels we never paint in case of
                            // overrun
                            if (startOrdinate > 0 && endOrdinate <= cursor.getLineStringLength()) {
                                if (maxAngleChange <= maxAngleDelta) {
                                    // a max distance related to both the font size, but also having
                                    // a visual limit
                                    // of a couple of millimeters assuming 90dpi
                                    double maxDistance = Math.min(painter.getLineHeight() / 2, 7);
                                    // if straight segment connecting the start and end ordinate is
                                    // really close, paint as a straight label
                                    if (maxAngleChange == 0
                                            || cursor.getMaxDistanceFromStraightLine(startOrdinate, endOrdinate)
                                                    < maxDistance) {
                                        painter.paintStraightLabel(tx);
                                    } else {
                                        painter.paintCurvedLabel(cursor);
                                    }
                                    painted = true;
                                }
                            }
                        } else {
                            // for straight labels, check overrun only if
                            // required
                            if (allowOverruns || startOrdinate > 0 && endOrdinate <= cursor.getLineStringLength()) {
                                painter.paintStraightLabel(tx);
                                painted = true;
                            }
                        }
                    }

                    // if we actually painted the label, add the envelope to the
                    // indexes and break out of the loop,
                    // otherwise move to the next candidate position in the
                    // displacement sequence
                    if (painted) {
                        labelCount++;

                        if (labelItem.isConflictResolutionEnabled()) {
                            if (DEBUG_CACHE_BOUNDS) {
                                painter.graphics.setStroke(new BasicStroke());
                                painter.graphics.setColor(Color.RED);
                                glyphVectorProcessor.process(new GlyphProcessor.BoundsPainter(painter));
                            }

                            // Add each glyph's bounding box to the index
                            glyphVectorProcessor.process(new GlyphProcessor.IndexAdder(painter, paintedBounds));
                        }

                        // do not paint the last label on a ring if we have painted its symmetric
                        // one and the residual space at the ends does not guarantee labelDistance
                        // between last and second to last
                        if (i == labelPositions.length - 2 && painted && mightSkipLastLabel) {
                            i++;
                        }
                    } else {
                        // generate a sequence like s, -2s, 3s, -4s,...
                        // which will make the cursor alternate on
                        // mid + s, mid - s, mid + 2s, mid - 2s, mid + 3s,...
                        currOffset = nextOffset(currOffset, step);
                        cursor.moveRelative(currOffset);
                        cursor.getCurrentPosition(centroid);
                    }
                }
            }
        }
        return labelCount > 0;
    }

    /**
     * Compute next offset. It generates a sequence like s, -2s, 3s, -4s,... which will make the cursor alternate on mid
     * + s, mid - s, mid + 2s, mid - 2s, mid + 3s, ...
     *
     * @param currOffset last offset
     * @param step step between two positions
     * @return next offset
     */
    private double nextOffset(double currOffset, double step) {
        double signum = Math.signum(currOffset);
        if (signum == 0) {
            return step;
        } else {
            return -1 * signum * (Math.abs(currOffset) + step);
        }
    }

    private boolean paintLineLabels(
            LabelPainter painter, AffineTransform originalTransform, Rectangle displayArea, LabelIndex paintedBounds)
            throws Exception {
        final LabelCacheItem labelItem = painter.getLabel();
        List<LineString> lines = getLineSetRepresentativeLocation(
                labelItem.getGeoms(), displayArea, labelItem.removeGroupOverlaps(), labelItem.isPartialsEnabled());

        if (lines == null || lines.isEmpty()) return false;

        // if we just want to label the longest line, remove the others
        if (!labelItem.labelAllGroup() && lines.size() > 1) {
            lines = Collections.singletonList(lines.get(0));
        }

        // pre compute some labelling params
        final Rectangle2D textBounds = painter.getFullLabelBounds();
        // ... use at least a 2 pixel step, no matter what the label length is
        final double step = painter.getAscent() > 2 ? painter.getAscent() : 2;
        int space = labelItem.getSpaceAround();
        int haloRadius = Math.round(
                labelItem.getTextStyle().getHaloFill() != null
                        ? labelItem.getTextStyle().getHaloRadius()
                        : 0);
        int extraSpace = space + haloRadius;
        // repetition distance, if any
        int labelDistance = labelItem.getRepeat();
        // min distance, if any
        int minDistance = labelItem.getMinGroupDistance();
        LabelIndex groupLabels = new LabelIndex();
        // Max displacement for the current label
        double labelOffset = labelItem.getMaxDisplacement();
        boolean allowOverruns = labelItem.allowOverruns();
        double maxAngleDelta = labelItem.getMaxAngleDelta();

        int labelCount = 0;
        final int perpendicularOffset = painter.getLabel().getTextStyle().getPerpendicularOffset();
        OffsetCurveBuilder offsetBuilder = null;
        if (perpendicularOffset != 0) {
            offsetBuilder = new OffsetCurveBuilder(perpendicularOffset, 2);
        }
        for (LineString line : lines) {
            // if we are following lines, use a simplified version of the line,
            // we don't want very small segments to influence the character
            // orientation
            if (labelItem.isFollowLineEnabled()) {
                line = decimateLineString(line, step);

                if (offsetBuilder != null) {
                    // offset the line and replace it
                    line = (LineString) offsetBuilder.offset(line);
                }
            }

            // max distance between candidate label points, if any
            final double lineStringLength = line.getLength();

            // if the line is too small compared to the label, don't label it
            // and exit right away, since the lines are sorted from longest to
            // shortest
            if ((!allowOverruns || labelItem.isFollowLineEnabled()) && line.getLength() < textBounds.getWidth())
                return labelCount > 0;

            double[] labelPositions = buildLabelPositions(labelDistance, lineStringLength);

            // Ok, now we try to paint each of the labels in each position, and
            // we take into
            // account that we might have to displace the labels
            LineStringCursor cursor = new LineStringCursor(line);
            AffineTransform tx = new AffineTransform();
            // Checks if the second to last and last (symmetric to the center) labels are too close
            // in a linear ring to be painted both. Will verify if the second to last is painted
            // before
            // painting the last one (making sure only one is)
            boolean mightSkipLastLabel =
                    line.isClosed() && lineStringLength - (labelPositions.length - 1) * labelDistance < labelDistance;
            for (int i = 0; i < labelPositions.length; i++) {
                cursor.moveTo(labelPositions[i]);
                Coordinate centroid = cursor.getCurrentPosition();
                double currOffset = 0;

                // label displacement loop
                boolean painted = false;
                while (Math.abs(currOffset) <= labelOffset * 2 && !painted) {
                    // reset transform and other computation parameters
                    tx.setToIdentity();
                    Rectangle2D labelEnvelope;
                    double maxAngleChange = 0;

                    // the line ordinates where we presume the label will start
                    // and end (using full bounds,
                    // thus taking into account shield and halo)
                    double startOrdinate = cursor.getCurrentOrdinate() - textBounds.getWidth() / 2;
                    double endOrdinate = cursor.getCurrentOrdinate() + textBounds.getWidth() / 2;

                    // compute label bounds
                    if (labelItem.followLineEnabled) {
                        // curved label, but we might end up drawing a straight
                        // one as an optimization
                        maxAngleChange = cursor.getMaxAngleChange(startOrdinate, endOrdinate);
                        if (maxAngleChange < MIN_CURVED_DELTA) {
                            // if label will be painted as straight, use the
                            // straight bounds
                            setupLineTransform(painter, cursor, centroid, tx, true);
                            labelEnvelope =
                                    tx.createTransformedShape(textBounds).getBounds2D();
                        } else {
                            // otherwise use curved bounds, more expensive to
                            // compute
                            labelEnvelope = getCurvedLabelBounds(
                                    cursor, startOrdinate, endOrdinate, textBounds.getHeight() / 2);
                        }
                    } else {
                        setupLineTransform(painter, cursor, centroid, tx, false);
                        labelEnvelope = tx.createTransformedShape(textBounds).getBounds2D();
                    }

                    // try to paint the label, the condition under which this
                    // happens are complex
                    if ((displayArea.contains(labelEnvelope) || labelItem.isPartialsEnabled())
                            && !(labelItem.isConflictResolutionEnabled()
                                    && paintedBounds.labelsWithinDistance(labelEnvelope, extraSpace))
                            && !groupLabels.labelsWithinDistance(labelEnvelope, minDistance)) {
                        if (labelItem.isFollowLineEnabled()) {
                            // for curved labels we never paint in case of
                            // overrun
                            if (startOrdinate > 0
                                    && endOrdinate <= cursor.getLineStringLength()
                                    && maxAngleChange < maxAngleDelta) {
                                // if straight segment connecting the start and end ordinate is
                                // really close, paint as a straight label
                                if (maxAngleChange == 0
                                        || cursor.getMaxDistanceFromStraightLine(startOrdinate, endOrdinate)
                                                < painter.getLineHeight() / 2) {
                                    painter.paintStraightLabel(tx);
                                } else {
                                    painter.paintCurvedLabel(cursor);
                                }
                                painted = true;
                            }
                        } else {
                            // for straight labels, check overrun only if
                            // required
                            if (allowOverruns || startOrdinate > 0 && endOrdinate <= cursor.getLineStringLength()) {
                                painter.paintStraightLabel(tx);
                                painted = true;
                            }
                        }
                    }

                    // if we actually painted the label, add the envelope to the
                    // indexes and break out of the loop,
                    // otherwise move to the next candidate position in the
                    // displacement sequence
                    if (painted) {
                        labelCount++;
                        groupLabels.addLabel(labelItem, labelEnvelope);
                        if (labelItem.isConflictResolutionEnabled()) {
                            if (DEBUG_CACHE_BOUNDS) {
                                painter.graphics.setStroke(new BasicStroke());
                                painter.graphics.setColor(Color.RED);
                                painter.graphics.draw(labelEnvelope);
                            }
                            paintedBounds.addLabel(labelItem, labelEnvelope);
                        }

                        // do not paint the last label on a ring if we have painted its symmetric
                        // one and the residual space at the ends does not guarantee labelDistance
                        // between last and second to last
                        if (i == labelPositions.length - 2 && painted && mightSkipLastLabel) {
                            i++;
                        }
                    } else {
                        // this will generate a sequence like s, -2s, 3s, -4s,
                        // ...
                        // which will make the cursor alternate on mid + s, mid
                        // - s, mid + 2s, mid - 2s, mid + 3s, ...
                        double signum = Math.signum(currOffset);
                        if (signum == 0) {
                            currOffset = step;
                        } else {
                            currOffset = -1 * signum * (Math.abs(currOffset) + step);
                        }
                        cursor.moveRelative(currOffset);
                        cursor.getCurrentPosition(centroid);
                    }
                }
            }
        }

        return labelCount > 0;
    }

    private double[] buildLabelPositions(int labelDistance, final double lineStringLength) {
        // create the candidate positions for the labels over the line. If
        // we can place just one label or we're not supposed to replicate them, create the mid
        // position, otherwise create mid and then create the sequence of before and after labels
        double[] labelPositions;
        if (labelDistance > 0 && labelDistance < lineStringLength / 2) {
            // one label in the middle, plus all the labels we can fit before/after on the two half
            // lines
            final int positionCount = (int) (lineStringLength / 2 / labelDistance) * 2 + 1;
            labelPositions = new double[positionCount];
            labelPositions[0] = lineStringLength / 2;
            double offset = labelDistance;
            for (int i = 1; i < labelPositions.length; i++) {
                labelPositions[i] = labelPositions[i - 1] + offset;
                // this will generate a sequence like s, -2s, 3s, -4s, ...
                // which will make the cursor alternate on mid + s, mid - s,
                // mid + 2s, mid - 2s, mid + 3s, ...
                offset = nextOffset(offset, labelDistance);
            }
        } else {
            labelPositions = new double[1];
            labelPositions[0] = lineStringLength / 2;
        }

        return labelPositions;
    }

    private Rectangle2D getCurvedLabelBounds(
            LineStringCursor cursor, double startOrdinate, double endOrdinate, double bufferSize) {
        LineString cut = cursor.getSubLineString(startOrdinate, endOrdinate);
        Envelope e = cut.getEnvelopeInternal();
        e.expandBy(bufferSize);
        return new Rectangle2D.Double(e.getMinX(), e.getMinY(), e.getWidth(), e.getHeight());
    }

    private LineString decimateLineString(LineString line, double step) {
        // ideally this method should use Decimator, but due to
        // https://github.com/locationtech/jts/issues/254 that is not possible
        // LineString copy = (LineString) LiteCoordinateSequence.cloneGeometry(line,
        // line.getCoordinateSequence().getDimension());
        // Decimator decimator = new Decimator(step, step);
        //  decimator.decimate(copy);

        Coordinate[] inputCoordinates = line.getCoordinates();
        List<Coordinate> simplified = new ArrayList<>();
        // add first
        Coordinate prev = inputCoordinates[0];
        simplified.add(prev);
        for (int i = 1; i < inputCoordinates.length - 1; i++) {
            Coordinate curr = inputCoordinates[i];
            // see if this one should be added
            if (Math.abs(curr.x - prev.x) > step || Math.abs(curr.y - prev.y) > step) {
                simplified.add(curr);
                prev = curr;
            }
        }
        // special case for closed lines, and we have not accumulated at least 3 points so far
        // the check is just first = last and at least 4 points
        if (line instanceof LinearRing) {
            while (simplified.size() < 3) {
                simplified.add(prev);
            }
        }
        // add last
        simplified.add(inputCoordinates[inputCoordinates.length - 1]);

        Coordinate[] newCoords = simplified.toArray(new Coordinate[simplified.size()]);
        // preserve close-ness if it was there
        if (line instanceof LinearRing) {
            return line.getFactory().createLinearRing(newCoords);
        } else {
            return line.getFactory().createLineString(newCoords);
        }
    }

    /**
     * Sets up the transformation needed to position the label at the specified point, using the positioning information
     * loaded from the the text style
     */
    private void setupPointTransform(
            AffineTransform tempTransform, Point centroid, TextStyle2D textStyle, LabelPainter painter) {

        tempTransform.translate(centroid.getX(), centroid.getY());

        double rotation = textStyle.getRotation();
        if (Double.isNaN(rotation) || Double.isInfinite(rotation)) {
            // might legitimately happen if the rotation is computed out of an expression
            rotation = 0.0;
        }

        tempTransform.rotate(rotation);

        Rectangle2D textBounds = painter.getLabelBounds();
        // This now does "centering" taking into account the anchoring
        // and the real positioning of the text bounds (the bounds are placed
        // so that the baseline is in the origin, and the text goes up in
        // the negative coordinates)
        double displacementX = textStyle.getAnchorX() * -textBounds.getWidth() + textStyle.getDisplacementX();
        double displacementY = textStyle.getAnchorY() * textBounds.getHeight()
                - textStyle.getDisplacementY()
                - textBounds.getHeight()
                + (painter.lines.size() == 1
                        ? painter.getLineHeight()
                        : painter.getLineHeightForAnchorY(textStyle.getAnchorY()));
        tempTransform.translate(displacementX, displacementY);
    }

    /**
     * Sets up the transformation needed to position the label at the current location of the line string, using the
     * positioning information loaded from the text style
     */
    private void setupLineTransform(
            LabelPainter painter,
            LineStringCursor cursor,
            Coordinate centroid,
            AffineTransform tempTransform,
            boolean followLine) {
        tempTransform.translate(centroid.x, centroid.y);

        TextStyle2D textStyle = painter.getLabel().getTextStyle();
        double anchorX = textStyle.getAnchorX();
        double anchorY = textStyle.getAnchorY();

        // undo the above if its point placement!
        double rotation;
        double displacementY = 0;
        Rectangle2D textBounds = painter.getLabelBounds();
        if (textStyle.isPointPlacement() && !followLine) {
            // use the one the user supplied!
            rotation = textStyle.getRotation();
        } else { // lineplacement
            if (painter.getLabel().isForceLeftToRightEnabled()) {
                rotation = cursor.getLabelOrientation();
            } else {
                rotation = cursor.getCurrentAngle();
            }
            // move it off the line. If there is a follow line enabled, the perpendicular offset
            // has been baked into the geometry, so no need to account for it here
            final int perpendicularOffset = followLine ? 0 : textStyle.getPerpendicularOffset();
            displacementY -= perpendicularOffset
                    + (painter.getLineCount() - 1) * (textBounds.getHeight() / painter.getLineCount());
            anchorX = 0.5; // centered
            anchorY = painter.getLinePlacementYAnchor();
        }

        double displacementX = anchorX * -textBounds.getWidth() + textStyle.getDisplacementX();
        displacementY += anchorY * textBounds.getHeight() - textStyle.getDisplacementY();

        if (Double.isNaN(rotation) || Double.isInfinite(rotation)) rotation = 0.0;
        tempTransform.rotate(rotation);
        tempTransform.translate(displacementX, displacementY);
    }

    /**
     * Gets a representative point and tries to place the label according to SLD. If a maxDisplacement has been set and
     * the default position does not work a search for a better position is tried on concentric circles around the label
     * up until the radius of the circle becomes bigger than the max displacement
     */
    private boolean paintPointLabel(
            LabelPainter painter, AffineTransform tempTransform, Rectangle displayArea, LabelIndex glyphs)
            throws Exception {
        LabelCacheItem labelItem = painter.getLabel();
        // get the point onto the shape has to be painted
        Point point =
                getPointSetRepresentativeLocation(labelItem.getGeoms(), displayArea, labelItem.isPartialsEnabled());
        if (point == null) return false;

        // prepare for the search loop
        TextStyle2D ts = labelItem.getTextStyle();
        AffineTransform tx = new AffineTransform(tempTransform);

        // if straight paint works we're good
        if (paintPointLabelInternal(painter, tx, displayArea, glyphs, labelItem, point, ts)) {
            return true;
        }

        // see if we have a search radius
        if (labelItem.maxDisplacement <= 0) {
            return false;
        }

        int[] displacementAngles = labelItem.getDisplacementAngles();

        // ... use at least a 2 pixel step, no matter what the label length is
        final double step = painter.getAscent() > 2 ? painter.getAscent() : 2;
        double radius = Math.sqrt(
                ts.getDisplacementX() * ts.getDisplacementX() + ts.getDisplacementY() * ts.getDisplacementY());

        // get a cloned text style that we can modify without issues
        TextStyle2D cloned = new TextStyle2D(ts);
        // ... and the closest quadrant angle that we'll use to start the search from
        int startAngle = getClosestStandardAngle(ts.getDisplacementX(), ts.getDisplacementY());
        int angle = startAngle;
        while (radius <= labelItem.maxDisplacement) {
            // check for special vertical displacement mode
            if (displacementAngles == null) {
                // the offset is used to generate a x, -x, 2x, -2x, 3x, -3x sequence
                for (int offset = 45; offset <= 360; offset = offset + 45) {
                    double dx = radius * Math.cos(Math.toRadians(angle));
                    double dy = radius * Math.sin(Math.toRadians(angle));

                    // using dx and dy would be easy but due to numeric approximations,
                    // it's actually very hard to get it right so we use the angle
                    double[] anchorPointCandidates;
                    // normalize the angle so that it's between 0 and 360
                    int normAngle = angle % 360;
                    if (normAngle < 0) normAngle = 360 + normAngle;
                    if (normAngle < 90 || normAngle > 270) {
                        anchorPointCandidates = RIGHT_ANCHOR_CANDIDATES;
                    } else if (normAngle > 90 && normAngle < 270) {
                        anchorPointCandidates = LEFT_ANCHOR_CANDIDATES;
                    } else {
                        anchorPointCandidates = MID_ANCHOR_CANDIDATES;
                    }

                    // try out various anchor point positions
                    for (int i = 0; i < anchorPointCandidates.length; i += 2) {
                        double ax = anchorPointCandidates[i];
                        double ay = anchorPointCandidates[i + 1];
                        cloned.setAnchorX(ax);
                        cloned.setAnchorY(ay);
                        cloned.setDisplacementX(dx);
                        cloned.setDisplacementY(dy);

                        tx = new AffineTransform(tempTransform);
                        if (paintPointLabelInternal(painter, tx, displayArea, glyphs, labelItem, point, cloned))
                            return true;
                    }

                    // make sure we do the jumps back and forth to generate the proper sequence
                    if (angle <= startAngle) angle = angle + offset;
                    else angle = angle - offset;
                }
            } else {
                for (int offset : displacementAngles) {
                    angle = offset;
                    double dx = radius * Math.cos(Math.toRadians(angle));
                    double dy = radius * Math.sin(Math.toRadians(angle));

                    double[] anchorPointCandidates = {0.5, 0.5};
                    if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.NE.getAngle()) {
                        anchorPointCandidates = RIGHT_UP_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.SE.getAngle()) {
                        anchorPointCandidates = RIGHT_DOWN_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.N.getAngle()) {
                        anchorPointCandidates = VERTICAL_UP_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.S.getAngle()) {
                        anchorPointCandidates = VERTICAL_DOWN_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.NW.getAngle()) {
                        anchorPointCandidates = LEFT_UP_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.SW.getAngle()) {
                        anchorPointCandidates = LEFT_DOWN_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.E.getAngle()) {
                        anchorPointCandidates = HORIZONTAL_LEFT_ANCHOR_CANDIDATES;
                    } else if (angle == org.geotools.api.style.TextSymbolizer.DisplacementMode.W.getAngle()) {
                        anchorPointCandidates = HORIZONTAL_RIGHT_ANCHOR_CANDIDATES;
                    }

                    // try out various anchor point positions
                    for (int i = 0; i < anchorPointCandidates.length; i += 2) {
                        double ax = anchorPointCandidates[i];
                        double ay = anchorPointCandidates[i + 1];
                        cloned.setAnchorX(ax);
                        cloned.setAnchorY(ay);
                        cloned.setDisplacementX(dx);
                        cloned.setDisplacementY(dy);
                        tx = new AffineTransform(tempTransform);
                        if (paintPointLabelInternal(painter, tx, displayArea, glyphs, labelItem, point, cloned))
                            return true;
                    }
                }
            }

            // increase the radius and move forward
            radius += step;
        }

        // we tried, we failed...
        return false;
    }

    /**
     * Returns the closest angle that is a multiple of 45°
     *
     * @return an angle in degrees
     */
    int getClosestStandardAngle(double x, double y) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        return (int) Math.round(angle / 45.0) * 45;
    }

    /** Actually try to paint the label by setting up transformations, checking for conflicts and so on */
    private boolean paintPointLabelInternal(
            LabelPainter painter,
            AffineTransform tempTransform,
            Rectangle displayArea,
            LabelIndex glyphs,
            LabelCacheItem labelItem,
            Point point,
            TextStyle2D textStyle)
            throws Exception {
        setupPointTransform(tempTransform, point, textStyle, painter);

        // check for overlaps and paint
        Rectangle2D transformed = tempTransform
                .createTransformedShape(painter.getFullLabelBounds())
                .getBounds2D();
        if (!(displayArea.contains(transformed) || labelItem.isPartialsEnabled())
                || labelItem.isConflictResolutionEnabled()
                        && glyphs.labelsWithinDistance(transformed, labelItem.getSpaceAround())) {
            return false;
        } else {
            painter.paintStraightLabel(tempTransform, point.getCoordinate());
            if (DEBUG_CACHE_BOUNDS) {
                painter.graphics.setStroke(new BasicStroke());
                painter.graphics.setColor(Color.RED);
                painter.graphics.draw(transformed);
            }
            if (labelItem.isConflictResolutionEnabled()) glyphs.addLabel(labelItem, transformed);
            return true;
        }
    }

    /** Splits the polygon into its component lines and labels those, one by one */
    private boolean paintPolygonBorder(
            LabelPainter painter, AffineTransform tempTransform, Rectangle displayArea, LabelIndex glyphs)
            throws Exception {
        // turn the polygon in its component lines
        Geometry geometry = painter.getLabel().getGeometry();
        // for offset based labeling to make sense on polygons (controlling inside vs outside)
        // we need to normalize the geometry (it basically fixing the coordinate order of the rings,
        // shell clockwise, holes counterclockwise)
        if (painter.getLabel().getTextStyle().getPerpendicularOffset() != 0) {
            geometry.normalize();
        }
        List<LineString> lines = new ArrayList<>();
        geometry.apply((GeometryComponentFilter) g -> {
            if (g instanceof LineString) {
                lines.add((LineString) g);
            }
        });

        // loop over every line and draw labels on it
        boolean painted = false;
        LabelCacheItem item = painter.getLabel();
        LabelCacheItem itemCopy = new LabelCacheItem(item);
        for (LineString ls : lines) {
            itemCopy.geoms.clear();
            itemCopy.geoms.add(ls);
            painter.setLabel(itemCopy);
            if (!DISABLE_LETTER_LEVEL_CONFLICT) {
                painted |= paintLineLabelsWithLetterConflict(painter, tempTransform, displayArea, glyphs);
            } else {
                painted |= paintLineLabels(painter, tempTransform, displayArea, glyphs);
            }
        }

        return painted;
    }

    /**
     * returns the representative geometry (for further processing)
     *
     * <p>TODO: handle lineplacement for a polygon (perhaps we're supposed to grab the outside line and label it, but
     * spec is unclear)
     */
    private boolean paintPolygonLabel(
            LabelPainter painter, AffineTransform tempTransform, Rectangle displayArea, LabelIndex glyphs)
            throws Exception {
        LabelCacheItem labelItem = painter.getLabel();
        Polygon geom =
                getPolySetRepresentativeLocation(labelItem.getGeoms(), displayArea, labelItem.isPartialsEnabled());
        if (geom == null) {
            return false;
        }

        Point centroid = RendererUtilities.getPolygonCentroid(geom);
        if (centroid == null) {
            return false;
        }

        // check we're inside, if not, use a different approach
        PreparedGeometry pg = PreparedGeometryFactory.prepare(geom);
        if (!pg.contains(centroid)) {
            // resort to sampling, computing the intersection is slow and
            // due invalid geometries can easily break with an exception
            Point central = RendererUtilities.sampleForInternalPoint(geom, centroid, pg, gf, 5d, -1);
            if (central != null) {
                centroid = central;
            } else {
                return false;
            }
        }

        // compute the transformation used to position the label
        TextStyle2DExt textStyle = new TextStyle2DExt(labelItem);
        if (labelItem.getMaxDisplacement() > 0) {
            textStyle.setDisplacementX(0);
            textStyle.setDisplacementY(0);
            textStyle.setAnchorX(0.5);
            textStyle.setAnchorY(0.5);
        }

        AffineTransform tx = null;
        boolean allowShrinking = labelItem.getFontShrinkSizeMin()
                        > org.geotools.api.style.TextSymbolizer.DEFAULT_FONT_SHRINK_SIZE_MIN
                && labelItem.getFontShrinkSizeMin() < textStyle.getFont().getSize();
        int shrinkSize = allowShrinking
                ? labelItem.getFontShrinkSizeMin()
                : textStyle.getFont().getSize();
        int textSize = textStyle.getFont().getSize();
        // if shrinking is allowed then try to paint polygon label. If no success reduce font size
        // by 1 unit and retry until fontShrinkSize is reached.
        while (textSize >= shrinkSize) {
            tx = new AffineTransform(tempTransform);
            LabelCacheItem labelItem2 = painter.getLabel();
            TextStyle2DExt textStyle2 = new TextStyle2DExt(labelItem2);
            if (labelItem2.getMaxDisplacement() > 0) {
                textStyle2.setDisplacementX(0);
                textStyle2.setDisplacementY(0);
                textStyle2.setAnchorX(0.5);
                textStyle2.setAnchorY(0.5);
            }
            labelItem2.setTextStyle(textStyle2);
            painter.setLabel(labelItem2);
            if (paintPolygonLabelInternal(painter, tx, displayArea, glyphs, labelItem2, pg, centroid, textStyle2)) {
                return true;
            }
            textSize -= 1;
            if (allowShrinking) {
                Font font = new Font(
                        textStyle2.getFont().getName(), textStyle2.getFont().getStyle(), textSize);
                textStyle2.setFont(font);
            }
        }

        int[] displacementAngles = labelItem.getDisplacementAngles();
        if (displacementAngles == null) {
            displacementAngles = DEFAULT_DISPLACEMENT_ANGLES;
        }

        painter.setLabel(labelItem);

        // ... use at least a 2 pixel step, no matter what the label length is
        final double step = painter.getAscent() > 2 ? painter.getAscent() : 2;
        double radius = step;
        Coordinate c = new Coordinate(centroid.getCoordinate());
        Coordinate cc = centroid.getCoordinate();
        Point testPoint = centroid.getFactory().createPoint(c);
        while (radius < labelItem.getMaxDisplacement()) {
            for (int angle : displacementAngles) {
                double dx = Math.cos(Math.toRadians(angle)) * radius;
                double dy = Math.sin(Math.toRadians(angle)) * radius;

                c.x = cc.x + dx;
                c.y = cc.y + dy;
                testPoint.geometryChanged();
                if (!pg.contains(testPoint)) continue;

                textStyle.setDisplacementX(dx);
                textStyle.setDisplacementY(dy);

                tx = new AffineTransform(tempTransform);
                if (paintPolygonLabelInternal(painter, tx, displayArea, glyphs, labelItem, pg, centroid, textStyle))
                    return true;
            }

            radius += step;
        }

        return false;
    }

    private boolean paintPolygonLabelInternal(
            LabelPainter painter,
            AffineTransform tempTransform,
            Rectangle displayArea,
            LabelIndex glyphs,
            LabelCacheItem labelItem,
            PreparedGeometry pg,
            Point centroid,
            TextStyle2DExt textStyle)
            throws Exception {
        // useful to debug the label/centroid relationship
        // painter.graphics.setColor(Color.RED);
        // painter.graphics.drawRect((int)(centroid.getX() - 2), (int) (centroid.getY() - 2), 2, 2);

        AffineTransform original = new AffineTransform(tempTransform);
        setupPointTransform(tempTransform, centroid, textStyle, painter);

        Rectangle2D transformed = tempTransform
                .createTransformedShape(painter.getFullLabelBounds())
                .getBounds2D();
        if (!(displayArea.contains(transformed) || labelItem.isPartialsEnabled())
                || labelItem.isConflictResolutionEnabled()
                        && glyphs.labelsWithinDistance(transformed, labelItem.getSpaceAround())
                || goodnessOfFit(painter, tempTransform, pg)
                        < painter.getLabel().getGoodnessOfFit()) {
            // try the alternate rotation if possible
            if (textStyle.flipRotation(pg.getGeometry())) {
                tempTransform.setTransform(original);
                setupPointTransform(tempTransform, centroid, textStyle, painter);

                transformed = tempTransform
                        .createTransformedShape(painter.getFullLabelBounds())
                        .getBounds2D();
                if (!(displayArea.contains(transformed) || labelItem.isPartialsEnabled())
                        || labelItem.isConflictResolutionEnabled()
                                && glyphs.labelsWithinDistance(transformed, labelItem.getSpaceAround())
                        || goodnessOfFit(painter, tempTransform, pg)
                                < painter.getLabel().getGoodnessOfFit()) {
                    textStyle.flipRotation(pg.getGeometry());
                    return false;
                }
            } else {
                return false;
            }
        }

        if (DEBUG_CACHE_BOUNDS) {
            painter.graphics.setStroke(new BasicStroke());
            painter.graphics.setColor(Color.RED);
            painter.graphics.draw(transformed);
        }
        painter.paintStraightLabel(tempTransform);
        if (labelItem.isConflictResolutionEnabled()) {
            glyphs.addLabel(labelItem, transformed);
        }
        return true;
    }

    Geometry widestGeometry(Geometry geometry) {
        if (!(geometry instanceof GeometryCollection)) {
            return geometry;
        }
        return widestGeometry((GeometryCollection) geometry);
    }

    Geometry widestGeometry(GeometryCollection gc) {
        if (gc.isEmpty()) {
            return gc;
        }

        Geometry widest = gc.getGeometryN(0);
        for (int i = 1; i < gc.getNumGeometries(); i++) {
            Geometry curr = gc.getGeometryN(i);
            if (curr.getEnvelopeInternal().getWidth()
                    > widest.getEnvelopeInternal().getWidth()) {
                widest = curr;
            }
        }
        return widest;
    }

    /**
     * 1. get a list of points from the input geometries that are inside the displayGeom NOTE: lines and polygons are
     * reduced to their centroids (you shouldnt really calling this with lines and polys) 2. choose the most "central"
     * of the points METRIC - choose anyone TODO: change metric to be "closest to the centoid of the possible points"
     *
     * @param geoms list of Point or MultiPoint (any other geometry types are rejected
     * @param partialsEnabled true if we don't want to exclude points out of the displayArea
     * @return a point or null (if there's nothing to draw)
     */
    Point getPointSetRepresentativeLocation(List<Geometry> geoms, Rectangle displayArea, boolean partialsEnabled) {
        // points that are inside the displayGeometry
        ArrayList<Point> pts = new ArrayList<>();

        for (Geometry g : geoms) {
            if (!(g instanceof Point || g instanceof MultiPoint)) // handle
                // lines,polys, gc, etc..
                g = g.getCentroid(); // will be point
            if (g instanceof Point) {
                Point point = (Point) g;
                if (displayArea.contains(point.getX(), point.getY()) || partialsEnabled) // this is robust!
                pts.add(point); // possible label location
            } else if (g instanceof MultiPoint) {
                for (int t = 0; t < g.getNumGeometries(); t++) {
                    Point gg = (Point) g.getGeometryN(t);
                    if (displayArea.contains(gg.getX(), gg.getY()) || partialsEnabled)
                        pts.add(gg); // possible label location
                }
            }
        }
        if (pts.isEmpty()) return null;

        // do better metric than this:
        return pts.get(0);
    }

    /**
     * 1. make a list of all the geoms (not clipped) NOTE: reject points, convert polygons to their exterior ring (you
     * shouldnt be calling this function with points and polys) 2. join the lines together 3. clip resulting lines to
     * display geometry 4. return longest line
     *
     * <p>NOTE: the joining has multiple solution. For example, consider a Y (3 lines): * * 1 2 * * * 3 * solutions are:
     * 1->2 and 3 1->3 and 2 2->3 and 1
     *
     * <p>(see mergeLines() below for detail of the algorithm; its basically a greedy algorithm that should form the
     * 'longest' possible route through the linework)
     *
     * <p>NOTE: we clip after joining because there could be connections "going on" outside the display bbox
     *
     * @param displayArea must be poly
     * @param partialsEnabled true if we don't want to clip lines on the displayArea
     */
    List<LineString> getLineSetRepresentativeLocation(
            List<Geometry> geoms, Rectangle displayArea, boolean removeOverlaps, boolean partialsEnabled) {

        // go through each geometry in the set.
        // if its a polygon or multipolygon, get the boundary (reduce to a line)
        // if its a line, add it to "lines"
        // if its a multiline, add each component line to "lines"
        List<LineString> lines = new ArrayList<>();
        for (Geometry g : geoms) {
            accumulateLineStrings(g, lines);
        }
        if (lines.isEmpty()) return null;

        // clip all the lines to the current bounds
        List<LineString> clippedLines = new ArrayList<>();
        for (LineString ls : lines) {
            // If we want labels to be entirely in the display area, clip the linestring
            if (!partialsEnabled) {
                // more robust clipper -- see its dox
                MultiLineString ll = clipLineString(ls);
                if (ll != null && !ll.isEmpty()) {
                    for (int t = 0; t < ll.getNumGeometries(); t++) clippedLines.add((LineString) ll.getGeometryN(t));
                }
            }
            // If we want to draw partial labels on border, keep the whole linestring
            else {
                clippedLines.add(ls);
            }
        }

        if (removeOverlaps) {
            List<LineString> cleanedLines = new ArrayList<>();
            List<Geometry> bufferCache = new ArrayList<>();
            for (LineString ls : clippedLines) {
                Geometry g = ls;
                for (int i = 0; i < cleanedLines.size(); i++) {
                    LineString cleaned = cleanedLines.get(i);
                    if (g.getEnvelopeInternal().intersects(cleaned.getEnvelopeInternal())) {
                        Geometry buffer = bufferCache.get(i);
                        if (buffer == null) {
                            buffer = cleaned.buffer(2);
                            bufferCache.set(i, buffer);
                        }
                        g = g.difference(buffer);
                    }
                }
                int added = accumulateLineStrings(g, cleanedLines);
                for (int i = 0; i < added; i++) {
                    bufferCache.add(null);
                }
            }
            clippedLines = cleanedLines;
        }

        if (clippedLines == null || clippedLines.isEmpty()) return null;

        // at this point "lines" now is a list of linestring
        // join this algo doesnt always do what you want it to do, but its
        // pretty good
        List<LineString> merged = mergeLines(clippedLines);

        // clippedLines is a list of LineString, all clipped (hopefully) to the
        // display geometry. we choose longest one
        if (merged.isEmpty()) return null;

        // sort have the longest lines first
        Collections.sort(merged, new LineLengthComparator());
        return merged;
    }

    private int accumulateLineStrings(Geometry g, List<LineString> lines) {
        if (!(g instanceof LineString
                || g instanceof MultiLineString
                || g instanceof Polygon
                || g instanceof MultiPolygon)) return 0;

        // reduce polygons to their boundaries
        if (g instanceof Polygon || g instanceof MultiPolygon) {
            g = g.getBoundary(); // line or multiline m
            // TODO: boundary included the inside rings, might want to
            // replace this with getExteriorRing()
            if (!(g instanceof LineString || g instanceof MultiLineString)) return 0;
        }

        // deal with line and multi line string, and finally with geom
        // collection
        if (g instanceof LineString) {
            if (g.getLength() != 0) {
                lines.add((LineString) g);
                return 1;
            } else {
                return 0;
            }
        } else if (g instanceof MultiLineString) { // multiline
            for (int t = 0; t < g.getNumGeometries(); t++) {
                LineString gg = (LineString) g.getGeometryN(t);
                lines.add(gg);
            }
            return g.getNumGeometries();
        } else {
            int count = 0;
            for (int t = 0; t < g.getNumGeometries(); t++) {
                count += accumulateLineStrings(g.getGeometryN(t), lines);
            }
            return count;
        }
    }

    /**
     * try to be more robust dont bother returning points
     *
     * <p>This will try to solve robustness problems, but read code as to what it does. It might return the unclipped
     * line if there's a problem!
     */
    public MultiLineString clipLineString(LineString line) {

        Geometry clip = line;
        // djb -- jessie should do this during generalization
        line.geometryChanged();
        if (clipper.getBounds().contains(line.getEnvelopeInternal())) {
            // shortcut -- entirely inside the display rectangle -- no clipping
            // required!
            LineString[] lns = new LineString[1];
            lns[0] = (LineString) clip;
            return line.getFactory().createMultiLineString(lns);
        }
        try {
            Geometry g = clipper.clip(line, false);
            if (g == null) {
                return null;
            } else if (g instanceof LineString) {
                return line.getFactory().createMultiLineString(new LineString[] {(LineString) g});
            } else {
                return (MultiLineString) g;
            }
        } catch (Exception e) {
            // TODO: should try to expand the bounding box and re-do the
            // intersection, but line-bounding box
            // problems are quite rare.
            return line.getFactory().createMultiLineString(new LineString[] {line});
        }
    }

    /**
     * 1. make a list of all the polygons clipped to the displayGeometry NOTE: reject any points or lines 2. choose the
     * largest of the clipped geometries
     *
     * @param partialsEnabled true if we don't want to clip lines on the displayArea
     */
    Polygon getPolySetRepresentativeLocation(List<Geometry> geoms, Rectangle displayArea, boolean partialsEnabled) {
        List<Polygon> polys = new ArrayList<>(); // points that are
        // inside the
        Geometry displayGeometry = gf.toGeometry(toEnvelope(displayArea));

        // go through each geometry in the input set
        // if its not a polygon or multipolygon ignore it
        // if its a polygon, add it to "polys"
        // if its a multipolgon, add each component to "polys"
        for (Geometry g : geoms) {
            if (!(g instanceof Polygon || g instanceof MultiPolygon)) continue;

            if (g instanceof Polygon) {
                polys.add((Polygon) g);
            } else {
                // multipoly
                for (int t = 0; t < g.getNumGeometries(); t++) {
                    Polygon gg = (Polygon) g.getGeometryN(t);
                    polys.add(gg);
                }
            }
        }
        if (polys.isEmpty()) return null;

        // at this point "polys" is a list of polygons. Clip them
        List<Polygon> clippedPolys = new ArrayList<>();
        Envelope displayGeomEnv = displayGeometry.getEnvelopeInternal();
        for (Polygon p : polys) {
            // If we want labels to be entirely in the display area, clip polygons
            if (!partialsEnabled) {
                MultiPolygon pp = clipPolygon(p, (Polygon) displayGeometry, displayGeomEnv);
                if (pp != null && !pp.isEmpty()) {
                    for (int t = 0; t < pp.getNumGeometries(); t++) clippedPolys.add((Polygon) pp.getGeometryN(t));
                }
            }
            // If we want to draw partial labels on border, keep the whole polygon
            else {
                clippedPolys.add(p);
            }
        }

        // clippedPolys is a list of Polygon, all cliped (hopefully) to the
        // display geometry. we choose largest one
        if (clippedPolys.isEmpty()) {
            return null;
        }
        double maxSize = -1;
        Polygon maxPoly = null;
        Polygon cpoly;
        for (Polygon clippedPoly : clippedPolys) {
            cpoly = clippedPoly;
            final double area = cpoly.getArea();
            if (area > maxSize) {
                maxPoly = cpoly;
                maxSize = area;
            }
        }
        // fast clipping may result in polygons with 0 area
        if (maxSize > 0) {
            return maxPoly;
        } else {
            return null;
        }
    }

    /**
     * try to do a more robust way of clipping a polygon to a bounding box. This might return the orginal polygon if it
     * cannot clip TODO: this is a bit simplistic, there's lots more to do.
     *
     * @return a MutliPolygon
     */
    public MultiPolygon clipPolygon(Polygon poly, Polygon bbox, Envelope displayGeomEnv) {

        Geometry clip = poly;
        poly.geometryChanged(); // djb -- jessie should do this during
        // generalization
        if (displayGeomEnv.contains(poly.getEnvelopeInternal())) {
            // shortcut -- entirely inside the display rectangle -- no clipping
            // required!
            Polygon[] polys = new Polygon[1];
            polys[0] = (Polygon) clip;
            return poly.getFactory().createMultiPolygon(polys);
        }

        try {
            clip = clipper.clip(poly, false);
        } catch (Exception e) {
            // TODO: should try to expand the bounding box and re-do the
            // intersection.
            // TODO: also, try removing the interior rings of the polygon

            clip = poly; // just return the unclipped version
        }
        if (clip instanceof MultiPolygon) return (MultiPolygon) clip;
        if (clip instanceof Polygon) {
            Polygon[] polys = new Polygon[1];
            polys[0] = (Polygon) clip;
            return poly.getFactory().createMultiPolygon(polys);
        }
        // otherwise we've got a point or line&point or empty
        if (clip instanceof Point) return null;
        if (clip instanceof MultiPoint) return null;
        if (clip instanceof LineString) return null;
        if (clip instanceof MultiLineString) return null;
        if (clip == null) return null;

        // its a GC
        GeometryCollection gc = (GeometryCollection) clip;
        List<Polygon> polys = new ArrayList<>();
        Geometry g;
        for (int t = 0; t < gc.getNumGeometries(); t++) {
            g = gc.getGeometryN(t);
            if (g instanceof Polygon) polys.add((Polygon) g);
            // dont think multiPolygon is possible, but not sure
        }

        // convert to multipoly
        if (polys.isEmpty()) return null;

        return poly.getFactory().createMultiPolygon(polys.toArray(new Polygon[1]));
    }

    private List<LineString> mergeLines(Collection<LineString> lines) {
        // optimization and avoid rebuilding the elements into a different for (e.g., ring -> line)
        if (lines.size() <= 1) {
            return new ArrayList<>(lines);
        }

        LineMerger lm = new LineMerger();
        lm.add(lines);
        // build merged lines
        @SuppressWarnings("unchecked")
        List<LineString> merged = new ArrayList<>(lm.getMergedLineStrings());

        if (merged.isEmpty()) {
            return null; // shouldnt happen
        } else if (merged.size() == 1) { // simple case - no need to continue
            // merging
            return merged;
        }

        // coordinate -> list of incoming/outgoing lines
        Map<Coordinate, List<LineString>> nodes = new HashMap<>(merged.size() * 2);
        for (LineString ls : merged) {
            putInNodeHash(ls.getCoordinateN(0), ls, nodes);
            putInNodeHash(ls.getCoordinateN(ls.getNumPoints() - 1), ls, nodes);
        }

        List<LineString> merged_list = new ArrayList<>(merged);

        // SORT -- sorting is important because order does matter.
        // sorted long->short
        Collections.sort(merged_list, lineLengthComparator);
        return processNodes(merged_list, nodes);
    }

    /**
     * pull a line from the list, and: 1. if nothing connects to it (its issolated), add it to "result" 2. otherwise,
     * merge it at the start/end with the LONGEST line there. 3. remove the original line, and the lines it merged with
     * from the hashtables 4. go again, with the merged line
     */
    public List<LineString> processNodes(List<LineString> edges, Map<Coordinate, List<LineString>> nodes) {
        List<LineString> result = new ArrayList<>();
        int index = 0; // index into edges
        while (index < edges.size()) // still more to do
        {
            // 1. get a line and remove it from the graph
            LineString ls = edges.get(index);
            Coordinate key = ls.getCoordinateN(0);
            List<LineString> nodeList = nodes.get(key);
            if (nodeList == null) { // this was removed in an earlier iteration
                index++;
                continue;
            } else if (!nodeList.contains(ls)) {
                index++;
                continue; // already processed
            }
            removeFromHash(nodes, ls); // we're removing this from the network

            Coordinate key2 = ls.getCoordinateN(ls.getNumPoints() - 1);
            List<LineString> nodeList2 = nodes.get(key2);

            // case 1 -- this line is independent
            if (nodeList.isEmpty() && nodeList2.isEmpty()) {
                result.add(ls);
                index++; // move to next line
                continue;
            }

            if (!nodeList.isEmpty()) // touches something at the start
            {
                LineString ls2 = getLongest(nodeList); // merge with this one
                ls = merge(ls, ls2);
                removeFromHash(nodes, ls2);
            }
            if (!nodeList2.isEmpty()) // touches something at the start
            {
                LineString ls2 = getLongest(nodeList2); // merge with this one
                ls = merge(ls, ls2);
                removeFromHash(nodes, ls2);
            }
            // need for further processing
            edges.set(index, ls); // redo this one.
            putInNodeHash(ls.getCoordinateN(0), ls, nodes);
            putInNodeHash(ls.getCoordinateN(ls.getNumPoints() - 1), ls, nodes);
        }

        return result;
    }

    public void removeFromHash(Map<Coordinate, List<LineString>> nodes, LineString ls) {
        Coordinate key = ls.getCoordinateN(0);
        List<LineString> nodeList = nodes.get(key);
        if (nodeList != null) {
            nodeList.remove(ls);
        }
        key = ls.getCoordinateN(ls.getNumPoints() - 1);
        nodeList = nodes.get(key);
        if (nodeList != null) {
            nodeList.remove(ls);
        }
    }

    private LineString getLongest(List<LineString> al) {
        if (al.size() == 1) return al.get(0);
        double maxLength = -1;
        LineString result = null;
        for (LineString l : al) {
            if (l.getLength() > maxLength) {
                result = l;
                maxLength = l.getLength();
            }
        }
        return result;
    }

    private void putInNodeHash(Coordinate node, LineString ls, Map<Coordinate, List<LineString>> nodes) {
        List<LineString> nodeList = nodes.get(node);
        if (nodeList == null) {
            nodeList = new ArrayList<>();
            nodeList.add(ls);
            nodes.put(node, nodeList);
        } else {
            nodeList.add(ls);
        }
    }

    /** reverse direction of points in a line */
    private LineString reverse(LineString l) {
        List<Coordinate> clist = Arrays.asList(l.getCoordinates());
        Collections.reverse(clist);
        return l.getFactory().createLineString(clist.toArray(new Coordinate[1]));
    }

    /** If possible, merge the two lines together (ie. their start/end points are equal) returns null if not possible */
    private LineString merge(LineString major, LineString minor) {
        Coordinate major_s = major.getCoordinateN(0);
        Coordinate major_e = major.getCoordinateN(major.getNumPoints() - 1);
        Coordinate minor_s = minor.getCoordinateN(0);
        Coordinate minor_e = minor.getCoordinateN(minor.getNumPoints() - 1);

        if (major_s.equals2D(minor_s)) {
            // reverse minor -> major
            return mergeSimple(reverse(minor), major);

        } else if (major_s.equals2D(minor_e)) {
            // minor -> major
            return mergeSimple(minor, major);
        } else if (major_e.equals2D(minor_s)) {
            // major -> minor
            return mergeSimple(major, minor);
        } else if (major_e.equals2D(minor_e)) {
            // major -> reverse(minor)
            return mergeSimple(major, reverse(minor));
        }
        return null; // no merge
    }

    /** simple linestring merge - l1 points then l2 points */
    private LineString mergeSimple(LineString l1, LineString l2) {
        List<Coordinate> clist = new ArrayList<>(Arrays.asList(l1.getCoordinates()));
        clist.addAll(Arrays.asList(l2.getCoordinates()));

        return l1.getFactory().createLineString(clist.toArray(new Coordinate[1]));
    }

    /** sorts a list of LineStrings by length (long=1st) */
    private static final class LineLengthComparator implements java.util.Comparator<LineString> {
        @Override
        public int compare(LineString o1, LineString o2) {
            // sort big->small
            return Double.compare(o2.getLength(), o1.getLength());
        }
    }

    // djb: replaced because old one was from sun's Rectangle class
    private Envelope intersection(Envelope e1, Envelope e2) {
        Envelope r = e1.intersection(e2);
        if (r.getWidth() < 0) return null;
        if (r.getHeight() < 0) return null;
        return r;
    }

    /**
     * adds a listener that responds to error events of feature rendered events.
     *
     * @see RenderListener
     * @param listener the listener to add.
     */
    public void addRenderListener(RenderListener listener) {
        renderListeners.add(listener);
    }
}
