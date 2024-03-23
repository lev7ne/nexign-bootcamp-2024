package ru.nexignbootcamp2024.cdr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс описание объекта CdrTransaction с полями:
 * id - уникальный идентификатор в базе данных,
 * transactionType - тип транзакции incoming/outcoming,
 * msisdn - идентификатор пользователя,
 * startTime - точное время начала транзакции в формате unix timestamp,
 * endTime - точное время окончания транзакции в формате unix timestamp
 *
 * Содержит enum TransactionType incoming/outcoming
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cdr_transaction")
public class CdrTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType transactionType;
    private Long msisdn;
    private Long startTime;
    private Long endTime;

    @Override
    public String toString() {
        return transactionType.toString() + "," + msisdn + "," + startTime + "," + endTime + "\n";
    }

    @Getter
    @AllArgsConstructor
    public enum TransactionType {
        INCOMING ("01"), OUTCOMING ("02");
        final String value;

        public static TransactionType toTransactionType(String string) {
            switch (string) {
                case "01" -> {
                    return INCOMING;
                }
                case "02" -> {
                    return OUTCOMING;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
