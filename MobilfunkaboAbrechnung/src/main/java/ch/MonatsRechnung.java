package ch;

public class MonatsRechnung {

  public static final double ANZAHL_GB_INBEGRIFFEN = 1.0;
  public static final double KOSTEN_PRO_ZUSAETZLICHES_GB = 2.0;
  public static final double MAXIMALE_KOSTEN = 20.0;

  public double berechneDatenkosten(double anzahlGbGebraucht) {
    if (anzahlGbGebraucht < 0.0) {
      throw new IllegalArgumentException("Negatives Datenvolumen");
    } else if (anzahlGbGebraucht <= ANZAHL_GB_INBEGRIFFEN) {
      return 0.0;
    } else {
      double kosten =
          (anzahlGbGebraucht - ANZAHL_GB_INBEGRIFFEN)
              * KOSTEN_PRO_ZUSAETZLICHES_GB;
      return Math.min(kosten, MAXIMALE_KOSTEN);
    }
  }
}