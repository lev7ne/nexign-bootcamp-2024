package ru.nexignbootcamp2024.cdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nexignbootcamp2024.cdr.model.CdrTransaction;

public interface CdrTransactionRepository extends JpaRepository<CdrTransaction, Long> {
}
