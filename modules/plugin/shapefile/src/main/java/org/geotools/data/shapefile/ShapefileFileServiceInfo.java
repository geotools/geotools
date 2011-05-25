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
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import org.geotools.data.ServiceInfo;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.feature.FeatureTypes;

/**
 * ServiceInfo for ShapefileDataStore.
 * 
 * @author Jody Garnett (Refractions Reserach)
 *
 *
 * @source $URL$
 */
public class ShapefileFileServiceInfo implements ServiceInfo {
    private final ShapefileDataStore shapefile;
    
    ShapefileFileServiceInfo(ShapefileDataStore shapefile) {
        this. shapefile = shapefile;
    }

    public URI getSchema() {
        // consider URI of the shapefile specification?
        return FeatureTypes.DEFAULT_NAMESPACE;
    }
    
    public Icon getIcon() {
        return null; // talk to Eclesia there is something in render
    }
    
    public URI getPublisher() {    
        String user = System.getProperty("user.name");
        try {
            return new URI( user );
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    public String getDescription() {
        StringBuffer buf = new StringBuffer();
        buf.append( shapefile.getCurrentTypeName() );
        buf.append( " local shapefile" );
        
        return buf.toString();
    }

    public String getTitle() {
        return shapefile.getCurrentTypeName();
    }
    
    public URI getSource() {
        String url = shapefile.shpFiles.get( ShpFileType.SHP );
        try {
            return new URI( url );
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public Set<String> getKeywords() {
        Set<String> words = new HashSet<String>();
        words.add( shapefile.getCurrentTypeName() );
        words.add( "shp" );
        words.add( "dbf" );
        words.add( "shx" );
        if( shapefile instanceof IndexedShapefileDataStore ){
            IndexedShapefileDataStore indexed = (IndexedShapefileDataStore) shapefile;
            if( indexed.indexUseable( ShpFileType.QIX ) ){
                words.add( "qix" );        
            }            
        }
        words.add( "shapefile" );
        
        return words;
    }
}
