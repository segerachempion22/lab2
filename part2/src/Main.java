import billing.ATVClub;
import model.ATV;
import model.UserATV;
import model.CorporateATV;
import model.ProtectiveGear;
import service.impl.EmailNotification;
import service.impl.SmsNotification;

public final class Main {
  private Main() {}

  public static void main(String[] args) {
    ProtectiveGear gear = new ProtectiveGear("Шлем и защита", 250.0);
    ATVClub smsClub = new ATVClub(new SmsNotification());
    ATV user = new UserATV("Иван", 1000.0, "Спорт");
    user.addGear(gear, 2);
    smsClub.processPayment(user, "+79991234567", user.calculateBillingPrice());
    ATVClub emailClub = new ATVClub(new EmailNotification());
    ATV corporate = new CorporateATV("Компания Турист", 1000.0, "Monster-Track", 5);
    corporate.addGear(gear);
    emailClub.processPayment(corporate, "office@example.com", corporate.calculateBillingPrice());
    ATV corrected = new CorporateATV("", -10.0, "Неизвестный", 0);
    System.out.println("Стоимость после проверок: " + corrected.calculateBillingPrice());
    smsClub.processPayment(user, "123", user.calculateBillingPrice());
    emailClub.processPayment(corporate, "invalid", corporate.calculateBillingPrice());
    ATVClub.printGlobalStats();
  }
}
