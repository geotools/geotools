package org.geotools.render;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Symbolizer;

public class StyleExamples {

private void styleFactoryExample() throws Exception {
    // styleFactoryExample start
    //
    // We are using the GeoTools styleFactory that allows access to get/set methods
    org.geotools.styling.StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
    sld.setName("sld");
    sld.setTitle("Example");
    sld.setAbstract("Example Style Layer Descriptor");
    
    UserLayer layer = sf.createUserLayer();
    layer.setName("layer");
    
    //
    // define constraint limited what features the sld applies to
    FeatureTypeConstraint constraint = sf.createFeatureTypeConstraint("Feature", Filter.INCLUDE,
            null);
    
    layer.layerFeatureConstraints().add(constraint);
    
    //
    // create a "user defined" style
    Style style = sf.createStyle();
    style.setName("style");
    style.getDescription().setTitle("User Style");
    style.getDescription().setAbstract("Definition of Style");
    
    //
    // define feature type styles used to actually define how features are rendered
    FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();
    
    // RULE 1
    // first rule to draw cities
    Rule rule1 = sf.createRule();
    rule1.setName("rule1");
    rule1.getDescription().setTitle("City");
    rule1.getDescription().setAbstract("Rule for drawing cities");
    rule1.setFilter(ff.less(ff.property("POPULATION"), ff.literal(50000)));
    
    //
    // create the graphical mark used to represent a city
    Stroke stroke = sf.stroke(ff.literal("#000000"), null, null, null, null, null, null);
    Fill fill = sf.fill(null, ff.literal(Color.BLUE), ff.literal(1.0));
    
    // OnLineResource implemented by gt-metadata - so no factory!
    OnLineResourceImpl svg = new OnLineResourceImpl(new URI("file:city.svg"));
    svg.freeze(); // freeze to prevent modification at runtime
    
    OnLineResourceImpl png = new OnLineResourceImpl(new URI("file:city.png"));
    png.freeze(); // freeze to prevent modification at runtime
    
    //
    // List of symbols is considered in order with the rendering engine choosing
    // the first one it can handle. Allowing for svg, png, mark order
    List<GraphicalSymbol> symbols = new ArrayList<GraphicalSymbol>();
    symbols.add(sf.externalGraphic(svg, "svg", null)); // svg preferred
    symbols.add(sf.externalGraphic(png, "png", null)); // png preferred
    symbols.add(sf.mark(ff.literal("circle"), fill, stroke)); // simple circle backup plan
    
    Expression opacity = null; // use default
    Expression size = ff.literal(10);
    Expression rotation = null; // use default
    AnchorPoint anchor = null; // use default
    Displacement displacement = null; // use default
    
    // define a point symbolizer of a small circle
    Graphic city = sf.graphic(symbols, opacity, size, rotation, anchor, displacement);
    PointSymbolizer pointSymbolizer = sf.pointSymbolizer("point", ff.property("the_geom"), null,
            null, city);
    
    rule1.symbolizers().add(pointSymbolizer);
    
    featureTypeStyle.rules().add(rule1);
    
    //
    // RULE 2 Default
    
    List<GraphicalSymbol> dotSymbols = new ArrayList<GraphicalSymbol>();
    dotSymbols.add(sf.mark(ff.literal("circle"), null, null));
    Graphic dotGraphic = sf.graphic(dotSymbols, null, ff.literal(3), null, null, null);
    PointSymbolizer dotSymbolizer = sf.pointSymbolizer("dot", null, null, null, dotGraphic);
    List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
    symbolizers.add(dotSymbolizer);
    Filter other = null; // null will mark this rule as "other" accepting all remaining features
    Rule rule2 = sf.rule("default", null, null, Double.MIN_VALUE, Double.MAX_VALUE, symbolizers,
            other);
    featureTypeStyle.rules().add(rule2);
    
    style.featureTypeStyles().add(featureTypeStyle);
    
    layer.userStyles().add(style);
    
    sld.layers().add(layer);
    // styleFactoryExample end
}

private void styleBuilderExample() throws Exception {
    // styleBuilderExample start
    //
    // We are using the GeoTools StyleBuilder that is helpful for quickly making things
    StyleBuilder builder = new StyleBuilder();
    FilterFactory2 ff = builder.getFilterFactory();
    
    // RULE 1
    // first rule to draw cities
    
    // define a point symbolizer representing a city
    Graphic city = builder.createGraphic();
    city.setSize(ff.literal(10));
    city.graphicalSymbols().add(builder.createExternalGraphic("file:city.svg", "svg")); // svg
                                                                                        // preferred
    city.graphicalSymbols().add(builder.createExternalGraphic("file:city.png", "png")); // png next
    city.graphicalSymbols().add(
            builder.createMark(StyleBuilder.MARK_CIRCLE, Color.BLUE, Color.BLACK, 1));
    PointSymbolizer pointSymbolizer = builder.createPointSymbolizer(city, "the_geom");
    
    Rule rule1 = builder.createRule(pointSymbolizer);
    rule1.setName("rule1");
    rule1.getDescription().setTitle("City");
    rule1.getDescription().setAbstract("Rule for drawing cities");
    rule1.setFilter(ff.less(ff.property("POPULATION"), ff.literal(50000)));
    
    //
    // RULE 2 Default
    Graphic dotGraphic = builder.createGraphic(null, builder.createMark(StyleBuilder.MARK_CIRCLE),
            null);
    PointSymbolizer dotSymbolize = builder.createPointSymbolizer(dotGraphic);
    Rule rule2 = builder.createRule(dotSymbolize);
    rule2.setIsElseFilter(true);
    
    //
    // define feature type styles used to actually define how features are rendered
    Rule rules[] = new Rule[] { rule1, rule2 };
    FeatureTypeStyle featureTypeStyle = builder.createFeatureTypeStyle("Feature", rules);
    
    //
    // create a "user defined" style
    Style style = builder.createStyle();
    style.setName("style");
    style.getDescription().setTitle("User Style");
    style.getDescription().setAbstract("Definition of Style");
    style.featureTypeStyles().add(featureTypeStyle);
    // styleBuilderExample end
}

private void splatExample() {
    // splatExample start
    StyleBuilder builder = new StyleBuilder();
    FilterFactory2 ff = builder.getFilterFactory();
    
    Graphic splat = builder.createGraphic(null, builder.createMark("splat"), null);
    PointSymbolizer symbolizer = builder.createPointSymbolizer(splat);
    
    // builder will fill in all the other classes with defaults
    Style style = builder.createStyle( symbolizer );    
    // splatExample end
}
}
