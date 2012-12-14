package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * The root object for wcs 2.0 scaling 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface ScalingType extends EObject {

    /**
     * Scales by uniform factor
     * 
     * @model
     */
    public ScaleByFactorType getScaleByFactor();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleByFactor <em>Scale By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale By Factor</em>' reference.
     * @see #getScaleByFactor()
     * @generated
     */
    void setScaleByFactor(ScaleByFactorType value);

    /**
     * Scales each axis by a different value
     * 
     * @model
     */
    public ScaleAxisByFactorType getScaleAxesByFactor();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleAxesByFactor <em>Scale Axes By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale Axes By Factor</em>' reference.
     * @see #getScaleAxesByFactor()
     * @generated
     */
    void setScaleAxesByFactor(ScaleAxisByFactorType value);

    /**
     * Scales each axis to a specific size
     * 
     * @model
     */
    public ScaleToSizeType getScaleToSize();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleToSize <em>Scale To Size</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale To Size</em>' reference.
     * @see #getScaleToSize()
     * @generated
     */
    void setScaleToSize(ScaleToSizeType value);

    /**
     * Scales each axis to a specific extent
     * 
     * @model
     */
    public ScaleToExtentType getScaleToExtent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleToExtent <em>Scale To Extent</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale To Extent</em>' reference.
     * @see #getScaleToExtent()
     * @generated
     */
    void setScaleToExtent(ScaleToExtentType value);

}
