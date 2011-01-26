/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
 * 
 *    thfParser library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    thfParser library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.edigeo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Logger;

public class EdigeoTHF {

    private Logger logger = Logger.getLogger("org.geotools.data.edigeo");
    
    // Edigeo extension file
    private static final String THFExtension = "thf";
    
    public File thfFile = null;
    // private EdigeoParser thfParser = null;

    private static final String nbVol = "LOCSN";
    private static final String lotName = "LONSA";
    private static final String genName = "GNNSA";
    private static final String geoName = "GONSA";
    private static final String dicName = "DINSA";
    private static final String scdName = "SCNSA";
    private static final String nbVec = "GDCSN";
    private static final String vectName = "GDNSA";
    
    /**
     * <p>
     * thfParser constructor opens an existing THF file
     * </p>
     *
     * @param path Full pathName of the thf file, can be specified without the
     *        .thf extension
     *
     * @throws IOException If the specified thf file could not be opened
     */
    public EdigeoTHF(String path) throws FileNotFoundException {
        super();
        thfFile = EdigeoFileFactory.setFile(path, THFExtension, true);
    }

    /**
     * 
     * @return HashMap<String, String> 
     */
    public HashMap<String, String> readTHFile() throws FileNotFoundException {

        // TODO : create structure for saving infos
        // idea : list defined with initial capacity given nb value , ordered
        // or a Map String, Map<String, String> with a numeric key index
        // at time only one lot is read 
        EdigeoParser thfParser = new EdigeoParser(thfFile);

        HashMap<String, String> thfValue = new HashMap<String, String>();
        String value = null;
        String lname = null;

        while (thfParser.readLine()) {

//            if (thfParser.line.contains(nbVol)) {
//                value = getValue(nbVol);
//                System.out.println("nb volm : "+value);
//            }

            if (thfParser.line.contains(lotName)) {
                lname = thfParser.getValue(lotName);
                continue;
            }

            if (thfParser.line.contains(genName)) {
                value = thfParser.getValue(genName);
                thfValue.put("genfname", lname + value);
                continue;
            }

            if (thfParser.line.contains(geoName)) {
                value = thfParser.getValue(geoName);
                thfValue.put("geofname", lname + value);
                continue;
            }

            if (thfParser.line.contains(dicName)) {
                value = thfParser.getValue(dicName);
                thfValue.put("dicfname", lname + value);
                continue;
            }

            if (thfParser.line.contains(scdName)) {
                value = thfParser.getValue(scdName);
                thfValue.put("scdfname", lname + value);
                continue;
            }

            if (thfParser.line.contains(nbVec)) {
                value = thfParser.getValue(nbVec);
                thfValue.put("nbvec", value);
                continue;
            }

            if (thfParser.line.contains(vectName)) {
                value = thfParser.getValue(vectName);
                thfValue.put("vecfname_"+value, lname + value);
                continue;
            }
        }
        thfParser.close();
        return thfValue;
    }
    
}
