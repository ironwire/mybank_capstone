package com.mybank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CARD_APPLICATIONS")
public class CardApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type_id", nullable = false)
    private CardType cardType;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "income", precision = 19, scale = 4)
    private BigDecimal income;

    @Column(name = "employment_status", length = 50)
    private String employmentStatus;

    @Column(name = "years_at_current_job")
    private Integer yearsAtCurrentJob;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    @Column(name = "requested_credit_limit", precision = 19, scale = 4)
    private BigDecimal requestedCreditLimit;

    @Column(name = "approved_credit_limit", precision = 19, scale = 4)
    private BigDecimal approvedCreditLimit;

    @Column(name = "processing_officer_id")
    private Integer processingOfficerId;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        applicationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}