/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author tkunicki@boundlessgeo.com */
public class FeatureTypeDBObject {

    static final String KEY_typeName = "typeName";

    static final String KEY_geometryDescriptor = "geometryDescriptor";

    static final String KEY_localName = "localName";

    static final String KEY_crs = "crs";

    static final String KEY_type = "type";

    static final String KEY_properties = "properties";

    static final String KEY_name = "name";

    static final String KEY_defaultValue = "defaultValue";

    static final String KEY_minOccurs = "minOccurs";

    static final String KEY_maxOccurs = "maxOccurs";

    static final String KEY_binding = "binding";

    static final String KEY_attributeDescriptors = "attributeDescriptors";

    static final String KEY_userData = "userData";

    static final String VALUE_name = "name";

    static final String PREFIX_URN_OGC = "urn:ogc:def:crs:";

    public static DBObject convert(SimpleFeatureType ft) {

        DBObject ftDBO = new BasicDBObject(KEY_typeName, ft.getTypeName());
        Map<String, String> ftUserData = typeCheck(ft.getUserData());
        if (!ftUserData.isEmpty()) {
            ftDBO.put(KEY_userData, new BasicDBObject(ftUserData));
        }

        // for geometry descriptor, just store name to reference against attribute
        GeometryDescriptor gd = ft.getGeometryDescriptor();
        String gdLocalName = gd.getLocalName();
        DBObject gdDBO = new BasicDBObject(KEY_localName, gdLocalName);
        CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
        if (crs == null) {
            crs = DefaultGeographicCRS.WGS84;
        }
        DBObject crsDBO = encodeCRSToGeoJSON(crs);
        if (crsDBO != null) {
            gdDBO.put(KEY_crs, crsDBO);
        }
        ftDBO.put(KEY_geometryDescriptor, gdDBO);

        BasicDBList adDBL = new BasicDBList();
        for (AttributeDescriptor ad : ft.getAttributeDescriptors()) {
            String adLocalName = ad.getLocalName();
            DBObject adDBO = new BasicDBObject(KEY_localName, adLocalName);
            if (!adLocalName.equals(gdLocalName)) {
                Object dv = ad.getDefaultValue();
                if (dv != null) {
                    adDBO.put(KEY_defaultValue, dv);
                }
                adDBO.put(KEY_minOccurs, ad.getMinOccurs());
                adDBO.put(KEY_maxOccurs, ad.getMaxOccurs());
            }
            Class<?> binding =
                    ad instanceof GeometryDescriptor ? Geometry.class : ad.getType().getBinding();
            adDBO.put(KEY_type, new BasicDBObject(KEY_binding, binding.getName()));
            Map<String, String> adUserData = typeCheck(ad.getUserData());
            if (!adUserData.isEmpty()) {
                adDBO.put(KEY_userData, new BasicDBObject(adUserData));
            }
            adDBL.add(adDBO);
        }
        ftDBO.put(KEY_attributeDescriptors, adDBL);
        return ftDBO;
    }

    public static SimpleFeatureType convert(DBObject ftDBO) {
        return convert(ftDBO, null);
    }

