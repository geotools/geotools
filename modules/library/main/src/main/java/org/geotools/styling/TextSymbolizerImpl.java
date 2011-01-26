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

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * Provides a Java representation of an SLD TextSymbolizer that defines how
 * text symbols should be rendered.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @source $URL$
 * @version $Id$
 */
public class TextSymbolizerImpl extends AbstractSymbolizer implements TextSymbolizer2, Cloneable {
    
    private Font font;
    
    private final FilterFactory filterFactory;
    private FillImpl fill;
    private HaloImpl halo;
    private LabelPlacement placement;
    private Expression label = null;
    private Graphic graphic = null;
    private Expression priority = null;
    private HashMap<String,String> optionsMap = null; //null=nothing in it
    private Expression abxtract = null;
    private Expression description = null;
    private OtherText otherText = null;

    protected TextSymbolizerImpl() {
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()) );
    }
    
    /**
     * Creates a new instance of DefaultTextSymbolizer
     */
    protected TextSymbolizerImpl( FilterFactory factory ) {
        this(factory,null,null,null);
    }
    
    protected TextSymbolizerImpl( FilterFactory factory, Description desc, String name, Unit<Length> uom ) {
        super(name, desc, (Expression) null, uom);
        this.filterFactory = factory;
        fill = new FillImpl(factory);
        fill.setColor(filterFactory.literal("#000000")); // default text fill is black
        halo = null;
        placement = new PointPlacementImpl();
    }
    

    /**
     * Returns the fill to be used to fill the text when rendered.
     *
     * @return The fill to be used.
     */
    public FillImpl getFill() {
        return fill;
    }

    /**
     * Setter for property fill.
     *
     * @param fill New value of property fill.
     */
    public void setFill(org.opengis.style.Fill fill) {
        if (this.fill == fill) {
            return;
        }
        this.fill = FillImpl.cast( fill );
    }

    public Font getFont() {
        return font;
    }
    
    public void setFont( org.opengis.style.Font font ){
        if( this.font == font ){
            return;
        }
        this.font = FontImpl.cast( font );
    }
    /**
     * Returns a device independent Font object that is to be used to render
     * the label.
     *
     * @return Device independent Font object to be used to render the label.
     */
    @Deprecated
    public  org.geotools.styling.Font[] getFonts() {
        
        if(font == null){
            return new org.geotools.styling.Font[0];
        }else{
            return new org.geotools.styling.Font[]{(org.geotools.styling.Font)font} ;
        }
    }

    /**
     * Setter for property font.
     *
     * @param font New value of property font.
     */
    @Deprecated
    public void addFont(org.geotools.styling.Font font) {
        this.font = font;
    }

    /**
     * Sets the list of fonts in the TextSymbolizer to the provided array of
     * Fonts.
     *
     * @param fonts The array of fonts to use in the symbolizer.
     */
    @Deprecated
    public void setFonts(org.geotools.styling.Font[] fonts) {
        
        if(fonts != null && fonts.length >0){
            this.font = fonts[0]; 
        }else{
            this.font = null;
        }
    }

    /**
     * A halo fills an extended area outside the glyphs of a rendered text
     * label to make the label easier to read over a background.
     *
     */
    public HaloImpl getHalo() {
        return halo;
    }

    /**
     * Setter for property halo.
     *
     * @param halo New value of property halo.
     */
    public void setHalo(org.opengis.style.Halo halo) {
        if (this.halo == halo) {
            return;
        }
        this.halo = HaloImpl.cast(halo);
    }

    /**
     * Returns the label expression.
     *
     * @return Label expression.
     */
    public Expression getLabel() {
        return label;
    }

    /**
     * Setter for property label.
     *
     * @param label New value of property label.
     */
    public void setLabel(Expression label) {
        this.label = label;
    }

    /**
     * A pointPlacement specifies how a text element should be rendered
     * relative to its geometric point.
     *
     * @return Value of property labelPlacement.
     */
    @Deprecated
    public LabelPlacement getPlacement() {
        return getLabelPlacement();
    }

    /**
     * Setter for property labelPlacement.
     *
     * @param labelPlacement New value of property labelPlacement.
     * @deprecated Use setLabelPlacement
     */
    public void setPlacement(LabelPlacement labelPlacement) {
        setLabelPlacement( labelPlacement );
    }

    /**
     * A pointPlacement specifies how a text element should be rendered
     * relative to its geometric point.
     *
     * @return Value of property labelPlacement.
     *
     */
    public LabelPlacement getLabelPlacement() {
        return placement;
    }

    /**
     * Setter for property labelPlacement.
     *
     * @param labelPlacement New value of property labelPlacement.
     */

    public void setLabelPlacement( org.opengis.style.LabelPlacement labelPlacement) {
        if (this.placement == labelPlacement) {
            return;
        }
        if( labelPlacement instanceof LinePlacement){
            this.placement = LinePlacementImpl.cast( labelPlacement );
        }
        else {
            this.placement = PointPlacementImpl.cast( labelPlacement );
        }
    }

    /**
     * Accept a StyleVisitor to perform an operation on this symbolizer.
     *
     * @param visitor The StyleVisitor to accept.
     */
    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * Creates a deep copy clone.   TODO: Need to complete the deep copy,
     * currently only shallow copy.
     *
     * @return The deep copy clone.
     *
     * @throws AssertionError DOCUMENT ME!
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // this should never happen.
        }
    }

    public void setPriority(Expression priority) {
        if (this.priority == priority) {
            return;
        }
        this.priority = priority;
    }

    public Expression getPriority() {
        return priority;
    }

    public void addToOptions(String key, String value) {
        getOptions().put(key, value.trim());
    }

    public String getOption(String key) {
        if (optionsMap == null) {
            return null;
        }

        return (String) optionsMap.get(key);
    }

    public Map<String,String> getOptions() {
        if (optionsMap == null) {
            optionsMap = new HashMap<String,String>();
        }
        return optionsMap;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        if (this.graphic == graphic) {
            return;
        }
        this.graphic = graphic;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<TextSymbolizerImp property=");
        buf.append( getGeometryPropertyName() );
        buf.append( " label=");
        buf.append( label );
        buf.append(">");
        buf.append( this.font );
        return buf.toString();
    }
    
    public Expression getSnippet() {
        return abxtract;
    }
    
    public void setSnippet(Expression abxtract) {
        this.abxtract = abxtract;
    }
    
    public Expression getFeatureDescription() {
        return description;
    }
    
    public void setFeatureDescription(Expression description) {
        this.description = description;
    }
    
    public OtherText getOtherText() {
        return otherText;
    }
    
    public void setOtherText(OtherText otherText) {
        this.otherText = otherText;
    }

    static TextSymbolizerImpl cast(org.opengis.style.Symbolizer symbolizer) {
        if( symbolizer == null ){
            return null;
        }
        else if (symbolizer instanceof TextSymbolizerImpl){
            return (TextSymbolizerImpl) symbolizer;
        }
        else {
            org.opengis.style.TextSymbolizer textSymbolizer = (org.opengis.style.TextSymbolizer) symbolizer;
            TextSymbolizerImpl copy = new TextSymbolizerImpl();
            copy.setDescription( textSymbolizer.getDescription());
            copy.setFill( textSymbolizer.getFill() );
            copy.setFont( textSymbolizer.getFont() );
            copy.setGeometryPropertyName( textSymbolizer.getGeometryPropertyName() );
            copy.setHalo(textSymbolizer.getHalo() );
            copy.setLabel(textSymbolizer.getLabel() );
            copy.setLabelPlacement(textSymbolizer.getLabelPlacement() );
            copy.setName(textSymbolizer.getName() );
            copy.setUnitOfMeasure( textSymbolizer.getUnitOfMeasure() );
            
            return copy;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((abxtract == null) ? 0 : abxtract.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((fill == null) ? 0 : fill.hashCode());
        result = prime * result + ((filterFactory == null) ? 0 : filterFactory.hashCode());
        result = prime * result + ((font == null) ? 0 : font.hashCode());
        result = prime * result + ((graphic == null) ? 0 : graphic.hashCode());
        result = prime * result + ((halo == null) ? 0 : halo.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((optionsMap == null) ? 0 : optionsMap.hashCode());
        result = prime * result + ((otherText == null) ? 0 : otherText.hashCode());
        result = prime * result + ((placement == null) ? 0 : placement.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TextSymbolizerImpl other = (TextSymbolizerImpl) obj;
        if (abxtract == null) {
            if (other.abxtract != null)
                return false;
        } else if (!abxtract.equals(other.abxtract))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (fill == null) {
            if (other.fill != null)
                return false;
        } else if (!fill.equals(other.fill))
            return false;
        if (filterFactory == null) {
            if (other.filterFactory != null)
                return false;
        } else if (!filterFactory.equals(other.filterFactory))
            return false;
        if (font == null) {
            if (other.font != null)
                return false;
        } else if (!font.equals(other.font))
            return false;
        if (graphic == null) {
            if (other.graphic != null)
                return false;
        } else if (!graphic.equals(other.graphic))
            return false;
        if (halo == null) {
            if (other.halo != null)
                return false;
        } else if (!halo.equals(other.halo))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (optionsMap == null) {
            if (other.optionsMap != null)
                return false;
        } else if (!optionsMap.equals(other.optionsMap))
            return false;
        if (otherText == null) {
            if (other.otherText != null)
                return false;
        } else if (!otherText.equals(other.otherText))
            return false;
        if (placement == null) {
            if (other.placement != null)
                return false;
        } else if (!placement.equals(other.placement))
            return false;
        if (priority == null) {
            if (other.priority != null)
                return false;
        } else if (!priority.equals(other.priority))
            return false;
        return true;
    }

}
