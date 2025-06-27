/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.mbstyle.layer.BackgroundMBLayer;
import org.geotools.mbstyle.layer.MBLayer;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.parse.MBObjectStops;
import org.geotools.mbstyle.source.MBSource;
import org.geotools.referencing.CRS;
import org.geotools.styling.StyleBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * MapBox Style implemented as wrapper around parsed JSON file.
 *
 * <p>This class is responsible for presenting the wrapped JSON in an easy to use / navigate form for Java developers:
 *
 * <ul>
 *   <li>get methods: access the json directly
 *   <li>query methods: provide logic / transforms to GeoTools classes as required.
 * </ul>
 *
 * <p>Access methods should return Java Objects, rather than generic maps. Additional access methods to perform common
 * queries are expected and encouraged.
 *
 * <p>This class works closely with {@link MBLayer} hierarchy used to represent the fill, line, symbol, raster, circle
 * layers. Additional support will be required to work with sprites and glyphs.
 *
 * @author Jody Garnett (Boundless)
 */
public class MBStyle {

    private static final int DEFAULT_LABEL_PRIORITY = 1000;
    /**
     * JSON document being wrapped by this class.
     *
     * <p>All methods act as accessors on this JSON document, no other state is maintained. This allows modifications to
     * be made cleaning with out chance of side-effect.
     */
    public final JSONObject json;

    /** Helper class used to perform JSON traversal and perform Expression and Filter conversions. */
    final MBObjectParser parse = new MBObjectParser(MBStyle.class);

    /**
     * MBStyle wrapper on the provided json
     *
     * @param json Map Box Style as parsed JSON
     */
    public MBStyle(JSONObject json) {
        this.json = json;
    }
    /**
     * Parse MBStyle for the provided json.
     *
     * @param json Required to be a JSONObject
     * @return MBStyle wrapping the provided json
     * @throws MBFormatException JSON content inconsistent with specification
     */
    public static MBStyle create(Object json) throws MBFormatException {
        if (json instanceof JSONObject) {
            return new MBStyle((JSONObject) json);
        } else if (json == null) {
            throw new MBFormatException("JSONObject required: null");
        } else {
            throw new MBFormatException("Root must be a JSON Object: " + json.toString());
        }
    }

    /**
     * Access the layer with the provided id.
     *
     * @param id Id of layer to access
     * @return layer with the provided id, or null if not found.
     */
    public MBLayer layer(String id) {
        if (id == null) {
            return null;
        }
        for (MBLayer layer : layers()) {
            if (id.equals(layer.getId())) {
                return layer;
            }
        }
        return null;
    }

    /**
     * Access all layers.
     *
     * @return list of layers
     */
    public List<MBLayer> layers() {
        JSONArray layers = parse.getJSONArray(json, "layers");
        List<MBLayer> layersList = new ArrayList<>();
        int labelPriority = 0;
        for (Object obj : layers) {
            if (obj instanceof JSONObject) {

                JSONObject jsonObject = (JSONObject) obj;

                MBLayer mbLayer = null;
                if (jsonObject.containsKey("ref")) {
                    String refLayer = jsonObject.get("ref").toString();
                    JSONObject refObject = referenceLayer(layers, refLayer);
                    if (!refObject.isEmpty()) {
                        // At a minimum, a type is needed to create a layer
                        applyReferenceObject(jsonObject, refObject);
                        mbLayer = MBLayer.create(jsonObject);
                    }
                } else {
                    mbLayer = MBLayer.create((JSONObject) obj);
                }
                // adjust label priority so that the labels of the last layer are painted first
                if (mbLayer instanceof SymbolMBLayer) {
                    labelPriority += DEFAULT_LABEL_PRIORITY;
                    ((SymbolMBLayer) mbLayer).setLabelPriority(labelPriority);
                }
                layersList.add(mbLayer);
            } else {
                throw new MBFormatException("Unexpected layer definition " + obj);
            }
        }
        return layersList;
    }

