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
package org.geotools.data.ogr.bridj;

import static org.bridj.Pointer.allocateInt;
import static org.bridj.Pointer.allocatePointer;
import static org.bridj.Pointer.pointerToBytes;
import static org.bridj.Pointer.pointerToCString;
import static org.bridj.Pointer.pointerToPointer;
import static org.geotools.data.ogr.bridj.BridjUtilities.getCString;
import static org.geotools.data.ogr.bridj.BridjUtilities.pointerToCStrings;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.CPLGetLastErrorMsg;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.OsrLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;
import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRJustification;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbByteOrder;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Interacts with OGR via the bridj library.
 * 
 * @author Andrea Aime, GeoSolutions
 * @author Justin Deoliveira, OpenGeo
 */
public class BridjOGR implements OGR {

    static {
        GdalInit.init();
    
        // perform OGR format registration once
        if (OGRGetDriverCount() == 0) {
            OGRRegisterAll();
        }
    }

    @Override
    public int GetDriverCount() {
        return OGRGetDriverCount();
    }

    @Override
    public Object GetDriver(int i) {
        return OGRGetDriver(i);
    }

    @Override
    public Object GetDriverByName(String name) {
        return OGRGetDriverByName(pointerToCString(name));
    }
    
    @Override
    public Object OpenShared(String dataSourceName, int mode) {
        return OGROpenShared(pointerToCString(dataSourceName), mode, null);
    }

    @Override
    public Object Open(String dataSourceName, int mode) {
        return OGROpen(pointerToCString(dataSourceName), mode, null);
    }
    
