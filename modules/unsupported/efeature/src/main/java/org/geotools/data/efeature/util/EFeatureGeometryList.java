package org.geotools.data.efeature.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.internal.EFeatureGeometryDelegate;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Unmodifiable list of {@link EFeatureGeometry} instances.
 * 
 * @author kengu
 * 
 */
public class EFeatureGeometryList<V extends Geometry> extends AbstractList<EFeatureGeometry<V>> {
    private static final long serialVersionUID = 1L;

    private final List<EFeatureGeometry<V>> eItems;

    private final Class<V> type;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    public EFeatureGeometryList(EFeatureInfo eInfo, EObject eObject, Class<V> type) {
        this.type = type;
        List<EFeatureGeometry<V>> eList = toList(eInfo, eObject, type);
        this.eItems = Collections.unmodifiableList(eList);
    }

    @SuppressWarnings("unchecked")
    public EFeatureGeometryList(List<? extends EFeatureProperty<V, ?>> eList, Class<V> type) {
        this.type = type;
        List<EFeatureGeometry<V>> eSelected = new ArrayList<EFeatureGeometry<V>>(eList.size());
        for (EFeatureProperty<V, ?> it : eList) {
            if (it instanceof EFeatureGeometry) {
                // Perform unchecked cast. This is always safe, since
                // EFeatureGeometry<V> extends EFeatureProperty<V,GeometryAttribute>
                //
                eSelected.add((EFeatureGeometry<V>) it);
            }
        }
        this.eItems = Collections.unmodifiableList(eSelected);
    }

    public EFeatureGeometryList(EFeatureGeometryList<V> eList) {
        this.type = eList.type;
        this.eItems = Collections.unmodifiableList(eList.eItems);
    }

    // ----------------------------------------------------- 
    //  EFeatureGeometryList implementation
    // -----------------------------------------------------

    public Class<V> getType() {
        return type;
    }

    // ----------------------------------------------------- 
    //  List implementation
    // -----------------------------------------------------

    @Override
    public EFeatureGeometry<V> get(int index) {
        return eItems.get(index);
    }

    @Override
    public int size() {
        return eItems.size();
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    public static List<EFeatureGeometry<Geometry>> toList(EFeatureInfo eFeatureInfo,
            EObject eObject, Collection<EAttribute> eAttributes) {
        return toList(eFeatureInfo, eObject, Geometry.class);
    }

    public static <V extends Geometry> List<EFeatureGeometry<V>> toList(EFeatureInfo eFeatureInfo,
            EObject eObject, Class<V> type) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toList(eFeatureInfo, eObject, eAttrMap.values(), type);
        }
        return null;
    }

    public static <V extends Geometry> List<EFeatureGeometry<V>> toList(EFeatureInfo eFeatureInfo,
            EObject eObject, Collection<EAttribute> eAttributes, Class<V> type) {
        List<EFeatureGeometry<V>> list = new ArrayList<EFeatureGeometry<V>>(eAttributes.size());
        for (EAttribute it : eAttributes) {
            EFeatureGeometry<V> d = EFeatureGeometryDelegate.create(eObject, it.getName(), type,
                    eFeatureInfo);
            if (d != null) {
                list.add(d);
            }
        }
        return list;

    }

}
