/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knot Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A knot is a breakpoint on a piecewise spline curve.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.KnotType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.KnotType#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link net.opengis.gml311.KnotType#getWeight <em>Weight</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getKnotType()
 * @model extendedMetaData="name='KnotType' kind='elementOnly'"
 * @generated
 */
public interface KnotType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The property "value" is the value of the parameter at the knot of the spline. The sequence of knots shall be a non-decreasing sequence. That is, each knot's value in the sequence shall be equal to or greater than the previous knot's value. The use of equal consecutive knots is normally handled using the multiplicity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(double)
     * @see net.opengis.gml311.Gml311Package#getKnotType_Value()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace'"
     * @generated
     */
    double getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.KnotType#getValue <em>Value</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.gml311.KnotType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(double)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.KnotType#getValue <em>Value</em>}' attribute is set.
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
     * Returns the value of the '<em><b>Multiplicity</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The property "multiplicity" is the multiplicity of this knot used in the definition of the spline (with the same weight).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multiplicity</em>' attribute.
     * @see #setMultiplicity(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getKnotType_Multiplicity()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='element' name='multiplicity' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMultiplicity();

    /**
     * Sets the value of the '{@link net.opengis.gml311.KnotType#getMultiplicity <em>Multiplicity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multiplicity</em>' attribute.
     * @see #getMultiplicity()
     * @generated
     */
    void setMultiplicity(BigInteger value);

    /**
     * Returns the value of the '<em><b>Weight</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The property "weight" is the value of the averaging weight used for this knot of the spline.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Weight</em>' attribute.
     * @see #isSetWeight()
     * @see #unsetWeight()
     * @see #setWeight(double)
     * @see net.opengis.gml311.Gml311Package#getKnotType_Weight()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='weight' namespace='##targetNamespace'"
     * @generated
     */
    double getWeight();

    /**
     * Sets the value of the '{@link net.opengis.gml311.KnotType#getWeight <em>Weight</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Weight</em>' attribute.
     * @see #isSetWeight()
     * @see #unsetWeight()
     * @see #getWeight()
     * @generated
     */
    void setWeight(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.KnotType#getWeight <em>Weight</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetWeight()
     * @see #getWeight()
     * @see #setWeight(double)
     * @generated
     */
    void unsetWeight();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.KnotType#getWeight <em>Weight</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Weight</em>' attribute is set.
     * @see #unsetWeight()
     * @see #getWeight()
     * @see #setWeight(double)
     * @generated
     */
    boolean isSetWeight();

} // KnotType
