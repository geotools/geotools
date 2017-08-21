/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Succession Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Feature succession is a semantic relationship derived from evaluation of observer, and 
 * 			Feature Substitution, Feature Division and Feature Fusion are defined as associations between 
 * 			previous features and next features in the temporal context. 
 * 			Successions shall be represented in either following two ways. 
 * 			* define a temporal topological complex element as a feature element 
 * 			* define an association same as temporal topological complex between features.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getSuccessionType()
 * @model extendedMetaData="name='SuccessionType'"
 * @generated
 */
public enum SuccessionType implements Enumerator {
    /**
     * The '<em><b>Substitution</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUBSTITUTION_VALUE
     * @generated
     * @ordered
     */
    SUBSTITUTION(0, "substitution", "substitution"),

    /**
     * The '<em><b>Division</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIVISION_VALUE
     * @generated
     * @ordered
     */
    DIVISION(1, "division", "division"),

    /**
     * The '<em><b>Fusion</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FUSION_VALUE
     * @generated
     * @ordered
     */
    FUSION(2, "fusion", "fusion"),

    /**
     * The '<em><b>Initiation</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INITIATION_VALUE
     * @generated
     * @ordered
     */
    INITIATION(3, "initiation", "initiation");

    /**
     * The '<em><b>Substitution</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Substitution</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SUBSTITUTION
     * @model name="substitution"
     * @generated
     * @ordered
     */
    public static final int SUBSTITUTION_VALUE = 0;

    /**
     * The '<em><b>Division</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Division</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DIVISION
     * @model name="division"
     * @generated
     * @ordered
     */
    public static final int DIVISION_VALUE = 1;

    /**
     * The '<em><b>Fusion</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Fusion</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #FUSION
     * @model name="fusion"
     * @generated
     * @ordered
     */
    public static final int FUSION_VALUE = 2;

    /**
     * The '<em><b>Initiation</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Initiation</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INITIATION
     * @model name="initiation"
     * @generated
     * @ordered
     */
    public static final int INITIATION_VALUE = 3;

    /**
     * An array of all the '<em><b>Succession Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SuccessionType[] VALUES_ARRAY =
        new SuccessionType[] {
            SUBSTITUTION,
            DIVISION,
            FUSION,
            INITIATION,
        };

    /**
     * A public read-only list of all the '<em><b>Succession Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SuccessionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Succession Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SuccessionType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SuccessionType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Succession Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SuccessionType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SuccessionType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Succession Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SuccessionType get(int value) {
        switch (value) {
            case SUBSTITUTION_VALUE: return SUBSTITUTION;
            case DIVISION_VALUE: return DIVISION;
            case FUSION_VALUE: return FUSION;
            case INITIATION_VALUE: return INITIATION;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private SuccessionType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //SuccessionType
