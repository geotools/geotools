/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr.jni;

import static org.gdal.ogr.ogrConstants.OFTBinary;
import static org.gdal.ogr.ogrConstants.OFTDate;
import static org.gdal.ogr.ogrConstants.OFTDateTime;
import static org.gdal.ogr.ogrConstants.OFTInteger;
import static org.gdal.ogr.ogrConstants.OFTIntegerList;
import static org.gdal.ogr.ogrConstants.OFTReal;
import static org.gdal.ogr.ogrConstants.OFTRealList;
import static org.gdal.ogr.ogrConstants.OFTString;
import static org.gdal.ogr.ogrConstants.OFTTime;
import static org.gdal.ogr.ogrConstants.OGRERR_CORRUPT_DATA;
import static org.gdal.ogr.ogrConstants.OGRERR_FAILURE;
import static org.gdal.ogr.ogrConstants.OGRERR_NONE;
import static org.gdal.ogr.ogrConstants.OGRERR_NOT_ENOUGH_DATA;
import static org.gdal.ogr.ogrConstants.OGRERR_NOT_ENOUGH_MEMORY;
import static org.gdal.ogr.ogrConstants.OGRERR_UNSUPPORTED_GEOMETRY_TYPE;
import static org.gdal.ogr.ogrConstants.OGRERR_UNSUPPORTED_OPERATION;
import static org.gdal.ogr.ogrConstants.OGRERR_UNSUPPORTED_SRS;
import static org.gdal.ogr.ogrConstants.OJRight;
import static org.gdal.ogr.ogrConstants.OLCCreateField;
import static org.gdal.ogr.ogrConstants.OLCDeleteFeature;
import static org.gdal.ogr.ogrConstants.OLCIgnoreFields;
import static org.gdal.ogr.ogrConstants.OLCRandomWrite;
import static org.gdal.ogr.ogrConstants.OLCSequentialWrite;
import static org.gdal.ogr.ogrConstants.wkbGeometryCollection;
import static org.gdal.ogr.ogrConstants.wkbGeometryCollection25D;
import static org.gdal.ogr.ogrConstants.wkbLineString;
import static org.gdal.ogr.ogrConstants.wkbLineString25D;
import static org.gdal.ogr.ogrConstants.wkbLinearRing;
import static org.gdal.ogr.ogrConstants.wkbMultiLineString;
import static org.gdal.ogr.ogrConstants.wkbMultiLineString25D;
import static org.gdal.ogr.ogrConstants.wkbMultiPoint;
import static org.gdal.ogr.ogrConstants.wkbMultiPolygon;
import static org.gdal.ogr.ogrConstants.wkbMultiPolygon25D;
import static org.gdal.ogr.ogrConstants.wkbNone;
import static org.gdal.ogr.ogrConstants.wkbPoint;
import static org.gdal.ogr.ogrConstants.wkbPoint25D;
import static org.gdal.ogr.ogrConstants.wkbPolygon;
import static org.gdal.ogr.ogrConstants.wkbPolygon25D;
import static org.gdal.ogr.ogrConstants.wkbUnknown;
import static org.gdal.ogr.ogrConstants.wkbXDR;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Vector;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.ogr.OGR;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Version;

@SuppressWarnings({"PMD.UseArrayListInsteadOfVector", "PMD.ReplaceVectorWithList"})
public class JniOGR implements OGR {

    private static final Method GET_FIELD_METHOD;

    private static final boolean USE_FLOAT_SECONDS;

    static {
        // perform OGR format registration once
        if (ogr.GetDriverCount() == 0) {
            ogr.RegisterAll();
        }
        Method getFieldAsDateTime = null;
        for (Method method : Feature.class.getDeclaredMethods()) {
            if (method.getName().equals("GetFieldAsDateTime")
                    && method.getParameterTypes().length == 8
                    && int.class.equals(method.getParameterTypes()[0])) {
                getFieldAsDateTime = method;
            }
        }
        if (getFieldAsDateTime == null) {
            throw new RuntimeException("Could not locate the desired GetFieldAsDateTime method from Feature");
        }
        GET_FIELD_METHOD = getFieldAsDateTime;
        USE_FLOAT_SECONDS = new Version(gdal.VersionInfo("RELEASE_NAME")).compareTo(new Version("2.0.0")) >= 0;
    }

