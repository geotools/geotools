/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Code has been forked from <b>spring-data-elastic</b>, it handles parsing and formatting dates the
 * way ElasticSearch does, without requiring a large dependency on elasticsearch-server, or causing
 * split packages (like the previous solutions, grabbing sources from elasticsearch-server, did).
 */
package org.geotools.data.elasticsearch.date;
