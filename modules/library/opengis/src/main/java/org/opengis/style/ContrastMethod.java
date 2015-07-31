/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import java.util.Map;

import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.annotation.XmlElement;

/**
 * The ContrastEnhancement element defines contrast enhancement for a channel of a false-color image or for a color image.
 * 
 * In the case of a color image, the relative grayscale brightness of a pixel color is used. “Normalize” means to stretch the contrast so that the
 * dimmest color is stretched to black and the brightest color is stretched to white, with all colors in between stretched out linearly. “Histogram”
 * means to stretch the contrast based on a histogram of how many colors are at each brightness level on input, with the goal of producing equal
 * number of pixels in the image at each brightness level on output. This has the effect of revealing many subtle ground features.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("ContrastEnhancement:type")
public interface ContrastMethod {
    public static final String NORMALIZE = "normalize";
    public static final String LOGARITHMIC = "logarithmic";
    public static final String EXPONENTIAL = "exponential";
    public static final String HISTOGRAM = "histogram";
    /**
     * discover the type of this method.
     * 
     * @return the type
     */
    public Expression getType();

    /**
     * Get the Algorithm that this method uses or null if none.
     * 
     * @return an expression for the name of the algorithm
     */
    public Expression getAlgorithm();

    /**
     * 
     */
    public Map<String, Expression> getParameters();

    /**
     * 
     */

    public String name();

    /**
     * Traversal of the style data structure.
     * 
     * @param visitor
     */
    public void accept(StyleVisitor visitor);

    /**
     * @return
     */
    public FilterFactory getFilterFactory();

    /**
     * @param key - the name of the parameter
     * @param value - an expression that evaluates the parameter value
     */
    public void addParameter(String key, Expression value);

    /**
     * @param name - the name of the algorithm to use (if required)
     */
    public void setAlgorithm(Expression name);

}