    Vector<String> vector(String[] opts) {
        return opts != null && opts.length > 0 ? new Vector<>(Arrays.asList(opts)) : null;
    }

    @Override
    public boolean IsGEOSEnabled() {
        boolean isGEOSEnabled = false;
        Geometry g1 = Geometry.CreateFromWkt("POINT (1 1)");
        Geometry g2 = Geometry.CreateFromWkt("POINT (2 2)");
        try {
            g1.Touches(g2);
            if (!GetLastErrorMsg().toLowerCase().contains("GEOS support not enabled".toLowerCase())) {
                isGEOSEnabled = true;
            }
        } catch (Exception ex) {
            // Do nothing
        }
        return isGEOSEnabled;
    }

    public String getVersion() {
        return gdal.VersionInfo("RELEASE_NAME");
    }

    @Override
    public int GetDriverCount() {
        return ogr.GetDriverCount();
    }

    @Override
    public Object GetDriver(int i) {
        return ogr.GetDriver(i);
    }

    @Override
    public Object GetDriverByName(String name) {
        return ogr.GetDriverByName(name);
    }

    @Override
    public Object OpenShared(String dataSourceName, int mode) {
        return ogr.OpenShared(dataSourceName, mode);
    }

    @Override
    public Object Open(String dataSourceName, int mode) {
        return ogr.Open(dataSourceName, mode);
    }

    @Override
    public void CheckError(int code) throws IOException {
        if (code == OGRERR_NONE) {
            return;
        }

        String error = GetLastErrorMsg();

        switch (code) {
            case OGRERR_CORRUPT_DATA:
                throw new IOException("OGR reported a currupt data error: " + error);
            case OGRERR_FAILURE:
                throw new IOException("OGR reported a generic failure: " + error);
                // case OGRERR_INVALID_HANDLE:
                //    throw new IOException("OGR reported an invalid handle error: " + error);
            case OGRERR_NOT_ENOUGH_DATA:
                throw new IOException("OGR reported not enough data was provided in the last call: " + error);
            case OGRERR_NOT_ENOUGH_MEMORY:
                throw new IOException("OGR reported not enough memory is available: " + error);
            case OGRERR_UNSUPPORTED_GEOMETRY_TYPE:
                throw new IOException("OGR reported a unsupported geometry type error: " + error);
            case OGRERR_UNSUPPORTED_OPERATION:
                throw new IOException("OGR reported a unsupported operation error: " + error);
            case OGRERR_UNSUPPORTED_SRS:
                throw new IOException("OGR reported a unsupported SRS error: " + error);
            default:
                throw new IOException("OGR reported an unrecognized error code: " + code);
        }
    }

    @Override
    public String GetLastErrorMsg() {
        return gdal.GetLastErrorMsg();
    }

    @Override
    public String DriverGetName(Object driver) {
        return ((Driver) driver).GetName();
    }

    @Override
    public Object DriverOpen(Object driver, String dataSourceName, int mode) {
        return ((Driver) driver).Open(dataSourceName, mode);
    }

    @Override
    public Object DriverCreateDataSource(Object driver, String dataSourceName, String[] opts) {
        return ((Driver) driver).CreateDataSource(dataSourceName, vector(opts));
    }

    @Override
    public void DriverRelease(Object driver) {
        ((Driver) driver).delete();
    }

    @Override
    public Object DataSourceGetDriver(Object dataSource) {
        return ((DataSource) dataSource).GetDriver();
    }

    @Override
    public int DataSourceGetLayerCount(Object dataSource) {
        return ((DataSource) dataSource).GetLayerCount();
    }

    @Override
    public Object DataSourceGetLayer(Object dataSource, int i) {
        return ((DataSource) dataSource).GetLayer(i);
    }

    @Override
    public Object DataSourceGetLayerByName(Object dataSource, String name) {
        return ((DataSource) dataSource).GetLayerByName(name);
    }

    @Override
    public void DataSourceRelease(Object dataSource) {
        // if (dataSource != null)
        ((DataSource) dataSource).delete();
    }

