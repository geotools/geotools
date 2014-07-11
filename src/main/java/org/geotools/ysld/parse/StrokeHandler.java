package org.geotools.ysld.parse;

import org.geotools.styling.Graphic;
import org.geotools.styling.Stroke;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public abstract class StrokeHandler extends YsldParseHandler {
    Stroke stroke;

    protected StrokeHandler(Factory factory) {
        super(factory);
        stroke = factory.style.createStroke(null, null);
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("color".equals(val)) {
            handlers.push(new ColorHandler(factory) {
                @Override
                protected void color(Expression color) {
                    stroke.setColor(color);
                }
            });
        }
        if ("width".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    stroke.setWidth(expr);
                }
            });
        }
        if ("opacity".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    stroke.setOpacity(expr);
                }
            });
        }
        if ("linejoin".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    stroke.setLineJoin(expr);
                }
            });
        }
        if ("linecap".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    stroke.setLineCap(expr);
                }
            });
        }
        if ("dasharray".equals(val)) {
            handlers.push(new FloatArrayHandler(factory) {
                protected void array(float[] arr) {
                    stroke.setDashArray(arr);
                }
            });
        }
        if ("dashoffset".equals(val)) {
            handlers.push(new ExpressionHandler(factory) {
                protected void expression(Expression expr) {
                    stroke.setDashOffset(expr);
                }
            });
        }
        if ("graphic-fill".equals(val)) {
            handlers.push(new GraphicHandler(factory) {
                @Override
                protected void graphic(Graphic graphic) {
                    stroke.setGraphicFill(graphic);
                }
            });
        }
        if ("graphic-stroke".equals(val)) {
            handlers.push(new GraphicHandler(factory) {
                @Override
                protected void graphic(Graphic graphic) {
                    stroke.setGraphicStroke(graphic);
                }
            });
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, Deque<YamlParseHandler> handlers) {
        stroke(stroke);
        handlers.pop();
    }

    protected abstract void stroke(Stroke stroke);
}
