package main;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import vo.BoardVO;

/*
위의 테이블을 작성하고 게시판을 관리하는
다음 기능들을 구현하시오.

기능 구현하기 ==> 전체 목록 출력, 새글작성, 수정, 삭제, 검색 
 
------------------------------------------------------------

게시판 테이블 구조 및 시퀀스

create table jdbc_board(
    board_no number not null,  -- 번호(자동증가)
    board_title varchar2(100) not null, -- 제목
    board_writer varchar2(50) not null, -- 작성자
    board_date date not null,   -- 작성날짜
    board_content clob,     -- 내용
    constraint pk_jdbc_board primary key (board_no)
);
create sequence board_seq
    start with 1   -- 시작번호
    increment by 1; -- 증가값
		
----------------------------------------------------------

// 시퀀스의 다음 값 구하기
//  시퀀스이름.nextVal

 */


public class BoardMain {
	
	
	private Scanner sc = new Scanner(System.in);
	final int pageSize = 5;
	private static SqlSessionFactory SqlSessionFactory = null; 
	
	public static void main(String[] args) {
		BoardMain obj = new BoardMain();
		obj.sqlSession();
		obj.start();
	}

	
	private void sqlSession() {
		
		try {
			Charset charset = Charset.forName("UTF-8");
			Resources.setCharset(charset);
			
			Reader rd = Resources.getResourceAsReader("config/mybatis-config.xml");
			
			SqlSessionFactory = new SqlSessionFactoryBuilder().build(rd);
			
			rd.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void start() {		
		String num;

		do {
			displayMenu();
			num = sc.nextLine();
			switch (num) {
				case "1":
					printAll();
					break;
				case "2":
					insertBoard();
					break;
				case "3":
					updateBoard();
					break;
				case "4":
					deleteBoard();
					break;
				case "5":
					searchBoard();
					break;
				case "6":
					close();
					break;
				default:
					System.out.println("메뉴를 잘못 입력했습니다. 다시 입력하세요");
			}
		} while (!num.equals("6"));
	}


	private void close() {
		System.out.println("접속을 종료합니다.");
	}


	private void searchBoard() {
		SqlSession session = SqlSessionFactory.openSession(true); 
		
		System.out.println("어떤 것으로 검색할까요?");
		System.out.println("1. 날짜");
		System.out.println("2. 작성자");
		System.out.println("3. 제목");
		System.out.println("4. 내용");
		System.out.println("5. 뒤로가기");
		System.out.println();
		
		String sel = sc.nextLine();
		
		if ( !(sel.equals("1")) && !(sel.equals("2")) && !(sel.equals("3")) && !(sel.equals("4")) ) {
			System.out.println("잘못된 입력입니다.");
			return;
		}
		
		if (sel.equals("1")) {
			// 날짜 검색
			
//			System.out.println("날짜를 입력해주세요. (예 : 2024.01.01)");
//			String dateSel = sc.nextLine();
			
			System.out.println("날짜를 입력해주세요.");
			System.out.print("연도 : ");
			String year = sc.nextLine();
			System.out.print("월 : ");
			String month = sc.nextLine();
			System.out.print("일 : ");
			String day = sc.nextLine();
			
			String dateSel = year +  month +  day;
			
			
//			String dateSel = year + "-" + month + "-" + day + " 00:00:00.000";
//			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//			LocalDateTime dateTime = LocalDateTime.parse(dateSel, format);
//	
//			BoardVO bv = new BoardVO();
//			bv.setDate(dateTime);
			
			
			try {
				List<BoardVO> dateSearch = session.selectList("Board.dateSearch", dateSel);
				
				System.out.println();
				System.out.println("======= 목록 보기 (날짜순 정렬) =======");
				for (BoardVO boardVO : dateSearch) {
					BigDecimal no = (BigDecimal) boardVO.getNo();
					
					LocalDateTime date = (LocalDateTime) boardVO.getDate();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String dateStr = date.format(formatter);
					
					String writter = (String) boardVO.getWriter();
					String title = (String) boardVO.getTitle();
					String content = (String) boardVO.getContent();
					
					System.out.println("No." + no + "\t[" + dateStr + "]\t[작성자] " + writter + "\t[제목] " + title + "\t[내용] " + content);
				}
			} catch (PersistenceException e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
			
			
		} else if (sel.equals("2")) {
			// 작성자 검색

			System.out.println("검색할 작성자를 입력해주세요.");
			String writterSel = "%" + sc.nextLine() + "%";
			
			BoardVO bv = new BoardVO();
			bv.setWriter(writterSel);
			
			
			try {
				List<BoardVO> writerSearch = session.selectList("Board.writerSearch", bv);
				
				System.out.println();
				System.out.println("======= 목록 보기 (날짜순 정렬) =======");
				for (BoardVO boardVO : writerSearch) {
					BigDecimal no = (BigDecimal) boardVO.getNo();
					
					LocalDateTime date = (LocalDateTime) boardVO.getDate();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String dateStr = date.format(formatter);
					
					String writer = (String) boardVO.getWriter();
					String title = (String) boardVO.getTitle();
					String content = (String) boardVO.getContent();
					
					System.out.println("No." + no + "\t[" + dateStr + "] [작성자] " + writer + "\t[제목] " + title + "\t[내용] " + content);
				}
			} catch (PersistenceException e) {
				e.printStackTrace();
			} finally {
				session.close();
			}

			
		} else if (sel.equals("3")) {
			// 제목 검색
			
			System.out.println("검색할 제목을 입력해주세요.");
			String titleSel = "%" + sc.nextLine() + "%";
			
			BoardVO bv = new BoardVO();
			bv.setTitle(titleSel);
			
			
			try {
				List<BoardVO> titleSearch = session.selectList("Board.titleSearch", bv);
				
				System.out.println();
				System.out.println("======= 목록 보기 (날짜순 정렬) =======");
				for (BoardVO boardVO : titleSearch) {
					BigDecimal no = (BigDecimal) boardVO.getNo();
					
					LocalDateTime date = (LocalDateTime) boardVO.getDate();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String dateStr = date.format(formatter);
					
					String writter = (String) boardVO.getWriter();
					String title = (String) boardVO.getTitle();
					String content = (String) boardVO.getContent();
					
					System.out.println("No." + no + "\t[" + dateStr + "]\t[작성자] " + writter + "\t[제목] " + title + "\t[내용] " + content);
				}
			} catch (PersistenceException e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
			
			
		} else if (sel.equals("4")) {
			// 내용 검색
			
			System.out.println("검색할 내용을 입력해주세요.");
			String contentSel = "%" + sc.nextLine() + "%";
			
			BoardVO bv = new BoardVO();
			bv.setContent(contentSel);
			
			
			try {
				List<BoardVO> contentSearch = session.selectList("Board.contentSearch", bv);
				
				System.out.println();
				System.out.println("======= 목록 보기 (날짜순 정렬) =======");
				for (BoardVO boardVO : contentSearch) {
					BigDecimal no = (BigDecimal) boardVO.getNo();
					
					LocalDateTime date = (LocalDateTime) boardVO.getDate();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String dateStr = date.format(formatter);
					
					String writter = (String) boardVO.getWriter();
					String title = (String) boardVO.getTitle();
					String content = (String) boardVO.getContent();
					
					System.out.println("No." + no + "\t[" + dateStr + "]\t[작성자] " + writter + "\t[제목] " + title + "\t[내용] " + content);
				}
			} catch (PersistenceException e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
			
			
		} else return;
	}


	private void deleteBoard() {
		SqlSession session = SqlSessionFactory.openSession(true); 
		
		System.out.println();
		System.out.println("삭제할 게시물을 확인하신 후, 1. 뒤로가기를 눌러주세요.");
		printAll();


		System.out.print("삭제할 게시글 번호를 선택하세요 : ");
		int num = sc.nextInt();
		BigDecimal no = new BigDecimal(num);
		sc.nextLine();
		
		System.out.println();
		System.out.println("정말로 삭제하시겠습니까?");
		System.out.println("1. 삭제\t2. 취소");
		int sel = sc.nextInt();
		sc.nextLine();
		
		if (sel==1) {
			BoardVO bv = new BoardVO(); 
			bv.setNo(no);
			
			
			try {
				int cnt = session.delete("Board.deleteBoard", bv);
				printResult(cnt);
			} catch (PersistenceException e) {
				e.printStackTrace();
			} finally {
				session.close();				
			}
			
			
		} else if (sel==2) { 
			System.out.println("삭제가 취소되었습니다.");
		}
	}


	private void updateBoard() {
		SqlSession session = SqlSessionFactory.openSession(true); 
		
		System.out.println();
		System.out.println("수정할 게시물을 확인하신 후, 1. 뒤로가기를 눌러주세요.");
		
		printAll();

		System.out.print("수정할 게시글 번호를 선택하세요 : ");
		int num = sc.nextInt();
		BigDecimal no = new BigDecimal(num);
		sc.nextLine();
		
		System.out.print(" 제목 : ");
		String title = sc.nextLine();
		
		System.out.print(" 내용 : ");
		String content = sc.nextLine();
		
		System.out.print(" 작성자(닉네임) : ");
		String writer = sc.nextLine();
		
		BoardVO bv = new BoardVO();
		bv.setTitle(title);
		bv.setWriter(writer);
		bv.setContent(content);
		bv.setNo(no);
		
		
		try {
			int cnt = session.update("Board.updateBoard", bv);
			printResult(cnt);
		} catch (PersistenceException e) {
			e.printStackTrace();
		} finally {
			session.close();				
		}
		
		
	}


	private void insertBoard() {
		SqlSession session = SqlSessionFactory.openSession(true); 
		
		System.out.println("게시판에 추가할 내용을 작성하세요.");
		System.out.println();
		
		System.out.print(" 제목 : ");
		String title = sc.nextLine();
		
		System.out.print(" 내용 : ");
		String content = sc.nextLine();
		
		System.out.print(" 작성자(닉네임) : ");
		String writer = sc.nextLine();
		
		BoardVO bv = new BoardVO();
		bv.setTitle(title);
		bv.setWriter(writer);
		bv.setContent(content);
		
		
		try {
			int cnt = session.insert("Board.insertBoard", bv);
			printResult(cnt);
		} catch (PersistenceException e) {
			e.printStackTrace();
		} finally {
			session.close();				
		}
		
		
	}


	private void printAll() {
		SqlSession session = SqlSessionFactory.openSession(true);
		
		System.out.println();
		System.out.println("======= 전체 목록 보기 (날짜순 정렬) =======");

		int page = 1;
		boolean end = false;

		
		try {
		
		while (true) {

			int prePage = (page-1) * pageSize +1;
			int nextPage = page * pageSize;

			
			BoardVO bv = new BoardVO();
			bv.setPrePage(prePage);
			bv.setNextPage(nextPage);
			

			List<BoardVO> printList =  session.selectList("Board.printAllBoard", bv);

			if (printList.isEmpty()) {
				System.out.println("마지막 페이지입니다.");
				System.out.println();
				page--;
				end = true;
			}

			if (!printList.isEmpty()) {
				end = false;
				for (BoardVO boardVO : printList) {
					BigDecimal no = (BigDecimal) boardVO.getNo();
					LocalDateTime date = (LocalDateTime) boardVO.getDate();
					String writter = (String) boardVO.getWriter();
					String title = (String) boardVO.getTitle();
					String content = (String) boardVO.getContent();
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String dateStr = date.format(formatter);

					System.out.println("No." + no + "\t[" + dateStr + "]\t[작성자] " + writter + "\t[제목] " + title + "\t[내용] " + content);
				}
				System.out.println();
				System.out.println(page + " 페이지");
				System.out.println();

				System.out.println("< 이전 페이지 : 다음페이지 >");
				System.out.println("1. 뒤로가기");
			}
			String sel = sc.nextLine();

			switch (sel) {
			case "<":
				if (page > 1 && end == true) break;
				if (page > 1) page--;
				break;
			case ">":
				page++;
				break;
			case "1":
				return;
			default:
				System.out.println("잘못 입력했습니다. 다시 입력하세요.");
				break;
			}
		}
		
		} catch (PersistenceException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		
	}
	
	
	public void printResult (int cnt) {
		System.out.println();
		if (cnt > 0) {
			System.out.println("(*°▽°*) 성공! (*°▽°*)");
		} else {
			System.out.println("(°ロ°) 실패! (°ロ°)");
		}
	}


	public void displayMenu() {
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 메 뉴 선 택 ===");
		System.out.println("  1. 전체 목록 출력");
		System.out.println("  2. 게시글 작성");
		System.out.println("  3. 게시글 수정");
		System.out.println("  4. 게시글 삭제");
		System.out.println("  5. 게시글 검색 ");
		System.out.println("  6. 종료");
		System.out.println("----------------------");
		System.out.print("원하는 메뉴를 선택하세요 >> ");
		System.out.println();
	}
}
