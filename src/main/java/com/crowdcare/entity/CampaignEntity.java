package com.crowdcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul campaign tidak boleh kosong")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Target dana harus diisi")
    @Min(value = 10000, message = "Target dana minimal Rp 10.000")
    @Column(name = "target_amount", nullable = false)
    private Long targetAmount;

    @Column(name = "collected_amount", nullable = false)
    private Long collectedAmount = 0L;

    @NotNull(message = "Tanggal mulai harus diisi")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "Tanggal selesai harus diisi")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @NotBlank(message = "Kategori tidak boleh kosong")
    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    // ========================
    // Konstruktor
    // ========================

    public CampaignEntity() {}

    public CampaignEntity(String title, String description, Long targetAmount,
                          LocalDate startDate, LocalDate endDate,
                          String category, UserEntity creator) {
        this.title = title;
        this.description = description;
        this.targetAmount = targetAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.creator = creator;
        this.collectedAmount = 0L;
        this.status = "PENDING";
    }

    // ========================
    // Getter & Setter
    // ========================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getTargetAmount() { return targetAmount; }
    public void setTargetAmount(Long targetAmount) { this.targetAmount = targetAmount; }

    public Long getCollectedAmount() { return collectedAmount; }
    public void setCollectedAmount(Long collectedAmount) { this.collectedAmount = collectedAmount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public UserEntity getCreator() { return creator; }
    public void setCreator(UserEntity creator) { this.creator = creator; }
}