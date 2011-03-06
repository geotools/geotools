/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.Collections;
import java.util.List;

import org.geotools.filter.ConstantExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;


/**
 * A Graphic is a "graphical symbol" with an inherent shape, color(s), and
 * possibly size.
 *
 * <p>
 * A "graphic" can very informally be defined as "a little picture" and can be
 * of either a raster or vector graphic source type.  The term graphic is used
 * since the term "symbol" is similar to "symbolizer" which is used in a
 * difference context in SLD. The graphical symbol to display can be provided
 * either as an external graphical resource or as a Mark.<br>
 * Multiple external URLs and marks can be referenced with the proviso that
 * they all provide equivalent graphics in different formats. The 'hot spot'
 * to use for positioning the rendering at a point must either be inherent
 * from the external format or be defined to be the  "central point" of the
 * graphic.
 * </p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="Graphic"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "Graphic" specifies or refers to a "graphic symbol" with inherent
 *       shape, size, and coloring.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *         &lt;xsd:element ref="sld:ExternalGraphic"/&gt;
 *         &lt;xsd:element ref="sld:Mark"/&gt;
 *       &lt;/xsd:choice&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="sld:Opacity" minOccurs="0"/&gt;
 *         &lt;xsd:element ref="sld:Size" minOccurs="0"/&gt;
 *         &lt;xsd:element ref="sld:Rotation" minOccurs="0"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * </p>
 *
 * <p>
 * Renderers can ue this information when displaying styled features, though it
 * must be remembered that not all renderers will be able to fully represent
 * strokes as set out by this interface.  For example, opacity may not be
 * supported.
 * </p>
 *
 * <p>
 * Notes:
 *
 * <ul>
 * <li>
 * The graphical parameters and their values are derived from SVG/CSS2
 * standards with names and semantics which are as close as possible.
 * </li>
 * </ul>
 * </p>
 *
 * @task REVISIT: There are no setter methods in this interface, is this a
 *       problem?
 * @source $URL$
 */
