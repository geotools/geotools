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
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The Labelling information that is put in the label cache.
 * 
 * @author jeichar
 * @author dblasby
 * @author simone giannecchini
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL$
 */
public class LabelCacheItem implements Comparable<LabelCacheItem> {
    
    public enum GraphicResize {NONE, STRETCH, PROPORTIONAL};

    TextStyle2D textStyle;

    List<Geometry> geoms = new ArrayList<Geometry>();

    double priority = 0.0;

    int spaceAround = 0;

    String label;

    private Set<String> layerIds = new HashSet<String>();

    int maxDisplacement = 0;

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

    public double getGoodnessOfFit() {
        return goodnessOfFit;
    }

    /**
     * A value between 0 and 1 representing the portion of the label
     * that overlaps with the geometry (atm used only for polygons)
     * @param goodnessOfFit
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

    /**
     * space around - "dont put any label near me by this # of pixels"
     */
    public int getSpaceAround() {
        return spaceAround;
    }

    /**
     * space around - "dont put any label near me by this # of pixels"
     */
    public void setSpaceAround(int space) {
        spaceAround = space;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double d) {
        priority = d;
    }

    /**
     * Construct <code>LabelCacheItem</code>.
     */
    public LabelCacheItem(String layerId, TextStyle2D textStyle, LiteShape2 shape, String label) {
        this.textStyle = textStyle;
        this.geoms.add(shape.getGeometry());
        this.label = label;
        this.layerIds.add(layerId);
    }

    /**
     * Return a modifiable set of ids
     * 
     * @return
     */
    public Set<String> getLayerIds() {
        return Collections.synchronizedSet(layerIds);
    }

    /**
     * The list of geometries this item maintains
     */
    public List<Geometry> getGeoms() {
        return geoms;
    }

    /**
     * The textstyle that is used to label the shape.
     */
    public TextStyle2D getTextStyle() {
        return textStyle;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        if (arg0 instanceof String) {
            String label = (String) arg0;
            return label.equals(textStyle.getLabel());
        }
        if (arg0 instanceof LabelCacheItem) {
            LabelCacheItem item = (LabelCacheItem) arg0;
            return textStyle.getLabel().equals(item.getTextStyle().getLabel());
        }
        if (arg0 instanceof TextStyle2D) {
            TextStyle2D text = (TextStyle2D) arg0;
            return textStyle.getLabel().equals(text.getLabel());
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */

    /**
     * Returns an example geometry from the list of geometries.
     */
    public Geometry getGeometry() {
        return (Geometry) geoms.get(0);
    }

    /**
     * Max amount of pixels the label will be moved around trying to find a non
     * conflicting location (how and if the moving will be done is geometry type
     * dependent)
     * 
     * @return
     */
    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(int maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    /**
     * When enabled, repeats labels every "repeat" pixels (works on lines only
     * atm)
     * 
     * @return
     */
    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /**
     * When grouping, wheter we should label only the biggest geometry, or the
     * others as well
     * 
     * @return
     */
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

    /**
     * Wheter labels are allowed to go past the start/end of the line
     * 
     * @return
     */
    public boolean allowOverruns() {
        return allowOverruns;
    }

    public void setAllowOverruns(boolean allowOverruns) {
        this.allowOverruns = allowOverruns;
    }

    public int getMinGroupDistance() {
        return minGroupDistance;
    }

    /**
     * Minimum cartesian distance between two labels in the same group, in
     * pixels
     * 
     * @param minGroupDistance
     */
    public void setMinGroupDistance(int minGroupDistance) {
        this.minGroupDistance = minGroupDistance;
    }

    /**
     * Enables curved labels on linear features
     * 
     * @return
     */
    public boolean isFollowLineEnabled() {
        return followLineEnabled;
    }

    public void setFollowLineEnabled(boolean followLineEnabled) {
        this.followLineEnabled = followLineEnabled;
    }

    /**
     * Max angle between two subsequence characters in a curved label, in
     * degrees. Good visual results are obtained with an angle of less than 25
     * degrees.
     * 
     * @return
     */
    public double getMaxAngleDelta() {
        return maxAngleDelta;
    }

    public void setMaxAngleDelta(double maxAngleDelta) {
        this.maxAngleDelta = maxAngleDelta;
    }

    /**
     * Automatically wraps long labels when the label width, in pixels, exceeds
     * the autowrap length
     * 
     * @return
     */
    public int getAutoWrap() {
        return autoWrap;
    }

    public void setAutoWrap(int autoWrap) {
        this.autoWrap = autoWrap;
    }

    public int hashCode() {
        return textStyle.getLabel().hashCode();
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
     * If enabled, text will be forced to follow a left to right alignement
     * (that makes it readable) no matter what the natural orientation of the 
     * line is
     * @return
     */
    public boolean isForceLeftToRightEnabled() {
        return forceLeftToRightEnabled;
    }

    public void setForceLeftToRightEnabled(boolean forceLeftToRight) {
        this.forceLeftToRightEnabled = forceLeftToRight;
    }
    
    /**
     * Checks if conflict resolution has been enabled for this label
     * @return
     */
    public boolean isConflictResolutionEnabled() {
        return conflictResolutionEnabled;
    }
    
    /**
     * Sets conflict resolution for this label. When on, this label outline/bbox will
     * be stored in the conflict resolution map and will prevent every other label
     * to be drawn in the same area
     * @param conflictResolutionEnabled
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
    
}

