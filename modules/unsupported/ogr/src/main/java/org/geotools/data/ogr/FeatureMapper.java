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
package org.geotools.data.ogr;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.ogr;
import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.type.GeometricAttributeType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Maps OGR features into Geotools ones, and vice versa. Chances are that if you
 * need to update a decode method a simmetric modification will be needed in the
 * encode method. This class is not thread safe, so each thread should create
 * its own instance.
 * 
 * @author aaime
 * 
 */
class FeatureMapper {

	/**
	 * From ogr_core.h, the byte order constants
	 */
	static final int WKB_XDR = 1;

	/**
	 * Enables usage of WKB encoding for OGR/Java Geometry conversion. At the
	 * time of writing, it cannot be used because it'll bring the virtual
	 * machine down (yes, a real crash...)
	 */
	static final boolean USE_WKB = true;

	GeometryFactory geomFactory;

	WKBReader wkbReader;

	WKTReader wktReader;

	WKBWriter wkbWriter;

	WKTWriter wktWriter;

	/**
	 * The date time format used by OGR when getting/setting times using strings
	 */
	DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

	public FeatureMapper(GeometryFactory geomFactory) {
		this.geomFactory = geomFactory;
		if (USE_WKB) {
			this.wkbReader = new WKBReader(geomFactory);
			this.wkbWriter = new WKBWriter();
		} else {
			this.wktReader = new WKTReader(geomFactory);
			this.wktWriter = new WKTWriter();
		}
	}

	/**
	 * Converts an OGR feature into a GeoTools one
	 * 
	 * @param schema
	 * @param ogrFeature
	 * @return
	 * @throws IOException
	 * @throws IllegalAttributeException
	 */
	Feature convertOgrFeature(FeatureType schema, org.gdal.ogr.Feature ogrFeature)
			throws IOException, IllegalAttributeException {
		// Extract all attributes (do not assume any specific order, the feature
		// type may have been re-ordered by the Query)
		Object[] attributes = new Object[schema.getAttributeCount()];

		// .. then extract each attribute using the attribute type to determine
		// which extraction method to call
		for (int i = 0; i < attributes.length; i++) {
			AttributeType at = schema.getAttributeType(i);
			if (at instanceof GeometricAttributeType) {
				org.gdal.ogr.Geometry ogrGeometry = ogrFeature.GetGeometryRef();
				try {
					attributes[i] = fixGeometryType(parseOgrGeometry(ogrGeometry), at);
				} finally {
					ogrGeometry.delete();
				}
			} else {
				attributes[i] = getOgrField(at, ogrFeature);
			}
		}

		// .. gather the FID
		String fid = convertOGRFID(schema, ogrFeature);

		// .. finally create the feature
		return schema.create(attributes, fid);
	}

	/**
	 * Turns a GeoTools feature into an OGR one
	 * 
	 * @param feature
	 * @return
	 * @throws DataSourceException
	 */
	org.gdal.ogr.Feature convertGTFeature(FeatureDefn ogrSchema, Feature feature)
			throws IOException {
		// create a new empty OGR feature
		org.gdal.ogr.Feature result = new org.gdal.ogr.Feature(ogrSchema);

		// go thru GeoTools feature attributes, and convert
		FeatureType schema = feature.getFeatureType();
		Object[] attributes = feature.getAttributes(new Object[schema.getAttributeCount()]);
		for (int i = 0, j = 0; i < attributes.length; i++) {
			AttributeType at = schema.getAttributeType(i);
			if (at instanceof GeometricAttributeType) {
                // using setGeoemtryDirectly the feature becomes the owner of the generated
                // OGR geometry and we don't have to .delete() it (it's faster, too) 
				result.SetGeometryDirectly(parseGTGeometry((Geometry) attributes[i]));
				continue;
			}

			if (attributes[i] == null) {
				result.UnsetField(j);
			} else {
                final FieldDefn ogrField = ogrSchema.GetFieldDefn(j);
                final int ogrType = ogrField.GetFieldType();
                ogrField.delete();
                if(ogrType == ogr.OFTInteger)
                    result.SetField(j, ((Number) attributes[i]).intValue());
                else if(ogrType == ogr.OFTReal)
                    result.SetField(j, ((Number) attributes[i]).doubleValue());
                else if (ogrType == ogr.OFTDateTime)
					result.SetField(j, dateTimeFormat.format((java.util.Date) attributes[i]));
				else if (ogrType == ogr.OFTDate)
					result.SetField(j, dateFormat.format((java.util.Date) attributes[i]));
				else if (ogrType == ogr.OFTTime)
					result.SetField(j, timeFormat.format((java.util.Date) attributes[i]));
				else
					result.SetField(j, attributes[i].toString());
			} 
			j++;
		}

		return result;
	}

