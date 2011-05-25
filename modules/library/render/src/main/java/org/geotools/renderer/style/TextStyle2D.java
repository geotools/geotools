/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

// J2SE dependencies
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.text.Bidi;

import javax.swing.Icon;

import org.geotools.resources.Classes;


/**
 * Style used to represent labels over lines, polygons and points
 * 
 *
 * @author Andrea Aime
 * @author dblasby
 * @version $Id$
 */

/** DJB:
	 * 
	 *  This class was fundamentally wrong - it tried to convert <LinePlacement> into <PointPlacement>. 
	 *   Not only was it doing a really crappy job, but its fundamentally the wrong place to do it.
	 * 
	 *   The SLD spec defines a <PointPlacement> as:
	 * <xsd:sequence>
	 *    <xsd:element ref="sld:AnchorPoint" minOccurs="0"/>
	 *    <xsd:element ref="sld:Displacement" minOccurs="0"/>
	 *    <xsd:element ref="sld:Rotation" minOccurs="0"/>
	 * </xsd:sequence>
	 * 
	 *  and <LinePlacement> as:
	 * <xsd:sequence>
	 *  <xsd:element ref="sld:PerpendicularOffset "minOccurs="0"/>
	 * </xsd:sequence>
	 * 
	 *   its annotated as:
	 * A "PerpendicularOffset" gives the perpendicular distance away from a line to draw a label.
     * which is a bit vague, but there's a little more details here:
     * 
     * The PerpendicularOffset element of a LinePlacement gives the perpendicular distance away from a line to draw a label.   ...
     * The distance is in pixels and is positive to the left-hand.
     * 
     *  Left hand/right hand for perpendicularOffset is just crap - I'm assuming them mean +ive --> "up" and -ive --> "down".
     *  See the actual label code for how it deals with this.
     * 
     *  I've removed all the absoluteLineDisplacement stuff and replaced it with
     *     isPointPlacement() (true) --> render normally (PointPlacement Attributes)
     *     isPointPlacement() (false) --> render LinePlacement 
     * 
     *   This replaces the old behavior which converted a LinePlacement -> pointplacement and set the absoluteLineDisplacement flag!
	 * 
 *
 * @source $URL$
	 * */
	 
public class TextStyle2D extends Style2D {
    GlyphVector textGlyphVector;
    Shape haloShape;
    String label;
    Font font;
    double rotation;
      /** yes = <PointPlacement> no = <LinePlacement>  default = yes**/
    boolean pointPlacement = true;
    int     perpendicularOffset =0; // only valid when using a LinePlacement
    double anchorX;
    double anchorY;
    double displacementX;
    double displacementY;
    Paint haloFill;
    Composite haloComposite;
    float haloRadius;
    Style2D graphic;


    /** Holds value of property fill. */
    private Paint fill;

    /** Holds value of property composite. */
    private Composite composite;
    
    public TextStyle2D() {
        // default constructor
    }
    
    public TextStyle2D(TextStyle2D t) {
        this.anchorX = t.anchorX;
        this.anchorY = t.anchorY;
        this.composite = t.composite;
        this.displacementX = t.displacementX;
        this.displacementY = t.displacementY;
        this.fill = t.fill;
        this.font = t.font;
        this.graphic = t.graphic;
        this.haloComposite = t.haloComposite;
        this.haloFill = t.haloFill;
        this.haloRadius = t.haloRadius;
        this.haloShape = t.haloShape;
        this.label = t.label;
        this.maxScale = t.maxScale;
        this.minScale = t.minScale;
        this.perpendicularOffset = t.perpendicularOffset;
        this.pointPlacement = t.pointPlacement;
        this.rotation = t.rotation;
        this.textGlyphVector = t.textGlyphVector;
    }

    /**
     */
    public double getAnchorX() {
        return anchorX;
    }

    /**
     */
    public double getAnchorY() {
        return anchorY;
    }

    /**
     */
    public Font getFont() {
        return font;
    }

    /**
     */
    public Composite getHaloComposite() {
        return haloComposite;
    }

    /**
     */
    public Paint getHaloFill() {
        return haloFill;
    }

    /**
     */
    public float getHaloRadius() {
        return haloRadius;
    }

