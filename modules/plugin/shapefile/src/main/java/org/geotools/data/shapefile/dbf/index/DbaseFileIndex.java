/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.geotools.data.DataUtilities;
import org.opengis.filter.Filter;

/**
 * Manages the use of optional Dbase index files to speedup alphanumeric filters in a DBF-file.
 * 
 * @author Alvaro Huarte
 */
public abstract class DbaseFileIndex implements java.io.Closeable {
    
    protected Charset charset;
    protected TimeZone timeZone;
    protected Calendar calendar;
    
    /**
     * Creates a new DbaseFileIndex object.
     */
    public DbaseFileIndex(Charset charset, TimeZone timeZone) {
        this.charset = charset!=null ? charset : Charset.forName("ISO-8859-1");
        this.timeZone = timeZone!=null ? timeZone : TimeZone.getDefault();
        this.calendar = Calendar.getInstance(timeZone, Locale.US);
    }
    
    /**
     * Returns the charset used.
     */
    public Charset getCharset() {
        return charset;
    }
    /**
     * Returns the TimeZone used.
     */
    public TimeZone getZimeZone() {
        return timeZone;
    }
    /**
     * Returns the Calendar used.
     */
    public Calendar getCalendar() {
        return calendar;
    }
    
    /**
     * Obtain a SeekableByteChannel from the given URL. 
     * If the url protocol is file, a FileChannel will be returned. Otherwise a generic channel will be obtained from the urls input stream.
     */
    protected SeekableByteChannel getSeekableReadChannel(URL url, int bufferSize) throws IOException {
        
        if (url.getProtocol().equalsIgnoreCase("file")) { //-> It is Local?
            File file = DataUtilities.urlToFile(url);
            return new FileSeekableByteChannel(file, bufferSize);
        }
        else {
            InputStream in = url.openConnection().getInputStream();
            return new MemorySeekableByteChannel(in);
        }
    }
    /**
     * Obtain a ReadableByteChannel from the given URL. 
     * If the url protocol is file, a FileChannel will be returned. Otherwise a generic channel will be obtained from the urls input stream.
     */
    protected ReadableByteChannel getReadChannel(URL url) throws IOException {
        
        if (url.getProtocol().equalsIgnoreCase("file")) { //-> It is Local?
            File file = DataUtilities.urlToFile(url);
            @SuppressWarnings("resource")
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            return raf.getChannel();
        }
        else {
            InputStream in = url.openConnection().getInputStream();
            return Channels.newChannel(in);
        }
    }
    
    /**
     * Gets the root index-node collection managed by this Dbase index file.
     */
    public abstract Map<String,Node> getIndexNodeMap();
    
    /**
     * Gets the records that matches the specified filter using Dbase indexing.
     * @throws IOException
     */
    public static List<Integer> queryDbaseIndex(Filter filter, ExpressionFilterVisitor filterVisitor, int maxFeatures) {
        
        ResultBuffer result = (ResultBuffer)filter.accept(filterVisitor, ResultBuffer.makeUndefined());
        
        // Full or wrong results?, returns 'null' to force normal processing of the Query without Dbase indexing.
        if (result==null || result.isFull()) {
            return null;
        }
        // Empty results?
        if (result.isEmpty() || result.isUndefined()) {
            return new ArrayList<Integer>();
        }
        
        Set <Integer> recnoResult = result.dataSet();
        List<Integer> recnoList = null;
        
        // Fill results to return.
        if (maxFeatures<0 || maxFeatures==Integer.MAX_VALUE) {
            recnoList = new ArrayList<Integer>(recnoResult.size());
            recnoList.addAll(recnoResult);
        }
        else {
            recnoList = new ArrayList<Integer>();
            int count = 0;
            
            for (Integer recno : recnoResult) {
                if (count==maxFeatures) break;
                recnoList.add(recno);
                count++;
            }
        }
        result.clear();
        
        return recnoList;
    }
}
