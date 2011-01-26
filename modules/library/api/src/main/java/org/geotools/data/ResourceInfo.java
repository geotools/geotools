/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Set;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Information about a resource.
 * <p>
 * This interface is based on Dublin Core and the RDF application profile.
 * </p>
 * There are two ids that may be associated with a resource:
 * <ul>
 * <li>name - unqiue with in the context of a Service
 * <li>schema - used to identify the type of resource
 * <ul>
 * 
 * @author Jody Garnett, Refractions Research
 * @author David Zwiers, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 *
 * @source $URL$
 */
public interface ResourceInfo {
    /**
     * Resource's title.
     * <p>
     * The title is human readable text representing the resource, in the 
     * current locale if available.
     * <p>
     * @return tile, in the current locale if available.
     */
    String getTitle();

    /**
     * Keywords associated with this resource
     * <p>
     * Known Mappings:
     * <ul>
     * <li> Maps to Dublin Core's Subject element
     * </ul>
     * </p>
     * @return Keywords for use with search, or <code>null</code> unavailable.
     */
    Set<String> getKeywords();

    /**
     * Resource's description or abstract.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>WFS GetCapabilities abstract
     * <li>WMS GetCapabilities abstract
     * </ul>
     * </p>
     * The description may be in the current locale if known.
     * 
     * @return description of resource, or <code>null</code> if unavailable
     */
    String getDescription();

    /**
     * Name of the resource with the context of its service.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>WFS typeName
     * <li>Database table name
     * <li>WMS layer name
     * <li>level of a grid coverage
     * </ul>
     * </p>
     * The name should be unique with in the context of a single Service.
     * 
     * @return name of the data, used with getSchema() to identify resource
     */
    String getName();
    
    /**
     * A URI used to identify the resource type.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>Dublin Code Format element
     * <li>WFS DescribeFeatureType URL
     * <li>file.toURI()
     * <li>xml namespace
     * <li>URL
     * </ul>
     * </p>
     * @return namespace, used with getName() to identify resource
     */
    URI getSchema();

    /**
     * Bounding box of the resource (in the native CRS), envelope isNull otherwise.
     * <p>
     * You can transform this envelope to Lat Long with a single line:
     * <code>
     * info.getBounds().transform( DefaultGeographicCRS.WGS84, true );
     * </code>
     * Here are several other options for LatLong:
     * <ul>
     * <li>DefaultGeographicCRS.WGS84
     * <li>EPSG:4369 (LatLong NAD83)
     * <li>ESPG 4326 (another LatLong)
     * </ul>
     * </p>
     * 
     * @return Bounding box of the resource (in natvie CRS), envelope.isNull() will return true if not known
     */
    ReferencedEnvelope getBounds();

    /**
     * Returns the CRS of the resource if known.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>2nd part of the Dublin Core Coverage
     * <li>Shapefile prj file
     * <li>WFS SRS
     * </ul>
     * </p>
     *
     * @return CRS of the resource, or <code>null</code> if unknown.
     */
    CoordinateReferenceSystem getCRS();
    
    /**
     * This method was considered because some services maintain this
     * information as metadata. If it was a bit more common (and axis order more consistent)
     * it would be worth considering.
     * <p>
     * Known Mappings:
     * <ul>
     * <li>1st part of the Dublin Core Coverage
     * <li>wfs LatLongBbox
     * </ul>
     * </p>
     */
    // ReferencedEnvelope getLatLongBbox();
}
