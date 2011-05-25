/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.brewer.color;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.ClassificationFunction;
import org.geotools.filter.function.EqualIntervalFunction;
import org.geotools.filter.function.ExplicitClassifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;


/**
 *
 *
 * @source $URL$
 */
public class StyleGeneratorTest extends DataTestCase {
    public StyleGeneratorTest(String arg0) {
        super(arg0);
    }

    public void checkFilteredResultNotEmpty(Rule[] rule, SimpleFeatureSource fs, String attribName)
        throws IOException {
        for (int i = 0; i < rule.length; i++) {
            Filter filter = rule[i].getFilter();
            SimpleFeatureCollection filteredCollection = fs.getFeatures(filter);
            assertTrue(filteredCollection.size() > 0);

            String filterInfo = "Filter \"" + filter.toString() + "\" contains "
                + filteredCollection.size() + " element(s) (";
            SimpleFeatureIterator it = filteredCollection.features();

            while (it.hasNext()) {
                SimpleFeature feature = (SimpleFeature) it.next();
                filterInfo += ("'" + feature.getAttribute(attribName) + "'");

                if (it.hasNext()) {
                    filterInfo += ", ";
                }
            }

            it.close();
            System.out.println(filterInfo + ")");
        }
    }

    public void testComplexExpression() throws Exception {
        ColorBrewer brewer = new ColorBrewer();
        brewer.loadPalettes();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Expression expr = null;
        Expression expr2 = null;
        SimpleFeatureType type = roadType;
        String attribName = type.getDescriptor(0).getLocalName();
        SimpleFeatureCollection fc = DataUtilities.collection(roadFeatures);
        SimpleFeatureSource fs = DataUtilities.source(fc);

        try {
            expr = ff.multiply(ff.property(attribName), ff.property(attribName));
            expr2 = ff.add(expr, ff.literal(3));
        } catch (IllegalFilterException e) {
            fail(e.getMessage());
        }

        String paletteName = "YlGn"; //type = Sequential

        //create the classification function
        ClassificationFunction function = new EqualIntervalFunction();
        List params = new ArrayList();
        params.add(0, expr2); //expression
        params.add(1, ff.literal(2)); //classes
        function.setParameters(params);

        Object object = function.evaluate(fc);
        assertTrue(object instanceof RangedClassifier);

        RangedClassifier classifier = (RangedClassifier) object;

        Color[] colors = brewer.getPalette(paletteName).getColors(2);

        // get the fts
        FeatureTypeStyle fts = StyleGenerator.createFeatureTypeStyle(classifier, expr2, colors,
                "myfts", roadFeatures[0].getFeatureType().getGeometryDescriptor(),
                StyleGenerator.ELSEMODE_IGNORE, 0.5, null);
        assertNotNull(fts);

        // test each filter
        Rule[] rule = fts.getRules();
        assertEquals(2, rule.length);
        //do a preliminary test to make sure each rule's filter returns some results
        checkFilteredResultNotEmpty(rule, fs, attribName);

        assertNotNull(StyleGenerator.toStyleExpression(rule[0].getFilter()));
        assertNotNull(StyleGenerator.toStyleExpression(rule[1].getFilter()));
    }
    
    
    /**
     * This test cases test the generation of a style
     * using a ExcplicitClassifier
     */
    public void testExplicitClassifier(){
    	 ColorBrewer brewer = new ColorBrewer();
         brewer.loadPalettes();

         FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
         Expression expr = null;

         SimpleFeatureType type = riverType;
         final String attribName = "river";
         SimpleFeatureCollection fc = DataUtilities.collection(riverFeatures);

         expr = ff.property(attribName);

         String paletteName = "YlGn"; //type = Sequential
         
         //TEST classifier with everything in a single bin
         final Set<String>[] binValues2 = new Set[1];
         binValues2[0] = new HashSet<String>();
         //assign each of the features to one of the bins
         try {
			fc.accepts(new FeatureVisitor(){
				public void visit(Feature feature) {				
					binValues2[0].add(((SimpleFeature)feature).getAttribute(attribName).toString() );
					
				}}, null);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
         
		 ExplicitClassifier classifier = new ExplicitClassifier(binValues2);
         Color[] colors = brewer.getPalette(paletteName).getColors(binValues2.length);

         // get the fts
         FeatureTypeStyle fts = StyleGenerator.createFeatureTypeStyle(classifier, expr, colors,
                 "myfts", riverFeatures[0].getFeatureType().getGeometryDescriptor(),
                 StyleGenerator.ELSEMODE_IGNORE, 0.5, null);
         assertNotNull(fts);

         // test each filter
         //we would expect two rules here - one for each of the two bins created
         List<Rule> rules = fts.rules();
         assertEquals(1, rules.size());
         
         //do a preliminary test to make sure each rule's filter returns some results     
         assertNotNull(StyleGenerator.toStyleExpression(rules.get(0).getFilter()));
         final Filter filter = rules.get(0).getFilter();
         try {
			fc.accepts(new FeatureVisitor(){

				public void visit(Feature feature) {
					if (!filter.evaluate(feature)){
						fail("Not all features accepted.");
					}
					
				}}, null);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
         
    }
}
