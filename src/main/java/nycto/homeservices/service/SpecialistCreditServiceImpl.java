package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.SpecialistCredit;
import nycto.homeservices.exceptions.CreditException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.SpecialistCreditRepository;
import nycto.homeservices.service.serviceInterface.SpecialistCreditService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialistCreditServiceImpl implements SpecialistCreditService {

    private final SpecialistCreditRepository specialistCreditRepository;


    @Override
    public void increaseSpecialistCredit(Specialist specialist, Long amount)
            throws NotFoundException{

        if (specialist == null)
            throw new NotFoundException("Specialist not found");

        if (amount == null || amount <= 0)
            throw new CreditException("Amount must be more than 0");


        SpecialistCredit credit = new SpecialistCredit();
        credit.setSpecialist(specialist);
        credit.setAmount(amount);

        specialistCreditRepository.save(credit);
    }


    @Override
    public void decreaseSpecialistCredit(Specialist specialist, Long amount) throws NotFoundException {
        if (specialist == null) {
            throw new NotFoundException("Specialist not found");
        }
        if (amount == null || amount <= 0) {
            throw new CreditException("Amount must be more than 0");
        }

        Long totalCredit = specialistCreditRepository.findTotalCreditBySpecialistId(specialist.getId());
        if (totalCredit == null || totalCredit < amount) {
            throw new CreditException("Insufficient credit");
        }

        SpecialistCredit credit = new SpecialistCredit();
        credit.setSpecialist(specialist);
        credit.setAmount(-amount);

        specialistCreditRepository.save(credit);
    }



}
