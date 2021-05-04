package com.ssu.project.service.like;

import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.item.ItemRepository;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public boolean addLike(Member member, Long itemId) {
        if (member == null) {
            throw new UsernameNotFoundException("wrong user");
        }

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) {
            throw new IllegalArgumentException("wrong item info!");
        }

        member = memberRepository.getOne(member.getId());
        List<Item> likeList = member.getLikes();

        if (likeList.contains(item)) {
            likeList.remove(item);
            return false;
        }

        likeList.add(item);

        return true;
    }

    public List<Item> getLikeList(Member member) {
         return memberRepository.findByEmail(member.getEmail()).getLikes();
    }
}
