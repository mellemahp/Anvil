package com.hmellema.anvil.core.exceptions;

import com.hmellema.anvil.core.annotations.HttpErrorCode;
import java.util.ArrayList;
import java.util.List;

@HttpErrorCode(code = 403)
public class InvalidParameterException extends RuntimeException {
    private final List<Violation> violations = new ArrayList<>();

    public InvalidParameterException(String message) {
      super(message);
    }

    public static class Violation {
      private String parameter;
      private String msg;

      public Violation(String parameter, String msg) {
        this.parameter = parameter;
        this.msg = msg;
      }

      public String getMsg() {
        return msg;
      }

      public String getParameter() {
        return parameter;
      }
    }

    public List<Violation> getViolations() {
      return this.violations;
    }

    public void putViolation(String parameter, String msg) {
      violations.add(new Violation(parameter, msg));
    }
}
