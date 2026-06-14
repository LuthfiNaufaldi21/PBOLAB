package com.crowdcare.repository;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {

    @EntityGraph(attributePaths = "creator")
    List<CampaignEntity> findByStatus(String status);

    @EntityGraph(attributePaths = "creator")
    List<CampaignEntity> findByCreator(UserEntity creator);

    List<CampaignEntity> findByCategory(String category);

    List<CampaignEntity> findByStatusAndCategory(String status, String category);

    @EntityGraph(attributePaths = "creator")
    @Query("SELECT c FROM CampaignEntity c WHERE c.id = :id")
    Optional<CampaignEntity> findWithCreatorById(@Param("id") Long id);
}
