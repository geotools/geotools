package org.geotools.ysld.parse;


import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public abstract class FillHandler extends YsldParseHandler {
    Fill fill;

    protected FillHandler(Factory factory) {
        super(factory);
        fill = factory.style.createFill(null);
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("color".equals(val)) {
            handlers.push(new ColorHandler(factory) {
                @Override
                protected void color(Expression color) {
                    fill.setColor(color);
                }
            });
        }
        if ("opacity".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    fill.setOpacity(expr);
                }
            });
        }
        if ("graphic".equals(val)) {
            handlers.push(new GraphicHandler(factory) {
                @Override
                protected void graphic(Graphic graphic) {
                    fill.setGraphicFill(graphic);
                }
            });
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, Deque<YamlParseHandler> handlers) {
        fill(fill);
        handlers.pop();
    }

    protected abstract void fill(Fill fill);
}
