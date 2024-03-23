DROP TABLE IF EXISTS subscriber;
DROP TABLE IF EXISTS cdr_transaction;

CREATE TABLE subscriber
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    msisdn BIGINT                                  NOT NULL,
    CONSTRAINT subscriber_msisdn UNIQUE (msisdn)
);

INSERT INTO subscriber (msisdn)
VALUES (79012345671),
       (79123456782),
       (79234567893),
       (79345678904),
       (79456789015),
       (79567890126),
       (79678901237),
       (79789012348),
       (79890123459),
       (79901234560);


CREATE TABLE cdr_transaction
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    transaction_type VARCHAR(2)                              NOT NULL,
    msisdn           BIGINT                                  NOT NULL,
    start_time       BIGINT                                  NOT NULL,
    end_time         BIGINT                                  NOT NULL
);
