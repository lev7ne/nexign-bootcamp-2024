package ru.nexignbootcamp2024.udr.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Класс описание объекта Udr с полями:
 * msisdn - идентификатор пользователя,
 * incomingCall - подробная информация о входящих вызовах,
 * outcomingCall - подробная информация об исходящих вызовах
 *
 * @see CallDetails
 */
@Getter
@Builder
public class Udr {
    private Long msisdn;
    private CallDetails incomingCall;
    private CallDetails outcomingCall;

}