package net.opengis.wcs20;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * A range item, either a single value or an interval 
 *  
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface RangeItemType extends EObject {

    /**
     * The single band chosen to to be returned
     * 
     * @model
     */
    public String getRangeComponent();
    
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeItemType#getRangeComponent <em>Range Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Component</em>' attribute.
     * @see #getRangeComponent()
     * @generated
     */
    void setRangeComponent(String value);


    /**
     * The band range chosen to to be returned
     * 
     * @model
     */
    public RangeIntervalType getRangeInterval();


    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeItemType#getRangeInterval <em>Range Interval</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Interval</em>' reference.
     * @see #getRangeInterval()
     * @generated
     */
    void setRangeInterval(RangeIntervalType value);
    

}
