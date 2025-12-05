package main.java;

/**
 * Strategie für die Implementierung der Währungsumrechnung:
 *
 * - Der Calculator bekommt ein IExchangeRateProvider-Objekt im Konstruktor,
 *   damit er den aktuellen USD→CHF-Wechselkurs von einer externen Quelle
 *   beziehen kann, ohne selbst von einer konkreten Implementierung abhängig zu sein.
 *
 * - Die Umrechnungsmethode im Calculator (z. B. convertUsdToChf) ruft zuerst den
 *   Wechselkurs beim Provider ab, multipliziert den Betrag in USD mit diesem Kurs
 *   und prüft danach, ob das Ergebnis noch im erlaubten Wertebereich (+/-10^12) liegt.
 *
 * - Der Provider selbst kann später z. B. harte Werte liefern, Konfigurationswerte
 *   verwenden oder eine externe API anfragen. Für Tests wird der Provider gemockt,
 *   damit der Calculator deterministisch getestet werden kann.
 *
 * - Bei ungültigen oder fehlenden Kursen, oder wenn Eingaben bzw. Ergebnis ausserhalb
 *   des Wertebereichs liegen, soll eine geeignete Exception geworfen werden.
 */
public interface IExchangeRateProvider {
  double getRate(Currency dst, Currency per);
}