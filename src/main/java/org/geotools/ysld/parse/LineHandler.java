package org.geotools.ysld.parse;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public class LineHandler extends SymbolizerHandler<LineSymbolizer> {

    protected LineHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createLineSymbolizer(), factory);
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
        else if ("offset".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                @Override
                protected void expression(Expression expr) {
                    sym.setPerpendicularOffset(expr);
                }
            });
        }
        else {
            super.scalar(evt, handlers);
        }
    }
}
