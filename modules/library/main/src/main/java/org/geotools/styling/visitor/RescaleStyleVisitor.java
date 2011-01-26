/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import static org.geotools.styling.TextSymbolizer.*;

import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbol;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * This is a style visitor that will produce a copy of the provided
 * style rescaled by a provided factor.
 * <p>
 * The provided scale will be use to modify all line widths, font sizes and
 * so forth. We may need to go the extra distance and play with the min/max
 * scale on rules, and if there is any DPI specific madness going on we are
 * going to cry.
 * <p>
 * According to the specification we are supposed to use environmental variables
 * to make our styles render in a resolution independent manner. The current
 * GeoTools environment variable visitor only does processing for <b>mapscale</b>
 * but does not have a dpi substitution. On the plus side this visitor accepts
 * a general Expression and you are free to use an environmental variable expression
 * in order to make sure a normal base style is tweaked in all the right spots.
 * <p>
 * 
 * @author Jody Garnett (Refractions Research)
 *
 * @source $URL$
 */
public class RescaleStyleVisitor extends DuplicatingStyleVisitor {
    
    /**
     * This is the scale used as a multiplication factory for everything that
     * has a size.
     */
    private Expression scale;

    public RescaleStyleVisitor( double scale ){
        this( CommonFactoryFinder.getFilterFactory2(null), scale );
    }
    
    public RescaleStyleVisitor( Expression scale){
        this( CommonFactoryFinder.getFilterFactory2(null), scale );
    }
    
    public RescaleStyleVisitor(FilterFactory2 filterFactory, double scale) {
        this( filterFactory, filterFactory.literal(scale));
    }

    public RescaleStyleVisitor(FilterFactory2 filterFactory, Expression scale) {
        super( CommonFactoryFinder.getStyleFactory( null ), filterFactory );
        this.scale = scale;
    }
    /**
     * Used to rescale the provided expr.
     * <p>
     * We do optimize the case where the provided expression is a literal; no
     * sense doing a calculation each time if we don't have to.
     * 
     * @param expr
     * @return expr multiplied by the provided scale
     */
    protected Expression rescale( Expression expr ) {
        if(expr == null) {
            return null;
        }
        if(expr == Expression.NIL) {
            return Expression.NIL;
        }
        
        Expression rescale = ff.multiply( scale, expr );
        if( expr instanceof Literal && scale instanceof Literal){
            double constant = (double) rescale.evaluate(null, Double.class);
            return ff.literal(constant);
        }
        return rescale;
    }
    
    /**
     * Increase stroke width.
     * <p>
     * Based on feedback we may need to change the dash array as well.
     * <p>
     */
    public void visit(org.geotools.styling.Stroke stroke) {
        Stroke copy = sf.getDefaultStroke();
        copy.setColor( copy(stroke.getColor()));
        copy.setDashArray( rescale(stroke.getDashArray()));
        copy.setDashOffset( rescale(stroke.getDashOffset()));
        copy.setGraphicFill( copy(stroke.getGraphicFill()));
        copy.setGraphicStroke( copy( stroke.getGraphicStroke()));
        copy.setLineCap(copy(stroke.getLineCap()));
        copy.setLineJoin( copy(stroke.getLineJoin()));
        copy.setOpacity( copy(stroke.getOpacity()));
        copy.setWidth( rescale(stroke.getWidth()));
        pages.push(copy);
    }   
    
    float[] rescale(float[] original) {
        if(original == null) {
            return null;
        }
        
        // rescale the dash array if possible
        float[] rescaled = new float[original.length];
        float scaleFactor = 1;
        if( scale instanceof Literal){
            scaleFactor = scale.evaluate(null, Float.class);
        }
        for (int i = 0; i < rescaled.length; i++) {
            rescaled[i] = scaleFactor * original[i];
        }
        
        return rescaled;
    }

