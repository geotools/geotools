/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.graticule.gridsupport;

import java.text.Format;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.grid.GridElement;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLine;
import org.geotools.referencing.CoordinateFormat;
import org.locationtech.jts.geom.Coordinate;

public class LineFeatureBuilder extends GridFeatureBuilder {
    /** Default feature TYPE name: "linegrid" */
    public static final String DEFAULT_TYPE_NAME = "linegrid";

    /** Name used for the integer id attribute: "id" */
    public static final String ID_ATTRIBUTE_NAME = "id";

    /** Name of the Integer level attribute ("level") */
    public static final String LEVEL_ATTRIBUTE_NAME = "level";

    /** Name of the Object value attribute ("vaStringlue") */
    public static final String VALUE_ATTRIBUTE_NAME = "value";

    public static final String VALUE_LABEL_NAME = "label";

    public static final String HORIZONTAL = "horizontal";
    public static final String TOP = "top";
    public static final String LEFT = "left";

    public static final String ANCHOR_X = "anchorX";

    public static final String ANCHOR_Y = "anchorY";

    public static final String OFFSET_X = "offsetX";

    public static final String OFFSET_Y = "offsetY";

    /**
     * "start", "mid", "end" to indicate where this line belongs in the progression of horizontal or
     * vertical lines
     */
    public static final String SEQUENCE = "sequence";

    public static final String SEQUENCE_START = "start";
    public static final String SEQUENCE_MID = "mid";

    public static final String SEQUENCE_END = "end";

    private boolean projected;

    protected int id;
    private final CoordinateFormat formatter;
    private final Format xFormat;
    private final Format yFormat;
    private final String xUnit;
    private final String yUnit;

    public LineFeatureBuilder(SimpleFeatureType type) {
        super(type);
        formatter = new CoordinateFormat();
        formatter.setNumberPattern("##0.00");
        formatter.setAnglePattern("DD.dd");

        formatter.setCoordinateReferenceSystem(type.getCoordinateReferenceSystem());
        xFormat = formatter.getFormat(0);
        yFormat = formatter.getFormat(1);
        CoordinateReferenceSystem coordinateSystem = type.getCoordinateReferenceSystem();
        projected = coordinateSystem instanceof ProjectedCRS;
        CoordinateSystem coordinateSystem1 = coordinateSystem.getCoordinateSystem();
        if (coordinateSystem1.getAxis(0).getUnit().getSymbol() != null) {
            xUnit = coordinateSystem1.getAxis(0).getUnit().getSymbol();
            yUnit = coordinateSystem1.getAxis(1).getUnit().getSymbol();
        } else if (coordinateSystem1.getAxis(0).getUnit().toString().equalsIgnoreCase("deg")) {
            xUnit = "°";
            yUnit = "°";
        } else {
            xUnit = coordinateSystem1.getAxis(0).getUnit().toString();
            yUnit = coordinateSystem1.getAxis(1).getUnit().toString();
        }
    }

    @Override
    public void setAttributes(GridElement el, Map<String, Object> attributes) {
        if (el instanceof OrthoLine) {
            OrthoLine orthoLine = (OrthoLine) el;
            attributes.put(ID_ATTRIBUTE_NAME, ++id);
            attributes.put(LEVEL_ATTRIBUTE_NAME, orthoLine.getLevel());

            Coordinate v0 = orthoLine.getVertices()[0];
            Double value = null;
            String label = "";
            Boolean horizontal;
            if (orthoLine.getOrientation() == LineOrientation.HORIZONTAL) {
                value = v0.y;
                label = yFormat.format(v0.y) + yUnit;
                // TODO: parameterize or localize this
                if (!projected) {
                    label = yFormat.format(Math.abs(v0.y)) + yUnit;
                    if (v0.y < 0) label += 'S';
                    else if (v0.y > 0) label += 'N';
                }
                horizontal = true;
            } else {
                value = v0.x;
                label = xFormat.format(v0.x) + xUnit;
                if (!projected) {
                    label = xFormat.format(Math.abs(v0.x)) + xUnit;
                    if (v0.x < 0) {
                        label += 'W';
                    } else if (v0.x > 0) {
                        label += 'E';
                    }
                }
                horizontal = false;
            }
            attributes.put(VALUE_ATTRIBUTE_NAME, value);
            attributes.put(VALUE_LABEL_NAME, label);
            attributes.put(HORIZONTAL, horizontal);
        } else {
            throw new IllegalArgumentException("Expected an instance of OrthoLine");
        }
    }
}
