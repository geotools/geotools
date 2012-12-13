package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * The range subset from the WCS 2.0 range subsetting specification 
 *  
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface RangeSubsetType extends EObject {

    /**
     * The range items to be returned 
     * 
     * @model
     */
    public EList<RangeItemType> getRangeItems();

}
