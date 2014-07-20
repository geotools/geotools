package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class SymbolizersHandler extends YsldParseHandler {

    Rule rule;

    public SymbolizersHandler(Rule rule, Factory factory) {
        super(factory);
        this.rule = rule;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj instanceof YamlMap ? YamlSeq.from(obj) : obj.seq();

        for (YamlObject o : seq) {
            YamlMap sym = o.map();

            if (sym.has("point")) {
                context.push(sym, "point", new PointHandler(rule, factory));
            }
            else if (sym.has("line")) {
                context.push(sym, "line", new LineHandler(rule, factory));
            }
            else if (sym.has("polygon")) {
                context.push(sym, "polygon", new PolygonHandler(rule, factory));
            }
            else if (sym.has("text")) {
                context.push(sym, "text", new TextHandler(rule, factory));
            }
            else if (sym.has("raster")) {
                context.push(sym, "raster", new RasterHandler(rule, factory));
            }
        }
    }
}
