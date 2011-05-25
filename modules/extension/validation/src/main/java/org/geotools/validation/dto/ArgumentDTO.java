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
package org.geotools.validation.dto;

/**
 * ArgumentConfig purpose.
 * 
 * <p>
 * Description of ArgumentConfig ...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class ArgumentDTO {
    private String name;
    private boolean _final;
    private Object value;

    /**
     * ArgumentConfig constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public ArgumentDTO() {
    }

    public ArgumentDTO(ArgumentDTO dto) {
        name = dto.getName();
        _final = isFinal();
        value = dto.getValue();
    }

    public Object clone() {
        return new ArgumentDTO(this);
    }

    public boolean equals(Object obj) {
        boolean r = true;

        if ((obj == null) || !(obj instanceof ArgumentDTO)) {
            return false;
        }

        ArgumentDTO dto = (ArgumentDTO) obj;
        r = r && (dto.isFinal() == _final);

        if (name != null) {
            r = r && (name.equals(dto.getName()));
        } else if (dto.getName() != null) {
            return false;
        }

        if (value != null) {
            r = r && (value.equals(dto.getValue()));
        } else if (dto.getValue() != null) {
            return false;
        }

        return r;
    }

    public int hashCode() {
        int r = 1;

        if (name != null) {
            r *= name.hashCode();
        }

        if (value != null) {
            r *= value.hashCode();
        }

        return r;
    }

    /**
     * Access _final property.
     *
     * @return Returns the _final.
     */
    public boolean isFinal() {
        return _final;
    }

    /**
     * Set _final to _final.
     *
     * @param _final The _final to set.
     */
    public void setFinal(boolean _final) {
        this._final = _final;
    }

    /**
     * Access name property.
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name to name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Access value property.
     *
     * @return Returns the value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set value to value.
     *
     * @param value The value to set.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
