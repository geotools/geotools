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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * HTML renderers registry
 * 
 * @author Andrea Aime
 * 
 */
public class HTMLRenderers {
	private static List<HTMLRenderer> renderers = new ArrayList<HTMLRenderer>();
	static {
		renderers.add(new ArrayRenderer());
	}

	private HTMLRenderers() {
	};

	public static List<HTMLRenderer> getRenderers() {
		return renderers;
	}

	public static void addRendered(HTMLRenderer renderer) {
		renderers.add(renderer);
	}

	private static class ArrayRenderer implements HTMLRenderer {

		public boolean canRender(Object o) {
			return o != null && o.getClass().isArray();
		}

		public String render(Object o) {
			int length = Array.getLength(o);
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (int i = 0; i < length; i++) {
				sb.append(HTMLBuilder.render(Array.get(o, i)));
				if (i < length - 1)
					sb.append(", ");
				else
					sb.append("]");
			}
			return sb.toString();
		}

	}
}
