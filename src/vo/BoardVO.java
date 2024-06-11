package vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BoardVO {

	private BigDecimal no;
	private LocalDateTime date;
	private String writer;
	private String title;
	private String content;
	private int prePage;
	private int nextPage;
	
	public BigDecimal getNo() {
		return no;
	}
	public void setNo(BigDecimal no) {
		this.no = no;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
	
	@Override
	public String toString() {
		return "BoardVO [no=" + no + ", date=" + date + ", writer=" + writer + ", title=" + title + ", content="
				+ content + "]";
	}
	
}
