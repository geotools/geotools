/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cqljson;

import static org.geotools.filter.text.cqljson.FilterToCQL2Json.ARGS;
import static org.geotools.filter.text.cqljson.FilterToCQL2Json.OP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import org.geotools.api.filter.expression.Add;
import org.geotools.api.filter.expression.BinaryExpression;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Multiply;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.temporal.Period;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;

/** This class is responsible to convert an expression to a CQL2-JSON expression. */
public class ExpressionToCQL2Json implements ExpressionVisitor {
    private ObjectMapper objectMapper;

    public ExpressionToCQL2Json(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        throw new UnsupportedOperationException("PropertyIsNil not supported");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return buildBinaryExpression(expression, extraData, "+");
    }

    private Object buildBinaryExpression(
            BinaryExpression binaryExpression, Object extraData, String opName) {
        ArrayNode output = asArrayNode(extraData);
        ArrayNode args = objectMapper.createArrayNode();
        binaryExpression.getExpression1().accept(this, args);
        binaryExpression.getExpression2().accept(this, args);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(OP, opName);
        objectNode.set(ARGS, args);
        output.add(objectNode);
        return output;
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return buildBinaryExpression(expression, extraData, "/");
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        ArrayNode output = asArrayNode(extraData);
        ObjectNode functionNode = objectMapper.createObjectNode();
        ObjectNode functionDetails = objectMapper.createObjectNode();
        functionDetails.put("name", expression.getName());
        ArrayNode args = objectMapper.createArrayNode();
        for (Expression parameter : expression.getParameters()) {
            parameter.accept(this, args);
        }
        functionDetails.set(ARGS, args);
        functionNode.set("function", functionDetails);
        output.add(functionNode);
        return output;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return buildBinaryExpression(expression, extraData, "*");
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        ArrayNode output = asArrayNode(extraData);
        ObjectNode propertyName = objectMapper.createObjectNode();
        propertyName.put("property", expression.getPropertyName());
        output.add(propertyName);
        return output;
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        return buildBinaryExpression(expression, extraData, "-");
    }

    @Override
    public Object visit(Literal expression, Object extraData) {
        ArrayNode output = asArrayNode(extraData);
        if (expression.getValue() instanceof Number) {
            toNumber(output, (Number) expression.getValue());
        } else if (expression.getValue() instanceof Geometry) {
            toGeoJSON(output, (Geometry) expression.getValue());
        } else if (expression.getValue() instanceof BoundingBox) {
            toGeoJSON(output, (BoundingBox) expression.getValue());
        } else if (expression.getValue() instanceof Date
                || expression.getValue() instanceof Instant) {
            toDate(output, expression.getValue());
        } else if (expression.getValue() instanceof Period) {
            toPeriod(output, (Period) expression.getValue());
        } else if (expression.getValue() instanceof Color) {
            toColor(output, (Color) expression.getValue());
        } else if (expression.getValue() instanceof Boolean) {
            output.add((Boolean) expression.getValue());
        } else {
            if (expression.getValue() == null) {
                throw new NullPointerException("CQL2-JSON does not support null literal value");
            }
            output.add(expression.getValue().toString());
        }

        return output;
    }

    private void toColor(ArrayNode output, Color value) {
        Color color = (Color) value;
        StringBuilder stringBuilder = new StringBuilder();
        String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        stringBuilder.append("'#");
        if (redCode.length() == 1) {
            stringBuilder.append("0");
        }
        stringBuilder.append(redCode.toUpperCase());

        if (greenCode.length() == 1) {
            stringBuilder.append("0");
        }
        stringBuilder.append(greenCode.toUpperCase());

        if (blueCode.length() == 1) {
            stringBuilder.append("0");
        }
        stringBuilder.append(blueCode.toUpperCase());
        output.add(stringBuilder.toString());
    }

    private void toPeriod(ArrayNode output, Period period) {
        if (period != null) {
            String date1 = toDateText(period.getBeginning().getPosition().getDate());
            String date2 = toDateText(period.getEnding().getPosition().getDate());
            ObjectNode intervalNode = objectMapper.createObjectNode();
            ArrayNode arrayNode = objectMapper.createArrayNode();
            arrayNode.add(date1);
            arrayNode.add(date2);
            intervalNode.set("interval", arrayNode);
        }
    }

    private void toDate(ArrayNode output, Object value) {
        if (value != null) {
            ObjectNode dateNode = objectMapper.createObjectNode();
            dateNode.put("date", toDateText(value));
            output.add(dateNode);
        }
    }

    private String toDateText(Object value) {
        Date date = Converters.convert(value, Date.class);
        final DateFormat formatter;
        // If the Date has millisecond resolution, print the millis.
        if (date.getTime() % 1000 == 0) {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        }

        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(date);
    }

    private void toGeoJSON(ArrayNode output, Geometry value) {
        try {
            output.add(objectMapper.readTree(GeoJSONWriter.toGeoJSON(value)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    "Unable to convert Geometry into GeoJSON while building CQL2-JSON");
        }
    }

    private void toGeoJSON(ArrayNode arrayNode, BoundingBox value) {
        ObjectNode bboxNode = objectMapper.createObjectNode();
        ArrayNode coords = objectMapper.createArrayNode();
        coords.add(value.getMinX());
        coords.add(value.getMinY());
        coords.add(value.getMaxX());
        coords.add(value.getMaxY());
        bboxNode.set("bbox", coords);
        arrayNode.add(bboxNode);
    }

    private void toNumber(ArrayNode arrayNode, Number value) {
        if (value.doubleValue() % 1 == 0) {
            arrayNode.add(value.intValue());
        } else {
            arrayNode.add(value.doubleValue());
        }
    }

    private ArrayNode asArrayNode(Object extraData) {
        if (extraData instanceof ArrayNode) {
            return (ArrayNode) extraData;
        }
        return objectMapper.createArrayNode();
    }
}
