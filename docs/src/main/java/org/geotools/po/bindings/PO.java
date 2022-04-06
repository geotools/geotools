/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.po.bindings;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://www.geotools.org/po schema.
 *
 * @generated
 */
public final class PO extends XSD {

    /** singleton instance */
    private static final PO instance = new PO();

    /** Returns the singleton instance. */
    public static final PO getInstance() {
        return instance;
    }

    /** private constructor */
    private PO() {}

    protected void addDependencies(Set dependencies) {
        // TODO: add dependencies here
    }

    /** Returns 'http://www.geotools.org/po'. */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'po.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("po.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.geotools.org/po";

    /* Type Definitions */
    /** @generated */
    public static final QName Items = new QName("http://www.geotools.org/po", "Items");
    /** @generated */
    public static final QName PurchaseOrderType =
            new QName("http://www.geotools.org/po", "PurchaseOrderType");
    /** @generated */
    public static final QName SKU = new QName("http://www.geotools.org/po", "SKU");
    /** @generated */
    public static final QName USAddress = new QName("http://www.geotools.org/po", "USAddress");
    /** @generated */
    public static final QName Items_item = new QName("http://www.geotools.org/po", "Items_item");

    /* Elements */
    /** @generated */
    public static final QName comment = new QName("http://www.geotools.org/po", "comment");
    /** @generated */
    public static final QName purchaseOrder =
            new QName("http://www.geotools.org/po", "purchaseOrder");

    /* Attributes */

}
