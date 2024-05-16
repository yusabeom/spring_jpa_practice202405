package com.study.jpa.chap05_practice.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter @Getter
@ToString(exclude = {"post"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_hash_tag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private Long id;

    private String tagName; // 해시태그 이름

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 참조부모 삭제시 자식도 같이 삭제!!!
    @JoinColumn(name = "post_no")
    private Post post;

}
