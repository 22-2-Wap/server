package com.wap.codingtimer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wap.codingtimer.domain.QTimer;
import com.wap.codingtimer.domain.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimerCustomRepositoryImpl implements TimerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTimer timer = QTimer.timer;

    @Override
    public Optional<Timer> findRecentTimer(Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(this.timer)
                        .orderBy(this.timer.date.desc())
                        .fetchOne());
    }
}
