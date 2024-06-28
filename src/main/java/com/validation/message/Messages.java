package com.validation.message;

public class Messages {

	private String content;
	private String type;
	
	public Messages(String content,String type)
	{
		this.content=content;
		this.type=type;
	}

	@Override
	public String toString() {
		return "Messages [content=" + content + ", type=" + type + "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
