package com.tempBank.bankManagement.dao;

import com.tempBank.bankManagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionsByAccountAccountId(Long accountId);

}
