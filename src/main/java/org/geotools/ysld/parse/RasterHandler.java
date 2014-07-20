package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
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
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();

        if (map.has("opacity")) {
            sym.setOpacity(Util.expression(map.str("opacity"), factory));
        }

        context.push("color-map", new ColorMapHandler(factory) {
            @Override
            protected void colorMap(ColorMap colorMap) {
                sym.setColorMap(colorMap);
            }
        });
        context.push("contrast-enhancement", new ContrastEnhancementHandler());

    }

    class ContrastEnhancementHandler extends YsldParseHandler {

        ContrastEnhancement contrast;

        protected ContrastEnhancementHandler() {
            super(RasterHandler.this.factory);
            contrast = factory.style.createContrastEnhancement();
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            sym.setContrastEnhancement(contrast);

            YamlMap map = obj.map();
            if (map.has("mode")) {
                String mode = map.str("mode");
                ContrastMethod method = ContrastMethod.valueOf(mode);
                if (method != null) {
                    contrast.setMethod(method);
                }
                else {
                    LOG.warning("Unknown contrast method: " + mode);
                }
            }
            if (map.has("gamma")) {
                contrast.setGammaValue(Util.expression(map.str("gamma"), factory));

            }
        }
    }
}
