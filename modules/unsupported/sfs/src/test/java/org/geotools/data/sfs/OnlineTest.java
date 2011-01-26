package org.geotools.data.sfs;

import java.io.Serializable;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.sfs.SFSDataStoreFactory;
import org.geotools.data.sfs.mock.MockSimpleFeatureService;

import junit.framework.TestCase;

/**
 * To have the online tests run start the {@link MockSimpleFeatureService} class 
 */
public abstract class OnlineTest extends TestCase {

    protected static final String URL = "http://localhost:8082/simplefeatureservice/";
    protected static final String NAMESPACE = "http://geo-solutions.it/sfs";
    protected static final boolean ONLINE_TEST;

    static {
        // check if the URL is online
        boolean test = false;
        try {
            URLConnection connection = new java.net.URL(URL).openConnection();
            connection.connect();
            test = true;
        } catch (Exception e) {

            test = false;
        }
        ONLINE_TEST = test;
    }

    protected static Map<String, Serializable> createParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(SFSDataStoreFactory.URLP.key, URL);
        params.put(SFSDataStoreFactory.NAMESPACEP.key, NAMESPACE);
        return params;
    }

    public OnlineTest() {
        super();
    }

    public OnlineTest(String name) {
        super(name);
    }

    protected boolean onlineTest(String testName) {
        if (!ONLINE_TEST) {
            System.out.println(testName + " Test skipped");
            return false;
        }
        return true;
    }
}
