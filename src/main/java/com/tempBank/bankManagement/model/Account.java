package com.tempBank.bankManagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;
    @ManyToOne
    @JoinColumn(name = "BENEFICIARY_ID", nullable = false)
    private Beneficiary beneficiary;

}