    public static SimpleFeatureType convert(DBObject ftDBO, Name name) {

        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();

        if (name == null) {
            ftBuilder.setName(extractString(ftDBO, KEY_typeName));
        } else {
            ftBuilder.setName(name);
        }

        DBObject gdDBO = extractDBObject(ftDBO, KEY_geometryDescriptor);
        String gdLocalName = extractString(gdDBO, KEY_localName);
        DBObject crsDBO = extractDBObject(gdDBO, KEY_crs, false);
        CoordinateReferenceSystem crs = decodeCRSFromGeoJSON(crsDBO);
        if (crs == null) {
            crs = DefaultGeographicCRS.WGS84;
        }

        AttributeTypeBuilder atBuilder = new AttributeTypeBuilder();

        List<?> adDBL = extractDBList(ftDBO, KEY_attributeDescriptors);
        for (Object adO : adDBL) {
            if (adO instanceof DBObject) {
                DBObject adDBO = (DBObject) adO;
                String adLocalName = extractString(adDBO, KEY_localName);
                String bindingName = extractString(extractDBObject(adDBO, KEY_type), KEY_binding);
                try {
                    atBuilder.binding(Class.forName(bindingName));
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(
                            "Unable to generate Class instance for binding " + bindingName);
                }
                BasicDBObject adUserDataDBO = extractDBObject(adDBO, KEY_userData, false);
                if (adUserDataDBO != null) {
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) adUserDataDBO.toMap()).entrySet()) {
                        atBuilder.userData(entry.getKey(), entry.getValue());
                    }
                }
                if (gdLocalName.equals(adLocalName)) {
                    atBuilder.crs(crs);
                    ftBuilder.add(
                            atBuilder.buildDescriptor(adLocalName, atBuilder.buildGeometryType()));
                } else {
                    Integer min = extractInteger(adDBO, KEY_minOccurs, false);
                    if (min != null) {
                        atBuilder.minOccurs(min);
                    }
                    Integer max = extractInteger(adDBO, KEY_maxOccurs, false);
                    if (max != null) {
                        atBuilder.maxOccurs(max);
                    }
                    Object dv = adDBO.get(KEY_defaultValue);
                    if (dv != null) {
                        atBuilder.defaultValue(dv);
                    }
                    ftBuilder.add(atBuilder.buildDescriptor(adLocalName));
                }
            }
        }

        SimpleFeatureType ft = ftBuilder.buildFeatureType();

        BasicDBObject ftUserDataDBO = extractDBObject(ftDBO, KEY_userData, false);
        if (ftUserDataDBO != null) {
            Map<Object, Object> ftUserData = ft.getUserData();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) ftUserDataDBO.toMap()).entrySet()) {
                ftUserData.put(entry.getKey(), entry.getValue());
            }
        }
        return ft;
    }

    private static Map<String, String> typeCheck(Map<?, ?> map) {
        Map<String, String> typeChecked = new LinkedHashMap<String, String>();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String && value instanceof String) {
                    typeChecked.put((String) key, (String) value);
                }
            }
        }
        return typeChecked;
    }

    private static <T> T extractAndVerifyType(
            Class<T> type, DBObject dbo, String key, boolean required) {
        Object o = dbo.get(key);
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        if (required) {
            throw new RuntimeException("Unable to extract " + key + " with type " + type.getName());
        }
        return null;
    }

    static BasicDBObject extractDBObject(DBObject dbo, String key) {
        return extractDBObject(dbo, key, true);
    }

    static BasicDBObject extractDBObject(DBObject dbo, String key, boolean required) {
        return extractAndVerifyType(BasicDBObject.class, dbo, key, required);
    }

    static BasicDBList extractDBList(DBObject dbo, String key) {
        return extractDBList(dbo, key, true);
    }

    static BasicDBList extractDBList(DBObject dbo, String key, boolean required) {
        return extractAndVerifyType(BasicDBList.class, dbo, key, required);
    }

    static String extractString(DBObject dbo, String key) {
        return extractString(dbo, key, true);
    }

    static String extractString(DBObject dbo, String key, boolean required) {
        return extractAndVerifyType(String.class, dbo, key, required);
    }

    static Integer extractInteger(DBObject dbo, String key) {
        return extractInteger(dbo, key, true);
    }

    static Integer extractInteger(DBObject dbo, String key, boolean required) {
        return extractAndVerifyType(Integer.class, dbo, key, required);
    }

    static CoordinateReferenceSystem decodeCRSFromGeoJSON(DBObject crsDBO) {
        if (crsDBO == null) {
            return null;
        }
        String type = extractString(crsDBO, KEY_type, false);
        if (type == null || !VALUE_name.equals(type)) {
            return null;
        }
        DBObject pDBO = extractDBObject(crsDBO, KEY_properties, false);
        if (pDBO == null) {
            return null;
        }
        String name = extractString(pDBO, KEY_name, false);
        if (name == null) {
            return null;
        }
        CoordinateReferenceSystem crs = null;
        if (name.startsWith(PREFIX_URN_OGC)) {
            // GeoJSON 1.0 spec says:
            // 1) authority must be namespaced with OGC URN.
            // 2) for geographic CRS axis ordering is (longitude, latitude).
            // This is a problem as use of the OGC URN will force CRS with *authority*
            // defined axis ordering. For urn:ogc:def:crs:EPSG:4326 this is
            // (latitude, longitude). For now just strip off OGC URN as this will
            // allow geotools to return a CRS with desired axis ordering...
            name = name.substring(PREFIX_URN_OGC.length());
        }
        try {
            crs = CRS.decode(name, true);
        } catch (FactoryException ignore) {
        }
        return crs;
    }

    static DBObject encodeCRSToGeoJSON(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }
        Integer epsgCode = null;
        try {
            epsgCode = CRS.lookupEpsgCode(crs, true);
        } catch (FactoryException ignore) {
        }
        if (epsgCode == null) {
            return null;
        }
        DBObject crsDBO = new BasicDBObject(KEY_type, VALUE_name);
        crsDBO.put(
                KEY_properties, new BasicDBObject(KEY_name, PREFIX_URN_OGC + "EPSG:" + epsgCode));
        return crsDBO;
    }
}
