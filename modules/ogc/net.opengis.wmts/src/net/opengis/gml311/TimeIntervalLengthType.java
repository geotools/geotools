/**
 */
package net.opengis.gml311;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Interval Length Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This type extends the built-in xsd:decimal simple type to allow floating-point 
 *       values for temporal length. According to  the ISO 11404 model you have to use 
 *       positiveInteger together with appropriate values for radix and factor. The 
 *       resolution of the time interval is to one radix ^(-factor) of the specified 
 *       time unit (e.g. unit="second", radix="10", factor="3" specifies a resolution 
 *       of milliseconds). It is a subtype of TimeDurationType.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeIntervalLengthType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeIntervalLengthType#getFactor <em>Factor</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeIntervalLengthType#getRadix <em>Radix</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeIntervalLengthType#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeIntervalLengthType()
 * @model extendedMetaData="name='TimeIntervalLengthType' kind='simple'"
 * @generated
 */
public interface TimeIntervalLengthType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getTimeIntervalLengthType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    BigDecimal getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeIntervalLengthType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Factor</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Factor</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Factor</em>' attribute.
     * @see #setFactor(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getTimeIntervalLengthType_Factor()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='factor'"
     * @generated
     */
    BigInteger getFactor();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeIntervalLengthType#getFactor <em>Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Factor</em>' attribute.
     * @see #getFactor()
     * @generated
     */
    void setFactor(BigInteger value);

    /**
     * Returns the value of the '<em><b>Radix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Radix</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Radix</em>' attribute.
     * @see #setRadix(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getTimeIntervalLengthType_Radix()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='radix'"
     * @generated
     */
    BigInteger getRadix();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeIntervalLengthType#getRadix <em>Radix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Radix</em>' attribute.
     * @see #getRadix()
     * @generated
     */
    void setRadix(BigInteger value);

    /**
     * Returns the value of the '<em><b>Unit</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Unit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unit</em>' attribute.
     * @see #setUnit(Object)
     * @see net.opengis.gml311.Gml311Package#getTimeIntervalLengthType_Unit()
     * @model dataType="net.opengis.gml311.TimeUnitType" required="true"
     *        extendedMetaData="kind='attribute' name='unit'"
     * @generated
     */
    Object getUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeIntervalLengthType#getUnit <em>Unit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unit</em>' attribute.
     * @see #getUnit()
     * @generated
     */
    void setUnit(Object value);

} // TimeIntervalLengthType
