/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.OGRUtils.*;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.ogr.bridj.OgrLibrary;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * A data store based on the OGR native library, bound to it via <a
 * href="http://code.google.com/p/bridj/">BridJ</a>
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
public class OGRDataStore extends ContentDataStore {
	
	private static final Logger LOGGER = Logger.getLogger(OGRDataStore.class.getName());

    static {
        GdalInit.init();

        // perform OGR format registration once
        if (OGRGetDriverCount() == 0) {
            OGRRegisterAll();
        }
    }

    String ogrSourceName;

    String ogrDriver;

    public OGRDataStore(String ogrName, String ogrDriver, URI namespace) {
        if (namespace != null) {
            setNamespaceURI(namespace.toString());
        }
        this.ogrSourceName = ogrName;
        this.ogrDriver = ogrDriver;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        Pointer dataSource = null;
        Pointer layer = null;
        try {
            dataSource = openOGRDataSource(false);

            List<Name> result = new ArrayList<Name>();
            int count = OGR_DS_GetLayerCount(dataSource);
            for (int i = 0; i < count; i++) {
                layer = OGR_DS_GetLayer(dataSource, i);
                String name = getLayerName(layer);
                if (name != null) {
                    result.add(new NameImpl(getNamespaceURI(), name));
                }
                OGRUtils.releaseLayer(layer);
            }
            return result;
        } catch (IOException e) {
            return Collections.emptyList();
        } finally {
            OGRUtils.releaseDataSource(dataSource);
            OGRUtils.releaseLayer(layer);
        }
    }

    Pointer openOGRDataSource(boolean update) throws IOException {
        Pointer ds = null;
        Pointer<Byte> sourcePtr = pointerToCString(ogrSourceName);
        int mode = update ? 1 : 0;
        if (ogrDriver != null) {
        	
            Pointer driver = OGRGetDriverByName(pointerToCString(ogrDriver));
            if (driver == null) {
                throw new IOException("Could not find a driver named " + driver);
            }
            ds = OGR_Dr_Open(driver, sourcePtr, mode);
            if (ds == null) {
                throw new IOException("OGR could not open '" + ogrSourceName + "' in "
                        + (update ? "read-write" : "read-only") + " mode with driver " + ogrDriver);
            }
        } else {
            ds = OGROpenShared(sourcePtr, mode, null);
            if (ds == null) {
                throw new IOException("OGR could not open '" + ogrSourceName + "' in "
                        + (update ? "read-write" : "read-only") + " mode");
            }
        }

        return ds;
    }

    Pointer openOGRLayer(Pointer dataSource, String layerName) throws IOException {
        Pointer layer = OGR_DS_GetLayerByName(dataSource, pointerToCString(layerName));
        if (layer == null) {
            throw new IOException("OGR could not find layer '" + layerName + "'");
        }
        return layer;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new OGRFeatureStore(entry, Query.ALL);
//FIXME mauro hack if you test a MapInfo TAB file this test doesn't work 
//        if (supportsInPlaceWrite(entry.getTypeName())) {
//            return new OGRFeatureStore(entry, Query.ALL);
//        } else {
//            return new OGRFeatureSource(entry, Query.ALL);
//        }
    }

    public boolean supportsInPlaceWrite(String typeName) throws IOException {
        Pointer ds = null;
        Pointer l = null;
        Pointer driver = null;
        try {
            driver = null; // FIXME HACK if you provide the driver the open method fail OGRGetDriverByName(pointerToCString(ogrDriver));

            // try opening in update mode
            ds = OGROpen(pointerToCString(ogrSourceName), 1, driver);
            if (ds == null) {
            	 String msg = CPLGetLastErrorMsg().getCString();
            	 if(msg == null || "".equals(msg) ){
            		 LOGGER.severe("OGROpen fail openning :" + ogrSourceName);
            	 } else {
            		 LOGGER.severe(msg);
            	 }
            	 
                return false;
            }
            l = openOGRLayer(ds, typeName);
            // for the moment we support working only with random writers
            boolean canDelete = OGR_L_TestCapability(l, pointerToCString(OLCDeleteFeature)) != 0;
            boolean canWriteRandom = OGR_L_TestCapability(l, pointerToCString(OLCRandomWrite)) != 0;
            boolean canWriteSequential = OGR_L_TestCapability(l,
                    pointerToCString(OLCSequentialWrite)) != 0;
            return canDelete && canWriteRandom && canWriteSequential;
        } finally {
            OGRUtils.releaseLayer(l);
            OGRUtils.releaseDataSource(ds);
            OGRUtils.releaseDefinition( driver ) ;
        }
    }

