package ru.nexignbootcamp2024.cdr.model;

import jakarta.persistence.AttributeConverter;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction.TransactionType;

@jakarta.persistence.Converter(autoApply = true)
public class TransactionalTypeConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType transactionType) {
        return transactionType.getValue();
    }

    @Override
    public TransactionType convertToEntityAttribute(String dbData) {
        return TransactionType.toTransactionType(dbData);
    }
}

