package service;

public interface NotificationService {
  boolean isValidRecipient(String recipient);

  void sendNotification(String message, String recipient);
}
