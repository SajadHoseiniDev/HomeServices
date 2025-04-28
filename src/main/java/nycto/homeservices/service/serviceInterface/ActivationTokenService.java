package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.entity.ActivationToken;
import nycto.homeservices.entity.User;

public interface ActivationTokenService {
    ActivationToken createActivationToken(User user);

    ActivationToken validateToken(String token);

    void markTokenAsUsed(ActivationToken token);
}
