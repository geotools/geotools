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
package org.geotools.data.wms;

import org.geotools.data.ows.OperationType;


/**
 * A subclass of OperationType that allows format getters and setters without
 * having to cast them.
 *
 * @author Richard Gould
 *
 * @source $URL$
 * @deprecated Use of OperationType should be sufficient
 */
public class WMSOperationType extends OperationType {
    private WMSOperationType() {
        super();
    }

    /**
     * 
     * @deprecated Use OperationType.getFormats();
     *
    public String[] getFormatStrings() {
        return (String[]) formats.toArray(new String[formats.size()]);
    }

    /**
     * 
     * @param formats
     * @deprecated Use OpeartionType.setFormats();
     *
    public void setFormatStrings(String[] formats) {
    	if (formats == null) {
    		this.formats = null;
    	} else {
    		this.formats = new ArrayList(formats.length);
    		this.formats.addAll(Arrays.asList(formats));
    	}    	
    }*/
}
