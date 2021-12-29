/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.data.sqlserver.reader;

import java.util.ArrayList;
import java.util.List;

/** @author Anders Bakkevold, Bouvet */
public class Figure {

    enum SequenceType {
        STRAIGHT,
        CURVED
    };

    /** Determines the role of this figure within the GEOMETRY structure. */
    private int attribute;

    private int pointOffset;
    private List<SequenceType> sequenceTypes = new ArrayList<>();

    public Figure(int attribute, int pointOffset) {
        this.attribute = attribute;
        this.pointOffset = pointOffset;
    }

    public int getAttribute() {
        return attribute;
    }

    public int getPointOffset() {
        return pointOffset;
    }

    public List<SequenceType> getSequenceTypes() {
        return sequenceTypes;
    }

    public void setSequenceTypes(List<SequenceType> sequenceTypes) {
        this.sequenceTypes = sequenceTypes;
    }
}
