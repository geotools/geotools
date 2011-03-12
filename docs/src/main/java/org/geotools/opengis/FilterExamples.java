package org.geotools.opengis;

import java.util.Collections;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

public class FilterExamples {
   public void ffExample(){
       // start ff example
       FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
       Filter filter;
       
       filter = Filter.INCLUDE;
       filter = Filter.EXCLUDE;
       
       filter = ff.like(ff.property("age"), "2%" );
       filter = ff.like(ff.property("age"), "2?", "*", "?", "\\");
       
       filter = ff.between( ff.property("age"), ff.literal(20), ff.literal("29") );
       // end ff example
       
       FunctionFinder finder = new FunctionFinder(null);
       finder.findFunction("pi", Collections.emptyList(), ff.literal(Math.PI));
       
   }
}
