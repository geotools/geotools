
package org.geotools.cql;

import org.opengis.filter.Filter;

/**
 * This class maintains set of common functions used in the CQL and ECQL examples
 * 
 * @author Mauricio Pazos
 *
 */
final class Utility {

	
	private Utility(){
		// utility class
	}
	public  static void prittyPrintFilter(Filter filter) {
        System.out.println("The filter result is:\n" + filter.toString());         
    }

}
