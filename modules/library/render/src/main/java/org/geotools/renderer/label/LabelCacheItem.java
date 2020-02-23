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
package org.geotools.renderer.label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;
import org.locationtech.jts.geom.Geometry;

/**
 * The Labelling information that is put in the label cache.
 *
 * @author jeichar
 * @author dblasby
 * @author simone giannecchini
 * @author Andrea Aime - OpenGeo
 */
public class LabelCacheItem implements Comparable<LabelCacheItem> {

    public enum GraphicResize {
        NONE,
        STRETCH,
        PROPORTIONAL
    };

    TextStyle2D textStyle;

    List<Geometry> geoms = new ArrayList<Geometry>();

    double priority = 0.0;

    int spaceAround = 0;

    String label;

    private Set<String> layerIds = new HashSet<String>();

    int maxDisplacement = 0;

    int[] displacementAngles;

    int minGroupDistance = 0;

    int repeat = 0;

    boolean labelAllGroup = false;

    boolean removeGroupOverlaps = false;

    boolean allowOverruns = true;

    boolean followLineEnabled = false;

    double maxAngleDelta;

    int autoWrap = 100;

    boolean forceLeftToRightEnabled = true;

    boolean conflictResolutionEnabled = true;

    double goodnessOfFit = 0;

    PolygonAlignOptions polygonAlign = PolygonAlignOptions.NONE;

    GraphicResize graphicsResize = GraphicResize.NONE;

    int[] graphicMargin = null;

    boolean textUnderlined = false;

    boolean textStrikethrough = false;

    double wordSpacing;

    TextSymbolizer symbolizer;

    int fontShrinkSizeMin;

    public double getGoodnessOfFit() {
        return goodnessOfFit;
    }

    boolean partialsEnabled = false;

    TextSymbolizer.GraphicPlacement graphicPlacement;

    /**
     * A value between 0 and 1 representing the portion of the label that overlaps with the geometry
     * (atm used only for polygons)
     */
    public void setGoodnessOfFit(double goodnessOfFit) {
        this.goodnessOfFit = goodnessOfFit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String l) {
        label = l;
    }

    /** space around - "dont put any label near me by this # of pixels" */
    public int getSpaceAround() {
        return spaceAround;
    }

    /** space around - "dont put any label near me by this # of pixels" */
    public void setSpaceAround(int space) {
        spaceAround = space;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double d) {
        priority = d;
    }

    /** Construct <code>LabelCacheItem</code>. */
    public LabelCacheItem(
            String layerId,
            TextStyle2D textStyle,
            LiteShape2 shape,
            String label,
            TextSymbolizer symbolizer) {
        this.textStyle = textStyle;
        this.geoms.add(shape.getGeometry());
        this.label = label;
        this.layerIds.add(layerId);
        this.symbolizer = symbolizer;
    }

    /** Construct <code>LabelCacheItem</code>. */
    LabelCacheItem(LabelCacheItem other) {
        // copy part
        this.textStyle = other.textStyle;
        this.label = other.label;
        this.layerIds.addAll(other.getLayerIds());
        this.geoms.addAll(other.geoms);
        this.symbolizer = other.symbolizer;
        this.priority = other.priority;
        this.spaceAround = other.spaceAround;
        this.label = other.label;
        this.maxDisplacement = other.maxDisplacement;
        this.minGroupDistance = other.minGroupDistance;
        this.repeat = other.repeat;
        this.labelAllGroup = other.labelAllGroup;
        this.removeGroupOverlaps = other.removeGroupOverlaps;
        this.allowOverruns = other.allowOverruns;
        this.followLineEnabled = other.followLineEnabled;
        this.maxAngleDelta = other.maxAngleDelta;
        this.autoWrap = other.autoWrap;
        this.forceLeftToRightEnabled = other.forceLeftToRightEnabled;
        this.conflictResolutionEnabled = other.conflictResolutionEnabled;
        this.goodnessOfFit = other.goodnessOfFit;
        this.polygonAlign = other.polygonAlign;
        this.graphicsResize = other.graphicsResize;
        this.graphicMargin = other.graphicMargin;
        this.textUnderlined = other.textUnderlined;
        this.symbolizer = other.symbolizer;
        this.fontShrinkSizeMin = other.fontShrinkSizeMin;
    }

    /** Return a modifiable set of ids */
    public Set<String> getLayerIds() {
        return Collections.synchronizedSet(layerIds);
    }

    /** The list of geometries this item maintains */
    public List<Geometry> getGeoms() {
        return geoms;
    }

    /** The textstyle that is used to label the shape. */
    public TextStyle2D getTextStyle() {
        return textStyle;
    }

    void setTextStyle(TextStyle2D textStyle) {
        this.textStyle = textStyle;
    }

    /** @see java.lang.Object#hashCode() */

    /** Returns an example geometry from the list of geometries. */
    public Geometry getGeometry() {
        return (Geometry) geoms.get(0);
    }

