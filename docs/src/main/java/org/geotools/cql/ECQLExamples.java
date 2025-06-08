/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.temporal.During;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;

/**
 * This class gathers up the ECQL examples shown in the sphinx documentation.
 *
 * <p>WORK IN PROGESS
 *
 * @author Mauricio Pazos
 */
public class ECQLExamples {

    private static final Logger LOGGER = Logger.getLogger(ECQLExamples.class.getName());

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            for (; ; ) {

                System.out.println("Select the ECQL Example:");
                System.out.println(" 1 - CQL Compatibility");
                System.out.println(" 2 - Comparison with property in the right hand: 1000 <= POPULTATION");
                System.out.println(" 3 - Using functions in comparison predicate: ..."); // TODO
                System.out.println(" 4 - like pattern using String: 'aabbcc' LIKE '%bb%'");
                System.out.println(" 5 - like using property name: cityName LIKE 'New%");
                System.out.println(" 6 - Insensitive like: cityName ILIKE 'new%'");
                System.out.println(
                        " 7 - Comparison: (under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
                System.out.println(" 8 - Between: population Between 10000 and 20000");
                System.out.println(" 9 - area( shape ) BETWEEN 10000 AND 30000");
                System.out.println(
                        "10 - Spatial Operation using the contains DE-9IM: RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
                System.out.println(
                        "11 - Spatial Operation using geometry expressions: INTERSECTS(POLYGON((1 2, 2 2, 2 3, 1 2)), POINT(1 2))"); // TODO
                System.out.println("12 - Temporal After: 2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
                System.out.println("13 - Temporal Before: 2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
                System.out.println(
                        "14 - Temporal During: 2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ");
                System.out.println(
                        "15 - Temporal During: lastEarthQuake DURING 1700-01-01T00:00:00Z/2011-01-01T00:00:00Z");
                System.out.println("16 - In predicate: principalMineralResource IN ('silver','oil', 'gold' )");
                System.out.println(
                        "17 - Temporal During using UTC Zone +3: 2006-11-30T01:00:00+03:00 DURING 2006-11-30T00:30:00+03:00/2006-11-30T01:30:00+03:00");
                System.out.println(
                        "18 - Spatial Operation using the contains DE-9IM: RELATE(geometry, SRID=4326;LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");

                System.out.println("0 - Quit");
                System.out.print(">");

                String line = reader.readLine();
                if (line == null) {
                    return;
                }
                int option = Integer.parseInt(line);
                switch (option) {
                    case 0:
                        System.out.println("bye!");
                        return;
                    case 1:
                        comparisonPredicateCQLCompatibility();
                        break;
                    case 2:
                        expressionLessThanOrEqualToProperty();
                        break;
                    case 3: // TODO
                        break;
                    case 4:
                        likePredicateInString();
                        break;
                    case 5:
                        likePredicate();
                        break;
                    case 6:
                        ilikePredicate();
                        break;
                    case 7:
                        comparisonUsingExpressions();
                        break;
                    case 8:
                        betweenPredicate();
                        break;
                    case 9:
                        betweenUsingExpression();
                        break;
                    case 10:
                        // TODO
                        break;
                    case 11:
                        relatePattern();
                        break;
                    case 12:
                        afterPredicateWithLefHandtExpression();
                        break;
                    case 13:
                        beforePredicateWithLefHandtExpression();
                        break;
                    case 14:
                        duringPredicateWithLefHandtExpression();
                        break;
                    case 15:
                        duringPredicateWithLefHandtAttribute();
                        break;

                    case 16:
                        inPredicate();
                        break;
                    case 17:
                        utcTimeZone();
                        break;

                    case 18:
                        referencedRelatePattern();
                        break;

                    default:
                        System.out.println("invalid option");
                }
                System.out.println("Press a key to continue.");
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /** Example using the "contains" intersection matrix. */
    private static void relatePattern() throws Exception {

        // ecql relatePattern start
        Filter filter =
                ECQL.toFilter("RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
        // ecql relatePattern end
        Utility.prittyPrintFilter(filter);

        SimpleFeature usa = DataExamples.getInstanceOfCountry();

        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Example using the "contains" intersection matrix. */
    private static void referencedRelatePattern() throws Exception {

        // ecql referencedRelatePattern start
        Filter filter = ECQL.toFilter(
                "RELATE(geometry, SRID=4326;LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
        // ecql referencedRelatePattern end
        Utility.prittyPrintFilter(filter);

        SimpleFeature usa = DataExamples.getInstanceOfCountry();

        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void afterPredicateWithLefHandtExpression() throws Exception {

        // afterPredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
        // afterPredicateWithLefHandtExpression end
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void beforePredicateWithLefHandtExpression() throws Exception {

        // beforePredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
        // beforePredicateWithLefHandtExpression end
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void duringPredicateWithLefHandtExpression() throws Exception {

        // duringPredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ");
        // duringPredicateWithLefHandtExpression end
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void utcTimeZone() throws Exception {

        // utcTimeZone start
        Filter filter =
                ECQL.toFilter("2006-11-30T01:00:00+03:00 DURING 2006-11-30T00:30:00+03:00/2006-11-30T01:30:00+03:00 ");
        // utcTimeZone end
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);

        During during = (During) filter;
        Literal literal = (Literal) during.getExpression1();
        Date date = (Date) literal.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        TimeZone tz = TimeZone.getTimeZone("GMT+0300");
        sdf.setTimeZone(tz);
        System.out.println("Expression 1 as Date: " + sdf.format(date));
    }

    private static void duringPredicateWithLefHandtAttribute() throws Exception {

        // duringPredicateWithLefHandtAttribute start
        During filter = (During) ECQL.toFilter("lastEarthQuake DURING 1700-01-01T00:00:00Z/2011-01-01T00:00:00Z");
        // duringPredicateWithLefHandtAttribute end
        Utility.prittyPrintFilter(filter);

        final SimpleFeature city = DataExamples.getInstanceOfCity();
        Expression leftExpr = filter.getExpression1();
        Expression rightExpr = filter.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** This example shows that ECQL syntax is compatible with CQL. */
    private static void comparisonPredicateCQLCompatibility() throws Exception {

        // comparisonCQLCompatibility start
        Filter filter = ECQL.toFilter("POPULTATION >= 1000");
        // comparisonCQLCompatibility end

        // the same syntax, now using CQL parser will produce the same filter
        Filter filterFromCQL = CQL.toFilter("POPULTATION >= 1000");

        if (!filter.equals(filterFromCQL)) {
            throw new Exception("uncompatible ECQL Syntax");
        } else {
            System.out.println("CQL and ECQL have produced the same filter for the predicate \"POPULTATION >= 1000\"");
            Utility.prittyPrintFilter(filter);
        }
    }

    /** ECQL allows expressions in the left hand of comparison predicate. */
    private static void expressionLessThanOrEqualToProperty() throws Exception {

        // ecql expressionLessThanOrEqualToProperty start
        Filter filter = ECQL.toFilter("1000 <= population");
        // ecql expressionLessThanOrEqualToProperty end

        Utility.prittyPrintFilter(filter);

        SimpleFeature usa = DataExamples.getInstanceOfCountry();
        System.out.println("Country: " + usa.getProperty("countryName").getValue());
        System.out.println("Population: " + usa.getProperty("population").getValue());

        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Comparison between persons under 18 years old and over 65 years old */
    private static void comparisonUsingExpressions() throws Exception {

        // comparisonUsingExpressions start
        Filter filter = ECQL.toFilter("(under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
        // comparisonUsingExpressions end
        SimpleFeature city = DataExamples.getInstanceOfCity();

        Utility.prittyPrintFilter(filter);

        PropertyIsLessThan lessThan = (PropertyIsLessThan) filter;
        Expression leftExpr = lessThan.getExpression1();
        Expression rightExpr = lessThan.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = lessThan.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void betweenPredicate() throws Exception {

        // ecql betweenPredicate start
        Filter filter = ECQL.toFilter("population BETWEEN 10000000 and 20000000");
        // ecql betweenPredicate end
        Utility.prittyPrintFilter(filter);

        SimpleFeature city = DataExamples.getInstanceOfCity();

        System.out.println("City population: " + city.getProperty("population").getValue());

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void betweenUsingExpression() throws Exception {

        // betweenUsingExpression start
        Filter filter = ECQL.toFilter("area( Polygon((10 10, 20 10, 20 20, 10 10)) ) BETWEEN 10000 AND 30000");
        // betweenUsingExpression end
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Matching a property with a text pattern (case sensitive) */
    private static void likePredicate() throws Exception {
        // ecql likePredicate start
        Filter filter = ECQL.toFilter("cityName LIKE 'New%'");
        // ecql likePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Matching a property with a text pattern (case insensitive) */
    private static void ilikePredicate() throws Exception {

        // ecql ilikePredicate start
        Filter filter = ECQL.toFilter("cityName ILIKE 'new%'");
        // ecql ilikePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Matching a literal string */
    private static void likePredicateInString() throws Exception {
        // ecql likePredicateInString start
        Filter filter = ECQL.toFilter("'aabbcc' LIKE '%bb%'");
        // ecql likePredicateInString end

        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void inPredicate() throws Exception {
        // ecql inPredicate start
        Filter filter = ECQL.toFilter("principalMineralResource IN ('silver','oil', 'gold' )");
        // ecql inPredicate end

        SimpleFeature country = DataExamples.getInstanceOfCountry();
        System.out.println("coutry: " + country.getAttribute("countryName"));
        System.out.println("population: " + country.getAttribute("principalMineralResource"));

        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(country);

        System.out.println("Result of filter evaluation: " + result);
    }
}
