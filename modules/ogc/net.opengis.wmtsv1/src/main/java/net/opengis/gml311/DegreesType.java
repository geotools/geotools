/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.Enumerator;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Degrees Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Integer number of degrees, plus the angle direction. This element can be used for geographic Latitude and Longitude. For Latitude, the XML attribute direction can take the values "N" or "S", meaning North or South of the equator. For Longitude, direction can take the values "E" or "W", meaning East or West of the prime meridian. This element can also be used for other angles. In that case, the direction can take the values "+" or "-" (of SignType), in the specified rotational direction from a specified reference direction.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DegreesType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DegreesType#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDegreesType()
 * @model extendedMetaData="name='DegreesType' kind='simple'"
 * @generated
 */
public interface DegreesType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDegreesType_Value()
     * @model dataType="net.opengis.gml311.DegreeValueType"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    BigInteger getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DegreesType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(BigInteger value);

    /**
     * Returns the value of the '<em><b>Direction</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Direction</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Direction</em>' attribute.
     * @see #setDirection(Enumerator)
     * @see net.opengis.gml311.Gml311Package#getDegreesType_Direction()
     * @model dataType="net.opengis.gml311.DirectionType"
     *        extendedMetaData="kind='attribute' name='direction'"
     * @generated
     */
    Enumerator getDirection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DegreesType#getDirection <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Direction</em>' attribute.
     * @see #getDirection()
     * @generated
     */
    void setDirection(Enumerator value);

} // DegreesType
