package ch.calc.expression;

import jakarta.annotation.Nonnull;

public interface IExpressionEvaluator {

  boolean expressionValidate(@Nonnull final String expression);

  double expressionEvaluate(@Nonnull final String expression);

}
