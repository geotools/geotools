/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Name Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.PropertyNameType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.PropertyNameType#getResolve <em>Resolve</em>}</li>
 *   <li>{@link net.opengis.wfs20.PropertyNameType#getResolveDepth <em>Resolve Depth</em>}</li>
 *   <li>{@link net.opengis.wfs20.PropertyNameType#getResolvePath <em>Resolve Path</em>}</li>
 *   <li>{@link net.opengis.wfs20.PropertyNameType#getResolveTimeout <em>Resolve Timeout</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType()
 * @model extendedMetaData="name='PropertyName_._type' kind='simple'"
 * @generated
 */
public interface PropertyNameType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(QName)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    QName getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyNameType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(QName value);

    /**
     * Returns the value of the '<em><b>Resolve</b></em>' attribute.
     * The default value is <code>"none"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.ResolveValueType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve</em>' attribute.
     * @see net.opengis.wfs20.ResolveValueType
     * @see #isSetResolve()
     * @see #unsetResolve()
     * @see #setResolve(ResolveValueType)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType_Resolve()
     * @model default="none" unsettable="true"
     *        extendedMetaData="kind='attribute' name='resolve'"
     * @generated
     */
    ResolveValueType getResolve();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolve <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve</em>' attribute.
     * @see net.opengis.wfs20.ResolveValueType
     * @see #isSetResolve()
     * @see #unsetResolve()
     * @see #getResolve()
     * @generated
     */
    void setResolve(ResolveValueType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolve <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolve()
     * @see #getResolve()
     * @see #setResolve(ResolveValueType)
     * @generated
     */
    void unsetResolve();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolve <em>Resolve</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve</em>' attribute is set.
     * @see #unsetResolve()
     * @see #getResolve()
     * @see #setResolve(ResolveValueType)
     * @generated
     */
    boolean isSetResolve();

    /**
     * Returns the value of the '<em><b>Resolve Depth</b></em>' attribute.
     * The default value is <code>"*"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve Depth</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve Depth</em>' attribute.
     * @see #isSetResolveDepth()
     * @see #unsetResolveDepth()
     * @see #setResolveDepth(Object)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType_ResolveDepth()
     * @model default="*" unsettable="true" dataType="net.opengis.wfs20.PositiveIntegerWithStar"
     *        extendedMetaData="kind='attribute' name='resolveDepth'"
     * @generated
     */
    Object getResolveDepth();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveDepth <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve Depth</em>' attribute.
     * @see #isSetResolveDepth()
     * @see #unsetResolveDepth()
     * @see #getResolveDepth()
     * @generated
     */
    void setResolveDepth(Object value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveDepth <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolveDepth()
     * @see #getResolveDepth()
     * @see #setResolveDepth(Object)
     * @generated
     */
    void unsetResolveDepth();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveDepth <em>Resolve Depth</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve Depth</em>' attribute is set.
     * @see #unsetResolveDepth()
     * @see #getResolveDepth()
     * @see #setResolveDepth(Object)
     * @generated
     */
    boolean isSetResolveDepth();

    /**
     * Returns the value of the '<em><b>Resolve Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve Path</em>' attribute.
     * @see #setResolvePath(String)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType_ResolvePath()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='resolvePath'"
     * @generated
     */
    String getResolvePath();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolvePath <em>Resolve Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve Path</em>' attribute.
     * @see #getResolvePath()
     * @generated
     */
    void setResolvePath(String value);

    /**
     * Returns the value of the '<em><b>Resolve Timeout</b></em>' attribute.
     * The default value is <code>"300"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve Timeout</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve Timeout</em>' attribute.
     * @see #isSetResolveTimeout()
     * @see #unsetResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getPropertyNameType_ResolveTimeout()
     * @model default="300" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='resolveTimeout'"
     * @generated
     */
    BigInteger getResolveTimeout();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveTimeout <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve Timeout</em>' attribute.
     * @see #isSetResolveTimeout()
     * @see #unsetResolveTimeout()
     * @see #getResolveTimeout()
     * @generated
     */
    void setResolveTimeout(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveTimeout <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolveTimeout()
     * @see #getResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @generated
     */
    void unsetResolveTimeout();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.PropertyNameType#getResolveTimeout <em>Resolve Timeout</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve Timeout</em>' attribute is set.
     * @see #unsetResolveTimeout()
     * @see #getResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @generated
     */
    boolean isSetResolveTimeout();

} // PropertyNameType
