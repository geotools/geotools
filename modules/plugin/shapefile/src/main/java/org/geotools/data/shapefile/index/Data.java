/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.index;

import java.util.ArrayList;

/**
 * Holds values (with associated DataDefinition)
 *
 * @author Tommaso Nolli
 */
public class Data {
    private DataDefinition def;
    private ArrayList values;

    /** @param def */
    public Data(DataDefinition def) {
        this.def = def;
        this.values = new ArrayList(def.getFieldsCount());
    }

    /** Check to see if a <code>Data</code> respects its <code>DataDefinition</code> */
    public final boolean isValid() {
        if (this.getValuesCount() != this.def.getFieldsCount()) {
            return false;
        }

        boolean ret = true;

        for (int i = 0; i < this.def.getFieldsCount(); i++) {
            if (!this.def.getField(i).getFieldClass().isInstance(this.getValue(i))) {
                ret = false;

                break;
            }
        }

        return ret;
    }

    /** @return - this Data object */
    public Data addValue(Object val) throws TreeException {
        if (this.values.size() == def.getFieldsCount()) {
            throw new TreeException("Max number of values reached!");
        }

        int pos = this.values.size();

        if (!val.getClass().equals(def.getField(pos).getFieldClass())) {
            throw new TreeException(
                    "Wrong class type, was expecting " + def.getField(pos).getFieldClass());
        }

        this.values.add(val);

        return this;
    }

    /** Return the KeyDefinition */
    public DataDefinition getDefinition() {
        return this.def;
    }

    public int getValuesCount() {
        return this.values.size();
    }

    /** @param i */
    public Object getValue(int i) {
        return this.values.get(i);
    }

    /** @see java.lang.Object#toString() */
    public String toString() {
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < this.values.size(); i++) {
            if (i > 0) {
                ret.append(" - ");
            }

            ret.append(this.def.getField(i).getFieldClass());
            ret.append(": ");
            ret.append(this.values.get(i));
        }

        return ret.toString();
    }

    public void clear() {
        values.clear();
    }
}
