/**
 */
package net.opengis.gml311;

import java.math.BigDecimal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Clothoid Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A clothoid, or Cornu's spiral, is plane
 *    curve whose curvature is a fixed function of its length.
 *    In suitably chosen co-ordinates it is given by Fresnel's
 *    integrals.
 * 
 *     x(t) = 0-integral-t cos(AT*T/2)dT    
 *     
 *     y(t) = 0-integral-t sin(AT*T/2)dT
 *    
 *    This geometry is mainly used as a transition curve between
 *    curves of type straight line to circular arc or circular arc
 *    to circular arc. With this curve type it is possible to 
 *    achieve a C2-continous transition between the above mentioned
 *    curve types. One formula for the Clothoid is A*A = R*t where
 *    A is constant, R is the varying radius of curvature along the
 *    the curve and t is the length along and given in the Fresnel 
 *    integrals.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ClothoidType#getRefLocation <em>Ref Location</em>}</li>
 *   <li>{@link net.opengis.gml311.ClothoidType#getScaleFactor <em>Scale Factor</em>}</li>
 *   <li>{@link net.opengis.gml311.ClothoidType#getStartParameter <em>Start Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.ClothoidType#getEndParameter <em>End Parameter</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getClothoidType()
 * @model extendedMetaData="name='ClothoidType' kind='elementOnly'"
 * @generated
 */
public interface ClothoidType extends AbstractCurveSegmentType {
    /**
     * Returns the value of the '<em><b>Ref Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ref Location</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ref Location</em>' containment reference.
     * @see #setRefLocation(RefLocationType)
     * @see net.opengis.gml311.Gml311Package#getClothoidType_RefLocation()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='refLocation' namespace='##targetNamespace'"
     * @generated
     */
    RefLocationType getRefLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ClothoidType#getRefLocation <em>Ref Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ref Location</em>' containment reference.
     * @see #getRefLocation()
     * @generated
     */
    void setRefLocation(RefLocationType value);

    /**
     * Returns the value of the '<em><b>Scale Factor</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The element gives the value for the
     *        constant in the Fresnel's integrals.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scale Factor</em>' attribute.
     * @see #setScaleFactor(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getClothoidType_ScaleFactor()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal" required="true"
     *        extendedMetaData="kind='element' name='scaleFactor' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getScaleFactor();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ClothoidType#getScaleFactor <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale Factor</em>' attribute.
     * @see #getScaleFactor()
     * @generated
     */
    void setScaleFactor(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Start Parameter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The startParameter is the arc length
     *        distance from the inflection point that will be the start
     *        point for this curve segment. This shall be lower limit
     *        used in the Fresnel integral and is the value of the
     *        constructive parameter of this curve segment at its start
     *        point. The startParameter can either be positive or
     *        negative. 
     *        NOTE! If 0.0 (zero), lies between the startParameter and
     *        the endParameter of the clothoid, then the curve goes
     *        through the clothoid's inflection point, and the direction
     *        of its radius of curvature, given by the second
     *        derivative vector, changes sides with respect to the
     *        tangent vector. The term length distance for the
     * <!-- end-model-doc -->
     * @return the value of the '<em>Start Parameter</em>' attribute.
     * @see #isSetStartParameter()
     * @see #unsetStartParameter()
     * @see #setStartParameter(double)
     * @see net.opengis.gml311.Gml311Package#getClothoidType_StartParameter()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='startParameter' namespace='##targetNamespace'"
     * @generated
     */
    double getStartParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ClothoidType#getStartParameter <em>Start Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Parameter</em>' attribute.
     * @see #isSetStartParameter()
     * @see #unsetStartParameter()
     * @see #getStartParameter()
     * @generated
     */
    void setStartParameter(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ClothoidType#getStartParameter <em>Start Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStartParameter()
     * @see #getStartParameter()
     * @see #setStartParameter(double)
     * @generated
     */
    void unsetStartParameter();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ClothoidType#getStartParameter <em>Start Parameter</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Start Parameter</em>' attribute is set.
     * @see #unsetStartParameter()
     * @see #getStartParameter()
     * @see #setStartParameter(double)
     * @generated
     */
    boolean isSetStartParameter();

    /**
     * Returns the value of the '<em><b>End Parameter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The endParameter is the arc length
     *        distance from the inflection point that will be the end
     *        point for this curve segment. This shall be upper limit
     *        used in the Fresnel integral and is the value of the
     *        constructive parameter of this curve segment at its
     *        start point. The startParameter can either be positive
     *        or negative.
     * <!-- end-model-doc -->
     * @return the value of the '<em>End Parameter</em>' attribute.
     * @see #isSetEndParameter()
     * @see #unsetEndParameter()
     * @see #setEndParameter(double)
     * @see net.opengis.gml311.Gml311Package#getClothoidType_EndParameter()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='endParameter' namespace='##targetNamespace'"
     * @generated
     */
    double getEndParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ClothoidType#getEndParameter <em>End Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Parameter</em>' attribute.
     * @see #isSetEndParameter()
     * @see #unsetEndParameter()
     * @see #getEndParameter()
     * @generated
     */
    void setEndParameter(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ClothoidType#getEndParameter <em>End Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetEndParameter()
     * @see #getEndParameter()
     * @see #setEndParameter(double)
     * @generated
     */
    void unsetEndParameter();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ClothoidType#getEndParameter <em>End Parameter</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>End Parameter</em>' attribute is set.
     * @see #unsetEndParameter()
     * @see #getEndParameter()
     * @see #setEndParameter(double)
     * @generated
     */
    boolean isSetEndParameter();

} // ClothoidType