    @Override
    public void CheckError(int code) throws IOException {
        if (code == OGRERR_NONE) {
            return;
        }
        
        String error = getCString(CPLGetLastErrorMsg());

        switch (code) {
        case OGRERR_CORRUPT_DATA:
            throw new IOException("OGR reported a currupt data error: " + error);
        case OGRERR_FAILURE:
            throw new IOException("OGR reported a generic failure: " + error);
        case OGRERR_INVALID_HANDLE:
            throw new IOException("OGR reported an invalid handle error: " + error);
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
        return getCString(CPLGetLastErrorMsg());
    }
    
    @Override
    public String DriverGetName(Object driver) {
        return getCString(OGR_Dr_GetName((Pointer<?>)driver));
    }
    
    @Override
    public Object DriverOpen(Object driver, String dataSourceName, int mode) {
        return OGR_Dr_Open((Pointer<?>)driver, pointerToCString(dataSourceName), mode);
    }
    
    @Override
    public Object DriverCreateDataSource(Object driver, String dataSourceName,
            String[] opts) {
        
        return OGR_Dr_CreateDataSource((Pointer<?>)driver, pointerToCString(dataSourceName), 
            pointerToCStrings(opts));
    }
    
    @Override
    public void DriverRelease(Object driver) {
        ((Pointer<?>)driver).release();
    }

    @Override
    public Object DataSourceGetDriver(Object dataSource) {
        return (Pointer<?>) OGR_DS_GetDriver((Pointer<?>)dataSource);
    }
    
    @Override
    public int DataSourceGetLayerCount(Object dataSource) {
        return OGR_DS_GetLayerCount((Pointer<?>)dataSource);
    }
    
    @Override
    public Object DataSourceGetLayer(Object dataSource, int i) {
        return OGR_DS_GetLayer((Pointer<?>)dataSource, i);
    }
    
    @Override
    public Object DataSourceGetLayerByName(Object dataSource, String name) {
        return OGR_DS_GetLayerByName((Pointer<?>)dataSource, pointerToCString(name));
    }
    
    @Override
    public void DataSourceRelease(Object dataSource) {
        OGRUtils.releaseDataSource((Pointer<?>)dataSource);
    }

    @Override
    public Object DataSourceCreateLayer(Object dataSource, String name,
            Object spatialReference, long geomType, String[] opts) {
        return OGR_DS_CreateLayer((Pointer<?>)dataSource, pointerToCString(name), 
            (Pointer<?>)spatialReference, OGRwkbGeometryType.fromValue(geomType), 
            BridjUtilities.pointerToCStrings(opts));
    }
    
    @Override
    public Object DataSourceExecuteSQL(Object dataSource, String sql, Object spatialFilter) {
        return OGR_DS_ExecuteSQL((Pointer<?>)dataSource, pointerToCString(sql), (Pointer<?>)spatialFilter, null);
    }
    
    @Override
    public Object LayerGetLayerDefn(Object layer) {
        return OGR_L_GetLayerDefn((Pointer<?>)layer);
    }
    
    @Override
    public int LayerGetFieldCount(Object layerDefn) {
        return OGR_FD_GetFieldCount((Pointer<?>)layerDefn);
    }
    
    @Override
    public Object LayerGetFieldDefn(Object layerDefn, int i) {
        return OGR_FD_GetFieldDefn((Pointer<?>)layerDefn, i);
    }
    
    @Override
    public String LayerGetName(Object layer) {
        return getCString(OGR_L_GetName((Pointer<?>)layer));
    }
    
    @Override
    public long LayerGetGeometryType(Object layerDefn) {
        return OGR_FD_GetGeomType((Pointer<?>)layerDefn).value();
    }
    
    @Override
    public Object LayerGetSpatialRef(Object layer) {
        return OGR_L_GetSpatialRef((Pointer<?>)layer);
    }
    
    @Override
    public Object LayerGetExtent(Object layer) {
        Pointer<OGREnvelope> boundsPtr = Pointer.allocate(OGREnvelope.class);
        int code = OGR_L_GetExtent((Pointer<?>)layer, boundsPtr, 0);
        if (code == OGRERR_FAILURE) {
            return null;
        }
        return boundsPtr;
    }
    
    @Override
    public long LayerGetFeatureCount(Object layer) {
        return OGR_L_GetFeatureCount((Pointer<?>)layer, 0);
    }
    
    @Override
    public void LayerRelease(Object layer) {
        OGRUtils.releaseLayer((Pointer<?>)layer);
    }

    @Override
    public void LayerReleaseLayerDefn(Object layerDefn) {
        OGRUtils.releaseDefinition((Pointer<?>) layerDefn);
    }

    @Override
    public boolean LayerCanDeleteFeature(Object layer) {
        return OGR_L_TestCapability((Pointer<?>)layer, pointerToCString(OLCDeleteFeature)) != 0;
    }
    
    @Override
    public boolean LayerCanWriteRandom(Object layer) {
        return OGR_L_TestCapability((Pointer<?>)layer, pointerToCString(OLCRandomWrite)) != 0;
    }
    
    @Override
    public boolean LayerCanWriteSequential(Object layer) {
        return OGR_L_TestCapability((Pointer<?>)layer,pointerToCString(OLCSequentialWrite)) != 0;
    }
    
    @Override
    public boolean LayerCanCreateField(Object layer) {
        return OGR_L_TestCapability((Pointer<?>)layer, pointerToCString(OLCCreateField)) != 0;
    }
    
    @Override
    public boolean LayerCanIgnoreFields(Object layer) {
        return OGR_L_TestCapability((Pointer<?>)layer, pointerToCString(OLCIgnoreFields)) != 0;
    }
    
    @Override
    public void LayerCreateField(Object layer, Object fieldDefn, int approx) {
        OGR_L_CreateField((Pointer<?>)layer, (Pointer<?>)fieldDefn, approx);
    }
    
    @Override
    public void LayerSyncToDisk(Object layer) {
        OGR_L_SyncToDisk((Pointer<?>)layer);
    }

    @Override
    public Object LayerNewFeature(Object layerDefn) {
        return OGR_F_Create((Pointer<?>)layerDefn);
    }
    
    @Override
    public ReferencedEnvelope toEnvelope(Object extent,
            CoordinateReferenceSystem crs) {
        OGREnvelope bounds = ((Pointer<OGREnvelope>)extent).get();
        return new ReferencedEnvelope(bounds.MinX(), bounds.MaxX(), bounds.MinY(),
                bounds.MaxY(), crs);
    }

    @Override
    public void LayerSetSpatialFilter(Object layer, Object geometry) {
        OGR_L_SetSpatialFilter((Pointer<?>)layer, (Pointer<?>)geometry);
    }
    
    @Override
    public void LayerSetAttributeFilter(Object layer, String attFilter) {
        OGR_L_SetAttributeFilter((Pointer<?>)layer, pointerToCString(attFilter));
    }

    @Override
    public int LayerSetIgnoredFields(Object layer, String[] fields) {
        Pointer<Pointer<Byte>> ifPtr = BridjUtilities.pointerToCStrings(fields);
        return OGR_L_SetIgnoredFields((Pointer<?>)layer, ifPtr);
    }

    @Override
    public void LayerResetReading(Object layer) {
        OGR_L_ResetReading((Pointer<?>)layer);
    }
    
    @Override
    public Object LayerGetNextFeature(Object layer) {
        return OGR_L_GetNextFeature((Pointer<?>)layer);
    }
    
    @Override
    public boolean LayerDeleteFeature(Object layer, long fid) {
        return OGR_L_DeleteFeature((Pointer<?>)layer, fid) == 0;
    }
    
    @Override
    public int LayerSetFeature(Object layer, Object feature) {
        return OGR_L_SetFeature((Pointer<?>)layer, (Pointer<?>)feature);
    }

    @Override
    public int LayerCreateFeature(Object layer, Object feature) {
        return OGR_L_CreateFeature((Pointer<?>)layer, (Pointer<?>)feature);
    }

    @Override
    public String FieldGetName(Object field) {
        return getCString(OGR_Fld_GetNameRef((Pointer<?>)field));
    }
    
    @Override
    public long FieldGetType(Object field) {
        return OGR_Fld_GetType((Pointer<?>)field).value();
    }
    
    @Override
    public int FieldGetWidth(Object field) {
        return OGR_Fld_GetWidth((Pointer<?>)field);
    }
    
    @Override
    public void FieldSetWidth(Object field, int width) {
        OGR_Fld_SetWidth((Pointer<?>)field, width);
    }
    
    @Override
    public void FieldSetJustifyRight(Object field) {
        OGR_Fld_SetJustify((Pointer<?>)field, OGRJustification.OJRight);
    }
    
    @Override
    public void FieldSetPrecision(Object field, int precision) {
        OGR_Fld_SetPrecision((Pointer<?>)field, 15);
    }
    
    @Override
    public boolean FieldIsIntegerType(long type) {
        return type == OGRFieldType.OFTInteger.value();
    }
    
    @Override
    public boolean FieldIsRealType(long type) {
        return type == OGRFieldType.OFTReal.value();
    }
    
    @Override
    public boolean FieldIsBinaryType(long type) {
        return type == OGRFieldType.OFTBinary.value();
    }
    
    @Override
    public boolean FieldIsDateType(long type) {
        return type == OGRFieldType.OFTDate.value();
    }
    
    @Override
    public boolean FieldIsTimeType(long type) {
        return type == OGRFieldType.OFTTime.value();
    }
    
    @Override
    public boolean FieldIsDateTimeType(long type) {
        return type == OGRFieldType.OFTDateTime.value();
    }
    
    @Override
    public boolean FieldIsIntegerListType(long type) {
        return type == OGRFieldType.OFTIntegerList.value();
    }
    
    @Override
    public boolean FieldIsRealListType(long type) {
        return type == OGRFieldType.OFTRealList.value();
    }
    
    @Override
    public Object CreateStringField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTString);
    }
    
    @Override
    public Object CreateIntegerField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTInteger);
    }
    
    @Override
    public Object CreateRealField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTReal);
    }
    
    @Override
    public Object CreateBinaryField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTBinary);
    }
    
    @Override
    public Object CreateDateField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTDate);
    }
    
    @Override
    public Object CreateTimeField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTTime);
    }
    
    @Override
    public Object CreateDateTimeField(String name) {
        return OGR_Fld_Create(pointerToCString(name), OGRFieldType.OFTDateTime);
    }
    
    @Override
    public long FeatureGetFID(Object feature) {
        return OGR_F_GetFID((Pointer<?>)feature);
    }
    
    @Override
    public boolean FeatureIsFieldSet(Object feature, int i) {
        return OGR_F_IsFieldSet((Pointer<?>)feature, i) != 0;
    }
    
    @Override
    public void FeatureSetGeometryDirectly(Object feature, Object geometry) {
        OGR_F_SetGeometryDirectly((Pointer<?>)feature, (Pointer<?>)geometry);
    }
    
    @Override
    public Object FeatureGetGeometry(Object feature) {
        return OGR_F_GetGeometryRef((Pointer<?>)feature);
    }
    
    @Override
    public void FeatureUnsetField(Object feature, int i) {
        OGR_F_UnsetField((Pointer<?>)feature, i);
    }

    @Override
    public void FeatureSetFieldInteger(Object feature, int field, int value) {
        OGR_F_SetFieldInteger((Pointer<?>)feature, field, value);
    }
    
    @Override
    public void FeatureSetFieldDouble(Object feature, int field, double value) {
        OGR_F_SetFieldDouble((Pointer<?>)feature, field, value);
    }
    
    @Override
    public void FeatureSetFieldBinary(Object feature, int field, int length, byte[] value) {
        OGR_F_SetFieldBinary((Pointer<?>)feature, field, value.length, pointerToBytes(value));
    }
    
    @Override
    public void FeatureSetFieldDateTime(Object feature, int field, int year,
            int month, int day, int hour, int minute, int second, int tz) {
        OGR_F_SetFieldDateTime((Pointer<?>)feature, field, year, month, day, hour, minute, second, tz);
    }

    @Override
    public void FeatureSetFieldString(Object feature, int field, String str) {
        OGR_F_SetFieldString((Pointer<?>)feature, field, pointerToCString(str));
    }

    @Override
    public String FeatureGetFieldAsString(Object feature, int i) {
        return getCString(OGR_F_GetFieldAsString((Pointer<?>)feature, i));
    }

    @Override
    public int FeatureGetFieldAsInteger(Object feature, int i) {
        return OGR_F_GetFieldAsInteger((Pointer<?>)feature, i);
    }
    
    @Override
    public double FeatureGetFieldAsDouble(Object feature, int i) {
        return OGR_F_GetFieldAsDouble((Pointer<?>)feature, i);
    }
    
    @Override
    public void FeatureGetFieldAsDateTime(Object feature, int i, int[] yeara, int[] montha, 
        int[] daya, int[] houra, int[] minutea, int[] seconda, int[] tzFlag) {
      
      Pointer<Integer> year = allocateInt();
      Pointer<Integer> month = allocateInt();
      Pointer<Integer> day = allocateInt();
      Pointer<Integer> hour = allocateInt();
      Pointer<Integer> minute = allocateInt();
      Pointer<Integer> second = allocateInt();
      Pointer<Integer> timeZone = allocateInt();
      
      OGR_F_GetFieldAsDateTime((Pointer<?>)feature, i, year, month, day, hour, minute, second, timeZone);

      yeara[0] = year.getInt();
      montha[0] = month.getInt();
      daya[0] = day.getInt();
      houra[0] = hour.getInt();
      minutea[0] = minute.getInt();
      seconda[0] = second.getInt();
      tzFlag[0] = timeZone.getInt();
    }

    @Override
    public void FeatureDestroy(Object feature) {
        OGR_F_Destroy((Pointer<?>)feature);
    }

    @Override
    public long GetPointType() {
        return OGRwkbGeometryType.wkbPoint.value();
    }
    
    @Override
    public long GetPoint25DType() {
        return OGRwkbGeometryType.wkbPoint25D.value();
    }
    
    @Override
    public long GetLinearRingType() {
        return OGRwkbGeometryType.wkbLinearRing.value();
    }
    
    @Override
    public long GetLineStringType() {
        return OGRwkbGeometryType.wkbLineString.value();
    }
    
    @Override
    public long GetLineString25DType() {
        return OGRwkbGeometryType.wkbLineString25D.value();
    }
    
    @Override
    public long GetPolygonType() {
        return OGRwkbGeometryType.wkbPolygon.value();
    }
    
    @Override
    public long GetPolygon25DType() {
        return OGRwkbGeometryType.wkbPolygon25D.value();
    }
    
    @Override
    public long GetMultiPointType() {
        return OGRwkbGeometryType.wkbMultiPoint.value();
    }
    
    @Override
    public long GetMultiLineStringType() {
        return OGRwkbGeometryType.wkbMultiLineString.value();
    }
    
    @Override
    public long GetMultiLineString25DType() {
        return OGRwkbGeometryType.wkbMultiLineString25D.value();
    }
    
    @Override
    public long GetMultiPolygonType() {
        return OGRwkbGeometryType.wkbMultiPolygon.value();
    }
    
    @Override
    public long GetMultiPolygon25DType() {
        return OGRwkbGeometryType.wkbMultiPolygon25D.value(); 
    }
    
    @Override
    public long GetGeometryCollectionType() {
        return OGRwkbGeometryType.wkbGeometryCollection.value();
    }
    
    @Override
    public long GetGeometryCollection25DType() {
        return OGRwkbGeometryType.wkbGeometryCollection25D.value();
    }

    @Override
    public long GetGeometryNoneType() {
        return OGRwkbGeometryType.wkbNone.value();
    }
    
    @Override
    public long GetGeometryUnknownType() {
        return OGRwkbGeometryType.wkbUnknown.value();
    }
    
    @Override
    public int GeometryGetWkbSize(Object geom) {
        return OGR_G_WkbSize((Pointer<?>)geom);
    }
    
    @Override
    public int GeometryExportToWkb(Object geom, byte[] wkb) {
        Pointer<Byte> p = pointerToBytes(wkb);
        int ret = OGR_G_ExportToWkb((Pointer<?>)geom, OGRwkbByteOrder.wkbXDR, p);
        System.arraycopy(p.getBytes(), 0, wkb, 0, wkb.length);
        return ret;
    }
    
    @Override
    public Object GeometryCreateFromWkb(byte[] wkb, int[] ret) {
        Pointer<Pointer<?>> ptr = allocatePointer();
        ret[0] = OGR_G_CreateFromWkb(pointerToBytes(wkb), null, ptr, wkb.length);
        return ptr.getPointer(Pointer.class);

    }

    @Override
    public String GeometryExportToWkt(Object geom, int[] ret) {
        Pointer<Pointer<Byte>> wktPtr = allocatePointer(Byte.class);
        ret[0] = OGR_G_ExportToWkt((Pointer<?>)geom, wktPtr);
        return getCString(wktPtr.getPointer(Byte.class));
    }

    @Override
    public Object GeometryCreateFromWkt(String wkt, int[] ret) {
        Pointer<Pointer<Byte>> ptr = pointerToPointer(pointerToCString(wkt));
        Pointer<Pointer<?>> geom = allocatePointer();
        ret[0] = OGR_G_CreateFromWkt(ptr, null, geom);
        return geom.getPointer(Pointer.class);
    }

    @Override
    public void GeometryDestroy(Object geometry) {
        OGR_G_DestroyGeometry((Pointer<?>)geometry);
    }

    @Override
    public String SpatialRefGetAuthorityCode(Object spatialRef, String authority) {
        return getCString(OSRGetAuthorityCode((Pointer<?>)spatialRef, pointerToCString(authority)));
    }
    
    @Override
    public String SpatialRefExportToWkt(Object spatialRef) {
        Pointer<Pointer<Byte>> wktPtr = allocatePointer(Byte.class);
        OSRExportToWkt((Pointer<?>)spatialRef, wktPtr);
        return getCString(wktPtr.getPointer(Byte.class));
    }

    @Override
    public void SpatialRefRelease(Object spatialRef) {
        OGRUtils.releaseSpatialReference((Pointer<?>) spatialRef);
    }

    @Override
    public Object NewSpatialRef(String wkt) {
        return OSRNewSpatialReference(pointerToCString(wkt));
    }

}
