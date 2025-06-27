/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Description;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.OtherText;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Provides a Java representation of an SLD TextSymbolizer that defines how text symbols should be rendered.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class TextSymbolizerImpl extends AbstractSymbolizer implements TextSymbolizer, Cloneable {
    private List<Font> fonts = new ArrayList<>(1);
    private final FilterFactory filterFactory;
    private FillImpl fill;
    private HaloImpl halo;
    private LabelPlacement placement;
    private Expression label = null;
    private Graphic graphic = null;
    private Expression priority = null;
    private Expression abxtract = null;
    private Expression description = null;
    private OtherText otherText = null;

    protected TextSymbolizerImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    /** Creates a new instance of DefaultTextSymbolizer */
    protected TextSymbolizerImpl(FilterFactory factory) {
        this(factory, null, null, null);
    }

    protected TextSymbolizerImpl(FilterFactory factory, Description desc, String name, Unit<Length> uom) {
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
    @Override
    public Fill getFill() {
        return fill;
    }

    /**
     * Setter for property fill.
     *
     * @param fill New value of property fill.
     */
    @Override
    public void setFill(org.geotools.api.style.Fill fill) {
        if (this.fill == fill) {
            return;
        }
        this.fill = FillImpl.cast(fill);
    }

    @Override
    public List<Font> fonts() {
        return fonts;
    }

    @Override
    public Font getFont() {
        return fonts.isEmpty() ? null : fonts.get(0);
    }

    @Override
    public void setFont(org.geotools.api.style.Font font) {
        if (this.fonts.size() == 1 && this.fonts.get(0) == font) {
            return; // no change
        }
        if (font != null) {
            if (this.fonts.isEmpty()) {
                this.fonts.add(font);
            } else {
                this.fonts.set(0, font);
            }
        }
    }

    /**
     * Setter for property font.
     *
     * @param font New value of property font.
     */
    public void addFont(Font font) {
        fonts.add(font);
    }

    /**
     * A halo fills an extended area outside the glyphs of a rendered text label to make the label easier to read over a
     * background.
     */
    @Override
    public Halo getHalo() {
        return halo;
    }

    /**
     * Setter for property halo.
     *
     * @param halo New value of property halo.
     */
    @Override
    public void setHalo(org.geotools.api.style.Halo halo) {
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
    @Override
    public Expression getLabel() {
        return label;
    }

    /**
     * Setter for property label.
     *
     * @param label New value of property label.
     */
    @Override
    public void setLabel(Expression label) {
        this.label = label;
    }

    /**
     * A pointPlacement specifies how a text element should be rendered relative to its geometric point.
     *
     * @return Value of property labelPlacement.
     */
    @Override
    public LabelPlacement getLabelPlacement() {
        return placement;
    }

    /**
     * Setter for property labelPlacement.
     *
     * @param labelPlacement New value of property labelPlacement.
     */
    @Override
    public void setLabelPlacement(org.geotools.api.style.LabelPlacement labelPlacement) {
        if (this.placement == labelPlacement) {
            return;
        }
        if (labelPlacement instanceof LinePlacement) {
            this.placement = LinePlacementImpl.cast(labelPlacement);
        } else {
            this.placement = PointPlacementImpl.cast(labelPlacement);
        }
    }

    /**
     * Accept a StyleVisitor to perform an operation on this symbolizer.
     *
     * @param visitor The StyleVisitor to accept.
     */
    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone. TODO: Need to complete the deep copy, currently only shallow copy.
     *
     * @return The deep copy clone.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // this should never happen.
        }
    }

    @Override
    public void setPriority(Expression priority) {
        if (this.priority == priority) {
            return;
        }
        this.priority = priority;
    }

    @Override
    public Expression getPriority() {
        return priority;
    }

    @Override
    public Graphic getGraphic() {
        return graphic;
    }

    @Override
    public void setGraphic(Graphic graphic) {
        if (this.graphic == graphic) {
            return;
        }
        this.graphic = graphic;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<TextSymbolizerImp property=");
        buf.append(getGeometryPropertyName());
        buf.append(" label=");
        buf.append(label);
        buf.append(">");
        buf.append(this.fonts);
        return buf.toString();
    }

    @Override
    public Expression getSnippet() {
        return abxtract;
    }

    @Override
    public void setSnippet(Expression abxtract) {
        this.abxtract = abxtract;
    }

    @Override
    public Expression getFeatureDescription() {
        return description;
    }

    @Override
    public void setFeatureDescription(Expression description) {
        this.description = description;
    }

    @Override
    public OtherText getOtherText() {
        return otherText;
    }

    @Override
    public void setOtherText(OtherText otherText) {
        this.otherText = otherText;
    }

    static TextSymbolizerImpl cast(org.geotools.api.style.Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        } else if (symbolizer instanceof TextSymbolizerImpl) {
            return (TextSymbolizerImpl) symbolizer;
        } else {
            org.geotools.api.style.TextSymbolizer textSymbolizer = (org.geotools.api.style.TextSymbolizer) symbolizer;
            TextSymbolizerImpl copy = new TextSymbolizerImpl();
            copy.setDescription(textSymbolizer.getDescription());
            copy.setFill(textSymbolizer.getFill());
            if (textSymbolizer.fonts() != null) copy.fonts().addAll(textSymbolizer.fonts());
            copy.setGeometryPropertyName(textSymbolizer.getGeometryPropertyName());
            copy.setHalo(textSymbolizer.getHalo());
            copy.setLabel(textSymbolizer.getLabel());
            copy.setLabelPlacement(textSymbolizer.getLabelPlacement());
            copy.setName(textSymbolizer.getName());
            copy.setUnitOfMeasure(textSymbolizer.getUnitOfMeasure());
            copy.setPriority(textSymbolizer.getPriority());
            copy.setGraphic(textSymbolizer.getGraphic());
            copy.setOtherText(textSymbolizer.getOtherText());
            copy.setFeatureDescription(textSymbolizer.getFeatureDescription());
            copy.setSnippet(textSymbolizer.getSnippet());
            if (textSymbolizer.getOptions() != null) copy.getOptions().putAll(textSymbolizer.getOptions());

            return copy;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (abxtract == null ? 0 : abxtract.hashCode());
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (fill == null ? 0 : fill.hashCode());
        result = prime * result + (filterFactory == null ? 0 : filterFactory.hashCode());
        result = prime * result + (fonts == null ? 0 : fonts.hashCode());
        result = prime * result + (graphic == null ? 0 : graphic.hashCode());
        result = prime * result + (halo == null ? 0 : halo.hashCode());
        result = prime * result + (label == null ? 0 : label.hashCode());
        result = prime * result + (otherText == null ? 0 : otherText.hashCode());
        result = prime * result + (placement == null ? 0 : placement.hashCode());
        result = prime * result + (priority == null ? 0 : priority.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        TextSymbolizerImpl other = (TextSymbolizerImpl) obj;
        if (abxtract == null) {
            if (other.abxtract != null) return false;
        } else if (!abxtract.equals(other.abxtract)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (fill == null) {
            if (other.fill != null) return false;
        } else if (!fill.equals(other.fill)) return false;
        if (filterFactory == null) {
            if (other.filterFactory != null) return false;
        } else if (!filterFactory.equals(other.filterFactory)) return false;
        if (fonts == null) {
            if (other.fonts != null) return false;
        } else if (!fonts.equals(other.fonts)) return false;
        if (graphic == null) {
            if (other.graphic != null) return false;
        } else if (!graphic.equals(other.graphic)) return false;
        if (halo == null) {
            if (other.halo != null) return false;
        } else if (!halo.equals(other.halo)) return false;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        if (otherText == null) {
            if (other.otherText != null) return false;
        } else if (!otherText.equals(other.otherText)) return false;
        if (placement == null) {
            if (other.placement != null) return false;
        } else if (!placement.equals(other.placement)) return false;
        if (priority == null) {
            if (other.priority != null) return false;
        } else if (!priority.equals(other.priority)) return false;
        return true;
    }
}
