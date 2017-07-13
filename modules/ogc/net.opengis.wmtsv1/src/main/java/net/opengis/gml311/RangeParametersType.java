/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Metadata about the rangeSet.  Definition of record structure.   
 *       This is required if the rangeSet is encoded in a DataBlock.  
 *       We use a gml:_Value with empty values as a map of the composite value structure.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RangeParametersType#isBoolean <em>Boolean</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCategory <em>Category</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCategoryExtent <em>Category Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getQuantityExtent <em>Quantity Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCountExtent <em>Count Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCompositeValueGroup <em>Composite Value Group</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getCompositeValue <em>Composite Value</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeParametersType#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRangeParametersType()
 * @model extendedMetaData="name='RangeParametersType' kind='elementOnly'"
 * @generated
 */
public interface RangeParametersType extends EObject {
    /**
     * Returns the value of the '<em><b>Boolean</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A value from two-valued logic, using the XML Schema boolean type.  An instance may take the values {true, false, 1, 0}.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean</em>' attribute.
     * @see #isSetBoolean()
     * @see #unsetBoolean()
     * @see #setBoolean(boolean)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Boolean()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='element' name='Boolean' namespace='##targetNamespace'"
     * @generated
     */
    boolean isBoolean();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#isBoolean <em>Boolean</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean</em>' attribute.
     * @see #isSetBoolean()
     * @see #unsetBoolean()
     * @see #isBoolean()
     * @generated
     */
    void setBoolean(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.RangeParametersType#isBoolean <em>Boolean</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetBoolean()
     * @see #isBoolean()
     * @see #setBoolean(boolean)
     * @generated
     */
    void unsetBoolean();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.RangeParametersType#isBoolean <em>Boolean</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Boolean</em>' attribute is set.
     * @see #unsetBoolean()
     * @see #isBoolean()
     * @see #setBoolean(boolean)
     * @generated
     */
    boolean isSetBoolean();

    /**
     * Returns the value of the '<em><b>Category</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A term representing a classification.  It has an optional XML attribute codeSpace, whose value is a URI which identifies a dictionary, codelist or authority for the term.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category</em>' containment reference.
     * @see #setCategory(CodeType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Category()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Category' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getCategory();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCategory <em>Category</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category</em>' containment reference.
     * @see #getCategory()
     * @generated
     */
    void setCategory(CodeType value);

    /**
     * Returns the value of the '<em><b>Quantity</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A numeric value with a scale.  The content of the element is an amount using the XML Schema type double which permits decimal or scientific notation.  An XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which the numeric value must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity</em>' containment reference.
     * @see #setQuantity(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Quantity()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Quantity' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getQuantity();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getQuantity <em>Quantity</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity</em>' containment reference.
     * @see #getQuantity()
     * @generated
     */
    void setQuantity(MeasureType value);

    /**
     * Returns the value of the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An integer representing a frequency of occurrence.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count</em>' attribute.
     * @see #setCount(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Count()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='element' name='Count' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getCount();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCount <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count</em>' attribute.
     * @see #getCount()
     * @generated
     */
    void setCount(BigInteger value);

    /**
     * Returns the value of the '<em><b>Boolean List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML List based on XML Schema boolean type.  An element of this type contains a space-separated list of boolean values {0,1,true,false}
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean List</em>' attribute.
     * @see #setBooleanList(List)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_BooleanList()
     * @model dataType="net.opengis.gml311.BooleanOrNullList" many="false"
     *        extendedMetaData="kind='element' name='BooleanList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getBooleanList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getBooleanList <em>Boolean List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean List</em>' attribute.
     * @see #getBooleanList()
     * @generated
     */
    void setBooleanList(List<Object> value);

    /**
     * Returns the value of the '<em><b>Category List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of terms or nulls.  A single XML attribute codeSpace may be provided, which authorises all the terms in the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category List</em>' containment reference.
     * @see #setCategoryList(CodeOrNullListType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CategoryList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CategoryList' namespace='##targetNamespace'"
     * @generated
     */
    CodeOrNullListType getCategoryList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCategoryList <em>Category List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category List</em>' containment reference.
     * @see #getCategoryList()
     * @generated
     */
    void setCategoryList(CodeOrNullListType value);

    /**
     * Returns the value of the '<em><b>Quantity List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space separated list of amounts or nulls.  The amounts use the XML Schema type double.  A single XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which all the amounts in the list must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity List</em>' containment reference.
     * @see #setQuantityList(MeasureOrNullListType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_QuantityList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='QuantityList' namespace='##targetNamespace'"
     * @generated
     */
    MeasureOrNullListType getQuantityList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getQuantityList <em>Quantity List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity List</em>' containment reference.
     * @see #getQuantityList()
     * @generated
     */
    void setQuantityList(MeasureOrNullListType value);

    /**
     * Returns the value of the '<em><b>Count List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of integers or nulls.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count List</em>' attribute.
     * @see #setCountList(List)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CountList()
     * @model dataType="net.opengis.gml311.IntegerOrNullList" many="false"
     *        extendedMetaData="kind='element' name='CountList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getCountList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCountList <em>Count List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count List</em>' attribute.
     * @see #getCountList()
     * @generated
     */
    void setCountList(List<Object> value);

    /**
     * Returns the value of the '<em><b>Category Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of ordinal values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category Extent</em>' containment reference.
     * @see #setCategoryExtent(CategoryExtentType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CategoryExtent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CategoryExtent' namespace='##targetNamespace'"
     * @generated
     */
    CategoryExtentType getCategoryExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCategoryExtent <em>Category Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category Extent</em>' containment reference.
     * @see #getCategoryExtent()
     * @generated
     */
    void setCategoryExtent(CategoryExtentType value);

    /**
     * Returns the value of the '<em><b>Quantity Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of numeric values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity Extent</em>' containment reference.
     * @see #setQuantityExtent(QuantityExtentType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_QuantityExtent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='QuantityExtent' namespace='##targetNamespace'"
     * @generated
     */
    QuantityExtentType getQuantityExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getQuantityExtent <em>Quantity Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity Extent</em>' containment reference.
     * @see #getQuantityExtent()
     * @generated
     */
    void setQuantityExtent(QuantityExtentType value);

    /**
     * Returns the value of the '<em><b>Count Extent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of frequency values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count Extent</em>' attribute.
     * @see #setCountExtent(List)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CountExtent()
     * @model dataType="net.opengis.gml311.CountExtentType" many="false"
     *        extendedMetaData="kind='element' name='CountExtent' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getCountExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCountExtent <em>Count Extent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count Extent</em>' attribute.
     * @see #getCountExtent()
     * @generated
     */
    void setCountExtent(List<Object> value);

    /**
     * Returns the value of the '<em><b>Composite Value Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Aggregate value built using the Composite pattern.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Composite Value Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CompositeValueGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='CompositeValue:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getCompositeValueGroup();

    /**
     * Returns the value of the '<em><b>Composite Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Aggregate value built using the Composite pattern.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Composite Value</em>' containment reference.
     * @see #setCompositeValue(CompositeValueType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_CompositeValue()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeValue' namespace='##targetNamespace' group='CompositeValue:group'"
     * @generated
     */
    CompositeValueType getCompositeValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getCompositeValue <em>Composite Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Composite Value</em>' containment reference.
     * @see #getCompositeValue()
     * @generated
     */
    void setCompositeValue(CompositeValueType value);

    /**
     * Returns the value of the '<em><b>Actuate</b></em>' attribute.
     * The literals are from the enumeration {@link org.w3.xlink.ActuateType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Actuate</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Actuate</em>' attribute.
     * @see org.w3.xlink.ActuateType
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #setActuate(ActuateType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Actuate()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='actuate' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ActuateType getActuate();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Actuate</em>' attribute.
     * @see org.w3.xlink.ActuateType
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #getActuate()
     * @generated
     */
    void setActuate(ActuateType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.RangeParametersType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetActuate()
     * @see #getActuate()
     * @see #setActuate(ActuateType)
     * @generated
     */
    void unsetActuate();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.RangeParametersType#getActuate <em>Actuate</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Actuate</em>' attribute is set.
     * @see #unsetActuate()
     * @see #getActuate()
     * @see #setActuate(ActuateType)
     * @generated
     */
    boolean isSetActuate();

    /**
     * Returns the value of the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arcrole</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arcrole</em>' attribute.
     * @see #setArcrole(String)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Arcrole()
     * @model dataType="org.w3.xlink.ArcroleType"
     *        extendedMetaData="kind='attribute' name='arcrole' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getArcrole();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getArcrole <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arcrole</em>' attribute.
     * @see #getArcrole()
     * @generated
     */
    void setArcrole(String value);

    /**
     * Returns the value of the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Href</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Href()
     * @model dataType="org.w3.xlink.HrefType"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

    /**
     * Returns the value of the '<em><b>Remote Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to an XML Schema fragment that specifies the content model of the propertys value. This is in conformance with the XML Schema Section 4.14 Referencing Schemas from Elsewhere.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remote Schema</em>' attribute.
     * @see #setRemoteSchema(String)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_RemoteSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='remoteSchema' namespace='##targetNamespace'"
     * @generated
     */
    String getRemoteSchema();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getRemoteSchema <em>Remote Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remote Schema</em>' attribute.
     * @see #getRemoteSchema()
     * @generated
     */
    void setRemoteSchema(String value);

    /**
     * Returns the value of the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Role</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Role</em>' attribute.
     * @see #setRole(String)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Role()
     * @model dataType="org.w3.xlink.RoleType"
     *        extendedMetaData="kind='attribute' name='role' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getRole();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getRole <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' attribute.
     * @see #getRole()
     * @generated
     */
    void setRole(String value);

    /**
     * Returns the value of the '<em><b>Show</b></em>' attribute.
     * The literals are from the enumeration {@link org.w3.xlink.ShowType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Show</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Show</em>' attribute.
     * @see org.w3.xlink.ShowType
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #setShow(ShowType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Show()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='show' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ShowType getShow();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Show</em>' attribute.
     * @see org.w3.xlink.ShowType
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #getShow()
     * @generated
     */
    void setShow(ShowType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.RangeParametersType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetShow()
     * @see #getShow()
     * @see #setShow(ShowType)
     * @generated
     */
    void unsetShow();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.RangeParametersType#getShow <em>Show</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Show</em>' attribute is set.
     * @see #unsetShow()
     * @see #getShow()
     * @see #setShow(ShowType)
     * @generated
     */
    boolean isSetShow();

    /**
     * Returns the value of the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' attribute.
     * @see #setTitle(String)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Title()
     * @model dataType="org.w3.xlink.TitleAttrType"
     *        extendedMetaData="kind='attribute' name='title' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getTitle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getTitle <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' attribute.
     * @see #getTitle()
     * @generated
     */
    void setTitle(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>"simple"</code>.
     * The literals are from the enumeration {@link org.w3.xlink.TypeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.w3.xlink.TypeType
     * @see #isSetType()
     * @see #unsetType()
     * @see #setType(TypeType)
     * @see net.opengis.gml311.Gml311Package#getRangeParametersType_Type()
     * @model default="simple" unsettable="true"
     *        extendedMetaData="kind='attribute' name='type' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    TypeType getType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeParametersType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see org.w3.xlink.TypeType
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(TypeType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.RangeParametersType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.RangeParametersType#getType <em>Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    boolean isSetType();

} // RangeParametersType
