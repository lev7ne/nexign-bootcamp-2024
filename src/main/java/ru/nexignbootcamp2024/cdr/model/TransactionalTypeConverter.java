package ru.nexignbootcamp2024.cdr.model;

import jakarta.persistence.AttributeConverter;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction.TransactionType;

/**
 * Класс преобразователь JPA, используемый для преобразования enum TransactionType в строку
 * при сохранении ее в базе данных и наоборот при ее извлечении из базы данных
 *
 * @see jakarta.persistence.Converter
 */
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

