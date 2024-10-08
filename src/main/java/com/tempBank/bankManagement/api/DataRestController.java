package com.tempBank.bankManagement.api;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    @Operation(summary = "Get a Beneficiary", description = "Fill in with a Beneficiary Id number",
            tags = {"getBeneficiaryById"})//for swagger documentation
    @GetMapping("getBeneficiary/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        log.info("Get in getBeneficiaryById method with id: {}", id);
        Beneficiary beneficiary = beneficiaryService.getById(id);
        return new ResponseEntity<>(beneficiary, HttpStatus.OK);
    }

    @Operation(summary = "Get the accounts of a beneficiary", description = "Fill in with a Beneficiary Id number",
            tags = {"getAccountsByBeneficiaryId"})//for swagger documentation
    @GetMapping("/{id}/getAccounts")
    public ResponseEntity<List<Account>> getAccountsByBeneficiaryId(@PathVariable Long id) {

        log.info("Get in getAccountsByBeneficiaryId method with id: {}", id);


        List<Account> accounts = accountService.getAccountsByBeneficiaryId(id);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @Operation(summary = "Get the transactions of a beneficiary",
            description = "Fill in with a Beneficiary Id number",
            tags = {"getTransactionsByBeneficiaryId"})//for swagger documentation
    @GetMapping("/{id}/getTransactions")
    public ResponseEntity<?> getTransactionsByBeneficiaryId(@PathVariable Long id) {

        log.info("Get in getTransactionsByBeneficiaryId method with id: {}", id);

        List<Transaction> transactions = transactionService.getTransactionsByBeneficiaryId(id);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    @Operation(summary = "Get the balance of a beneficiary",
            description = "Fill in with a Beneficiary Id number",
            tags = {"getBeneficiaryBalance"})//for swagger documentation
    @GetMapping("/{id}/getBeneficiaryBalance")
    public ResponseEntity<BigDecimal> getBeneficiaryBalance(@PathVariable Long id) {

        log.info("Get in getBeneficiaryBalance method with id: {}", id);

        BigDecimal balance = transactionService.getBeneficiaryBalance(id);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @Operation(summary = "Get the biggest amount of withdrawn of a beneficiary of the last month",
            description = "Fill in with a Beneficiary Id number",
            tags = {"getMaxWithdrawalLastMonth"})//for swagger documentation
    @GetMapping("/{id}/getMaxWithdrawalLastMonth")
    public ResponseEntity<BigDecimal> getMaxWithdrawalLastMonth(@PathVariable Long id) {

        log.info("Get in getMaxWithdrawalLastMonth method with id: {}", id);

        BigDecimal balance = transactionService.getMaxWithdrawalLastMonth(id);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

}
