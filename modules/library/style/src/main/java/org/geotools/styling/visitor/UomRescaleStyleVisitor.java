/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.measure.Units;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicalSymbol;

/**
 * Visitor used for rescaling a Style given a map scale (e.g., meters per pixel) and taking into
 * consideration the Unit of Measure (UOM, e.g., SI.METRE, USCustomary.FOOT) of each symbolizer. The
 * resulting Style's Symbolizer sizes will all be given in PIXELS, so that they can be directly used
 * by a renderer that is unaware of units of measure or the current map scale. For example, points
 * with size == 100 meters could be rescaled to 10 pixels for higher levels of zoom and 2 pixels for
 * a lower level of zoom.
 *
 * <p>This visitor extends {@link DuplicatingStyleVisitor} and as such yields a copy of the original
 * Style. Usage is simply to call the desired visit() method and then call getCopy() to retrieve the
 * result.
 *
 * @author milton
 * @author Andrea Aime - GeoSolutions
 */
public class UomRescaleStyleVisitor extends DuplicatingStyleVisitor {

    double mapScale;

    /**
     * Constructor: requires the current mapScale to inform the window to viewport (world to screen)
     * relation in order to correctly rescaleDashArray sizes according to units of measure given in
     * world units (e.g., SI.METRE, USCustomary.FOOT, etc).
     *
     * @param mapScale The specified map scale, given in pixels per meter.
     */
    public UomRescaleStyleVisitor(double mapScale) {
        if (mapScale <= 0)
            throw new IllegalArgumentException(
                    "The mapScale is out of range. Value is "
                            + Double.toString(mapScale)
                            + ". It must be positive.");

        this.mapScale = mapScale;
    }

    /**
     * Used to rescaleDashArray the provided unscaled value.
     *
     * @param unscaled the unscaled value.
     * @param mapScale the mapScale in pixels per meter.
     * @param uom the unit of measure that will be used to scale.
     * @return the expression multiplied by the provided scale.
     */
    protected Expression rescale(Expression unscaled, Unit<Length> uom) {
        if (unscaled == null || unscaled.equals(Expression.NIL)) {
            return unscaled;
        }

        Measure m = new Measure(unscaled, uom);
        return RescalingMode.RealWorld.rescaleToExpression(ff.literal(mapScale), m);
    }

    /** Rescale a list of expressions, can handle null. */
    protected List<Expression> rescaleDashArray(List<Expression> expressions, Unit<Length> uom) {
        if (expressions == null || expressions.isEmpty()) {
            return expressions;
        }
        List<Expression> rescaled = new ArrayList<>(expressions.size());
        final Expression scale = rescale(ff.literal(1), uom);

        for (Expression expression : expressions) {
            Expression rescale = ff.function("listMultiply", scale, expression);
            if (expression instanceof Literal) {
                rescaled.add(ff.literal(rescale.evaluate(null)));
            } else {
                rescaled.add(rescale);
            }
        }
        return rescaled;
    }

    /**
     * Used to rescaleDashArray the provided unscaled value.
     *
     * @param unscaled the unscaled value.
     * @param mapScale the mapScale in pixels per meter.
     * @param uom the unit of measure that will be used to scale.
     * @return the expression multiplied by the provided scale.
     */
    protected String rescale(String unscaled, Unit<Length> uom) {
        if (unscaled == null) {
            return unscaled;
        }

        Measure v = new Measure(unscaled, uom);
        return RescalingMode.RealWorld.rescaleToString(mapScale, v);
    }

    /**
     * Used to rescaleDashArray the provided stroke.
     *
     * @param stroke the unscaled stroke, which will be modified in-place.
     * @param mapScale the mapScale in pixels per meter.
     * @param uom the unit of measure that will be used to scale.
     */
    protected void rescaleStroke(Stroke stroke, Unit<Length> uom) {
        if (stroke != null) {
            stroke.setWidth(rescale(stroke.getWidth(), uom));
            stroke.setDashArray(rescaleDashArray(stroke.dashArray(), uom));
            stroke.setDashOffset(rescale(stroke.getDashOffset(), uom));
            rescale(stroke.getGraphicFill(), uom);
            rescale(stroke.getGraphicStroke(), uom);
        }
    }

    @Override
    public void visit(PointSymbolizer ps) {
        super.visit(ps);
        PointSymbolizer copy = (PointSymbolizer) pages.peek();

        Unit<Length> uom = copy.getUnitOfMeasure();
        Graphic copyGraphic = copy.getGraphic();
        rescale(copyGraphic, uom);
        copy.setUnitOfMeasure(Units.PIXEL);
    }

