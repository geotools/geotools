package org.geotools.ysld.parse;

import org.geotools.styling.Graphic;
import org.geotools.styling.Stroke;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class StrokeHandler extends YsldParseHandler {
    Stroke stroke;

    protected StrokeHandler(Factory factory) {
        super(factory);
        stroke = factory.style.createStroke(null, null);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        stroke(stroke);

        YamlMap map = obj.map();

        if (map.has("color")) {
            stroke.setColor(Util.color(map.str("color"), factory));
        }
        if (map.has("width")) {
            stroke.setWidth(Util.expression(map.str("width"), factory));
        }
        if (map.has("opacity")) {
            stroke.setOpacity(Util.expression(map.str("opacity"), factory));
        }
        if (map.has("linejoin")) {
            stroke.setLineJoin(Util.expression(map.str("linejoin"), factory));
        }
        if (map.has("linecap")) {
            stroke.setLineCap(Util.expression(map.str("linecap"), factory));
        }
        if (map.has("dasharray")) {
            stroke.setDashArray(Util.floatArray(map.str("dasharray")));
        }
        if (map.has("dashoffset")) {
            stroke.setDashOffset(Util.expression(map.str("dashoffset"), factory));
        }

        context.push("graphic-fill", new GraphicHandler(factory) {
            @Override
            protected void graphic(Graphic g) {
                stroke.setGraphicFill(g);
            }
        });
        context.push("graphic-stroke", new GraphicHandler(factory) {
            @Override
            protected void graphic(Graphic g) {
                stroke.setGraphicStroke(g);
            }
        });
    }

    protected abstract void stroke(Stroke stroke);
}
