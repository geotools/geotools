package org.geotools.data.sdmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;

public class Helper {

    //  public static String URL =
    // "http://stat.data.abs.gov.au/restsdmx/sdmx.ashx/GetDataStructure/ABS_SEIFA_LGA";
    public static String PROVIDER_OLD = "ABS";
    public static String PROVIDER_NEW = "ABS2";
    public static String URL = "http://stat.data.abs.gov.au/restsdmx/sdmx.ashx";
    public static String NAMESPACE = "http://aurin.org.au";
    public static String USER = "testuser";
    public static String PASSWORD = "testpassword";

    public static String T04 = "ABS_CENSUS2011_T04__SDMX";
    public static String T04_LGA = "ABS_CENSUS2011_T04__SDMX";
    public static String T04_DIMENSIONS = "ABS_CENSUS2011_T04__SDMX__DIMENSIONS";

    public static String T04SA = "ABS____ABS_C16_T04_SA____1_____0_____0__SDMX";
    public static String T04SA_DIMENSIONS =
            "ABS____ABS_C16_T04_SA____1_____0_____0__SDMX__DIMENSIONS";

    public static String SEIFA_LGA = "ABS_SEIFA_LGA__SDMX";
    public static String SEIFA_LGA_DIMENSIONS = "ABS_SEIFA_LGA__SDMX__DIMENSIONS";
    public static String ERP_LGA = "ABS_ANNUAL_ERP_LGA2016__SDMX";
    public static String ERP_LGA_DIMENSIONS = "ABS_ANNUAL_ERP_LGA2016__SDMX__DIMENSIONS";
    public static String DSD_NIS_ECO = "DSD_NIS_ECO__SDMX";
    public static String DSD_NIS_ECO_DIMENSIONS = "DSD_NIS_ECO__SDMX__DIMENSIONS";
    public static String KN_NIS = "KH_NIS____DF_TRADE____1_____1__SDMX";
    public static String KN_NIS_DIMENSIONS = "KH_NIS____DF_TRADE____1_____1__SDMX__DIMENSIONS";

    public Helper() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Helper method to read a XMl file into a stream
     *
     * @param fileName File name to load
     * @return XML content of the file
     * @throws FileNotFoundException
     */
    public static InputStream readXMLAsStream(String fileName) throws FileNotFoundException {
        return new FileInputStream(new File(Helper.class.getResource(fileName).getFile()));
    }

    // Create an alternative datastore pointing to the old SDMX ABS endpoint
    public static DataStore createSDMXTestDataStore() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(SDMXDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        params.put(SDMXDataStoreFactory.PROVIDER_PARAM.key, PROVIDER_OLD);
        params.put(SDMXDataStoreFactory.CONCURRENCY_PARAM.key, 1);
        params.put(SDMXDataStoreFactory.USER_PARAM.key, USER);
        params.put(SDMXDataStoreFactory.PASSWORD_PARAM.key, PASSWORD);
        return (SDMXDataStore) (new SDMXDataStoreFactory()).createDataStore(params);
    }

    // Create an alternative datastore pointing to the new SDMX ABS endpoint
    public static DataStore createSDMXTestDataStore2() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(SDMXDataStoreFactory.NAMESPACE_PARAM.key, NAMESPACE);
        params.put(SDMXDataStoreFactory.PROVIDER_PARAM.key, PROVIDER_NEW);
        params.put(SDMXDataStoreFactory.CONCURRENCY_PARAM.key, 1);
        params.put(SDMXDataStoreFactory.USER_PARAM.key, USER);
        params.put(SDMXDataStoreFactory.PASSWORD_PARAM.key, PASSWORD);
        return (SDMXDataStore) (new SDMXDataStoreFactory()).createDataStore(params);
    }
}
