public final class ProtectiveGear {
  private final String name;
  private double hourlyPrice;

  public ProtectiveGear(String name, double hourlyPrice) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Название экипировки не должно быть пустым");
    }
    this.name = name.strip();
    setHourlyPrice(hourlyPrice);
  }

  public String getName() {
    return name;
  }

  public double getHourlyPrice() {
    return hourlyPrice;
  }

  public void setHourlyPrice(double hourlyPrice) {
    if (!Double.isFinite(hourlyPrice) || hourlyPrice < 0) {
      throw new IllegalArgumentException("Цена экипировки должна быть конечной и неотрицательной");
    }
    this.hourlyPrice = hourlyPrice;
  }
}
