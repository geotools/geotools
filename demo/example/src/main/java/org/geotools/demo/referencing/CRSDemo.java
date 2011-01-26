package org.geotools.demo.referencing;

import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.SI;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.DefiningConversion;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.Conversion;

/**
 * The CoordinateReferenceSystem data structure is the gateway to the geotools
 * referencing module.
 * 
 * @author Jody
 *
 * @source $URL$
 */
public class CRSDemo {

public void create_ProjectedCRS_from_DefaultGeogCRS(){
        
        System.out.println("Start: Create ProjectedCRS from DefaultGeographicCRS.\n");
        
        /* Properties of the Projected CRS */
        Map props = new HashMap();
        props.put(IdentifiedObject.NAME_KEY, "My arbitrary name"); // Mandatory
//        props.put(ReferenceSystem.VALID_AREA_KEY,e); // Optional
        
        
        /* Geographic CoordinateReferenceSystem */
        //TODO: this is hard coded below because the compiler doesn't work.
        CoordinateReferenceSystem geogCRS = 
            org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        
        
        /* Defining Conversion: Name, Parameters */
        final String dcName = "A Mercator";
        /* Parameters for the Mercator */
        DefaultMathTransformFactory mtf = new DefaultMathTransformFactory();
        ParameterValueGroup pvg = null;
        try {
            pvg = mtf.getDefaultParameters("Mercator_1SP");
        } catch (NoSuchIdentifierException nsiex){
            System.err.println("On DefaultPrameterGroup creation: "+ nsiex.getMessage());
        }
        //Start Test Output
//            ParameterDescriptorGroup dg = pvg.getDescriptor()
//            for (GeneralParameterDescriptor descriptor : dg.descriptors()) {
//                System.out.println(descriptor.getName().getCode());
//            }
        //End Test Output
        DefiningConversion dc = new DefiningConversion(dcName,pvg);
        //TODO: Added to make the compiler happy, could merge with above.
        Conversion c = (Conversion) dc;
        
        
        
        /* Coordinate System */
        Map map = new HashMap();
        CSFactory csFactory = ReferencingFactoryFinder.getCSFactory(null);
        CoordinateSystemAxis xAxis = null;
        CoordinateSystemAxis yAxis = null;
        CartesianCS worldCS = null;
        try {
            map.clear();
            map.put("name", "Cartesian X axis");
            xAxis = csFactory.createCoordinateSystemAxis(map, "X", AxisDirection.EAST, SI.METER);
            map.clear();
            map.put("name", "Cartesian Y axis");
            yAxis = csFactory.createCoordinateSystemAxis(map, "Y", AxisDirection.NORTH, SI.METER);
            map.clear();
            map.put("name", "Cartesian CS");
            worldCS = csFactory.createCartesianCS(map, xAxis, yAxis);
        } catch (FactoryException fex) {
            System.err.println("On cartesianCS creation: " + fex.getMessage());
        }
        
        /* Projected CRS */
        ReferencingFactoryContainer fg = ReferencingFactoryContainer.instance(null);
        try{
            CoordinateReferenceSystem projCRS = fg.createProjectedCRS(props,
                        org.geotools.referencing.crs.DefaultGeographicCRS.WGS84,
                                            c,
                                            worldCS);
//           //TODO: figure out why this breaks but above works.
//           projCRS = fg.createProjectedCRS(props,
//                   geogCRS,
//                   dc,
//                   worldCS);
        } catch (FactoryException fex) {
            System.err.println("On projectedCRS creation: " + fex.getMessage());
        }
//        System.out.println(projCRS.toWKT())
    }
}
