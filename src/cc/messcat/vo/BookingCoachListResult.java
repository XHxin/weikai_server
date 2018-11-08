package cc.messcat.vo;

import java.util.List;

public class BookingCoachListResult extends CommonResult {
	
	private List<BookingCoachVo> bookingCoachList;

	public List<BookingCoachVo> getBookingCoachList() {
		return bookingCoachList;
	}

	public void setBookingCoachList(List<BookingCoachVo> bookingCoachList) {
		this.bookingCoachList = bookingCoachList;
	}

}
