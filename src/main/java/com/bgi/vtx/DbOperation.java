package com.bgi.vtx;

import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

/**
 * 数据库操作接口 处理数据库的增删改查等
 *
 * @author 李英乔
 * @since 2019-06-13
 */
public interface DbOperation<T extends SQLConnection> {

	/**
	 * 1.获取数据库连接，
	 * 2.执行数据库操作（增删改查等），
	 * 3.数据库操作成功/失败的处理，
	 * 4.关闭数据库连接
	 *
	 * @param routingContext
	 * @param operate
	 */
	public void deal(RoutingContext routingContext, Operate<T> operate);

	/**
	 * 执行数据库操作（增删改查等）及 数据库操作成功/失败的处理 的接口
	 *
	 * @author 李英乔
	 * @since 2019-06-13
	 */
	public interface Operate<T> { void operate(OperateSuccess<T> success, OperateFailed<T> failed, T conn); }

	/**
	 * 执行数据库操作（增删改查等）及 数据库操作成功/失败的处理 的接口
	 *
	 * @author 李英乔
	 * @since 2019-06-13
	 */
	public interface OperateWrapper<T> { void operate(OperateSuccess<T> success, OperateFailed<T> failed, T conn); }

	/**
	 * 数据库执行失败的操作 接口
	 *
	 * @author 李英乔
	 * @since 2019-06-13
	 */
	public interface OperateFailed<T> { void failed(T conn, boolean closeConnection, FailedHandle failedHandle); }

	/**
	 * 数据库操作失败的后续操作 接口
	 *
	 * @author 李英乔
	 * @since 2019-06-13
	 */
	public interface FailedHandle { void failed(); }

	/**
	 * 数据库执行成功的操作 接口
	 *
	 * @author 李英乔
	 * @since 2019-06-13
	 */
	public interface OperateSuccess<T> { void success(T conn); }

}
