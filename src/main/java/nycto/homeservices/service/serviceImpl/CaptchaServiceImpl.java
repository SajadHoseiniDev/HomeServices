package nycto.homeservices.service.serviceImpl;

import nycto.homeservices.service.serviceInterface.CaptchaService;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public boolean validateCaptcha(String captchaToken) {
        return true;
    }
}
