/**
 * 
 */
package org.geotools.data.ogr;

import static org.bridj.Pointer.pointerToCString;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGRGetDriverByName;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_DS_GetLayerByName;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_Dr_Open;

import org.bridj.Pointer;
import org.bridj.ValuedEnum;
import org.geotools.data.ogr.bridj.OgrLibrary;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbGeometryType;

/**
 * This example creates a file with the specified format and writes one feature.
 * 
 * Inspired in OGR API Tutorial http://www.gdal.org/ogr/ogr_apitut.html
 * 
 * To display the resultant file "ogrinfo -al fileName" (ex ogrinfo -al out.tab) 
 * 
 * @author Mauricio Pazos
 *
 */
public class TabFileWriter extends TabFileReader {

	static {
		GdalInit.init();

		OgrLibrary.OGRRegisterAll();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	    // FIXME to avoid errors old file should be deleted before creating a new one	
		// FIXME remove this comment 
		final String esriFormat = "ESRI Shapefile";
		// createFile_OGR_Dr_CreateDataSource SHP format: OK
		createFile_OGR_Dr_CreateDataSource(esriFormat, "/home/mauro/Downloads/gis-test-data/out.shp", "out");

		// createFile_OGR_Dr_Open: ??? (strategy used in OGRDataStore) does not work
//		createFile_OGR_Dr_Open(esriFormat, "/home/mauro/Downloads/gis-test-data/out.shp", "out");
//		appendFeatures(esriFormat, "/home/mauro/Downloads/gis-test-data/out.tab", "out");

		// observation: the name of layer (other) is not registered in the tab file, instead "out" is used as layer name (valid for shp and tab)
		// So layer name is the file name (without ext)
		// createFile_OGR_Dr_CreateDataSource TAB format: OK
//				
//		createFile_OGR_Dr_CreateDataSource("MapInfo File", "/home/mauro/Downloads/gis-test-data/out.tab", "other");
//		
		
		// test with SHP and TAB it doesn't work (cannot in update mode)				
//		appendFeatures("MapInfo File", "/home/mauro/Downloads/gis-test-data/out.tab", "other");
		
	}

	private static void appendFeatures(final String format, final String fileName, final String layerName) {
		
        Pointer driver = OgrLibrary.OGRGetDriverByName(pointerToCString(format));
        Pointer<?> pDataSource = OgrLibrary.OGR_Dr_Open(driver, pointerToCString(fileName), 1); // with 0-read work
       
        assert pDataSource != null;
        Pointer<?> pLayer = OgrLibrary.OGR_DS_GetLayerByName(pDataSource, pointerToCString(layerName));
        
        
		addFeature(pLayer, "valueAdded");
        
		OgrLibrary.OGR_DS_Destroy(pDataSource);
	}
	
	private static void createFile_OGR_Dr_Open(final String format, final String fileName, final String layerName) {

		Pointer<Byte> pDriverName = pointerToCString(format);
		Pointer<?> driver = OgrLibrary.OGRGetDriverByName(pDriverName);

		// opens the DataSource
		Pointer<Byte> pDSName = pointerToCString(fileName);
        Pointer<?> pDataSource = OgrLibrary.OGR_Dr_Open(driver, pointerToCString(fileName), 1); // with 0-read work
		
        Pointer<?> pLayer = null;
        if(pDataSource == null){
    		pDataSource = OgrLibrary.OGR_Dr_CreateDataSource(driver, pointerToCString(fileName), null);
    		pLayer =  createLayer(pDataSource, fileName);
    		
        } else {
        	// It work if the file exist
    		pLayer = OgrLibrary.OGR_DS_GetLayerByName(pDataSource, pointerToCString(layerName)); // 
        }
		
		// HACK reopens to insert features
//		OgrLibrary.OGR_DS_Destroy(pDataSource);
//        pDataSource = OgrLibrary.OGR_Dr_Open(driver, pointerToCString(fileName), 1); // with 0-read work
//		assert pDataSource != null;
//		pLayer = OgrLibrary.OGR_DS_GetLayerByName(pDataSource, pointerToCString(fileName));
		
		// creates a feature with a geometry and name field
		addFeature(pLayer, "value1");
		addFeature(pLayer, "value2");
		
		// finally destroy the data store
		
		OgrLibrary.OGR_DS_Destroy(pDataSource);
		
	}
	
