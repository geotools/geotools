/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 13 November 2002, 13:52
 */
package org.geotools.styling;

import java.util.HashMap;
import java.util.Map;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.ContrastMethodStrategy;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.factory.CommonFactoryFinder;

/**
 * The ContrastEnhancement object defines contrast enhancement for a channel of a false-color image or for a color
 * image. Its format is:
 *
 * <pre>
 * &lt;xs:element name=&quot;ContrastEnhancement&quot;&gt;
 *   &lt;xs:complexType&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:choice minOccurs=&quot;0&quot;&gt;
 *         &lt;xs:element ref=&quot;sld:Normalize&quot;/&gt;
 *         &lt;xs:element ref=&quot;sld:Histogram&quot;/&gt;
 *       &lt;/xs:choice&gt;
 *       &lt;xs:element ref=&quot;sld:GammaValue&quot; minOccurs=&quot;0&quot;/&gt;
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name=&quot;Normalize&quot;&gt;
 *   &lt;xs:complexType/&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name=&quot;Histogram&quot;&gt;
 *   &lt;xs:complexType/&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name=&quot;GammaValue&quot; type=&quot;xs:double&quot;/&gt;
 * </pre>
 *
 * In the case of a color image, the relative grayscale brightness of a pixel color is used. ?Normalize? means to
 * stretch the contrast so that the dimmest color is stretched to black and the brightest color is stretched to white,
 * with all colors in between stretched out linearly. ?Histogram? means to stretch the contrast based on a histogram of
 * how many colors are at each brightness level on input, with the goal of producing equal number of pixels in the image
 * at each brightness level on output. This has the effect of revealing many subtle ground features. A ?GammaValue?
 * tells how much to brighten (value greater than 1.0) or dim (value less than 1.0) an image. The default GammaValue is
 * 1.0 (no change). If none of Normalize, Histogram, or GammaValue are selected in a ContrastEnhancement, then no
 * enhancement is performed.
 *
 * @author iant
 */
public class ContrastEnhancementImpl implements org.geotools.api.style.ContrastEnhancement {

    @SuppressWarnings("PMD.UnusedPrivateField")
    private FilterFactory filterFactory;

    private Expression gamma;

    private ContrastMethod method;

    private Map<String, Expression> options;

    public ContrastEnhancementImpl() {
        this(CommonFactoryFinder.getFilterFactory(null));
    }

    public ContrastEnhancementImpl(FilterFactory factory) {
        this(factory, null);
    }

    public ContrastEnhancementImpl(FilterFactory factory, ContrastMethod method) {
        filterFactory = factory;
        this.method = method;
    }

    public ContrastEnhancementImpl(org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        filterFactory = CommonFactoryFinder.getFilterFactory(null);
        org.geotools.api.style.ContrastMethod meth = contrastEnhancement.getMethod();
        if (meth != null) {
            this.method = ContrastMethod.valueOf(meth.name());
        }
        this.gamma = contrastEnhancement.getGammaValue();
        if (contrastEnhancement instanceof ContrastEnhancement) {
            ContrastEnhancement other = contrastEnhancement;
            if (other.getOptions() != null) {
                this.options = new HashMap<>();
                this.options.putAll(other.getOptions());
            }
        }
    }

    public ContrastEnhancementImpl(FilterFactory factory, Expression gamma, ContrastMethod method) {
        this.filterFactory = factory;
        this.gamma = gamma;
        this.method = method;
    }

    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    @Override
    public Expression getGammaValue() {
        return gamma;
    }

    @Override
    public void setGammaValue(Expression gamma) {
        this.gamma = gamma;
    }

    @Override
    public ContrastMethod getMethod() {
        return method;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    static ContrastEnhancementImpl cast(org.geotools.api.style.ContrastEnhancement enhancement) {
        if (enhancement == null) {
            return null;
        } else if (enhancement instanceof ContrastEnhancementImpl) {
            return (ContrastEnhancementImpl) enhancement;
        } else {
            ContrastEnhancementImpl copy = new ContrastEnhancementImpl();
            copy.setGammaValue(enhancement.getGammaValue());
            copy.setMethod(enhancement.getMethod());
            return copy;
        }
    }

    @Override
    public void setMethod(ContrastMethod method) {
        this.method = method;
    }

    @Override
    public Map<String, Expression> getOptions() {
        if (this.options == null) {
            this.options = new HashMap<>();
        }
        return this.options;
    }

    @Override
    public boolean hasOption(String key) {
        if (this.options == null) {
            this.options = new HashMap<>();
        }
        return options.containsKey(key);
    }

    @Override
    public Expression getOption(String key) {
        return this.options.get(key);
    }

    @Override
    public void addOption(String key, Expression value) {
        if (this.options == null) {
            this.options = new HashMap<>();
        }
        options.put(key, value);
    }

    @Override
    public void setOptions(Map<String, Expression> options) {
        this.options = options;
    }

    @Override
    public void setMethod(ContrastMethodStrategy method) {
        this.method = method.getMethod();
        this.options = method.getOptions();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (gamma == null ? 0 : gamma.hashCode());
        result = prime * result + (method == null ? 0 : method.hashCode());
        result = prime * result + (options == null ? 0 : options.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ContrastEnhancementImpl)) {
            return false;
        }
        ContrastEnhancementImpl other = (ContrastEnhancementImpl) obj;
        if (gamma == null) {
            if (other.gamma != null) {
                return false;
            }
        } else if (!gamma.equals(other.gamma)) {
            return false;
        }
        if (method == null) {
            if (other.method != null) {
                return false;
            }
        } else if (!method.equals(other.method)) {
            return false;
        }
        if (options == null) {
            if (other.options != null) {
                return false;
            }
        } else if (!options.equals(other.options)) {
            return false;
        }
        return true;
    }
}
