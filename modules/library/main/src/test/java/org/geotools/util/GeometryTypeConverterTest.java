package org.geotools.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.test.TestData;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
/**
 * Test suite for the GeometryTypeConverterFactory Converters.
 * 
 * @author Mauro Bartolomeoli
 *
 *
 * @source $URL$
 */
public class GeometryTypeConverterTest extends TestCase {
	Set<ConverterFactory> factories=null;
	List<String> tests=new ArrayList<String>();
	
	WKTReader wktReader=new WKTReader();

	
	protected Converter getConverter(Geometry source,Class<?> target) {
		assertNotNull("Cannot get ConverterFactory for Geometry -> Geometry conversion",factories);
		if(factories==null)
			return null;
		for(ConverterFactory factory : factories) {
			Converter candidate=factory.createConverter(source.getClass(), target,null);
			if(candidate!=null)
				return candidate;
		}
		fail("Cannot get ConverterFactory for "+source.getClass().getName()+" -> "+target.getClass().getName()+" conversion");
		return null;
	}
	
	public void setUp() throws Exception {
		factories=Converters.getConverterFactories(Geometry.class, Geometry.class);
		File testData=TestData.file(this, "converter/tests.txt");
		assertNotNull("Cannot find test file (converter.txt)",testData);		
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(testData));
			String line=null;
			
			while((line=reader.readLine())!=null)
				tests.add(line);
				
		} finally {
			if(reader!=null)
				reader.close();
		}
		
		
        super.setUp();
    }
	
	public void tearDown() throws Exception {        
        super.tearDown();
    }
	
	public void testData() throws Exception {
		for(String test : tests) {
			String[] parts=test.split(":");
			assertEquals("Test rows should have the form \"source:target:expected:description\"",4,parts.length);
			
			Geometry source=wktReader.read(parts[0]);						
			Class<?> target=Class.forName("com.vividsolutions.jts.geom."+parts[1]);
			String expected=parts[2];
			Geometry converted=convert(source, target);
			assertEquals(expected,converted.toText());			
//			System.out.println(parts[3]+": OK");
		}
	
	}

	private Geometry convert(Geometry source, Class<?> target) throws Exception {		
		Converter converter=getConverter(source, target);
		Object dest=converter.convert(source, target);
		assertNotNull("Cannot convert "+source.toText()+" to "+target.getName(),dest);
		assertTrue("Converted object is not a Geometry",dest instanceof Geometry);
		return (Geometry)dest;
	}
	
	
}
