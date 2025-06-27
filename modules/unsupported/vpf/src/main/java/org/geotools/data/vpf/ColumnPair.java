/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

/**
 * This class encapsulates a join between two columns. It was originally intended to join two VPFColumn types, but there
 * should not be anything about it which constrains it to those two types.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
class ColumnPair {
    /** The first column to join */
    public VPFColumn column1;
    /** The second column to join */
    public VPFColumn column2;

    /**
     * The only constructor
     *
     * @param c1 the first column
     * @param c2 the second column
     */
    public ColumnPair(VPFColumn c1, VPFColumn c2) {
        column1 = c1;
        column2 = c2;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 78236951;
        if (column1 != null) result = result * 37 + column1.hashCode();
        if (column2 != null) result ^= result * 37 + column2.hashCode();

        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        boolean result = false;

        if (arg0 == this) {
            result = true;
        } else {
            ColumnPair columnPair = (ColumnPair) arg0;

            if (columnPair != null
                    && columnPair.column1.equals(this.column1)
                    && columnPair.column2.equals(this.column2)) {
                result = true;
            }
        }

        return result;
    }
}
