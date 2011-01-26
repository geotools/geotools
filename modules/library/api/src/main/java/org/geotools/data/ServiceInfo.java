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


/**
 * Information about a service.
 * <p>
 * You can treat this bean as a "summary view" on more complete metadata information
 * that may be accessible as header or table information.
 * </p>
 * <p>
 * The names used in this class have been taken from Dublin Code
 * and it's application profile for RDF.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author David Zwiers, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 *
 * @source $URL$
 */
public interface ServiceInfo {

    /**
     * Human readable title representing the service.
     * <p>
     * The title is used to represent the service in the context
     * of a user interface and should make use of the current Locale
     * if possible.
     * </p>
     * @return title, null if unsupported.
     */
    String getTitle();

    /**
     * Keywords associated with this service.
     * <p>
     * Maps to the Dublin Core Subject element.
     * @return keywords associated with this service.
     */
    Set<String> getKeywords();

    /**
     * Human readable description of this service.
     *<p>
     * This use is understood to be in agreement with "dublin-core",
     * implementors may use either abstract or description as needed.
     * <p>
     * <ul>
     * <li>Dublin Core:
     * <quote>
     * A textual description of the content of the resource, including
     * abstracts in the case of document-like objects or content
     * descriptions in the case of visual resources.
     * </quote>
     * When providing actual dublin-core metadata you can gather up
     * all the description information into a single string for
     * searching.
     * </li>
     * <li>WMS: abstract</li>
     * <li>WFS: abstract</li>
     * <li>shapefile shp.xml information</li>
     * </ul>
     *
     * @return Human readable description, may be null.
     */
    String getDescription();

    /**
     * Party responsible for providing this service.
     * <p>
     * Known mappings:
     * <ul>
     * <li>WMS contact info
     * <li>File formats may wish to use the current user, or the last user to modify the file
     * </ul>
     * @return URI identifying the publisher of this service
     */
    URI getPublisher();

    /**
     * A URI used to identify the service type.
     * <p>
     * Maps to the Dublin Code Format element.
     * <p>
     * <ul>
     * <li>Service type for open web services
     * <li>File format or extension for on disk files
     * <li>XML schema namespace for this service type.
     * </ul>
     * <p>
     * @return URI used to identify service type
     */
    URI getSchema();

    /**
     * Returns the service source.
     * <p>
     * Maps to the Dublin Core Server Element.
     * <p>
     * <ul>
     * <li>Open web services can use the online resource of their capabilies document
     * <li>File formats may wish to use their parent directory
     * </ul>
     * <p>
     * @return Source of this service
     */
    URI getSource();

}
