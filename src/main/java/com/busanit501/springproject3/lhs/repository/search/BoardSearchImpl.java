package com.busanit501.springproject3.lhs.repository.search;

import com.busanit501.boot501.domain.Board;
import com.busanit501.boot501.domain.QBoard;
import com.busanit501.boot501.domain.QReply;
import com.busanit501.boot501.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

// Querydsl 사용하기 위한 조건
// 인터페이스 이름 + Impl
// 상속 : QuerydslRepositorySupport,
// BoardSearch 구현을 해야합니다.
@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

  public BoardSearchImpl() {
    super(Board.class);
  }

  @Override
  public Page<Board> search(Pageable pageable) {
    // Querydsl 이용하는 기본 문법
    QBoard board = QBoard.board;
    // select from board 하는 것과 비슷한 기능.
    // query -> sql 관련 문장을, 자바 문법으로 표현하고 있다.
    JPQLQuery<Board> query = from(board);

    //BooleanBuilder를 이용한 조건절 추가 해보기.
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.or(board.title.contains("1"));
    booleanBuilder.or(board.content.contains("1"));

    // where 조건절  where title like
//    query.where(board.title.contains("1"));
    // BooleanBuilder를 적용하기.
    query.where(booleanBuilder);
    // bno > 0 보다 큰 조건 넣을 경우
    query.where(board.bno.gt(0L));


    // 페이징 처리 적용하기.
    this.getQuerydsl().applyPagination(pageable, query);

    // 해당 조건에 맞는 데이터 가져오기
    List<Board> list = query.fetch();
    // 해당 조건에 맞는 데이터 갯수
    long count = query.fetchCount();
    return null;
  }

  @Override
  public Page<com.busanit501.springproject3.lhs.entity.Board> searchAll(String[] types, String keyword, Pageable pageable) {
    // Querydsl를 이용할 때는 Q 도메인을 이용한다.
    QBoard board = QBoard.board;
    JPQLQuery<Board> query = from(board);

    // 조건절을 자바 문법으로만 구성해서, 전달해보기.
    if ((types != null && types.length > 0) && keyword != null) {
      // BooleanBuilder , 조건절의 옵션을 추가하기 쉽게하는 도구.
      log.info("조건절 실행여부 확인 1 ");
      BooleanBuilder   booleanBuilder = new BooleanBuilder();
      //String[] types = {"t","w" }
      for (String type : types) {
        switch (type) {
          case "t":
            log.info("조건절 실행여부 확인 2 :  title");
            booleanBuilder.or(board.title.contains(keyword));
          case "w":
            log.info("조건절 실행여부 확인 2 :  writer");
            booleanBuilder.or(board.writer.contains(keyword));
          case "c":
            log.info("조건절 실행여부 확인 2 :  content");
            booleanBuilder.or(board.content.contains(keyword));
        } //switch
      } // end for
      // BooleanBuilder를 적용하기.
      query.where(booleanBuilder);
    } // end if


    // bno >0 보다 큰 조건.
    query.where(board.bno.gt(0L));

    // 페이징 처리 적용하기.
    this.getQuerydsl().applyPagination(pageable, query);

    // 해당 조건에 맞는 데이터 가져오기
    List<Board> list = query.fetch();
    // 해당 조건에 맞는 데이터 갯수
    long count = query.fetchCount();

    // 검색 조건, 페이징 처리 다하고, 재사용해야하니. 반환하기. 결과물.
    Page<Board> result = new PageImpl<>(list, pageable,count);

    return result;
  }

  //Left Join(Outer join) , 다른 테이블을 연결하는데.
  // 널인 데이터도 같이 붙이겠다.
  @Override
  public Page<com.busanit501.springproject3.lhs.dto.BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
    // Querydsl 버전에 조인하는 방법.
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    //Left Join(Outer join)
    //추가
    JPQLQuery<Board> query = from(board);
    query.leftJoin(reply).on(reply.board.eq(board));

    query.groupBy(board);
    ////추가

    //검색 조건, 페이징 조건도, 적용을해야함.
    // 조건절을 자바 문법으로만 구성해서, 전달해보기.
    if ((types != null && types.length > 0) && keyword != null) {
      // BooleanBuilder , 조건절의 옵션을 추가하기 쉽게하는 도구.
      log.info("조건절 실행여부 확인 1 ");
      BooleanBuilder   booleanBuilder = new BooleanBuilder();
      //String[] types = {"t","w" }
      for (String type : types) {
        switch (type) {
          case "t":
            log.info("조건절 실행여부 확인 2 :  title");
            booleanBuilder.or(board.title.contains(keyword));
            break;
          case "w":
            log.info("조건절 실행여부 확인 2 :  writer");
            booleanBuilder.or(board.writer.contains(keyword));
            break;
          case "c":
            log.info("조건절 실행여부 확인 2 :  content");
            booleanBuilder.or(board.content.contains(keyword));
            break;
        } //switch
      } // end for
      // BooleanBuilder를 적용하기.
      query.where(booleanBuilder);
    } // end if


    // bno >0 보다 큰 조건.
    query.where(board.bno.gt(0L));


    //Querydsl -> JPQL 를 이용해서 디비에 명령을 전달.
    // 편의기능, JPQL 이용해서, 해당 데이터의 결과를 자동으로 모델에 맵핑 해주는기능.
    // Projection 기능. , 자동으로 조회된 데이터 결과를 변환 해주기, DTO로

    ////추가
    JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
            Projections.bean(BoardListReplyCountDTO.class,
                    board.bno,
                    board.title,
                    board.writer,
                    board.regDate,
                    reply.count().as("replyCount"))
    );


    // 페이징 처리 적용하기.
    // query -> dtoQuery 수정.
    this.getQuerydsl().applyPagination(pageable, dtoQuery);

    // 위의 조건으로 디비에서 정보 가져오기.
    List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
    // 조회된 데이터의 갯수
    long count = dtoQuery.fetchCount();

    return new PageImpl<>(dtoList, pageable, count);

  }
}







