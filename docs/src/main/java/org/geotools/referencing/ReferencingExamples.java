package org.geotools.referencing;

import java.util.Iterator;

import org.opengis.metadata.Identifier;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.util.GenericName;

/**
 * The following examples are taken from CTSTutorial provided by Rueben Schulz
 * 
 * @author Jody Garnett
 */
public class ReferencingExamples {

ReferencingExamples() {
    try {
        creatCRSFromWKT();
        //createFromEPSGCode();
        //createCRSByHand1();
        //createCRSByHand2();
        //createCRSByHand3();
        //createMathTransformBetweenCRSs();
        //transformUsingCRSUtility();
        //createAndUseMathTransform();
        //hintExample();
        // createTransformFromAuthorityCode();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
 * An example of creating a CRS from a WKT string. Additional examples of WKT strings can be found in
 * http://svn.geotools.org/geotools/trunk/gt/module/referencing/test/org/geotools/referencing
 * /test-data/
 * 
 * @throws Exception
 */
void creatCRSFromWKT() throws Exception {
    System.out.println("------------------------------------------");
    System.out.println("Creating a CRS from a WKT string:");
    // START SNIPPET: crsFromWKT
    CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
    String wkt = "PROJCS[\"UTM_Zone_10N\", " + "GEOGCS[\"WGS84\", " + "DATUM[\"WGS84\", "
            + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], " + "PRIMEM[\"Greenwich\", 0.0], "
            + "UNIT[\"degree\",0.017453292519943295], " + "AXIS[\"Longitude\",EAST], "
            + "AXIS[\"Latitude\",NORTH]], " + "PROJECTION[\"Transverse_Mercator\"], "
            + "PARAMETER[\"semi_major\", 6378137.0], "
            + "PARAMETER[\"semi_minor\", 6356752.314245179], "
            + "PARAMETER[\"central_meridian\", -123.0], "
            + "PARAMETER[\"latitude_of_origin\", 0.0], " + "PARAMETER[\"scale_factor\", 0.9996], "
            + "PARAMETER[\"false_easting\", 500000.0], " + "PARAMETER[\"false_northing\", 0.0], "
            + "UNIT[\"metre\",1.0], " + "AXIS[\"x\",EAST], " + "AXIS[\"y\",NORTH]]";
    
    CoordinateReferenceSystem crs = crsFactory.createFromWKT(wkt);
    // END SNIPPET: crsFromWKT
    System.out.println("  CRS: " + crs.toWKT());
    System.out.println("Identified CRS object:");
    printIdentifierStuff(crs);
    System.out.println("Identified Datum object:");
    printIdentifierStuff(((ProjectedCRS) crs).getDatum());
    System.out.println("------------------------------------------");
}

/**
 * Print out information about an identified object
 */
// START SNIPPET: identifiedObject
void printIdentifierStuff(IdentifiedObject identObj) {
    System.out.println("  getName().getCode() - " + identObj.getName().getCode());
    System.out.println("  getName().getAuthority() - " + identObj.getName().getAuthority());
    System.out.println("  getRemarks() - " + identObj.getRemarks());
    System.out.println("  getAliases():");
    Iterator<GenericName> aliases = identObj.getAlias().iterator();
    if (!aliases.hasNext()) {
        System.out.println("    no aliases");
    } else {
        for (int i = 0; aliases.hasNext(); i++) {
            System.out.println("    alias(" + i + "): " + (GenericName) aliases.next());
        }
    }

    System.out.println("  getIdentifiers():");
    // Identifier[]
    Iterator<? extends Identifier> idents = identObj.getIdentifiers().iterator();
    if (!idents.hasNext()) {
        System.out.println("    no extra identifiers");
    } else {
        for (int i = 0; idents.hasNext(); i++) {
            Identifier ident = idents.next();
            System.out.println("    identifier(" + i + ").getCode() - " + ident.getCode());
            System.out.println("    identifier(" + i + ").getAuthority() - "
                    + ident.getAuthority());
        }
    }
}

// END SNIPPET: identifiedObject

public static void main(String[] args) {
    new ReferencingExamples();
}
}
