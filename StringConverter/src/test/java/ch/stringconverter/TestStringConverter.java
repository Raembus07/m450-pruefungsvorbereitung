package ch.stringconverter;

import ch.lib.BaseMockitoUnitTest;
import ch.numberconverter.StringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestStringConverter extends BaseMockitoUnitTest {

  private StringConverter testee;

  @BeforeEach
  void setUp() {
    testee = new StringConverter();
  }

  @Nested
  class TestConvertToWords {

    @Test
    void test1() {
      final var input = "siebenundzwanzig";
      final var output = 27;

      final var result = testee.convertToInt(input);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test2() {
      final var input = "einhundertdreiundzwanzig";
      final var output = 123;

      final var result = testee.convertToInt(input);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test3() {
      final var input = "minus einhundert";
      final var output = -100;

      final var result = testee.convertToInt(input);

      Assertions.assertEquals(output, result);
    }
  }

}