    /**
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * recompute each time
     */
    public GlyphVector getTextGlyphVector(Graphics2D graphics) {
        // arabic and hebrew are scripted and right to left, they do require full layout
        // whilst western chars are easier to deal with. Find out which case we're dealing with,
        // and create the glyph vector with the appropriate call
        final char[] chars = label.toCharArray();
        final int length = label.length();
        if(Bidi.requiresBidi(chars, 0, length) && 
                new Bidi(label, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isRightToLeft())
            textGlyphVector = font.layoutGlyphVector(graphics.getFontRenderContext(), chars, 
                    0, length, Font.LAYOUT_RIGHT_TO_LEFT);
        else
            textGlyphVector = font.createGlyphVector(graphics.getFontRenderContext(), chars);
        return textGlyphVector;
    }

    /**
     */
    public Shape getHaloShape(Graphics2D graphics) 
    {
            GlyphVector gv = getTextGlyphVector(graphics);
            haloShape = new BasicStroke(2f * haloRadius, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND).createStrokedShape(gv.getOutline());
        return haloShape;
    }

    /**
     * @param f
     */
    public void setAnchorX(double f) {
        anchorX = f;
    }

    /**
     * @param f
     */
    public void setAnchorY(double f) {
        anchorY = f;
    }

    /**
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @param composite
     */
    public void setHaloComposite(Composite composite) {
        haloComposite = composite;
    }

    /**
     * @param paint
     */
    public void setHaloFill(Paint paint) {
        haloFill = paint;
    }

    /**
     * @param f
     */
    public void setHaloRadius(float f) {
        haloRadius = f;
    }

    /**
     * @param f
     */
    public void setRotation(double f) {
        rotation = f;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the pointPlacement (true => point placement, false => line placement)
     */
    public boolean isPointPlacement() {
        return pointPlacement;
    }

    /**
     * @param pointPlacement (true => point placement, false => line placement.)
     */
    public void setPointPlacement(boolean pointPlacement) {
        this.pointPlacement = pointPlacement;
    }

    /**
     * @return Returns the displacementX.
     */
    public double getDisplacementX() {
        return displacementX;
    }

    /**
     * @param displacementX The displacementX to set.
     */
    public void setDisplacementX(double displacementX) {
        this.displacementX = displacementX;
    }

    /**
     * @return Returns the displacementY.
     */
    public double getDisplacementY() {
        return displacementY;
    }

    /**
     * @param displacementY The displacementY to set.
     */
    public void setDisplacementY(double displacementY) {
        this.displacementY = displacementY;
    }

    /**
     * Getter for property fill.
     *
     * @return Value of property fill.
     */
    public Paint getFill() {
        return this.fill;
    }

    /**
     * Setter for property fill.
     *
     * @param fill New value of property fill.
     */
    public void setFill(Paint fill) {
        this.fill = fill;
    }
    
    /**
     *  only valid for a isPointPlacement=false (ie. a lineplacement)
     * @param displace in pixels
     */
    public void setPerpendicularOffset(int displace)
    {
    	perpendicularOffset = displace;
    }
    
    /**
     * only valid for a isPointPlacement=false (ie. a lineplacement)
     * @return displacement in pixels
     */
    public int getPerpendicularOffset()
    {
    	return perpendicularOffset;
    }
	
	

    /**
     * Getter for property composite.
     *
     * @return Value of property composite.
     */
    public Composite getComposite() {
        return this.composite;
    }

    /**
     * Setter for property composite.
     *
     * @param composite New value of property composite.
     */
    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    /**
     * Returns a string representation of this style.
     */
    public String toString() {
        return Classes.getShortClassName(this) + "[\"" + label + "\"]";
    }
    
        
        /**
         * Sets the style2D to be drawn underneath this text
         */
    public void setGraphic(Style2D s) {
    	graphic = s;
    }
    
   /**
     * gets the Style2D to be drawn underneath this text
     */
    public Style2D getGraphic() {
    	return graphic;
    }
    
    public Rectangle getGraphicDimensions() {
    	if (graphic instanceof MarkStyle2D) {
    	    return  ((MarkStyle2D)graphic).getTransformedShape(0f, 0f).getBounds(); 
    	} else if (graphic instanceof GraphicStyle2D) {
    		BufferedImage i = ((GraphicStyle2D)graphic).getImage();
    		return new Rectangle(i.getWidth(),i.getHeight());
    	} else if(graphic instanceof IconStyle2D) {
    	    final Icon icon = ((IconStyle2D) graphic).getIcon();
            return new Rectangle(icon.getIconWidth(), icon.getIconWidth());
    	} else {
    		throw new RuntimeException("Can't render graphic which is not a MarkStyle2D or a GraphicStyle2D");
    	}
    }
        
        
    
}