public interface Graphic extends GraphicLegend,
                                 org.opengis.style.Graphic,
                                 org.opengis.style.GraphicFill, 
                                 org.opengis.style.GraphicStroke {
    /**
     * A default Graphic instance.
     * <p>
     * For some attributes the standard does not define a default, so a
     * reasonable value is supplied.
     * </p>
     */
    public static final Graphic DEFAULT = new ConstantGraphic() {
            public ExternalGraphic[] getExternalGraphics() {
                return ExternalGraphic.EXTERNAL_GRAPHICS_EMPTY;
            }

            public Mark[] getMarks() {
                return Mark.MARKS_EMPTY;
            }

            public Symbol[] getSymbols() {
                return Symbol.SYMBOLS_EMPTY;
            }

            public List<GraphicalSymbol> graphicalSymbols() {
	            return Collections.emptyList();
            }
            public Expression getOpacity() {
                return ConstantExpression.ONE;
            }

            public Expression getSize() {
                // default size is unknown, it depends on the target
                return Expression.NIL;
            }

            public Displacement getDisplacement() {
                return Displacement.DEFAULT;
            }

            public Expression getRotation() {
                return ConstantExpression.ZERO;
            }

            public String getGeometryPropertyName() {
                return "";
            }

        };

    /**
     * Indicates an absense of graphic.
     * <p>
     * This value is used to indicate that the Graphics based opperation
     * should be skipped. Aka this is used by Stroke.Stroke as place holders
     * for GRAPHIC_FILL and GRAPHIC_STROKE.
     * </p>
     */
    public static final Graphic NULL = new ConstantGraphic() {
            public ExternalGraphic[] getExternalGraphics() {
                return ExternalGraphic.EXTERNAL_GRAPHICS_EMPTY;
            }

            public Mark[] getMarks() {
                return Mark.MARKS_EMPTY;
            }

            public Symbol[] getSymbols() {
                return Symbol.SYMBOLS_EMPTY;
            }
            public List<GraphicalSymbol> graphicalSymbols() {
	            return Collections.emptyList();
            }
            public Expression getOpacity() {
                return ConstantExpression.NULL;
            }

            public Expression getSize() {
                return ConstantExpression.NULL;
            }

            public Displacement getDisplacement() {
                return Displacement.NULL;
            }

            public Expression getRotation() {
                return ConstantExpression.NULL;
            }

            public String getGeometryPropertyName() {
                return "";
            }

        };

    /**
     * List of all symbols used to represent this graphic. 
     * @return List of ExternalGraphic or Mark in the order provided.
     */
    List<GraphicalSymbol> graphicalSymbols();
        
    /**
     * Provides a list of external graphics which can be used to represent this
     * graphic. Each one should be an equivalent representation but in a
     * different format. If none are provided, or if none of the formats are
     * supported, then the list of Marks should be used instead.
     *
     * @return An array of ExternalGraphics objects which should be equivalents
     *         but in different formats.  If null is returned, use getMarks
     *         instead.
     *
     * @task REVISIT: The following may be a handy extra to have in this
     *       interface. public ExternalGraphic getExternalGraphic(String
     *       formats); return the first external graphic to match one of the
     *       given formats
     * 
     * @deprecated this method is replaced by a set : graphicalSymbols
     */
    ExternalGraphic[] getExternalGraphics();

    /**
     * @param externalGraphics 
     * @deprecated Please use graphicalSymbols().clear(); and graphicalSymbols().addAll( externalGraphics )
     */
    void setExternalGraphics(ExternalGraphic[] externalGraphics);

    /**
     * @deprecated Please use graphicalSymbols().add( externalGraphic )
     */
    void addExternalGraphic(ExternalGraphic externalGraphic);

    /**
     * Provides a list of suitable marks which can be used to represent this
     * graphic. These should only be used if no ExternalGraphic is provided,
     * or if none of the external graphics formats are supported.
     *
     * @return An array of marks to use when displaying this Graphic.  By
     *         default, a "square" with 50% gray fill and black outline with a
     *         size of 6 pixels (unless a size is specified) is provided.
     * 
     * @deprecated Please use graphicalSymbols()
     */
    Mark[] getMarks();

    /**
     * @deprecated Please use graphicSymbols().addAll()
     */
    void setMarks(Mark[] marks);

    /**
     * @deprecated Please use grpahicSymbols().add( Mark )
     */
    void addMark(Mark mark);

    /**
     * Provides a list of all the symbols which can be used to represent this
     * graphic. A symbol is an ExternalGraphic, Mark or any other object which
     * implements the Symbol interface. These are returned in the order they
     * were set.
     *
     * @return An array of symbols to use when displaying this Graphic.  By
     *         default, a "square" with 50% gray fill and black outline with a
     *         size of 6 pixels (unless a size is specified) is provided.
     * 
     * @deprecated Please use graphicalSymbols
     */
    Symbol[] getSymbols();
    
    /**
     * @deprecated symbolizers and underneath classes will be immutable in 2.6.x
     */
    @Deprecated
    void setSymbols(Symbol[] symbols);

    /**
     * @deprecated symbolizers and underneath classes are immutable
     */
    @Deprecated
    void addSymbol(Symbol symbol);

    /**
     * Location inside of the Graphic (or Label) to position the main-geometry point.
     * <p>
     * The coordinates are provided as 0.0 to 1.0 range amounting to a percentage
     * of the graphic width/height. So the default of 0.5/0.5 indicates that the
     * graphic would be centered.
     * <p>
     * Please keep in mind that a system may shuffel things around a bit in order
     * to prevent graphics from overlapping (so this AnchorPoint is only a hint
     * about how things should be if there is enough room).
     *
     * @return AnchorPoint , if null should use a default point X=0.5 Y=0.5
     */
    public AnchorPoint getAnchorPoint();
    
    /**
     * Anchor point (expressed as an x/y percentage of the graphic size).
     * 
     * @param anchorPoint
     */
    public void setAnchorPoint(org.opengis.style.AnchorPoint anchorPoint);
    
    /**
     * This specifies the level of translucency to use when rendering the  graphic.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with
     * 0.0 representing totally transparent and 1.0 representing totally
     * opaque, with a linear scale of translucency for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity.   The default value is
     * 1.0 (opaque).
     *
     * @return The opacity of the Graphic, where 0.0 is completely  transparent
     *         and 1.0 is completely opaque.
     */
    Expression getOpacity();

    /**
     * @param opacity opacity between 0 and 1
     */
    void setOpacity(Expression opacity);

    /**
     * This paramteter gives the absolute size of the graphic in pixels encoded
     * as a floating point number.
     *
     * <p>
     * The default size of an image format (such as GIFD) is the inherent size
     * of the image.  The default size of a format without an inherent size
     * (such as SVG) is defined to be 16 pixels in height and the
     * corresponding aspect in width. If a size is specified, the height of
     * the graphic will be scaled to that size and the corresponding aspect
     * will be used for the width.
     * </p>
     *
     * @return The size of the graphic.  The default is context specific.
     *         Negative values are not possible.
     */
    Expression getSize();

    /**
     * @param size Size of graphic
     */
    void setSize(Expression size);

    /**
     * @return Offset of graphic
     */
    Displacement getDisplacement();

    /**
     * @param offset Amount to offset graphic
     */
    void setDisplacement(org.opengis.style.Displacement offset);

    /**
     * This parameter defines the rotation of a graphic in the clockwise
     * direction about its centre point in decimal degrees.   The value
     * encoded as a floating point number.
     *
     * @return The angle of rotation in decimal degrees.  Negative values
     *         represent counter-clockwise rotation. The default is 0.0 (no
     *         rotation).
     */
    Expression getRotation();

    /**
     * 
     * @param rotation
     */
    void setRotation(Expression rotation);

    /**
     * Getter for property geometryPropertyName.
     *
     * @return Value of property geometryPropertyName.
     * @deprecated Please use symbolizer geometry expresion
     */
    String getGeometryPropertyName();

    /**
     * Setter for property geometryPropertyName.
     *
     * @param geometryPropertyName New value of property geometryPropertyName.
     * @deprecated Please use symbolizer geometry expression
     */
    void setGeometryPropertyName(String geometryPropertyName);

    Expression getGap();
    
    void setGap(Expression gap );
    
    Expression getInitialGap();
    
    void setInitialGap( Expression initialGap );
    
    /**
     * accepts a StyleVisitor - used by xmlencoder and other packages which
     * need to walk the style tree
     *
     * @param visitor - the visitor object
     */
    void accept(org.geotools.styling.StyleVisitor visitor);
}


