package ch.orders;

import java.math.BigDecimal;

public interface IShippingService {

  BigDecimal getShippingCost(Order order, BigDecimal subTotalAfterDiscount);
}