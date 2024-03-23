package ru.nexignbootcamp2024.udr.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Udr {
    private Long msisdn;
    private CallDetails incomingCall;
    private CallDetails outcomingCall;

}