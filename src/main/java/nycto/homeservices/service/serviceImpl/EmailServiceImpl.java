package nycto.homeservices.service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.service.serviceInterface.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.activation.base-url}")
    private String activationBaseUrl;

    @Override
    public void sendActivationEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("activating email");
        helper.setText(
                "<h1>welcome!</h1>" +
                        "<p>click on this link to activate your email :</p>" +
                        "<a href=\"" + activationBaseUrl + "?token=" + token + "\">activation account</a>" +
                        "<p>This link can only be used once and is valid for 24 hours.</p>",
                true
        );

        mailSender.send(message);
    }
}
