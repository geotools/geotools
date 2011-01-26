package org.geotools.data.shapefile.indexed;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.index.CloseableIterator;

public class CloseableIteratorWrapper<E> implements CloseableIterator<E> {
    Iterator<E> delegate;

    public CloseableIteratorWrapper(Iterator<E> delegate) {
        this.delegate = delegate;
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public E next() {
        return delegate.next();
    }

    public void remove() {
        delegate.remove();
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

}
