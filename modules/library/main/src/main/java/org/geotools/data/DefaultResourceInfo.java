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
package org.geotools.data;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Default implementation of ResourceInfo; a simple java bean.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class DefaultResourceInfo implements ResourceInfo {

    private String title;
    private URI schema;
    private String name;
    private Set<String> keywords;
    private String description;
    private CoordinateReferenceSystem crs;
    private ReferencedEnvelope bounds;

    public DefaultResourceInfo(){        
    }
    
    public DefaultResourceInfo( ResourceInfo copy ){
        this.title = copy.getTitle();
        this.schema = copy.getSchema();
        this.name = copy.getName();
        this.keywords = new HashSet<String>();
        if( copy.getKeywords() != null ){
            this.keywords.addAll( copy.getKeywords() );
        }
        this.description = copy.getDescription();
        this.crs = copy.getCRS();
        this.bounds = copy.getBounds();                
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the schema
     */
    public URI getSchema() {
        return schema;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the keywords
     */
    public Set<String> getKeywords() {
        return keywords;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the bounds
     */
    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    /**
     * @return the crs
     */
    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /**
     * @param crs the crs to set
     */
    public void setCRS( CoordinateReferenceSystem crs ) {
        this.crs = crs;
    }

    /**
     * @param title the title to set
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema( URI schema ) {
        this.schema = schema;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords( Set<String> keywords ) {
        this.keywords = keywords;
    }

    /**
     * @param description the description to set
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * @param bounds the bounds to set
     */
    public void setBounds( ReferencedEnvelope bounds ) {
        this.bounds = bounds;
    }

}
