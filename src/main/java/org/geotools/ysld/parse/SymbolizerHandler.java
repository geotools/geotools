package org.geotools.ysld.parse;

import org.geotools.measure.Units;
import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UomOgcMapping;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class SymbolizerHandler<T extends Symbolizer> extends YsldParseHandler {

    protected T sym;

    protected SymbolizerHandler(Rule rule, T sym, Factory factory) {
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
            super(SymbolizerHandler.this.factory);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlMap map = obj.map();
            for (String key : map) {
                sym.getOptions().put(toCamelCase(key), map.str(key));
            }
        }

        String toCamelCase(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }

            StringBuilder sb = new StringBuilder();
            boolean upper = false;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (ch == '-') {
                    upper = true;
                }
                else {
                    sb.append(upper?Character.toUpperCase(ch):ch);
                    upper = false;
                }
            }

            return sb.toString();
        }
    }
}
