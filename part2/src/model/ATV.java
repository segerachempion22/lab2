package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class ATV {
  private static final double MIN_BASE_PRICE = 100.0;
  private final String name;
  private final double basePrice;
  private final String tariff;
  private ProtectiveGear appliedGear = null;
  private int gearHours;

  protected ATV(String name, double basePrice) {
    this(name, basePrice, "Стандарт");
  }

  protected ATV(String name, double basePrice, String tariff) {
    if (name == null || name.isBlank()) {
      System.out.println("Предупреждение: пустое имя заменено на 'Клиент'");
      this.name = "Клиент";
    } else {
      this.name = name.strip();
    }
    if (!Double.isFinite(basePrice) || basePrice <= 0) {
      System.out.println("Предупреждение: установлена минимальная цена 100.0 руб.");
      this.basePrice = MIN_BASE_PRICE;
    } else {
      this.basePrice = basePrice;
    }
    this.tariff = tariff == null ? "" : tariff;
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

  protected BigDecimal pricePerVehicle() {
    BigDecimal coefficient = switch (tariff) {
      case "Стандарт" -> BigDecimal.ONE;
      case "Спорт" -> new BigDecimal("1.5");
      case "Monster-Track" -> new BigDecimal("2.1");
      default -> {
        System.out.println("Предупреждение администратору: неизвестный тариф '" + tariff
            + "', применен коэффициент Стандарт");
        yield BigDecimal.ONE;
      }
    };
    BigDecimal result = BigDecimal.valueOf(basePrice).multiply(coefficient);
    if (appliedGear != null) {
      result = result.add(BigDecimal.valueOf(appliedGear.getHourlyPrice())
          .multiply(BigDecimal.valueOf(gearHours)));
    }
    return result;
  }

  protected double roundPrice(BigDecimal price) {
    return price.setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  public abstract double calculateBillingPrice();

  public double calculateFinalPrice() {
    return calculateBillingPrice();
  }

  public double calculateFinalPrice(ProtectiveGear gear) {
    return addGear(gear);
  }

  public double calculateFinalPrice(ProtectiveGear gear, int hours) {
    return addGear(gear, hours);
  }

  public double addGear(ProtectiveGear gear) {
    return addGear(gear, 1);
  }

  public double addGear(ProtectiveGear gear, int hours) {
    if (gear == null) {
      System.out.println("Предупреждение: экипировка не выбрана");
      return calculateBillingPrice();
    }
    if (hours < 1) {
      System.out.println("Предупреждение: срок аренды экипировки исправлен на 1 час");
      hours = 1;
    }
    appliedGear = gear;
    gearHours = hours;
    return calculateBillingPrice();
  }
}
