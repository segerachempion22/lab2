package model;

import java.math.BigDecimal;

public final class CorporateATV extends ATV {
  private final int activeVehiclesCount;

  public CorporateATV(String name, double basePrice, String tariff, int activeVehiclesCount) {
    super(name, basePrice, tariff);
    if (activeVehiclesCount < 1) {
      System.out.println("Предупреждение: количество квадроциклов исправлено на 1");
      this.activeVehiclesCount = 1;
    } else {
      this.activeVehiclesCount = activeVehiclesCount;
    }
  }

  public int getActiveVehiclesCount() {
    return activeVehiclesCount;
  }

  @Override
  public double calculateBillingPrice() {
    return roundPrice(pricePerVehicle().multiply(BigDecimal.valueOf(activeVehiclesCount)));
  }
}
