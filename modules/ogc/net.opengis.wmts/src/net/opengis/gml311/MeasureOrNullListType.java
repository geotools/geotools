/**
 */
package net.opengis.gml311;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measure Or Null List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of numbers with a uniform scale.  
 *       A member of the list may be a typed null. 
 *       The value of uom (Units Of Measure) attribute is a reference to 
 *       a Reference System for the amount, either a ratio or position scale. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MeasureOrNullListType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.MeasureOrNullListType#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMeasureOrNullListType()
 * @model extendedMetaData="name='MeasureOrNullListType' kind='simple'"
 * @generated
 */
public interface MeasureOrNullListType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(List)
     * @see net.opengis.gml311.Gml311Package#getMeasureOrNullListType_Value()
     * @model dataType="net.opengis.gml311.DoubleOrNullList" many="false"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    List<Object> getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MeasureOrNullListType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(List<Object> value);

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
     * @see net.opengis.gml311.Gml311Package#getMeasureOrNullListType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MeasureOrNullListType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // MeasureOrNullListType
