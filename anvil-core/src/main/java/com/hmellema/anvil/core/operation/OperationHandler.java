package com.hmellema.anvil.core.operation;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface OperationHandler extends Handler<RoutingContext> {
  String getOperationName();
}