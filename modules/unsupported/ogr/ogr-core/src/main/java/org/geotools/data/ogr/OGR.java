/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Encapsulates calls to the OGR library.
 *
 * <p>
 *
 * @author Justin Deoliveira, OpenGeo
 */
public interface OGR {

    //
    // Global
    //
    int GetDriverCount();

    Object GetDriver(int i);

    Object GetDriverByName(String name);

    Object OpenShared(String dataSourceName, int mode);

    Object Open(String dataSourceName, int mode);

    boolean IsGEOSEnabled();

    /**
     * Checks the ogr error status code and throws java exceptions accordingly.
     *
     * @param code The ogr error code.
     */
    void CheckError(int code) throws IOException;

    String GetLastErrorMsg();

    //
    // Driver
    //
    String DriverGetName(Object driver);

    Object DriverOpen(Object driver, String dataSourceName, int mode);

    Object DriverCreateDataSource(Object driver, String dataSourceName, String[] opts);

    void DriverRelease(Object driver);

    //
    // DataSource
    //
    Object DataSourceGetDriver(Object dataSource);

    int DataSourceGetLayerCount(Object dataSource);

    Object DataSourceGetLayer(Object dataSource, int i);

    Object DataSourceGetLayerByName(Object dataSource, String name);

    void DataSourceRelease(Object dataSource);

    Object DataSourceCreateLayer(
            Object dataSource, String name, Object spatialReference, long geomType, String[] opts);

    Object DataSourceExecuteSQL(Object dataSource, String sql, Object spatialFilter);

    //
    // Layer
    //
    Object LayerGetLayerDefn(Object layer);

    int LayerGetFieldCount(Object layerDefn);

    Object LayerGetFieldDefn(Object layerDefn, int i);

    String LayerGetName(Object layer);

    long LayerGetGeometryType(Object layerDefn);

    Object LayerGetSpatialRef(Object layer);

    Object LayerGetExtent(Object layer);

    long LayerGetFeatureCount(Object layer);

    void LayerRelease(Object layer);

    void LayerReleaseLayerDefn(Object layerDefn);

    boolean LayerCanDeleteFeature(Object layer);

    boolean LayerCanWriteRandom(Object layer);

    boolean LayerCanWriteSequential(Object layer);

    boolean LayerCanCreateField(Object layer);

    boolean LayerCanIgnoreFields(Object layer);

    void LayerCreateField(Object layer, Object fieldDefn, int approx);

    void LayerSyncToDisk(Object layer);

    Object LayerNewFeature(Object layerDefn);

    ReferencedEnvelope toEnvelope(Object extent, CoordinateReferenceSystem crs);

    void LayerSetSpatialFilter(Object layer, Object geometry);

    void LayerSetAttributeFilter(Object layer, String attFilter);

    int LayerSetIgnoredFields(Object layer, String[] fields);

    void LayerResetReading(Object layer);

    Object LayerGetNextFeature(Object layer);

    boolean LayerDeleteFeature(Object layer, long fid);

    int LayerSetFeature(Object layer, Object feature);

    int LayerCreateFeature(Object layer, Object feature);

    String LayerGetFIDColumnName(Object layer);

    //
    // Field
    //
    String FieldGetName(Object field);

    long FieldGetType(Object field);

    int FieldGetWidth(Object field);

    void FieldSetWidth(Object field, int width);

    void FieldSetJustifyRight(Object field);

    void FieldSetPrecision(Object field, int precision);

    boolean FieldIsIntegerType(long type);

    boolean FieldIsRealType(long type);

    boolean FieldIsBinaryType(long type);

    boolean FieldIsDateType(long type);

    boolean FieldIsTimeType(long type);

    boolean FieldIsDateTimeType(long type);

    boolean FieldIsIntegerListType(long type);

    boolean FieldIsRealListType(long type);

    Object CreateStringField(String name);

    Object CreateIntegerField(String name);

    Object CreateRealField(String name);

    Object CreateBinaryField(String name);

    Object CreateDateField(String name);

    Object CreateTimeField(String name);

    Object CreateDateTimeField(String name);

    //
    // Feature
    //

    long FeatureGetFID(Object feature);

    boolean FeatureIsFieldSet(Object feature, int i);

    void FeatureSetGeometryDirectly(Object feature, Object geometry);

    Object FeatureGetGeometry(Object feature);

    void FeatureUnsetField(Object feature, int i);

    void FeatureSetFieldInteger(Object feature, int field, int value);

    void FeatureSetFieldDouble(Object feature, int field, double value);

    void FeatureSetFieldBinary(Object feature, int field, int length, byte[] value);

    void FeatureSetFieldDateTime(
            Object feature,
            int field,
            int year,
            int month,
            int day,
            int hour,
            int minute,
            int second,
            int tz);

    void FeatureSetFieldString(Object feature, int field, String str);

    String FeatureGetFieldAsString(Object feature, int i);

    int FeatureGetFieldAsInteger(Object feature, int i);

    double FeatureGetFieldAsDouble(Object feature, int i);

    void FeatureGetFieldAsDateTime(
            Object feature,
            int i,
            int[] year,
            int[] month,
            int[] day,
            int[] hour,
            int[] minute,
            int[] second,
            int[] tzFlag);

    void FeatureDestroy(Object feature);

    //
    // Geometry
    //
    long GetPointType();

    long GetPoint25DType();

    long GetLinearRingType();

    long GetLineStringType();

    long GetLineString25DType();

    long GetPolygonType();

    long GetPolygon25DType();

    long GetMultiPointType();

    long GetMultiLineStringType();

    long GetMultiLineString25DType();

    long GetMultiPolygonType();

    long GetMultiPolygon25DType();

    long GetGeometryCollectionType();

    long GetGeometryCollection25DType();

    long GetGeometryNoneType();

    long GetGeometryUnknownType();

    int GeometryGetWkbSize(Object geom);

    int GeometryExportToWkb(Object geom, byte[] wkb);

    Object GeometryCreateFromWkb(byte[] wkb, int[] ret);

    String GeometryExportToWkt(Object geom, int[] ret);

    Object GeometryCreateFromWkt(String wkt, int[] ret);

    void GeometryDestroy(Object geometry);

    //
    // SpatialReference
    //
    String SpatialRefGetAuthorityCode(Object spatialRef, String authority);

    String SpatialRefExportToWkt(Object spatialRef);

    void SpatialRefRelease(Object spatialRef);

    Object NewSpatialRef(String wkt);
}
