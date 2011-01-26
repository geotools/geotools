/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Range Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The range of an interval. If the "min" or "max" element is not included, there is no value limit in that direction. Inclusion of the specified minimum and maximum values in the range shall be defined by the "closure". (The interval can be bounded or semi-bounded with different closures.) The data type and the semantic of the values are inherited by children and may be superceded by them. This range may be qualitative, i.e., nominal (age range) or qualitative (percentage) meaning that a value between min/max can be queried.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#getMin <em>Min</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#getMax <em>Max</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#isAtomic <em>Atomic</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#getClosure <em>Closure</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#getSemantic <em>Semantic</em>}</li>
 *   <li>{@link net.opengis.wcs10.ValueRangeType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType()
 * @model extendedMetaData="name='valueRangeType' kind='elementOnly'"
 * @generated
 */
public interface ValueRangeType extends EObject {
    /**
	 * Returns the value of the '<em><b>Min</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Minimum value of this numeric parameter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min</em>' containment reference.
	 * @see #setMin(TypedLiteralType)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Min()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='min' namespace='##targetNamespace'"
	 * @generated
	 */
    TypedLiteralType getMin();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#getMin <em>Min</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min</em>' containment reference.
	 * @see #getMin()
	 * @generated
	 */
    void setMin(TypedLiteralType value);

    /**
	 * Returns the value of the '<em><b>Max</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Maximum value of this numeric parameter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max</em>' containment reference.
	 * @see #setMax(TypedLiteralType)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Max()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='max' namespace='##targetNamespace'"
	 * @generated
	 */
    TypedLiteralType getMax();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#getMax <em>Max</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max</em>' containment reference.
	 * @see #getMax()
	 * @generated
	 */
    void setMax(TypedLiteralType value);

    /**
	 * Returns the value of the '<em><b>Atomic</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * What does this attribute mean? Is it useful and not redundant? When should this attribute be included or omitted? TBD.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Atomic</em>' attribute.
	 * @see #isSetAtomic()
	 * @see #unsetAtomic()
	 * @see #setAtomic(boolean)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Atomic()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='atomic'"
	 * @generated
	 */
    boolean isAtomic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#isAtomic <em>Atomic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Atomic</em>' attribute.
	 * @see #isSetAtomic()
	 * @see #unsetAtomic()
	 * @see #isAtomic()
	 * @generated
	 */
    void setAtomic(boolean value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.ValueRangeType#isAtomic <em>Atomic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetAtomic()
	 * @see #isAtomic()
	 * @see #setAtomic(boolean)
	 * @generated
	 */
    void unsetAtomic();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ValueRangeType#isAtomic <em>Atomic</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Atomic</em>' attribute is set.
	 * @see #unsetAtomic()
	 * @see #isAtomic()
	 * @see #setAtomic(boolean)
	 * @generated
	 */
    boolean isSetAtomic();

    /**
	 * Returns the value of the '<em><b>Closure</b></em>' attribute.
	 * The default value is <code>"closed"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.ClosureType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Shall be included unless the default value applies.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Closure</em>' attribute.
	 * @see net.opengis.wcs10.ClosureType
	 * @see #isSetClosure()
	 * @see #unsetClosure()
	 * @see #setClosure(ClosureType)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Closure()
	 * @model default="closed" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='closure' namespace='##targetNamespace'"
	 * @generated
	 */
    ClosureType getClosure();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#getClosure <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Closure</em>' attribute.
	 * @see net.opengis.wcs10.ClosureType
	 * @see #isSetClosure()
	 * @see #unsetClosure()
	 * @see #getClosure()
	 * @generated
	 */
    void setClosure(ClosureType value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.ValueRangeType#getClosure <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetClosure()
	 * @see #getClosure()
	 * @see #setClosure(ClosureType)
	 * @generated
	 */
    void unsetClosure();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ValueRangeType#getClosure <em>Closure</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Closure</em>' attribute is set.
	 * @see #unsetClosure()
	 * @see #getClosure()
	 * @see #setClosure(ClosureType)
	 * @generated
	 */
    boolean isSetClosure();

    /**
	 * Returns the value of the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Can be omitted when the semantics or meaning of values in this interval is clearly specified elsewhere, or the "semantic" attribute is included in an enclosing element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Semantic</em>' attribute.
	 * @see #setSemantic(String)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Semantic()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='semantic' namespace='##targetNamespace'"
	 * @generated
	 */
    String getSemantic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#getSemantic <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic</em>' attribute.
	 * @see #getSemantic()
	 * @generated
	 */
    void setSemantic(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Can be omitted when the datatype of values in this interval is string, or the "type" attribute is included in an enclosing element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see net.opengis.wcs10.Wcs10Package#getValueRangeType_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValueRangeType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

} // ValueRangeType
