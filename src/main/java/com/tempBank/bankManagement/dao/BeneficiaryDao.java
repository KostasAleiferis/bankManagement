package com.tempBank.bankManagement.dao;

import com.tempBank.bankManagement.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryDao extends JpaRepository<Beneficiary, Long> {
}
