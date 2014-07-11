package org.geotools.ysld.parse;


import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class MarkHandler extends YsldParseHandler {

    Mark mark;

    protected MarkHandler(Factory factory) {
        super(factory);
        mark = factory.style.createMark();
        mark.setStroke(null);
        mark.setFill(null);
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("shape".equals(val)) {
            context.push(new ExpressionHandler(factory) {
                @Override
                protected void expression(Expression expr) {
                    mark.setWellKnownName(expr);
                }
            });
        }
        if ("stroke".equals(val)) {
            context.push(new StrokeHandler(factory) {
                @Override
                protected void stroke(Stroke stroke) {
                    mark.setStroke(stroke);
                }
            });
        }
        if ("fill".equals(val)) {
            context.push(new FillHandler(factory) {
                @Override
                protected void fill(Fill fill) {
                    mark.setFill(fill);
                }
            });
        }

        //TODO: external mark?
        if ("url".equals(val)) {
        }
        if ("inline".equals(val)) {
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, YamlParseContext context) {
        mark(mark);
        context.pop();
    }

    protected abstract void mark(Mark mark);
}
