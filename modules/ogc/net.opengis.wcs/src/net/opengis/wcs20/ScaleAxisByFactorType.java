package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Used in the WCS 2.0 scaling extension to specify a different rescaling for each axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface ScaleAxisByFactorType extends EObject {

    /**
     * The scale factor to be applied on all axis
     * 
     * @model
     */
    public EList<ScaleAxisType> getScaleAxis();

}
