package org.geotools.data.efeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public abstract class EObjectFilter {

    /**
     * An ID indicating that no feature ID information is applicable.
     */
    public final static int NO_FEATURE_ID = -1;

    public final static EObjectFilter INCLUSIVE = new EObjectFilter() {

        @Override
        public boolean matches(Object object) {
            return true;
        }
    };

    public final static EObjectFilter EXCLUSIVE = new EObjectFilter() {

        @Override
        public boolean matches(Object object) {
            return false;
        }
    };

    public abstract boolean matches(Object object);

    /**
     * Creates a new filter combining me with another as a boolean conjunction. The "and" operation
     * short-circuits; the <code>other</code> filter is not consulted when I (the first filter) do
     * not match.
     * 
     * @param other another filter (must not be <code>null</code>)
     * 
     * @return a new "and" filter
     */
    public final EObjectFilter and(final EObjectFilter other) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return EObjectFilter.this.matches(object) && other.matches(object);
            }
        };
    }

    /**
     * Creates a new filter combining me with another as a boolean disjunction. The "or" operation
     * short-circuits; the <code>other</code> filter is not consulted when I (the first filter)
     * match.
     * 
     * @param other another filter (must not be <code>null</code>)
     * 
     * @return a new "or" filter
     */
    public final EObjectFilter or(final EObjectFilter other) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return EObjectFilter.this.matches(object) || other.matches(object);
            }
        };
    }

    /**
     * Creates a new filter that is the boolean negation of me.
     * 
     * @return the opposite of me
     */
    public final EObjectFilter negated() {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return !EObjectFilter.this.matches(object);
            }
        };
    }

    /**
     * Creates a filter matching any {@link EObject} with given id.
     * 
     * @param id - a {@link EObject} instance id
     * 
     * @return the filter
     * @see {@link EcoreUtil#getID(EObject)}
     */
    public static EObjectFilter createObjectIDFilter(final String id) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return (object instanceof EObject ? id.equals(EcoreUtil.getID((EObject) object))
                        : false);
            }
        };
    }

    /**
     * Creates a filter matching any specified object.
     * 
     * @param object - a object instance
     * 
     * @return the filter
     * 
     * @see {@link EcoreUtil#equals(EObject, EObject)}
     */
    public static EObjectFilter createObjectFilter(final EObject eObject) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return (object instanceof EObject ? EcoreUtil.equals((EObject) object, eObject)
                        : false);
            }
        };
    }

    /**
     * Creates a filter matching specified objects.
     * 
     * @param all - if <code>true</code>, all objects must match
     * @param negated - if <code>true</code>, concatenation is negated
     * @param objects - object instances
     * 
     * @return the filter
     */
    public static EObjectFilter createObjectFilter(final boolean all, final boolean negated,
            final EObject... eObjects) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EObject) {
                    int count = 0;
                    EObject match = (EObject) object;
                    for (EObject it : eObjects) {
                        if (EcoreUtil.equals(match, it)) {
                            count++;
                            if (!all)
                                break;
                        } else if (all) {
                            count = 0;
                            break;
                        }
                    }
                    return (all ? count > 0 : count == 1);
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching any specified feature.
     * 
     * @param feature a structural feature meta-object
     * 
     * @return the filter
     */
    public static EObjectFilter createFeatureFilter(final EStructuralFeature feature) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return object == feature;
            }
        };
    }

    /**
     * Creates a filter matching specified features.
     * 
     * @param all - if <code>true</code>, all features must match
     * @param negated - if <code>true</code>, concatenation is negated
     * @param features - structural features
     * 
     * @return the filter
     */
    public static EObjectFilter createFeatureFilter(final boolean all, final boolean negated,
            final EStructuralFeature... features) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EStructuralFeature) {
                    int count = 0;
                    EStructuralFeature match = (EStructuralFeature) object;
                    for (EObject it : features) {
                        if (EcoreUtil.equals(match, it)) {
                            count++;
                            if (!all)
                                break;
                        } else if (all) {
                            count = 0;
                            break;
                        }
                    }
                    return (all ? count > 0 : count == 1);
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching the specified feature.
     * <p>
     * <strong>NOTE</strong>: This filter only work on {@link EObject}s that implement
     * {@link EStructuralFeature}
     * <p>
     * 
     * @param ownerType the object type as a Java class or interface
     * @param featureId the feature's numeric ID
     * 
     * @return the filter
     */
    public static EObjectFilter createFeatureFilter(final Class<?> ownerType, final int featureId) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return (object instanceof EObject) && ownerType.isInstance(object)
                        && (getFeatureID(ownerType, (EObject) object) == featureId);
            }
        };
    }

    public static int getFeatureID(Class<?> expectedClass, EObject eObject) {
        if (eObject instanceof EStructuralFeature) {
            EStructuralFeature feature = (EStructuralFeature) eObject;
            return ((InternalEObject) eObject).eDerivedStructuralFeatureID(feature.getFeatureID(),
                    feature.getContainerClass());
        }
        return EObjectFilter.NO_FEATURE_ID;
    }

    /**
     * Creates a filter matching any instance of the specified type. This variant is useful for
     * notifiers that are not modeled via Ecore.
     * 
     * @param type the object type as a Java class or interface
     * 
     * @return the filter
     */
    public static EObjectFilter createTypeFilter(final Class<?> type) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return type.isInstance(object);
            }
        };
    }

    /**
     * Creates a filter matching any instance of the specified type. This variant is useful for
     * notifiers that are not modeled via Ecore.
     * 
     * @param all - if <code>true</code>, all objects must match type
     * @param negated - if <code>true</code>, concatenation is negated
     * @param types - the object types as a Java class or interface
     * 
     * @return the filter
     */
    public static EObjectFilter createTypeFilter(final boolean all, final boolean negated,
            final Class<?>... types) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                int count = 0;
                for (Class<?> it : types) {
                    if (it.isInstance(object)) {
                        count++;
                        if (!all)
                            break;
                    } else if (all) {
                        count = 0;
                        break;
                    }
                }
                return (all ? count > 0 : count == 1);
            }
        };
    }

    /**
     * Creates a filter matching any instance of the specified {@link EClassifier} type instance.
     * This variant is useful for notifiers that are modelled via Ecore.
     * 
     * @param type the {@link EClassifier} type
     * 
     * @return the filter
     */
    public static EObjectFilter createClassifierFilter(final EClassifier type) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                return type.isInstance(object);
            }
        };
    }

    /**
     * Creates a filter matching any instance of the specified {@link EClassifier} type instance.
     * This variant is useful for notifiers that are modelled via Ecore.
     * 
     * @param all - if <code>true</code>, all objects must match given classifier
     * @param negated - if <code>true</code>, concatenation is negated
     * @param types the {@link EClassifier} types
     * 
     * @return the filter
     */
    public static EObjectFilter createClassifierFilter(final boolean all, final boolean negated,
            final EClassifier... types) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EObject) {
                    int count = 0;
                    for (EClassifier it : types) {
                        if (it.isInstance(object)) {
                            count++;
                            if (!all)
                                break;
                        } else if (all) {
                            count = 0;
                            break;
                        }
                    }
                    return (all ? count > 0 : count == 1);
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching given instance of the specified {@link EClassifier} type instance
     * having given data type instance This variant is useful for notifiers that are modelled via
     * Ecore.
     * 
     * @param type the {@link EClassifier} type
     * 
     * @return the filter
     */
    public static EObjectFilter createDataTypeFilter(final Class<? extends EDataType> cls,
            final Class<? extends Serializable> type, final boolean equals) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EStructuralFeature) {
                    final EClassifier eType = ((EStructuralFeature) object).getEType();
                    if (cls.isInstance(eType)) {
                        Class<?> instClass = eType.getInstanceClass();
                        return (equals ? instClass == type : instClass.isAssignableFrom(type));
                    }
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching contents of given object.
     * <p>
     * 
     * @param eGeometryType the filter to use on containers
     * @param all - if <code>true</code>, all contents must be matched by the filter
     * @param tree - if <code>true</code>, {@link EObject#eAllContents()} is used instead of
     *        {@link EObject#eContents()}
     * 
     * @return the filter
     * 
     */
    public static EObjectFilter createContainerFilter(final EObjectFilter filter) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EObject) {
                    EObject eObj = (EObject) object;
                    return filter.matches(eObj.eContainer());
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching contents of given object.
     * <p>
     * 
     * @param filter - the ancestor filter
     * @param all - if <code>true</code>, all contents must be matched by the filter
     * @param tree - if <code>true</code>, {@link EObject#eAllContents()} is used instead of
     *        {@link EObject#eContents()}
     * 
     * @return the filter
     * 
     */
    public static EObjectFilter createAncestorFilter(final EObjectFilter filter) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EObject) {
                    EObject eObj = (EObject) object;
                    EObject eAncestor = null;
                    while (eObj != null) {
                        if (filter.matches(eObj)) {
                            return true;
                        }
                        eAncestor = eObj.eContainer();
                        // Reached break condition?
                        //
                        eObj = (eObj == eAncestor ? null : eAncestor);
                    }
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter matching contents of given object.
     * <p>
     * 
     * @param eGeometryType the filter to use on contents
     * @param all - if <code>true</code>, all contents must be matched by the filter
     * @param tree - if <code>true</code>, {@link EObject#eAllContents()} is used instead of
     *        {@link EObject#eContents()}
     * 
     * @return the filter
     * 
     */
    public static EObjectFilter createContentFilter(final EObjectFilter filter, final boolean all,
            final boolean tree) {
        return new EObjectFilter() {
            @Override
            public boolean matches(Object object) {
                if (object instanceof EObject) {
                    int count = 0;
                    EObject eObj = (EObject) object;
                    if (all) {
                        TreeIterator<EObject> contents = eObj.eAllContents();
                        while (contents.hasNext()) {
                            if (filter.matches(contents.next())) {
                                count++;
                                if (!all)
                                    break;
                            } else if (all) {
                                count = 0;
                                break;
                            }
                        }
                    } else {
                        EList<EObject> contents = eObj.eContents();
                        for (EObject it : contents) {
                            if (filter.matches(it)) {
                                count++;
                                if (!all)
                                    break;
                            } else if (all) {
                                count = 0;
                                break;
                            }
                        }
                    }
                    return (all ? count > 0 : count == 1);
                }
                return false;
            }
        };
    }

    /**
     * Creates a filter composite.
     * <p>
     * 
     * @param all - if <code>true</code>, all filters must match
     * @param negated - if <code>true</code>, concatenation is negated
     * @param filters - filters to concatenate
     * 
     * @return the filter
     * 
     */
    public static EObjectFilter createCompositeFilter(final boolean all, final boolean negated,
            final EObjectFilter... filters) {
        EObjectFilter filter = null;
        for (EObjectFilter it : filters) {
            if (filter == null) {
                filter = it;
            } else {
                filter = (all ? filter.and(it) : filter.or(it));
            }
        }
        return (negated ? filter.negated() : filter);
    }

    /**
     * Utility method for conditional object selection
     * 
     * @param <T> - object type
     * @param items - items to select from
     * @param filter - filter used to match items
     * @return a list of selected items
     */
    public static <T> Collection<T> select(Collection<T> items, EObjectFilter filter) {
        List<T> selected = new ArrayList<T>(items.size());
        for (T it : items) {
            if (filter.matches(it)) {
                selected.add(it);
            }
        }
        return selected;
    }

    /**
     * Utility method for conditional object selection
     * 
     * @param <T> - object type
     * @param items - items to select from
     * @param filter - filter used to match items
     * @return first match found, or <code>null</code> if not found
     */
    public static <T> T selectHead(Collection<T> items, EObjectFilter filter) {
        List<T> selected = new ArrayList<T>(items.size());
        for (T it : items) {
            if (filter.matches(it)) {
                selected.add(it);
            }
        }
        return null;
    }

    /**
     * Utility method for conditional object selection
     * 
     * @param <T> - object type
     * @param items - items to select from
     * @param filter - filter used to match items
     * @return a list of selected items
     */
    public static <T> Collection<T> select(TreeIterator<T> items, EObjectFilter filter) {
        List<T> selected = new ArrayList<T>();
        while (items.hasNext()) {
            T it = items.next();
            if (filter.matches(it)) {
                selected.add(it);
            }
        }
        return selected;
    }

}
