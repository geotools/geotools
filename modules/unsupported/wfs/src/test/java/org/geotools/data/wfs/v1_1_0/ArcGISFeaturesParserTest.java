package org.geotools.data.wfs.v1_1_0;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.junit.Test;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

public class ArcGISFeaturesParserTest {
	private static SimpleFeatureSource featureSource;
	private static SimpleFeatureCollection featureCollection;
	private static DataStore data;
	private static final String layerName = new String("Projected_StPaul");
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String wfsEndPoint = new String("http://us-dspatialgis.oit.umn.edu:6080/arcgis/services/Test_SaintPaul/TestStPaul/MapServer/WFSServer");
		Boolean isFromArcGISServer = true;
		
		String getCapabilities = wfsEndPoint + "?REQUEST=GetCapabilities&VERSION=1.1.0";
		// Both ArcGIS Server 9.3 and 10 are compliant with WFS 1.1.0, so hard-coding VERSION=1.1.0 should be fine. 

		Map<String, String> connectionParameters = new HashMap<String, String>();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		if (isFromArcGISServer)
			connectionParameters.put("WFSDataStoreFactory:WFS_STRATEGY", "arcgis");
		
		data = DataStoreFinder.getDataStore( connectionParameters );
		
    }
	
	@Test
	public void testParseArcGISServerWFSFeatures() throws Exception{
		
		
		String typeNames[] = data.getTypeNames();
		//find the typeName we're looking for
		String typeName = "";
		for (int i = 0; i < typeNames.length; i++){
			if (typeNames[i].contains(layerName)){
				typeName = typeNames[i];
				break;
			}
		}
		
		featureSource = data.getFeatureSource( typeName );
		featureCollection = featureSource.getFeatures();
		
		Boolean isNotEmpty = !featureCollection.isEmpty();
		assertTrue(isNotEmpty);
	}
	
}
