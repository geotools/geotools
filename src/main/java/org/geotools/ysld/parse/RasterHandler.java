package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public class RasterHandler extends SymbolizerHandler<RasterSymbolizer> {

    public RasterHandler(Rule rule, Factory factory) {
        super(rule, factory.style.createRasterSymbolizer(), factory);
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("opacity".equals(val)) {
            context.push(new ExpressionHandler(factory) {
                @Override
                protected void expression(Expression expr) {
                    sym.setOpacity(expr);
                }
            });
        }
        else if ("color-map".equals(val)) {
            context.push(new ColorMapHandler(factory) {
                @Override
                protected void colorMap(ColorMap colorMap) {
                    sym.setColorMap(colorMap);
                }
            });
        }
        else if ("contrast-enhancement".equals(val)) {
            context.push(new ContrastEnhancementHandler());
        }
        else {
            super.scalar(evt, context);
        }
    }

    class ContrastEnhancementHandler extends YsldParseHandler {

        ContrastEnhancement contrast;

        protected ContrastEnhancementHandler() {
            super(RasterHandler.this.factory);
            sym.setContrastEnhancement(contrast = factory.style.createContrastEnhancement());
        }

        @Override
        public void scalar(ScalarEvent evt, YamlParseContext context) {
            String val = evt.getValue();
            if ("mode".equals(val)) {
                context.push(new ValueHandler(factory) {
                    @Override
                    protected void value(String value, Event event) {
                        ContrastMethod method = ContrastMethod.valueOf(value);
                        if (method != null) {
                            contrast.setMethod(method);
                        }
                        else {
                            LOG.warning("Unknown contrast method: " + value);
                        }
                    }
                });
            }
            else if ("gamma".equals(val)) {
                context.push(new ExpressionHandler(factory) {
                    @Override
                    protected void expression(Expression expr) {
                        contrast.setGammaValue(expr);
                    }
                });
            }
        }

        @Override
        public void endMapping(MappingEndEvent evt, YamlParseContext context) {
            super.endMapping(evt, context);
            context.pop();
        }
    }
}
