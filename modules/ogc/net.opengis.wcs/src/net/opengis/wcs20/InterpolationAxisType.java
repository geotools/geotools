package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * The interpolation to be used on a specific axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface InterpolationAxisType extends EObject {

    /**
     * The axis name
     * 
     * @model
     */
    public String getAxis();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.InterpolationAxisType#getAxis <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' attribute.
     * @see #getAxis()
     * @generated
     */
    void setAxis(String value);

    /**
     * The interpolation method to be used on the specified axis
     * 
     * @model
     */
    public String getInterpolationMethod();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.InterpolationAxisType#getInterpolationMethod <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Method</em>' attribute.
     * @see #getInterpolationMethod()
     * @generated
     */
    void setInterpolationMethod(String value);
    
    

}
