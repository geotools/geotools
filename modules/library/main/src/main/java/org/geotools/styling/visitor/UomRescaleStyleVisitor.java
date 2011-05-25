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
package org.geotools.styling.visitor;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.filter.LiteralExpression;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;

/**
 * Visitor used for rescaling a Style given a map scale (e.g., meters per pixel) and taking into
 * consideration the Unit of Measure (UOM, e.g., SI.METER, NonSI.FOOT) of each symbolizer. The
 * resulting Style's Symbolizer sizes will all be given in PIXELS, so that they can be directly used
 * by a renderer that is unaware of units of measure or the current map scale. For example, points
 * with size == 100 meters could be rescaled to 10 pixels for higher levels of zoom and 2 pixels for
 * a lower level of zoom.
 * <p>
 * This visitor extends {@link DuplicatingStyleVisitor} and as such yields a copy of the original
 * Style. Usage is simply to call the desired visit() method and then call getCopy() to retrieve the
 * result.
 * 
 * @author milton
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/styling/visitor/UomRescaleStyleVisitor.java $
 */
public class UomRescaleStyleVisitor extends DuplicatingStyleVisitor {

    private double mapScale = 1;

    /**
     * Constructor: requires the current mapScale to inform the window to viewport (world to screen)
     * relation in order to correctly rescale sizes according to units of measure given in world
     * units (e.g., SI.METER, NonSI.FOOT, etc).
     * 
     * @param mapScale
     *            The specified map scale, given in pixels per meter.
     */
    public UomRescaleStyleVisitor(double mapScale) {
        if (mapScale <= 0)
            throw new IllegalArgumentException("The mapScale is out of range. Value is "
                    + Double.toString(mapScale) + ". It must be positive.");

        this.mapScale = mapScale;
    }

    /**
     * Computes a rescaling multiplier to be applied to an unscaled value.
     * 
     * @param mapScale
     *            the mapScale in pixels per meter.
     * @param uom
     *            the unit of measure that will be used to scale.
     * @return the rescaling multiplier for the provided parameters.
     */
    protected double computeRescaleMultiplier(double mapScale, Unit<Length> uom) {
        // no scaling to do if UOM is PIXEL (or null, which stands for PIXEL as well)
        if (uom == null || uom.equals(NonSI.PIXEL))
            return 1;

        // converts value from meters to given UOM
        UnitConverter converter = uom.getConverterTo(SI.METER);
        return converter.convert(mapScale);
    }

    /**
     * Used to rescale the provided unscaled value.
     * 
     * @param unscaled
     *            the unscaled value.
     * @param mapScale
     *            the mapScale in pixels per meter.
     * @param uom
     *            the unit of measure that will be used to scale.
     * @return the expression multiplied by the provided scale.
     */
    protected Expression rescale(Expression unscaled, double mapScale, Unit<Length> uom) {
        if (unscaled == null || unscaled.equals(Expression.NIL))
            return unscaled;

        if (unscaled instanceof LiteralExpression && unscaled.evaluate(null, Double.class) != null) {
            // if given Expression is a literal, we can return a literal
            double rescaled = rescale(unscaled.evaluate(null, Double.class), mapScale, uom);
            return ff.literal(rescaled);
        } else {
            // otherwise, we return a Multiply expression with the rescaling multiplier
            double rescaleMultiplier = computeRescaleMultiplier(mapScale, uom);
            return ff.multiply(unscaled, ff.literal(rescaleMultiplier));
        }
    }

    /**
     * Used to rescale the provided unscaled value.
     * 
     * @param unscaled
     *            the unscaled value.
     * @param mapScale
     *            the mapScale in pixels per meter.
     * @param uom
     *            the unit of measure that will be used to scale.
     * @return a scaled value.
     */
    protected double rescale(double unscaled, double mapScale, Unit<Length> uom) {
        // computes the basic rescaled value
        return unscaled * computeRescaleMultiplier(mapScale, uom);
    }

    /**
     * Used to rescale the provided dash array.
     * 
     * @param dashArray
     *            the unscaled dash array. If null, the method returns null.
     * @param mapScale
     *            the mapScale in pixels per meter.
     * @param uom
     *            the unit of measure that will be used to scale.
     * @return the rescaled dash array
     */
    protected float[] rescale(float[] dashArray, double mapScale, Unit<Length> unitOfMeasure) {
        if (dashArray == null)
            return null;
        if (unitOfMeasure == null || unitOfMeasure.equals(NonSI.PIXEL))
            return dashArray;

        float[] rescaledDashArray = new float[dashArray.length];

        for (int i = 0; i < rescaledDashArray.length; i++) {
            rescaledDashArray[i] = (float) rescale((double) dashArray[i], mapScale, unitOfMeasure);
        }
        return rescaledDashArray;
    }

