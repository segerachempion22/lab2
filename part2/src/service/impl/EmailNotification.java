package service.impl;

import service.NotificationService;

public final class EmailNotification implements NotificationService {
  @Override
  public boolean isValidRecipient(String recipient) {
    if (recipient == null || recipient.isBlank()) {
      return false;
    }
    int at = recipient.indexOf('@');
    int dot = recipient.lastIndexOf('.');
    return at > 0 && at == recipient.lastIndexOf('@') && dot > at + 1
        && dot < recipient.length() - 1 && recipient.chars().noneMatch(Character::isWhitespace);
  }

  @Override
  public void sendNotification(String message, String recipient) {
    if (isValidRecipient(recipient)) {
      System.out.println("[EMAIL] На адрес " + recipient + " отправлено письмо: " + message);
    } else {
      System.out.println("Предупреждение: некорректный email, уведомление не отправлено");
    }
  }
}
