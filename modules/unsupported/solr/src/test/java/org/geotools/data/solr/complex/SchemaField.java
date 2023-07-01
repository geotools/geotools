/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr.complex;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data model class that will be used to retrieve from a XML file the Apache Solr index schema
 * fields definitions.
 */
public final class SchemaField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private Boolean multi;

    public SchemaField() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMulti() {
        return multi;
    }

    public void setMulti(Boolean multi) {
        this.multi = multi;
    }

    @XmlRootElement(name = "schema")
    public static final class SchemaFields implements Serializable {

        private static final long serialVersionUID = 1L;

        private List<SchemaField> fields;

        @XmlElement(name = "field")
        public List<SchemaField> getFields() {
            return fields;
        }

        public void setFields(List<SchemaField> fields) {
            this.fields = fields;
        }
    }
}
