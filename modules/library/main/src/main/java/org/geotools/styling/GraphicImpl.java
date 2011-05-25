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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.AnchorPoint;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * Direct implementation of Graphic.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 *
 * @source $URL$
 * @version $Id$
 */
public class GraphicImpl implements Graphic, Cloneable {
    /** The logger for the default core module. */
    //private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    
    private final List<GraphicalSymbol> graphics = new ArrayList<GraphicalSymbol>();
    private AnchorPointImpl anchor;
    private Expression gap;
    private Expression initialGap;
    
    private FilterFactory filterFactory;
    private String geometryPropertyName = "";
    private Expression rotation = null;
    private Expression size = null;
    private DisplacementImpl displacement = null;
    private Expression opacity = null;

    /**
     * Creates a new instance of DefaultGraphic
     */
    protected GraphicImpl() {
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints())); 
    }

    public GraphicImpl(FilterFactory factory) {
        this(factory,null,null,null);
    }
    
    public GraphicImpl(FilterFactory factory, AnchorPoint anchor,Expression gap, Expression initialGap) {
        filterFactory = factory;
        this.anchor = AnchorPointImpl.cast(anchor);
        
        if(gap == null) this.gap = ConstantExpression.constant(0);
        else this.gap = gap;
        if(initialGap == null) this.initialGap = ConstantExpression.constant(0);
        else this.initialGap = initialGap;
        
    }

    @Deprecated
    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    public List<GraphicalSymbol> graphicalSymbols() {
        return graphics;
    }

    /**
     * Provides a list of external graphics which can be used to represent this
     * graphic. Each one should be an equivalent representation but in a
     * different format. If none are provided, or if none of the formats are
     * supported, then the list of Marks should be used instead.
     *
     * @return An array of ExternalGraphics objects which should be equivalents
     *         but in different formats.  If null is returned use getMarks
     *         instead.
     */
    @Deprecated
    public ExternalGraphic[] getExternalGraphics() {        
        Collection<ExternalGraphic> exts = new ArrayList<ExternalGraphic>();
        
        for(GraphicalSymbol s : graphics){
            if(s instanceof ExternalGraphic){
                exts.add((ExternalGraphic) s);
            }
        }
        
        return exts.toArray(new ExternalGraphic[0]);
    }

    @Deprecated
    public void setExternalGraphics(ExternalGraphic[] externalGraphics) {
    	Collection<GraphicalSymbol> currExternalGraphics = new ArrayList<GraphicalSymbol>();
    	for(GraphicalSymbol s : graphics){
            if (s instanceof ExternalGraphic){
                currExternalGraphics.add(s);
            }
        }
    	graphics.removeAll(currExternalGraphics);
        
        for(ExternalGraphic g : externalGraphics){
            graphics.add(g);
        }
    }

    @Deprecated
    public void addExternalGraphic(ExternalGraphic externalGraphic) {
        graphics.add(externalGraphic);
    }

    /**
     * Provides a list of suitable marks which can be used to represent this
     * graphic. These should only be used if no ExternalGraphic is provided,
     * or if none of the external graphics formats are supported.
     *
     * @return An array of marks to use when displaying this Graphic. By
     *         default, a "square" with 50% gray fill and black outline with a
     *         size of 6 pixels (unless a size is specified) is provided.
     */
    @Deprecated
    public Mark[] getMarks() {        
        Collection<Mark> exts = new ArrayList<Mark>();
        
        for(GraphicalSymbol s : graphics){
            if(s instanceof Mark){
                exts.add((Mark) s);
            }
        }
        
        return exts.toArray(new Mark[0]);
    }

    @Deprecated
    public void setMarks(Mark[] marks) {
    	Collection<GraphicalSymbol> currMarks = new ArrayList<GraphicalSymbol>();
    	for(GraphicalSymbol s : graphics){
            if (s instanceof Mark){
                currMarks.add(s);
            }
        }
    	graphics.removeAll(currMarks);
        
        for(Mark g : marks){
            graphics.add(g);
        }
    }

    @Deprecated
    public void addMark(Mark mark) {
        graphics.add(mark);
    }

    /**
     * Provides a list of all the symbols which can be used to represent this
     * graphic
     * <p>
     * A symbol is an ExternalGraphic, Mark or any other object which
     * implements the Symbol interface. These are returned in the order they
     * were set.
     * <p>
     * This class operates as a "view" on getMarks() and getExternalGraphics()
     * with the added magic that if nothing has been set ever a single default
     * MarkImpl will be provided. This default will not effect the internal
     * state it is only there as a sensible default for rendering.
     *
     * @return An array of symbols to use when displaying this Graphic. By
     *         default, a "square" with 50% gray fill and black outline with a
     *         size of 6 pixels (unless a size is specified) is provided.
     */
    @Deprecated
    public Symbol[] getSymbols() {
        ArrayList<Symbol> symbols = new ArrayList<Symbol>();
        for( GraphicalSymbol graphic : graphics ){
            if( graphic instanceof Symbol ){
                symbols.add( (Symbol) graphic );
            }
        }
        return symbols.toArray(new Symbol[0]);
    }
    
