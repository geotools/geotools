/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.identity;

import org.opengis.annotation.XmlElement;


/**
 * ObjectId refered to by Filter 1.1 specification (as an example).
 * <p>
 * Although ObjectId is refered to as an example we are making explicit use of it
 * here in order to show identification being defined with a long (as with several
 * popular object relational mappers).
 * </p>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/identity/ObjectId.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=5929">Implementation specification 1.1</A>
 * @author Jody Garnett, Refractions Research Inc.
 * @since GeoAPI 2.1
 */
@XmlElement("RecordId")
public interface ObjectId extends Identifier {

    /**
     * The identifier value, which is a Long.
     */
    @XmlElement("id")
    Long getID();

    /**
     * Evaluates the identifer value against the given Object.
     *
     * @param obj Object to be tested.
     *
     * @return <code>true</code> if a match, otherwise <code>false</code>
     */
    boolean matches( Object obj );

}
