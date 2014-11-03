package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.geotools.ysld.Band;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.style.ContrastMethod;

public class RasterParser extends SymbolizerParser<RasterSymbolizer> {

    public RasterParser(Rule rule, Factory factory) {
        super(rule, factory.style.createRasterSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();

        if (map.has("opacity")) {
            sym.setOpacity(Util.expression(map.str("opacity"), factory));
        }

        context.push("color-map", new ColorMapParser(factory) {
            @Override
            protected void colorMap(ColorMap colorMap) {
                sym.setColorMap(colorMap);
            }
        });
        context.push("contrast-enhancement", new ContrastEnhancementHandler());
        context.push("channels", new ChannelsHandler());
    }

    class ContrastEnhancementHandler extends YsldParseHandler {

        ContrastEnhancement contrast;

        protected ContrastEnhancementHandler() {
            super(RasterParser.this.factory);
            contrast = factory.style.createContrastEnhancement();
            set();
        }
        
        protected void set() {
            sym.setContrastEnhancement(contrast);
        }
        
        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
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
    
    class ChannelsHandler extends YsldParseHandler {
        
        ChannelSelection selection;
        
        protected ChannelsHandler() {
            super(RasterParser.this.factory);
            this.selection = sym.getChannelSelection();
        }
        
        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            
            YamlMap map = obj.map();
            if (map.has(Band.GRAY.key)) {
                if(map.has(Band.RED.key) || map.has(Band.GREEN.key) || map.has(Band.BLUE.key)) throw new IllegalArgumentException("grey and RGB can not be combined");
                SelectedChannelType gray = factory.style.selectedChannelType(null, null);
                selection.setGrayChannel(gray);
                context.push(Band.GRAY.key, new SelectedChannelHandler(gray));
            } else {
                if(!(map.has(Band.RED.key) && map.has(Band.GREEN.key) && map.has(Band.BLUE.key))) throw new IllegalArgumentException("all of red green and blue must be preset");
                SelectedChannelType red = factory.style.selectedChannelType(null, null);
                SelectedChannelType green = factory.style.selectedChannelType(null, null);
                SelectedChannelType blue = factory.style.selectedChannelType(null, null);
                selection.setRGBChannels(red, green, blue);
                context.push(Band.RED.key, new SelectedChannelHandler(red));
                context.push(Band.GREEN.key, new SelectedChannelHandler(green));
                context.push(Band.BLUE.key, new SelectedChannelHandler(blue));
            }
        }
        
    }
    
    class SelectedChannelHandler extends YsldParseHandler {
        SelectedChannelType sel;
        
        public SelectedChannelHandler(SelectedChannelType sel) {
            super(RasterParser.this.factory);
            this.sel = sel;
        }
        
        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            String name = obj.map().str("name");
            sel.setChannelName(name);
            context.push("contrast-enhancement", new ContrastEnhancementHandler(){
    
                @Override
                protected void set() {
                    sel.setContrastEnhancement(this.contrast);
                }
                
            });
        }
    }
}
