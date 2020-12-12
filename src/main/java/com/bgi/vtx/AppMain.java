package com.bgi.vtx;

import com.bgi.util.CommonUtil;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.reqresp.response.RespVo;
import com.bgi.vtx.reqresp.response.RespVo;
import com.bgi.util.CommonUtil;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.*;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * APP 启动类
 * 读取配置信息
 * 扫描包，解析注解
 * 注入bean
 * 配置过滤器拦截器等
 * 配置websocket
 * 等
 *
 * @author 李英乔
 * @since 2019-05-16
 */
public class AppMain extends AbstractVerticle {

	private static final Scanner scanner = new Scanner();
	private static final Set<Class<?>> controllers = new ConcurrentHashSet<>();
	private static final Set<Class<?>> webSockets = new ConcurrentHashSet<>();
	private static InternalLogger logger;
	private static JsonObject config;
	public static final Map<String, Object> BEANS = new ConcurrentHashMap<String, Object>();
	private static final Map<Controller.ControllerEntity, Map<String, RequestMapping.RequestMappingEntity>> swagger
			= new ConcurrentHashMap<>();

	private static final String logLocation = "log" + File.separator + "log4j2.xml";

	public static void main(String[] args) throws IOException {
		//Launcher s = null; 配置文件
		String verticleID = AppMain.class.getName();
		runExample(verticleID);
	}

