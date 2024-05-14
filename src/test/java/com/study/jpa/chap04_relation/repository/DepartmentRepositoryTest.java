package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    /*
        JPA 에서 사용하는 엔터티들을 관리하는 역할을 수행하는 클래스. (영속성 컨텍스트를 관리함)
        영속성 컨텍스트: 엔터티를 영구 저장하는 환경 -> Spring Data JPA 에서만 사용하지 않음.
        영속성 컨텍스트 내의 내용들을 DB에 반영시키거나 비워내거나 수명을 관리할 수있는 객체 (commit, rollback, flush)
     */
    @Autowired
    EntityManager entityManager; // 스프링 핵심 객체임

    @Test
    @DisplayName("특정 부서를 조회하면 해당 부서원들도 함께 조회되어야 한다.")
    void testFindDept() {
        // given

        Long id = 2L;
        // when
        Department department = departmentRepository.findById(id).orElseThrow();

        // then
        System.out.println("\n\n\n");
        System.out.println("department = " + department);
        System.out.println("department.getEmployees = " + department.getEmployees());
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("Lazy 로딩과 Eager 로딩의 차이")
    void testLazyAndEager() {
        // 3번 사원을 조회하고 싶은데, 굳이 부서 정보는 필요없음.

        // given
        Long id = 3L;

        // when
        Employee employee = employeeRepository.findById(id)
                .orElseThrow();

        // then
        System.out.println("\n\n\n");
        System.out.println("employee = " + employee);
        System.out.println("dept_name = " + employee.getDepartment().getName());
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("양방향 연관관계에서 연관데이터의 수정")
    void testChangeDept() {
        // 1번 사원의 부서를 1번 부서에서 2번 부서로 변경해야함.
        // given
        Employee foundEmp = employeeRepository.findById(3L).orElseThrow();

        Department newDept = departmentRepository.findById(1L).orElseThrow();

        foundEmp.setDepartment(newDept);
//        newDept.getEmployees().add(foundEmp);

        employeeRepository.save(foundEmp);

        // 변경 감지(더티 체크) 후 변경된 내용을 DB에 즉시 반영하는 메서드
        entityManager.flush(); // DB로 밀어내기
        entityManager.clear(); // 영속성 컨텍스트 비우기(비우지 않으면 컨텍스트 내의 참조하려 하기때문)

        // when
        // 1번 부서 정보를 조회해서 모든 사원을 보겠다.
        Department foundDept = departmentRepository.findById(1L)
                .orElseThrow();
        // then
        System.out.println("\n\n\n");
        foundDept.getEmployees().forEach(System.out::println); // 2번 부서 모든사원정보 출력하라
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("N+1 문제 발생 예시")
    void testNPlusOneEx() {
        // given
        List<Department> departments = departmentRepository.findAll();
        // when
        departments.forEach(dept -> {
            System.out.println("\n\n========== 사원 리스트 ==========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");
        });

        // then
    }

    @Test
    @DisplayName("N+1 문제 해결 예시")
    void testNPlusOneSolution() {
        // given
        List<Department> departments = departmentRepository.findAllIncludesEmployees();
        // when
        departments.forEach(dept -> {
            System.out.println("\n\n========== 사원 리스트 ==========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");
        });

        // then
    }
}





















