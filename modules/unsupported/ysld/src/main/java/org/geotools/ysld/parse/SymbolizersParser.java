package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

public class SymbolizersParser extends YsldParseHandler {

    Rule rule;

    public SymbolizersParser(Rule rule, Factory factory) {
        super(factory);
        this.rule = rule;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj instanceof YamlMap ? YamlSeq.from(obj) : obj.seq();

        for (YamlObject o : seq) {
            YamlMap sym = o.map();

            if (sym.has("point")) {
                context.push(sym, "point", new PointParser(rule, factory));
            }
            else if (sym.has("line")) {
                context.push(sym, "line", new LineParser(rule, factory));
            }
            else if (sym.has("polygon")) {
                context.push(sym, "polygon", new PolygonParser(rule, factory));
            }
            else if (sym.has("text")) {
                context.push(sym, "text", new TextParser(rule, factory));
            }
            else if (sym.has("raster")) {
                context.push(sym, "raster", new RasterParser(rule, factory));
            }
        }
    }
}
