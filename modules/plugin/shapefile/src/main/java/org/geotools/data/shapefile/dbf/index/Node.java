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

import java.io.IOException;
import java.util.List;

/**
 * Defines an index node of a Dbase index file.
 * 
 * @author Alvaro Huarte
 */
public interface Node {
    
    /**
     * Gets the key of the Node.
     */
    NodeKey getKey();
    
    /**
     * Returns a list containing all of the child Nodes of this object (Null when it hasn't children).
     * @throws IOException
     */
    Node[] getChildren(NodeVisitorArgs visitorArgs) throws IOException;
    
    /**
     * Fill a list containing all of the records managed by this object.
     * @throws IOException
     * @return count of records fetched.
     */
    int fillRecordIds(List<Integer> recordList, NodeVisitorArgs visitorArgs) throws IOException;
    
    /**
     * Accepts a visitor.
     * <p>
     * Implementations of all sub-interfaces must have with a method whose content is the following:
     * <pre>return visitor.{@linkplain NodeVisitor#visit visit}(this, extraData);</pre>
     * </p>
     * @throws IOException
     */
    Object accept(NodeVisitor visitor, Object extraData, NodeVisitorArgs visitorArgs) throws IOException;
}
