package com.challenge.devchall.domain.point.entity;

import com.challenge.devchall.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Point extends BaseEntity {

    private Long currentPoint;
    private Long totalPoint;

    public void add(long cost){
        this.currentPoint+=cost;
        this.totalPoint+=cost;
        System.out.printf("참가 비용 %d 원이 추가되었습니다.", cost);
    }

    public void subtract(long cost){
        this.currentPoint-=cost;
        System.out.printf("참가 비용 %d 원이 지불되었습니다.", cost);

    }

}