    public void createSchema(SimpleFeatureType schema) throws IOException {
        // TODO: add a field to allow approximate definitions
        createSchema(schema, false, null);
    }

    /**
     * Creates a new OGR layer with provided schema and options
     * 
     * @param schema the geotools schema
     * @param approximateFields if true, OGR will try to create fields that are approximations of
     *        the required ones when an exact match cannt be provided
     * @param options OGR data source/layer creation options
     * @throws IOException
     */
    public void createSchema(SimpleFeatureType schema, boolean approximateFields, String[] options)
            throws IOException {
        Pointer dataSource = null;
        Pointer layer = null;

        try {
            // either open datasource, or try creating one
            Pointer<Pointer<Byte>> optionsPointer = null;
            if (options != null && options.length > 0) {
                optionsPointer = pointerToCStrings(options);
            }
            dataSource = openOrCreateDataSource(options, dataSource, optionsPointer);

            FeatureTypeMapper mapper = new FeatureTypeMapper();

            layer = createNewLayer(schema, dataSource, optionsPointer, mapper);

            // check the ability to create fields
            if (OGR_L_TestCapability(layer, pointerToCString(OLCCreateField)) == 0) {
                throw new DataSourceException(
                        "OGR reports it's not possible to create fields on this layer");
            }

            // create fields
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor())
                    continue;

                Pointer fieldDefinition = mapper.getOGRFieldDefinition(ad);
                OGR_L_CreateField(layer, fieldDefinition, approximateFields ? 1 : 0);
            }

