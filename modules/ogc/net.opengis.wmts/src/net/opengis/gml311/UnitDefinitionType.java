/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unit Definition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a unit of measure (or uom). The definition includes a quantityType property, which indicates the phenomenon to which the units apply, and a catalogSymbol, which gives the short symbol used for this unit. This element is used when the relationship of this unit to other units or units systems is unknown.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.UnitDefinitionType#getQuantityType <em>Quantity Type</em>}</li>
 *   <li>{@link net.opengis.gml311.UnitDefinitionType#getCatalogSymbol <em>Catalog Symbol</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getUnitDefinitionType()
 * @model extendedMetaData="name='UnitDefinitionType' kind='elementOnly'"
 * @generated
 */
public interface UnitDefinitionType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Quantity Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Informal description of the phenomenon or type of quantity that is measured or observed. For example, "length", "angle", "time", "pressure", or "temperature". When the quantity is the result of an observation or measurement, this term is known as Observable Type or Measurand.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity Type</em>' containment reference.
     * @see #setQuantityType(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getUnitDefinitionType_QuantityType()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='quantityType' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getQuantityType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.UnitDefinitionType#getQuantityType <em>Quantity Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity Type</em>' containment reference.
     * @see #getQuantityType()
     * @generated
     */
    void setQuantityType(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Catalog Symbol</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * For global understanding of a unit of measure, it is often possible to reference an item in a catalog of units, using a symbol in that catalog. The "codeSpace" attribute in "CodeType" identifies a namespace for the catalog symbol value, and might reference the catalog. The "string" value in "CodeType" contains the value of a symbol that is unique within this catalog namespace. This symbol often appears explicitly in the catalog, but it could be a combination of symbols using a specified algebra of units. For example, the symbol "cm" might indicate that it is the "m" symbol combined with the "c" prefix.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Catalog Symbol</em>' containment reference.
     * @see #setCatalogSymbol(CodeType)
     * @see net.opengis.gml311.Gml311Package#getUnitDefinitionType_CatalogSymbol()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='catalogSymbol' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getCatalogSymbol();

    /**
     * Sets the value of the '{@link net.opengis.gml311.UnitDefinitionType#getCatalogSymbol <em>Catalog Symbol</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Catalog Symbol</em>' containment reference.
     * @see #getCatalogSymbol()
     * @generated
     */
    void setCatalogSymbol(CodeType value);

} // UnitDefinitionType
