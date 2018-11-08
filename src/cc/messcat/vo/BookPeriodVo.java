package cc.messcat.vo;

public class BookPeriodVo {
	private String bookTime;//预约时间段（1上午,2下午,3晚上)
	private String bookCount;//预约人数
	
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getBookCount() {
		return bookCount;
	}
	public void setBookCount(String bookCount) {
		this.bookCount = bookCount;
	}
	
	
  
}
