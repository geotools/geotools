package org.geotools.opengis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.identity.Version;
import org.opengis.filter.identity.Version.Action;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

public class FilterExamples {

public void includeExclude() {
    Filter filter;
    // includeExclude start
    filter = Filter.INCLUDE; // no filter provided! include everything
    filter = Filter.EXCLUDE; // no filter provided! exclude everything
    // includeExclude end
}

public void ffExample() {
    // start ff example
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;

    // the most common selection criteria is a simple equal test
    ff.equal(ff.property("land_use"), ff.literal("URBAN"));

    // You can also quickly test if a property has a value
    filter = ff.isNull(ff.property("approved"));

    // The usual array of property comparisons is supported
    // the comparison is based on the kind of data so both
    // numeric, date and string comparisons are supported.
    filter = ff.less(ff.property("depth"), ff.literal(300));
    filter = ff.lessOrEqual(ff.property("risk"), ff.literal(3.7));
    filter = ff.greater(ff.property("name"), ff.literal("Smith"));
    filter = ff.greaterOrEqual(ff.property("schedule"), ff.literal(new Date()));

    // PropertyIsBetween is a short inclusive test between two values
    filter = ff.between(ff.property("age"), ff.literal(20), ff.literal("29"));
    filter = ff.between(ff.property("group"), ff.literal("A"), ff.literal("D"));

    // In a similar fashion there is a short cut for notEqual
    filter = ff.notEqual(ff.property("type"), ff.literal("draft"));

    // pattern based "like" filter
    filter = ff.like(ff.property("code"), "2300%");
    // you can customise the wildcard characters used
    filter = ff.like(ff.property("code"), "2300?", "*", "?", "\\");
    // end ff example
}

public void nilExample() {
    // start nil example
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;

    // previous example tested if approved equals "null"
    filter = ff.isNull(ff.property("approved"));

    // this example checks if approved exists at all
    filter = ff.isNil(ff.property("approved"),"no approval available");

    // end nil example
}

public void id(){
    // id start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    
    filter = ff.id(ff.featureId("CITY.98734597823459687235"),
                   ff.featureId("CITY.98734592345235823474"));
    // id end
}

public void rid(){
    // rid start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    
    filter = ff.id(ff.resourceId("CITY.98734597823459687235","A457",new Version(Action.PREVIOUS) ));
    // rid end
}

public void idSet(){
    // idSet start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    
    Set<FeatureId> selected = new HashSet<FeatureId>();
    selected.add(ff.featureId("CITY.98734597823459687235"));
    selected.add(ff.featureId("CITY.98734592345235823474"));
    
    filter = ff.id(selected);
    // idSet end
}


public void logical() {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    // logical start

    // you can use *not* to invert the test; this is especially handy
    // with like filters (allowing you to select content that does not
    // match the provided pattern)
    filter = ff.not(ff.like(ff.property("code"), "230%"));

    // you can also combine filters to narrow the results returned
    filter = ff.and(ff.greater(ff.property("rainfall"), ff.literal(70)),
            ff.equal(ff.property("land_use"), ff.literal("urban"), false));

    filter = ff.or(ff.equal(ff.property("code"), ff.literal("approved")),
            ff.greater(ff.property("funding"), ff.literal(23000)));

    // logical end
}

void temporal() throws Exception {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    // temporal start

    // use the default implementations from gt-main

    DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    Date date1 = FORMAT.parse("2001-07-05T12:08:56.235-0700");
    Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));

    // Simple check if property is after provided temporal instant
    Filter after = ff.after(ff.property("date"), ff.literal(temporalInstant));

    // can also check of property is within a certain period
    Date date2 = FORMAT.parse("2001-07-04T12:08:56.235-0700");
    Instant temporalInstant2 = new DefaultInstant(new DefaultPosition(date2));
    Period period = new DefaultPeriod(temporalInstant, temporalInstant2);

    Filter within = ff.toverlaps(ff.property("constructed_date"),
            ff.literal(period));
    // temporal end
}

void caseSensitive() throws Exception {
    // caseSensitive start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    // default is matchCase = true
    Filter filter = ff.equal(ff.property("state"), ff.literal("queensland"));

    // You can override this default with matchCase = false
    filter = ff.equal(ff.property("state"), ff.literal("new south wales"),
            false);

    // All property comparisons allow you to control case sensitivity
    Filter welcome = ff.greater(ff.property("zone"), ff.literal("danger"),
            false);
    // caseSensitive end
}

public void matchAction() {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    // matchAction start
    filter = ff.greater(ff.property("child/age"), ff.literal(12), true,
            MatchAction.ALL);
    // matchAction end
}

public void matchActionAny() {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    // matchActionAny start
    List<Integer> ages = Arrays.asList(new Integer[] { 7, 8, 10, 15 });

    filter = ff.greater(ff.literal(ages), ff.literal(12), false,
            MatchAction.ANY);
    System.out.println("Any: " + filter.evaluate(null)); // prints Any: true
    // matchActionAny end
}

public void matchActionAll() {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    // matchActionAll start
    List<Integer> ages = Arrays.asList(new Integer[] { 7, 8, 10, 15 });

    filter = ff.greater(ff.literal(ages), ff.literal(12), false,
            MatchAction.ALL);
    System.out.println("All: " + filter.evaluate(null)); // prints All: false
    // matchActionAll end
}

public void matchActionOne() {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;
    // matchActionOne start
    List<Integer> ages = Arrays.asList(new Integer[] { 7, 8, 10, 15 });

    filter = ff.greater(ff.literal(ages), ff.literal(12), false,
            MatchAction.ONE);
    System.out.println("One: " + filter.evaluate(null)); // prints One: true
    // matchActionOne end
}

public void bbox() {
    double x1 = 0;
    double x2 = 0;
    double y1 = 0;
    double y2 = 0;
    
    // bbox start
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    ReferencedEnvelope bbox = new ReferencedEnvelope(x1, x2, y1, y2, DefaultGeographicCRS.WGS84 );
    Filter filter = ff.bbox(ff.property("the_geom"), bbox);
    // bbox end
}
}
