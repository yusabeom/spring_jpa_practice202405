package com.study.jpa.chap01_basic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // 엔터티로 쓸거임
@Table(name = "tbl_product") // 안적으면 클래스이름이 테이블이름이됨
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL 의 AUTO_INCREMENT
    @Column(name = "prod_id")
    private Long id; // 객체타입(null 을 받을수있음), 기본타입은 null 불가

    @Column(name = "prod_name", nullable = false, length = 30) // not Null
    private String name;

    private int price;
    @Enumerated(EnumType.STRING) // EnumType.ORDINAL 은 몇번쨰값인지
    private Category category;

    @CreationTimestamp // insert 되는 그시간이 defaultCurrent
    @Column(updatable = false) // insert 이후에는 수정불가
    private LocalDateTime createDate;

    @UpdateTimestamp // 자동으로 업데이트될때 시간 자동으로 들어감
    private LocalDateTime updateDate;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }

}