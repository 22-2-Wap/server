package com.wap.codingtimer.member.repository;

import com.wap.codingtimer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByNickname(String name);

    Member findByNickname(String nickName);

}
