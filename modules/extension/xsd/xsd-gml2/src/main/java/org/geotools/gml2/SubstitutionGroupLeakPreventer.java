/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml2;

import java.util.ArrayList;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDPackage;
import org.geotools.util.Utilities;

/**
 * Adapter for preventing memory leaks created by substitution group affiliations.
 *
 * <p>When an application schema contains an element in the gml:_Feature substitution group a link from gml:_Feature
 * back to the app schema element is created. Since the gml schema (and thus the gml:_Feature) element is a singleton
 * this creates a memory leak. This adapter watches the {@link XSDElementDeclaration#getSubstitutionGroup()} of the
 * gml:_Feature element and prevents it from growing in size by making it a unique list of {@link XSDElementDeclaration}
 * based on qualified name.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class SubstitutionGroupLeakPreventer implements Adapter {

    XSDElementDeclaration target;

    @Override
    public Notifier getTarget() {
        return target;
    }

    @Override
    public void setTarget(Notifier newTarget) {
        target = (XSDElementDeclaration) newTarget;
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type instanceof XSDElementDeclaration;
    }

    @Override
    public void notifyChanged(Notification notification) {
        int featureId = notification.getFeatureID(target.getClass());
        if (featureId != XSDPackage.XSD_ELEMENT_DECLARATION__SUBSTITUTION_GROUP) {
            return;
        }

        if (notification.getEventType() != Notification.ADD) {
            return;
        }
        if (!(notification.getNewValue() instanceof XSDElementDeclaration)) {
            return;
        }

        XSDElementDeclaration el = (XSDElementDeclaration) notification.getNewValue();
        XSDElementDeclaration e = target;

        while (e != null) {
            synchronized (e) {
                ArrayList<Integer> toremove = new ArrayList<>();
                for (int i = 0; i < e.getSubstitutionGroup().size(); i++) {
                    XSDElementDeclaration se = e.getSubstitutionGroup().get(i);
                    if (se == null
                            || Utilities.equals(el.getTargetNamespace(), se.getTargetNamespace())
                                    && Utilities.equals(el.getName(), se.getName())) {
                        toremove.add(i);
                    }
                }

                // iterate back in reverse order and skip the last element as to keep the latest
                // version of the element
                ArrayList<XSDElementDeclaration> removed = new ArrayList<>();
                for (int i = toremove.size() - 2; i > -1; i--) {
                    removed.add(e);
                }

                // set the removed elements sub affiliation to a clone of the actual element
                for (XSDElementDeclaration se : removed) {
                    if (se != null && e.equals(se.getSubstitutionGroupAffiliation())) {
                        XSDElementDeclaration clone = (XSDElementDeclaration) e.cloneConcreteComponent(false, false);
                        clone.setTargetNamespace(GML.NAMESPACE);

                        se.setSubstitutionGroupAffiliation(clone);
                    }
                }
            }
            e = e.getSubstitutionGroupAffiliation();
        }
    }
}
