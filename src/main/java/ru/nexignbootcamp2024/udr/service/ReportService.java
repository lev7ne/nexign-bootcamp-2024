package ru.nexignbootcamp2024.udr.service;

public interface ReportService {
    void generateReport();
    void generateReport(Long msisdn);

    void generateReport(Long msisdn, Integer month);
}
