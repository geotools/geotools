package org.geotools.styling;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Abstract base class for implementing style factories.
 *
 * @author Igor Volkov
 *
 * @source $URL$
 */
public interface StyleFactory3 extends StyleFactory2 {
    Stroke2 createStroke2(Literal color, Literal width);

    Stroke2 createStroke2(Expression color, Expression width,
                          Expression opacity);

    Stroke2 createStroke2(Expression color, Expression width,
                          Expression opacity, Expression lineJoin, Expression lineCap,
                          Expression[] dashArray, Expression dashOffset, Graphic graphicFill,
                          Graphic graphicStroke);
}
