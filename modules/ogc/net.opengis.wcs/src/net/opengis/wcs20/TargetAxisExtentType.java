package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * Used in the WCS 2.0 scaling extension to specify the scale for a particular axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface TargetAxisExtentType extends EObject {

    /**
     * The axis to be scaled
     * 
     * @model
     */
    public String getAxis();

  
    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getAxis <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' attribute.
     * @see #getAxis()
     * @generated
     */
    void setAxis(String value);


    /**
     * The min axis value
     * @model
     */
    public double getLow();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getLow <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Low</em>' attribute.
     * @see #getLow()
     * @generated
     */
    void setLow(double value);

    /**
     * The max axis value
     * @model
     */
    public double getHigh();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getHigh <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>High</em>' attribute.
     * @see #getHigh()
     * @generated
     */
    void setHigh(double value);
    

}