    /** Make graphics (such as used with PointSymbolizer) bigger */
    public void visit(Graphic gr) {
        Graphic copy = null;

        Displacement displacementCopy = null;

        if (gr.getDisplacement() != null) {
            gr.getDisplacement().accept(this);
            displacementCopy = (Displacement) pages.pop();
        }

        ExternalGraphic[] externalGraphics = gr.getExternalGraphics();
        ExternalGraphic[] externalGraphicsCopy = new ExternalGraphic[externalGraphics.length];

        int length=externalGraphics.length;
        for (int i = 0; i < length; i++) {
            externalGraphicsCopy[i] = copy( externalGraphics[i]);
        }

        Mark[] marks = gr.getMarks();
        Mark[] marksCopy = new Mark[marks.length];
        length=marks.length;
        for (int i = 0; i < length; i++) {
            marksCopy[i] = copy( marks[i]);
        }

        Expression opacityCopy = copy( gr.getOpacity() );
        Expression rotationCopy = copy( gr.getRotation() );
        Expression sizeCopy = rescale( gr.getSize() );
        
        Symbol[] symbols = gr.getSymbols();
        length=symbols.length;
        Symbol[] symbolCopys = new Symbol[length];

        for (int i = 0; i < length; i++) {
            symbolCopys[i] = copy( symbols[i] );
        }

        copy = sf.createDefaultGraphic();
        copy.setGeometryPropertyName(gr.getGeometryPropertyName());
        copy.setDisplacement(displacementCopy);
        copy.setExternalGraphics(externalGraphicsCopy);
        copy.setMarks(marksCopy);
        copy.setOpacity((Expression) opacityCopy);
        copy.setRotation((Expression) rotationCopy);
        copy.setSize((Expression) sizeCopy);
        copy.setSymbols(symbolCopys);

        pages.push(copy);
    }
    
    @Override
    public void visit(TextSymbolizer text) {
        super.visit(text);
        TextSymbolizer copy = (TextSymbolizer) pages.peek();

        // rescales fonts
        Font[] fonts = copy.getFonts();
        for (Font font : fonts) {
            font.setSize(rescale(font.getSize()));
        }
        copy.setFonts(fonts);

        // rescales label placement
        LabelPlacement placement = copy.getLabelPlacement();
        if (placement instanceof PointPlacement) {
            // rescales point label placement
            PointPlacement pointPlacement = (PointPlacement) placement;
            Displacement disp = pointPlacement.getDisplacement();
            if (disp != null) {
                disp.setDisplacementX(rescale(disp.getDisplacementX()));
                disp.setDisplacementY(rescale(disp.getDisplacementY()));
                pointPlacement.setDisplacement(disp);
            }
        } else if (placement instanceof LinePlacement) {
            // rescales line label placement
            LinePlacement linePlacement = (LinePlacement) placement;
            linePlacement.setGap(rescale(linePlacement.getGap()));
            linePlacement.setInitialGap(rescale(linePlacement.getInitialGap()));
            linePlacement.setPerpendicularOffset(rescale(linePlacement.getPerpendicularOffset()));
        }
        copy.setLabelPlacement(placement);
        
        // rescale the halo
        if(copy.getHalo() != null) {
            copy.getHalo().setRadius(rescale(copy.getHalo().getRadius()));
        }
        
        // deal with the format options specified in pixels
        Map<String, String> options = copy.getOptions();
        rescaleOption(options, SPACE_AROUND_KEY, DEFAULT_SPACE_AROUND);
        rescaleOption(options, MAX_DISPLACEMENT_KEY, DEFAULT_MAX_DISPLACEMENT);
        rescaleOption(options, MIN_GROUP_DISTANCE_KEY, DEFAULT_MIN_GROUP_DISTANCE);
        rescaleOption(options, LABEL_REPEAT_KEY, DEFAULT_LABEL_REPEAT);
        rescaleOption(options, AUTO_WRAP_KEY, DEFAULT_AUTO_WRAP);
    }
    
    /**
     * Rescales the specified vendor option
     * @param options
     * @param key
     * @param defaultAutoWrap
     * @param value
     */
    protected void rescaleOption(Map<String, String> options, String key, double defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        if(options.get(key) != null) {
            double rescaled = Converters.convert(options.get(key), Double.class) * scaleFactor;
            options.put(key, String.valueOf(rescaled));
        } else if(defaultValue != 0) {
            options.put(key, String.valueOf(defaultValue * scaleFactor));
        }
        
    };
    
    /**
     * Rescales the specified vendor option
     * @param options
     * @param key
     * @param defaultAutoWrap
     * @param value
     */
    protected void rescaleOption(Map<String, String> options, String key, int defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        if(options.get(key) != null) {
            int rescaled = (int) Math.round(Converters.convert(options.get(key), Double.class) * scaleFactor);
            options.put(key, String.valueOf(rescaled));
        } else if(defaultValue != 0) {
            options.put(key, String.valueOf((int) Math.round(defaultValue * scaleFactor)));
        }
        
    };
        
}
