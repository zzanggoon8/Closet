package com.ssu.project.domain.social.google;

import com.ssu.project.domain.social.Social;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleSocialRepository extends JpaRepository<GoogleSocial, Long> {
    public GoogleSocial findByGoogleSocialId(String googleSocialId);
}
