package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * Used in the WCS 2.0 scaling extension to specify the scale for a particular axis
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface TargetAxisSizeType extends EObject {

    /**
     * The axis to be scaled
     * 
     * @model
     */
    public String getAxis();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisSizeType#getAxis <em>Axis</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' attribute.
     * @see #getAxis()
     * @generated
     */
    void setAxis(String value);

    /**
     * The target axis size
     * 
     * @model
     */
    public double getTargetSize();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisSizeType#getTargetSize <em>Target Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Size</em>' attribute.
     * @see #getTargetSize()
     * @generated
     */
    void setTargetSize(double value);

}
