/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.hdf4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * Utility class for supporting HDF products like SST, Albedo, etc..
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public abstract class HDF4Products implements List<HDF4Products.HDF4Product> {
    
	private ArrayList<HDF4Product> productList;
    
    private class HDF4ProductsIterator implements Iterator<HDF4Product> {

        private Iterator<HDF4Product> it;

        public boolean hasNext() {
            return it.hasNext();
        }

        public HDF4Product next() {
            return it.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();

        }

        public HDF4ProductsIterator(Iterator<HDF4Product> it) {
            this.it = it;
        }
    }

    public boolean add(HDF4Product o) {
        return false;
    }

    public void add(int index, HDF4Product element) {
        productList.add(index, element);
    }

    public boolean addAll(Collection<? extends HDF4Product> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean addAll(int index, Collection<? extends HDF4Product> c) {
        throw new UnsupportedOperationException ();
    }

    public void clear() {
        throw new UnsupportedOperationException ();
    }

    public boolean contains(Object o) {
        if (productList!=null)
            return productList.contains(o);
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public HDF4Product get(int productIndex) {
        if (productIndex > nProducts)
            throw new ArrayIndexOutOfBoundsException(
                    "Specified product index is out of range");
        else
            return productList.get(productIndex);
    }

    public int indexOf(Object o) {
        return productList.indexOf(o);
    }

    public boolean isEmpty() {
        return productList.isEmpty();
    }

    public Iterator<HDF4Product> iterator() {
        return new HDF4ProductsIterator(productList.iterator());
    }

    public int lastIndexOf(Object o) {
        return productList.lastIndexOf(o);
    }

    public ListIterator<HDF4Product> listIterator() {
        throw new UnsupportedOperationException ();
    }

    public ListIterator<HDF4Product> listIterator(int index) {
        throw new UnsupportedOperationException ();
    }

    public HDF4Product remove(int index) {
        throw new UnsupportedOperationException ();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException ();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public HDF4Product set(int productIndex, HDF4Product product) {
        if (productIndex > nProducts)
            throw new ArrayIndexOutOfBoundsException(
                    "Specified product index is out of range");
        else
            productList.set(productIndex, product);
        return productList.get(productIndex);
    }

    public int size() {
        return productList.size();
    }

    public List<HDF4Product> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException ();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException ();
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException ();
    }

    private int nProducts;

    public HDF4Products(final int nProducts) {
        productList = new ArrayList<HDF4Product>(nProducts);
        this.nProducts = nProducts;
    }

    public HDF4Product get(final String productName) {
        final int prodNum = nProducts;
        for (int i = 0; i < prodNum; i++) {
            final HDF4Product product = productList.get(i);
            if (product.getProductName().equals(productName))
                return product;
        }
        return null;
    }

    public class HDF4Product {
        private String productName;

        private int nBands;

        public HDF4Product(final String productName, final int nBands) {
            this.productName = productName;
            this.nBands = nBands;
        }

        public int getNBands() {
            return nBands;
        }

        public String getProductName() {
            return productName;
        }
    }

    public int getNProducts() {
        return nProducts;
    }
}
