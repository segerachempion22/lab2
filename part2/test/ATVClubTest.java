import billing.ATVClub;
import model.ATV;
import model.UserATV;
import model.CorporateATV;
import model.ProtectiveGear;
import service.impl.EmailNotification;
import service.impl.SmsNotification;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class ATVClubTest {
  private static int checks;

  private static void equal(double expected, double actual) {
    checks++;
    if (!Double.isFinite(actual) || Math.abs(expected - actual) > 0.000001) {
      throw new AssertionError("Ожидалось " + expected + ", получено " + actual);
    }
  }

  private static void truth(boolean condition) {
    checks++;
    if (!condition) {
      throw new AssertionError("Условие не выполнено");
    }
  }

  public static void main(String[] args) {
    ProtectiveGear gear = new ProtectiveGear("Защита", 250);
    ATV user = new UserATV("Иван", 1000, "Спорт");
    ATV corporate = new CorporateATV("Компания", 1000, "Monster-Track", 5);
    equal(1500, user.calculateBillingPrice());
    equal(10500, corporate.calculateBillingPrice());
    equal(2000, user.addGear(gear, 2));
    equal(11750, corporate.addGear(gear));
    equal(2000, user.calculateFinalPrice());
    equal(2500, user.calculateFinalPrice(gear, 4));
    equal(1750, user.calculateFinalPrice(gear));
    equal(100, new UserATV("Иван", -1).getBasePrice());
    equal(100, new UserATV("Иван", Double.NaN).getBasePrice());
    equal(100, new UserATV("Иван", Double.POSITIVE_INFINITY).getBasePrice());
    equal(1, new CorporateATV("Компания", 1000, "Стандарт", 0).getActiveVehiclesCount());
    equal(1000, new UserATV("Иван", 1000, "Другой").calculateBillingPrice());
    equal(1000, new UserATV("Иван", 1000, null).calculateBillingPrice());
    truth(new UserATV("", 1000).getName().equals("Клиент"));
    equal(1750, user.addGear(gear, 0));
    equal(1, user.getGearHours());
    equal(1750, user.addGear(null));
    EmailNotification email = new EmailNotification();
    SmsNotification sms = new SmsNotification();
    truth(email.isValidRecipient("user@example.com"));
    truth(!email.isValidRecipient("bad"));
    truth(!email.isValidRecipient("@example.com"));
    truth(!email.isValidRecipient("a@b."));
    truth(!email.isValidRecipient("a b@example.com"));
    truth(!email.isValidRecipient(null));
    truth(sms.isValidRecipient("+79991234567"));
    truth(sms.isValidRecipient("89991234567"));
    truth(!sms.isValidRecipient("+7999123456"));
    truth(!sms.isValidRecipient("899912345678"));
    truth(!sms.isValidRecipient("8999123456x"));
    truth(!sms.isValidRecipient(null));
    int count = ATVClub.getTotalOrdersCount();
    double revenue = ATVClub.getTotalRevenue();
    ATVClub smsClub = new ATVClub(sms);
    ATVClub emailClub = new ATVClub(email);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream previous = System.out;
    try (PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8)) {
      System.setOut(capture);
      smsClub.processPayment(user, "+79991234567", user.calculateBillingPrice());
      emailClub.processPayment(corporate, "office@example.com", corporate.calculateBillingPrice());
      smsClub.processPayment(user, "bad", user.calculateBillingPrice());
      emailClub.processPayment(corporate, "bad", corporate.calculateBillingPrice());
      smsClub.processPayment(null, "+79991234567", 1000);
      smsClub.processPayment(user, "+79991234567", -1);
      smsClub.processPayment(user, "+79991234567", Double.NaN);
      smsClub.processPayment(user, "+79991234567", 1);
      email.sendNotification("Тест", "bad");
      sms.sendNotification("Тест", "bad");
    } finally {
      System.setOut(previous);
    }
    String log = output.toString(StandardCharsets.UTF_8);
    truth(log.contains("[SMS] На номер +79991234567"));
    truth(log.contains("[EMAIL] На адрес office@example.com"));
    truth(log.contains("оплата отклонена"));
    truth(log.contains("уведомление не отправлено"));
    equal(count + 2, ATVClub.getTotalOrdersCount());
    equal(revenue + 13500, ATVClub.getTotalRevenue());
    System.out.println("Успешно: " + checks + " проверок");
  }
}
