package fr.malt.feesrulesengine.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java implementation of
 *
 * <p>https://github.com/mike182uk/timestring/blob/master/index.js
 */
@UtilityClass
public class TimeString {

  public long parseTimestring(String text) {
    return parseTimestring(text, Unit.MILLISECOND);
  }

  public long parseTimestring(String text, Unit returnUnit) {
    return parseTimestring(text, returnUnit, null);
  }

  /**
   * Parse a timestring
   *
   * @param text
   * @param returnUnit
   * @param opts
   * @return
   */
  public long parseTimestring(String text, Unit returnUnit, Options opts) {
    int totalSeconds = 0;
    EnumMap<Unit, Double> unitValues =
        getUnitValues(Optional.ofNullable(opts).orElse(new Options()));
    Matcher matcher =
        Pattern.compile("[-+]?([0-9.]+)([a-z]+)")
            .matcher(text.toLowerCase().replace("[^.\\w+-]+", ""));

    if (matcher.matches()) {
      for (int g = 1; g <= matcher.groupCount(); g++) {
        double value = Double.parseDouble(matcher.group(g++));
        String unit = matcher.group(g++);
        totalSeconds += getSeconds(value, unit, unitValues);
      }
    }

    if (returnUnit != null) {
      return convert(totalSeconds, returnUnit, unitValues);
    }

    return totalSeconds;
  }

  /**
   * Get unit values based on the passed options
   *
   * @param opts
   * @returns
   */
  public EnumMap<Unit, Double> getUnitValues(Options opts) {
    EnumMap<Unit, Double> unitValues = new EnumMap<>(Unit.class);
    unitValues.put(Unit.MILLISECOND, 0.001);
    unitValues.put(Unit.SECOND, 1D);
    unitValues.put(Unit.MINUTE, 60D);
    unitValues.put(Unit.HOUR, 3600D);

    unitValues.put(Unit.DAY, opts.hoursPerDay * unitValues.get(Unit.HOUR));
    unitValues.put(Unit.WEEK, opts.daysPerWeek * unitValues.get(Unit.DAY));
    unitValues.put(Unit.MONTH, (opts.daysPerYear / opts.monthsPerYear) * unitValues.get(Unit.DAY));
    unitValues.put(Unit.YEAR, opts.daysPerYear * unitValues.get(Unit.DAY));

    return unitValues;
  }

  /**
   * Get the key for a unit
   *
   * @param unit
   * @returns
   */
  private Unit getUnitKey(String unit) {
    for (Unit u : Unit.values()) {
      if (u.words.contains(unit)) {
        return u;
      }
    }
    throw new IllegalArgumentException("The unit [" + unit + "] is not supported by timestring");
  }

  /**
   * Get the number of seconds for a value, based on the unit
   *
   * @param value
   * @param unit
   * @param unitValues
   * @returns
   */
  private double getSeconds(double value, String unit, EnumMap<Unit, Double> unitValues) {
    return value * unitValues.get(getUnitKey(unit));
  }

  /**
   * Convert a value from its existing unit to a new unit
   *
   * @param value
   * @param unit
   * @param unitValues
   * @returns
   */
  private long convert(int value, Unit unit, EnumMap<Unit, Double> unitValues) {
    return Double.valueOf(value / unitValues.get(unit)).longValue();
  }

  public static void main(String[] args) {
    System.out.println(TimeString.parseTimestring("1month", Unit.DAY, new Options()));
  }

  /** Map of accepted strings to unit */
  public enum Unit {
    MILLISECOND("ms", "milli", "millisecond", "milliseconds"),
    SECOND("s", "sec", "secs", "second", "seconds"),
    MINUTE("m", "min", "mins", "minute", "minutes"),
    HOUR("h", "hr", "hrs", "hour", "hours"),
    DAY("d", "day", "days"),
    WEEK("w", "week", "weeks"),
    MONTH("mon", "mth", "mths", "month", "months"),
    YEAR("y", "yr", "yrs", "year", "years");
    private final List<String> words;

    Unit(String... word) {
      this.words = Arrays.asList(word);
    }
  }

  /** Default options to use when parsing a timestring */
  static class Options {
    private double hoursPerDay = 24;
    private double daysPerWeek = 7;
    private double weeksPerMonth = 4;
    private double monthsPerYear = 12;
    private double daysPerYear = 365.25;
  }
}
