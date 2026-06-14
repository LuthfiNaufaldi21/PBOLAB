package com.crowdcare.api;

import com.crowdcare.entity.DonationEntity;
import com.crowdcare.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> donate(@RequestBody Map<String, Object> body) {
        try {
            String donorId    = (String) body.get("donorId");
            Long campaignId   = Long.valueOf(body.get("campaignId").toString());
            Long amount       = Long.valueOf(body.get("amount").toString());
            String message    = (String) body.getOrDefault("message", "");

            DonationEntity donation = donationService.donate(donorId, campaignId, amount, message);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "donationId", donation.getId(),
                    "amount", donation.getAmount(),
                    "message", "Donasi berhasil disimpan."
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/history/{donorId}")
    public ResponseEntity<List<DonationEntity>> getDonationHistory(@PathVariable String donorId) {
        return ResponseEntity.ok(donationService.getDonationHistoryByDonor(donorId));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<DonationEntity>> getDonationsByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(donationService.getDonationsByCampaign(campaignId));
    }

    @GetMapping("/total/{donorId}")
    public ResponseEntity<Map<String, Object>> getTotalByDonor(@PathVariable String donorId) {
        Long total = donationService.getTotalDonationByDonor(donorId);
        return ResponseEntity.ok(Map.of("donorId", donorId, "total", total));
    }
}