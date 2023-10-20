package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {

    @Query("select h from Hint h join fetch h.theme where h.theme.id =:themeId")
    List<Hint> findHintsByThemeId(Long themeId);
}