    /**
     * Max amount of pixels the label will be moved around trying to find a non conflicting location
     * (how and if the moving will be done is geometry type dependent)
     */
    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(int maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    /**
     * defines the actual angle towards which displacement of label will take place (applies only in
     * polygon or point features)
     */
    public int[] getDisplacementAngles() {
        return displacementAngles;
    }

    public void setDisplacementAngles(int[] displacementAngles) {
        this.displacementAngles = displacementAngles;
    }

    /** When enabled, repeats labels every "repeat" pixels (works on lines only atm) */
    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /** When grouping, wheter we should label only the biggest geometry, or the others as well */
    public boolean labelAllGroup() {
        return labelAllGroup;
    }

    public void setLabelAllGroup(boolean labelAllGroup) {
        this.labelAllGroup = labelAllGroup;
    }

    public boolean removeGroupOverlaps() {
        return removeGroupOverlaps;
    }

    public void setRemoveGroupOverlaps(boolean removeGroupOverlaps) {
        this.removeGroupOverlaps = removeGroupOverlaps;
    }

    /** Wheter labels are allowed to go past the start/end of the line */
    public boolean allowOverruns() {
        return allowOverruns;
    }

    public void setAllowOverruns(boolean allowOverruns) {
        this.allowOverruns = allowOverruns;
    }

    public int getMinGroupDistance() {
        return minGroupDistance;
    }

    /** Minimum cartesian distance between two labels in the same group, in pixels */
    public void setMinGroupDistance(int minGroupDistance) {
        this.minGroupDistance = minGroupDistance;
    }

    /** Enables curved labels on linear features */
    public boolean isFollowLineEnabled() {
        return followLineEnabled;
    }

    public void setFollowLineEnabled(boolean followLineEnabled) {
        this.followLineEnabled = followLineEnabled;
    }

    /**
     * Max angle between two subsequence characters in a curved label, in degrees. Good visual
     * results are obtained with an angle of less than 25 degrees.
     */
    public double getMaxAngleDelta() {
        return maxAngleDelta;
    }

    public void setMaxAngleDelta(double maxAngleDelta) {
        this.maxAngleDelta = maxAngleDelta;
    }

    /**
     * Automatically wraps long labels when the label width, in pixels, exceeds the autowrap length
     */
    public int getAutoWrap() {
        return autoWrap;
    }

    public void setAutoWrap(int autoWrap) {
        this.autoWrap = autoWrap;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(LabelCacheItem other) {
        return Double.compare(this.getPriority(), other.getPriority());
    }

    /**
     * If enabled, text will be forced to follow a left to right alignement (that makes it readable)
     * no matter what the natural orientation of the line is
     */
    public boolean isForceLeftToRightEnabled() {
        return forceLeftToRightEnabled;
    }

    public void setForceLeftToRightEnabled(boolean forceLeftToRight) {
        this.forceLeftToRightEnabled = forceLeftToRight;
    }

    /** Checks if conflict resolution has been enabled for this label */
    public boolean isConflictResolutionEnabled() {
        return conflictResolutionEnabled;
    }

    /**
     * Sets conflict resolution for this label. When on, this label outline/bbox will be stored in
     * the conflict resolution map and will prevent every other label to be drawn in the same area
     */
    public void setConflictResolutionEnabled(boolean conflictResolutionEnabled) {
        this.conflictResolutionEnabled = conflictResolutionEnabled;
    }

    public GraphicResize getGraphicsResize() {
        return graphicsResize;
    }

    public void setGraphicsResize(GraphicResize graphicsResize) {
        this.graphicsResize = graphicsResize;
    }

    public int[] getGraphicMargin() {
        return graphicMargin;
    }

    public void setGraphicMargin(int[] graphicMargin) {
        this.graphicMargin = graphicMargin;
    }

    void setPolygonAlign(PolygonAlignOptions polygonAlign) {
        this.polygonAlign = polygonAlign;
    }

    PolygonAlignOptions getPolygonAlign() {
        return polygonAlign;
    }

    public boolean isPartialsEnabled() {
        return partialsEnabled;
    }

    public void setPartialsEnabled(boolean partialsEnabled) {
        this.partialsEnabled = partialsEnabled;
    }

    public boolean isTextUnderlined() {
        return textUnderlined;
    }

    public void setTextUnderlined(boolean textUnderlined) {
        this.textUnderlined = textUnderlined;
    }

    public boolean isTextStrikethrough() {
        return textStrikethrough;
    }

    public void setTextStrikethrough(boolean textStrikethrough) {
        this.textStrikethrough = textStrikethrough;
    }

    public double getWordSpacing() {
        return wordSpacing;
    }

    public void setWordSpacing(double wordSpacing) {
        this.wordSpacing = wordSpacing;
    }

    public int getFontShrinkSizeMin() {
        return fontShrinkSizeMin;
    }

    public void setFontShrinkSizeMin(int fontShrinkSize) {
        this.fontShrinkSizeMin = fontShrinkSize;
    }

    public TextSymbolizer.GraphicPlacement getGraphicPlacement() {
        return graphicPlacement == null ? TextSymbolizer.GraphicPlacement.LABEL : graphicPlacement;
    }

    public void setGraphicPlacement(TextSymbolizer.GraphicPlacement graphicPlacement) {
        this.graphicPlacement = graphicPlacement;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((symbolizer == null) ? 0 : System.identityHashCode(symbolizer));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LabelCacheItem other = (LabelCacheItem) obj;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        if (symbolizer != other.symbolizer) {
            return false;
        }
        return true;
    }
}
