package org.geotools.data.vpf;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataStore;
import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.KVP;
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

}
