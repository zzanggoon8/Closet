package com.ssu.project.domain.social;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Long> {
    public Social findBySocialId(String socialId);
}
