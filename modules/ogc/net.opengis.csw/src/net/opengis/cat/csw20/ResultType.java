/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Result Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.cat.csw20.Csw20Package#getResultType()
 * @model extendedMetaData="name='ResultType'"
 * @generated
 */
public enum ResultType implements Enumerator {
    /**
     * The '<em><b>Results</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RESULTS_VALUE
     * @generated
     * @ordered
     */
    RESULTS(0, "results", "results"),

    /**
     * The '<em><b>Hits</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #HITS_VALUE
     * @generated
     * @ordered
     */
    HITS(1, "hits", "hits"),

    /**
     * The '<em><b>Validate</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #VALIDATE_VALUE
     * @generated
     * @ordered
     */
    VALIDATE(2, "validate", "validate");

    /**
     * The '<em><b>Results</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Include results in the response.
     * <!-- end-model-doc -->
     * @see #RESULTS
     * @model name="results"
     * @generated
     * @ordered
     */
    public static final int RESULTS_VALUE = 0;

    /**
     * The '<em><b>Hits</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Provide a result set summary, but no results.
     * <!-- end-model-doc -->
     * @see #HITS
     * @model name="hits"
     * @generated
     * @ordered
     */
    public static final int HITS_VALUE = 1;

    /**
     * The '<em><b>Validate</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Validate the request and return an Acknowledgement message if it
     * 	      is valid. Continue processing the request asynchronously.
     * <!-- end-model-doc -->
     * @see #VALIDATE
     * @model name="validate"
     * @generated
     * @ordered
     */
    public static final int VALIDATE_VALUE = 2;

    /**
     * An array of all the '<em><b>Result Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final ResultType[] VALUES_ARRAY =
        new ResultType[] {
            RESULTS,
            HITS,
            VALIDATE,
        };

    /**
     * A public read-only list of all the '<em><b>Result Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<ResultType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Result Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ResultType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ResultType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Result Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ResultType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ResultType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Result Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ResultType get(int value) {
        switch (value) {
            case RESULTS_VALUE: return RESULTS;
            case HITS_VALUE: return HITS;
            case VALIDATE_VALUE: return VALIDATE;
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
    private ResultType(int value, String name, String literal) {
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
    
} //ResultType
