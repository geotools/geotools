/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import org.geotools.renderer.ScreenMap;
import org.geotools.styling.Rule;

/**
 * This is a simple class that contains the information needed to render a layer.
 * 
 * Basically, for a SLD, you create one of these for each of the FeatureTypeStyles inside it.
 * LiteRenderer uses this to do the actual renderering.
 * 
 * It contains: a. a BufferedImage so lite knows where to do the drawing b. a list of rules (minimal
 * # -- ie. remove the ones that dont apply to this scale) c. "else" rule list
 * 
 * To process this, you would a) foreach FEATURE b) foreach LiteFeatureTypeStyle c) <process rules
 * and draw to the appropriate image> d) combine the images
 * 
 * This was setup so you can "parallelize" literenderer in the simple way -- only read data once.
 * The old implementation would re-read the data for each one FeatureTypeStyle.
 * 
 * NOTE: a) the SLD spec says that each FeatureTypeStyle is rendered in order & independently b) If
 * you have a request like LAYERS=a,a&STYLES=a_style1,a_styel2 then you could optimize to something
 * like this (!!)
 * 
 * NOTE: a) this also sets up the image -- clears it et al.
 * 
 * @author dblasby
 *
 * @source $URL$
 */
public final class LiteFeatureTypeStyle {
    public BufferedImage myImage;

    public Rule[] ruleList;

    public Rule[] elseRules;

    public Graphics2D graphics;

    /**
     * The bit map used to decide whether to skip geometries that have been already drawn
     */
    ScreenMap screenMap;

    public LiteFeatureTypeStyle(BufferedImage image, AffineTransform at, List ruleList,
            List elseRule, RenderingHints hints) {
        this.myImage = image;
        this.ruleList = (Rule[]) ruleList.toArray(new Rule[ruleList.size()]);
        this.elseRules = (Rule[]) elseRule.toArray(new Rule[elseRule.size()]);
        this.graphics = image.createGraphics();

        if (hints != null) {
            graphics.setRenderingHints(hints);
        }
    }

    /**
     * use this for only the 1st FTS. We dont actually create an image for it -- we just use the
     * graphics. WATCH OUT FOR THIS. NOTE: image=null in this case
     * 
     * @param graphics
     * @param ruleList
     * @param elseRuleList
     */
    public LiteFeatureTypeStyle(Graphics2D graphics, List ruleList, List elseRuleList) {

        this.myImage = null;
        this.graphics = graphics;
        this.ruleList = (Rule[]) ruleList.toArray(new Rule[ruleList.size()]);
        this.elseRules = (Rule[]) elseRuleList.toArray(new Rule[elseRuleList.size()]);
    }

}
