package ru.nexignbootcamp2024.udr.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction.TransactionType;
import ru.nexignbootcamp2024.udr.model.CallDetails;
import ru.nexignbootcamp2024.udr.model.Udr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс с методами, отвечающими за основную функциональность, требуемую в рамках ТЗ,
 * создание отчетов (reports/) с разными параметрами
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Value("${reports.rootDirectory}")
    private String reportsRootDirectory;
    @Value("${cdr.rootDirectory}")
    private String cdrRootDirectory;

    /**
     * Создание годового отчета в директории (reports/) без передаваемых параметров,
     * вывод отчета в консоль
     */
    @Override
    public void generateReport() {
        List<Udr> udrList = new ArrayList<>();

        List<CdrTransaction> summaryCdrTransaction = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String stringCdrPath = cdrRootDirectory + "/" + i + "_2023.txt";
            List<CdrTransaction> cdrTransactionList = fromFileToCdrTransactionListWithoutMsisdn(stringCdrPath);
            summaryCdrTransaction.addAll(cdrTransactionList);
        }

        Map<Long, List<CdrTransaction>> summaryCdrTransactionMap = summaryCdrTransaction.stream()
                .collect(Collectors.groupingBy(CdrTransaction::getMsisdn));

        for (Map.Entry<Long, List<CdrTransaction>> entry : summaryCdrTransactionMap.entrySet()) {
            Long incoming = entry.getValue().stream()
                    .filter(cdrTransaction -> cdrTransaction.getTransactionType().equals(TransactionType.INCOMING))
                    .mapToLong(cdrTransaction -> Math.subtractExact(cdrTransaction.getEndTime(), cdrTransaction.getStartTime()))
                    .sum();
            Long outcoming = entry.getValue().stream()
                    .filter(cdrTransaction -> cdrTransaction.getTransactionType().equals(TransactionType.OUTCOMING))
                    .mapToLong(cdrTransaction -> Math.subtractExact(cdrTransaction.getEndTime(), cdrTransaction.getStartTime()))
                    .sum();
            udrList.add(Udr.builder()
                    .msisdn(entry.getKey())
                    .incomingCall(new CallDetails(incoming))
                    .outcomingCall(new CallDetails(outcoming))
                    .build());
        }

        String json = reportsWriter(reportsRootDirectory + "/2023_total.json", udrList);

        System.out.println(json);
    }

    /**
     * Создание годового отчета в директории (reports/) для конкретного пользователя,
     * вывод отчета в консоль
     *
     * @param msisdn
     */
    @Override
    public void generateReport(Long msisdn) {
        List<Udr> udrList = new ArrayList<>();

        List<CdrTransaction> summaryCdrTransaction = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String stringCdrPath = cdrRootDirectory + "/" + i + "_2023.txt";
            List<CdrTransaction> cdrTransactionList = fromFileToCdrTransactionListWithoutMsisdn(stringCdrPath);
            summaryCdrTransaction.addAll(cdrTransactionList);
        }
        udrConstructor(msisdn, udrList, summaryCdrTransaction);

        String json = reportsWriter(reportsRootDirectory + "/" + msisdn + "_total.json", udrList);

        System.out.println(json);
    }

    /**
     * Создание отчета в директории (reports/) для конкретного пользователя, в конкретном месяце,
     * вывод отчета в консоль
     * @param msisdn
     * @param month
     */
    @Override
    public void generateReport(Long msisdn, Integer month) {
        List<Udr> udrList = new ArrayList<>();

        String stringCdrPath = cdrRootDirectory + "/" + month + "_2023.txt";
        List<CdrTransaction> cdrTransactionListForMonth = fromFileToCdrTransactionListWithoutMsisdn(stringCdrPath);

        udrConstructor(msisdn, udrList, cdrTransactionListForMonth);

        String json = reportsWriter(reportsRootDirectory + "/" + month + "_" + msisdn + ".json", udrList);

        System.out.println(json);
    }

    /**
     * Утилитарный метод для фильтрации полученных из файла данных и сбора объектов для записи в отчет (reports/)
     *
     * @param msisdn
     * @param udrList
     * @param cdrTransactionListForMonth
     */
    private void udrConstructor(Long msisdn, List<Udr> udrList, List<CdrTransaction> cdrTransactionListForMonth) {
        List<CdrTransaction> cdrTransactionListForMonthForMsisdn = cdrTransactionListForMonth.stream()
                .filter(cdrTransaction -> cdrTransaction.getMsisdn().equals(msisdn))
                .toList();

        Long incoming = cdrTransactionListForMonthForMsisdn.stream()
                .filter(cdrTransaction -> cdrTransaction.getTransactionType().equals(TransactionType.INCOMING))
                .mapToLong(cdrTransaction -> Math.subtractExact(cdrTransaction.getEndTime(), cdrTransaction.getStartTime()))
                .sum();

        Long outcoming = cdrTransactionListForMonthForMsisdn.stream()
                .filter(cdrTransaction -> cdrTransaction.getTransactionType().equals(TransactionType.OUTCOMING))
                .mapToLong(cdrTransaction -> Math.subtractExact(cdrTransaction.getEndTime(), cdrTransaction.getStartTime()))
                .sum();

        udrList.add(Udr.builder()
                .msisdn(msisdn)
                .incomingCall(new CallDetails(incoming))
                .outcomingCall(new CallDetails(outcoming))
                .build());
    }

    /**
     * Утилитарный метод для чтения из файла в List<CdrTransaction>
     *
     * @param stringCdrPath
     * @return Помесячный список CdrTransactions
     */
    private List<CdrTransaction> fromFileToCdrTransactionListWithoutMsisdn(String stringCdrPath) {
        List<CdrTransaction> cdrTransactionList = new ArrayList<>();
        Path cdrPath = Paths.get(stringCdrPath);
        if (Files.exists(cdrPath)) {
            try {
                String data = Files.readString(cdrPath);
                cdrTransactionList = toCdrTransactionFromString(data);
            } catch (IOException e) {
                log.error("Ошибка при загрузке: " + cdrPath);
            }
        }
        return cdrTransactionList;
    }

    /**
     * Вспомогательный для утилитарного метода, отвечает за парсинга данных из строки в список объектов CdrTransaction
     *
     * @param value
     * @return Сформированный из строки список CdrTransaction
     */
    private List<CdrTransaction> toCdrTransactionFromString(String value) {
        List<CdrTransaction> cdrTransactionList = new ArrayList<>();
        String[] strings = value.split("\n");
        for (int i = 0; i < strings.length; i++) {
            String[] string = strings[i].split(",");
            CdrTransaction cdrTransaction = CdrTransaction.builder()
                    .transactionType(TransactionType.toTransactionType(string[0]))
                    .msisdn(Long.parseLong(string[1]))
                    .startTime(Long.parseLong(string[2]))
                    .endTime(Long.parseLong(string[3]))
                    .build();
            cdrTransactionList.add(cdrTransaction);
        }
        return cdrTransactionList.stream()
                .sorted(Comparator.comparing(CdrTransaction::getStartTime))
                .toList();
    }

    /**
     * Утилитарный метод создания файла в директории reports/ и записи в этот файл объектов udrList в формате .json
     *
     * @param reportsPathString
     * @param udrList
     * @return В случае успешной записи, возвращает записанный объект, иначе пустую строку
     */
    private String reportsWriter(String reportsPathString, List<Udr> udrList) {

        Path dirPath = Paths.get(reportsRootDirectory);
        Path reportsPath = Paths.get(reportsPathString);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
            if (!Files.exists(reportsPath)) {
                Files.createFile(reportsPath);
            }
        } catch (IOException e) {
            log.error("Возникла ошибка при создании файла: " + reportsPathString, e);
        }

        Gson gson = new Gson();
        String json = gson.toJson(udrList);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportsPathString))) {
            writer.write(json);
        } catch (IOException e) {
            log.error("Возникла ошибка при записи в файл: " + reportsPathString, e);
        }

        log.info("Данные успешно записаны в файл: " + reportsPathString);
        return json;
    }
}
