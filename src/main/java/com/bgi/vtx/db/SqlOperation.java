package com.bgi.vtx.db;

import com.bgi.vtx.DbOperation;
import com.bgi.vtx.reqresp.response.RespVo;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.reqresp.response.RespVo;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

/**
 * 数据库操作的实现类 处理数据库的增删改查等
 *
 * @author 李英乔
 * @since 2019-06-13
 */
public class SqlOperation implements DbOperation {

	private static InternalLogger logger = InternalLoggerFactory.getInstance(SqlOperation.class);

    /**
     * 数据库连接客户端
     */
	private final SQLClient sqlClient;

    public SqlOperation(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    /**
     * 数据库执行失败的操作 实现类
     *
     * @author 李英乔
     * @since 2019-06-13
     */
	private static final OperateFailed<SQLConnection> failed = (conn, closeConnection, failedHandle) -> {
		if (closeConnection) {
			conn.rollback(handle -> {
				if (!handle.succeeded()) {
					logger.error("数据库回滚失败！");
				}
				conn.close();
			});
		}
		if (null != failedHandle) {
			failedHandle.failed();
		}
	};

    /**
     * 数据库执行成功的操作 实现类
     *
     * @author 李英乔
     * @since 2019-06-13
     */
	private static final OperateSuccess<SQLConnection> success = conn -> {
		conn.commit(handler -> {
			if (!handler.succeeded()) {
				logger.error(handler.cause());
				conn.rollback(handler1 -> {
					if (!handler1.succeeded()) {
						logger.error("数据库回滚失败");
					}
				});
			}
			conn.close();
		});
	};

    /**
     * 1.获取数据库连接，
     * 2.执行数据库操作（增删改查等），
     * 3.数据库操作成功/失败的处理，
     * 4.关闭数据库连接
     *
     * @param routingContext
     * @param operate
     */
	public @Override void deal(RoutingContext routingContext, Operate operate) {
		sqlClient.getConnection(res -> {
			if (!res.succeeded()) {
				logger.error("获取数据库连接失败,原因:{}", res.cause());
			} else {
			    //获取数据库连接
				SQLConnection connection = res.result();
				connection.setAutoCommit(false, handle -> {
					if (!handle.succeeded()) {
						logger.error("获取数据库连接失败,原因:{}", handle.cause());
						if (null != routingContext && !routingContext.response().ended()) {
							routingContext.response().end(RespVo.failure(
									"获取数据库连接失败,原因:", handle.cause()).toString());
						}
						return;
					}
					try {
					    //执行数据库操作（sql操作），及操作成功，失败的处理
						operate.operate(success, failed, connection);
					} catch (Exception e) {
						connection.rollback(handler -> connection.close());
						logger.error("数据库操作失败，原因：{}", e);
						if (null != routingContext && !routingContext.response().ended()) {
							routingContext.response().end(RespVo.failure(
									"获取数据库连接失败,原因:", handle.cause()).toString());
						}
					}
				});
			}
		});
	}

}
