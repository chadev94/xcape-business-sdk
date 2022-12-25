package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAll();

    List<Theme> findThemesByMerchant(Merchant merchant);

    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE Theme set " +
                    "name = :name, mainImage = :mainImage, bgImage = :bgImage, price = :price, description = :desc, reasoning = :reasoning, observation = :observation, activity = :activity, teamwork = :teamwork, minPersonnel = :minPersonnel, maxPersonnel = :maxPersonnel, difficulty = :difficulty, genre = :genre, point = :point, youtubeLink = :youtubeLink, colorCode = :colorCode, hasXKit = :hasXKit, isCrimeScene = :isCrimeScene " +
                    "where id = :id"
    )
    void modifyThemeById(
            @Param(value = "id") Long id,
            @Param(value = "name")String name,
            @Param(value = "mainImage")String mainImage,
            @Param(value = "bgImage")String bgImage,
            @Param(value = "price")Long price,
            @Param(value = "desc")String desc,
            @Param(value = "reasoning")Integer reasoning,
            @Param(value = "observation")Integer observation,
            @Param(value = "activity")Integer activity,
            @Param(value = "teamwork")Integer teamwork,
            @Param(value = "minPersonnel")Integer minPersonnel,
            @Param(value = "maxPersonnel")Integer maxPersonnel,
            @Param(value = "difficulty")Integer difficulty,
            @Param(value = "genre")String genre,
            @Param(value = "point")String point,
            @Param(value = "youtubeLink")String youtubeLink,
            @Param(value = "colorCode")String colorCode,
            @Param(value = "hasXKit")Boolean hasXKit,
            @Param(value = "isCrimeScene")Boolean isCrimeScene
    );

    void deleteById(Long id);
}