package org.geotools.brewer;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.brewer.color.ColorBrewer;
import org.geotools.brewer.color.StyleGenerator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.ExplicitClassifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.styling.FeatureTypeStyle;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;

public class BrewerExamples {

public void classiferExample() {
    SimpleFeatureCollection collection = null;
    SimpleFeature feature = null;
    // classiferExample start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Function classify = ff.function("Quantile", ff.property("name"), ff.literal(2));
    
    Classifier groups = (Classifier) classify.evaluate(collection);
    // classiferExample end
    // classiferExample2 start
    groups.setTitle(0, "Group A");
    groups.setTitle(1, "Group B");
    // classiferExample2 end
    
    // classiferExample3 start
    // groups is a classifier with "Group A" and "Group B"
    Function sort = ff.function("classify", ff.property("name"), ff.literal(groups));
    int slot = (Integer) sort.evaluate(feature);
    
    System.out.println(groups.getTitle(slot)); // ie. "Group A"
    // classiferExample3 end
}

public void classiferQuantile() {
    SimpleFeatureCollection collection = null;
    SimpleFeature feature = null;
    // classiferQuantile start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Function classify = ff.function("Quantile", ff.property("zone"), ff.literal(2));
    
    // Zones assigned by a municipal board do not have an intrinsic numerical
    // meaning making them suitable for display using:
    // - qualitative palette where each zone would have the same visual impact
    Classifier groups = (Classifier) classify.evaluate(collection);
    // classiferQuantile end
}

public void classiferEqualInterval() {
    SimpleFeatureCollection collection = null;
    SimpleFeature feature = null;
    // classiferEqualInterval start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Function classify = ff.function("EqualInterval", ff.property("height"), ff.literal(5));
    
    // this will create a nice smooth series of intervals suitable for presentation
    // with:
    // - sequential color palette to make each height blend smoothly into the next
    // - diverging color palettes if you want to make higher and lower areas stand out more
    Classifier height = (Classifier) classify.evaluate(collection);
    // classiferEqualInterval end
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public void explicitClassifierExample() {
    // explicitClassifierExample start
    Set[] zones = new Set[3];
    // urban commercial or residencial
    zones[0] = new HashSet(Arrays.asList(new String[] { "Zone 1", "Zone 2", }));
    // municipal or crown parkland
    zones[1] = new HashSet(Arrays.asList(new String[] { "Zone 4", "Crown 2", }));
    // industrial
    zones[2] = new HashSet(Arrays.asList(new String[] { "Zone 3" }));
    
    Classifier landuse = new ExplicitClassifier(zones);
    landuse.setTitle(0, "urban");
    landuse.setTitle(1, "park");
    landuse.setTitle(2, "industrial");
    // explicitClassifierExample end
}

public void rangedClassifierExample() {
    // rangedClassifierExample start
    Comparable min[] = new Comparable[25];
    Comparable max[] = new Comparable[25];
    for (int i = 0; i < 25; i++) {
        min[i] = (char) ('A' + i);
        max[i] = (char) ('B' + i);
    }
    Classifier alphabetical = new RangedClassifier(min, max);
    // rangedClassifierExample end
}

void colorBrewerExample(SimpleFeatureCollection featureCollection) {
    // colorBrewerExample start
    // STEP 0 Set up Color Brewer
    ColorBrewer brewer = ColorBrewer.instance();
    
    // STEP 1 - call a classifier function to summarise your content
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    PropertyName propteryExpression = ff.property("height");
    
    // classify into five categories
    Function classify = ff.function("Quantile", propteryExpression, ff.literal(5));
    Classifier groups = (Classifier) classify.evaluate(featureCollection);
    
    // STEP 2 - look up a predefined palette from color brewer
    String paletteName = "GrBu";
    Color[] colors = brewer.getPalette(paletteName).getColors(5);
    
    // STEP 3 - ask StyleGenerator to make a set of rules for the Classifier
    // assigning features the correct color based on height
    FeatureTypeStyle style = StyleGenerator.createFeatureTypeStyle(
            groups,
            propteryExpression,
            colors,
            "Generated FeatureTypeStyle for GreeBlue",
            featureCollection.getSchema().getGeometryDescriptor(),
            StyleGenerator.ELSEMODE_IGNORE,
            0.95,
            null);
    // colorBrewerExample end
}
}