            OGR_L_SyncToDisk(layer);
        } finally {
            OGRUtils.releaseLayer(layer);
            OGRUtils.releaseDataSource(dataSource);
        }
    }

    /**
     * Creates a new OGR layer with provided data and options. This call is specifically made
     * available for the OGC store since for some data source types, such as GML or KML, it is not
     * possible to call createSchema() independently from a write, as the result will not contain
     * the schema definition without having data too. Also, in those formats, the output is writable
     * only so as long as it's empty, it's not possible to write against an existing GML file for
     * example.
     * 
     * @param data
     * @param approximateFields if true, OGR will try to create fields that are approximations of
     *        the required ones when an exact match cannt be provided
     * @param options OGR data source/layer creation options
     * @throws IOException
     */
    public void createSchema(SimpleFeatureCollection data, boolean approximateFields,
            String[] options) throws IOException {
        Pointer dataSource = null;
        Pointer layer = null;
        SimpleFeatureType schema = data.getSchema();
        SimpleFeatureIterator features;
        try {
            // either open datasource, or try creating one
            Pointer<Pointer<Byte>> optionsPointer = null;
            if (options != null && options.length > 0) {
                optionsPointer = pointerToCStrings(options);
            }
            dataSource = openOrCreateDataSource(options, dataSource, optionsPointer);

            FeatureTypeMapper mapper = new FeatureTypeMapper();

            layer = createNewLayer(schema, dataSource, optionsPointer, mapper);

            // check the ability to create fields
            if (OGR_L_TestCapability(layer, pointerToCString(OLCCreateField)) == 0) {
                throw new DataSourceException(
                        "OGR reports it's not possible to create fields on this layer");
            }

            // create fields
            Map<String, String> nameMap = new HashMap<String, String>();
            for (int i = 0, j = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor()) {
                    continue;
                }

                Pointer fieldDefinition = mapper.getOGRFieldDefinition(ad);
                OGR_L_CreateField(layer, fieldDefinition, approximateFields ? 1 : 0);
                
                // the data source might have changed the name of the field, map them
                String newName = getCString(OGR_Fld_GetNameRef(fieldDefinition));
                nameMap.put(newName, ad.getLocalName());
                j++;
            }

            // get back the feature definition
            Pointer layerDefinition = OGR_L_GetLayerDefn(layer);

            // remap positions, as the store might add extra attributes (and the field api is
            // positional)
            Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
            int count = OGR_FD_GetFieldCount(layerDefinition);
            for (int i = 0; i < count; i++) {
                Pointer fd = OGR_FD_GetFieldDefn(layerDefinition, i);
                String newName = getCString(OGR_Fld_GetNameRef(fd));
                if (newName != null) {
                    String oldName = nameMap.get(newName);
                    for (int j = 0; j < schema.getAttributeCount(); j++) {
                        if (schema.getDescriptor(j).getLocalName().equals(oldName)) {
                            indexMap.put(j, i);
                        }
                    }
                }
            }

            // iterate and write out without going throught the ContentDataStore api, which
            // assumes it's possible to let go of it later
            GeometryMapper geomMapper = new GeometryMapper.WKB(new GeometryFactory());
            features = data.features();
            while (features.hasNext()) {
                SimpleFeature feature = features.next();

                // create the equivalent ogr feature
                Pointer ogrFeature = OGR_F_Create(layerDefinition);
                for (int i = 0; i < schema.getAttributeCount(); i++) {
                    Object value = feature.getAttribute(i);
                    if (value instanceof Geometry) {
                        // using setGeoemtryDirectly the feature becomes the owner of the generated
                        // OGR geometry and we don't have to .delete() it (it's faster, too)
                        Pointer geometry = geomMapper.parseGTGeometry((Geometry) value);
                        OGR_F_SetGeometryDirectly(ogrFeature, geometry);
                    } else {
                        // remap index
                        int ogrIndex = indexMap.get(i);
                        FeatureMapper.setFieldValue(layerDefinition, ogrFeature, ogrIndex, value);
                    }
                }

                // write it out
                checkError(OGR_L_CreateFeature(layer, ogrFeature));
                OGR_F_Destroy(ogrFeature);
            }

            OGR_L_SyncToDisk(layer);
        } finally {
            OGRUtils.releaseLayer(layer);
            OGRUtils.releaseDataSource(dataSource);
        }
    }

    private Pointer createNewLayer(SimpleFeatureType schema, Pointer dataSource,
            Pointer<Pointer<Byte>> optionsPointer, FeatureTypeMapper mapper) throws IOException,
            DataSourceException {
        Pointer layer;
        // get the spatial reference corresponding to the default geometry
        GeometryDescriptor geomType = schema.getGeometryDescriptor();
        ValuedEnum<OGRwkbGeometryType> ogrGeomType = mapper.getOGRGeometryType(geomType);
        Pointer spatialReference = mapper.getSpatialReference(geomType
                .getCoordinateReferenceSystem());

        // create the layer
        layer = OGR_DS_CreateLayer(dataSource, pointerToCString(schema.getTypeName()),
                spatialReference, ogrGeomType, optionsPointer);
        if (layer == null) {
            throw new DataSourceException("Could not create the OGR layer: "
                    + OGRUtils.getCString(CPLGetLastErrorMsg()));
        }
        return layer;
    }

    private Pointer openOrCreateDataSource(String[] options, Pointer dataSource,
            Pointer<Pointer<Byte>> optionsPointer) throws IOException, DataSourceException {
        try {
            dataSource = openOGRDataSource(true);
        } catch (IOException e) {
            if (ogrDriver != null) {
                Pointer driver = OGRGetDriverByName(pointerToCString(ogrDriver));
                dataSource = OGR_Dr_CreateDataSource(driver, pointerToCString(ogrSourceName),
                        optionsPointer);
                driver.release();

                if (dataSource == null)
                    throw new IOException("Could not create OGR data source with driver "
                            + ogrDriver + " and options " + options);
            } else {
                throw new DataSourceException("Driver not provided, and could not "
                        + "open data source neither");
            }
        }
        return dataSource;
    }

}
