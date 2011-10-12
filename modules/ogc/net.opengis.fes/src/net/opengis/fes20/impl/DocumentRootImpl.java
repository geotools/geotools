/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.AbstractAdhocQueryExpressionType;
import net.opengis.fes20.AbstractIdType;
import net.opengis.fes20.AbstractQueryExpressionType;
import net.opengis.fes20.BBOXType;
import net.opengis.fes20.BinaryComparisonOpType;
import net.opengis.fes20.BinaryLogicOpType;
import net.opengis.fes20.BinarySpatialOpType;
import net.opengis.fes20.BinaryTemporalOpType;
import net.opengis.fes20.ComparisonOpsType;
import net.opengis.fes20.DistanceBufferType;
import net.opengis.fes20.DocumentRoot;
import net.opengis.fes20.ExtensionOpsType;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.FilterCapabilitiesType;
import net.opengis.fes20.FilterType;
import net.opengis.fes20.FunctionType;
import net.opengis.fes20.LiteralType;
import net.opengis.fes20.LogicOpsType;
import net.opengis.fes20.LogicalOperatorsType;
import net.opengis.fes20.PropertyIsBetweenType;
import net.opengis.fes20.PropertyIsLikeType;
import net.opengis.fes20.PropertyIsNilType;
import net.opengis.fes20.PropertyIsNullType;
import net.opengis.fes20.ResourceIdType;
import net.opengis.fes20.SortByType;
import net.opengis.fes20.SpatialOpsType;
import net.opengis.fes20.TemporalOpsType;
import net.opengis.fes20.UnaryLogicOpType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAbstractAdhocQueryExpression <em>Abstract Adhoc Query Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAbstractProjectionClause <em>Abstract Projection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAbstractSelectionClause <em>Abstract Selection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAbstractSortingClause <em>Abstract Sorting Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAfter <em>After</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getTemporalOps <em>Temporal Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAnd <em>And</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getLogicOps <em>Logic Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getAnyInteracts <em>Any Interacts</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getBBOX <em>BBOX</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getSpatialOps <em>Spatial Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getBefore <em>Before</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getBegins <em>Begins</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getBegunBy <em>Begun By</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getBeyond <em>Beyond</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getComparisonOps <em>Comparison Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getContains <em>Contains</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getCrosses <em>Crosses</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getDisjoint <em>Disjoint</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getDuring <em>During</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getDWithin <em>DWithin</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getEndedBy <em>Ended By</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getEnds <em>Ends</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getEquals <em>Equals</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getExtensionOps <em>Extension Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getIntersects <em>Intersects</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getLiteral <em>Literal</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getLogicalOperators <em>Logical Operators</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getMeets <em>Meets</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getMetBy <em>Met By</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getNot <em>Not</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getOr <em>Or</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getOverlappedBy <em>Overlapped By</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getOverlaps <em>Overlaps</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsBetween <em>Property Is Between</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsEqualTo <em>Property Is Equal To</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsGreaterThan <em>Property Is Greater Than</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsGreaterThanOrEqualTo <em>Property Is Greater Than Or Equal To</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsLessThan <em>Property Is Less Than</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsLessThanOrEqualTo <em>Property Is Less Than Or Equal To</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsLike <em>Property Is Like</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsNil <em>Property Is Nil</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsNotEqualTo <em>Property Is Not Equal To</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getPropertyIsNull <em>Property Is Null</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getResourceId <em>Resource Id</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getTContains <em>TContains</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getTEquals <em>TEquals</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getTouches <em>Touches</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getTOverlaps <em>TOverlaps</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DocumentRootImpl#getWithin <em>Within</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xSISchemaLocation;

    /**
     * The default value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected static final String VALUE_REFERENCE_EDEFAULT = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, Fes20Package.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractIdType getId() {
        return (AbstractIdType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ID, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetId(AbstractIdType newId, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ID, newId, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractAdhocQueryExpressionType getAbstractAdhocQueryExpression() {
        return (AbstractAdhocQueryExpressionType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractAdhocQueryExpression(AbstractAdhocQueryExpressionType newAbstractAdhocQueryExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION, newAbstractAdhocQueryExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractQueryExpressionType getAbstractQueryExpression() {
        return (AbstractQueryExpressionType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractQueryExpression(AbstractQueryExpressionType newAbstractQueryExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION, newAbstractQueryExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getAbstractProjectionClause() {
        return (EObject)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractProjectionClause(EObject newAbstractProjectionClause, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE, newAbstractProjectionClause, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getAbstractSelectionClause() {
        return (EObject)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractSelectionClause(EObject newAbstractSelectionClause, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE, newAbstractSelectionClause, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getAbstractSortingClause() {
        return (EObject)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractSortingClause(EObject newAbstractSortingClause, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE, newAbstractSortingClause, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getAfter() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__AFTER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAfter(BinaryTemporalOpType newAfter, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__AFTER, newAfter, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAfter(BinaryTemporalOpType newAfter) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__AFTER, newAfter);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalOpsType getTemporalOps() {
        return (TemporalOpsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__TEMPORAL_OPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalOps(TemporalOpsType newTemporalOps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__TEMPORAL_OPS, newTemporalOps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryLogicOpType getAnd() {
        return (BinaryLogicOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__AND, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnd(BinaryLogicOpType newAnd, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__AND, newAnd, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnd(BinaryLogicOpType newAnd) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__AND, newAnd);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LogicOpsType getLogicOps() {
        return (LogicOpsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__LOGIC_OPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLogicOps(LogicOpsType newLogicOps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__LOGIC_OPS, newLogicOps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getAnyInteracts() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ANY_INTERACTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyInteracts(BinaryTemporalOpType newAnyInteracts, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ANY_INTERACTS, newAnyInteracts, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyInteracts(BinaryTemporalOpType newAnyInteracts) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__ANY_INTERACTS, newAnyInteracts);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BBOXType getBBOX() {
        return (BBOXType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__BBOX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBBOX(BBOXType newBBOX, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__BBOX, newBBOX, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBBOX(BBOXType newBBOX) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__BBOX, newBBOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialOpsType getSpatialOps() {
        return (SpatialOpsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__SPATIAL_OPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpatialOps(SpatialOpsType newSpatialOps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__SPATIAL_OPS, newSpatialOps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getBefore() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__BEFORE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBefore(BinaryTemporalOpType newBefore, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__BEFORE, newBefore, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBefore(BinaryTemporalOpType newBefore) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__BEFORE, newBefore);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getBegins() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__BEGINS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBegins(BinaryTemporalOpType newBegins, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__BEGINS, newBegins, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBegins(BinaryTemporalOpType newBegins) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__BEGINS, newBegins);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getBegunBy() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__BEGUN_BY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBegunBy(BinaryTemporalOpType newBegunBy, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__BEGUN_BY, newBegunBy, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBegunBy(BinaryTemporalOpType newBegunBy) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__BEGUN_BY, newBegunBy);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DistanceBufferType getBeyond() {
        return (DistanceBufferType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__BEYOND, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBeyond(DistanceBufferType newBeyond, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__BEYOND, newBeyond, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBeyond(DistanceBufferType newBeyond) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__BEYOND, newBeyond);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComparisonOpsType getComparisonOps() {
        return (ComparisonOpsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__COMPARISON_OPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComparisonOps(ComparisonOpsType newComparisonOps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__COMPARISON_OPS, newComparisonOps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getContains() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__CONTAINS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContains(BinarySpatialOpType newContains, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__CONTAINS, newContains, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContains(BinarySpatialOpType newContains) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__CONTAINS, newContains);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getCrosses() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__CROSSES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCrosses(BinarySpatialOpType newCrosses, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__CROSSES, newCrosses, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCrosses(BinarySpatialOpType newCrosses) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__CROSSES, newCrosses);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getDisjoint() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__DISJOINT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDisjoint(BinarySpatialOpType newDisjoint, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__DISJOINT, newDisjoint, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDisjoint(BinarySpatialOpType newDisjoint) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__DISJOINT, newDisjoint);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getDuring() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__DURING, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDuring(BinaryTemporalOpType newDuring, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__DURING, newDuring, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDuring(BinaryTemporalOpType newDuring) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__DURING, newDuring);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DistanceBufferType getDWithin() {
        return (DistanceBufferType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__DWITHIN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDWithin(DistanceBufferType newDWithin, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__DWITHIN, newDWithin, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDWithin(DistanceBufferType newDWithin) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__DWITHIN, newDWithin);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getEndedBy() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ENDED_BY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEndedBy(BinaryTemporalOpType newEndedBy, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ENDED_BY, newEndedBy, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndedBy(BinaryTemporalOpType newEndedBy) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__ENDED_BY, newEndedBy);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getEnds() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__ENDS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEnds(BinaryTemporalOpType newEnds, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__ENDS, newEnds, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEnds(BinaryTemporalOpType newEnds) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__ENDS, newEnds);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getEquals() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__EQUALS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEquals(BinarySpatialOpType newEquals, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__EQUALS, newEquals, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEquals(BinarySpatialOpType newEquals) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__EQUALS, newEquals);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExpression() {
        return (EObject)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExpression(EObject newExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__EXPRESSION, newExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtensionOpsType getExtensionOps() {
        return (ExtensionOpsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__EXTENSION_OPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtensionOps(ExtensionOpsType newExtensionOps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__EXTENSION_OPS, newExtensionOps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FilterType getFilter() {
        return (FilterType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__FILTER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFilter(FilterType newFilter, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__FILTER, newFilter, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilter(FilterType newFilter) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__FILTER, newFilter);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FilterCapabilitiesType getFilterCapabilities() {
        return (FilterCapabilitiesType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__FILTER_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFilterCapabilities(FilterCapabilitiesType newFilterCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__FILTER_CAPABILITIES, newFilterCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilterCapabilities(FilterCapabilitiesType newFilterCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__FILTER_CAPABILITIES, newFilterCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FunctionType getFunction() {
        return (FunctionType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__FUNCTION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFunction(FunctionType newFunction, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__FUNCTION, newFunction, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFunction(FunctionType newFunction) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__FUNCTION, newFunction);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getIntersects() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__INTERSECTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIntersects(BinarySpatialOpType newIntersects, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__INTERSECTS, newIntersects, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIntersects(BinarySpatialOpType newIntersects) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__INTERSECTS, newIntersects);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LiteralType getLiteral() {
        return (LiteralType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__LITERAL, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLiteral(LiteralType newLiteral, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__LITERAL, newLiteral, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLiteral(LiteralType newLiteral) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__LITERAL, newLiteral);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LogicalOperatorsType getLogicalOperators() {
        return (LogicalOperatorsType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__LOGICAL_OPERATORS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLogicalOperators(LogicalOperatorsType newLogicalOperators, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__LOGICAL_OPERATORS, newLogicalOperators, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLogicalOperators(LogicalOperatorsType newLogicalOperators) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__LOGICAL_OPERATORS, newLogicalOperators);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getMeets() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__MEETS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMeets(BinaryTemporalOpType newMeets, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__MEETS, newMeets, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMeets(BinaryTemporalOpType newMeets) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__MEETS, newMeets);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getMetBy() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__MET_BY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetBy(BinaryTemporalOpType newMetBy, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__MET_BY, newMetBy, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetBy(BinaryTemporalOpType newMetBy) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__MET_BY, newMetBy);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnaryLogicOpType getNot() {
        return (UnaryLogicOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__NOT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNot(UnaryLogicOpType newNot, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__NOT, newNot, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNot(UnaryLogicOpType newNot) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__NOT, newNot);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryLogicOpType getOr() {
        return (BinaryLogicOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__OR, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOr(BinaryLogicOpType newOr, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__OR, newOr, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOr(BinaryLogicOpType newOr) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__OR, newOr);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getOverlappedBy() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPPED_BY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOverlappedBy(BinaryTemporalOpType newOverlappedBy, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPPED_BY, newOverlappedBy, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOverlappedBy(BinaryTemporalOpType newOverlappedBy) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPPED_BY, newOverlappedBy);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getOverlaps() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOverlaps(BinarySpatialOpType newOverlaps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPS, newOverlaps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOverlaps(BinarySpatialOpType newOverlaps) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__OVERLAPS, newOverlaps);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyIsBetweenType getPropertyIsBetween() {
        return (PropertyIsBetweenType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsBetween(PropertyIsBetweenType newPropertyIsBetween, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN, newPropertyIsBetween, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsBetween(PropertyIsBetweenType newPropertyIsBetween) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN, newPropertyIsBetween);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsEqualTo() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsEqualTo(BinaryComparisonOpType newPropertyIsEqualTo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO, newPropertyIsEqualTo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsEqualTo(BinaryComparisonOpType newPropertyIsEqualTo) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO, newPropertyIsEqualTo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsGreaterThan() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsGreaterThan(BinaryComparisonOpType newPropertyIsGreaterThan, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN, newPropertyIsGreaterThan, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsGreaterThan(BinaryComparisonOpType newPropertyIsGreaterThan) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN, newPropertyIsGreaterThan);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsGreaterThanOrEqualTo() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsGreaterThanOrEqualTo(BinaryComparisonOpType newPropertyIsGreaterThanOrEqualTo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO, newPropertyIsGreaterThanOrEqualTo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsGreaterThanOrEqualTo(BinaryComparisonOpType newPropertyIsGreaterThanOrEqualTo) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO, newPropertyIsGreaterThanOrEqualTo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsLessThan() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsLessThan(BinaryComparisonOpType newPropertyIsLessThan, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN, newPropertyIsLessThan, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsLessThan(BinaryComparisonOpType newPropertyIsLessThan) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN, newPropertyIsLessThan);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsLessThanOrEqualTo() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsLessThanOrEqualTo(BinaryComparisonOpType newPropertyIsLessThanOrEqualTo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO, newPropertyIsLessThanOrEqualTo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsLessThanOrEqualTo(BinaryComparisonOpType newPropertyIsLessThanOrEqualTo) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO, newPropertyIsLessThanOrEqualTo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyIsLikeType getPropertyIsLike() {
        return (PropertyIsLikeType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LIKE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsLike(PropertyIsLikeType newPropertyIsLike, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LIKE, newPropertyIsLike, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsLike(PropertyIsLikeType newPropertyIsLike) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_LIKE, newPropertyIsLike);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyIsNilType getPropertyIsNil() {
        return (PropertyIsNilType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NIL, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsNil(PropertyIsNilType newPropertyIsNil, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NIL, newPropertyIsNil, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsNil(PropertyIsNilType newPropertyIsNil) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NIL, newPropertyIsNil);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryComparisonOpType getPropertyIsNotEqualTo() {
        return (BinaryComparisonOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsNotEqualTo(BinaryComparisonOpType newPropertyIsNotEqualTo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO, newPropertyIsNotEqualTo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsNotEqualTo(BinaryComparisonOpType newPropertyIsNotEqualTo) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO, newPropertyIsNotEqualTo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyIsNullType getPropertyIsNull() {
        return (PropertyIsNullType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NULL, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPropertyIsNull(PropertyIsNullType newPropertyIsNull, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NULL, newPropertyIsNull, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPropertyIsNull(PropertyIsNullType newPropertyIsNull) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__PROPERTY_IS_NULL, newPropertyIsNull);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceIdType getResourceId() {
        return (ResourceIdType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__RESOURCE_ID, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResourceId(ResourceIdType newResourceId, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__RESOURCE_ID, newResourceId, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceId(ResourceIdType newResourceId) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__RESOURCE_ID, newResourceId);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SortByType getSortBy() {
        return (SortByType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__SORT_BY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSortBy(SortByType newSortBy, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__SORT_BY, newSortBy, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSortBy(SortByType newSortBy) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__SORT_BY, newSortBy);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getTContains() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__TCONTAINS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTContains(BinaryTemporalOpType newTContains, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__TCONTAINS, newTContains, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTContains(BinaryTemporalOpType newTContains) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__TCONTAINS, newTContains);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getTEquals() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__TEQUALS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTEquals(BinaryTemporalOpType newTEquals, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__TEQUALS, newTEquals, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTEquals(BinaryTemporalOpType newTEquals) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__TEQUALS, newTEquals);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getTouches() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__TOUCHES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTouches(BinarySpatialOpType newTouches, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__TOUCHES, newTouches, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTouches(BinarySpatialOpType newTouches) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__TOUCHES, newTouches);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryTemporalOpType getTOverlaps() {
        return (BinaryTemporalOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__TOVERLAPS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTOverlaps(BinaryTemporalOpType newTOverlaps, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__TOVERLAPS, newTOverlaps, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTOverlaps(BinaryTemporalOpType newTOverlaps) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__TOVERLAPS, newTOverlaps);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValueReference() {
        return (String)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__VALUE_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueReference(String newValueReference) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__VALUE_REFERENCE, newValueReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinarySpatialOpType getWithin() {
        return (BinarySpatialOpType)getMixed().get(Fes20Package.Literals.DOCUMENT_ROOT__WITHIN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWithin(BinarySpatialOpType newWithin, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Fes20Package.Literals.DOCUMENT_ROOT__WITHIN, newWithin, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWithin(BinarySpatialOpType newWithin) {
        ((FeatureMap.Internal)getMixed()).set(Fes20Package.Literals.DOCUMENT_ROOT__WITHIN, newWithin);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.DOCUMENT_ROOT__MIXED:
                return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
            case Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case Fes20Package.DOCUMENT_ROOT__ID:
                return basicSetId(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION:
                return basicSetAbstractAdhocQueryExpression(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION:
                return basicSetAbstractQueryExpression(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE:
                return basicSetAbstractProjectionClause(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE:
                return basicSetAbstractSelectionClause(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE:
                return basicSetAbstractSortingClause(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__AFTER:
                return basicSetAfter(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__TEMPORAL_OPS:
                return basicSetTemporalOps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__AND:
                return basicSetAnd(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__LOGIC_OPS:
                return basicSetLogicOps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ANY_INTERACTS:
                return basicSetAnyInteracts(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__BBOX:
                return basicSetBBOX(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__SPATIAL_OPS:
                return basicSetSpatialOps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__BEFORE:
                return basicSetBefore(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__BEGINS:
                return basicSetBegins(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__BEGUN_BY:
                return basicSetBegunBy(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__BEYOND:
                return basicSetBeyond(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__COMPARISON_OPS:
                return basicSetComparisonOps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__CONTAINS:
                return basicSetContains(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__CROSSES:
                return basicSetCrosses(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__DISJOINT:
                return basicSetDisjoint(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__DURING:
                return basicSetDuring(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__DWITHIN:
                return basicSetDWithin(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ENDED_BY:
                return basicSetEndedBy(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__ENDS:
                return basicSetEnds(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__EQUALS:
                return basicSetEquals(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__EXPRESSION:
                return basicSetExpression(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__EXTENSION_OPS:
                return basicSetExtensionOps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__FILTER:
                return basicSetFilter(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__FILTER_CAPABILITIES:
                return basicSetFilterCapabilities(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__FUNCTION:
                return basicSetFunction(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__INTERSECTS:
                return basicSetIntersects(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__LITERAL:
                return basicSetLiteral(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__LOGICAL_OPERATORS:
                return basicSetLogicalOperators(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__MEETS:
                return basicSetMeets(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__MET_BY:
                return basicSetMetBy(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__NOT:
                return basicSetNot(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__OR:
                return basicSetOr(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__OVERLAPPED_BY:
                return basicSetOverlappedBy(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__OVERLAPS:
                return basicSetOverlaps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN:
                return basicSetPropertyIsBetween(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO:
                return basicSetPropertyIsEqualTo(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN:
                return basicSetPropertyIsGreaterThan(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO:
                return basicSetPropertyIsGreaterThanOrEqualTo(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN:
                return basicSetPropertyIsLessThan(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO:
                return basicSetPropertyIsLessThanOrEqualTo(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LIKE:
                return basicSetPropertyIsLike(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NIL:
                return basicSetPropertyIsNil(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO:
                return basicSetPropertyIsNotEqualTo(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NULL:
                return basicSetPropertyIsNull(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__RESOURCE_ID:
                return basicSetResourceId(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__SORT_BY:
                return basicSetSortBy(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__TCONTAINS:
                return basicSetTContains(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__TEQUALS:
                return basicSetTEquals(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__TOUCHES:
                return basicSetTouches(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__TOVERLAPS:
                return basicSetTOverlaps(null, msgs);
            case Fes20Package.DOCUMENT_ROOT__WITHIN:
                return basicSetWithin(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case Fes20Package.DOCUMENT_ROOT__ID:
                return getId();
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION:
                return getAbstractAdhocQueryExpression();
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION:
                return getAbstractQueryExpression();
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE:
                return getAbstractProjectionClause();
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE:
                return getAbstractSelectionClause();
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE:
                return getAbstractSortingClause();
            case Fes20Package.DOCUMENT_ROOT__AFTER:
                return getAfter();
            case Fes20Package.DOCUMENT_ROOT__TEMPORAL_OPS:
                return getTemporalOps();
            case Fes20Package.DOCUMENT_ROOT__AND:
                return getAnd();
            case Fes20Package.DOCUMENT_ROOT__LOGIC_OPS:
                return getLogicOps();
            case Fes20Package.DOCUMENT_ROOT__ANY_INTERACTS:
                return getAnyInteracts();
            case Fes20Package.DOCUMENT_ROOT__BBOX:
                return getBBOX();
            case Fes20Package.DOCUMENT_ROOT__SPATIAL_OPS:
                return getSpatialOps();
            case Fes20Package.DOCUMENT_ROOT__BEFORE:
                return getBefore();
            case Fes20Package.DOCUMENT_ROOT__BEGINS:
                return getBegins();
            case Fes20Package.DOCUMENT_ROOT__BEGUN_BY:
                return getBegunBy();
            case Fes20Package.DOCUMENT_ROOT__BEYOND:
                return getBeyond();
            case Fes20Package.DOCUMENT_ROOT__COMPARISON_OPS:
                return getComparisonOps();
            case Fes20Package.DOCUMENT_ROOT__CONTAINS:
                return getContains();
            case Fes20Package.DOCUMENT_ROOT__CROSSES:
                return getCrosses();
            case Fes20Package.DOCUMENT_ROOT__DISJOINT:
                return getDisjoint();
            case Fes20Package.DOCUMENT_ROOT__DURING:
                return getDuring();
            case Fes20Package.DOCUMENT_ROOT__DWITHIN:
                return getDWithin();
            case Fes20Package.DOCUMENT_ROOT__ENDED_BY:
                return getEndedBy();
            case Fes20Package.DOCUMENT_ROOT__ENDS:
                return getEnds();
            case Fes20Package.DOCUMENT_ROOT__EQUALS:
                return getEquals();
            case Fes20Package.DOCUMENT_ROOT__EXPRESSION:
                return getExpression();
            case Fes20Package.DOCUMENT_ROOT__EXTENSION_OPS:
                return getExtensionOps();
            case Fes20Package.DOCUMENT_ROOT__FILTER:
                return getFilter();
            case Fes20Package.DOCUMENT_ROOT__FILTER_CAPABILITIES:
                return getFilterCapabilities();
            case Fes20Package.DOCUMENT_ROOT__FUNCTION:
                return getFunction();
            case Fes20Package.DOCUMENT_ROOT__INTERSECTS:
                return getIntersects();
            case Fes20Package.DOCUMENT_ROOT__LITERAL:
                return getLiteral();
            case Fes20Package.DOCUMENT_ROOT__LOGICAL_OPERATORS:
                return getLogicalOperators();
            case Fes20Package.DOCUMENT_ROOT__MEETS:
                return getMeets();
            case Fes20Package.DOCUMENT_ROOT__MET_BY:
                return getMetBy();
            case Fes20Package.DOCUMENT_ROOT__NOT:
                return getNot();
            case Fes20Package.DOCUMENT_ROOT__OR:
                return getOr();
            case Fes20Package.DOCUMENT_ROOT__OVERLAPPED_BY:
                return getOverlappedBy();
            case Fes20Package.DOCUMENT_ROOT__OVERLAPS:
                return getOverlaps();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN:
                return getPropertyIsBetween();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO:
                return getPropertyIsEqualTo();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN:
                return getPropertyIsGreaterThan();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO:
                return getPropertyIsGreaterThanOrEqualTo();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN:
                return getPropertyIsLessThan();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO:
                return getPropertyIsLessThanOrEqualTo();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LIKE:
                return getPropertyIsLike();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NIL:
                return getPropertyIsNil();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO:
                return getPropertyIsNotEqualTo();
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NULL:
                return getPropertyIsNull();
            case Fes20Package.DOCUMENT_ROOT__RESOURCE_ID:
                return getResourceId();
            case Fes20Package.DOCUMENT_ROOT__SORT_BY:
                return getSortBy();
            case Fes20Package.DOCUMENT_ROOT__TCONTAINS:
                return getTContains();
            case Fes20Package.DOCUMENT_ROOT__TEQUALS:
                return getTEquals();
            case Fes20Package.DOCUMENT_ROOT__TOUCHES:
                return getTouches();
            case Fes20Package.DOCUMENT_ROOT__TOVERLAPS:
                return getTOverlaps();
            case Fes20Package.DOCUMENT_ROOT__VALUE_REFERENCE:
                return getValueReference();
            case Fes20Package.DOCUMENT_ROOT__WITHIN:
                return getWithin();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Fes20Package.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__AFTER:
                setAfter((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__AND:
                setAnd((BinaryLogicOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__ANY_INTERACTS:
                setAnyInteracts((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__BBOX:
                setBBOX((BBOXType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEFORE:
                setBefore((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEGINS:
                setBegins((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEGUN_BY:
                setBegunBy((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEYOND:
                setBeyond((DistanceBufferType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__CONTAINS:
                setContains((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__CROSSES:
                setCrosses((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__DISJOINT:
                setDisjoint((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__DURING:
                setDuring((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__DWITHIN:
                setDWithin((DistanceBufferType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__ENDED_BY:
                setEndedBy((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__ENDS:
                setEnds((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__EQUALS:
                setEquals((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__FILTER:
                setFilter((FilterType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilitiesType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__FUNCTION:
                setFunction((FunctionType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__INTERSECTS:
                setIntersects((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__LITERAL:
                setLiteral((LiteralType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__LOGICAL_OPERATORS:
                setLogicalOperators((LogicalOperatorsType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__MEETS:
                setMeets((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__MET_BY:
                setMetBy((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__NOT:
                setNot((UnaryLogicOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__OR:
                setOr((BinaryLogicOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPPED_BY:
                setOverlappedBy((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPS:
                setOverlaps((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN:
                setPropertyIsBetween((PropertyIsBetweenType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO:
                setPropertyIsEqualTo((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN:
                setPropertyIsGreaterThan((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO:
                setPropertyIsGreaterThanOrEqualTo((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN:
                setPropertyIsLessThan((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO:
                setPropertyIsLessThanOrEqualTo((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LIKE:
                setPropertyIsLike((PropertyIsLikeType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NIL:
                setPropertyIsNil((PropertyIsNilType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO:
                setPropertyIsNotEqualTo((BinaryComparisonOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NULL:
                setPropertyIsNull((PropertyIsNullType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__RESOURCE_ID:
                setResourceId((ResourceIdType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__SORT_BY:
                setSortBy((SortByType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__TCONTAINS:
                setTContains((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__TEQUALS:
                setTEquals((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__TOUCHES:
                setTouches((BinarySpatialOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__TOVERLAPS:
                setTOverlaps((BinaryTemporalOpType)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__VALUE_REFERENCE:
                setValueReference((String)newValue);
                return;
            case Fes20Package.DOCUMENT_ROOT__WITHIN:
                setWithin((BinarySpatialOpType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Fes20Package.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case Fes20Package.DOCUMENT_ROOT__AFTER:
                setAfter((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__AND:
                setAnd((BinaryLogicOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__ANY_INTERACTS:
                setAnyInteracts((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__BBOX:
                setBBOX((BBOXType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEFORE:
                setBefore((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEGINS:
                setBegins((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEGUN_BY:
                setBegunBy((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__BEYOND:
                setBeyond((DistanceBufferType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__CONTAINS:
                setContains((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__CROSSES:
                setCrosses((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__DISJOINT:
                setDisjoint((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__DURING:
                setDuring((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__DWITHIN:
                setDWithin((DistanceBufferType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__ENDED_BY:
                setEndedBy((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__ENDS:
                setEnds((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__EQUALS:
                setEquals((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__FILTER:
                setFilter((FilterType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilitiesType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__FUNCTION:
                setFunction((FunctionType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__INTERSECTS:
                setIntersects((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__LITERAL:
                setLiteral((LiteralType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__LOGICAL_OPERATORS:
                setLogicalOperators((LogicalOperatorsType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__MEETS:
                setMeets((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__MET_BY:
                setMetBy((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__NOT:
                setNot((UnaryLogicOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__OR:
                setOr((BinaryLogicOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPPED_BY:
                setOverlappedBy((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPS:
                setOverlaps((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN:
                setPropertyIsBetween((PropertyIsBetweenType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO:
                setPropertyIsEqualTo((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN:
                setPropertyIsGreaterThan((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO:
                setPropertyIsGreaterThanOrEqualTo((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN:
                setPropertyIsLessThan((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO:
                setPropertyIsLessThanOrEqualTo((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LIKE:
                setPropertyIsLike((PropertyIsLikeType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NIL:
                setPropertyIsNil((PropertyIsNilType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO:
                setPropertyIsNotEqualTo((BinaryComparisonOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NULL:
                setPropertyIsNull((PropertyIsNullType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__RESOURCE_ID:
                setResourceId((ResourceIdType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__SORT_BY:
                setSortBy((SortByType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__TCONTAINS:
                setTContains((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__TEQUALS:
                setTEquals((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__TOUCHES:
                setTouches((BinarySpatialOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__TOVERLAPS:
                setTOverlaps((BinaryTemporalOpType)null);
                return;
            case Fes20Package.DOCUMENT_ROOT__VALUE_REFERENCE:
                setValueReference(VALUE_REFERENCE_EDEFAULT);
                return;
            case Fes20Package.DOCUMENT_ROOT__WITHIN:
                setWithin((BinarySpatialOpType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Fes20Package.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case Fes20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case Fes20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case Fes20Package.DOCUMENT_ROOT__ID:
                return getId() != null;
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION:
                return getAbstractAdhocQueryExpression() != null;
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION:
                return getAbstractQueryExpression() != null;
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE:
                return getAbstractProjectionClause() != null;
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE:
                return getAbstractSelectionClause() != null;
            case Fes20Package.DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE:
                return getAbstractSortingClause() != null;
            case Fes20Package.DOCUMENT_ROOT__AFTER:
                return getAfter() != null;
            case Fes20Package.DOCUMENT_ROOT__TEMPORAL_OPS:
                return getTemporalOps() != null;
            case Fes20Package.DOCUMENT_ROOT__AND:
                return getAnd() != null;
            case Fes20Package.DOCUMENT_ROOT__LOGIC_OPS:
                return getLogicOps() != null;
            case Fes20Package.DOCUMENT_ROOT__ANY_INTERACTS:
                return getAnyInteracts() != null;
            case Fes20Package.DOCUMENT_ROOT__BBOX:
                return getBBOX() != null;
            case Fes20Package.DOCUMENT_ROOT__SPATIAL_OPS:
                return getSpatialOps() != null;
            case Fes20Package.DOCUMENT_ROOT__BEFORE:
                return getBefore() != null;
            case Fes20Package.DOCUMENT_ROOT__BEGINS:
                return getBegins() != null;
            case Fes20Package.DOCUMENT_ROOT__BEGUN_BY:
                return getBegunBy() != null;
            case Fes20Package.DOCUMENT_ROOT__BEYOND:
                return getBeyond() != null;
            case Fes20Package.DOCUMENT_ROOT__COMPARISON_OPS:
                return getComparisonOps() != null;
            case Fes20Package.DOCUMENT_ROOT__CONTAINS:
                return getContains() != null;
            case Fes20Package.DOCUMENT_ROOT__CROSSES:
                return getCrosses() != null;
            case Fes20Package.DOCUMENT_ROOT__DISJOINT:
                return getDisjoint() != null;
            case Fes20Package.DOCUMENT_ROOT__DURING:
                return getDuring() != null;
            case Fes20Package.DOCUMENT_ROOT__DWITHIN:
                return getDWithin() != null;
            case Fes20Package.DOCUMENT_ROOT__ENDED_BY:
                return getEndedBy() != null;
            case Fes20Package.DOCUMENT_ROOT__ENDS:
                return getEnds() != null;
            case Fes20Package.DOCUMENT_ROOT__EQUALS:
                return getEquals() != null;
            case Fes20Package.DOCUMENT_ROOT__EXPRESSION:
                return getExpression() != null;
            case Fes20Package.DOCUMENT_ROOT__EXTENSION_OPS:
                return getExtensionOps() != null;
            case Fes20Package.DOCUMENT_ROOT__FILTER:
                return getFilter() != null;
            case Fes20Package.DOCUMENT_ROOT__FILTER_CAPABILITIES:
                return getFilterCapabilities() != null;
            case Fes20Package.DOCUMENT_ROOT__FUNCTION:
                return getFunction() != null;
            case Fes20Package.DOCUMENT_ROOT__INTERSECTS:
                return getIntersects() != null;
            case Fes20Package.DOCUMENT_ROOT__LITERAL:
                return getLiteral() != null;
            case Fes20Package.DOCUMENT_ROOT__LOGICAL_OPERATORS:
                return getLogicalOperators() != null;
            case Fes20Package.DOCUMENT_ROOT__MEETS:
                return getMeets() != null;
            case Fes20Package.DOCUMENT_ROOT__MET_BY:
                return getMetBy() != null;
            case Fes20Package.DOCUMENT_ROOT__NOT:
                return getNot() != null;
            case Fes20Package.DOCUMENT_ROOT__OR:
                return getOr() != null;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPPED_BY:
                return getOverlappedBy() != null;
            case Fes20Package.DOCUMENT_ROOT__OVERLAPS:
                return getOverlaps() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_BETWEEN:
                return getPropertyIsBetween() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO:
                return getPropertyIsEqualTo() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN:
                return getPropertyIsGreaterThan() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO:
                return getPropertyIsGreaterThanOrEqualTo() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN:
                return getPropertyIsLessThan() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO:
                return getPropertyIsLessThanOrEqualTo() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_LIKE:
                return getPropertyIsLike() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NIL:
                return getPropertyIsNil() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO:
                return getPropertyIsNotEqualTo() != null;
            case Fes20Package.DOCUMENT_ROOT__PROPERTY_IS_NULL:
                return getPropertyIsNull() != null;
            case Fes20Package.DOCUMENT_ROOT__RESOURCE_ID:
                return getResourceId() != null;
            case Fes20Package.DOCUMENT_ROOT__SORT_BY:
                return getSortBy() != null;
            case Fes20Package.DOCUMENT_ROOT__TCONTAINS:
                return getTContains() != null;
            case Fes20Package.DOCUMENT_ROOT__TEQUALS:
                return getTEquals() != null;
            case Fes20Package.DOCUMENT_ROOT__TOUCHES:
                return getTouches() != null;
            case Fes20Package.DOCUMENT_ROOT__TOVERLAPS:
                return getTOverlaps() != null;
            case Fes20Package.DOCUMENT_ROOT__VALUE_REFERENCE:
                return VALUE_REFERENCE_EDEFAULT == null ? getValueReference() != null : !VALUE_REFERENCE_EDEFAULT.equals(getValueReference());
            case Fes20Package.DOCUMENT_ROOT__WITHIN:
                return getWithin() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
