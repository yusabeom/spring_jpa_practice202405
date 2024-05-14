package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByName(String name); //동명이인이 있을수있으니 리스트로 받겠다!

    List<Student> findByCityAndMajor(String city, String major); // SELECT 절로

    List<Student> findByMajorContaining(String major); // LIKE 절로 변환

    // 네이티브 쿼리 사용
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :nm", nativeQuery = true)
    List<Student> findNameWithSQL(@Param("nm") String name);

    // native-sql
    // SELECT 컬럼명 FROM 테이블명
    // WHERE 컬럼 = ?

    // JPQL
    // SELECT 별칭 FROM 엔터티클래스명 AS 별칭
    // WHERE 별칭.필드명 = ?
    // SELECT st FROM Student AS st
    // WHERE st.name = ?

    // 도시이름으로 학생조회
    @Query(value = "SELECT st FROM Student AS st WHERE st.city = :ct")
    List<Student> getByCityWithJPQL(@Param("ct") String city);
    // @param 이름안줄려면 ?1 첫번째 파람이라고 표시가능


    /* native-sql 로 작성한거!
    @Query(value = "SELECT * FROM tbl_student WHERE city = ?1", nativeQuery = true )
    List<Student> getByCityWithJPQL(String city);
     */

}





















