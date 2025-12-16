package ch.numberconverter;

import ch.lib.BaseMockitoUnitTest;
import ch.stub.StubStringConverter;
import org.junit.jupiter.api.*;

public class TestNumberConverter extends BaseMockitoUnitTest {

  private NumberConverter testee;

  @BeforeEach
  void setUp() {
    testee = new NumberConverter(new StubStringConverter());
  }

  @Nested
  class RoundUp {

    @Test
    @DisplayName("round up float 2.3 to float 3")
    void test1() {
      final var input = 2.3f;
      final var output = 3f;

      final var result = testee.roundUp(input);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test2() {
      final var input = -2.3f;
      final var output = -2f;

      final var result = testee.roundUp(input);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test3() {
      final var input = 2.0f;
      final var output = 2f;

      final var result = testee.roundUp(input);

      Assertions.assertEquals(output, result);
      Assertions.assertEquals(input, result);
    }

    @Test
    void test4() {
      final var input = -2.0f;
      final var output = -2f;

      final var result = testee.roundUp(input);

      Assertions.assertEquals(output, result);
      Assertions.assertEquals(input, result);
    }

    @Test
    void roundUp_boundaryAround2() {
      float below = Math.nextAfter(2.0f, 1.0f);
      float exact = 2.0f;
      float above = Math.nextAfter(2.0f, 3.0f);

      Assertions.assertEquals(2, testee.roundUp(below));
      Assertions.assertEquals(2, testee.roundUp(exact));
      Assertions.assertEquals(3, testee.roundUp(above));
    }

    @Test
    void roundUp_boundaryAroundMinus2() {
      float below = Math.nextAfter(-2.0f, -3.0f);
      float exact = -2.0f;
      float above = Math.nextAfter(-2.0f, -0.0f);

      Assertions.assertEquals(-2, testee.roundUp(below));
      Assertions.assertEquals(-2, testee.roundUp(exact));
      Assertions.assertEquals(-1, testee.roundUp(above));
    }

  }

  @Nested
  class RoundDown {

    @Test
    void test1() {
      final var input = 2.7f;
      final var output = 2f;

      final var result = testee.roundDown(input);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test2() {
      final var input = -2.7f;
      final var output = -3f;

      final var result = testee.roundDown(input);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test3() {
      final var input = 2.0f;
      final var output = 2f;

      final var result = testee.roundDown(input);

      Assertions.assertEquals(output, result);
      Assertions.assertEquals(input, result);
    }

    @Test
    void test4() {
      final var input = -2.0f;
      final var output = -2f;

      final var result = testee.roundDown(input);

      Assertions.assertEquals(output, result);
      Assertions.assertEquals(input, result);
    }

    @Test
    void roundDown_boundaryAround2() {
      float below = Math.nextAfter(2.0f, 1.0f);
      float exact = 2.0f;
      float above = Math.nextAfter(2.0f, 3.0f);

      Assertions.assertEquals(1, testee.roundDown(below));
      Assertions.assertEquals(2, testee.roundDown(exact));
      Assertions.assertEquals(2, testee.roundDown(above));
    }

    @Test
    void roundDown_boundaryAroundMinus2() {
      float below = Math.nextAfter(-2.0f, -3.0f);
      float exact = -2.0f;
      float above = Math.nextAfter(-2.0f, 0.0f);

      Assertions.assertEquals(-3, testee.roundDown(below));
      Assertions.assertEquals(-2, testee.roundDown(exact));
      Assertions.assertEquals(-2, testee.roundDown(above));
    }
  }

  @Nested
  class RoundToPowerOfTen_FloatValue {

    @Test
    void test1() {
      final var input = 136.2f;
      final var power = 1;
      final var output = 140f;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test2() {
      final var input = 136.2f;
      final var power = 2;
      final var output = 100f;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test3() {
      final var input = 136.2f;
      final var power = 3;
      final var output = 0f;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
      Assertions.assertNotEquals(input, result);
    }

    @Test
    void test4(){
      final var input = 125;
      final var power = -1;

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.roundToPowerOfTen(input, power));
    }
  }

  @Nested
  class RoundToPowerOfTen_StringValue {

    @Test
    void test1(){
      final var input = "siebenundzwanzig";
      final var power = 1;
      final var output = 30;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test2(){
      final var input = "siebenundzwanzig";
      final var power = 3;
      final var output = 0;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test3(){
      final var input = "zweimilliardeneinhundertsiebenundvierzigmillionenvierhundertdreiundachtzigtausendsechshundertsiebenundvierzig";
      final var power = 5;
      final var output = 2147483647;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
    }

    @Test
    void test4(){
      final var input = "minussiebenundzwanzig";
      final var power = 1;

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.roundToPowerOfTen(input, power));
    }

    @Test
    void test5(){
      final var input = "siebenundzwanzig";
      final var power = -2;

      Assertions.assertThrows(IllegalArgumentException.class, () -> testee.roundToPowerOfTen(input, power));
    }

    @Test
    void test6(){
      final var input = "invalid number string";
      final var power = 1;
      final var output = 0;

      final var result = testee.roundToPowerOfTen(input, power);

      Assertions.assertEquals(output, result);
    }
  }
}