abstract class ConstantGraphic implements Graphic {
    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Graphic may not be modified");
    }
    
    public void setDisplacement(org.opengis.style.Displacement offset) {
        cannotModifyConstant();
    }

    public void setExternalGraphics(ExternalGraphic[] externalGraphics) {
        cannotModifyConstant();
    }

    public void addExternalGraphic(ExternalGraphic externalGraphic) {
        cannotModifyConstant();
    }

    public void setMarks(Mark[] marks) {
        cannotModifyConstant();
    }

    public void addMark(Mark mark) {
        cannotModifyConstant();
    }

    public void setGap(Expression gap) {
        cannotModifyConstant();
    }

    public void setInitialGap(Expression initialGap) {
        cannotModifyConstant();
    }

    public void setSymbols(Symbol[] symbols) {
        cannotModifyConstant();
    }

    public void addSymbol(Symbol symbol) {
        cannotModifyConstant();
    }

    public void setOpacity(Expression opacity) {
        cannotModifyConstant();
    }

    public void setSize(Expression size) {
        cannotModifyConstant();
    }

    public void setRotation(Expression rotation) {
        cannotModifyConstant();
    }

    public void setGeometryPropertyName(String geometryPropertyName) {
        cannotModifyConstant();
    }

    public void setAnchorPoint(AnchorPoint anchor) {
        cannotModifyConstant();
    }
    
    public Object accept(org.opengis.style.StyleVisitor visitor, Object data) {
        return visitor.visit((org.opengis.style.GraphicStroke)this,data);
    }
    
    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    public List<GraphicalSymbol> graphicalSymbols() {
        return Collections.emptyList();
    }

    public AnchorPoint getAnchorPoint() {
        return org.geotools.styling.AnchorPoint.DEFAULT;
    }
    
    public void setAnchorPoint(org.opengis.style.AnchorPoint anchorPoint) {
        cannotModifyConstant();
    }
    
    public Expression getGap(){
        return ConstantExpression.constant(0);
    }
    
    public Expression getInitialGap(){
        return ConstantExpression.constant(0);
    }
    
}
