package cc.redis.domain;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 2107298435272602166L;
	private int id;
    private String content;
    
	public Message(int id, String content) {
		super();
		this.id = id;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
}
