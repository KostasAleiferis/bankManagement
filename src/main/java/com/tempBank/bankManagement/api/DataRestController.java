package com.tempBank.bankManagement.api;

import com.tempBank.bankManagement.Exception.ResourceNotFoundException;
import com.tempBank.bankManagement.model.Account;
import com.tempBank.bankManagement.model.Beneficiary;
import com.tempBank.bankManagement.model.Transaction;
import com.tempBank.bankManagement.service.impl.AccountServiceImpl;
import com.tempBank.bankManagement.service.impl.BeneficiaryServiceImpl;
import com.tempBank.bankManagement.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class DataRestController {

    private final BeneficiaryServiceImpl beneficiaryService;
    private final AccountServiceImpl accountService;
    private final TransactionServiceImpl transactionService;

    public DataRestController(BeneficiaryServiceImpl beneficiaryService, AccountServiceImpl accountService, TransactionServiceImpl transactionService) {
        this.beneficiaryService = beneficiaryService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get a Beneficiary", description = "Fill in with a BeneficiaryId number",
                tags = {"getBeneficiaryById"})//for swagger documentation
    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        log.info("Get in getBeneficiaryById method with id: {}", id);
        Beneficiary beneficiary = beneficiaryService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with ID: " + id));
        return new ResponseEntity<>(beneficiary, HttpStatus.OK);
    }

    @Operation(summary = "Get the accounts of a beneficiary", description = "Fill in with a BeneficiaryId number",
               tags = {"getAccountsByBeneficiaryId"})//for swagger documentation
    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<Account>> getAccountsByBeneficiaryId(@PathVariable Long id) {
        log.info("Get in getAccountsByBeneficiaryId method with id: {}", id);

        // Check if the beneficiary exists
        ResponseEntity<Beneficiary> beneficiaryResponse = getBeneficiaryById(id);
        if (beneficiaryResponse.getStatusCode() == HttpStatus.OK) {
            Beneficiary beneficiary = beneficiaryResponse.getBody();

            // Get accounts for the existing beneficiary
            List<Account> accounts = accountService.getAccountsByBeneficiaryId(beneficiary.getBeneficiaryId());

            if (CollectionUtils.isEmpty(accounts)) {
                throw new ResourceNotFoundException("Accounts not found with BeneficiaryId: " + id);
            }
            return new ResponseEntity<>(accounts, HttpStatus.OK);

        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "", description = "Get the transactions of a beneficiary number",
                tags = {"getTransactionsByBeneficiaryId"})//for swagger documentation
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactionsByBeneficiaryId(@PathVariable Long id) {
        log.info("Get in getTransactionsByBeneficiaryId method with id: {}", id);

        ResponseEntity<List<Account>> accountResponse = getAccountsByBeneficiaryId(id);

        if (accountResponse.getStatusCode() == HttpStatus.OK) {
            // Get accounts from the response body
            List<Account> accounts = accountResponse.getBody();
            // Collect all transactions for the found accounts
            List<Transaction> transactions = new ArrayList<>();
            for (Account account : accounts) {
                transactions.addAll(transactionService.getTransactionsByAccountId(account.getAccountId()));
            }
            if (CollectionUtils.isEmpty(transactions)) {
                throw new ResourceNotFoundException("Transactions not found with BeneficiaryId: " + id);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    }

}
