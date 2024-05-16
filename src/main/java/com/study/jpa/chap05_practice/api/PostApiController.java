package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "post API", description = "게시물 조회, 등록 및 수정, 삭제 api 입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;
    // 리소스: 게시물 (Post)
    /*   api 명세서
        게시물 목록 조회: /posts            - GET, param: (page, size)
        게시물 개별 조회: /posts/{id}       - GET
        게시물 등록:     /posts            - POST, payload: (writer, title, content, hashTags)
        게시물 수정:     /posts/           - PATCH
        게시물 삭제:     /posts/{id}       - DELETE
     */

    // 게시물 목록 조회: /posts            - GET, param: (page, size)
    @GetMapping
    public ResponseEntity<?> list(PageDTO pageDTO) {
        log.info("api/v1/posts?page={}&size={}", pageDTO.getPage(), pageDTO.getSize());

        PostListResponseDTO dto = postService.getPosts(pageDTO);

        return ResponseEntity.ok().body(dto);

    }

    // 게시물 개별 조회: /posts/{id}       - GET
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("/api/v1/posts/{}: ", id);

        try { // 정상 아이디일 경우
            PostDetailResponseDTO dto = postService.getDetailPost(id);
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) { // 에러
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 등록
    @Operation(summary = "게시물 등록", description = "게시물 작성 및 등록을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "writer", description = "게시물의 작성자 이름을 쓰세요!", example = "김뽀삐", required = true),
            @Parameter(name = "title", description = "게시물의 제목을 쓰세요!", example = "제목제목", required = true),
            @Parameter(name = "content", description = "게시물의 내용을 쓰세요!", example = "내용내용"),
            @Parameter(name = "hashTags", description = "게시물의 해시태그를 작성하세요!", example = "['하하', '호호']")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto,
            BindingResult result // 검증 에러 정보를 가진 객체
    ) {
        log.info("/api/v1/posts POST!! - payload: {}", dto);

        if (dto == null){
            return ResponseEntity
                    .badRequest()
                    .body("등록 게시물 정보를 전달해 주세요!");
        }


        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);

        if (fieldErrors != null) return fieldErrors;
        // 위에 존재하는 if 문을 모두 건너뜀 -> dto 가  null 아니고, 입력값 검증도 모두 통과함 -> service 에게 명령하자.
        try {
            PostDetailResponseDTO responseDTO = postService.insert(dto);

            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body("서버 에러 원인: " + e.getMessage());
        }
    }

    // 입력값 검증(Validation)의 결과를 처리해 주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidateResult(BindingResult result) {
        if (result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }

    // 게시물 수정
    // 전체 수정 PUT, 일부 수정 PATCH
    // 둘다 가능하게 받을수 있다 아래 맵핑처럼
    @Operation(summary = "게시글 수정", description = "게시물 수정을 담당하는 메서드 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 완료!",
                    content = @Content(schema = @Schema(implementation = PostDetailResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND!"),
    })
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> update(
            // json으로 오면 @RequestBody 달아줘야함
            // 입력값 검증은 @Validated
            @Validated @RequestBody PostModifyDTO dto,
            BindingResult result,
            HttpServletRequest request
    ) {
        log.info("/api/v1/posts {} - payload: {}", request.getMethod(), dto);

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;

        PostDetailResponseDTO responseDTO = postService.modify(dto);

        return ResponseEntity.ok().body(responseDTO);

    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("/api/v1/posts/{}: ", id);

        postService.delete(id);

        return ResponseEntity.ok("delScucess!");
    }

}
























