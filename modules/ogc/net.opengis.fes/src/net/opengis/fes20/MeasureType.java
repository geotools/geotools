/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measure Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.MeasureType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.fes20.MeasureType#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getMeasureType()
 * @model extendedMetaData="name='MeasureType' kind='simple'"
 * @generated
 */
public interface MeasureType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(double)
     * @see net.opengis.fes20.Fes20Package#getMeasureType_Value()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    double getValue();

    /**
     * Sets the value of the '{@link net.opengis.fes20.MeasureType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #getValue()
     * @generated
     */
    void setValue(double value);

    /**
     * Unsets the value of the '{@link net.opengis.fes20.MeasureType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(double)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link net.opengis.fes20.MeasureType#getValue <em>Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Value</em>' attribute is set.
     * @see #unsetValue()
     * @see #getValue()
     * @see #setValue(double)
     * @generated
     */
    boolean isSetValue();

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uom</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.fes20.Fes20Package#getMeasureType_Uom()
     * @model dataType="net.opengis.fes20.UomIdentifier" required="true"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.fes20.MeasureType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // MeasureType
