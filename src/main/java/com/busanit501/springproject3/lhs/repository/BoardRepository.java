package com.busanit501.springproject3.lhs.repository;


import com.busanit501.springproject3.lhs.entity.Board;
import com.busanit501.springproject3.lhs.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// 인터페이스 이름 BoardRepository, 생성시 주의하고,
// JpaRepository<엔티티클래스명,pk(@Id)의 타입>
// 약속.
// 스프링 JPA 진영에서, 기본적인 쿼리 메소드를 만들어줌. crud
public interface BoardRepository extends JpaRepository<Board,Long> , BoardSearch {
  // 쿼리 스트링으로만, 통계 관련 쿼리 작성시, 문제점. 복잡함.
  Page<Board> findByTitleContainingOrderByBnoDesc(String title, String keyword, Pageable pageable);
  // 문장이 복잡해져서,
  // @Query 어노테이션 이용해서, 표준 SQL 구문으로 작성해보기, 단점, 정적이다.

  @Query("select  b from Board b where b.title  like concat('%',:keyword ,'%')")
  Page<Board> findByKeyword(String keyword, Pageable pageable);

  // 동적으로 작성이 가능하고, 컴파일러의 문법 체크 도구를 도움 받기.
  // QueryDSL 도구를 설치
  // JPQL -> SQL 같은 문법.
  // 단점, 또 새로 배워야 하네, 학습 부담감.

  // 해당 디비에서 만 존재하는 구문을 이용시,
  // nativeQuery = true 옵션을 이용한다.
  @Query(value = "select now()", nativeQuery = true)
  String getTime();

}













