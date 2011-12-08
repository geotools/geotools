/**
 * 
 */
package org.geotools.data.ogr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.ogr.bridj.OgrLibrary;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author Mauricio Pazos
 *
 */
public final class OGRDataStoreWriter {

	static {
		GdalInit.init();

		OgrLibrary.OGRRegisterAll();
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {

		// SHP test (OK)
//		try{
//			final String path = "/home/mauro/Downloads/gis-test-data/";
//	        final String file = path + "test-shp.shp";
//	        final String format = "ESRI shapefile";
//	        FeatureCollection features = createFeatureCollection();
//
//	        OGRDataStore dataStore = new OGRDataStore(file, format, null);
//	        
//	        writeFeatures(dataStore, features);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		// TAB test (Fail opening in OGR_Dr_Open(d,s,1)) 
//		try{
//			final String path = "/home/mauro/Downloads/gis-test-data/"; 		
//	        final String file = path + "test-tab.tab";
//	        final String format = "MapInfo File";
//	        FeatureCollection features = createFeatureCollectionForMapInfoDriver();
//
//	        OGRDataStore dataStore = new OGRDataStore(file, format, null);
//	        
//	        writeFeatures(dataStore, features);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// this use the createSchema to write all features (works OK)
		try{
			final String path = "/home/mauro/Downloads/gis-test-data/"; 		
	        final String file = path + "test-tab.tab";
	        final String format = "MapInfo File";
	        SimpleFeatureCollection features = createFeatureCollectionForMapInfoDriver();

	        OGRDataStore dataStore = new OGRDataStore(file, format, null);
	    
			dataStore.createSchema( features, true, new String[]{} );
			
			dataStore.dispose();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
    private static void writeFeatures(DataStore dataStore, FeatureCollection<SimpleFeatureType, SimpleFeature> fc) throws Exception {
        
    	// creates the feature writer using the schema of source feature collection.
    	SimpleFeatureType schema = fc.getSchema();

        dataStore.createSchema(schema);
        String typeName = dataStore.getTypeNames()[0];
		FeatureWriter fw = dataStore.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);

        System.out.println("adding features ....");
        FeatureIterator it = fc.features();
        while (it.hasNext()) {
            SimpleFeature sf = (SimpleFeature) it.next();
            ((SimpleFeature) fw.next()).setAttributes(sf.getAttributes());
            fw.write();
        }
        it.close();
        fw.close();
        System.out.println("the end!!!");

    }
	
    protected static ListFeatureCollection createFeatureCollectionForMapInfoDriver() throws Exception {
        
        SimpleFeatureType type =buildFeatureTypeForMapInfo();
      
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        ListFeatureCollection features = new ListFeatureCollection(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            features.add(
            	fb.buildFeature(null, 
	            	new Object[] {
	                    new GeometryFactory().createPoint(new Coordinate(1, -1)), 
	                    //new Byte((byte) i),
	                    //new Short((short) i), 
	                    //new Double(i), 
	                    //new Float(i), 
	                    new String(i + "strValue"),
	                    //new Date(i), 
	                    //new Boolean(true), 
	                    //new Integer(22),
	                    //new Long(1234567890123456789L),
	                    // new BigDecimal(new BigInteger("12345678901234567890123456789"), 2),
	                    //new BigInteger("12345678901234567890123456789") 
	                    }));
        }
        return features;
    }
    private static SimpleFeatureType buildFeatureTypeForMapInfo(){
    	
        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setCRS(DefaultGeographicCRS.WGS84);
        tbuilder.setName("junk");
        
        tbuilder.add("a", Point.class);
        // not supported tbuilder.add("b", Byte.class);
        // FIXME tbuilder.add("c", Short.class);
        // FIXME tbuilder.add("d", Double.class);
        // FIXME tbuilder.add("e", Float.class);
        tbuilder.add("f", String.class);
        // not supported tbuilder.add("g", Date.class);
        // not supported tbuilder.add("h", Boolean.class);
        // not supported tbuilder.add("i", Number.class);
        // FIXME tbuilder.add("j", Long.class);
        // not supported tbuilder.add("k", BigDecimal.class);
        // not supported tbuilder.add("l", BigInteger.class);
    	
        return tbuilder.buildFeatureType();
    }
	
    protected static ListFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setCRS(DefaultGeographicCRS.WGS84);
        tbuilder.setName("junk");
        tbuilder.add("a", Point.class);
        tbuilder.add("b", Byte.class);
        tbuilder.add("c", Short.class);
        tbuilder.add("d", Double.class);
        tbuilder.add("e", Float.class);
        tbuilder.add("f", String.class);
        tbuilder.add("g", Date.class);
        tbuilder.add("h", Boolean.class);
        tbuilder.add("i", Number.class);
        tbuilder.add("j", Long.class);
        tbuilder.add("k", BigDecimal.class);
        tbuilder.add("l", BigInteger.class);
        SimpleFeatureType type = tbuilder.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        ListFeatureCollection features = new ListFeatureCollection(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            features.add(fb.buildFeature(null, new Object[] {
                    new GeometryFactory().createPoint(new Coordinate(1, -1)), new Byte((byte) i),
                    new Short((short) i), new Double(i), new Float(i), new String(i + " "),
                    new Date(i), new Boolean(true), new Integer(22),
                    new Long(1234567890123456789L),
                    new BigDecimal(new BigInteger("12345678901234567890123456789"), 2),
                    new BigInteger("12345678901234567890123456789") }));
        }
        return features;
    }
    

}