    @Override
    public Object DataSourceCreateLayer(Object dataSource, String name, Object srs, long geomType, String[] opts) {

        return ((DataSource) dataSource).CreateLayer(name, (SpatialReference) srs, (int) geomType, vector(opts));
    }

    @Override
    public Object DataSourceExecuteSQL(Object dataSource, String sql, Object spatialFilter) {
        return ((DataSource) dataSource).ExecuteSQL(sql, (Geometry) spatialFilter);
    }

    @Override
    public Object LayerGetLayerDefn(Object layer) {
        return ((Layer) layer).GetLayerDefn();
    }

    @Override
    public int LayerGetFieldCount(Object layerDefn) {
        return ((FeatureDefn) layerDefn).GetFieldCount();
    }

    @Override
    public Object LayerGetFieldDefn(Object layerDefn, int i) {
        return ((FeatureDefn) layerDefn).GetFieldDefn(i);
    }

    @Override
    public String LayerGetName(Object layer) {
        return ((Layer) layer).GetName();
    }

    @Override
    public long LayerGetGeometryType(Object layerDefn) {
        return ((FeatureDefn) layerDefn).GetGeomType();
    }

    @Override
    public Object LayerGetSpatialRef(Object layer) {
        return ((Layer) layer).GetSpatialRef();
    }

    @Override
    public Object LayerGetExtent(Object layer) {
        return ((Layer) layer).GetExtent();
    }

    @Override
    public long LayerGetFeatureCount(Object layer) {
        return ((Layer) layer).GetFeatureCount();
    }

    @Override
    public void LayerRelease(Object layer) {
        // if (layer != null)
        ((Layer) layer).delete();
    }

    @Override
    public void LayerReleaseLayerDefn(Object layerDefn) {
        ((FeatureDefn) layerDefn).delete();
    }

    @Override
    public boolean LayerCanDeleteFeature(Object layer) {
        return ((Layer) layer).TestCapability(OLCDeleteFeature);
    }

    @Override
    public boolean LayerCanWriteRandom(Object layer) {
        return ((Layer) layer).TestCapability(OLCRandomWrite);
    }

    @Override
    public boolean LayerCanWriteSequential(Object layer) {
        return ((Layer) layer).TestCapability(OLCSequentialWrite);
    }

    @Override
    public boolean LayerCanCreateField(Object layer) {
        return ((Layer) layer).TestCapability(OLCCreateField);
    }

    @Override
    public boolean LayerCanIgnoreFields(Object layer) {
        return ((Layer) layer).TestCapability(OLCIgnoreFields);
    }

    @Override
    public void LayerCreateField(Object layer, Object fieldDefn, int approx) {
        ((Layer) layer).CreateField((FieldDefn) fieldDefn, approx);
    }

    @Override
    public void LayerSyncToDisk(Object layer) {
        ((Layer) layer).SyncToDisk();
    }

    @Override
    public Object LayerNewFeature(Object layerDefn) {
        return new Feature((FeatureDefn) layerDefn);
    }

    @Override
    public ReferencedEnvelope toEnvelope(Object extent, CoordinateReferenceSystem crs) {
        double[] d = (double[]) extent;
        return new ReferencedEnvelope(d[0], d[1], d[2], d[3], crs);
    }

    @Override
    public void LayerSetSpatialFilter(Object layer, Object geometry) {
        ((Layer) layer).SetSpatialFilter((Geometry) geometry);
    }

    @Override
    public void LayerSetAttributeFilter(Object layer, String attFilter) {
        ((Layer) layer).SetAttributeFilter(attFilter);
    }

    @Override
    public int LayerSetIgnoredFields(Object layer, String[] fields) {
        return ((Layer) layer).SetIgnoredFields(vector(fields));
    }

    @Override
    public void LayerResetReading(Object layer) {
        ((Layer) layer).ResetReading();
    }

    @Override
    public Object LayerGetNextFeature(Object layer) {
        return ((Layer) layer).GetNextFeature();
    }

    @Override
    public boolean LayerDeleteFeature(Object layer, long fid) {
        return ((Layer) layer).DeleteFeature(fid) == 0;
    }

    @Override
    public int LayerSetFeature(Object layer, Object feature) {
        return ((Layer) layer).SetFeature((Feature) feature);
    }

