package com.tempBank.bankManagement.dao;

import com.tempBank.bankManagement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

    List<Account> findByBeneficiaryBeneficiaryId(Long beneficiaryId);

}
