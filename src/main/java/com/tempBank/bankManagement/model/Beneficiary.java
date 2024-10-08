package com.tempBank.bankManagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "BENEFICIARY")
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BENEFICIARY_ID", nullable = false)
    private Long beneficiaryId;
    private String firstName;
    private String lastName;
}
