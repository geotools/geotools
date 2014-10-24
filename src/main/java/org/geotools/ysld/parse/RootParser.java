package org.geotools.ysld.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

import com.google.common.base.Optional;

public class RootParser extends YsldParseHandler {

    StyledLayerDescriptor sld;
    Style style;

    public RootParser() {
        super(new Factory());
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        sld = factory.style.createStyledLayerDescriptor();

        NamedLayer layer = factory.style.createNamedLayer();
        sld.layers().add(layer);

        layer.styles().add(style = factory.style.createStyle());

        YamlMap root = obj.map();
        
        if(root.has("grid")){
            context.setDocHint(ZoomContext.HINT_ID, getZoomContext(root.map("grid")));
        }
        
        style.setName(root.str("name"));
        if (root.has("title")) {
            style.setTitle(root.str("title"));
        }
        if (root.has("abstract")) {
            style.setAbstract(root.str("abstract"));
        }
        style.setTitle(root.str("title"));
        style.setAbstract(root.str("abstract"));
        style.setName(root.str("name"));

        if (root.has("feature-styles")) {
            context.push("feature-styles", new FeatureStyleParser(style, factory));
        }
        else if (root.has("rules")) {
            context.push("rules", new RuleParser(newFeatureTypeStyle(), factory));
        }
        else if (root.has("symbolizers")) {
            context.push("symbolizers", new SymbolizersParser(newRule(), factory));
        }
        else if (root.has("point") || root.has("line") || root.has("polygon")
            || root.has("text") || root.has("raster")) {
            context.push(new SymbolizersParser(newRule(), factory));
        }
    }

    @SuppressWarnings("unchecked")
    protected ZoomContext getZoomContext(YamlMap map) {
        ZoomContext result = null;
        if(map.has("name")) {
            result = getNamedZoomContext(map.str("name")).orNull();
        }
        
        if(result==null && map.has("scales")) {
            final List<?> raw = map.seq("scales").raw();
            final List<Double> scaleDenoms = new ArrayList<>(raw.size());
            for(Number s: (List<Number>)raw) {
                scaleDenoms.add(s.doubleValue());
            }
            final int initialLevel = map.intOr("initial-level", 0); 
            
            result = new ListZoomContext(scaleDenoms, initialLevel);
        }
        
        if (result==null && map.has("initial-scale")) {
            final double initialScale = map.doub("initial-scale");
            final double ratio = map.doubOr("ratio", 2d);
            final int initialLevel = map.intOr("initial-level", 0);
            
            result = new RatioZoomContext(initialLevel, initialScale, ratio);
        }
        
        if(result==null) {
            throw new IllegalArgumentException();
        }
        
        return result;
    }
    
    protected Optional<ZoomContext> getNamedZoomContext(String name){
        // TODO Simple extension point so gs-ysld can plug in GWC based lookup.
        return getWellKnownZoomContext(name);
    }
    
    static final Map<String, ZoomContext> wellKnownZoomContexts;
    static {
        wellKnownZoomContexts = new HashMap<>();
        
        ZoomContext googleMercatorExtended = new RatioZoomContext(559082263.9508929, 2);
        wellKnownZoomContexts.put("WebMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("SphericalMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("GoogleMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3587".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:900913".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3857".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3785".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("OSGEO:41001".toUpperCase(), googleMercatorExtended);
    }
    /**
     * Retrieve a ZoomContext by name from the set of well known contexts.
     * @param name
     * @return
     */
    // FIXME this should go somewhere else
    public static Optional<ZoomContext> getWellKnownZoomContext(String name) {
        return Optional.fromNullable(wellKnownZoomContexts.get(name.toUpperCase()));
    }
    
    public FeatureTypeStyle newFeatureTypeStyle() {
        FeatureTypeStyle fts = factory.style.createFeatureTypeStyle();
        style.featureTypeStyles().add(fts);
        return fts;
    }

    public Rule newRule() {
        Rule rule = factory.style.createRule();
        newFeatureTypeStyle().rules().add(rule);
        return rule;
    }

    public StyledLayerDescriptor sld() {
        return sld;
    }
}
