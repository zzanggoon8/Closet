package com.ssu.project.service.cody;

import com.ssu.project.domain.cody.Cody;
import com.ssu.project.domain.cody.CodyForm;
import com.ssu.project.domain.cody.CodyRepository;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodyService {

    private final CodyRepository codyRepository;
    private final MemberRepository memberRepository;

    public Cody createNewCody(Member member, CodyForm codyForm) {

        if (codyForm.getTopId()==null) {
            codyForm.setTopId(0L);
        }

        Cody cody = Cody.builder()
                .member(member)
                .topId(codyForm.getTopId())
                .bottomId(codyForm.getBottomId())
                .shoesId(codyForm.getShoesId())
                .backgroundId(codyForm.getBackgroundId())
                .topSize(codyForm.getTopSize())
                .bottomSize(codyForm.getBottomSize())
                .shoesSize(codyForm.getShoesSize())
                .build();

        if (codyForm.getOuterId()!=null) {
            cody.setOuterId(codyForm.getOuterId());
            cody.setOuterSize(codyForm.getOuterSize());
        }

        if (codyForm.getAccId()!=null) {
            cody.setAccId(codyForm.getAccId());
            cody.setAccSize(codyForm.getAccSize());
        }

        codyRepository.save(cody);

        return cody;

    }

    public List<Cody> getCodyList(Member member) {
        List<Cody> codyList = codyRepository.findAllByMember(member);

        return codyList;
    }

    public List<Cody> getAllList() {
        List<Cody> codyList = codyRepository.findAll();

        return codyList;
    }

//    @Transactional
//    public boolean addCodyLike(Member member, Long codyId) {
//        if (member == null){
//            throw new UsernameNotFoundException("wrong user");
//        }
//        Optional<Cody> optional = codyRepository.findById(codyId);
//        Cody cody = optional.get();
//
//        // codyId의 유효성 판별
//        if(cody == null){
//            throw new IllegalArgumentException("wrong cody info!");
//        }
//
//        // member는 detach 상태 -> Repo를 통해 Select문으로 한 번 조회해야 한다.
//        member = memberRepository.getOne(member.getId()); // detach -> persist 상태로 변환된다.
//        List<Cody> codyLikeList = member.getCodyLikes();
//
//        if(codyLikeList.contains(cody)){
//            codyLikeList.remove(cody);
//            return false;
//        }
//
//        codyLikeList.add(cody);
//        return true;
//    }
}