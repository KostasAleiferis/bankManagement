package com.tempBank.bankManagement.service.impl;

import com.tempBank.bankManagement.Exception.ResourceNotFoundException;
import com.tempBank.bankManagement.dao.AccountDao;
import com.tempBank.bankManagement.dao.TransactionDao;
import com.tempBank.bankManagement.enums.Type;
import com.tempBank.bankManagement.model.Account;
import com.tempBank.bankManagement.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl {

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountServiceImpl accountService;

    public List<Transaction> getTransactionsByBeneficiaryId(Long beneficiaryId) {
        List<Account> accounts = accountService.getAccountsByBeneficiaryId(beneficiaryId);
        // Collect all transactions for the found accounts
        List<Transaction> transactions = new ArrayList<>();
        for (Account account : accounts) {
            transactions.addAll(transactionDao.findTransactionsByAccountAccountId(account.getAccountId()));
        }
        if (CollectionUtils.isEmpty(transactions)) {
            throw new ResourceNotFoundException("Transactions not found with BeneficiaryId: " + beneficiaryId);
        }
        return transactions;
    }

    public BigDecimal getBeneficiaryBalance(Long beneficiaryId) {
        List<Transaction> transactions = getTransactionsByBeneficiaryId(beneficiaryId);
        BigDecimal balance = BigDecimal.ZERO;
        // Calculate balance based on transaction type (deposit or withdrawal)
        for (Transaction transaction : transactions) {
            BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
            if (transaction.getType() == Type.DEPOSIT) {
                balance = balance.add(amount);  // Add for deposit
            } else if (transaction.getType() == Type.WITHDRAWAL) {
                balance = balance.subtract(amount);  // Subtract for withdrawal
            }
        }
        return balance;
    }

    public BigDecimal getMaxWithdrawalLastMonth(Long beneficiaryId) {

        List<Transaction> transactions = getTransactionsByBeneficiaryId(beneficiaryId);

        // Get the current date
        LocalDate now = LocalDate.now();

        // Get the first and last day of the previous month
        YearMonth lastMonth = YearMonth.from(now).minusMonths(1);
        LocalDate startOfMonth = lastMonth.atDay(1);
        LocalDate endOfMonth = lastMonth.atEndOfMonth();

        BigDecimal maxWithdrawal = BigDecimal.ZERO;

        // Iterate over the transactions to find the max withdrawal in the last month
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Type.WITHDRAWAL &&
                    transaction.getDate().isAfter(startOfMonth.minusDays(1)) &&
                    transaction.getDate().isBefore(endOfMonth.plusDays(1))) {

                BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
                // Update maxWithdrawal if the current transaction's amount is larger
                if (amount.compareTo(maxWithdrawal) > 0) {
                    maxWithdrawal = amount;
                }
            }
        }

        if (maxWithdrawal.equals(BigDecimal.ZERO)) {
            throw new ResourceNotFoundException("No withdrawal in the last month (" + lastMonth.getMonth().name() + ") for this Beneficiary Id: " + beneficiaryId);
        }
        return maxWithdrawal;
    }

    public void saveTransactionsFromCsv(String csvFilePath) {
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // Read the header line
            String headerLine = br.readLine();
            log.info("Header: " + headerLine); // Log the header

            // Process each line
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Check for empty lines

                    String[] transactionData = line.split(csvSplitBy);

                    try {
                        Transaction transaction = new Transaction();

                        Long accountId = Long.parseLong(transactionData[1]);
                        Account account = accountDao.findById(accountId)
                                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
                        transaction.setAccount(account); // Set the associated Account

                        transaction.setAmount(Double.parseDouble(transactionData[2]));

                        transaction.setType(Type.valueOf(transactionData[3].toUpperCase())); // Enum conversion

                        transaction.setDate(LocalDate.parse(transactionData[4], DateTimeFormatter.ofPattern("MM/dd/yy"))); // Adjust pattern as necessary

                        // Save the Transaction
                        transactionDao.save(transaction);
                    } catch (NumberFormatException e) {
                        log.error("Error parsing number in line: " + line + ", error: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid transaction type in line: " + line + ", error: " + e.getMessage());
                    } catch (DateTimeParseException e) {
                        log.error("Invalid date format in line: " + line + ", error: " + e.getMessage());
                    } catch (RuntimeException e) {
                        log.error("Error processing line: " + line + ", error: " + e.getMessage());
                    }
                } else {
                    log.warn("Empty line found, skipping."); // Log if an empty line is found
                }
            }
        } catch (IOException e) {
            log.error("Error reading CSV file: " + e.getMessage());
        }
    }

}