//    public List<Symbol> graphicalSymbols() {
//        return symbols;
//    }

    @Deprecated
    public void setSymbols(Symbol[] symbols) {
        graphics.clear();
        
        for(Symbol g : symbols){
            graphics.add(g);
        }
    }

    @Deprecated
    public void addSymbol(Symbol symbol) {
        graphics.add(symbol);
    }
    
    public AnchorPointImpl getAnchorPoint() {
        return anchor;
    }

    public void setAnchorPoint(org.geotools.styling.AnchorPoint anchor) {
        this.anchor = AnchorPointImpl.cast( anchor );
    }

    public void setAnchorPoint(org.opengis.style.AnchorPoint anchorPoint) {
        this.anchor = AnchorPointImpl.cast( anchorPoint );
    }

    /**
     * This specifies the level of translucency to use when rendering the graphic.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with
     * 0.0 representing totally transparent and 1.0 representing totally
     * opaque, with a linear scale of translucency for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is
     * 1.0 (opaque).
     *
     * @return The opacity of the Graphic, where 0.0 is completely transparent
     *         and 1.0 is completely opaque.
     */
    public Expression getOpacity() {
        return opacity;
    }

    /**
     * This parameter defines the rotation of a graphic in the clockwise
     * direction about its centre point in decimal degrees. The value encoded
     * as a floating point number.
     *
     * @return The angle of rotation in decimal degrees. Negative values
     *         represent counter-clockwise rotation.  The default is 0.0 (no
     *         rotation).
     */
    public Expression getRotation() {
        return rotation;
    }

    /**
     * This paramteter gives the absolute size of the graphic in pixels encoded
     * as a floating point number.
     * 
     * <p>
     * The default size of an image format (such as GIFD) is the inherent size
     * of the image.  The default size of a format without an inherent size
     * (such as SVG) is defined to be 16 pixels in height and the
     * corresponding aspect in width.  If a size is specified, the height of
     * the graphic will be scaled to that size and the corresponding aspect
     * will be used for the width.
     * </p>
     *
     * @return The size of the graphic, the default is context specific.
     *         Negative values are not possible.
     */
    public Expression getSize() {
        return size;
    }

    public DisplacementImpl getDisplacement() {
        return displacement;
    }

    public Expression getInitialGap() {
        return initialGap;
    }
    
    public void setInitialGap( Expression initialGap ){
        this.initialGap = initialGap;
    }

    public Expression getGap() {
        return gap;
    }
    
    public void setGap(Expression gap) {
        this.gap = gap;
    }
    
    public void setDisplacement(org.opengis.style.Displacement offset) {
        this.displacement = DisplacementImpl.cast( offset );
    }

    /**
     * Graphic opacity.
     * 
     * @param opacity New value of property opacity.
     */
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    @Deprecated
    public void setOpacity(double opacity) {
        setOpacity(filterFactory.literal(opacity));
    }

    /**
     * Setter for property rotation.
     *
     * @param rotation New value of property rotation.
     */
    public void setRotation(Expression rotation) {
        this.rotation = rotation;
    }

    @Deprecated
    public void setRotation(double rotation) {
        setRotation(filterFactory.literal(rotation));
    }

    /**
     * Setter for property size.
     *
     * @param size New value of property size.
     */
    public void setSize(Expression size) {
        this.size = size;
    }

    @Deprecated
    public void setSize(int size) {
        setSize(filterFactory.literal(size));
    }

    @Deprecated
    public void setGeometryPropertyName(String name) {
        geometryPropertyName = name;
    }

    /**
     * Getter for property geometryPropertyName.
     *
     * @return Value of property geometryPropertyName.
     */
    public java.lang.String getGeometryPropertyName() {
        return geometryPropertyName;
    }

    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit((org.opengis.style.GraphicStroke)this, data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }

    
    /**
     * Creates a deep copy clone.
     *
     * @return The deep copy clone.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object clone() {
        GraphicImpl clone;

        try {
            clone = (GraphicImpl) super.clone();
            clone.graphics.clear();
            clone.graphics.addAll(graphics);
            
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    /**
     * Override of hashcode
     *
     * @return The hashcode.
     */
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (geometryPropertyName != null) {
            result = (PRIME * result) + geometryPropertyName.hashCode();
        }

        if (graphics != null) {
            result = (PRIME * result) + graphics.hashCode();
        }

        if (rotation != null) {
            result = (PRIME * result) + rotation.hashCode();
        }

        if (size != null) {
            result = (PRIME * result) + size.hashCode();
        }

        if (opacity != null) {
            result = (PRIME * result) + opacity.hashCode();
        }
        
