package cc.messcat.vo;

import java.util.List;

public class VehicleListResult extends CommonResult {
	
	public List<VehicleVo> vehicleList;

	public List<VehicleVo> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<VehicleVo> vehicleList) {
		this.vehicleList = vehicleList;
	}

}
