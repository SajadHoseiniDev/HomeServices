package nycto.homeservices.service;

import nycto.homeservices.service.serviceInterface.CaptchaService;

public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public boolean validateCaptcha(String captchaToken) {
        return true;
    }
}
