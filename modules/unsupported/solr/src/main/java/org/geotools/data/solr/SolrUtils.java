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

import java.util.Date;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.common.util.NamedList;
import org.locationtech.jts.geom.Geometry;

/** Utilities static methods for SOLR store */
public class SolrUtils {

    /** Maps SOLR types to JAVA types */
    public static Class<?> decodeSolrFieldType(String className) {
        if (className.equals("org.apache.solr.schema.TextField")
                || className.equals("org.apache.solr.schema.StrField")) return String.class;
        if (className.equals("org.apache.solr.schema.TrieLongField")
                || className.equals("org.apache.solr.schema.LongField")) return Long.class;
        if (className.equals("org.apache.solr.schema.BoolField")) return Boolean.class;
        if (className.equals("org.apache.solr.schema.SpatialRecursivePrefixTreeFieldType")
                || className.equals("org.apache.solr.schema.LatLonType")
                || className.equals("org.apache.solr.schema.BBoxField")
                || className.equals("org.apache.solr.spatial.pending.BBoxFieldType")
                || className.equals("org.apache.solr.schema.RptWithGeometrySpatialField"))
            return Geometry.class;
        if (className.equals("org.apache.solr.schema.DateField")
                || className.equals("org.apache.solr.schema.TrieDateField")) return Date.class;
        if (className.equals("org.apache.solr.schema.IntField")
                || className.equals("org.apache.solr.schema.TrieIntField")) return Integer.class;
        if (className.equals("org.apache.solr.schema.FloatField")
                || className.equals("org.apache.solr.schema.TrieFloatField")) return Float.class;
        if (className.equals("org.apache.solr.schema.DoubleField")
                || className.equals("org.apache.solr.schema.TrieDoubleField")) return Double.class;

        return null;
    }

    /**
     * Add methods to extract unique and multivalued informations from SOLR schema obtained by
     * {@link LukeResponse}
     */
    public static class ExtendedFieldSchemaInfo {

        private Boolean uniqueKey = false;

        private Boolean multivalued = false;

        /**
         * Fills the uniqueKey and multivalued field details
         *
         * @param processSchema LukeResponse with SOLR schema definition
         * @param processField LukeResponse with dynamic and static fields details
         * @param fieldName name of SOLR field to examine
         */
        public ExtendedFieldSchemaInfo(
                LukeResponse processSchema, LukeResponse processField, String fieldName) {
            NamedList schema = (NamedList) processSchema.getResponse().get("schema");
            NamedList<NamedList> flds = (NamedList<NamedList>) schema.get("fields");
            for (Entry<String, NamedList> entry : flds) {
                String fn = entry.getKey();
                if (fn.equals(fieldName)) {
                    NamedList om = entry.getValue();
                    if (om.get("uniqueKey") != null) {
                        this.uniqueKey = new Boolean(om.get("uniqueKey").toString());
                    } else {
                        this.uniqueKey = false;
                    }
                    break;
                }
            }

            flds = (NamedList<NamedList>) processField.getResponse().get("fields");
            for (Entry<String, NamedList> entry : flds) {
                String fn = entry.getKey();
                if (fn.equals(fieldName)) {
                    NamedList om = entry.getValue();
                    if (om.get("schema") != null && om.get("schema").toString().contains("M")) {
                        this.multivalued = true;
                    } else {
                        this.multivalued = false;
                    }
                    break;
                }
            }
        }

        public Boolean getUniqueKey() {
            return uniqueKey;
        }

        public Boolean getMultivalued() {
            return multivalued;
        }
    }
}
