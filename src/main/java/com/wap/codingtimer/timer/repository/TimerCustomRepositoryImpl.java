package com.wap.codingtimer.timer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wap.codingtimer.timer.domain.QTimer;
import com.wap.codingtimer.timer.domain.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimerCustomRepositoryImpl implements TimerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTimer timer = QTimer.timer;

    @Override
    public Optional<Timer> findRecentTimer(String memberId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(timer)
                        .where(timer.member.id.eq(memberId))
                        .orderBy(timer.date.desc())
                        .fetchOne());
    }
}
