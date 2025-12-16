package ch.numberconverter;

import ch.lib.BaseMockitoUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class TestNumberConverterMockito extends BaseMockitoUnitTest {

  private NumberConverter testee;

  @Mock
  private IStringConverter stringConverter;

  @BeforeEach
  void setUp() {
    testee = new NumberConverter(stringConverter);
  }

  @Nested
  class TestConvertToWords {

    @Test
    void test1() {
      final var input = "siebenundzwanzig";
      final var precision = 1;
      final var output = 30;

      Mockito.when(stringConverter.convertToInt(input)).thenReturn(output);

      final var result = testee.roundToPowerOfTen(input, precision);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test2() {
      final var input = "minus vierhundert";
      final var precision = 2;
      final var output = -400;

      Mockito.when(stringConverter.convertToInt(input)).thenReturn(output);

      final var result = testee.roundToPowerOfTen(input, precision);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test3() {
      final var input = "einhundert";
      final var precision = 3;
      final var output = 1000;

      Mockito.when(stringConverter.convertToInt(input)).thenReturn(output);

      final var result = testee.roundToPowerOfTen(input, precision);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test4() {
      final var input = "null";
      final var precision = 5;
      final var output = 0;

      Mockito.when(stringConverter.convertToInt(input)).thenReturn(output);

      final var result = testee.roundToPowerOfTen(input, precision);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test5() {
      final var input = "invalid";
      final var precision = 2;

      Mockito.when(stringConverter.convertToInt(input)).thenThrow(new IllegalArgumentException());

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.roundToPowerOfTen(input, precision));
    }
  }

}
