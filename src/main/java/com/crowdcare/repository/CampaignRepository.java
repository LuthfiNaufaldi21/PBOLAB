package com.crowdcare.repository;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {

    List<CampaignEntity> findByStatus(String status);

    List<CampaignEntity> findByCreator(UserEntity creator);

    List<CampaignEntity> findByCategory(String category);

    List<CampaignEntity> findByStatusAndCategory(String status, String category);
}