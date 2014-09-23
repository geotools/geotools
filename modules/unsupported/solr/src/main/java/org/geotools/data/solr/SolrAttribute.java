/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.solr;

import java.io.Serializable;

/**
 * Describes a SOLR field, that is, a field stored in remote SOLR document
 * </br>
 * This class carries information about:
 * <li> The name of field
 * <li> The mapped field type
 * <li> If the field can be used as the PK in the feature
 * <li> If the field can be used as attribute in the feature
 * <li> If the field is multiValued as defined in SOLR schema
 * <li> If the field has no data in SOLR store
 * <li> The native srid if the filed if a geometry type 
 * the geometry type and native srid (as in most databases those informations are not available on.
 * 
 * @see {@link SolrUtils#decodeSolrFieldType}
 * @see {@link SolrDataStore#getSolrAttributes}
 * 
 */

public class SolrAttribute implements Serializable {

    private static final long serialVersionUID = 8839579461838862328L;

    private String name;
    private Class<?> type;
    private Boolean pk = false;
    private Boolean use = false;
    private Boolean multivalued = false;
    private Boolean empty = true;
    private Integer srid;   

    public SolrAttribute(String name, Class<?> type) {
        super();
        this.name = name;
        this.type = type;
        this.use = false;
        this.multivalued = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Boolean isPk() {
        return pk;
    }

    public void setPk(Boolean pk) {
        this.pk = pk;
    }

    public Boolean isUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    } 

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public void setMultivalued(Boolean multivalued) {
        this.multivalued = multivalued;        
    }

    public Boolean getMultivalued() {
        return multivalued;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (pk ? 1231 : 1237);
        result = prime * result + ((srid == null) ? 0 : srid.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SolrAttribute other = (SolrAttribute) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (pk != other.pk)
            return false;
        if (srid == null) {
            if (other.srid != null)
                return false;
        } else if (!srid.equals(other.srid))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SolrAttribute [name=" + name + ", pk=" + pk + ", srid=" + srid + ", type=" + type + ", use=" + use + ", empty" + empty+"]";
    }

}
