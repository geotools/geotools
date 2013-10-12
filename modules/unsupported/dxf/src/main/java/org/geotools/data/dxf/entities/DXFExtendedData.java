package org.geotools.data.dxf.entities;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.parser.DXFParseException;

/**
 * Obtain all the Extended Data from dxf entities
 * 
 * @author rui.travassos
 * 
 * @see http://www.autodesk.com/techpubs/autocad/acad2000/dxf/index.htm
 */
public class DXFExtendedData {
	private static final Log log = LogFactory.getLog(DXFExtendedData.class);
	protected String appName;
	protected String layerName;
	protected List<Object> attributes;
	protected Double distance, scaleFactor;

	public DXFExtendedData(String appName, String layerName, List<Object> attributes, Double distance, Double scaleFactor) {
		this.appName = appName;
		this.layerName = layerName;
		this.attributes = attributes;
		this.distance = distance;
		this.scaleFactor = scaleFactor;
	}

	public DXFExtendedData() {
	}

	public static DXFExtendedData getExtendedData(DXFLineNumberReader lnr) throws IOException {
		DXFExtendedData data = null;
		String appName = null, layerName = null;
		List<Object> attributes = new LinkedList<Object>();
		Double[] wsDisplacement = new Double[3], wsPostion = new Double[3], reals = new Double[3], wDirection = new Double[3];
		Double distance = null, scaleFactor = null;
		
		DXFCodeValuePair cvp = null;
		DXFGroupCode gc = null;
		int sln = lnr.getLineNumber();
		log.debug(">>Enter at line: " + sln);

		boolean attribute = false;
		boolean loop = true;
		while (loop) {
			cvp = new DXFCodeValuePair();
			try {
				lnr.mark();
				gc = cvp.read(lnr);
			} catch (DXFParseException ex) {
				throw new IOException("DXF Extended Data parse error" + ex.getLocalizedMessage());
			} catch (EOFException e) {
				loop = false;
				break;
			}
			switch (gc) {
			case XDATA_APPLICATION_NAME:
				appName = cvp.getStringValue();
				break;
			case XDATA_LAYER_NAME:
				appName = cvp.getStringValue();
				break;
			case XDATA_CONTROL_STRING:
				// start or end the attributes list
				attribute = !attribute;
				break;
			case XDATA_ASCII_STRING:
				if (attribute)
					attributes.add(cvp.getStringValue());
				break;
			case XDATA_CHUNK_OF_BYTES:
				if (attribute)
					attributes.add(cvp.getBinHexValue());
				break;
			case XDATA_DOUBLE:
				if (attribute)
					attributes.add(cvp.getDoubleValue());
				break;
			case XDATA_INT16:
				if (attribute)
					attributes.add(cvp.getShortValue());
				break;
			case XDATA_INT32:
				if (attribute)
					attributes.add(cvp.getIntValue());
				break;
			case XDATA_DISTANCE:
				distance = cvp.getDoubleValue();
				break;
			case XDATA_SCALE_FACTOR:
				scaleFactor = cvp.getDoubleValue();
				break;
			// reals
			case XDATA_X_1:
				reals[0] = cvp.getDoubleValue();
				break;
			case XDATA_Y_1:
				reals[1] = cvp.getDoubleValue();
				break;
			case XDATA_Z_1:
				reals[2] = cvp.getDoubleValue();
				break;
			// World space position
			case XDATA_X_2:
				wsPostion[0] = cvp.getDoubleValue();
				break;
			case XDATA_Y_2:
				wsPostion[1] = cvp.getDoubleValue();
				break;
			case XDATA_Z_2:
				wsPostion[2] = cvp.getDoubleValue();
				break;
			// World space displacement
			case XDATA_X_3:
				wsDisplacement[0] = cvp.getDoubleValue();
				break;
			case XDATA_Y_3:
				wsDisplacement[1] = cvp.getDoubleValue();
				break;
			case XDATA_Z_3:
				wsDisplacement[2] = cvp.getDoubleValue();
				break;
			// World direction
			case XDATA_X_4:
				wDirection[0] = cvp.getDoubleValue();
				break;
			case XDATA_Y_4:
				wDirection[1] = cvp.getDoubleValue();
				break;
			case XDATA_Z_4:
				wDirection[2] = cvp.getDoubleValue();
				break;
			default:
				lnr.reset();
				int lineNumber = lnr.getLineNumber();
				data = new DXFExtendedData(appName, layerName, attributes, distance, scaleFactor);
				log.debug("Extended data -> " + data.toString());
				log.debug(">>Exit at line: " + lineNumber);
				return data;
			}
		}
		lnr.reset();
		data = new DXFExtendedData(appName, layerName, attributes, distance, scaleFactor);
		log.debug("Extended data -> " + data.toString());
		log.debug(">>Exit at line: " + lnr.getLineNumber());
		return data;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public List<Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Object> attributes) {
		this.attributes = attributes;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(Double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	@Override
	public String toString() {
		return "DXFExtendedData [appName=" + appName + ", layerName=" + layerName + ", attributes=" + this.toStringAttributes() + ", distance=" + distance + ", scaleFactor=" + scaleFactor + "]";
	}

	public String toStringAttributes() {
		StringBuilder sb = new StringBuilder();

		sb.append("{");
		for (Object o : attributes) {
			try {
				if (o instanceof String)
					sb.append((String) o);
				else if (o instanceof Number)
					sb.append((Number) o);
				else
					sb.append(o.toString());
			} catch (Exception e) {
				log.warn("Parse to string attribute problem. Skipping...");
			} finally {
				sb.append(";");
			}
		}
		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(DXFExtendedData extData) {
		Map<String, Object> map = null;
		if (extData != null) {
			map = new HashMap<String, Object>();
			map.put("layerName", extData.layerName);
			map.put("appName", extData.appName);
			map.put("attributes", extData.attributes);
			map.put("distance", extData.distance);
			map.put("scaleFactor", extData.scaleFactor);
		}
		return map;
	}
}
