package com.bgi.vtx;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

public class FilterChains extends LinkedList<Handler<RoutingContext>> implements Handler<RoutingContext> {


    public FilterChains(Handler<RoutingContext>... handlers) {
        super(Arrays.asList(handlers));
    }

    public FilterChains(Collection<? extends Handler<RoutingContext>> handlers) {
        super(handlers);
    }

    public FilterChains() {
        super();
    }

    @Override
    public void handle(RoutingContext event) {
        LinkedList<Handler<RoutingContext>> list = this;
        for (Handler<RoutingContext> handler : list) {
            handler.handle(event);
        }
    }
}