    @Override
    public int LayerCreateFeature(Object layer, Object feature) {
        return ((Layer) layer).CreateFeature((Feature) feature);
    }

    @Override
    public String LayerGetFIDColumnName(Object layer) {
        return ((Layer) layer).GetFIDColumn();
    }

    @Override
    public String FieldGetName(Object field) {
        return ((FieldDefn) field).GetName();
    }

    @Override
    public long FieldGetType(Object field) {
        return ((FieldDefn) field).GetFieldType();
    }

    @Override
    public int FieldGetWidth(Object field) {
        return ((FieldDefn) field).GetWidth();
    }

    @Override
    public void FieldSetWidth(Object field, int width) {
        ((FieldDefn) field).SetWidth(width);
    }

    @Override
    public void FieldSetJustifyRight(Object field) {
        ((FieldDefn) field).SetJustify(OJRight);
    }

    @Override
    public void FieldSetPrecision(Object field, int precision) {
        ((FieldDefn) field).SetPrecision(precision);
    }

    @Override
    public boolean FieldIsIntegerType(long type) {
        return type == OFTInteger;
    }

    @Override
    public boolean FieldIsRealType(long type) {
        return type == OFTReal;
    }

    @Override
    public boolean FieldIsBinaryType(long type) {
        return type == OFTBinary;
    }

    @Override
    public boolean FieldIsDateType(long type) {
        return type == OFTDate;
    }

    @Override
    public boolean FieldIsTimeType(long type) {
        return type == OFTTime;
    }

    @Override
    public boolean FieldIsDateTimeType(long type) {
        return type == OFTDateTime;
    }

    @Override
    public boolean FieldIsIntegerListType(long type) {
        return type == OFTIntegerList;
    }

    @Override
    public boolean FieldIsRealListType(long type) {
        return type == OFTRealList;
    }

    @Override
    public Object CreateStringField(String name) {
        return new FieldDefn(name, OFTString);
    }

    @Override
    public Object CreateIntegerField(String name) {
        return new FieldDefn(name, OFTInteger);
    }

    @Override
    public Object CreateRealField(String name) {
        return new FieldDefn(name, OFTReal);
    }

    @Override
    public Object CreateBinaryField(String name) {
        return new FieldDefn(name, OFTBinary);
    }

    @Override
    public Object CreateDateField(String name) {
        return new FieldDefn(name, OFTDate);
    }

    @Override
    public Object CreateTimeField(String name) {
        return new FieldDefn(name, OFTTime);
    }

    @Override
    public Object CreateDateTimeField(String name) {
        return new FieldDefn(name, OFTDateTime);
    }

    @Override
    public long FeatureGetFID(Object feature) {
        return ((Feature) feature).GetFID();
    }

    @Override
    public boolean FeatureIsFieldSet(Object feature, int i) {
        return ((Feature) feature).IsFieldSet(i);
    }

    @Override
    public void FeatureSetGeometryDirectly(Object feature, Object geometry) {
        ((Feature) feature).SetGeometryDirectly((Geometry) geometry);
    }

    @Override
    public Object FeatureGetGeometry(Object feature) {
        return ((Feature) feature).GetGeometryRef();
    }

    @Override
    public void FeatureUnsetField(Object feature, int i) {
        ((Feature) feature).UnsetField(i);
    }

    @Override
    public void FeatureSetFieldInteger(Object feature, int field, int value) {
        ((Feature) feature).SetField(field, value);
    }

    @Override
    public void FeatureSetFieldDouble(Object feature, int field, double value) {
        ((Feature) feature).SetField(field, value);
    }

    @Override
    public void FeatureSetFieldBinary(Object feature, int field, int length, byte[] value) {
        // TODO: doesn't seem to be available in the api?
        // ((Feature)feature).SetFieldBinary()
    }

    @Override
    public void FeatureSetFieldDateTime(
            Object feature, int field, int year, int month, int day, int hour, int minute, int second, int tz) {
        ((Feature) feature).SetField(field, year, month, day, hour, minute, second, tz);
    }

    @Override
    public void FeatureSetFieldString(Object feature, int field, String str) {
        ((Feature) feature).SetField(field, str);
    }

    @Override
    public String FeatureGetFieldAsString(Object feature, int i) {
        return ((Feature) feature).GetFieldAsString(i);
    }

