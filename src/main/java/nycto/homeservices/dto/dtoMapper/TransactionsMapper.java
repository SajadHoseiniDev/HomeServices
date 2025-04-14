package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.transactionsDto.TransactionsDto;
import nycto.homeservices.entity.Transactions;
import org.springframework.stereotype.Component;

@Component
public class TransactionsMapper {
    public TransactionsDto toDto(Transactions transactions) {
        return new TransactionsDto(
                transactions.getId(),
                transactions.getCustomer() !=null ? transactions.getCustomer().getId() : null,
                transactions.getSpecialist() != null ? transactions.getSpecialist().getId() : null,
                transactions.getCreditDeducted(),
                transactions.getCreditAdded(),
                transactions.getOrder() != null ? transactions.getOrder().getId() : null,
                transactions.getDescription(),
                transactions.getTransactionDate()
        );
    }
}
