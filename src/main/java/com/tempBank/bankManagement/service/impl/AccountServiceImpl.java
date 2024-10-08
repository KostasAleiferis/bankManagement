package com.tempBank.bankManagement.service.impl;

import com.tempBank.bankManagement.Exception.ResourceNotFoundException;
import com.tempBank.bankManagement.dao.AccountDao;
import com.tempBank.bankManagement.model.Account;
import com.tempBank.bankManagement.model.Beneficiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private BeneficiaryServiceImpl beneficiaryService;

    public List<Account> getAccountsByBeneficiaryId(Long beneficiaryId) {
        beneficiaryService.getById(beneficiaryId);
        List<Account> accounts = accountDao.findByBeneficiaryBeneficiaryId(beneficiaryId);
        if (CollectionUtils.isEmpty(accounts)) {
            throw new ResourceNotFoundException("Accounts not found with BeneficiaryId: " + beneficiaryId);
        }
        return accounts;
    }

    // Save accounts from CSV
    public void saveAccountsFromCsv(String csvFilePath) {
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] accountData = line.split(csvSplitBy);
                if (accountData.length < 2) {
                    log.warn("Insufficient data in line: " + line);
                    continue; // Skip this line
                }

                try {
                    Long beneficiaryId = Long.parseLong(accountData[1]);
                    Beneficiary beneficiary = beneficiaryService.getById(beneficiaryId);

                    Account account = new Account();
                    account.setBeneficiary(beneficiary);
                    accountDao.save(account);
                } catch (NumberFormatException e) {
                    log.warn("Invalid beneficiary ID format in line: " + line);
                } catch (RuntimeException e) {
                    log.warn(e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}