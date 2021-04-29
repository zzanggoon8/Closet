package com.ssu.project.domain.cody;

import com.ssu.project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodyRepository extends JpaRepository<Cody, Long> {

    public List<Cody> findAllByMember(Member member);

    public List<Cody> findAll();

    public Optional<Cody> findById(Long id);

}