//        if (gap != null) {
//            result = (PRIME * result) + gap.hashCode();
//        }
//        
//        if (initialGap != null) {
//            result = (PRIME * result) + initialGap.hashCode();
//        }

        return result;
    }

    /**
     * Compares this GraphicImpl with another for equality.
     * 
     * <p>
     * Two graphics are equal if and only if they both have the same geometry
     * property name and the same list of symbols and the same rotation, size
     * and opacity.
     * </p>
     *
     * @param oth The other GraphicsImpl to compare with.
     *
     * @return True if this is equal to oth according to the above conditions.
     */
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof GraphicImpl) {
            GraphicImpl other = (GraphicImpl) oth;

            return Utilities.equals(this.geometryPropertyName,
                other.geometryPropertyName)
            && Utilities.equals(this.size, other.size)
            && Utilities.equals(this.rotation, other.rotation)
            && Utilities.equals(this.opacity, other.opacity)
            &&    Arrays.equals(this.getMarks(), other.getMarks() )
            &&    Arrays.equals( this.getExternalGraphics(), other.getExternalGraphics() )
            &&    Arrays.equals( this.getSymbols(), other.getSymbols() ); 
            
            
//                        return 
//            Utilities.equals(this.geometryPropertyName,other.geometryPropertyName)
//            && Utilities.equals(this.size, other.size)
//            && Utilities.equals(this.rotation, other.rotation)
//            && Utilities.equals(this.opacity, other.opacity)
//            && Utilities.equals(this.graphics, other.graphics )
//            && Utilities.equals(this.anchor, other.anchor)
//            && Utilities.equals(this.gap, other.gap)
//            && Utilities.equals(this.initialGap, other.initialGap);       
            
        }

        return false;
    }

    static GraphicImpl cast(org.opengis.style.Graphic graphic) {
        if( graphic == null){
            return null;
        }
        else if ( graphic instanceof GraphicImpl){
            return (GraphicImpl) graphic;
        }
        else {
            GraphicImpl copy = new GraphicImpl();            
            copy.setAnchorPoint( graphic.getAnchorPoint() );
            copy.setDisplacement( graphic.getDisplacement() );
            if( graphic.graphicalSymbols() != null ){
                for ( GraphicalSymbol item : graphic.graphicalSymbols()) {
                    if( item instanceof org.opengis.style.ExternalGraphic){
                        copy.graphicalSymbols().add( ExternalGraphicImpl.cast( item ));
                    }
                    else if( item instanceof org.opengis.style.Mark){
                        copy.graphicalSymbols().add( MarkImpl.cast( item ));
                    }
                }
            }
            return copy;
        }
    }

}
