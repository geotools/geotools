package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * The interpolation method (the standard provides a list of values, but the XSD leaves it open
 * ended)
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface InterpolationMethodType extends EObject {

    /**
     * The interpolation method 
     * 
     * @model
     */
    public String getInterpolationMethod();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.InterpolationMethodType#getInterpolationMethod <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Method</em>' attribute.
     * @see #getInterpolationMethod()
     * @generated
     */
    void setInterpolationMethod(String value);

}