	public static void runExample(String verticleID) {
		System.out.println("runExample:");
		System.out.println(AppMain.class.getClassLoader().getResource(""));
		URL url = AppMain.class.getClassLoader().getResource("");
		String resourceLocation = null;
		if (null != url) {
			resourceLocation = url.getFile();
		} else {
			System.err.println("bgi classloader resource is null");
			resourceLocation = new File("").getAbsolutePath() + File.separator;
		}
		System.out.println("bgi resource location : " + resourceLocation);
		// Force logging to Log4j2
		System.setProperty("log4j.configurationFile", resourceLocation + logLocation);
		InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
		logger = InternalLoggerFactory.getInstance(AppMain.class);

		System.out.println("bgi config : " + resourceLocation + "configFile");
		config = Config.getConfigConcurrent(resourceLocation + "configFile");
		try {
			String location = config.getString("controller.location");
			System.out.println("bgi controller location : " + location);
			if (null == location || "".equals(location.trim())) {
				throw new Exception("property : controller.location not config app will exit");
			}
			String[] locations = location.split(",");
			for (String loc : locations) { controllers.addAll(scanner.getControllers(loc)); }


			location = config.getString("websocket.location");
			System.out.println("bgi websocket location : " + location);
			if (null == location || "".equals(location.trim())) {
				throw new Exception("property : websocket.location not config app will exit");
			}
			locations = location.split(",");
			for (String loc : locations) { webSockets.addAll(scanner.getWebSockets(loc)); }
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		DeploymentOptions deplyOptions = new DeploymentOptions();/*.setWorker(true);
		deplyOptions.setConfig(null != config ? config : deplyOptions.getConfig());
		deplyOptions.setWorkerPoolName("bgi-worker").setWorkerPoolSize(config.getInteger("workerPoolSize"));*/
		deplyOptions.setInstances(16);
		Consumer<Vertx> runner = vertx -> {
			vertx.deployVerticle(verticleID, deplyOptions, res -> {
				logger.info(Config.beans.toString());
				logger.info(Config.singletonBeans.toString());
				Config.beans.clear(); Config.singletonBeans.clear();
				if (res.succeeded()) { logger.info("bgi Deployment id is: {}", res.result());}
				else { logger.error("bgi Deployment failed! cause: {}", res.cause()); }
			});
		};
		// Vert.x实例是vert.x api的入口点，我们调用vert.x中的核心服务时，均要先获取vert.x实例，
		// 通过该实例来调用相应的服务，例如部署verticle、创建http server
		VertxOptions options = new VertxOptions().setWorkerPoolSize(config.getInteger("workerPoolSize"))
				.setBlockedThreadCheckInterval(config.getInteger("blockedThreadCheckInterval"));
		options.setBlockedThreadCheckIntervalUnit(TimeUnit.valueOf(config.getString("blockedThreadCheckIntervalUnit")));
		options.setMaxWorkerExecuteTime(config.getInteger("maxWorkerExecuteTime"));
		options.setMaxWorkerExecuteTimeUnit(TimeUnit.valueOf(config.getString("maxWorkerExecuteTimeUnit")));
		options.setInternalBlockingPoolSize(config.getInteger("internalBlockingPoolSize"));
		Vertx vertx = Vertx.vertx(options);
		runner.accept(vertx);
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("start:");
		vertx.eventBus().addInboundInterceptor( sendContext ->{
			Message message = sendContext.message();
			// 自由发挥……
			sendContext.next();
		});
		Set<Class<?>> classes = new HashSet<>();
		String resourceLocation = null;
		URL url = AppMain.class.getClassLoader().getResource("");
		if (null != url) {
			resourceLocation = url.getFile();
		} else {
			System.err.println("bgi classloader resource is null");
			resourceLocation = new File("").getAbsolutePath() + File.separator;
		}
		if (null == config) {
			System.out.println("bgi log location : " + resourceLocation + logLocation);
			System.setProperty("log4j.configurationFile", resourceLocation + logLocation);
			InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

			logger = InternalLoggerFactory.getInstance(AppMain.class);
			System.out.println("bgi config : " + resourceLocation + "configFile");
			config = Config.getConfigConcurrent(resourceLocation + "configFile");
		}
		String location = config.getString("bean.config.location");
		BEANS.put("resourceLocation", resourceLocation);
		config.put("resourceLocation", resourceLocation);
		System.out.println("bgi bean config location : " + location);
		if (null != location && !"".equals(location)) {
			String[] locations = location.split(",");
			for (String loc : locations) { classes.addAll(scanner.getClasses(loc)); }
		}
		Config.putConfigBean(classes, vertx, BEANS, config);
		BEANS.put("webSocketMap", webSocketMap);
		location = config.getString("scanPackages");
		System.out.println("bgi scanPackages : " + location);
		String[] scanPackages = location.split(",");
		for (String pack : scanPackages) {
			Config.beanAutowired(scanner.getClasses(pack), vertx, BEANS, config);
		}

		Context context = vertx.getOrCreateContext();
		if (context.isEventLoopContext()) { logger.info("bgi Context attached to Event Loop"); }
		else if (context.isWorkerContext()) { logger.info("bgi Context attached to Worker Thread"); }
		else if (context.isMultiThreadedWorkerContext()) { logger.info("bgi Context attached to Worker Thread - multi threaded worker"); }
		else if (!Context.isOnVertxThread()) { logger.info("bgi Context not attached to a thread managed by vert.x"); }
		context.runOnContext(v -> { logger.info("bgi This will be executed asynchronously in the same context"); });

		//HttpServerOptions httpServerOptions = new HttpServerOptions().setLogActivity(true);
		/*httpServerOptions.setWebsocketAllowServerNoContext(true);
		httpServerOptions.setWebsocketPreferredClientNoContext(true);
		httpServerOptions.setPerFrameWebsocketCompressionSupported(true);
		httpServerOptions.setPerMessageWebsocketCompressionSupported(true);*/
		//httpServerOptions.setWebsocketSubProtocols("123456789");
		//httpServerOptions.getWebsocketSubProtocols();
		HttpServer server = vertx.createHttpServer();//httpServerOptions);
		Router router = Router.router(vertx);

		//router.route().handler(CookieHandler.create());
		//router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setAuthProvider(authProvider));

		Filters filters = (Filters) BEANS.get("filters");
		filters.filterBefore(router);

		String urlPrefix = BEANS.get("urlPrefix").toString();
		synchronized (controllers) {
			for (Class<?> cls : controllers) {
				Method[] methods = cls.getDeclaredMethods();
				Object instance = Config.singletonBeans.get(cls);
				Controller controller = cls.getAnnotation(Controller.class);
				try {
					if (null == instance) {
						instance = cls.newInstance();
						Config.singletonBeans.put(cls, instance);
					}
				} catch (Exception e) {
					continue;
				}
				Map<String, RequestMapping.RequestMappingEntity> entities = new HashMap<>();
				JsonObject key = null;
				RequestMapping.RequestMappingEntity value = null;
				for (Method method : methods) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						value = new RequestMapping.RequestMappingEntity(requestMapping);
						key = new JsonObject();
						key.put("//_path_", value.remove("//_path_")).put("//_desc_", value.remove("//_description_"));
						entities.put(key.toString(), value);
						logger.info("bgi requestMapping: " + urlPrefix + requestMapping.path());
						router.route(requestMapping.method(), urlPrefix + requestMapping.path())
								//.blockingHandler((Handler<RoutingContext>) method.invoke(instance), false);
								.handler((Handler<RoutingContext>) method.invoke(instance));
								//.failureHandler(routingContext -> routingContext.fail(500, routingContext.failure()));
					}
				}
				swagger.put(new Controller.ControllerEntity(controller), entities);
			}
		}