    @Override
    public int FeatureGetFieldAsInteger(Object feature, int i) {
        return ((Feature) feature).GetFieldAsInteger(i);
    }

    @Override
    public double FeatureGetFieldAsDouble(Object feature, int i) {
        return ((Feature) feature).GetFieldAsDouble(i);
    }

    @Override
    public void FeatureGetFieldAsDateTime(
            Object feature,
            int i,
            int[] year,
            int[] month,
            int[] day,
            int[] hour,
            int[] minute,
            int[] second,
            int[] tzFlag) {
        try {
            if (USE_FLOAT_SECONDS) {
                float[] secondFloat = new float[second.length];
                GET_FIELD_METHOD.invoke(feature, i, year, month, day, hour, minute, secondFloat, tzFlag);
                for (int j = 0; j < second.length; j++) {
                    second[j] = (int) secondFloat[j];
                }
            } else {
                GET_FIELD_METHOD.invoke(feature, i, year, month, day, hour, minute, second, tzFlag);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unexpected exception found while retrieving a datetime field, using float seconds: "
                            + USE_FLOAT_SECONDS
                            + " and method "
                            + GET_FIELD_METHOD,
                    e);
        }
    }

    @Override
    public void FeatureDestroy(Object feature) {
        ((Feature) feature).delete();
    }

    @Override
    public long GetPointType() {
        return wkbPoint;
    }

    @Override
    public long GetPoint25DType() {
        return wkbPoint25D;
    }

    @Override
    public long GetLinearRingType() {
        return wkbLinearRing;
    }

    @Override
    public long GetLineStringType() {
        return wkbLineString;
    }

    @Override
    public long GetLineString25DType() {
        return wkbLineString25D;
    }

    @Override
    public long GetPolygonType() {
        return wkbPolygon;
    }

    @Override
    public long GetPolygon25DType() {
        return wkbPolygon25D;
    }

    @Override
    public long GetMultiPointType() {
        return wkbMultiPoint;
    }

    @Override
    public long GetMultiLineStringType() {
        return wkbMultiLineString;
    }

    @Override
    public long GetMultiLineString25DType() {
        return wkbMultiLineString25D;
    }

    @Override
    public long GetMultiPolygonType() {
        return wkbMultiPolygon;
    }

    @Override
    public long GetMultiPolygon25DType() {
        return wkbMultiPolygon25D;
    }

    @Override
    public long GetGeometryCollectionType() {
        return wkbGeometryCollection;
    }

    @Override
    public long GetGeometryCollection25DType() {
        return wkbGeometryCollection25D;
    }

    @Override
    public long GetGeometryNoneType() {
        return wkbNone;
    }

    @Override
    public long GetGeometryUnknownType() {
        return wkbUnknown;
    }

    @Override
    public int GeometryGetWkbSize(Object geom) {
        return ((Geometry) geom).WkbSize();
    }

    @Override
    public int GeometryExportToWkb(Object geom, byte[] wkb) {
        return ((Geometry) geom).ExportToWkb(wkb, wkbXDR);
    }

    @Override
    public Object GeometryCreateFromWkb(byte[] wkb, int[] ret) {
        return Geometry.CreateFromWkb(wkb);
    }

    @Override
    public String GeometryExportToWkt(Object geom, int[] ret) {
        return ((Geometry) geom).ExportToWkt();
    }

    @Override
    public Object GeometryCreateFromWkt(String wkt, int[] ret) {
        return Geometry.CreateFromWkt(wkt);
    }

    @Override
    public void GeometryDestroy(Object geometry) {
        ((Geometry) geometry).delete();
    }

    @Override
    public String SpatialRefGetAuthorityCode(Object spatialRef, String authority) {
        return ((SpatialReference) spatialRef).GetAuthorityCode(authority);
    }

    @Override
    public String SpatialRefExportToWkt(Object spatialRef) {
        return ((SpatialReference) spatialRef).ExportToWkt();
    }

    @Override
    public void SpatialRefRelease(Object spatialRef) {
        ((SpatialReference) spatialRef).delete();
    }

    @Override
    public Object NewSpatialRef(String wkt) {
        return new SpatialReference(wkt);
    }
}
