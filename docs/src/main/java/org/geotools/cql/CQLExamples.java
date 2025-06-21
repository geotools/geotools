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
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.During;
import org.geotools.filter.text.cql2.CQL;

/**
 * This class gathers up the CQL examples shown in the sphinx documentation.
 *
 * <p>WORK IN PROGRESS
 *
 * @author Mauricio Pazos
 */
public class CQLExamples {

    private static final Logger LOGGER = Logger.getLogger(CQLExamples.class.getName());

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            for (; ; ) {

                System.out.println("Select the CQL Example:");
                System.out.println(" 1 - Comparison: population >= 10000000");
                System.out.println(" 2 - Using functions in comparison predicate: "); // TODO
                System.out.println(" 3 - like using property name: cityName LIKE 'New%");
                System.out.println(" 4 - Between: population Between 10000 and 20000");
                System.out.println(
                        " 5 - Spatial Operation using the contains DE-9IM: RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)"); // FIXME
                System.out.println(" 6 - Temporal After: lastEarthQuake AFTER 2006-11-30T01:30:00Z");
                System.out.println(" 7 - Temporal After: lastEarthQuake AFTER 2006-11-30T01:30:00+03:00");
                System.out.println(" 8 - Temporal Before: lastEarthQuake BEFORE 2006-11-30T01:30:00Z");
                System.out.println(
                        " 9 - Temporal During: lastEarthQuake DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ");

                System.out.println(" 0 - quite");
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
                        comparisonPredicate();
                        break;
                    case 2:
                        break;
                    case 3:
                        likePredicate();
                        break;
                    case 4:
                        betweenPredicate();
                        break;
                    case 5:
                        relatePattern();
                        break;
                    case 6:
                        afterPredicate();
                        break;
                    case 7:
                        afterPredicateGMT3();
                        break;
                    case 8:
                        beforePredicate();
                        break;
                    case 9:
                        duringPredicate();
                        break;
                    default:
                        System.out.println("invalid option");
                }
                System.out.println();
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private static void beforePredicate() throws Exception {

        // cql_beforePredicate start
        Before filter = (Before) CQL.toFilter("lastEarthQuake BEFORE 2006-11-30T01:30:00Z");
        // cql_beforePredicate end
        Utility.prittyPrintFilter(filter);

        final SimpleFeature city = DataExamples.getInstanceOfCity();
        Expression leftExpr = filter.getExpression1();
        Expression rightExpr = filter.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void afterPredicate() throws Exception {

        // cql_afterPredicate start
        After filter = (After) CQL.toFilter("lastEarthQuake AFTER 2006-11-30T01:30:00Z");
        // cql_afterPredicate end
        Utility.prittyPrintFilter(filter);

        final SimpleFeature city = DataExamples.getInstanceOfCity();
        Expression leftExpr = filter.getExpression1();
        Expression rightExpr = filter.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void afterPredicateGMT3() throws Exception {

        // cql_afterPredicateGMT3 start
        After filter = (After) CQL.toFilter("lastEarthQuake AFTER 2006-11-30T01:30:00+03:00");
        // cql_afterPredicateGMT3 end
        Utility.prittyPrintFilter(filter);

        final SimpleFeature city = DataExamples.getInstanceOfCity();
        Expression leftExpr = filter.getExpression1();
        Expression rightExpr = filter.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void duringPredicate() throws Exception {

        // cql_duringPredicate start
        During filter = (During) CQL.toFilter("lastEarthQuake DURING 1700-01-01T00:00:00/2011-01-01T00:00:00");
        // cql_duringPredicate end
        Utility.prittyPrintFilter(filter);

        final SimpleFeature city = DataExamples.getInstanceOfCity();
        Expression leftExpr = filter.getExpression1();
        Expression rightExpr = filter.getExpression2();
        System.out.println("left expression value: " + leftExpr.evaluate(city));
        System.out.println("right expression value: " + rightExpr.evaluate(city));

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Example using the "contains" intersection matrix. */
    private static void relatePattern() throws Exception {

        // cql relatePattern start
        Filter filter =
                CQL.toFilter("RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
        // cql relatePattern end
        Utility.prittyPrintFilter(filter);

        SimpleFeature usa = DataExamples.getInstanceOfCountry();

        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void comparisonPredicate() throws Exception {
        // cql comparison start
        Filter filter = CQL.toFilter("population >= 10000000");
        // cql comparison end

        SimpleFeature city = DataExamples.getInstanceOfCity();

        System.out.println("City population: " + city.getProperty("population").getValue());

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    private static void betweenPredicate() throws Exception {

        // cql betweenPredicate start
        Filter filter = CQL.toFilter("population BETWEEN 10000000 and 20000000");
        // cql betweenPredicate end
        Utility.prittyPrintFilter(filter);

        SimpleFeature city = DataExamples.getInstanceOfCity();

        System.out.println("City population: " + city.getProperty("population").getValue());

        Boolean result = filter.evaluate(city);
        System.out.println("Result of filter evaluation: " + result);
    }

    /** Matching a property with a text pattern (case sensitive) */
    private static void likePredicate() throws Exception {
        // cql likePredicate start
        Filter filter = CQL.toFilter("cityName LIKE 'New%'");
        // cql likePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }
}
