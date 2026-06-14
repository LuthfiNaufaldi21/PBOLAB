package com.crowdcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nominal donasi harus diisi")
    @Min(value = 1000, message = "Nominal donasi minimal Rp 1.000")
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "donated_at", nullable = false)
    private LocalDateTime donatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private UserEntity donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private CampaignEntity campaign;

    // ========================
    // Konstruktor
    // ========================

    public DonationEntity() {}

    public DonationEntity(Long amount, String message, UserEntity donor, CampaignEntity campaign) {
        this.amount = amount;
        this.message = message;
        this.donor = donor;
        this.campaign = campaign;
        this.donatedAt = LocalDateTime.now();
    }

    // ========================
    // Getter & Setter
    // ========================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDonatedAt() { return donatedAt; }
    public void setDonatedAt(LocalDateTime donatedAt) { this.donatedAt = donatedAt; }

    public UserEntity getDonor() { return donor; }
    public void setDonor(UserEntity donor) { this.donor = donor; }

    public CampaignEntity getCampaign() { return campaign; }
    public void setCampaign(CampaignEntity campaign) { this.campaign = campaign; }
}