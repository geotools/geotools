package org.geotools.ysld.parse;


import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
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
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        mark(mark);

        YamlMap map = obj.map();

        if (map.has("shape")) {
            mark.setWellKnownName(Util.expression(map.str("shape"), factory));
        }

        context.push(map, new StrokeHandler(factory) {
            @Override
            protected void stroke(Stroke stroke) {
                mark.setStroke(stroke);
            }
        });
        context.push(map, new FillHandler(factory) {
            @Override
            protected void fill(Fill fill) {
                mark.setFill(fill);
            }
        });

        //TODO: external mark?
        /*if ("url".equals(val)) {
        }
        if ("inline".equals(val)) {
        }*/
    }

    protected abstract void mark(Mark mark);
}
