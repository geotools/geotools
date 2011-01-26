package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.RemoteOWS;
import org.geotools.styling.StyleFactory;

public class RemoteOWSBuilder<P> implements Builder<RemoteOWS> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private String service;

    private String onlineResource;

    public RemoteOWSBuilder() {
        this(null);
    }

    public RemoteOWSBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public String resource() {
        return onlineResource;
    }

    public RemoteOWSBuilder<P> resource(String onlineResource) {
        this.onlineResource = onlineResource;
        this.unset = false;
        return this;
    }

    public String service() {
        return service;
    }

    public RemoteOWSBuilder<P> service(String service) {
        this.service = service;
        this.unset = false;
        return this;
    }

    public RemoteOWS build() {
        if (unset) {
            return null;
        }
        RemoteOWS remote = sf.createRemoteOWS(service, onlineResource);
        return remote;
    }

    public P end() {
        return parent;
    }

    public RemoteOWSBuilder<P> reset() {
        unset = true;
        this.onlineResource = null;
        this.service = null;
        return this;
    }

    public RemoteOWSBuilder<P> reset(RemoteOWS remote) {
        if (remote == null) {
            return reset();
        }
        this.onlineResource = remote.getOnlineResource();
        this.service = remote.getService();
        unset = false;
        return this;
    }

    public RemoteOWSBuilder<P> unset() {
        unset = true;
        this.onlineResource = null;
        this.service = null;

        return this;
    }

}
