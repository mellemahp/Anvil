package com.hmellema.anvil.core.operation;

import com.hmellema.anvil.core.exceptions.InvalidParameterException;
import com.hmellema.anvil.core.conversion.RequestDataConverter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Objects;

/**
 * Base handler for API routes defined by an OpenAPI operation.
 *
 * <p>The request/response Types should be auto-generated from the OpenAPI spec
 * to ensure responses follow the Api contract </p>
 *
 * @param <RequestTypeT>  Model type to convert to a json request into
 * @param <ResponseTypeT> Model type to convert to a json response
 */
public abstract class AbstractAnvilOperationHandler<RequestTypeT, ResponseTypeT> implements OperationHandler {
  private final RequestDataConverter<RequestTypeT> dataConverter;
  private final Validator validator;

  protected AbstractAnvilOperationHandler(Class<RequestTypeT> requestTypeClass) {
    Objects.requireNonNull(requestTypeClass, "requestTypeClass cannot be null.");
    this.dataConverter = new RequestDataConverter<>(requestTypeClass);
    validator  = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Override
  public void handle(RoutingContext routingContext) {
    var input = dataConverter.convertToInputType(routingContext);
    validateRequest(input);
    handleImpl(input, routingContext)
        .map(this::validateResponse)
        .map(this::mapResponse)
        .onFailure(routingContext::fail)
        .onSuccess(routingContext::end);
  }

  private void validateRequest(RequestTypeT request) {
    var violations = validator.validate(request);
    if (violations != null && !violations.isEmpty()) {
      var err = new InvalidParameterException("One or more invalid parameters.");
      for (var violation : violations) {
        err.putViolation(violation.getPropertyPath().toString(), violation.getMessage());
      }
      throw err;
    }
  }

  private ResponseTypeT validateResponse(ResponseTypeT response) {
    var violations = validator.validate(response);
    if (violations != null && !violations.isEmpty()) {
      var err = new InvalidParameterException("One or more invalid parameters.");
      for (var violation : violations) {
        err.putViolation(violation.getPropertyPath().toString(), violation.getMessage());
      }
      throw err;
    }
    return response;
  }

  private String mapResponse(ResponseTypeT result){
    return JsonObject.mapFrom(result).encodePrettily();
  }

  protected abstract Future<ResponseTypeT> handleImpl(RequestTypeT request, RoutingContext context);
}