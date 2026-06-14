package com.crowdcare.repository;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.DonationEntity;
import com.crowdcare.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY LAYER - Spring Data JPA
 *
 * Akses database untuk tabel DONATIONS.
 */
@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    /**
     * Riwayat donasi oleh donor tertentu
     */
    @EntityGraph(attributePaths = {"campaign", "donor"})
    List<DonationEntity> findByDonorOrderByDonatedAtDesc(UserEntity donor);

    /**
     * Semua donasi untuk campaign tertentu
     */
    @EntityGraph(attributePaths = {"campaign", "donor"})
    List<DonationEntity> findByCampaignOrderByDonatedAtDesc(CampaignEntity campaign);

    /**
     * Total donasi yang sudah terkumpul untuk campaign tertentu
     */
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM DonationEntity d WHERE d.campaign = :campaign")
    Long sumAmountByCampaign(@Param("campaign") CampaignEntity campaign);

    /**
     * Total donasi yang pernah dilakukan donor
     */
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM DonationEntity d WHERE d.donor = :donor")
    Long sumAmountByDonor(@Param("donor") UserEntity donor);
}
