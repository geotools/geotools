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
package org.geotools.cql;

import org.geotools.api.filter.Filter;

/**
 * This class maintains set of common functions used in the CQL and ECQL examples
 *
 * @author Mauricio Pazos
 */
final class Utility {

    private Utility() {
        // utility class
    }

    public static void prittyPrintFilter(Filter filter) {
        System.out.println("The filter result is:\n" + filter.toString());
    }
}
