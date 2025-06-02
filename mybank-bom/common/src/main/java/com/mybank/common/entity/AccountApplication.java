package com.mybank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT_APPLICATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "initial_deposit", precision = 19, scale = 4)
    private BigDecimal initialDeposit;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @Column(name = "source_of_funds", length = 100)
    private String sourceOfFunds;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.applicationDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Pending";
        }
        if (this.initialDeposit == null) {
            this.initialDeposit = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}