	/**
	 * creates files using OGR_Dr_CreateDataSource
	 * 
	 * @param format
	 * @param fileName
	 * @param layerName
	 */
	private static void createFile_OGR_Dr_CreateDataSource(final String format, final String fileName, final String layerName) {

		Pointer<Byte> pDriverName = pointerToCString(format);
		Pointer<?> driver = OgrLibrary.OGRGetDriverByName(pDriverName);
		Pointer<?> pDataSource = OgrLibrary.OGR_Dr_CreateDataSource(driver, pointerToCString(fileName), null);

		// creates schema with one field
		Pointer<?>  pLayer =  createLayer(pDataSource, fileName);
//
//		Pointer<Byte> pFieldName = pointerToCString("Name");
//		Pointer<?> pField = OgrLibrary.OGR_Fld_Create(pFieldName, OGRFieldType.OFTString);
//		OgrLibrary.OGR_Fld_SetWidth(pField, 30);
//		
//		OgrLibrary.OGR_L_CreateField(pLayer, pField, 1);
//		
//		OgrLibrary.OGR_Fld_Destroy(pField);
		
		// creates a feature with a geometry and name field
		addFeature(pLayer, "value1");
		addFeature(pLayer, "value2");
		
		// finally destroy the data store
		
		OgrLibrary.OGR_DS_Destroy(pDataSource);
		
	}

	private static Pointer<?> createLayer( Pointer<?> pDataSource,  final String fileName){
		
		
		Pointer<?> pLayer = OgrLibrary.OGR_DS_CreateLayer(
								pDataSource, 
								pointerToCString(fileName), 
								null,// no funcion pointerToCString("WGS84") 
								OgrLibrary.OGRwkbGeometryType.wkbPoint , 
								null);
		
		// creates schema with one field
		
		
		Pointer<Byte> pFieldName = pointerToCString("Name");
		Pointer<?> pField = OgrLibrary.OGR_Fld_Create(pFieldName, OGRFieldType.OFTString);
		OgrLibrary.OGR_Fld_SetWidth(pField, 30);
		
		OgrLibrary.OGR_L_CreateField(pLayer, pField, 1);
		
		OgrLibrary.OGR_Fld_Destroy(pField);

		return pLayer;
	}

	private static void addFeature(Pointer<?> pLayer , String value){

		Pointer<?> layerDefn = OgrLibrary.OGR_L_GetLayerDefn(pLayer);
		
		// creates a feature with a geometry and name field
		Pointer<?> pFeature = OgrLibrary.OGR_F_Create(layerDefn);
		
		int iField = OgrLibrary.OGR_F_GetFieldIndex(pFeature, pointerToCString("Name"));
		Pointer<Byte> pStrValue = pointerToCString(value);
		OgrLibrary.OGR_F_SetFieldString(pFeature, iField, pStrValue);
		
		Pointer<?> pGeom = OgrLibrary.OGR_G_CreateGeometry(OGRwkbGeometryType.wkbPoint);
		OgrLibrary.OGR_G_SetPoint_2D(pGeom, 0, 43, 3);
		OgrLibrary.OGR_F_SetGeometry(pFeature, pGeom);
		OgrLibrary.OGR_G_DestroyGeometry(pGeom);

		// adds the feature to the layer
		OgrLibrary.OGR_L_CreateFeature(pLayer, pFeature);
		OgrLibrary.OGR_F_Destroy(pFeature);
	}
	
}
