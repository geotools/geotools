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

import org.geotools.xsd.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.geotools.org/po schema.
 *
 * @generated
 */
public class POConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public POConfiguration() {
        super(PO.getInstance());

        // TODO: add dependencies here
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    @Override
    protected final void registerBindings(MutablePicoContainer container) {
        // Types
        container.registerComponentImplementation(PO.Items, ItemsBinding.class);
        container.registerComponentImplementation(PO.PurchaseOrderType, PurchaseOrderTypeBinding.class);
        container.registerComponentImplementation(PO.SKU, SKUBinding.class);
        container.registerComponentImplementation(PO.USAddress, USAddressBinding.class);
        container.registerComponentImplementation(PO.Items_item, Items_itemBinding.class);

        // Elements
        container.registerComponentImplementation(PO.comment, CommentBinding.class);
        container.registerComponentImplementation(PO.purchaseOrder, PurchaseOrderBinding.class);
    }
}
