package ru.nexignbootcamp2024.udr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nexignbootcamp2024.cdr.service.CdrTransactionGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReportServiceImplTest {
    @Value("${reports.rootDirectory}")
    private String reportsRootDirectory;

    @Autowired
    private CdrTransactionGenerator cdrTransactionGenerator;

    @Autowired
    private ReportService reportService;

    @Test
    void reportService_whenCallWithoutParam_thenCreateYearReport() throws IOException {
        Path dirPath = Paths.get(reportsRootDirectory + "/2023_total.json");
        Files.deleteIfExists(dirPath);

        reportService.generateReport();
        assertTrue(Files.exists(dirPath));
    }

    @Test
    void reportService_whenCallWithMsisdn_thenCreateYearReportForMsisdn() throws IOException {
        long msisdn = 79012345671L;
        Path dirPath = Paths.get(reportsRootDirectory + "/" + msisdn + "_total.json");
        Files.deleteIfExists(dirPath);

        reportService.generateReport(79012345671L);
        assertTrue(Files.exists(dirPath));
    }

    @Test
    void reportService_whenCallWithMsisdnAndMonth_thenCreateMonthReportForMsisdnAndMonth() throws IOException {
        long msisdn = 79012345671L;
        int month = 2;
        Path dirPath = Paths.get(reportsRootDirectory + "/" + month + "_" + msisdn + ".json");
        Files.deleteIfExists(dirPath);

        reportService.generateReport(79012345671L, 2);
        assertTrue(Files.exists(dirPath));
    }
}