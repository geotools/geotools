/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle;

import java.awt.Color;
import java.awt.Point;

import org.geotools.factory.CommonFactoryFinder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Displacement;

public class CircleMBLayer extends MBLayer {

	private JSONObject paint;

	private JSONObject layout;

	private static String type = "circle";

	public CircleMBLayer(JSONObject json) {
		super(json);

		paint = paint();
		layout = layout();
	}

	/**
	 * (Optional) Circle radius.
	 * 
	 * Units in pixels. Defaults to 5.
	 * 
	 * @return Number
	 * @throws MBFormatException
	 */
	public Number getCircleRadius() throws MBFormatException {
		return parse.optional(Double.class, paint, "circle-radius", 5.0);
	}

	/**
	 * Access circle-radius as literal or function expression, defaults to 5
	 * 
	 * @return Access circle-radius as literal or function expression
	 * @throws MBFormatException
	 */
	public Expression circleRadius() throws MBFormatException {
		return parse.percentage(paint, "circle-radius", 5);
	}

	/**
	 * (Optional) The fill color of the circle.
	 * 
	 * Defaults to #000000.
	 * 
	 * @return Color - fill color of the circle
	 * @throws MBFormatException
	 * 
	 */
	public Color getCircleColor() throws MBFormatException {
		return parse.optional(Color.class, paint, "circle-color", Color.BLACK);
	}

	/**
	 * Access circle-color as literal or function expression, defaults to black.
	 * 
	 * @return Access circle-color as literal or function expression
	 * @throws MBFormatException
	 */
	public Expression circleColor() throws MBFormatException {
		return parse.color(paint, "circle-color", Color.BLACK);
	}

	/**
	 * (Optional) Amount to blur the circle. 1 blurs the circle such that only
	 * the centerpoint is full opacity.
	 * 
	 * Defaults to 0.
	 * 
	 * @return Number - Amount to blur circle
	 * @throws MBFormatException
	 * 
	 */
	public Number getCircleBlur() throws MBFormatException {
		return parse.optional(Double.class, paint, "circle-blur", 0.0);
	}

	/**
	 * Access circle-blur as literal or function expression, defaults to 0
	 * 
	 * @return Access circle-blur as literal or function expression
	 * @throws MBFormatException
	 */
	public Expression circleBlur() throws MBFormatException {
		return parse.percentage(paint, "circle-blur", 0);
	}

	/**
	 * (Optional) The opacity at which the circle will be drawn.
	 * 
	 * Defaults to 1.
	 * 
	 * @return Number - The opacity at which the circle will be drawn.
	 * @throws MBFormatException
	 * 
	 */
	public Number getCircleOpacity() throws MBFormatException {
		return parse.optional(Double.class, paint, "circle-opacity", 1.0);
	}

	/**
	 * Access circle-opacity.
	 * 
	 * @return Access circle-opacity as literal or function expression, defaults
	 *         to 1.
	 * @throws MBFormatException
	 */
	public Expression circleOpacity() throws MBFormatException {
		return parse.percentage(paint, "circle-opacity", 1);
	}

	/**
	 * (Optional) The geometry's offset. Values are [x, y] where negatives
	 * indicate left and up, respectively.
	 * 
	 * Units in pixels. Defaults to 0, 0.
	 * 
	 * @return double[] - double[] of offset in pixels
	 * @throws MBFormatException
	 */
	public double[] getCircleTranslate() throws MBFormatException {
		return parse.array(paint, "circle-translate", new double[] { 0.0, 0.0 });
	}

	/**
	 * Access circle-translate
	 * 
	 * @return circle-translate as Point
	 * @throws MBFormatException
	 */
	public Point circleTranslate() throws MBFormatException {
		if (paint.get("circle-translate") != null) {
			JSONArray array = (JSONArray) paint.get("circle-translate");
			Number x = (Number) array.get(0);
			Number y = (Number) array.get(1);
			return new Point(x.intValue(), y.intValue());
		} else {
			return new Point(0, 0);
		}
	}

	/**
	 * Controls the translation reference point.
	 * 
	 * Map: The circle is translated relative to the map.
	 * 
	 * Viewport: The circle is translated relative to the viewport.
	 *
	 */
	public enum CircleTranslateAnchor {
		MAP, VIEWPORT
	}

