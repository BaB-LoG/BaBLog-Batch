package com.ssafy.bablog.batch.member;

import java.util.List;

public interface MemberIdProvider {
    List<Long> fetchAllMemberIds();
}
