package org.geotools.ysld.validate;

import org.geotools.ysld.parse.Util;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.awt.*;

public class ColorValidator extends ScalarValidator {

    @Override
    protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
        try {
            Expression expr = Util.color(value, context.factory);
            if (expr instanceof Literal) {
                Color col = expr.evaluate(null, Color.class);
                if (col == null) {
                    return "Invalid color, must be one of: 0xrrggbb, rgb(r,g,b), or expression";
                }
            }
            return null;
        }
        catch(Exception e) {
            return e.getMessage();
        }
    }
}
