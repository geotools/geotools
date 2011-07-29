package org.geotools.cql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 * This class gathers up the CQL examples shown in the sphinx documentation.
 * 
 * WORK IN PROGRESS
 * 
 * 
 * @author Mauricio Pazos
 *
 */
public class CQLExamples {

	private static final Logger LOGGER = Logger.getLogger(CQLExamples.class.getName());


	public static void main(String[] args){
		
		try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {

                System.out.println("Select the CQL Example:");
                System.out.println(" 1 - Comparison: population >= 10000000"); 
                System.out.println(" 2 - Using functions in comparison predicate: "); // TODO
                System.out.println(" 3 - like using property name: cityName LIKE 'New%");
                System.out.println(" 4 - Between: population Between 10000 and 20000");
                System.out.println(" 5 - Spatial Operation using the contains DE-9IM: RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)"); // FIXME
                System.out.println(" 6 - Temporal After: 2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z"); // TODO
                System.out.println(" 7 - Temporal Before: 2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z"); // TODO
                System.out.println(" 8 - Temporal During: 2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z "); // TODO
                System.out.println(" 9 - Temporal During: lastEarthQuake DURING 1700-01-01T00:00:00Z/2011-01-01T00:00:00Z"); // TODO
                
                System.out.println(" 0 - quite");
                System.out.print(">");

                String line = reader.readLine();
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
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
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
    	
    	// cql relatePattern start
        Filter filter = CQL.toFilter( "RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");
    	// cql relatePattern end
		Utility.prittyPrintFilter(filter);
		
        SimpleFeature usa = DataExamples.getInstanceOfCountry();
		
        Boolean result = filter.evaluate(usa);
        System.out.println("Result of filter evaluation: " + result);
		
	}

	
	
    static private void comparisonPredicate() throws Exception {
        // cql comparison start
        Filter filter = CQL.toFilter("population >= 10000000");
        // cql comparison end

        SimpleFeature city = DataExamples.getInstanceOfCity();

        System.out.println("City population: " + city.getProperty("population").getValue());
        
        Boolean result = filter.evaluate(city);
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

    
    /**
     *  Matching a property with a text pattern (case sensitive)
     * 
     * @throws Exception
     */
    static private void likePredicate() throws Exception {
        // cql likePredicate start
        Filter filter = ECQL.toFilter("cityName LIKE 'New%'");
        // cql likePredicate end

        Utility.prittyPrintFilter(filter);

        SimpleFeature newYork = DataExamples.getInstanceOfCity();
        System.out.println("City Name: " + newYork.getProperty("cityName").getValue());

        Boolean result = filter.evaluate(newYork);
        System.out.println("Result of filter evaluation: " + result);
    }
    
	
}
