package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.temporal.During;

/**
 * This class gathers up the ECQL examples shown in the sphinx documentation.
 * 
 * WORK IN PROGESS 
 * 
 * @author Mauricio Pazos
 * 
 */
public class ECQLExamples {

    private final static Logger LOGGER = Logger.getLogger(ECQLExamples.class
            .getName());

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            for (;;) {

                System.out.println("Select the ECQL Example:");
                System.out.println(" 1 - CQL Compatibility");
                System.out.println(" 2 - Comparison with property in the right hand: 1000 <= POPULTATION");
                System.out.println(" 3 - Using functions in comparison predicate: ..."); // TODO
                System.out.println(" 4 - like pattern using String: 'aabbcc' LIKE '%bb%'");
                System.out.println(" 5 - like using property name: cityName LIKE 'New%");
                System.out.println(" 6 - Insensitive like: cityName ILIKE 'new%'");
                System.out.println(" 7 - Comparison: (under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
                System.out.println(" 8 - Between: population Between 10000 and 20000");
                System.out.println(" 9 - area( shape ) BETWEEN 10000 AND 30000");
                System.out.println("10 - Spatial Operation using the contains DE-9IM: RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)"); 
                System.out.println("11 - Temporal After: 2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
                System.out.println("12 - Temporal Before: 2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
                System.out.println("13 - Temporal During: 2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ");
                System.out.println("14 - Temporal During: lastEarthQuake DURING 1700-01-01T00:00:00Z/2011-01-01T00:00:00Z");
                System.out.println("15 - In predicate: principalMineralResource IN ('silver','oil', 'gold' )");
                
                System.out.println("0 - quite");
                System.out.print(">");

                String line = reader.readLine();
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
                    relatePattern();
                    break;
                case 11:
                    afterPredicateWithLefHandtExpression();
                    break;
                case 12:
                    beforePredicateWithLefHandtExpression();
                    break;
                case 13:
                    duringPredicateWithLefHandtExpression();
                    break;
                case 14:
                	duringPredicateWithLefHandtAttribute();
                	break;
                	
                case 15:
                	inPredicate();
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


    /**
     * Example using the "contains" intersection matrix.
     *  
     * @throws Exception
     */
    private static void relatePattern() throws Exception{
    	
    	// ecql relatePattern start
        Filter filter = ECQL.toFilter( "RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
    	// ecql relatePattern end
		Utility.prittyPrintFilter(filter);
		
        SimpleFeature usa = DataExamples.getInstanceOfCountry();
		
        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
		
	}


	private static void afterPredicateWithLefHandtExpression() throws Exception{
    	
    	// afterPredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z");
    	// afterPredicateWithLefHandtExpression end
		Utility.prittyPrintFilter(filter);
		
        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
		
	}
    private static void beforePredicateWithLefHandtExpression() throws Exception{
    	
    	// beforePredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z");
    	// beforePredicateWithLefHandtExpression end
        Utility.prittyPrintFilter(filter);
		
        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
		
	}
    
    
    private static void duringPredicateWithLefHandtExpression() throws Exception{
    	
    	// duringPredicateWithLefHandtExpression start
        Filter filter = ECQL.toFilter("2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ");
    	// duringPredicateWithLefHandtExpression end
        Utility.prittyPrintFilter(filter);
		
        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
	}

    private static void duringPredicateWithLefHandtAttribute() throws Exception{
    	
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


    /**
     * This example shows that ECQL syntax is compatible with CQL.
     * 
     * @throws Exception
     */
    static private void comparisonPredicateCQLCompatibility() throws Exception {

        // comparisonCQLCompatibility start
        Filter filter = ECQL.toFilter("POPULTATION >= 1000");
        // comparisonCQLCompatibility end

        // the same syntax, now using CQL parser will produce the same filter
        Filter filterFromCQL = CQL.toFilter("POPULTATION >= 1000");

        if (!filter.equals(filterFromCQL)) {
            throw new Exception("uncompatible ECQL Syntax");
        } else {
            System.out.println("CQL and ECQL have produced the same filter for the predicate \"POPULTATION >= 1000\"");
            Utility.prittyPrintFilter( filter );
        }
    }


    /**
     * ECQL allows expressions in the left hand of comparison predicate.
     * 
     * @throws Exception
     */
    static private void expressionLessThanOrEqualToProperty() throws Exception {

        
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

    /**
     * Comparison between persons under 18 years old and over 65 years old
     * @throws Exception
     */
    static private void comparisonUsingExpressions() throws Exception {
        
        // comparisonUsingExpressions start
        Filter filter = ECQL.toFilter("(under18YearsOld * 19541453 / 100 ) < (over65YearsOld * 19541453 / 100 )");
        // comparisonUsingExpressions end
        SimpleFeature city = DataExamples.getInstanceOfCity();
        
        Utility.prittyPrintFilter(filter);

        PropertyIsLessThan lessThan =(PropertyIsLessThan) filter; 
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


    
    /**
     *  Matching a property with a text pattern (case sensitive)
     * 
     * @throws Exception
     */
    static private void likePredicate() throws Exception {
        // ecql likePredicate start
        Filter filter = ECQL.toFilter("cityName LIKE 'New%'");
        // ecql likePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }

    /**
     *  Matching a property with a text pattern (case insensitive)
     * 
     * @throws Exception
     */
    static private void ilikePredicate() throws Exception {

        // ecql ilikePredicate start
        Filter filter = ECQL.toFilter("cityName ILIKE 'new%'");
        // ecql ilikePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }

    /**
     * Matching a literal string
     * 
     * @throws Exception
     */
    static private void likePredicateInString() throws Exception {
        // ecql likePredicateInString start
        Filter filter = ECQL.toFilter("'aabbcc' LIKE '%bb%'");
        // ecql likePredicateInString end
        
        Utility.prittyPrintFilter(filter);

        Boolean result = filter.evaluate(null);
        System.out.println("Result of filter evaluation: " + result);
    }

    static private void inPredicate() throws Exception {
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
