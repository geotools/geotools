/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.ows;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * A class that provides functionality for performing basic requests
 *
 * @author Richard Gould
 * @source $URL$
 */
public abstract class AbstractRequest implements Request{
    /** Represents OGC Exception MIME types */
    public static final String EXCEPTION_XML = "application/vnd.ogc.se_xml"; //$NON-NLS-1$
    
    protected URL onlineResource;
    protected Properties properties;

    /**
     * Creates an AbstractRequest.
     * 
     * If properties isn't <code>null</code>, it will use them instead of
     * creating a new Properties object.
     * 
     * This constructor will strip all the query parameters off of
     * onlineResource and put them in the properties map. This allows clients
     * to provide their own parameters and have them saved and used along with
     * the OWS specific ones.
     * 
     * However, certain parameters will be over-written by individual requests
     * themselves. Examples of such parameters include, but are not limited to:
     * <ul>
     * <li>WMTVER
     * <li>REQUEST
     * <li>VERSION
     * <li>SERVICE
     * </ul>
     *
     * @param onlineResource the URL to construct the Request for
     * @param properties a map of pre-set parameters to be used. Can be null.
     */
    public AbstractRequest(URL onlineResource, Properties properties) {
    	
        if (properties == null) {
            this.properties = new Properties();
        } else {
            this.properties = properties;
        }
    	
        // Need to strip off the query, as getFinalURL will add it back
        // on, with all the other properties. If we don't, elements will
        // be duplicated.
        int index = onlineResource.toExternalForm().lastIndexOf("?"); //$NON-NLS-1$
        String urlWithoutQuery = null;

        if (index <= 0) {
            urlWithoutQuery = onlineResource.toExternalForm() + "?";
        } else {
            urlWithoutQuery = onlineResource.toExternalForm().substring(0, index);
            boolean once=true;   	        
	        // Doing this preserves all of the query parameters while
	        // enforcing the mandatory ones
	        if (onlineResource.getQuery() != null) {
	            StringTokenizer tokenizer = new StringTokenizer(onlineResource.getQuery(),
	                    "&"); //$NON-NLS-1$
	
	            while (tokenizer.hasMoreTokens()) {
	                String token = tokenizer.nextToken();
	                String[] param = token.split("="); //$NON-NLS-1$'
	                if (param != null && param.length>0 && param[0] != null) {
	                    String key=param[0];
	                    String value;
	                    if( param.length==1 ){
	                    	// some servers like to keep a few additional settings in their URL
	                    	// (even though this is not part of the specification we gotta
	                    	//  let them get away with it)
	                    	if( once ){
	                    		urlWithoutQuery += "?"+param[0]+"&";
	                    		once = false;	                    		
	                    	}
	                    	else {
	                    		urlWithoutQuery += param[0]+"&";                    		
	                    	}
	                    }
	                    else {
	                        value = param[1];
	                        setProperty(key.toUpperCase(), value);                        
	                    }
	                }
	            }
	        }
	        if( once ){
	        	urlWithoutQuery += "?";
	        }
        }
        try {
            this.onlineResource = new URL(urlWithoutQuery);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error parsing URL. This is likely a bug in the code.");
        }        
        initService();
        initRequest();
        initVersion();
    }

    /**
     * @see org.geotools.data.wms.request.Request#getFinalURL()
     */
    public URL getFinalURL() {
    	if (onlineResource.getProtocol().equalsIgnoreCase("file")) {
    		return onlineResource;
    	}
        String url = onlineResource.toExternalForm();

        if (!url.contains("?")) { //$NON-NLS-1$        	
            url = url.concat("?"); //$NON-NLS-1$
        }
        
        Iterator iter = properties.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            
            String value = (String) entry.getValue();
            /*
             * Some servers do not follow the rule that parameter names 
             * must be case insensitive. We will let each specification
             * implementation deal with it in their own way.
             */
            String param = processKey((String) entry.getKey());
            if( value != null && param.length() != 0 ){
            	param += "=" + value;
            }            
            if (iter.hasNext()) {
                param = param.concat("&"); //$NON-NLS-1$
            }

            url = url.concat(param);
        }

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
        	e.printStackTrace();
            //If something is wrong here, this is something wrong with the code above.
        }

        return null;
    }

    /**
     * Some Open Web Servers do not abide by the fact that parameter keys should
     * be case insensitive. 
     * 
     * This method will allow a specification to determine the way that the
     * parameter keys should be encoded in requests made by the server.
     * 
     * @param key the key to be processed
     * @return the key, after being processed. (made upper case, for example)
     */
    protected String processKey (String key ) {
        return key;
    }

    public void setProperty(String name, String value) {
    	if (value == null) {
    		properties.remove(name);
    	} else {
    		properties.setProperty(name, value);
    	}
    }
    
    /**
     * @return a copy of this request's properties
     */
    public Properties getProperties() {
        return (Properties) properties.clone();
    }
    
    protected abstract void initRequest();
    
    /**
     * Implementing subclass requests must specify their own "SERVICE" value.
     * Example: setProperty("SERVICE", "WFS");
     */
    protected abstract void initService();

    /**
     * Sets up the version number for this request. Typically something like
     * setProperty("VERSION", "1.1.1");
     */
    protected abstract void initVersion();

    /**
     * Default POST content type is xml
     */
	public String getPostContentType() {
		return "application/xml";
	}    
	
    /**
	 * Default to not requiring POST. Implementors can override if they need to.
	 */
	public void performPostOutput(OutputStream outputStream) throws IOException {
		
	}

	/**
	 * Default to not requiring POST. Implementors can override if they need to.
	 */
	public boolean requiresPost() {
		return false;
	}
}
