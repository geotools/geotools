/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.wololo.flatgeobuf.ColumnMeta;
import org.wololo.flatgeobuf.GeometryConversions;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.generated.ColumnType;
import org.wololo.flatgeobuf.generated.GeometryType;

public class HeaderMetaUtil {

    public static SimpleFeatureType toFeatureType(HeaderMeta headerMeta, String defaultName) {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        String name = headerMeta.name;
        if (name == null || name.isEmpty()) ftb.setName(defaultName);
        else ftb.setName(name);
        ftb.setAbstract(false);
        ftb.setDefaultGeometry("geom");
        CoordinateReferenceSystem crs = null;
        try {
            if (headerMeta.srid != 0) {
                crs = CRS.decode("EPSG:" + headerMeta.srid, true);
            }
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
        ftb.crs(crs);
        ftb.add("geom", GeometryConversions.getGeometryClass(headerMeta.geometryType));
        for (ColumnMeta columnMeta : headerMeta.columns)
            ftb.add(columnMeta.name, columnMeta.getBinding());
        SimpleFeatureType featureType = ftb.buildFeatureType();
        return featureType;
    }

    public static HeaderMeta fromFeatureType(SimpleFeatureType featureType, long featuresCount) {
        List<AttributeDescriptor> types = featureType.getAttributeDescriptors();
        List<ColumnMeta> columns = new ArrayList<>();

        for (AttributeDescriptor ad : types) {
            if (ad instanceof GeometryDescriptor) continue;
            String key = ad.getLocalName();
            Class<?> binding = ad.getType().getBinding();
            ColumnMeta column = new ColumnMeta();
            column.name = key;
            if (binding.isAssignableFrom(Boolean.class)) column.type = ColumnType.Bool;
            else if (binding.isAssignableFrom(Byte.class)) column.type = ColumnType.Byte;
            else if (binding.isAssignableFrom(Short.class)) column.type = ColumnType.Short;
            else if (binding.isAssignableFrom(Integer.class)) column.type = ColumnType.Int;
            else if (binding.isAssignableFrom(BigInteger.class)) column.type = ColumnType.Long;
            else if (binding.isAssignableFrom(BigDecimal.class)) column.type = ColumnType.Double;
            else if (binding.isAssignableFrom(Long.class)) column.type = ColumnType.Long;
            else if (binding.isAssignableFrom(Double.class)) column.type = ColumnType.Double;
            else if (binding.isAssignableFrom(LocalDateTime.class)
                    || binding.isAssignableFrom(LocalDate.class)
                    || binding.isAssignableFrom(LocalTime.class)
                    || binding.isAssignableFrom(OffsetDateTime.class)
                    || binding.isAssignableFrom(OffsetTime.class)
                    || binding.isAssignableFrom(java.sql.Date.class)
                    || binding.isAssignableFrom(java.sql.Time.class)
                    || binding.isAssignableFrom(java.sql.Timestamp.class))
                column.type = ColumnType.DateTime;
            else if (binding.isAssignableFrom(String.class)) column.type = ColumnType.String;
            else throw new RuntimeException("Cannot handle type " + binding.getName());
            columns.add(column);
        }

        byte geometryType = GeometryType.Unknown;
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();

        HeaderMeta headerMeta = new HeaderMeta();
        headerMeta.featuresCount = featuresCount;
        headerMeta.geometryType = geometryType;
        headerMeta.columns = columns;

        if (geometryDescriptor != null) {
            CoordinateReferenceSystem crs = geometryDescriptor.getCoordinateReferenceSystem();
            geometryType =
                    GeometryConversions.toGeometryType(
                            featureType.getGeometryDescriptor().getType().getBinding());
            if (crs != null) {
                try {
                    Integer srid = CRS.lookupEpsgCode(crs, false);
                    if (srid != null) headerMeta.srid = srid;
                } catch (FactoryException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return headerMeta;
    }
}