		router.route(HttpMethod.GET, "/swagger").handler(routingContext -> {
			//swagger.put("definitions", RequestMapping.RequestMappingEntity.definitions);
			routingContext.response().putHeader("content-type", "application/json")
					.putHeader("Access-Control-Allow-Origin", "*")
					.end(Json.encode(swagger));
		});


		filters.filterAfter(router);

		router.errorHandler(404, routingContext -> {

			// This handler will be called for every request
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");

			// Write to the response and end it
			response.end("bgi!:" + routingContext.statusCode());
		});



		router.errorHandler(500, routingContext -> {

			// This handler will be called for every request
			HttpServerResponse response = routingContext.response();
			try {
				CommonUtil.accessControlAllow(response);
				response.putHeader("content-type", "application/json");
				logger.error(routingContext.failure());
			} catch (Exception e) {
				logger.error(e);
			}

			// Write to the response and end it
			if (!response.ended()) {
				RespVo respVo = RespVo.failure("", routingContext.failure());
				respVo.setStatus(500);
				response.end(respVo.toString());
			}
		});

		router.route().failureHandler(frc -> {

			// This will be called for failures that occur
			// when routing requests to paths starting with
			// '/somepath/'
			RespVo respVo = RespVo.failure(null, frc.failure());
			respVo.setStatus(frc.statusCode());
			/*JsonObject jsonObject = new JsonObject();
			jsonObject.put("errorName", "Liyingqiao");
			jsonObject.put("errorCode", frc.statusCode());
			jsonObject.put("errorThrow", frc.failure());*/
			HttpServerResponse response = frc.response();
			//response.setStatusCode(frc.statusCode());

			if (!response.ended()) {
				CommonUtil.accessControlAllow(response);
				response.end(respVo.toString());
			}
		});

		for (Class<?> cls : webSockets) {
			Method[] methods = cls.getDeclaredMethods();
			Object instance = Config.singletonBeans.get(cls);
			try {
				if (null == instance) {
					instance = cls.newInstance();
					Config.singletonBeans.put(cls, instance);
				}
			} catch (Exception e) {
				continue;
			}
			for (Method method : methods) {
				if (method.getReturnType().isAssignableFrom(AbstractMap.SimpleEntry.class)) {
					BGIWebSocket.put((AbstractMap.SimpleEntry<String, Handler<ServerWebSocket>>) method.invoke(instance));
				}
			}
		}
		Map<String, String> wsMapping = (Map<String, String>) BEANS.get("wsMapping");
		new BGIWebSocket(server, webSocketMap, wsMapping);

		Integer port = config.getInteger("port");
		logger.info("bgi port : " + port);
		server.requestHandler(router).listen(port, httpServerAsyncResult -> {
		    if (httpServerAsyncResult.succeeded()) {
		        logger.info("success");
		        return;
            }
            logger.error(httpServerAsyncResult.cause());
        });
	}
	private static final Map<String, ServerWebSocket> webSocketMap = new ConcurrentHashMap<>();

}
