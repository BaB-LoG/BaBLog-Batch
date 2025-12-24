package com.ssafy.bablog.batch.member;

import com.ssafy.bablog.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryMemberIdProvider implements MemberIdProvider {

    private final MemberRepository memberRepository;

    public RepositoryMemberIdProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Long> fetchAllMemberIds() {
        return memberRepository.findAllIds();
    }
}
