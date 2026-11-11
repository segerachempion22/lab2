package billing;

import model.ATV;
import service.NotificationService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ATVClub {
  private final NotificationService notificationService;
  private static int totalOrdersCount;
  private static double totalRevenue;

  public ATVClub(NotificationService notificationService) {
    if (notificationService == null) {
      throw new IllegalArgumentException("Сервис уведомлений обязателен");
    }
    this.notificationService = notificationService;
  }

  public void processPayment(ATV rental, String contact, double finalPrice) {
    if (rental == null || !Double.isFinite(finalPrice) || finalPrice <= 0) {
      System.out.println("Предупреждение: некорректная аренда или сумма, оплата отклонена");
      return;
    }
    if (Double.compare(finalPrice, rental.calculateBillingPrice()) != 0) {
      System.out.println("Предупреждение: сумма не совпадает со стоимостью аренды");
      return;
    }
    if (!notificationService.isValidRecipient(contact)) {
      System.out.println("Предупреждение: некорректный контакт, оплата отклонена");
      return;
    }
    BigDecimal revenue = BigDecimal.valueOf(totalRevenue).add(BigDecimal.valueOf(finalPrice))
        .setScale(2, RoundingMode.HALF_UP);
    if (!Double.isFinite(revenue.doubleValue())) {
      System.out.println("Предупреждение: превышен предел выручки, оплата отклонена");
      return;
    }
    notificationService.sendNotification("Аренда для " + rental.getName()
        + " оплачена: " + finalPrice + " руб.", contact);
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
}
