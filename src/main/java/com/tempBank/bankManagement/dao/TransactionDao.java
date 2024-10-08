package com.tempBank.bankManagement.dao;

import com.tempBank.bankManagement.model.Account;
import com.tempBank.bankManagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.beneficiary.beneficiaryId = :beneficiaryId")
    List<Transaction> findTransactionsByBeneficiaryId(Long beneficiaryId);
    List<Transaction> findTransactionsByAccountAccountId(Long accountId);

}
