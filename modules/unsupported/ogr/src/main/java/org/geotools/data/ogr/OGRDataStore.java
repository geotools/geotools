package org.geotools.data.ogr;

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bridj.Pointer;
import org.geotools.data.Query;
import org.geotools.data.ogr.bridj.GdalInit;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

@SuppressWarnings("rawtypes")
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
        Pointer ds = OGROpenShared(pointerToCString(ogrSourceName), update ? 1 : 0, null);
        if (ds == null) {
            throw new IOException("OGR could not open '" + ogrSourceName + "'");
        }
        return ds;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new OGRFeatureSource(entry, Query.ALL);
    }

}
