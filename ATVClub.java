import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ATVClub {
  private static int totalOrdersCount;
  private static double totalRevenue;

  private ATVClub() {}

  public static void registerPayment(double finalPrice) {
    if (!Double.isFinite(finalPrice) || finalPrice <= 0) {
      throw new IllegalArgumentException("Оплата должна быть положительной и конечной");
    }
    BigDecimal revenue = BigDecimal.valueOf(totalRevenue).add(BigDecimal.valueOf(finalPrice))
        .setScale(2, RoundingMode.HALF_UP);
    if (!Double.isFinite(revenue.doubleValue())) {
      throw new IllegalArgumentException("Суммарная выручка слишком велика");
    }
    totalOrdersCount++;
    totalRevenue = revenue.doubleValue();
  }

  public static int getTotalOrdersCount() {
    return totalOrdersCount;
  }

  public static double getTotalRevenue() {
    return totalRevenue;
  }

  public static void printGlobalStats() {
    System.out.printf("Оплаченных заказов: %d; выручка: %.2f руб.%n",
        totalOrdersCount, totalRevenue);
  }

  public static void main(String[] args) {
    ProtectiveGear gear = new ProtectiveGear("Шлем и защита", 250.0);
    ATV standard = new ATV("Иван", 1000.0);
    ATV sport = new ATV("Анна", 1000.0, "Спорт");
    ATV monster = new ATV("Олег", 1000.0, "Monster-Track");
    double standardPrice = standard.calculateFinalPrice();
    double sportPrice = sport.calculateFinalPrice(gear);
    double monsterPrice = monster.calculateFinalPrice(gear, 3);
    System.out.printf("Стандарт: %.2f; Спорт с защитой: %.2f; Monster-Track, защита на 3 часа: %.2f%n",
        standardPrice, sportPrice, monsterPrice);
    registerPayment(standardPrice);
    registerPayment(sportPrice);
    registerPayment(monsterPrice);
    System.out.printf("Перегрузки addGear: %.2f; %.2f%n", sport.addGear(gear), monster.addGear(gear, 3));
    printGlobalStats();
  }
}
