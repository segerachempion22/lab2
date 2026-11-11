package model;

public final class UserATV extends ATV {
  public UserATV(String name, double basePrice) {
    this(name, basePrice, "Стандарт");
  }

  public UserATV(String name, double basePrice, String tariff) {
    super(name, basePrice, tariff);
  }

  @Override
  public double calculateBillingPrice() {
    return roundPrice(pricePerVehicle());
  }
}
