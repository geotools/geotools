package org.geotools.referencing.tools;

import java.util.Set;
import java.util.regex.Pattern;

import org.geotools.referencing.CRS;

/**
 * Does a full scan of the known SRS codes
 *
 * @source $URL$
 */
public class LoadStresser {
    static Pattern p = Pattern.compile("\\d+");
    
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Set<String> codes = CRS.getSupportedCodes("EPSG");
        for (String code : codes) {
            if(p.matcher(code).matches()) {
                try {
                    CRS.decode("EPSG:" + code);
                } catch(Exception e) {
                    System.out.println("EPSG:" + code + " failed: " + e.getMessage());
                }
            }
                
        }
        long end = System.currentTimeMillis();
        System.out.println("Codes: " + codes.size() + ", full scan " + (end - start) / 1000.0 + "s");
    }
}
