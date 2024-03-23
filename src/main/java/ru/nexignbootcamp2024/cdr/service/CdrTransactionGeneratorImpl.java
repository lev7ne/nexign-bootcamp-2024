package ru.nexignbootcamp2024.cdr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction.TransactionType;
import ru.nexignbootcamp2024.cdr.repository.CdrTransactionRepository;
import ru.nexignbootcamp2024.subscriber.model.Subscriber;
import ru.nexignbootcamp2024.subscriber.repository.SubscriberRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrTransactionGeneratorImpl implements CdrTransactionGenerator {
    @Value("${cdr.rootDirectory}")
    private String cdrRootDirectory;
    private final SubscriberRepository subscriberRepository;
    private final CdrTransactionRepository cdrRepository;

    @Override
    @Transactional
    public void generateRandomCdrTransactions() {
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        List<Long> msisdns = subscriberList.stream().map(Subscriber::getMsisdn).toList();

        for (int i = 1; i <= 12; i++) {
            List<CdrTransaction> cdrTransactions = new ArrayList<>();
            for (Long msisdn : msisdns) {
                for (int j = 0; j < new Random().nextInt(8, 12); j++) {
                    Instant startTime = LocalDateTime.of(
                            2023,
                            i,
                            new Random().nextInt(1, 28),
                            new Random().nextInt(0, 24),
                            new Random().nextInt(0, 60),
                            new Random().nextInt(0, 60)).atZone(ZoneId.systemDefault()).toInstant();
                    Instant endTime = startTime.plusSeconds(new Random().nextLong(1L, 3600L));
                    CdrTransaction anyCdrTransaction = CdrTransaction.builder()
                            .msisdn(msisdn)
                            .startTime(startTime.getEpochSecond())
                            .endTime(endTime.getEpochSecond())
                            .transactionType(TransactionType.values()[new Random().nextInt(0, TransactionType.values().length)])
                            .build();
                    cdrTransactions.add(anyCdrTransaction);
                }
            }

            List<CdrTransaction> sortedCdrTransactions = cdrTransactions.stream()
                    .sorted(Comparator.comparing(CdrTransaction::getStartTime))
                    .toList();

            Path dirPath = Paths.get(cdrRootDirectory);
            Path cdrPath = Paths.get(cdrRootDirectory + "/" + i + "_2023.txt");
            try {
                if (!Files.exists(dirPath)) {
                    Files.createDirectory(dirPath);
                }
                if (!Files.exists(cdrPath)) {
                    Files.createFile(cdrPath);
                }
            } catch (IOException e) {
                log.error("Возникла ошибка при создании файла: " + cdrPath, e);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cdrPath.toString()))) {
                for (CdrTransaction cdrTransaction : sortedCdrTransactions) {
                    writer.write(cdrTransaction.toString());
                }

            } catch (IOException e) {
                log.error("Возникла ошибка при записи объекта: " + cdrPath, e);
            }
            cdrRepository.saveAll(cdrTransactions);
        }
    }

}
