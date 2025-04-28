package nycto.homeservices.service.serviceInterface;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendActivationEmail(String to, String token) throws MessagingException;
}
