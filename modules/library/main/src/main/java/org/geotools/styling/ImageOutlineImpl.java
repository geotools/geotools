/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.util.Utilities;

public class ImageOutlineImpl implements ImageOutline {

	Symbolizer symbolizer;
	
	public Symbolizer getSymbolizer() {
		return symbolizer;
	}

	public void setSymbolizer(Symbolizer symbolizer) {
		if (
			symbolizer instanceof LineSymbolizer ||
			symbolizer instanceof PolygonSymbolizer
		) {
			this.symbolizer = symbolizer;
		}
		else {
			throw new IllegalArgumentException("Symbolizer must be Line or Polygon.");
		}
	}
	
	public boolean equals(Object obj) {
		 if (this == obj) {
            return true;
        }
	        
        if (obj instanceof ImageOutlineImpl) {
        	ImageOutlineImpl other = (ImageOutlineImpl) obj;
            return Utilities.equals(symbolizer, other.symbolizer);
        }
        
        return false;
	}
	
	public int hashCode() {
		final int PRIME = 1000003;
	    int result = 0;
	    
	    if (symbolizer != null) {
	    	result = PRIME * result + symbolizer.hashCode();
	    }
        
        return result;
	}

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

}
