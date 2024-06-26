package ru.nexignbootcamp2024.subscriber.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Класс описание объекта Subscriber с полями:
 * id - уникальный идентификатор в базе данных,
 * msisdn - идентификатор пользователя
 *
 * Содержит enum TransactionType incoming/outcoming
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriber")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long msisdn;
}