    /**
     * Used to rescale the provided stroke.
     * 
     * @param stroke
     *            the unscaled stroke, which will be modified in-place.
     * @param mapScale
     *            the mapScale in pixels per meter.
     * @param uom
     *            the unit of measure that will be used to scale.
     */
    protected void rescaleStroke(Stroke stroke, double mapScale, Unit<Length> uom) {
        if (stroke != null) {
            stroke.setWidth(rescale(stroke.getWidth(), mapScale, uom));
            stroke.setDashArray(rescale(stroke.getDashArray(), mapScale, uom));
            stroke.setDashOffset(rescale(stroke.getDashOffset(), mapScale, uom));
        }
    }

    @Override
    public void visit(PointSymbolizer ps) {
        super.visit(ps);
        PointSymbolizer copy = (PointSymbolizer) pages.peek();

        Graphic copyGraphic = copy.getGraphic();
        copyGraphic.setSize(rescale(copyGraphic.getSize(), mapScale, copy.getUnitOfMeasure()));
        copy.setUnitOfMeasure(NonSI.PIXEL);
    }

    @Override
    public void visit(LineSymbolizer line) {
        super.visit(line);
        LineSymbolizer copy = (LineSymbolizer) pages.peek();

        Stroke copyStroke = copy.getStroke();
        rescaleStroke(copyStroke, mapScale, copy.getUnitOfMeasure());
        copy.setUnitOfMeasure(NonSI.PIXEL);
    }

    @Override
    public void visit(PolygonSymbolizer poly) {
        super.visit(poly);
        PolygonSymbolizer copy = (PolygonSymbolizer) pages.peek();

        Stroke copyStroke = copy.getStroke();
        rescaleStroke(copyStroke, mapScale, copy.getUnitOfMeasure());

        // rescales the graphic fill, if available
        Fill copyFill = copy.getFill();
        if (copyFill != null) {
            Graphic copyGraphic = copyFill.getGraphicFill();
            if (copyGraphic != null) {
                copyGraphic.setSize(rescale(copyGraphic.getSize(), mapScale, copy
                        .getUnitOfMeasure()));
                copyFill.setGraphicFill(copyGraphic);
            }
            copy.setFill(copyFill);
        }
        copy.setUnitOfMeasure(NonSI.PIXEL);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void visit(TextSymbolizer text) {
        super.visit(text);
        TextSymbolizer copy = (TextSymbolizer) pages.peek();

        Unit<Length> uom = copy.getUnitOfMeasure();

        // rescales fonts
        Font[] fonts = copy.getFonts();
        for (Font font : fonts)
            font.setSize(rescale(font.getSize(), mapScale, uom));
        copy.setFonts(fonts);

        // rescales label placement
        LabelPlacement placement = copy.getLabelPlacement();
        if (placement instanceof PointPlacement) {
            // rescales point label placement
            PointPlacement pointPlacement = (PointPlacement) placement;
            Displacement disp = pointPlacement.getDisplacement();
            if (disp != null) {
                disp.setDisplacementX(rescale(disp.getDisplacementX(), mapScale, uom));
                disp.setDisplacementY(rescale(disp.getDisplacementY(), mapScale, uom));
                pointPlacement.setDisplacement(disp);
            }
        } else if (placement instanceof LinePlacement) {
            // rescales line label placement
            LinePlacement linePlacement = (LinePlacement) placement;
            linePlacement.setGap(rescale(linePlacement.getGap(), mapScale, uom));
            linePlacement.setInitialGap(rescale(linePlacement.getInitialGap(), mapScale, uom));
            linePlacement.setPerpendicularOffset(rescale(linePlacement.getPerpendicularOffset(),
                    mapScale, uom));
        }
        copy.setLabelPlacement(placement);
        
        // rescale the halo
        if(copy.getHalo() != null) {
            copy.getHalo().setRadius(rescale(copy.getHalo().getRadius(), mapScale, uom));
        }
        
        copy.setUnitOfMeasure(NonSI.PIXEL);
    }
}
