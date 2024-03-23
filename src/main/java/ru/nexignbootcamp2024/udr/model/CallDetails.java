package ru.nexignbootcamp2024.udr.model;

import lombok.Data;

/**
 * Класс описание объекта CallDetails с полем totalTime - суммарное время для запрашиваемого периода,
 * в конструкторе приводится к формату, запрашиваемому в ТЗ
 */
@Data
public class CallDetails {
    private String totalTime;

    public CallDetails(Long totalTime) {
        this.totalTime = String.format("%02d:%02d:%02d", totalTime / 3600, (totalTime % 3600) / 60, (totalTime % 60));
    }

    @Override
    public String toString() {
        return "totalTime: " + totalTime;
    }
}