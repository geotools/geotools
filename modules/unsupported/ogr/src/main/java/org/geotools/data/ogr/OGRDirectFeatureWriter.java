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

import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.OGRUtils.*;
import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;
import org.geotools.data.FeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * OGR feature writer leveraging OGR capabilities to rewrite a file using random access and in place
 * deletes
 * 
 * @author Andrea Aime - GeoSolutions
 */
class OGRDirectFeatureWriter implements FeatureWriter {

    private OGRFeatureReader reader;

    private SimpleFeatureType featureType;

    private SimpleFeature original;

    private SimpleFeature live;

    private Pointer layer;

    private FeatureMapper mapper;

    private boolean deletedFeatures;

    private Pointer layerDefinition;

    /**
     * Creates a new direct OGR feature writer
     * 
     * @param reader
     * @param featureType
     * @param layer
     */
    public OGRDirectFeatureWriter(OGRFeatureReader reader, SimpleFeatureType targetSchema,
            SimpleFeatureType originalSchema, GeometryFactory gf) {
        this.reader = reader;
        this.featureType = reader.getFeatureType();
        this.layer = reader.layer;
        this.layerDefinition = OGR_L_GetLayerDefn(layer);
        this.mapper = new FeatureMapper(targetSchema, originalSchema, gf);
        this.deletedFeatures = false;
    }

    public void close() throws IOException {
        if (reader != null) {
            original = null;
            live = null;
            Pointer<?> driver = OGR_DS_GetDriver(reader.dataSource);
            Pointer<Byte> driverName = OGR_Dr_GetName(driver);
            if ("ESRI Shapefile".equals(getCString(driverName)) && deletedFeatures) {
                String layerName =  getCString(OGR_L_GetName(reader.layer));
                OGR_DS_ExecuteSQL(reader.dataSource, pointerToCString("REPACK " + layerName), null, null);
            }
            OGR_L_SyncToDisk(reader.layer);
            reader.close();
        }
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public SimpleFeature next() throws IOException {
        if (live != null) {
            write();
        }

        if (reader.hasNext()) {
            original = reader.next();
            live = SimpleFeatureBuilder.copy(original);
        } else {
            original = null;
            live = SimpleFeatureBuilder.template(featureType, null);
        }

        return live;
    }

    public void remove() throws IOException {
        long ogrId = mapper.convertGTFID(original);
        if (OGR_L_DeleteFeature(layer, ogrId) != 0) {
            throw new IOException(getCString(CPLGetLastErrorMsg()));
        }
        deletedFeatures = true;
    }

    public void write() throws IOException {
        if (live == null)
            throw new IOException("No current feature to write");

        // this will return true only in update mode, otherwise original is null
        boolean changed = live.equals(original);
        if (!changed && original != null) {
            // nothing to do, just skip
        } else if(original != null) {
            // not equals, we're updating an existing one
            Pointer ogrFeature = mapper.convertGTFeature(layerDefinition, live);
            OGR_L_SetFeature(layer, ogrFeature);
        } else {
            Pointer ogrFeature = mapper.convertGTFeature(layerDefinition, live);
            OGR_L_CreateFeature(layer, ogrFeature);
        }

        // reset state
        live = null;
        original = null;
    }

}
