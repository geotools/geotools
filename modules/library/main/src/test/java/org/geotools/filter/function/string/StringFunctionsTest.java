package org.geotools.filter.function.string;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;


public class StringFunctionsTest extends TestCase {

	FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
	
	public void testConcatenate() throws Exception {
		Function function = ff.function( "Concatenate",
				ff.literal("hello"), ff.literal(" "), ff.literal("world"));
		assertEquals( "hello world", function.evaluate(null, String.class));
		
	}
}
