package com.crowdcare.config;

import com.crowdcare.entity.CampaignEntity;
import com.crowdcare.entity.DonationEntity;
import com.crowdcare.entity.UserEntity;
import com.crowdcare.repository.CampaignRepository;
import com.crowdcare.repository.DonationRepository;
import com.crowdcare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;

    public DataSeeder(UserRepository userRepository,
                      CampaignRepository campaignRepository,
                      DonationRepository donationRepository) {
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public void run(String... args) {

        // Hanya seed jika belum ada data
        if (userRepository.count() > 0) return;

        UserEntity admin = userRepository.save(new UserEntity(
                "USR-ADMIN-001",
                "Administrator",
                "admin",
                "admin",
                "ADMIN"
        ));

        UserEntity donor = userRepository.save(new UserEntity(
                "USR-DONOR-001",
                "Donatur",
                "user",
                "user",
                "DONOR"
        ));

        UserEntity fundraiser = userRepository.save(new UserEntity(
                "USR-FUNDRAISER-001",
                "Penggalang Dana",
                "penggalang",
                "penggalang",
                "FUNDRAISER"
        ));

        CampaignEntity pendidikan = new CampaignEntity(
                "Bantu Pendidikan Anak Desa",
                "Membantu menyediakan perlengkapan sekolah dan fasilitas belajar yang layak bagi anak-anak di desa terpencil.",
                10_000_000L,
                LocalDate.now().minusDays(18),
                LocalDate.now().plusDays(12),
                "Pendidikan",
                fundraiser
        );
        pendidikan.setStatus("APPROVED");
        pendidikan.setCollectedAmount(7_200_000L);

        CampaignEntity kesehatan = new CampaignEntity(
                "Bantuan Operasi untuk Raka",
                "Penggalangan dana untuk biaya operasi, perawatan, dan pemulihan Raka.",
                20_000_000L,
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(20),
                "Kesehatan",
                fundraiser
        );
        kesehatan.setStatus("APPROVED");
        kesehatan.setCollectedAmount(12_600_000L);

        CampaignEntity bencana = new CampaignEntity(
                "Bantuan Korban Banjir",
                "Pengadaan makanan, obat-obatan, dan kebutuhan harian untuk keluarga terdampak banjir.",
                25_000_000L,
                LocalDate.now().minusDays(7),
                LocalDate.now().plusDays(15),
                "Bencana",
                fundraiser
        );
        bencana.setStatus("APPROVED");
        bencana.setCollectedAmount(15_000_000L);

        CampaignEntity lingkungan = new CampaignEntity(
                "Gerakan Tanam Seribu Pohon",
                "Program penghijauan kawasan padat penduduk melalui penanaman dan perawatan bibit pohon.",
                8_000_000L,
                LocalDate.now().minusDays(4),
                LocalDate.now().plusDays(30),
                "Lingkungan",
                fundraiser
        );
        lingkungan.setStatus("APPROVED");
        lingkungan.setCollectedAmount(3_200_000L);

        CampaignEntity pending = new CampaignEntity(
                "Renovasi Rumah Singgah Pasien",
                "Merenovasi rumah singgah agar keluarga pasien luar kota memiliki tempat tinggal sementara yang layak.",
                18_000_000L,
                LocalDate.now(),
                LocalDate.now().plusDays(25),
                "Sosial",
                fundraiser
        );

        campaignRepository.save(pendidikan);
        campaignRepository.save(kesehatan);
        campaignRepository.save(bencana);
        campaignRepository.save(lingkungan);
        campaignRepository.save(pending);

        donationRepository.save(new DonationEntity(100_000L, "Semoga lancar.", donor, pendidikan));
        donationRepository.save(new DonationEntity(250_000L, "Untuk pendidikan anak-anak.", donor, pendidikan));
        donationRepository.save(new DonationEntity(150_000L, "Lekas pulih.", donor, kesehatan));

        // Variabel admin tetap dibuat agar akun default eksplisit dalam seed.
        if (admin != null) {
            System.out.println("[CrowdCare] Database H2 berhasil di-seed dengan akun, campaign, dan donasi default.");
        }
    }
}
