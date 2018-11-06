/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;

// import edu.psu.geovista.colorbrewer.OriginalColor;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Color;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.function.ClassificationFunction;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/** @author James Macgill */
public class PaletteFunction extends FunctionExpressionImpl {
    ClassificationFunction classifier;
    String paletteName;
    FilterFactory ff;

    // public static FunctionName NAME = new FunctionNameImpl("Palette","classifier","paletteName");
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "Palette",
                    parameter("color", Color.class),
                    parameter("classifier", ClassificationFunction.class),
                    parameter("paletteName", String.class));

    /** Creates a new instance of PaletteFunction */
    public PaletteFunction() {
        this(CommonFactoryFinder.getFilterFactory(null));
    }

    public PaletteFunction(FilterFactory factory) {
        super(NAME);
        ff = factory;
    }

    public void setFilterFactory(FilterFactory factory) {
        ff = factory;
    }

    public void setParameters(List args) {
        super.setParameters(args);
        classifier = (ClassificationFunction) getExpression(0);
        paletteName = ((Literal) getExpression(1)).evaluate(null, String.class);
    }

    public Expression getEvaluationExpression() {
        return classifier.getParameters().get(0);
    }

    public void setEvaluationExpression(Expression e) {
        classifier.getParameters().set(0, e);
    }

    public ClassificationFunction getClassifier() {
        return classifier;
    }

    public void setClassifier(ClassificationFunction cf) {
        classifier = cf;
    }

    public int getNumberOfClasses() {
        return classifier.getClasses();
    }

    public void setNumberOfClasses(int i) {
        classifier.setClasses(i);
    }

    public String getPaletteName() {
        return paletteName;
    }

    public void setPaletteName(String s) {
        paletteName = s;
    }

    private String intToHex(int i) {
        String prelim = Integer.toHexString(i);

        while (prelim.length() < 2) {
            prelim = "0" + prelim;
        }

        if (prelim.length() > 2) {
            prelim = prelim.substring(0, 1);
        }

        return prelim;
    }

    public Object evaluate(Object feature) {
        int classNum = classifier.getClasses();
        ColorBrewer brewer = new ColorBrewer();
        int klass = ((Integer) classifier.evaluate(feature)).intValue();

        BrewerPalette pal = brewer.getPalette(paletteName);
        Color[] colors = pal.getColors(classNum);
        String color =
                "#"
                        + intToHex(colors[klass].getRed())
                        + intToHex(colors[klass].getGreen())
                        + intToHex(colors[klass].getBlue());

        return color;
    }

    public String toString() {
        return "Color Brewer palette";
    }
}
