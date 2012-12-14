package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Specifies a different interpolation for each axis  
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface InterpolationAxesType extends EObject {

    /**
     * The interpolation method (in case it is uniform along all axes)
     * 
     * @model
     */
    public EList<InterpolationAxisType> getInterpolationAxis();
    

}
