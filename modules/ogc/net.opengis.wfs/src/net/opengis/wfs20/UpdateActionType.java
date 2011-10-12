/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Update Action Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs20.Wfs20Package#getUpdateActionType()
 * @model extendedMetaData="name='UpdateActionType'"
 * @generated
 */
public enum UpdateActionType implements Enumerator {
    /**
     * The '<em><b>Replace</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REPLACE_VALUE
     * @generated
     * @ordered
     */
    REPLACE(0, "replace", "replace"),

    /**
     * The '<em><b>Insert Before</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INSERT_BEFORE_VALUE
     * @generated
     * @ordered
     */
    INSERT_BEFORE(1, "insertBefore", "insertBefore"),

    /**
     * The '<em><b>Insert After</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INSERT_AFTER_VALUE
     * @generated
     * @ordered
     */
    INSERT_AFTER(2, "insertAfter", "insertAfter"),

    /**
     * The '<em><b>Remove</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REMOVE_VALUE
     * @generated
     * @ordered
     */
    REMOVE(3, "remove", "remove");

    /**
     * The '<em><b>Replace</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Replace</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REPLACE
     * @model name="replace"
     * @generated
     * @ordered
     */
    public static final int REPLACE_VALUE = 0;

    /**
     * The '<em><b>Insert Before</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Insert Before</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INSERT_BEFORE
     * @model name="insertBefore"
     * @generated
     * @ordered
     */
    public static final int INSERT_BEFORE_VALUE = 1;

    /**
     * The '<em><b>Insert After</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Insert After</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INSERT_AFTER
     * @model name="insertAfter"
     * @generated
     * @ordered
     */
    public static final int INSERT_AFTER_VALUE = 2;

    /**
     * The '<em><b>Remove</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Remove</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REMOVE
     * @model name="remove"
     * @generated
     * @ordered
     */
    public static final int REMOVE_VALUE = 3;

    /**
     * An array of all the '<em><b>Update Action Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final UpdateActionType[] VALUES_ARRAY =
        new UpdateActionType[] {
            REPLACE,
            INSERT_BEFORE,
            INSERT_AFTER,
            REMOVE,
        };

    /**
     * A public read-only list of all the '<em><b>Update Action Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<UpdateActionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Update Action Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static UpdateActionType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            UpdateActionType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Update Action Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static UpdateActionType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            UpdateActionType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Update Action Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static UpdateActionType get(int value) {
        switch (value) {
            case REPLACE_VALUE: return REPLACE;
            case INSERT_BEFORE_VALUE: return INSERT_BEFORE;
            case INSERT_AFTER_VALUE: return INSERT_AFTER;
            case REMOVE_VALUE: return REMOVE;
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
    private UpdateActionType(int value, String name, String literal) {
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
    
} //UpdateActionType
