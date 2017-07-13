/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Curve Segment Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Curve segment defines a homogeneous segment of a curve.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativeInterior <em>Num Derivative Interior</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtEnd <em>Num Derivatives At End</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtStart <em>Num Derivatives At Start</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractCurveSegmentType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractCurveSegmentType' kind='empty'"
 * @generated
 */
public interface AbstractCurveSegmentType extends EObject {
    /**
     * Returns the value of the '<em><b>Num Derivative Interior</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "numDerivativesInterior" specifies the type of continuity that is guaranteed interior to the curve. The default value of "0" means simple continuity, which is a mandatory minimum level of continuity. This level is referred to as "C 0 " in mathematical texts. A value of 1 means that the function and its first derivative are continuous at the appropriate end point: "C 1 " continuity. A value of "n" for any integer means the function and its first n derivatives are continuous: "C n " continuity.
     * NOTE: Use of these values is only appropriate when the basic curve definition is an underdetermined system. For example, line string segments cannot support continuity above C 0 , since there is no spare control parameter to adjust the incoming angle at the end points of the segment. Spline functions on the other hand often have extra degrees of freedom on end segments that allow them to adjust the values of the derivatives to support C 1 or higher continuity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Num Derivative Interior</em>' attribute.
     * @see #isSetNumDerivativeInterior()
     * @see #unsetNumDerivativeInterior()
     * @see #setNumDerivativeInterior(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractCurveSegmentType_NumDerivativeInterior()
     * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='numDerivativeInterior'"
     * @generated
     */
    BigInteger getNumDerivativeInterior();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativeInterior <em>Num Derivative Interior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Num Derivative Interior</em>' attribute.
     * @see #isSetNumDerivativeInterior()
     * @see #unsetNumDerivativeInterior()
     * @see #getNumDerivativeInterior()
     * @generated
     */
    void setNumDerivativeInterior(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativeInterior <em>Num Derivative Interior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetNumDerivativeInterior()
     * @see #getNumDerivativeInterior()
     * @see #setNumDerivativeInterior(BigInteger)
     * @generated
     */
    void unsetNumDerivativeInterior();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativeInterior <em>Num Derivative Interior</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Num Derivative Interior</em>' attribute is set.
     * @see #unsetNumDerivativeInterior()
     * @see #getNumDerivativeInterior()
     * @see #setNumDerivativeInterior(BigInteger)
     * @generated
     */
    boolean isSetNumDerivativeInterior();

    /**
     * Returns the value of the '<em><b>Num Derivatives At End</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "numDerivativesAtEnd" specifies the type of continuity between this curve segment and its successor. If this is the last curve segment in the curve, one of these values, as appropriate, is ignored. The default value of "0" means simple continuity, which is a mandatory minimum level of continuity. This level is referred to as "C 0 " in mathematical texts. A value of 1 means that the function and its first derivative are continuous at the appropriate end point: "C 1 " continuity. A value of "n" for any integer means the function and its first n derivatives are continuous: "C n " continuity.
     * NOTE: Use of these values is only appropriate when the basic curve definition is an underdetermined system. For example, line string segments cannot support continuity above C 0 , since there is no spare control parameter to adjust the incoming angle at the end points of the segment. Spline functions on the other hand often have extra degrees of freedom on end segments that allow them to adjust the values of the derivatives to support C 1 or higher continuity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Num Derivatives At End</em>' attribute.
     * @see #isSetNumDerivativesAtEnd()
     * @see #unsetNumDerivativesAtEnd()
     * @see #setNumDerivativesAtEnd(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractCurveSegmentType_NumDerivativesAtEnd()
     * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='numDerivativesAtEnd'"
     * @generated
     */
    BigInteger getNumDerivativesAtEnd();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtEnd <em>Num Derivatives At End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Num Derivatives At End</em>' attribute.
     * @see #isSetNumDerivativesAtEnd()
     * @see #unsetNumDerivativesAtEnd()
     * @see #getNumDerivativesAtEnd()
     * @generated
     */
    void setNumDerivativesAtEnd(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtEnd <em>Num Derivatives At End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetNumDerivativesAtEnd()
     * @see #getNumDerivativesAtEnd()
     * @see #setNumDerivativesAtEnd(BigInteger)
     * @generated
     */
    void unsetNumDerivativesAtEnd();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtEnd <em>Num Derivatives At End</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Num Derivatives At End</em>' attribute is set.
     * @see #unsetNumDerivativesAtEnd()
     * @see #getNumDerivativesAtEnd()
     * @see #setNumDerivativesAtEnd(BigInteger)
     * @generated
     */
    boolean isSetNumDerivativesAtEnd();

    /**
     * Returns the value of the '<em><b>Num Derivatives At Start</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "numDerivativesAtStart" specifies the type of continuity between this curve segment and its predecessor. If this is the first curve segment in the curve, one of these values, as appropriate, is ignored. The default value of "0" means simple continuity, which is a mandatory minimum level of continuity. This level is referred to as "C 0 " in mathematical texts. A value of 1 means that the function and its first derivative are continuous at the appropriate end point: "C 1 " continuity. A value of "n" for any integer means the function and its first n derivatives are continuous: "C n " continuity.
     * NOTE: Use of these values is only appropriate when the basic curve definition is an underdetermined system. For example, line string segments cannot support continuity above C 0 , since there is no spare control parameter to adjust the incoming angle at the end points of the segment. Spline functions on the other hand often have extra degrees of freedom on end segments that allow them to adjust the values of the derivatives to support C 1 or higher continuity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Num Derivatives At Start</em>' attribute.
     * @see #isSetNumDerivativesAtStart()
     * @see #unsetNumDerivativesAtStart()
     * @see #setNumDerivativesAtStart(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractCurveSegmentType_NumDerivativesAtStart()
     * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='numDerivativesAtStart'"
     * @generated
     */
    BigInteger getNumDerivativesAtStart();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtStart <em>Num Derivatives At Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Num Derivatives At Start</em>' attribute.
     * @see #isSetNumDerivativesAtStart()
     * @see #unsetNumDerivativesAtStart()
     * @see #getNumDerivativesAtStart()
     * @generated
     */
    void setNumDerivativesAtStart(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtStart <em>Num Derivatives At Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetNumDerivativesAtStart()
     * @see #getNumDerivativesAtStart()
     * @see #setNumDerivativesAtStart(BigInteger)
     * @generated
     */
    void unsetNumDerivativesAtStart();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.AbstractCurveSegmentType#getNumDerivativesAtStart <em>Num Derivatives At Start</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Num Derivatives At Start</em>' attribute is set.
     * @see #unsetNumDerivativesAtStart()
     * @see #getNumDerivativesAtStart()
     * @see #setNumDerivativesAtStart(BigInteger)
     * @generated
     */
    boolean isSetNumDerivativesAtStart();

} // AbstractCurveSegmentType
