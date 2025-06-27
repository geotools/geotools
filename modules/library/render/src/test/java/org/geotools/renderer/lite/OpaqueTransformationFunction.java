/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import org.geotools.api.coverage.grid.GridGeometry;
import org.geotools.api.data.Query;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * A rendering transformation that wraps the input feature collection in a way that prevents the StreamingRenderer from
 * recognizing it, and checking it goes through converters as a last attempt to turn the transformation result into a
 * {@link SimpleFeatureCollection} again.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class OpaqueTransformationFunction extends FunctionExpressionImpl implements RenderingTransformation {

    public static FunctionName NAME = new FunctionNameImpl("OpaqueTransformation");

    public OpaqueTransformationFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        // take the input collection and wrap it so that the StreamingRenderer cannot recognize it
        return new ResultHolder((SimpleFeatureCollection) object);
    }

    @Override
    public Query invertQuery(Query targetQuery, GridGeometry gridGeometry) {
        return targetQuery;
    }

    @Override
    public GridGeometry invertGridGeometry(Query targetQuery, GridGeometry targetGridGeometry) {
        return targetGridGeometry;
    }

    private static class ResultHolder {
        private final SimpleFeatureCollection result;

        public ResultHolder(SimpleFeatureCollection result) {
            this.result = result;
        }

        public SimpleFeatureCollection getResult() {
            return result;
        }
    }

    public static final class ResultHolderConverterFactory implements ConverterFactory {

        @Override
        public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
            if (ResultHolder.class.isAssignableFrom(source) && FeatureCollection.class.isAssignableFrom(target)) {
                return new Converter() {
                    @Override
                    public <T> T convert(Object source, Class<T> target) throws Exception {
                        if (source instanceof ResultHolder) {
                            return target.cast(((ResultHolder) source).getResult());
                        }
                        return null;
                    }
                };
            }

            return null;
        }
    }
}
