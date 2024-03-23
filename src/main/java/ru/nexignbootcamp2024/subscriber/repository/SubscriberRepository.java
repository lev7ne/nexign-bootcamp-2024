package ru.nexignbootcamp2024.subscriber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nexignbootcamp2024.subscriber.model.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}
