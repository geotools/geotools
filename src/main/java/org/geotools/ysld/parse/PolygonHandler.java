package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public class PolygonHandler extends SymbolizerHandler<PolygonSymbolizer> {

    public PolygonHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createPolygonSymbolizer(), factory);
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("stroke".equals(val)) {
            handlers.push(new StrokeHandler(factory) {
                @Override
                protected void stroke(Stroke stroke) {
                    sym.setStroke(stroke);
                }
            });
        }
        else if ("fill".equals(val)) {
            handlers.push(new FillHandler(factory) {
                @Override
                protected void fill(Fill fill) {
                    sym.setFill(fill);
                }
            });
        }
        else if ("offset".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                @Override
                protected void expression(Expression expr) {
                    sym.setPerpendicularOffset(expr);
                }
            });
        }
        else if ("displacement".equals(val)) {
            handlers.push(new DisplacementHandler(factory) {
                @Override
                protected void displace(Displacement displacement) {
                    sym.setDisplacement(displacement);
                }
            });
        }
        else {
            super.scalar(evt, handlers);
        }
    }
}
