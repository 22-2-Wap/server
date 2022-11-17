package com.wap.codingtimer.member.repository;

import com.wap.codingtimer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsMemberByNickname(String name);

    Member findByNickname(String nickName);
}
