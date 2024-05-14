package com.study.jpa.chap03_pagination.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPageRepository extends JpaRepository<Student, String> {

    // 학생 조건 없이 전체 조회 페이징 (기본기능 - 안만들어도 됨)
    List<Student> findAll(Sort sort);

    // 학생의 이름에 특정 단어가 포함된 걸 조회 + 페이징
    Page<Student> findByNameContaining(String name, Pageable pageable);

}