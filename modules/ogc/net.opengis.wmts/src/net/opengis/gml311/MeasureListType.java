/**
 */
package net.opengis.gml311;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Measure List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of numbers with a uniform scale.  
 *       The value of uom (Units Of Measure) attribute is a reference to 
 *       a Reference System for the amount, either a ratio or position scale. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MeasureListType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.MeasureListType#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMeasureListType()
 * @model extendedMetaData="name='MeasureListType' kind='simple'"
 * @generated
 */
public interface MeasureListType extends EObject {
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
     * @see net.opengis.gml311.Gml311Package#getMeasureListType_Value()
     * @model dataType="net.opengis.gml311.DoubleList" many="false"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    List<Double> getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MeasureListType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(List<Double> value);

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
     * @see net.opengis.gml311.Gml311Package#getMeasureListType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MeasureListType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // MeasureListType
