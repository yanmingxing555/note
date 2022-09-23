package com.fomp.note.util.http;

import java.io.Serializable;

/**
 * 封装httpClient响应结果
 */
public class HttpClientResult implements Serializable {
	//响应状态码
	private int code;
	//响应请求头
	private String header;
	//响应数据
	private String content;

	public HttpClientResult() {
	}
	public HttpClientResult(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public HttpClientResult(String content) {
		this.content = content;
	}
	public HttpClientResult(int code, String content) {
		this.code = code;
		this.content = content;
	}
	public HttpClientResult(int code, String content, String header) {
		super();
		this.code = code;
		this.content = content;
		this.header = header;
	}
	@Override
	public String toString() {
		return "HttpClientResult [code=" + code + //", header=" + header +
				", content=" + content+"]";
	}

}