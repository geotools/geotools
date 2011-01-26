package org.geotools.data.postgis.synch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisTests;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class WrapperTest extends AbstractSynchronizedPostgisDataTestCase {
	
	Map remote;
	private PostgisTests.Fixture f;

	public WrapperTest(String name) {
		super(name);
	}

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        f = PostgisTests.newFixture("versioned.properties");
        remote = new HashMap();
        remote.put("dbtype", "postgis-synchronized");
        remote.put("charset", "");
        remote.put("host", f.host);
        remote.put("port", f.port);
        remote.put("database", f.database);
        remote.put("user", f.user);
        remote.put("passwd", f.password);
        remote.put("namespace", f.namespace);

        super.setUp();
    }
    
    public void testTableHidings() throws Exception {
        SynchronizedPostgisDataStore ds = (SynchronizedPostgisDataStore) DataStoreFinder
        .getDataStore(remote);

        String[] typeNames = new String[] {
        		SynchronizedPostgisDataStore.TBL_SYNCH_UNITS,
        		SynchronizedPostgisDataStore.TBL_SYNCH_TABLES,
        		SynchronizedPostgisDataStore.TBL_SYNCH_UNIT_TABLES,
        		SynchronizedPostgisDataStore.TBL_SYNCH_HISTORY,
        		SynchronizedPostgisDataStore.TBL_SYNCH_OUTSTANDING,
        		SynchronizedPostgisDataStore.TBL_SYNCH_CONFLICTS
        };
        
        String[] names = ds.getTypeNames();
        for(int i = 0; i < typeNames.length; i++) {
        	for(int j = 0; j < names.length; j++) {
        		if(typeNames[i].equals(names[j]))
        			fail(typeNames[i] + " must not be visible.");
        	}
        }
        
        List<Name> nameList = ds.getNames();
        for(int i = 0; i < typeNames.length; i++) {
        	for(Name name : nameList) {
        		if(typeNames[i].equals(name.getLocalPart()))
        			fail(typeNames[i] + " must not be visible.");
        	}
        }
        	
        for(int i = 0; i < typeNames.length; i++) {
        	try {
        		SimpleFeatureType schema = ds.getSchema(typeNames[i]);
        		fail(typeNames[i] + " must not resolve.");
        	} catch(IOException ignore) {
        		;
        	}
        }
        
    }
}
