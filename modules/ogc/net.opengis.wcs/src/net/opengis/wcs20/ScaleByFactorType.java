package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * Used in the WCS 2.0 scaling extension to specify the scale for a particular axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface ScaleByFactorType extends EObject {

    /**
     * The scale factor to be applied on all axis
     * 
     * @model
     */
    public double getScaleFactor();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScaleByFactorType#getScaleFactor <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale Factor</em>' attribute.
     * @see #getScaleFactor()
     * @generated
     */
    void setScaleFactor(double value);

}
