package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.entity.Specialist;
import nycto.homeservices.exceptions.NotFoundException;

public interface SpecialistCreditService {
    void increaseSpecialistCredit(Specialist specialist, Long amount)
            throws NotFoundException;

    void decreaseSpecialistCredit(Specialist specialist, Long amount) throws NotFoundException;
}
