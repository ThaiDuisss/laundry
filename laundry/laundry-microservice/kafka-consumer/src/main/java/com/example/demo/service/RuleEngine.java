package com.example.demo.service;

import com.example.demo.model.event.RuleHitEvent;
import com.example.demo.model.event.TransactionEvent;
import com.example.demo.repository.RuleState;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j(topic = "RuleRULE")
@Service
@RequiredArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class RuleEngine {
    final RuleState ruleState;
    final RuleHitProducer ruleHitProducer;
    @Value("${rules.max-transactions-last-minutes.id}")
    private String ruleId;

    @Value("${rules.max-transactions-last-minutes.minutes}")
    private Integer periodInMinutes;

    private Integer periodInMs;

    @Value("${rules.max-transactions-last-minutes.max-transactions}")
    private Integer maxTransactions;

    @PostConstruct
    public void init() {
        periodInMs = periodInMinutes * 60 * 1000;
    }

    @SneakyThrows
    public void process(TransactionEvent transaction) {
        processMaxTransactionsLastMinutes(transaction.getDebitAccount(), transaction);
        processMaxTransactionsLastMinutes(transaction.getCreditAccount(), transaction);
    }

    private void processMaxTransactionsLastMinutes(String accountNumber, TransactionEvent transactionEvent ){
        ruleState.addTransaction(accountNumber, transactionEvent.getTransactionId(), transactionEvent.getCreatedAt());
        long from = transactionEvent.getCreatedAt() - periodInMs;
        Integer count = ruleState.countTransactionsInRange(accountNumber, from, transactionEvent.getCreatedAt());
        log.info("count{}{}", count, maxTransactions);
        if(count >= maxTransactions) {
            var ruleHit = RuleHitEvent.builder()
                    .accountNumber(accountNumber)
                    .ruleId(ruleId)
                    .issuedAt(transactionEvent.getCreatedAt())
                    .transactionId(transactionEvent.getTransactionId())
                    .build();
            log.info("[{}] Account number {} hit the rule with transaction id {}", ruleId, accountNumber, transactionEvent.getTransactionId());
            ruleHitProducer.send(ruleHit);
        }
        CompletableFuture.runAsync(() -> ruleState.removeTransactionsBefore(transactionEvent.getTransactionId(), from));
    }

}
