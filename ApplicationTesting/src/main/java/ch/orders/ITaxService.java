package ch.orders;

import java.math.BigDecimal;

public interface ITaxService {

  /**
   * @param countryCode ISO country code (e.g. "CH")
   * @return tax rate between 0 and 1 (e.g. 0.077 for 7.7%)
   */
  BigDecimal getTaxRate(String countryCode);
}