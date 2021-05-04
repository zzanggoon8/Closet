package com.ssu.project.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository <Member, Long> {
    @Query(value = "SELECT * FROM Member", nativeQuery = true)
    List<Member> findAllDesc();

    public boolean existsByEmail(String email);

    public Member findByEmail(String email);

    public Member findByEmailAndPassword(String email, String password);
}
