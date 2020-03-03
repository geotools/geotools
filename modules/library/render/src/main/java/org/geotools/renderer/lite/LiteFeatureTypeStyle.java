/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import org.geotools.data.util.ScreenMap;
import org.geotools.map.Layer;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.styling.Rule;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/**
 * This is a simple class that contains the information needed to render a layer.
 *
 * <p>Basically, for a SLD, you create one of these for each of the FeatureTypeStyles inside it.
 * LiteRenderer uses this to do the actual renderering.
 *
 * <p>It contains: a. a BufferedImage so lite knows where to do the drawing b. a list of rules
 * (minimal # -- ie. remove the ones that dont apply to this scale) c. "else" rule list
 *
 * <p>To process this, you would a) foreach FEATURE b) foreach LiteFeatureTypeStyle c) <process
 * rules and draw to the appropriate image> d) combine the images
 *
 * <p>This was setup so you can "parallelize" literenderer in the simple way -- only read data once.
 * The old implementation would re-read the data for each one FeatureTypeStyle.
 *
 * <p>NOTE: a) the SLD spec says that each FeatureTypeStyle is rendered in order & independently b)
 * If you have a request like LAYERS=a,a&STYLES=a_style1,a_styel2 then you could optimize to
 * something like this (!!)
 *
 * <p>NOTE: a) this also sets up the image -- clears it et al.
 *
 * @author dblasby
 */
final class LiteFeatureTypeStyle {

    public Layer layer;

    public Rule[] ruleList;

    public Rule[] elseRules;

    public Graphics2D graphics;

    public Expression transformation;

    public Composite composite;

    public SortBy[] sortBy;

    /** When true, the first matching rule will be applied, skipping the others */
    boolean matchFirst = false;

    /** The bit map used to decide whether to skip geometries that have been already drawn */
    ScreenMap screenMap;

    /**
     * Whether the feature should be generalized in memory, or not (in this case, the store did it
     * for us). True by default
     */
    boolean inMemoryGeneralization = true;

    /**
     * The handler that will be called to process the geometries to deal with projections
     * singularities and dateline wrapping
     */
    ProjectionHandler projectionHandler;

    /** The meta buffer for the current layer */
    int metaBuffer;

    /**
     * use this for only the 1st FTS. We don't actually create an image for it -- we just use the
     * graphics. WATCH OUT FOR THIS. NOTE: image=null in this case
     */
    public LiteFeatureTypeStyle(
            Layer layer,
            Graphics2D graphics,
            List ruleList,
            List elseRuleList,
            Expression transformation) {
        this.layer = layer;
        this.graphics = graphics;
        this.ruleList = (Rule[]) ruleList.toArray(new Rule[ruleList.size()]);
        this.elseRules = (Rule[]) elseRuleList.toArray(new Rule[elseRuleList.size()]);
        this.transformation = transformation;
    }
}
