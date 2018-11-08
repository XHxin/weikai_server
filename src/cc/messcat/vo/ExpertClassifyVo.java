package cc.messcat.vo;

public class ExpertClassifyVo {

	private Long id;       
	private String name;     //专家分类名称
	private String icon;	 //图标
    private Integer sort;     //排序ID
    private String isSelect;   //是否被选中
    private String blueIcon;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	public String getBlueIcon() {
		return blueIcon;
	}
	public void setBlueIcon(String blueIcon) {
		this.blueIcon = blueIcon;
	}
	
}
