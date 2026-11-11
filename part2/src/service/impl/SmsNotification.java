package service.impl;

import service.NotificationService;

public final class SmsNotification implements NotificationService {
  @Override
  public boolean isValidRecipient(String recipient) {
    if (recipient == null) {
      return false;
    }
    if (recipient.startsWith("+7") && recipient.length() == 12) {
      return recipient.substring(1).chars().allMatch(Character::isDigit);
    } else if (recipient.startsWith("8") && recipient.length() == 11) {
      return recipient.chars().allMatch(Character::isDigit);
    }
    return false;
  }

  @Override
  public void sendNotification(String message, String recipient) {
    if (isValidRecipient(recipient)) {
      System.out.println("[SMS] На номер " + recipient + " отправлено сообщение: " + message);
    } else {
      System.out.println("Предупреждение: некорректный телефон, уведомление не отправлено");
    }
  }
}
