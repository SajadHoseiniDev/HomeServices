package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.SpecialistCredit;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SpecialistCreditRepository;
import nycto.homeservices.util.ValidationUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialistCreditService {

    private final SpecialistCreditRepository specialistCreditRepository;
    private final ValidationUtil validationUtil;


    public void increaseSpecialistCredit(Specialist specialist, Long amount)
            throws NotFoundException, NotValidInputException {

        if (specialist == null)
            throw new NotFoundException("Specialist not found");

        if (amount == null || amount <= 0)
            throw new NotValidInputException("Amount must be more than 0");


        SpecialistCredit credit = new SpecialistCredit();
        credit.setSpecialist(specialist);
        credit.setAmount(amount);

        specialistCreditRepository.save(credit);
    }



}
