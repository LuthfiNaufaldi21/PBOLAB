package com.crowdcare.service;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.DonationEntity;
import com.crowdcare.entity.UserEntity;
import com.crowdcare.repository.CampaignRepository;
import com.crowdcare.repository.DonationRepository;
import com.crowdcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public DonationService(DonationRepository donationRepository,
                           CampaignRepository campaignRepository,
                           UserRepository userRepository) {
        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    public DonationEntity donate(String donorId, Long campaignId, Long amount, String message) {

        UserEntity donor = userRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor tidak ditemukan."));

        if (!"DONOR".equals(donor.getRole())) {
            throw new IllegalArgumentException("Hanya Donatur yang bisa berdonasi.");
        }

        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign tidak ditemukan."));

        if (!"APPROVED".equals(campaign.getStatus())) {
            throw new IllegalArgumentException("Campaign belum disetujui untuk menerima donasi.");
        }

        if (amount < 1000) {
            throw new IllegalArgumentException("Nominal donasi minimal Rp 1.000.");
        }

        // Update total terkumpul di campaign
        campaign.setCollectedAmount(campaign.getCollectedAmount() + amount);
        campaignRepository.save(campaign);

        // Simpan record donasi
        DonationEntity donation = new DonationEntity(amount, message, donor, campaign);
        return donationRepository.save(donation);
    }

    @Transactional(readOnly = true)
    public List<DonationEntity> getDonationHistoryByDonor(String donorId) {
        UserEntity donor = userRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor tidak ditemukan."));
        return donationRepository.findByDonorOrderByDonatedAtDesc(donor);
    }

    @Transactional(readOnly = true)
    public List<DonationEntity> getDonationsByCampaign(Long campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign tidak ditemukan."));
        return donationRepository.findByCampaignOrderByDonatedAtDesc(campaign);
    }

    @Transactional(readOnly = true)
    public Long getTotalDonationByDonor(String donorId) {
        UserEntity donor = userRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor tidak ditemukan."));
        return donationRepository.sumAmountByDonor(donor);
    }
}