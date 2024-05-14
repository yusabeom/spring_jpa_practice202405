package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PageResponseDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // JPA 레파지토리는 트랜잭션 단위로 동작하기 때문에 작성해야함!
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;

    public PostListResponseDTO getPosts(PageDTO dto) {

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        // 데이터 베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pageable);

        // 게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();

        // 게시물 정보를 응답용 DTO 의 형태의 맞게 변환
        List<PostDetailResponseDTO> detailList = postList.stream()
                .map(PostDetailResponseDTO::new)
                .collect(Collectors.toList());

        // DB에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환.
        // 페이지 구성 정보와 위에있는 게시물 정보를 또다른 DTO 로 한번에 포장해서 리턴할 예정.
        // -> PostListResponseDTO 새롭게 만들예정
        return PostListResponseDTO.builder()
                .count(detailList.size()) // 총 게시물 수가 아니라 페이징에 의해 조회된 게시물의 개수
                .pageInfo(new PageResponseDTO(posts)) // JPA 가 준 페이지 정보가 담긴 객체를 DTO 에게 전달해서 그쪽으로 알고리즘 돌리게 시킴
                .posts(detailList)
                .build();

    }
}














































