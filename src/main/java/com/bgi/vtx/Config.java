package com.bgi.vtx;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Component;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class Config {

	protected static Vertx vertx;

	protected static Map<String, Object> beans;

	protected static JsonObject config;

	//protected static Router router;

	private static InternalLogger logger = InternalLoggerFactory.getInstance(Config.class);

	protected static Map<Class<?>, Object> singletonBeans = new ConcurrentHashMap<Class<?>, Object>();

	public synchronized static void beanAutowired(Set<Class<?>> configClasses, Vertx vertx,
			Map<String, Object> beans, JsonObject config) throws Exception {
		if (null == Config.vertx) Config.vertx = vertx;
		if (null == Config.beans) Config.beans = beans;
		if (null == Config.config) Config.config = config;

		if (null != configClasses && configClasses.size() > 0) {
			Object instance = null, value = null;
			Component component = null;
			for (Class<?> configClass : configClasses) {
				component = configClass.getAnnotation(Component.class);
				if (null == component) continue;
				try {
					instance = Config.beans.get(component.value());
					if (null == instance) {
						instance = singletonBeans.get(configClass);
						if (null == instance) {
							instance = configClass.newInstance();
							singletonBeans.put(configClass, instance);
						}
						Config.beans.put(component.value(), instance);
					}
				} catch (Exception e) {
					logger.error("can not instance of " + configClass.getName());
					continue;
				}
				Field[] fields = configClass.getDeclaredFields();
				if (null == fields || fields.length <= 0) { continue; }
				for (Field field : fields) {
					if (null == field.getAnnotation(Component.Autowired.class)) { continue; }
					field.setAccessible(true);
					value = Config.beans.get(field.getName());
					try {
						if (null != value) {
							field.set(instance, value);
						} else {
							value = singletonBeans.get(field.getType());
							if (null != value) {
								field.set(instance, value);
							} else {
								value = field.getType().newInstance();
								singletonBeans.put(field.getType(), value);
								field.set(instance, value);
							}
							Config.beans.put(field.getName(), value);
						}
					} catch (Exception e) {
						//e.printStackTrace();
						logger.error("bgi: bean autowired error of " + configClass.getName() +
								" \n'bgi' of " + field.getName() + "\n'bgi' cause: " + e.getMessage());
						continue;
					}
				}
			}
		}
	}

	public synchronized static void putConfigBean(Set<Class<?>> configClasses, Vertx vertx,
			Map<String, Object> beans, JsonObject config) throws Exception {
		if (null == Config.vertx) Config.vertx = vertx;
		if (null == Config.beans) Config.beans = beans;
		if (null == Config.config) Config.config = config;

		if (null != configClasses && configClasses.size() > 0) {
			Object instance = null;
			for (Class<?> configClass : configClasses) {
				if (Config.class.isAssignableFrom(configClass)) {
					Method[] methods = configClass.getDeclaredMethods();
					if (null == methods || methods.length <= 0) { continue; }
					instance = configClass.newInstance();
					for (Method method : methods) {
						if (null != Config.beans.get(method.getName())) {
							logger.error("bean name repeated : " + method.getName());
							//throw new Exception("bean name repeated : " + method.getName());
						} else {
							Config.beans.put(method.getName(), method.invoke(instance));
						}
					}
				}
			}
		}
	}

	public static JsonObject getConfigConcurrent(String path) {
		AtomicInteger fileNum = new AtomicInteger(0), folderNum = new AtomicInteger(0);
		JsonObject jsonConfig = new JsonObject(new ConcurrentHashMap<String, Object>());
		File file = new File(path);
		if (!file.exists()) {
			logger.error("文件不存在!");
			return null;
		}
		ConcurrentLinkedQueue<File> quene = new ConcurrentLinkedQueue<File>();
		List<File> fileList = Arrays.asList(file.listFiles());
		fileList.parallelStream().forEach((file2) -> {
			if (file2.isDirectory()) {
				logger.info("文件夹:{}" ,file2.getAbsolutePath());
				quene.add(file2);
				folderNum.getAndAdd(1);
			} else {
				try {
					jsonConfig.mergeIn(new JsonObject(new String(
							Files.readAllBytes(Paths.get(file2.getAbsolutePath())))), true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					logger.info("文件:{}", file2.getAbsolutePath());
					fileNum.getAndAdd(1);
				}
			}
		});
		quene.parallelStream().forEach((tempFile) -> {
			List<File> files = Arrays.asList(tempFile.listFiles());
			files.parallelStream().forEach((file2) -> {
				if (file2.isDirectory()) {
					logger.info("文件夹:{}", file2.getAbsolutePath());
					quene.add(file2);
					folderNum.getAndAdd(1);
				} else {
					try {
						jsonConfig.mergeIn(new JsonObject(new String(
								Files.readAllBytes(Paths.get(file2.getAbsolutePath())))), true);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						logger.info("文件:{}", file2.getAbsolutePath());
						fileNum.getAndAdd(1);
					}
				}
			});
		});

		logger.info("文件夹共有:{}, 文件共有：{}", folderNum.intValue(), fileNum.intValue());
		return jsonConfig;
	}

	public static JsonObject getConfig(String path) {
		int fileNum = 0, folderNum = 0;
		JsonObject jsonConfig = new JsonObject();
		File file = new File(path);
		if (!file.exists()) {
			logger.error("文件不存在!");
			return null;
		}
		LinkedList<File> list = new LinkedList<File>();
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file2.isDirectory()) {
				logger.info("文件夹:{}", file2.getAbsolutePath());
				list.add(file2);
				folderNum++;
			} else {
				try {
					jsonConfig.mergeIn(new JsonObject(new String(
							Files.readAllBytes(Paths.get(file2.getAbsolutePath())))), true);
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					logger.info("文件:{}", file2.getAbsolutePath());
					fileNum++;
				}
			}
		}
		File temp_file;
		while (!list.isEmpty()) {
			temp_file = list.removeFirst();
			files = temp_file.listFiles();
			for (File file2 : files) {
				if (file2.isDirectory()) {
					logger.info("文件夹:{}", file2.getAbsolutePath());
					list.add(file2);
					folderNum++;
				} else {
					try {
						jsonConfig.mergeIn(new JsonObject(new String(
								Files.readAllBytes(Paths.get(file2.getAbsolutePath())))), true);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						logger.info("文件:{}", file2.getAbsolutePath());
						fileNum++;
					}
				}
			}
		}
		logger.info("文件夹共有:{},文件共有:{}", folderNum, fileNum);

		return jsonConfig;
	}

	public static void responseEnd(HttpServerResponse response) {
		if (!response.ended()) {
			response.end();
		}
	}

}
