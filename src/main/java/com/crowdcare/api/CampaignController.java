package com.crowdcare.api;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.service.CampaignService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignEntity>> getApprovedCampaigns() {
        return ResponseEntity.ok(campaignService.getApprovedCampaigns());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CampaignEntity>> getPendingCampaigns() {
        return ResponseEntity.ok(campaignService.getPendingCampaigns());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CampaignEntity>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaignById(@PathVariable Long id) {
        Optional<CampaignEntity> campaign = campaignService.getCampaignById(id);
        if (campaign.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(campaign.get());
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<List<CampaignEntity>> getCampaignsByCreator(@PathVariable String userId) {
        return ResponseEntity.ok(campaignService.getCampaignsByCreator(userId));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCampaign(@RequestBody Map<String, String> body) {
        try {
            String title       = body.get("title");
            String description = body.get("description");
            Long targetAmount  = Long.parseLong(body.get("targetAmount"));
            LocalDate startDate = LocalDate.parse(body.get("startDate"));
            LocalDate endDate   = LocalDate.parse(body.get("endDate"));
            String category    = body.get("category");
            String creatorId   = body.get("creatorId");

            CampaignEntity campaign = campaignService.createCampaign(
                    title, description, targetAmount,
                    startDate, endDate, category, creatorId
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", campaign.getId());
            response.put("title", campaign.getTitle());
            response.put("status", campaign.getStatus());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "Terjadi kesalahan."));
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveCampaign(@PathVariable Long id) {
        try {
            CampaignEntity campaign = campaignService.approveCampaign(id);
            return ResponseEntity.ok(Map.of("success", true, "status", campaign.getStatus()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectCampaign(@PathVariable Long id) {
        try {
            CampaignEntity campaign = campaignService.rejectCampaign(id);
            return ResponseEntity.ok(Map.of("success", true, "status", campaign.getStatus()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}