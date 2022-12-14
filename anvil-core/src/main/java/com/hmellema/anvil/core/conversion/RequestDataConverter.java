package com.hmellema.anvil.core.conversion;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Converts a json request to a RequestTypeT object.
 *
 * <p>The input Type should be auto-generated from the OpenAPI spec
 * to ensure responses follow the Api contract
 *
 * @param <RequestTypeT> Model type to convert JSON input into
 */
public class RequestDataConverter<RequestTypeT> {

  private final Class<RequestTypeT> inputTypeClass;

  public RequestDataConverter(Class<RequestTypeT> inputTypeClass) {
    this.inputTypeClass = Objects.requireNonNull(inputTypeClass, "inputTypeClass cannot be null.");
  }

  /**
   * Converts a JSON request into a RequestTypeT object.
   *
   * @param routingContext vert.x routing context
   * @return converted Request Object
   */
  public RequestTypeT convertToInputType(final RoutingContext routingContext) {
    return extractCombinedParams(routingContext).mapTo(inputTypeClass);
  }

  private JsonObject extractCombinedParams(final RoutingContext context) {
    JsonObject metadata = extractParameters(context);
    Optional
        .ofNullable(context.body().asJsonObject())
        .ifPresent(metadata::mergeIn);

    return metadata;
  }

  private JsonObject extractParameters(final RoutingContext context) {
    MultiMap allParams = MultiMap.caseInsensitiveMultiMap();
    allParams.addAll(context.queryParams());
    allParams.addAll(context.pathParams());

    return multiMapToJsonObject(allParams);
  }

  private JsonObject multiMapToJsonObject(final MultiMap multimap) {
    JsonObject jsonData = new JsonObject();
    for (Map.Entry<String, String> entry : multimap.entries()) {
      jsonData.put(entry.getKey(), entry.getValue());
    }
    return jsonData;
  }
}
