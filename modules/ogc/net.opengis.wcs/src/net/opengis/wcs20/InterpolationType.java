package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * The root object for wcs 2.0 interpolation extension 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface InterpolationType extends EObject {

    /**
     * The interpolation method (in case it is uniform along all axes)
     * 
     * @model
     */
    public InterpolationMethodType getInterpolationMethod();
    
    /**
	 * Sets the value of the '{@link net.opengis.wcs20.InterpolationType#getInterpolationMethod <em>Interpolation Method</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interpolation Method</em>' reference.
	 * @see #getInterpolationMethod()
	 * @generated
	 */
    void setInterpolationMethod(InterpolationMethodType value);

    /**
     * Interpolation method on a per axis basis
     * 
     * @model
     */
    public InterpolationAxesType getInterpolationAxes();

    /**
	 * Sets the value of the '{@link net.opengis.wcs20.InterpolationType#getInterpolationAxes <em>Interpolation Axes</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interpolation Axes</em>' reference.
	 * @see #getInterpolationAxes()
	 * @generated
	 */
    void setInterpolationAxes(InterpolationAxesType value);

   

}