    private void rescale(Graphic graphic, Unit<Length> unit) {
        if (graphic != null) {
            graphic.setSize(rescale(graphic.getSize(), unit));
            graphic.setGap(rescale(graphic.getGap(), unit));

            Displacement disp = graphic.getDisplacement();
            if (disp != null) {
                disp.setDisplacementX(rescale(disp.getDisplacementX(), unit));
                disp.setDisplacementY(rescale(disp.getDisplacementY(), unit));
                graphic.setDisplacement(disp);
            }

            if (graphic.graphicalSymbols() != null) {
                for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
                    if (gs instanceof Mark) {
                        Mark mark = (Mark) gs;
                        rescaleStroke(mark.getStroke(), unit);
                        rescaleFill(mark.getFill(), unit);
                    }
                }
            }
        }
    }

    @Override
    public void visit(LineSymbolizer line) {
        super.visit(line);
        LineSymbolizer copy = (LineSymbolizer) pages.peek();
        Unit<Length> uom = copy.getUnitOfMeasure();
        Stroke copyStroke = copy.getStroke();
        rescaleStroke(copyStroke, uom);
        copy.setPerpendicularOffset(rescale(copy.getPerpendicularOffset(), uom));
        copy.setUnitOfMeasure(Units.PIXEL);
    }

    @Override
    public void visit(PolygonSymbolizer poly) {
        super.visit(poly);
        PolygonSymbolizer copy = (PolygonSymbolizer) pages.peek();

        Unit<Length> uom = copy.getUnitOfMeasure();
        rescaleStroke(copy.getStroke(), uom);
        rescaleFill(copy.getFill(), uom);
        scaleIntArrayOption(copy.getOptions(), PolygonSymbolizer.GRAPHIC_MARGIN_KEY, uom);
        copy.setUnitOfMeasure(Units.PIXEL);
    }

    private void rescaleFill(Fill copyFill, Unit<Length> unit) {
        // rescale the graphic fill, if any
        if (copyFill != null) {
            rescale(copyFill.getGraphicFill(), unit);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void visit(TextSymbolizer text) {
        super.visit(text);
        TextSymbolizer copy = (TextSymbolizer) pages.peek();

        Unit<Length> uom = copy.getUnitOfMeasure();
        // rescales fonts
        for (Font font : copy.fonts()) {
            font.setSize(rescale(font.getSize(), uom));
        }

        // rescales label placement
        LabelPlacement placement = copy.getLabelPlacement();
        if (placement instanceof PointPlacement) {
            // rescales point label placement
            PointPlacement pointPlacement = (PointPlacement) placement;
            Displacement disp = pointPlacement.getDisplacement();
            if (disp != null) {
                disp.setDisplacementX(rescale(disp.getDisplacementX(), uom));
                disp.setDisplacementY(rescale(disp.getDisplacementY(), uom));
                pointPlacement.setDisplacement(disp);
            }
        } else if (placement instanceof LinePlacement) {
            // rescales line label placement
            LinePlacement linePlacement = (LinePlacement) placement;
            linePlacement.setGap(rescale(linePlacement.getGap(), uom));
            linePlacement.setInitialGap(rescale(linePlacement.getInitialGap(), uom));
            linePlacement.setPerpendicularOffset(
                    rescale(linePlacement.getPerpendicularOffset(), uom));
        }
        copy.setLabelPlacement(placement);

        // rescale the halo
        if (copy.getHalo() != null) {
            copy.getHalo().setRadius(rescale(copy.getHalo().getRadius(), uom));
        }

        if (copy instanceof TextSymbolizer2) {
            TextSymbolizer2 copy2 = (TextSymbolizer2) copy;

            rescale(copy2.getGraphic(), uom);
        }

        // scale various options as well
        Map<String, String> options = copy.getOptions();
        scaleIntOption(options, TextSymbolizer.MAX_DISPLACEMENT_KEY, uom);
        scaleIntOption(options, TextSymbolizer.SPACE_AROUND_KEY, uom);
        scaleIntOption(options, TextSymbolizer.MIN_GROUP_DISTANCE_KEY, uom);
        scaleIntOption(options, TextSymbolizer.LABEL_REPEAT_KEY, uom);
        scaleIntOption(options, TextSymbolizer.AUTO_WRAP_KEY, uom);
        scaleIntArrayOption(options, TextSymbolizer.GRAPHIC_MARGIN_KEY, uom);

        copy.setUnitOfMeasure(Units.PIXEL);
    }

    private void scaleIntOption(Map<String, String> options, String optionName, Unit<Length> uom) {
        if (options.containsKey(optionName)) {
            String rescaled = rescale(options.get(optionName), uom);

            options.put(optionName, toInt(rescaled));
        }
    }

    private void scaleIntArrayOption(
            Map<String, String> options, String optionName, Unit<Length> uom) {
        if (options.containsKey(optionName)) {
            String strValue = options.get(optionName);
            String[] splitted = strValue.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String value : splitted) {
                String rescaled = rescale(value, uom);
                sb.append(toInt(rescaled)).append(" ");
            }
            sb.setLength(sb.length() - 1);
            options.put(optionName, sb.toString());
        }
    }

    String toInt(String value) {
        Double dv = Double.valueOf(value);
        return String.valueOf(dv.intValue());
    }
}
