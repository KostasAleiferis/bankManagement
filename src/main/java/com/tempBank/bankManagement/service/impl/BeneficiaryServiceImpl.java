package com.tempBank.bankManagement.service.impl;

import com.tempBank.bankManagement.Exception.ResourceNotFoundException;
import com.tempBank.bankManagement.dao.BeneficiaryDao;
import com.tempBank.bankManagement.model.Beneficiary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BeneficiaryServiceImpl {

    @Autowired
    private BeneficiaryDao beneficiaryDao;

    // Save beneficiaries from CSV
    public void saveBeneficiariesFromCsv(String filePath) {
        List<Beneficiary> beneficiaries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            // Read the CSV file line by line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) {
                    log.warn("Insufficient data in line: " + line);
                    continue;
                }
                String firstName = values[1];
                String lastName = values[2];

                // Create a new Beneficiary and add it to the list
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setFirstName(firstName);
                beneficiary.setLastName(lastName);
                beneficiaries.add(beneficiary);
            }

            // Save all beneficiaries to the database
            beneficiaryDao.saveAll(beneficiaries);
            log.info("All beneficiaries have been saved successfully!");

        } catch (IOException e) {
            log.error("Error reading the CSV file: " + e.getMessage());
        }
    }


    public Beneficiary getById(Long id) {
        return beneficiaryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with ID: " + id));
    }

}