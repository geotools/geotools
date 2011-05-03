package org.geotools.data.teradata;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread local table that holds transaction local query bands.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class QueryBand {

    /**
     * Session query band for name of application.
     */
    public static String APPLICATION = "Application";
    
    /**
     * Local query band for user name executing request.
     */
    public static String CLIENT_USER = "ClientUser";
    /**
     * Local query band for host name executing request.
     */
    public static String CLIENT_HOST = "ClientHostName";
    /**
     * Local query band for layer being worked against.
     */
    public static String LAYER = "Layer";
    /**
     * Local query band for type of statement, SELECT, DELETE, etc...
     */
    public static String PROCESS = "Process";
    
    static ThreadLocal<Map<String,String>> LOCAL = new ThreadLocal<Map<String,String>>() {
        protected Map<String,String> initialValue() {
            return new HashMap<String, String>();
        };
    };
    
    /**
     * Returns local query band map.
     */
    public static Map<String,String> local() {
        return LOCAL.get();
    }

}
