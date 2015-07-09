package org.geotools.geojson;

import java.io.FileInputStream;
import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Feature;
import junit.framework.TestCase;

public class GEOT5158RegressionTest extends TestCase{
	
	@Test
	public void testArrayOfObjectsParsing(){
		SimpleFeatureCollection fc;
		FeatureJSON io = new FeatureJSON();
		try {
			fc = (SimpleFeatureCollection) io.readFeatureCollection(this.getClass().getResourceAsStream("sample.geojson"));
			
			FeatureIterator iterator = fc.features(); 
		    while( iterator.hasNext() ){
		          Feature feature = iterator.next();
		          System.out.println("------ Feature information ------");
		          //System.out.println(feature.toString());
		          System.out.println("isBridge : " + feature.getProperty("isBridge").getValue());
		          System.out.println("isDetailedCity : " + feature.getProperty("isDetailedCity").getValue());
		          System.out.println("isUrban : " + feature.getProperty("isUrban").getValue());
		          System.out.println("directionOfTravelSource : " + feature.getProperty("directionOfTravelSource").getValue());
		          System.out.println("supplementalGeometry : " + feature.getProperty("supplementalGeometry").getValue());
		          System.out.println("roadNames : " + feature.getProperty("roadNames").getValue());
		          System.out.println("mapEdgeId: " + feature.getProperty("mapEdgeId").getValue());
		          System.out.println("Intersection Category : " + feature.getProperty("intersectionCategory").getValue());
		          System.out.println("functional Class : " + feature.getProperty("functionalClass").getValue());
		          System.out.println("Direction of Travel : " + feature.getProperty("directionOfTravel").getValue());
		          Assert.assertEquals("Both", feature.getProperty("directionOfTravel").getValue());
		          Assert.assertNotNull("Object RoadNames is null", feature.getProperty("roadNames").getValue());
		     }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
