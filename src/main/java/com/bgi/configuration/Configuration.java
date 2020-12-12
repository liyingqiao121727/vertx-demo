package com.bgi.configuration;

import com.bgi.vtx.Config;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration extends Config {

	public static final JsonObject configuration = Config.config;

	private static WebClient webClient;

	public static synchronized WebClient webClient() {
		if (null == webClient) {
			webClient = WebClient.create(vertx);
		}
		return webClient;
	}

	public static synchronized String fontPath() {
		return config.getString("fontPath");
	}

	public static synchronized String salaryTemplateLocation() {
		return config.getString("salaryTemplateLocation");
	}

	public static synchronized String payTemplateLocation() {
		return config.getString("payTemplateLocation");
	}

	public static synchronized String attendTemplateLocation() {
		return config.getString("attendTemplateLocation");
	}

	public static synchronized String attendEarthEnergyTemplateLocation() {
		return config.getString("attendEarthEnergyTemplateLocation");
	}

	public static synchronized String glodonTemplateLocation() {
		return config.getString("glodonTemplateLocation");
	}

	public static synchronized String otherTemplateLocation() {
		return config.getString("otherTemplateLocation");
	}

	public static synchronized String shareTemplateLocation() {
		return config.getString("shareTemplateLocation");
	}

	public static synchronized String projectTotalLocation() {
		return config.getString("projectTotalLocation");
	}

	public static synchronized String urlPrefix() {
		return config.getString("urlPrefix");
	}

	public static synchronized JsonArray webSocketPushUrls() {
		return config.getJsonArray("webSocketPushUrls");
	}

	public static synchronized String bgiHost() {
		return config.getJsonObject("bgiUrl").getString("host");
	}

	public static synchronized Integer bgiPort() {
		return config.getJsonObject("bgiUrl").getInteger("port");
	}

	public static synchronized String resourceLocation() {
		return config.getString("resourceLocation");
	}

	public static synchronized String bgiLogin() {
		return config.getJsonObject("bgiUrl").getJsonObject("login").getString("requestUri");
	}

	public static synchronized String bgiUserInfo() {
		return config.getJsonObject("bgiUrl").getJsonObject("userInfo").getString("requestUri");
	}

	public static synchronized String misHost() {
		return config.getJsonObject("misUrl").getString("host");
	}

	public static synchronized Integer misPort() {
		return config.getJsonObject("misUrl").getInteger("port");
	}

	public static synchronized String misProject() {
		return config.getJsonObject("misUrl")
				.getJsonObject("project").getString("requestUri");
	}

	private static Map<String, String> wsMapping;
	public static synchronized Map<String, String> wsMapping() {
		if (null == wsMapping) {
			wsMapping = new ConcurrentHashMap<>();
		}
		return wsMapping;
	}

}
