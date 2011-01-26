package org.geotools.demo.style;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.style.Graphic;


/**
 * The following style snippets are used to keep the wiki honest.
 * <p>
 * To view the context for these examples: http://docs.codehaus.org/display/GEOTDOC/04+Styling
 * 
 *
 * @source $URL$
 */
public class StyleSnippets {

    public void styleLayerDescriptor() {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        sld.setName("example");
        sld.setAbstract("Example Style Layer Descriptor");
        {
            UserLayer layer = styleFactory.createUserLayer();
            layer.setName("layer");
            {
                FeatureTypeConstraint constraint = styleFactory.createFeatureTypeConstraint(
                        "Feature", Filter.INCLUDE, null);

                layer.layerFeatureConstraints().add(constraint);
            }
            {
                Style style = styleFactory.createStyle();
                
                style.getDescription().setTitle("Style");
                style.getDescription().setAbstract( "Definition of Style" );
                
                // define feature type styles used to actually
                // define how features are rendered
                //
                layer.userStyles().add(style);
            }
            sld.layers().add(layer);
        }        
    }

    /**
     * This snippet illustrates creating a feature type style that uses
     * an external graphic
     */
    public void externalGraphicStyle(){
        StyleBuilder styleBuilder = new StyleBuilder();
        Style style = styleBuilder.createStyle();        
        {
            {   PointSymbolizer pointSymbolizer = styleBuilder.createPointSymbolizer();
            
                {   Graphic graphic = styleBuilder.createGraphic();
                    ExternalGraphic external = styleBuilder.createExternalGraphic( "file:///C:/images/house.gif", "image/gif");
                    graphic.graphicalSymbols().add( external );
                    graphic.graphicalSymbols().add( styleBuilder.createMark("circle"));
                    
                    pointSymbolizer.setGraphic(graphic);
                }
                Rule rule = styleBuilder.createRule(pointSymbolizer);            
                FeatureTypeStyle featureTypeStyle = styleBuilder.createFeatureTypeStyle("Feature", rule );
                style.featureTypeStyles().add( featureTypeStyle );
            }
        }
    }
    
    /**
     * This snippet illustrates creating a feature type style with different
     * color fills based on the feature's data. 
     */
    public void fillFromFeatureAttribute() {
        StyleBuilder styleBuilder = new StyleBuilder();
        Style style = styleBuilder.createStyle();

        /*
         * Assume that we are working with a feature type that has
         * an attribute PERCENT with integer values between 0 and 100
         */
        final String typeName = "pretend"; // feature type name
        final String attrName = "PERCENT"; // data attribute name

        // breaks for PERCENT value ranges
        final int[] breaks = {10, 25, 50, 75, 100};

        // corresponding fill colors
        final Color[] colors = {
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.RED
        };

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Rule[] rules = new Rule[breaks.length];

        for (int i = 0; i < breaks.length; i++) {
            // create a fill for this value range
            Fill fill = styleBuilder.createFill(colors[i], 0.5d);  // partially transparent

            // create the polygon outline stroke
            Stroke stroke = styleBuilder.createStroke(colors[i], 1.0d); // line width 1.0

            // create the symbolizer
            PolygonSymbolizer symbolizer = styleBuilder.createPolygonSymbolizer(stroke, fill);

            // create a rule and set the condition (value range) for which features
            // it will apply to
            Rule rule = styleBuilder.createRule(symbolizer);
            Filter filter = ff.lessOrEqual(ff.property(attrName), ff.literal(breaks[i]));
            rule.setFilter(filter);

            // if this is not the first rule it is an 'else' rule
            rule.setIsElseFilter(i > 0);

            rules[i] = rule;
        }

        // package our rules as a feature type style
        FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(typeName, rules);

        // package that in our Style and we're ready to render !
        style.featureTypeStyles().add(fts);
    }
}
