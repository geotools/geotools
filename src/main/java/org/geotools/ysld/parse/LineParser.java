package org.geotools.ysld.parse;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

public class LineParser extends SymbolizerParser<LineSymbolizer> {

    protected LineParser(Rule rule, Factory factory) {
        super(rule, factory.style.createLineSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();
        context.push(obj, new StrokeParser(factory) {
            @Override
            protected void stroke(Stroke stroke) {
                sym.setStroke(stroke);
            }
        });
        if (map.has("offset")) {
            sym.setPerpendicularOffset(Util.expression(map.str("offset"), factory));
        }
    }
}
