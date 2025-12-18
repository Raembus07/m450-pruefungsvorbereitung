package ch.orders;

import java.math.BigDecimal;

public interface IDiscountService {

  /**
   * @param order    order context
   * @param subTotal subtotal before discount
   * @return absolute discount amount (>= 0)
   */
  BigDecimal calculateDiscountAmount(Order order, BigDecimal subTotal);
}