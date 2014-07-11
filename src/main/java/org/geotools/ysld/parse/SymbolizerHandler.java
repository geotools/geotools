package org.geotools.ysld.parse;

import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
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
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("geometry".equals(val)) {
            context.push(new ExpressionHandler(factory) {
                @Override
                protected void expression(Expression expr) {
                    sym.setGeometry(expr);
                }
            });
        }
        else if ("options".equals(val)) {
            context.push(new OptionsHandler());
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, YamlParseContext context) {
        super.endMapping(evt, context);
        context.pop();
    }

    class OptionsHandler extends YsldParseHandler {

        OptionsHandler() {
            super(SymbolizerHandler.this.factory);
        }

        @Override
        public void scalar(ScalarEvent evt, YamlParseContext context) {
            final String key = evt.getValue();
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event event) {
                    sym.getOptions().put(toCamelCase(key), value);
                }
            });
        }

        @Override
        public void endMapping(MappingEndEvent evt, YamlParseContext context) {
            super.endMapping(evt, context);
            context.pop();
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
