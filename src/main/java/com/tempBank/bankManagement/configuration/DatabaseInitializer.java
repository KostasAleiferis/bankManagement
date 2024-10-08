package com.tempBank.bankManagement.configuration;

import com.tempBank.bankManagement.service.impl.AccountServiceImpl;
import com.tempBank.bankManagement.service.impl.BeneficiaryServiceImpl;
import com.tempBank.bankManagement.service.impl.TransactionServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseInitializer {

    private final BeneficiaryServiceImpl beneficiaryService;
    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;

    public DatabaseInitializer(BeneficiaryServiceImpl beneficiaryService, AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.beneficiaryService = beneficiaryService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostConstruct
    public void importData() {

        String beneficiariesCsvFilePath = "src/main/resources/beneficiaries.csv";
        String accountsCsvFilePath = "src/main/resources/accounts.csv";
        String transactionsCsvFilePath = "src/main/resources/transactions.csv";
        beneficiaryService.saveBeneficiariesFromCsv(beneficiariesCsvFilePath);
        accountService.saveAccountsFromCsv(accountsCsvFilePath);
        log.info("All Accounts have been saved successfully!");
        transactionService.saveTransactionsFromCsv(transactionsCsvFilePath);
        log.info("All Transactions have been saved successfully!");
    }

}
