package tests.test;

public class FloatTest {
  public FloatTest() {
  }

  /*
      float POSITIVE_INFINITY = 1.0f / 0.0f;
      float NEGATIVE_INFINITY = -1.0f / 0.0f;
      float NaN = 0.0f / 0.0f;
      float MAX_VALUE = 3.4028235e+38f;
      float MIN_VALUE = 1.4e-45f;
   */

  public static void main(String args[]) {
    test1();
    test2();
    test3();
    test4();
  }

  public static void test1() {
    float size = 0;
    float average = 10 / size;
    float infinity = 1.40e38f * 1.40e38f;
    System.out.println(average); // Prints Inf
    System.out.println(infinity); // Prints Inf
    System.out.println(infinity * 0); // Prints NaN
  }

  public static void test2() {
    float nan = Float.NaN;
    float positiveInfinity = Float.POSITIVE_INFINITY;
    float negativeInfinity = Float.NEGATIVE_INFINITY;

    // The following statements print false
    System.out.println(nan == nan);
    System.out.println(nan < nan);
    System.out.println(nan > nan);
    System.out.println(positiveInfinity < positiveInfinity
                       );

    // The following statements print true
    System.out.println(positiveInfinity == positiveInfinity);
    System.out.println(5.2 < positiveInfinity);
    System.out.println(5.2 > negativeInfinity);
  }

  public static void test3() {
    // FP literals are double type by default.
    // Append F or f to make float or cast to float
    float x = 5.1f;
    float y = 0.0f;
    System.out.println("\n" + "With: x = " + x + ",  y = " + y);

    float divByZero = x / y;
    System.out.println("Divide By Zero = x/y = " + divByZero);

    x = -1.0f;
    divByZero = x / y;
    System.out.println("Divide negative by zero = x/y = " + divByZero);

    x = 1.0e-45f;
    y = 1.0e-10f;
    float positiveUnderflow = x * y;
    System.out.println("Positive underflow = " + positiveUnderflow);

    x = -1.0e-45f;
    y = 1.0e-10f;
    float negativeUnderflow = x * y;
    System.out.println("Negative underflow = " + negativeUnderflow);

    x = 0.0f;
    y = 0.0f;
    float divZeroByZero = x / y;
    System.out.println("Divide zero by zero = " + divZeroByZero);

  }

  /* OUTPUT:
    Infinity
    Infinity
    NaN
    false
    false
    false
    false
    true
    true
    true
    Divide By Zero = x/y = Infinity
    Divide negative by zero = x/y = -Infinity
    Positive underflow = 0.0
    Negative underflow = -0.0
    Divide zero by zero = NaN
   */

  public static void test4() {
    double x = 0.0;
    double y = 1.0;
    double divResult = y/x;
    System.out.println("divResult: " + divResult);
    double result = Math.atan(divResult);
    System.out.println("Result: " + result);
  }
}