    private JSONObject referenceLayer(JSONArray layers, String refLayer) {
        JSONObject refObject = new JSONObject();
        for (Object check : layers) {
            if (check instanceof JSONObject
                    && refLayer.equalsIgnoreCase(((JSONObject) check).get("id").toString())) {
                refObject = (JSONObject) check;
            }
        }
        return refObject;
    }

    @SuppressWarnings("unchecked")
    private JSONObject applyReferenceObject(JSONObject layer, JSONObject refLayer) {
        HashMap<String, Object> jsonObject = (HashMap<String, Object>) layer;

        jsonObject.put("type", refLayer.get("type"));
        jsonObject.put("source", refLayer.get("source"));
        jsonObject.put("source-layer", refLayer.get("source-layer"));
        jsonObject.put("minzoom", refLayer.get("minzoom"));
        jsonObject.put("maxzoom", refLayer.get("maxzoom"));
        jsonObject.put("filter", refLayer.get("filter"));
        if (!jsonObject.containsKey("layout")) {
            jsonObject.put("layout", refLayer.get("layout"));
        }
        if (!jsonObject.containsKey("paint")) {
            jsonObject.put("paint", refLayer.get("paint"));
        }
        return layer;
    }

    /**
     * Access layers matching provided source.
     *
     * @param source Data source
     * @return list of layers matching provided source
     */
    public List<MBLayer> layers(String source) throws MBFormatException {
        JSONArray layers = parse.getJSONArray(json, "layers");
        List<MBLayer> layersList = new ArrayList<>();
        for (Object obj : layers) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                if (jsonObject.containsKey("ref")) {
                    String refLayer = jsonObject.get("ref").toString();
                    JSONObject refObject = referenceLayer(layers, refLayer);

                    if (!refObject.isEmpty()) {
                        applyReferenceObject(jsonObject, refObject);

                        MBLayer layer = MBLayer.create(jsonObject);
                        if (source.equals(layer.getSource())) {
                            layersList.add(layer);
                        }
                    }
                } else {
                    MBLayer layer = MBLayer.create(jsonObject);
                    if (source.equals(layer.getSource())) {
                        layersList.add(layer);
                    }
                }
            } else {
                throw new MBFormatException("Unexpected layer definition " + obj);
            }
        }
        return layersList;
    }

    /**
     * A human-readable name for the style
     *
     * @return human-readable name, or "name" if the style has no name.
     */
    public String getName() {
        return parse.optional(String.class, json, "name", null);
    }

    /**
     * (Optional) Arbitrary properties useful to track with the stylesheet, but do not influence rendering. Properties
     * should be prefixed to avoid collisions, like 'mapbox:'.
     *
     * @return {@link JSONObject} containing the metadata, or an empty JSON object the style has no metadata.
     */
    public JSONObject getMetadata() {
        return parse.getJSONObject(json, "metadata", new JSONObject());
    }

    /**
     * (Optional) Default map center in longitude and latitude. The style center will be used only if the map has not
     * been positioned by other means (e.g. map options or user interaction).
     *
     * @return A {@link Point} for the map center, or null if the style contains no center.
     */
    public Point2D getCenter() {
        double[] coords = parse.array(json, "center", (double[]) null);
        if (coords == null) {
            return null;
        } else if (coords.length != 2) {
            throw new MBFormatException("\"center\" array must be length 2.");
        } else {
            return new Point2D.Double(coords[0], coords[1]);
        }
    }

    /**
     * (Optional) Default zoom level. The style zoom will be used only if the map has not been positioned by other means
     * (e.g. map options or user interaction).
     *
     * @return Number for the zoom level, or null if the style has no default zoom level.
     */
    public Number getZoom() {
        return parse.optional(Number.class, json, "zoom", null);
    }

    /**
     * (Optional) Default bearing, in degrees clockwise from true north. The style bearing will be used only if the map
     * has not been positioned by other means (e.g. map options or user interaction).
     *
     * @return The bearing in degrees. Defaults to 0 if the style has no bearing.
     */
    public Number getBearing() {
        return parse.optional(Number.class, json, "bearing", 0);
    }

    /**
     * (Optional) Default pitch, in degrees. Zero is perpendicular to the surface, for a look straight down at the map,
     * while a greater value like 60 looks ahead towards the horizon. The style pitch will be used only if the map has
     * not been positioned by other means (e.g. map options or user interaction).
     *
     * @return The pitch in degrees. Defaults to 0 if the style has no pitch.
     */
    public Number getPitch() {
        return parse.optional(Number.class, json, "pitch", 0);
    }

    /**
     * A base URL for retrieving the sprite image and metadata. The extensions .png, .json and scale factor @2x.png will
     * be automatically appended. This property is required if any layer uses the background-pattern, fill-pattern,
     * line-pattern, fill-extrusion-pattern, or icon-image properties.
     *
     * @return The sprite URL, or null if the style has no sprite URL.
     */
    public String getSprite() {
        return parse.optional(String.class, json, "sprite", null);
    }

    /**
     * (Optional) A URL template for loading signed-distance-field glyph sets in PBF format. The URL must include
     * {fontstack} and {range} tokens. This property is required if any layer uses the text-field layout property. <br>
     * Example: <br>
     * <code>"glyphs": "mapbox://fonts/mapbox/{fontstack}/{range}.pbf"</code>
     *
     * @return The glyphs URL template, or null if the style has no glyphs URL template.
     */
    public String getGlyphs() {
        return parse.optional(String.class, json, "glyphs", null);
    }

    /**
     * Data source specifications.
     *
     * @see MBSource
     * @return Map of data source name to {@link MBSource} instances.
     */
    public Map<String, MBSource> getSources() {
        Map<String, MBSource> sourceMap = new HashMap<>();
        JSONObject sources = parse.getJSONObject(json, "sources", new JSONObject());
        for (Object o : sources.keySet()) {
            if (o instanceof String) {
                String k = (String) o;
                JSONObject j = parse.getJSONObject(sources, k);
                MBSource s = MBSource.create(j, parse);
                sourceMap.put(k, s);
            }
        }
        return sourceMap;
    }

    /**
     * Transform MBStyle to a GeoTools StyledLayerDescriptor.
     *
     * @return StyledLayerDescriptor
     */
    public StyledLayerDescriptor transform() {
        StyleFactory sf = parse.getStyleFactory();
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();

        List<MBLayer> layers = layers();
        if (layers.isEmpty()) {
            throw new MBFormatException("layers empty");
        }

        // TODO: Just track last NamedLayer
        NamedLayer currentNamedLayer = null;
        String currentName = null;
        BackgroundMBLayer background = null;
        for (MBLayer layer : layers) {
            List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
            MBObjectStops mbObjectStops = new MBObjectStops(layer);

            int layerMaxZoom = layer.getMaxZoom();
            int layerMinZoom = layer.getMinZoom();
            Double layerMinScaleDenominator = layerMaxZoom == Integer.MAX_VALUE
                    ? null
                    : MBObjectStops.zoomLevelToScaleDenominator(Math.min(25d, layerMaxZoom));
            Double layerMaxScaleDenominator = layerMinZoom == Integer.MIN_VALUE
                    ? null
                    : MBObjectStops.zoomLevelToScaleDenominator(Math.max(-25d, layerMinZoom));

            if (layer.visibility()) {
                // check for property and zoom functions, if true we will have a layer for each one
                // that becomes a feature type style.
                if (mbObjectStops.ls.zoomPropertyStops) {
                    List<Double> stopLevels = mbObjectStops.stops;
                    int i = 0;
                    for (MBLayer l : mbObjectStops.layersForStop) {
                        double s = stopLevels.get(i);
                        double[] rangeForStopLevel = mbObjectStops.getRangeForStop(s, mbObjectStops.ranges);
                        Double maxScaleDenominator = MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[0]);
                        Double minScaleDenominator = null;
                        if (rangeForStopLevel[1] != -1) {
                            minScaleDenominator = MBObjectStops.zoomLevelToScaleDenominator(rangeForStopLevel[1]);
                        }

                        featureTypeStyles.addAll(l.transform(this, minScaleDenominator, maxScaleDenominator));
                        i++;
                    }
                } else if (layer instanceof BackgroundMBLayer) {
                    background = (BackgroundMBLayer) layer;
                } else {
                    featureTypeStyles.addAll(layer.transform(this, layerMinScaleDenominator, layerMaxScaleDenominator));
                }
            }

            if (!featureTypeStyles.isEmpty()) {
                if (!(layer instanceof BackgroundMBLayer)) {
                    String sourceLayer = layer.getSourceLayer();
                    if (sourceLayer == null) {
                        // If source-layer is not set, assume the source just has one layer which
                        // shares its name
                        sourceLayer = layer.getSource();
                    }
                    // Append to existing namedlayer if source name is the same, otherwise create a
                    // new one
                    if (currentNamedLayer == null || !sourceLayer.equals(currentName)) {
                        currentNamedLayer = sf.createNamedLayer();
                        currentName = sourceLayer;
                        currentNamedLayer.setName(currentName);
                        // TODO: When NamedLayer supports description, use layer.getId() for
                        // description

                        Style style = sf.createStyle();
                        currentNamedLayer.styles().add(style);

                        sld.layers().add(currentNamedLayer);
                    }
                    // Add all featureTypeStyles to the first (and only) UserStyle
                    currentNamedLayer.styles().get(0).featureTypeStyles().addAll(featureTypeStyles);
                }
            }
        }

        if (background != null) {
            // attach to the first layer if possible
            FilterFactory ff = parse.getFilterFactory();
            if (sld.getStyledLayers().length > 0) {
                NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
                Style firstStyle = layer.getStyles()[0];
                firstStyle.setBackground(background.getFill(this));
            } else {
                // odd situation, the only spec is a background layer? build a fake SLD layer then
                addLoneBackgroundLayer(sf, sld, background, ff);
            }
        } else if (sld.layers().isEmpty()) {
            throw new MBFormatException("No visibile layers");
        }

        sld.setName(getName());
        return sld;
    }

    private void addLoneBackgroundLayer(
            StyleFactory sf, StyledLayerDescriptor sld, BackgroundMBLayer background, FilterFactory ff) {
        // Background does not use a source; construct a user later with a world extent
        // inline feature so that we still have a valid SLD.
        UserLayer userLayer = sf.createUserLayer();
        Style style = sf.createStyle();

        final SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
        final GeometryFactory jtsFactory = new GeometryFactory(pm, 4326);

        // must include a geometry so that the layer is rendered
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

            ftb.add("geometry", org.locationtech.jts.geom.Polygon.class, crs);
            ftb.setCRS(crs);
            ftb.setDefaultGeometry("geometry");
            ftb.setName("background");
            SimpleFeatureType featureType = ftb.buildFeatureType();

            final DefaultFeatureCollection fc = new DefaultFeatureCollection();
            fc.add(SimpleFeatureBuilder.build(
                    featureType,
                    new Object[] {jtsFactory.toGeometry(new ReferencedEnvelope(CRS.getEnvelope(crs)))},
                    "background"));

            userLayer.setInlineFeatureType(featureType);
            userLayer.setInlineFeatureDatastore(DataUtilities.dataStore(fc));
            userLayer.setName("background");

            // fake style and filter, won't ever be ued
            StyleBuilder sb = new StyleBuilder();
            Rule rule = sb.createRule(sb.createPolygonSymbolizer());
            rule.setFilter(ff.equal(ff.literal(0), ff.literal(1)));
            FeatureTypeStyle featureTypeStyle = sb.createFeatureTypeStyle("background", rule);
            style.featureTypeStyles().add(featureTypeStyle);
            // the actual background color
            style.setBackground(background.getFill(this));

            userLayer.userStyles().add(style);
            sld.layers().add(userLayer);
        } catch (FactoryException e) {
            throw new MBFormatException("Error constructing background layer", e);
        }
    }
}
