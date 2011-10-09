package org.geotools.data.ogr;

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.ogr.bridj.GdalInit;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

@SuppressWarnings("rawtypes")
/**
 * 
 *
 * @source $URL$
 */
public class OGRDataStore extends ContentDataStore {

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
                Pointer<Byte> name = OGR_L_GetName(layer);
                if (name != null) {
                    result.add(new NameImpl(getNamespaceURI(), name.getCString()));
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
        Pointer ds = OGROpen(pointerToCString(ogrSourceName), update ? 1 : 0, null);
        if (ds == null) {
            throw new IOException("OGR could not open '" + ogrSourceName + "'");
        }
        return ds;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new OGRFeatureSource(entry, Query.ALL);
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

            FeatureTypeMapper mapper = new FeatureTypeMapper();

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

            // create fields
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor())
                    continue;

                Pointer fieldDefinition = mapper.getOGRFieldDefinition(ad);
                OGR_L_CreateField(layer, fieldDefinition, approximateFields ? 1 : 0);
            }
        } finally {
            OGRUtils.releaseLayer(layer);
            OGRUtils.releaseDataSource(dataSource);
        }
    }

}
