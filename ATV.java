import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ATV {
  private final String name;
  private final double basePrice;
  private final String tariff;
  private ProtectiveGear appliedGear = null;
  private int gearHours;

  public ATV(String name, double basePrice) {
    this(name, basePrice, "Стандарт");
  }

  public ATV(String name, double basePrice, String tariff) {
    if (name == null || name.isBlank() || !Double.isFinite(basePrice) || basePrice <= 0) {
      throw new IllegalArgumentException("Нужны название и положительная конечная цена");
    }
    if (tariff == null) {
      throw new IllegalArgumentException("Тариф не должен быть null");
    }
    this.name = name.strip();
    this.basePrice = basePrice;
    this.tariff = tariff;
  }

  public String getName() {
    return name;
  }

  public double getBasePrice() {
    return basePrice;
  }

  public String getTariff() {
    return tariff;
  }

  public ProtectiveGear getAppliedGear() {
    return appliedGear;
  }

  public int getGearHours() {
    return gearHours;
  }

  public double calculateFinalPrice() {
    BigDecimal coefficient = switch (tariff) {
      case "Стандарт" -> BigDecimal.ONE;
      case "Спорт" -> new BigDecimal("1.5");
      case "Monster-Track" -> new BigDecimal("2.1");
      default -> throw new IllegalArgumentException("Неизвестный тариф: " + tariff);
    };
    double result = BigDecimal.valueOf(basePrice).multiply(coefficient)
        .setScale(2, RoundingMode.HALF_UP).doubleValue();
    if (!Double.isFinite(result)) {
      throw new IllegalArgumentException("Итоговая стоимость слишком велика");
    }
    return result;
  }

  public double calculateFinalPrice(ProtectiveGear gear) {
    return calculateFinalPrice(gear, 1);
  }

  public double calculateFinalPrice(ProtectiveGear gear, int hours) {
    if (gear == null || hours < 1) {
      throw new IllegalArgumentException("Нужны экипировка и положительное число часов");
    }
    BigDecimal result = BigDecimal.valueOf(calculateFinalPrice())
        .add(BigDecimal.valueOf(gear.getHourlyPrice()).multiply(BigDecimal.valueOf(hours)))
        .setScale(2, RoundingMode.HALF_UP);
    if (!Double.isFinite(result.doubleValue())) {
      throw new IllegalArgumentException("Итоговая стоимость слишком велика");
    }
    appliedGear = gear;
    gearHours = hours;
    return result.doubleValue();
  }

  public double addGear(ProtectiveGear gear) {
    return calculateFinalPrice(gear);
  }

  public double addGear(ProtectiveGear gear, int hours) {
    return calculateFinalPrice(gear, hours);
  }
}
