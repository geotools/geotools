/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.geotools.data.AttributeWriter;
import org.geotools.data.DataUtilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
/**
 * Simple AttributeWriter that produces Java properties files.
 * <p>
 * This AttributeWriter is part of the geotools2 DataStore tutorial, and
 * should be considered a Toy.
 * </p>
 * <p>
 * The content produced witll start with the property "_" with the
 * value being the typeSpec describing the featureType. Thereafter each line
 * will represent a Features with FeatureID as the property and the attribtues
 * as the value separated by | characters.
 * </p> 
 * <pre><code>
 * _=id:Integer|name:String|geom:Geometry
 * fid1=1|Jody|<i>well known text</i>
 * fid2=2|Brent|<i>well known text</i>
 * fid3=3|Dave|<i>well known text</i>
 * </code></pre>
 * @author jgarnett
 * @source $URL$
 */
public class PropertyAttributeWriter implements AttributeWriter {
    BufferedWriter writer;    
    SimpleFeatureType type;    
    public PropertyAttributeWriter( File file, SimpleFeatureType featureType ) throws IOException {
        writer = new BufferedWriter( new FileWriter( file ) );
        type = featureType;                
        writer.write( "_=" );
        writer.write( DataUtilities.spec( type ) );                                        
    }
    public int getAttributeCount() {
        return type.getAttributeCount();
    }
    public AttributeDescriptor getAttributeType(int index) throws ArrayIndexOutOfBoundsException {
        return type.getDescriptor(index);
    }
    public boolean hasNext() throws IOException {
        return false;
    }    
    public void next() throws IOException {
        if( writer == null){
            throw new IOException("Writer has been closed");
        }
        writer.newLine();
        writer.flush();
    }            
    public void echoLine( String line ) throws IOException{
        if( writer == null ){
            throw new IOException("Writer has been closed");
        }
        if( line == null ){
            return;
        }
        writer.write( line );
    }
    public void writeFeatureID( String fid ) throws IOException{
        if( writer == null){
            throw new IOException("Writer has been closed");
        }        
        writer.write( fid );                
    }
    public void write(int position, Object attribute) throws IOException {
        if( writer == null){
            throw new IOException("Writer has been closed");
        }
        writer.write( position == 0 ? "=" : "|" );
        if( attribute == null ){
        	// nothing!
        }
        else if( attribute instanceof Geometry){
            writer.write( ((Geometry)attribute).toText() );
        }
        else {
            writer.write( attribute.toString() );
        }
    }
    public void close() throws IOException {
        if( writer == null){
            throw new IOException("Writer has already been closed");            
        }
        writer.close();
        writer = null;
        type = null;        
    }
}
