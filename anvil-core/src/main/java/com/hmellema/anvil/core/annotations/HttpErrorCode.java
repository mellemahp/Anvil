package com.hmellema.anvil.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: Add Java doc
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpErrorCode {
  /**
   * <p>Http error code to use when reporting a given exception to the
   * caller.</p>
   *
   * @return http error code to use for exception
   */
  int code();
}