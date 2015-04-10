/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;

/**
 * Data structure holding a MapContent that has its own compositing base
 * 
 * @author Andrea Aime - GeoSolutions
 */
class CompositingGroup {

    private static StyleFactory STYLE_FACTORY = CommonFactoryFinder.getStyleFactory();

    public static List<CompositingGroup> splitOnCompositingBase(Graphics2D graphics,
            Rectangle screenSize,
            MapContent mc) {
        List<CompositingGroup> result = new ArrayList<>();
        List<Layer> layers = new ArrayList<>();
        for (Layer layer : mc.layers()) {
            Style style = layer.getStyle();
            if (layer instanceof DirectLayer) {
                layers.add(layer);
            } else {
                List<Style> styles = splitOnCompositingBase(style);
                for (Style s : styles) {
                    FeatureTypeStyle firstFts = s.featureTypeStyles().get(0);
                    if (isCompositingBase(firstFts) && !layers.isEmpty()) {
                        addToCompositingMapContents(graphics, screenSize, result, layers);
                    }
                    if (s == style) {
                        layers.add(layer);
                    } else {
                        // all the streaming renderer cares about is normally contained
                        // in a feature layer
                        FeatureLayer clone = new FeatureLayer(layer.getFeatureSource(), s);
                        clone.setQuery(layer.getQuery());
                        clone.setVisible(layer.isVisible());
                        clone.setSelected(layer.isSelected());
                        clone.getUserData().putAll(layer.getUserData());
                        layers.add(clone);
                    }
                }
            }
        }

        // do we have it simple?
        if (!layers.isEmpty()) {
            addToCompositingMapContents(graphics, screenSize, result, layers);
        }

        return result;
    }

    private static void addToCompositingMapContents(Graphics2D graphics, Rectangle screenSize,
            List<CompositingGroup> compositingContents, List<Layer> layers) {
        Composite composite = getComposite(layers);
        Graphics2D cmcGraphic;
        if (compositingContents.size() == 0 && !hasAlphaCompositing(layers)) {
            cmcGraphic = graphics;
        } else {
            cmcGraphic = new DelayedBackbufferGraphic(graphics, screenSize);
        }
        MapContent current = new MapContent();
        current.addLayers(layers);
        CompositingGroup cmc = new CompositingGroup(cmcGraphic, current,
                composite);
        compositingContents.add(cmc);
        layers.clear();
    }

    private static Composite getComposite(List<Layer> layers) {
        Style styles = layers.get(0).getStyle();
        List<FeatureTypeStyle> featureTypeStyles = styles.featureTypeStyles();
        if (featureTypeStyles.size() > 0) {
            FeatureTypeStyle firstFts = featureTypeStyles.get(0);
            Composite composite = SLDStyleFactory.getComposite(firstFts.getOptions());
            return composite;
        } else {
            return null;
        }
    }

    /**
     * Returns true if alpha compositing is used anywhere in the style
     * 
     * @param current
     * @return
     */
    private static boolean hasAlphaCompositing(List<Layer> layers) {
        AlphaCompositeVisitor visitor = new AlphaCompositeVisitor();
        for (Layer layer : layers) {
            Style style = layer.getStyle();
            style.accept(visitor);
            if (visitor.alphaComposite) {
                return true;
            }
        }

        return false;
    }

    private static List<Style> splitOnCompositingBase(Style style) {
        List<Style> styles = new ArrayList<>();
        List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (isCompositingBase(fts)) {
                addToStyles(styles, featureTypeStyles);
            }
            featureTypeStyles.add(fts);
        }

        addToStyles(styles, featureTypeStyles);

        return styles;
    }

    private static void addToStyles(List<Style> styles, List<FeatureTypeStyle> featureTypeStyles) {
        if (!featureTypeStyles.isEmpty()) {
            Style s = STYLE_FACTORY.createStyle();
            s.featureTypeStyles().addAll(featureTypeStyles);
            styles.add(s);
            featureTypeStyles.clear();
        }
    }

    private static boolean isCompositingBase(FeatureTypeStyle fts) {
        return "true".equalsIgnoreCase(fts.getOptions().get(FeatureTypeStyle.COMPOSITE_BASE));
    }

    Graphics2D graphics;

    MapContent mapContent;

    Composite composite;

    CompositingGroup(Graphics2D graphics, MapContent mapContent, Composite composite) {
        super();
        this.graphics = graphics;
        this.mapContent = mapContent;
        this.composite = composite;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public MapContent getMapContent() {
        return mapContent;
    }

    public Composite getComposite() {
        return composite;
    }

}
