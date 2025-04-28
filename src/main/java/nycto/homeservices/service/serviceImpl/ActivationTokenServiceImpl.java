package nycto.homeservices.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.ActivationToken;
import nycto.homeservices.entity.User;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.ActivationTokenRepository;
import nycto.homeservices.service.serviceInterface.ActivationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenServiceImpl implements ActivationTokenService {

    private final ActivationTokenRepository tokenRepository;
    private static final int EXPIRY_HOURS = 24;

    @Override
    public ActivationToken createActivationToken(User user) {
        String token = UUID.randomUUID().toString();
        ActivationToken activationToken = ActivationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(EXPIRY_HOURS))
                .used(false)
                .build();

        return tokenRepository.save(activationToken);
    }

    @Override
    public ActivationToken validateToken(String token) {
        ActivationToken activationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid activation token: " + token));

        if (activationToken.isExpired()) {
            throw new IllegalStateException("Activation token has expired");
        }

        if (activationToken.isUsed()) {
            throw new IllegalStateException("Activation token has already been used");
        }

        return activationToken;
    }

    @Override
    public void markTokenAsUsed(ActivationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
