package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import org.bridj.BridJ;
import org.bridj.Pointer;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;

public class DumpShapefile {
    

    public static void main(String[] args) {
        GdalInit.init();
        OGRRegisterAll();
        for (int i = 0; i < OGRGetDriverCount(); i++) {
            Pointer driver = OGRGetDriver(i);
            System.out.println(OGR_Dr_GetName(driver).getCString());
        }
        
        String path = "/home/aaime/devel/gisData/world.shp";
        Pointer ds = OGROpen(Pointer.pointerToCString(path), 0, null);
        System.out.println(ds);
        Pointer layer = OGR_DS_GetLayerByName(ds, Pointer.pointerToCString("world"));
        
        Pointer hFDefn = OGR_L_GetLayerDefn(layer);
        int iField;
        
        Pointer hFeature;

        OGR_L_ResetReading(layer);
        while( (hFeature = OGR_L_GetNextFeature(layer)) != null)
        {
            for( iField = 0; iField < OGR_FD_GetFieldCount(hFDefn); iField++ )
            {
                Pointer hFieldDefn = OGR_FD_GetFieldDefn( hFDefn, iField );
    
                if( OGR_Fld_GetType(hFieldDefn) == OGRFieldType.OFTInteger )
                    System.out.println(OGR_F_GetFieldAsInteger( hFeature, iField ) );
                else if( OGR_Fld_GetType(hFieldDefn) == OGRFieldType.OFTReal )
                    System.out.println(OGR_F_GetFieldAsDouble( hFeature, iField) );
                else
                    System.out.println(OGR_F_GetFieldAsString( hFeature, iField).getCString() );
            }
            OGR_F_Destroy(hFeature);
        }
        // OGR_FD_Release(hFDefn);
        OGR_L_Dereference(layer);
        OGR_DS_Destroy(ds);
    }
}