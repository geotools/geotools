/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

class ShpFilesLocker {
    /**
     * When true, the stack trace that got a lock that wasn't released is recorded and then
     * printed out when warning the user about this.
     */
    protected static final Boolean TRACE_ENABLED = "true".equalsIgnoreCase(System.getProperty("gt2.shapefile.trace"));
    
    final URI uri;
    final URL url;
    final FileReader reader;
    final FileWriter writer;
    boolean upgraded;
    private Trace trace;

    public ShpFilesLocker( URL url, FileReader reader ) {
        this.url = url;
        this.reader = reader;
        this.writer = null;
        ShapefileDataStoreFactory.LOGGER.fine("Read lock: " + url + " by " + reader.id());
        setTraceException();
        
    	// extracts the URI from the URL, if possible
        this.uri = getURI(url);
    }

    URI getURI(URL url) {
        try {
    		return url.toURI();
    	} catch (URISyntaxException e) {
    		// may fail if URL does not conform to RFC 2396
 		}
        return null;
    }

    public ShpFilesLocker( URL url, FileWriter writer ) {
        this.url = url;
        this.reader = null;
        this.writer = writer;
        ShapefileDataStoreFactory.LOGGER.fine("Write lock: " + url + " by " + writer.id());
        setTraceException();
        
    	// extracts the URI from the URL, if possible
        this.uri = getURI(url);
    }

    private void setTraceException() {
        String name = Thread.currentThread().getName();
        String type, id;
        if (reader != null) {
            type = "read";
            id = reader.id();
        } else {
            type = "write";
            id = writer.id();
        }
        if(TRACE_ENABLED) {
            trace = new Trace("Locking " + url + " for " + type + " by " + id + " in thread " + name);
        }
    }

    /**
     * Returns the Exception that is created when the Locker is created. This is simply a way of
     * determining who created the Locker.
     * 
     * @return the Exception that is created when the Locker is created
     */
    public Exception getTrace() {
        return trace;
    }

    /**
     * Verifies that the url and requestor are the same as the url and the reader or writer of this
     * class. assertions are used so this will do nothing if assertions are not enabled.
     */
    public void compare( URL url2, Object requestor ) {
        URL url = this.url;
        assert (url2 == url) : "Expected: " + url + " but got: " + url2;
        assert (reader == null || requestor == reader) : "Expected the requestor and the reader to be the same object: "
                + reader.id();
        assert (writer == null || requestor == writer) : "Expected the requestor and the writer to be the same object: "
                + writer.id();
    }

    @Override
    public String toString() {
        if (reader != null) {
            return "read on " + url + " by " + reader.id();
        } else {
            return "write on " + url + " by " + writer.id();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reader == null) ? 0 : reader.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((writer == null) ? 0 : writer.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ShpFilesLocker other = (ShpFilesLocker) obj;
        if (reader == null) {
            if (other.reader != null)
                return false;
        } else if (!reader.equals(other.reader))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else {
        	// calls URI.equals if a valid URI is available  
        	if (uri != null) {
        		if (!uri.equals(other.uri))
            		return false;
        	}
        	// if URI is not available, calls URL.equals (which may take a long time)  
        	else if (!url.equals(other.url))
                return false;
        }
        if (writer == null) {
            if (other.writer != null)
                return false;
        } else if (!writer.equals(other.writer))
            return false;
        return true;
    }

    private static class Trace extends Exception {

        private static final long serialVersionUID = 1L;

        public Trace( String message ) {
            super(message);
        }
    }

}
