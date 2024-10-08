package com.tempBank.bankManagement.service.impl;

import com.tempBank.bankManagement.dao.AccountDao;
import com.tempBank.bankManagement.dao.TransactionDao;
import com.tempBank.bankManagement.enums.Type;
import com.tempBank.bankManagement.model.Account;
import com.tempBank.bankManagement.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl {

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private AccountDao accountDao;

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionDao.findTransactionsByAccountAccountId(accountId);
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
                        // Create a new Transaction
                        Transaction transaction = new Transaction();

                        // Fetch account by ID
                        Long accountId = Long.parseLong(transactionData[1]);
                        Account account = accountDao.findById(accountId)
                                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
                        transaction.setAccount(account); // Set the associated Account

                        // Set the amount
                        transaction.setAmount(Double.parseDouble(transactionData[2]));

                        // Set the type
                        transaction.setType(Type.valueOf(transactionData[3].toUpperCase())); // Enum conversion

                        // Set the date
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