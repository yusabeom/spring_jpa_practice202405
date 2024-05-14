package com.study.jpa.chap04_relation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = {"employees"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_dept")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name", nullable = false)
    private String name;

    // 양방향 맵핑에서는 상대방 엔터티의 갱신에 관여할 수 없습니다.
    // 단순히 읽기 전용(조회)으로만 사용해야 합니다.
    // mappedBy 에는 상대방 엔터티의 조인되는 필드명을 작성.
    @OneToMany(mappedBy = "department") //관계를 맺고 있는 상대방 엔터티의 필드명
    private List<Employee> employees = new ArrayList<>(); // 초기화가 필요합니다. (NPE 방지)

}
