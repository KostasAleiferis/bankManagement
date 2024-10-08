package com.tempBank.bankManagement.model;

import com.tempBank.bankManagement.enums.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID", nullable = false)
    private Long transactionId;
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;
    @Column(name = "AMOUNT", nullable = false)
    private double amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private Type type;
    @Column(name = "DATE", nullable = false)
    private LocalDate date;

}
