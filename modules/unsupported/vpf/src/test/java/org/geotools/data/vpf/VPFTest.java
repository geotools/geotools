package org.geotools.data.vpf;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.KVP;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

/**
 * 
 * @author jody
 *
 */
public class VPFTest extends OnlineTestCase {
    
    File vmap = null;
    
    @Override
    protected boolean isOnline() throws Exception {
        File file = new File( fixture.getProperty( "vmap") );
        
        return file.exists();
    }

    @Override
    protected void connect() throws Exception {
        super.connect();
        
        String path = fixture.getProperty("vmap");
        vmap = new File( path );
    }
    @Override
    protected void disconnect() throws Exception {
        vmap = null;
        super.disconnect();
    }
    protected String getFixtureId(){
        return "vpf.vmap";
    }
    
    /**
     * As the VMAP dataset is so large it requires a sepeate download.
     * We are treating this as an online test - asking you to supply the
     * location of this dataset on your machine.
     * 
     * @return fixture listing where vmap folder (that contains lht)
     */
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("vmap", "C:\\data\\v0noa_5\\vmaplv0\\noamer");
        return fixture;
    }
    
    public void testFactory() throws Exception {
        assertNotNull( "Check fixture is provided", vmap );
        assertTrue("vmap found", vmap.exists() );
        assertTrue("directory check", vmap.isDirectory() );
        File lht = new File( vmap, "lht");
        assertTrue("lht check", lht.exists() );
        
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        
        URL url = DataUtilities.fileToURL( vmap );
        
        Map<String,Object> params = new KVP("url", url);
        assertTrue("Can connect", factory.canProcess(params ) );
        
        DataStore vpf = factory.createDataStore( params );
        assertNotNull("connect", vpf );
        
        List<Name> names = vpf.getNames();
        assertFalse( "content check", names.isEmpty() );
        
        System.out.println( names );
    }
    
    public void testSchema() throws Exception {
        assertNotNull( "Check fixture is provided", vmap );
        assertTrue("vmap found", vmap.exists() );
        assertTrue("directory check", vmap.isDirectory() );
        File lht = new File( vmap, "lht");
        assertTrue("lht check", lht.exists() );
        
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = DataUtilities.fileToURL( vmap );
        Map<String,Object> params = new KVP("url", url);
        DataStore store = factory.createDataStore( params );
        List<Name> names = store.getNames();
        
        Name name = names.get(0);
        SimpleFeatureType schema = store.getSchema(name);
        assertNotNull( "schema found", schema );
        GeometryDescriptor geom = schema.getGeometryDescriptor();
        assertNotNull("spatial", geom );
        // assertNotNull("crs", geom.getCoordinateReferenceSystem() );
        
        for( AttributeDescriptor attribute : schema.getAttributeDescriptors() ){
            System.out.print( attribute.getName() );
            System.out.print( "," );
        }
        System.out.println();
    }
    
    public void testFeatureReader() throws Exception {
        File lht = new File( vmap, "lht");
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = DataUtilities.fileToURL( vmap );
        Map<String,Object> params = new KVP("url", url);
        DataStore store = factory.createDataStore( params );
        List<Name> names = store.getNames();
        
        Name name = names.get(0);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(new Query(name.getLocalPart()), Transaction.AUTO_COMMIT );
        
        int count=0;
        ReferencedEnvelope bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        try {
            while( reader.hasNext() ){
                SimpleFeature feature = reader.next();
                count++;
                bounds.include( feature.getBounds() );
            }
        }
        finally {
            if( reader != null ){
                reader.close();
            }
        }
        System.out.println("count:"+count);
        System.out.println("bounds:"+bounds);
    }
    public void testFeatureSource() throws Exception {
        File lht = new File( vmap, "lht");
        VPFDataStoreFactory factory = new VPFDataStoreFactory();
        URL url = DataUtilities.fileToURL( vmap );
        Map<String,Object> params = new KVP("url", url);
        DataStore store = factory.createDataStore( params );
        List<Name> names = store.getNames();
        
        Name name = names.get(0);
        SimpleFeatureSource source = store.getFeatureSource(name);
        ReferencedEnvelope extent = source.getBounds();
        //assertNotNull( "extent", extent ); // should be from header
        System.out.println("extent:"+extent);
        
        SimpleFeatureCollection features = source.getFeatures();
        SimpleFeatureIterator iterator=null;
        int count=0;
        ReferencedEnvelope bounds = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        try {
            iterator = features.features();
            while( iterator.hasNext() ){
                SimpleFeature feature = iterator.next();
                count++;
                bounds.include( feature.getBounds() );
            }
        }
        finally {
            if( iterator != null ){
                iterator.close();
            }
        }
        System.out.println("count:"+count);
        System.out.println("bounds:"+bounds);
    }

}
