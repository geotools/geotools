package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlObject;

public class PointParser extends SymbolizerParser<PointSymbolizer> {

    public PointParser(Rule rule, Factory factory) {
        super(rule, factory.style.createPointSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);
        context.push(new GraphicParser(factory, sym.getGraphic()));
    }
}
