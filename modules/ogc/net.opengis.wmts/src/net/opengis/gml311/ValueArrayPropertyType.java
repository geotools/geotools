/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * GML property which refers to, or contains, a set of homogeneously typed Values.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getBoolean <em>Boolean</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCategory <em>Category</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCategoryExtent <em>Category Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getQuantityExtent <em>Quantity Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCountExtent <em>Count Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCompositeValueGroup <em>Composite Value Group</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getCompositeValue <em>Composite Value</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getObjectGroup <em>Object Group</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getObject <em>Object</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayPropertyType#getNull <em>Null</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType()
 * @model extendedMetaData="name='ValueArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface ValueArrayPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Value()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='Value:0'"
     * @generated
     */
    FeatureMap getValue();

    /**
     * Returns the value of the '<em><b>Boolean</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Boolean}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A value from two-valued logic, using the XML Schema boolean type.  An instance may take the values {true, false, 1, 0}.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Boolean()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Boolean" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Boolean' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<Boolean> getBoolean();

    /**
     * Returns the value of the '<em><b>Category</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CodeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A term representing a classification.  It has an optional XML attribute codeSpace, whose value is a URI which identifies a dictionary, codelist or authority for the term.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Category()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Category' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<CodeType> getCategory();

    /**
     * Returns the value of the '<em><b>Quantity</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.MeasureType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A numeric value with a scale.  The content of the element is an amount using the XML Schema type double which permits decimal or scientific notation.  An XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which the numeric value must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Quantity()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Quantity' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<MeasureType> getQuantity();

    /**
     * Returns the value of the '<em><b>Count</b></em>' attribute list.
     * The list contents are of type {@link java.math.BigInteger}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An integer representing a frequency of occurrence.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Count()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Integer" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Count' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<BigInteger> getCount();

    /**
     * Returns the value of the '<em><b>Boolean List</b></em>' attribute list.
     * The list contents are of type {@link java.util.List}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML List based on XML Schema boolean type.  An element of this type contains a space-separated list of boolean values {0,1,true,false}
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean List</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_BooleanList()
     * @model unique="false" dataType="net.opengis.gml311.BooleanOrNullList" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BooleanList' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<List> getBooleanList();

    /**
     * Returns the value of the '<em><b>Category List</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CodeOrNullListType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of terms or nulls.  A single XML attribute codeSpace may be provided, which authorises all the terms in the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category List</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CategoryList()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CategoryList' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<CodeOrNullListType> getCategoryList();

    /**
     * Returns the value of the '<em><b>Quantity List</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.MeasureOrNullListType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space separated list of amounts or nulls.  The amounts use the XML Schema type double.  A single XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which all the amounts in the list must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity List</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_QuantityList()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='QuantityList' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<MeasureOrNullListType> getQuantityList();

    /**
     * Returns the value of the '<em><b>Count List</b></em>' attribute list.
     * The list contents are of type {@link java.util.List}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of integers or nulls.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count List</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CountList()
     * @model unique="false" dataType="net.opengis.gml311.IntegerOrNullList" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CountList' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<List> getCountList();

    /**
     * Returns the value of the '<em><b>Category Extent</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CategoryExtentType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of ordinal values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category Extent</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CategoryExtent()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CategoryExtent' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<CategoryExtentType> getCategoryExtent();

    /**
     * Returns the value of the '<em><b>Quantity Extent</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.QuantityExtentType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of numeric values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity Extent</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_QuantityExtent()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='QuantityExtent' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<QuantityExtentType> getQuantityExtent();

    /**
     * Returns the value of the '<em><b>Count Extent</b></em>' attribute list.
     * The list contents are of type {@link java.util.List}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of frequency values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count Extent</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CountExtent()
     * @model unique="false" dataType="net.opengis.gml311.CountExtentType" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CountExtent' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<List> getCountExtent();

    /**
     * Returns the value of the '<em><b>Composite Value Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Aggregate value built using the Composite pattern.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Composite Value Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CompositeValueGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='CompositeValue:group' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    FeatureMap getCompositeValueGroup();

    /**
     * Returns the value of the '<em><b>Composite Value</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CompositeValueType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Aggregate value built using the Composite pattern.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Composite Value</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_CompositeValue()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeValue' namespace='##targetNamespace' group='CompositeValue:group'"
     * @generated
     */
    EList<CompositeValueType> getCompositeValue();

    /**
     * Returns the value of the '<em><b>Object Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element is the head of a substitutionGroup hierararchy which may contain either simpleContent or complexContent elements.  It is used to assert the model position of "class" elements declared in other GML schemas.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Object Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_ObjectGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='_Object:group' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    FeatureMap getObjectGroup();

    /**
     * Returns the value of the '<em><b>Object</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element is the head of a substitutionGroup hierararchy which may contain either simpleContent or complexContent elements.  It is used to assert the model position of "class" elements declared in other GML schemas.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Object</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Object()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Object' namespace='##targetNamespace' group='_Object:group'"
     * @generated
     */
    EList<EObject> getObject();

    /**
     * Returns the value of the '<em><b>Null</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Null</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Null</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getValueArrayPropertyType_Null()
     * @model unique="false" dataType="net.opengis.gml311.NullType" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Null' namespace='##targetNamespace' group='#Value:0'"
     * @generated
     */
    EList<Object> getNull();

} // ValueArrayPropertyType
