package cc.messcat.vo;

import java.util.List;

public class BookingListResult extends CommonResult {
	
	private List<BookingStudentVo> bookingList;

	public List<BookingStudentVo> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<BookingStudentVo> bookingList) {
		this.bookingList = bookingList;
	}

}