	/**
	 * Turns line and polygon into multiline and multipolygon. This is a
	 * stop-gap measure to make things works against shapefiles, I've asked the
	 * GDAL mailing list on how to properly handle this in the meantime
	 * 
	 * @param ogrGeometry
	 * @param at
	 * @return
	 */
	Geometry fixGeometryType(Geometry ogrGeometry, AttributeType at) {
		if (MultiPolygon.class.equals(at.getType())) {
			if (ogrGeometry instanceof MultiPolygon)
				return ogrGeometry;
			else
				return geomFactory.createMultiPolygon(new Polygon[] { (Polygon) ogrGeometry });
		} else if (MultiLineString.class.equals(at.getType())) {
			if (ogrGeometry instanceof MultiLineString)
				return ogrGeometry;
			else
				return geomFactory
						.createMultiLineString(new LineString[] { (LineString) ogrGeometry });
		}
		return ogrGeometry;

	}

	/**
	 * Reads the current feature's geometry using wkb encoding. A wkbReader
	 * should be provided since it's not thread safe by design.
	 * 
	 * @throws IOException
	 */
	Geometry parseOgrGeometry(org.gdal.ogr.Geometry geom) throws IOException {
		// Extract the geometry using either WKT or WKB. Rationale: the SWIG
		// bindings do not provide subclasses. Even if they did, going thru the
		// JNI barrier often is expensive, so it's better to gather the geometry
		// is a single call
		if (USE_WKB) {
			int wkbSize = geom.WkbSize();
			// the gdal interface uses a char* type, maybe because in C it's
			// unsigned and has
			// the same size as a byte, unfortunately this means we have to
			// unpack it
			// to byte format by doing bit masking and shifting
			byte[] byteBuffer = new byte[wkbSize];
			geom.ExportToWkb(byteBuffer, WKB_XDR);
			try {
				Geometry g = wkbReader.read(byteBuffer);
				return g;
			} catch (ParseException pe) {
				throw new DataSourceException(
						"Could not parse the current Geometry in WKB format.", pe);
			}
		} else {
			String[] stringArray = new String[1];
			geom.ExportToWkt(stringArray);
			try {
				return wktReader.read(stringArray[0]);
			} catch (ParseException pe) {
				throw new DataSourceException(
						"Could not parse the current Geometry in WKB format.", pe);
			}
		}
	}

	org.gdal.ogr.Geometry parseGTGeometry(Geometry geometry) throws DataSourceException {
		final org.gdal.ogr.Geometry ogrGeom;
		if (USE_WKB) {
			byte[] wkb = wkbWriter.write(geometry);
			ogrGeom = ogr.CreateGeometryFromWkb(wkb, null);
			if (ogrGeom == null)
				throw new DataSourceException(
						"Could not turn JTS geometry into an OGR one thought WKB");
		} else {
			String wkt = wktWriter.write(geometry);
			ogrGeom = ogr.CreateGeometryFromWkt(wkt, null);
			if (ogrGeom == null)
				throw new DataSourceException(
						"Could not turn JTS geometry into an OGR one thought WKT");
		}
		return ogrGeom;
	}

	/**
	 * Reads the current feature's specified field using the most appropriate
	 * OGR field extraction method
	 * 
	 * @param at
	 * @return
	 */
	Object getOgrField(AttributeType at, org.gdal.ogr.Feature ogrFeature) throws IOException {
		String name = at.getName();
		Class clazz = at.getType();

		// check for null fields
		if (!ogrFeature.IsFieldSet(name))
			return null;

		// hum, ok try and parse it
		if (clazz.equals(String.class)) {
			return ogrFeature.GetFieldAsString(name);
		} else if (clazz.equals(Integer.class)) {
			return new Integer(ogrFeature.GetFieldAsInteger(name));
		} else if (clazz.equals(Double.class)) {
			return new Double(ogrFeature.GetFieldAsDouble(name));
		} else if (clazz.equals(Float.class)) {
			return new Float(ogrFeature.GetFieldAsDouble(name));
		} else if (clazz.equals(Integer.class)) {
			return new Integer(ogrFeature.GetFieldAsInteger(name));
		} else if (clazz.equals(java.util.Date.class)) {
			String date = ogrFeature.GetFieldAsString(name);
			if (date == null || date.trim().equals(""))
				return null;
			int ogrType = ogrFeature.GetFieldType(name);
			try {
				if (ogrType == ogr.OFTDateTime)
					return dateTimeFormat.parse(date);
				else if (ogrType == ogr.OFTDate)
					return dateFormat.parse(date);
				else if (ogrType == ogr.OFTTime)
					return timeFormat.parse(date);
			} catch (java.text.ParseException e) {
				throw new DataSourceException("Could not parse date value", e);
			}
			throw new IOException("Date attribute, but field type is not compatible: " + ogrType);
		} else {
			throw new IllegalArgumentException("Don't know how to read " + clazz.getName()
					+ " fields");
		}
	}

	/**
	 * Generates a GT2 feature id given its feature type and an OGR feature
	 * 
	 * @param schema
	 * @param ogrFeature
	 * @return
	 */
	String convertOGRFID(FeatureType schema, org.gdal.ogr.Feature ogrFeature) {
		return schema.getTypeName() + "." + ogrFeature.GetFID();
	}

	/**
	 * Decodes a GT2 feature id into an OGR one
	 * 
	 * @param feature
	 * @return
	 */
	int convertGTFID(Feature feature) {
		String id = feature.getID();
		return Integer.parseInt(id.substring(id.indexOf(".") + 1));
	}

}
