package com.crowdcare.service;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.UserEntity;
import com.crowdcare.repository.CampaignRepository;
import com.crowdcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public CampaignService(CampaignRepository campaignRepository, UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    public CampaignEntity createCampaign(String title, String description, Long targetAmount,
                                         LocalDate startDate, LocalDate endDate,
                                         String category, String creatorId) {

        UserEntity creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan."));

        if (!"FUNDRAISER".equals(creator.getRole())) {
            throw new IllegalArgumentException("Hanya Penggalang Dana yang bisa membuat campaign.");
        }

        if (targetAmount < 10000) {
            throw new IllegalArgumentException("Target dana minimal Rp 10.000.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Tanggal selesai tidak boleh sebelum tanggal mulai.");
        }

        CampaignEntity campaign = new CampaignEntity(title, description, targetAmount,
                startDate, endDate, category, creator);

        return campaignRepository.save(campaign);
    }

    public CampaignEntity approveCampaign(Long campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign tidak ditemukan."));

        campaign.setStatus("APPROVED");
        return campaignRepository.save(campaign);
    }

    public CampaignEntity rejectCampaign(Long campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign tidak ditemukan."));

        campaign.setStatus("REJECTED");
        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    public List<CampaignEntity> getApprovedCampaigns() {
        return campaignRepository.findByStatus("APPROVED");
    }

    @Transactional(readOnly = true)
    public List<CampaignEntity> getPendingCampaigns() {
        return campaignRepository.findByStatus("PENDING");
    }

    @Transactional(readOnly = true)
    public List<CampaignEntity> getCampaignsByCreator(String creatorId) {
        UserEntity creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan."));
        return campaignRepository.findByCreator(creator);
    }

    @Transactional(readOnly = true)
    public List<CampaignEntity> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CampaignEntity> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }
}