package org.geotools.ysld.parse;


import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class FillHandler extends YsldParseHandler {
    Fill fill;

    protected FillHandler(Factory factory) {
        super(factory);
        fill = factory.style.createFill(null);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        fill(fill);
        YamlMap map = obj.map();
        if (map.has("color")) {
            fill.setColor(Util.color(map.str("color"), factory));
        }
        if (map.has("opacity")) {
            fill.setOpacity(Util.expression(map.str("opacity"), factory));
        }
        context.push("graphic", new GraphicHandler(factory) {
            @Override
            protected void graphic(Graphic g) {
                fill.setGraphicFill(g);
            }
        });
    }

    protected abstract void fill(Fill fill);
}
