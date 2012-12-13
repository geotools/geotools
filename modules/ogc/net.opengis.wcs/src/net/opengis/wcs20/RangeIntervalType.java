package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * A range interval, selecting bands by providing low and high extremas
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface RangeIntervalType extends EObject {

    /**
     * The single band chosen to to be returned
     * 
     * @model
     */
    public String getStartComponent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeIntervalType#getStartComponent <em>Start Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Component</em>' attribute.
     * @see #getStartComponent()
     * @generated
     */
    void setStartComponent(String value);

    /**
     * The band range chosen to to be returned
     * 
     * @model
     */
    public String getEndComponent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeIntervalType#getEndComponent <em>End Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Component</em>' attribute.
     * @see #getEndComponent()
     * @generated
     */
    void setEndComponent(String value);

}
