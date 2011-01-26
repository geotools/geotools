/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.spatialindex;



/** Identify nodes by the region they represent.
 *
 * @author crousson
 *
 *
 * @source $URL$
 */
public final class RegionNodeIdentifier extends NodeIdentifier {
    private static final long serialVersionUID = 6630434291791608926L;
    private Region shape;

    /** Used for serialization only.
     * So kept package private.
     *
     */
    RegionNodeIdentifier() {
        super();
    }

    /** Identify a new node.
     *
     * @param node
     */
    public RegionNodeIdentifier(Node n) {
        this();
        if (n.getShape() instanceof Region) {
            this.shape = new Region((Region) n.getShape());
        } else {
            throw new IllegalArgumentException(
                "DefaultNodeIdentifier can only identify nodes representing a Region.");
        }
    }

    public RegionNodeIdentifier(Region r) {
        this();
        this.shape = r;
    }

    /**
     * Returns a copy of the nodes shape
     */
    public Shape getShape() {
        return new Region(shape);
    }

    public int hashCode() {
        return shape.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof RegionNodeIdentifier) {
            RegionNodeIdentifier ni = (RegionNodeIdentifier) o;

            return shape.equals(ni.getShape());
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(shape.toString());

        return sb.toString();
    }
}
