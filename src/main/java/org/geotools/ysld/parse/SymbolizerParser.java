package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UomOgcMapping;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

public class SymbolizerParser<T extends Symbolizer> extends YsldParseHandler {

    protected T sym;

    protected SymbolizerParser(Rule rule, T sym, Factory factory) {
        super(factory);
        rule.symbolizers().add(this.sym = sym);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlMap map = obj.map();
        sym.setName(map.str("name"));
        if (map.has("geometry")) {
            sym.setGeometry(Util.expression(map.str("geometry"), factory));
        }
        if (map.has("uom")) {
            sym.setUnitOfMeasure(UomOgcMapping.get(map.str("uom")).getUnit());
        }
        context.push("options", new OptionsHandler());
    }

    class OptionsHandler extends YsldParseHandler {

        OptionsHandler() {
            super(SymbolizerParser.this.factory);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlMap map = obj.map();
            for (String key : map) {
                sym.getOptions().put(key, map.str(key));
            }
        }
    }
}
