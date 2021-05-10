package com.ssu.project.domain.social.kakao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoRepository extends JpaRepository <KakaoSocial, Long> {
    public KakaoSocial findByKakaoSocialId(String kakaoSocialId);
}