	/**
	 * (Optional) Controls the translation reference point.
	 * 
	 * {@link CircleTranslateAnchor#MAP}: The circle is translated relative to
	 * the map.
	 * 
	 * {@link CircleTranslateAnchor#VIEWPORT}: The circle is translated relative
	 * to the viewport.
	 * 
	 * Defaults to {@link CircleTranslateAnchor#MAP}. Requires circle-translate.
	 * 
	 */
	public CircleTranslateAnchor getCircleTranslateAnchor() {
		Object value = paint.get("circle-translate-anchor");
		if (value != null && "viewport".equalsIgnoreCase((String) value)) {
			return CircleTranslateAnchor.VIEWPORT;
		} else {
			return CircleTranslateAnchor.MAP;
		}
	}

	/**
	 * Controls the translation reference point.
	 * 
	 * Map: The circle is translated relative to the map.
	 * 
	 * Viewport: The circle is translated relative to the viewport.
	 *
	 */
	public enum CirclePitchScale {
		MAP, VIEWPORT
	}

	/**
	 * (Optional) Controls the scaling behavior of the circle when the map is
	 * pitched.
	 * 
	 * {@link CirclePitchScale#MAP}: Circles are scaled according to their
	 * apparent distance to the camera.
	 * 
	 * {@link CirclePitchScale#VIEWPORT}: Circles are not scaled.
	 * 
	 * Defaults to {@link CirclePitchScale#MAP}.
	 * 
	 */
	public CirclePitchScale getCirclePitchScale() {
		Object value = paint.get("circle-pitch-scale");
		if (value != null && "viewport".equalsIgnoreCase((String) value)) {
			return CirclePitchScale.VIEWPORT;
		} else {
			return CirclePitchScale.MAP;
		}
	}

	/**
	 * (Optional) The width of the circle's stroke. Strokes are placed outside
	 * of the circle-radius.
	 * 
	 * Units in pixels. Defaults to 0.
	 * 
	 * @return Number representing the circle stroke width
	 * @throws MBFormatException
	 * 
	 */
	public Number getCircleStrokeWidth() throws MBFormatException {
		return parse.optional(Double.class, paint, "circle-stroke-width", 0.0);
	}

	/**
	 * Access circle-stroke-width.
	 * 
	 * @return Access circle-stroke-width as literal or function expression,
	 *         defaults to 0.
	 * @throws MBFormatException
	 */
	public Expression circleStrokeWidth() throws MBFormatException {
		return parse.percentage(paint, "circle-stroke-width", 0);
	}

	/**
	 * (Optional) The stroke color of the circle.
	 * 
	 * Defaults to #000000.
	 * 
	 * @return Color - the color of the circle stroke
	 * @throws MBFormatException
	 * 
	 */
	public Color getCircleStrokeColor() throws MBFormatException {
		return parse.optional(Color.class, paint, "circle-stroke-color", Color.BLACK);
	}

	/**
	 * Access circle-stroke-color as literal or function expression, defaults to
	 * black.
	 * 
	 * @return Access circle-stroke-width as literal or function expression,
	 *         defaults to black.
	 * @throws MBFormatException
	 */
	public Expression circleStrokeColor() throws MBFormatException {
		return parse.color(paint, "circle-stroke-color", Color.BLACK);
	}

	/**
	 * (Optional) The opacity of the circle's stroke.
	 * 
	 * Defaults to 1.
	 * 
	 * @return Number - Number representing the stroke opacity
	 * @throws MBFormatException
	 * 
	 */
	public Number getCircleStrokeOpacity() throws MBFormatException {
		return parse.optional(Double.class, paint, "circle-stroke-opacity", 1.0);
	}

	/**
	 * Access circle-stroke-opacity.
	 * 
	 * @return Access circle-stroke-opacity as literal or function expression,
	 *         defaults to 1.
	 * @throws MBFormatException
	 */
	public Expression circleStrokeOpacity() throws MBFormatException {
		return parse.percentage(paint, "circle-stroke-opacity", 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return type;
	}

	/**
	 * Processes the filter-translate into a Displacement.
	 * <p>
	 * This should handle both literals and function stops:
	 * </p>
	 * 
	 * <pre>
	 * filter-translate: [0,0]
	 * filter-translate: { property: "building-height", "stops": [[0,[0,0]],[5,[1,2]]] }
	 * filter-translate: [ 0, { property: "building-height", "type":"exponential","stops": [[0,0],[30, 5]] }
	 * </pre>
	 * 
	 * @return
	 */
	public Displacement toDisplacement() {
		Object defn = paint.get("filter-translate");
		if (defn == null) {
			return null;
		} else if (defn instanceof JSONArray) {
			JSONArray array = (JSONArray) defn;
			return sf.displacement(parse.number(array, 0, 0), parse.number(array, 1, 0));
		}
		return null;
	}

}
