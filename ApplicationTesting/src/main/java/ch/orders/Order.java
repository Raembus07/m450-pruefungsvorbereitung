package ch.orders;

import java.util.List;

public record Order(
        List<OrderLine> lines,
        String countryCode,
        String couponCode
) { }