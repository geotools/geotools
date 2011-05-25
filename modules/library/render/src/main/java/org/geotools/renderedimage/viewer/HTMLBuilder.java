/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2007, GeoTools Project Managment Committee (PMC)
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
package org.geotools.renderedimage.viewer;


/**
 * Simple helper for building an HTML document
 * 
 * @author Andrea Aime
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools/renderedimage/viewer/HTMLBuilder.java $
 */
public class HTMLBuilder {
	
	private StringBuffer sb = new StringBuffer();

	public HTMLBuilder() {
		sb.append("<html><body>");
	}

	public void title(String title) {
		sb.append("<h2>").append(title).append("</h2><hr>");
	}

	public void dataLine(String label, Object value) {
		sb.append("<b>").append(label).append(":</b> ").append(render(value))
				.append("<br>");
	}

	public String getHtml() {
		sb.append("</html></body>");
		String result = sb.toString();
		sb = null;
		return result;
	}

	public static String render(Object value) {
		if (value == null)
			return "-";

		String result = null;
		for (HTMLRenderer renderer : HTMLRenderers.getRenderers()) {
			if (renderer.canRender(value))
				result = renderer.render(value);
		}
		if (result == null)
			result = value.toString();
		return result.replace("\n", "<br>");
	}

}
