package org.geotools.data.ogr;

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.OsrLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ogr.bridj.OGREnvelope;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

@SuppressWarnings("rawtypes")
/**
 * 
 *
 * @source $URL$
 */
public class OGRFeatureSource extends ContentFeatureSource {

    public OGRFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    @Override
    public OGRDataStore getDataStore() {
        return (OGRDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        CoordinateReferenceSystem crs = getSchema().getCoordinateReferenceSystem();
        
        // we need to know how much we can translate of the filter (the translator will also 
        // simplify the filter)
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            // empty results
            return new ReferencedEnvelope(crs);
        } else if (!filterTx.isFilterFullySupported()) {
            return null;
        } else {
            // encodable, we then encode and get the bounds
            Pointer dataSource = null;
            Pointer layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                Pointer<OGREnvelope> boundsPtr = allocate(OGREnvelope.class);
                int code = OGR_L_GetExtent(layer, boundsPtr, 0);
                if (code == OGRERR_FAILURE) {
                    return null;
                } else {
                    OGREnvelope bounds = boundsPtr.get();
                    return new ReferencedEnvelope(bounds.MinX(), bounds.MaxX(), bounds.MinY(),
                            bounds.MaxY(), crs);
                }
            } finally {
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
            }
        }
    }

    /**
     * Sets the spatial filter and attribute filter on the specified layer
     * 
     * @param layer
     * @param filterTx
     * @throws IOException
     */
    private void setLayerFilters(Pointer layer, OGRFilterTranslator filterTx) throws IOException {
        Geometry spatialFilter = filterTx.getSpatialFilter();
        if (spatialFilter != null) {
            Pointer ogrGeometry = new GeometryMapper.WKB(new GeometryFactory())
                    .parseGTGeometry(spatialFilter);
            OGR_L_SetSpatialFilter(layer, ogrGeometry);
        }

        String attFilter = filterTx.getAttributeFilter();
        if (attFilter != null) {
            OGR_L_SetAttributeFilter(layer, pointerToCString(attFilter));
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // check how much we can encode
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            return 0;
        } else if (!filterTx.isFilterFullySupported()) {
            // too expensive then
            return -1;
        } else {
            // encode and count
            Pointer dataSource = null;
            Pointer layer = null;

            try {
                // grab the layer
                String typeName = getEntry().getTypeName();
                dataSource = getDataStore().openOGRDataSource(false);
                layer = openOGRLayer(dataSource, typeName);

                // filter it
                setLayerFilters(layer, filterTx);

                return OGR_L_GetFeatureCount(layer, 0);
            } finally {
                OGRUtils.releaseLayer(layer);
                OGRUtils.releaseDataSource(dataSource);
            }
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        OGRFilterTranslator filterTx = new OGRFilterTranslator(getSchema(), query.getFilter());
        if (Filter.EXCLUDE.equals(filterTx.getFilter())) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(getSchema());
        } 
        
        
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        Pointer dataSource = null;
        Pointer layer = null;
        Pointer definition = null;

        try {
            // setup the builder
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            String typeName = getEntry().getTypeName();
            tb.setName(typeName);
            tb.setNamespaceURI(getDataStore().getNamespaceURI());

            // grab the layer definition
            dataSource = getDataStore().openOGRDataSource(false);
            layer = openOGRLayer(dataSource, typeName);
            definition = OGR_L_GetLayerDefn(layer);

            // figure out the geometry
            Class<? extends Geometry> geometryBinding = getGeometryBinding(definition);
            if (geometryBinding != null) {
                CoordinateReferenceSystem crs = getCRS(layer);
                tb.add("the_geom", geometryBinding, crs);
            }

            // get the non geometric fields
            final int count = OGR_FD_GetFieldCount(definition);
            for (int i = 0; i < count; i++) {
                Pointer field = OGR_FD_GetFieldDefn(definition, i);
                String name = OGR_Fld_GetNameRef(field).getCString();
                Class binding = getBinding(field);
                int width = OGR_Fld_GetWidth(field);
                if (width > 0) {
                    tb.length(width);
                }
                tb.add(name, binding);
            }

            return tb.buildFeatureType();
        } finally {
            OGRUtils.releaseDefinition(definition);
            OGRUtils.releaseLayer(layer);
            OGRUtils.releaseDataSource(dataSource);
        }
    }

    private Class getBinding(Pointer field) {
        ValuedEnum<OGRFieldType> type = OGR_Fld_GetType(field);
        long value = type.value();
        if (value == OGRFieldType.OFTInteger.value()) {
            return Integer.class;
        } else if (value == OGRFieldType.OFTIntegerList.value()) {
            return int[].class;
        } else if (value == OGRFieldType.OFTReal.value()) {
            return Double.class;
        } else if (value == OGRFieldType.OFTRealList.value()) {
            return double[].class;
        } else if (value == OGRFieldType.OFTBinary.value()) {
            return byte[].class;
        } else if (value == OGRFieldType.OFTDate.value()) {
            return java.sql.Date.class;
        } else if (value == OGRFieldType.OFTTime.value()) {
            return java.sql.Time.class;
        } else if (value == OGRFieldType.OFTDateTime.value()) {
            return java.sql.Timestamp.class;
        } else {
            // whatever else we'll map a string
            return String.class;
        }

    }

    private CoordinateReferenceSystem getCRS(Pointer layer) throws IOException {
        Pointer spatialReference = null;
        CoordinateReferenceSystem crs = null;
        try {
            spatialReference = OGR_L_GetSpatialRef(layer);
            if (spatialReference == null) {
                return null;
            }

            try {
                Pointer<Byte> code = OSRGetAuthorityCode(spatialReference, pointerToCString("EPSG"));
                if (code != null) {
                    String fullCode = "EPSG:" + code;
                    crs = CRS.decode(fullCode);
                }
            } catch (Exception e) {
                // fine, the code might be unknown to out authority
            }
            if (crs == null) {
                try {
                    Pointer<Pointer<Byte>> wktPtr = allocatePointer(Byte.class);
                    OSRExportToWkt(spatialReference, wktPtr);
                    String wkt = wktPtr.getPointer(Byte.class).getCString();
                    crs = CRS.parseWKT(wkt);
                } catch (Exception e) {
                    // the wkt might reference an unsupported projection
                }
            }
            return crs;
        } finally {
            OGRUtils.releaseSpatialReference(spatialReference);
        }
    }

    private Class<? extends Geometry> getGeometryBinding(Pointer definition) throws IOException {
        ValuedEnum<OGRwkbGeometryType> gt = OGR_FD_GetGeomType(definition);
        long value = gt.value();
        if (value == OGRwkbGeometryType.wkbPoint.value()
                || value == OGRwkbGeometryType.wkbPoint25D.value()) {
            return Point.class;
        } else if (value == OGRwkbGeometryType.wkbLinearRing.value()) {
            return LinearRing.class;
        } else if (value == OGRwkbGeometryType.wkbLineString.value()
                || value == OGRwkbGeometryType.wkbLineString25D.value()) {
            return LineString.class;
        } else if (value == OGRwkbGeometryType.wkbMultiLineString.value()
                || value == OGRwkbGeometryType.wkbMultiLineString25D.value()) {
            return MultiLineString.class;
        } else if (value == OGRwkbGeometryType.wkbPolygon.value()
                || value == OGRwkbGeometryType.wkbPolygon25D.value()) {
            return Polygon.class;
        } else if (value == OGRwkbGeometryType.wkbMultiPolygon.value()
                || value == OGRwkbGeometryType.wkbMultiPolygon25D.value()) {
            return MultiPolygon.class;
        } else if (value == OGRwkbGeometryType.wkbGeometryCollection.value()
                || value == OGRwkbGeometryType.wkbGeometryCollection25D.value()) {
            return GeometryCollection.class;
        } else if (value == OGRwkbGeometryType.wkbNone.value()) {
            return null;
        } else if (value == OGRwkbGeometryType.wkbUnknown.value()) {
            return Geometry.class;
        } else {
            throw new IOException("Unknown geometry type: " + value);
        }
    }

    Pointer openOGRLayer(Pointer dataSource, String layerName) throws IOException {
        Pointer layer = OGR_DS_GetLayerByName(dataSource, pointerToCString(layerName));
        if (layer == null) {
            throw new IOException("OGR could not find layer '" + layerName + "'");
        }
        return layer;
    }

}
