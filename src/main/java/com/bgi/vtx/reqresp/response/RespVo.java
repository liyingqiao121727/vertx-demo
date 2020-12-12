package com.bgi.vtx.reqresp.response;

import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.LinkedList;

public class RespVo {

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";

	@SwaggerProperty("成功：success, 失败：failure")
	private static final String code = "code";

	@SwaggerProperty("http状态码")
	private static final String status = "status";
	@SwaggerProperty("返回信息（正常的和异常的）")
	private static final String message = "message";
	private JsonObject contents = new JsonObject();

	protected final JsonObject entry;

	protected RespVo() {
		entry = new JsonObject();
		entry.put("data", contents);
	}

	protected RespVo(int i) {
		entry = new JsonObject();
	}

	public RespVo(JsonObject entry) {
		this.entry = null == entry ? new JsonObject() : entry;
		if (null == entry.getValue("contents")) {
			entry.put("data", contents);
		}
	}

	public static RespVo success(String message) {
		RespVo respVo = new RespVo();
		respVo.setStatus(200);
		respVo.setCode(RespVo.SUCCESS);
		respVo.setMessage(message);

		return respVo;
	}

	public static RespVo failure(String message, Throwable throwable) {
		RespVo respVo = new RespVo();
		respVo.setStatus(200);
		respVo.setCode(RespVo.FAILURE);
		respVo.setMessage(null != throwable ? throwable.toString() + message: message);

		return respVo;
	}

	@Override
	public String toString() {
		return entry.toString();
	}

	public RespVo addContent(String key, Object value) {
		contents.put(key, value);
		return this;
	}

	public RespVo addContent(Object value) {
		entry.put("data", value);
		return this;
	}

	public RespVo put(String key, Object value) {
		entry.put(key, value);
		return this;
	}

	public void setCode(String code) {
		entry.put(RespVo.code, code);
	}

	public void setMessage(String message) {
		entry.put(RespVo.message, message);
	}

	public void setStatus(int status) {
		entry.put(RespVo.status, status);
	}

}
