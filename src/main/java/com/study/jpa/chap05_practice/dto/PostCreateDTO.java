package com.study.jpa.chap05_practice.dto;

import com.study.jpa.chap05_practice.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDTO {

    // @NotNull -> null 만 허용하지 않음
    // @NotEmpty -> null, ""을 허용하지 않고, 공백" "은 허용.
    @NotBlank // -> null, "", " "모두를 허용하지 않음.
    @Size(min = 2, max = 5)
    private String writer;

    @NotBlank
    @Size(min = 1, max = 20)
    private String title;
    private String content;
    private List<String> hashTags;

    // dto를 엔터티로 변환하는 메서드
    public Post toEntity() {
        return Post
                .builder()
                .writer(writer)
                .title(title)
                .content(content)
//                .hashTags(hashTags) 해시태그는 여기서 넣는게 아님!
                .build();
    }

}
