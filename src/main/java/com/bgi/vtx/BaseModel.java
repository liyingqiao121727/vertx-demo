package com.bgi.vtx;

import com.bgi.common.ObjectId;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.ObjectId;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基本的实体类，包含id, gmt_create, gmt_modified, del_flag, permission_id等字段
 * 所有的实体都要继承它
 *
 * @author 李英乔
 * @since 2019-05-26
 */
public class BaseModel {

	private static InternalLogger logger = InternalLoggerFactory.getInstance(BaseModel.class);

	/**
	 * 实际存放的实体对象
	 */
	protected transient JsonObject entries;

	/**
	 * 实体基础字段的映射关系 key为实体字段，value为数据库字段
	 */
	protected static final HashMap<String, String> map = new HashMap<String, String>(6){{
		put(id, "id");put(gmtCreate, "gmt_create");put(gmtModified, "gmt_modified");
		put(delFlag, "del_flag");put(permissionId, "permission_id");
	}};

	/**
	 * 表名
	 */
	protected static final String TABLE = "TABLE_NAME ";

	/**
	 * id
	 */
	@SwaggerProperty
	protected static final String id = "id";
	/**
	 * 创建时间
	 */
	@SwaggerProperty(value = "创建时间")
	protected static final String gmtCreate = "gmtCreate";
	/**
	 * 修改时间
	 */
	@SwaggerProperty(value = "修改时间")
	protected static final String gmtModified = "gmtModified";
	/**
	 * 删除标志
	 */
	@SwaggerProperty(value = "删除标志")
	protected static final String delFlag = "delFlag";
	/**
	 * 权限
	 */
	@SwaggerProperty(value = "权限")
	protected static final String permissionId = "permissionId";
	/**
	 * 版本号
	 */
	@SwaggerProperty("版本号")
	protected static final String version = "version";

	public BaseModel(JsonObject jsonObject, String tableName) {
		jsonObject = null == jsonObject ? new JsonObject() : jsonObject;
		this.entries = jsonObject;
		this.entries.put(TABLE, tableName);
		this.init();
	}

	public BaseModel(JsonObject jsonObject, String tableName, boolean init) {
		jsonObject = null == jsonObject ? new JsonObject() : jsonObject;
		this.entries = jsonObject;
		this.entries.put(TABLE, tableName);
		if (init) {
			this.init();
		}
	}

	/**
	 * 查询数据库基础字段 含as
	 * gmt_create as gmtCreate, gmt_modified as gmtModified, del_flag as delFlag, permission_id as permissionId, version
	 *
	 * @return
	 */
	protected static String baseColumnList() {
		return new StringBuilder(" ").append(map.get(gmtCreate)).append(" as ").append(gmtCreate).append(", ")
				.append(map.get(gmtModified)).append(" as ").append(gmtModified).append(", ")
				.append(map.get(delFlag)).append(" as ").append(delFlag).append(", ")
				.append(map.get(permissionId)).append(" as ").append(permissionId).append(", version ").toString();
	}

	/**
	 * 查询数据库基础字段 不含as
	 * gmt_create, gmt_modified, del_flag, permission_id, version
	 *
	 * @return
	 */
	protected static String baseColumn() {
		return new StringBuilder(" ").append(map.get(gmtCreate)).append(", ")
				.append(map.get(gmtModified)).append(", ")
				.append(map.get(delFlag)).append(", ")
				.append(map.get(permissionId)).append(", version ").toString();
	}

	/**
	 * 插入或更新时使用，用于完善基础字段
	 * @param params
	 */
	protected void addParams(JsonArray params) {
		params.add(this.getGmtCreate()).add(this.getGmtModified()).add(this.getDelFlag())
				.add(this.getPermissionId()).add(this.getVersion());
	}

	/**
	 * 返回实际存放的实体对象
	 * @return
	 */
	public JsonObject getEntries() {
		entries.remove(TABLE);
		return entries;
	}

	/**
	 * 初始化 ，添加时间，修改时间，删除标识，id等
	 */
	private void init() {
		if (null == this.getGmtCreate()) {
			this.setGmtCreate(LocalDateTime.now());
		}
		if (null == this.getGmtModified()) {
			this.setGmtModified(LocalDateTime.now());
		}
		if (null == this.getDelFlag()) {
			this.setDelFlag(0);
		}
		if (null == this.getVersion()) {
			this.setVersion(0L);
		}
		if (null == this.getId()) {
			this.setId(ObjectId.id());
		}
	}

	/**
	 * 返回数据库基础字段 由子类进行覆盖 含as
	 * gmt_create as gmtCreate, gmt_modified as gmtModified, del_flag as delFlag, permission_id as permissionId, version
	 *
	 * @return
	 */
	protected String columnList() {
		return baseColumnList();
	}

	/**
	 * sql查询单条记录 接口
	 *
	 * @author 李英乔
	 * @since 2019-05-26
	 */
	public interface QueryOne<T extends BaseModel> {
		void nextOperate(SQLConnection conn, T result, AsyncResult<ResultSet> res);
	}

	/**
	 * 将查询结果封装为具体的实体 接口
	 *
	 * @author 李英乔
	 * @since 2019-05-26
	 */
	public interface ResultWrapper<T> {  T getInstance(JsonObject result); }

	/**
	 * sql对数据库的增删改操作 接口
	 *
	 * @author 李英乔
	 * @since 2019-05-26
	 */
	public interface WriteDB { void nextOperate(SQLConnection conn, AsyncResult<UpdateResult> res); }

	/**
	 * 根据id查询
	 * @param conn
	 * @param wrapper
	 * @param nextOperate
	 * @param <T>
	 */
	public static <T extends BaseModel> void getById(SQLConnection conn,
													 ResultWrapper<T> wrapper, QueryOne<T> nextOperate) {
		T instance = wrapper.getInstance(new JsonObject());
		String sql = " select " + instance.columnList() + " from " + instance.entries.getString(TABLE) +
				" where id = ? and del_flag = 0 ;";
		logger.info(sql);
		conn.queryWithParams(sql, new JsonArray().add(instance.entries.getValue("id")), res -> {
			if (res.succeeded() && res.result().getNumRows() > 0) {
				Object value = instance.entries.remove(TABLE);
				instance.entries = res.result().getRows().get(0);
				instance.entries = null == instance.entries ? new JsonObject() : instance.entries;
				instance.entries.put(TABLE, value);
				nextOperate.nextOperate(conn, instance, res);
				return;
			}
			nextOperate.nextOperate(conn, null, null);
		});
	}

	/**
	 * 插入一条记录
	 *
	 * @param conn
	 * @param nextOperate
	 */
	public void insertOne(SQLConnection conn, WriteDB nextOperate) {
		StringBuilder columnNames = new StringBuilder(), values = new StringBuilder();
		JsonArray params = new JsonArray();
		Map<String, Object> map = this.entries.getMap();
		if (null == map || map.isEmpty()) {
			nextOperate.nextOperate(conn, null);
			return;
		}
		Object value = map.remove(TABLE);
		Map<String, String> subMap = this.getMap();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = subMap.get(entry.getKey());
			if (null != key) {
				columnNames.append(',').append(key);
				values.append(" ,? ");
				params.add(entry.getValue());
			}
		}
		map.put(TABLE, value);
		String sql = "insert into " + this.entries.getString(TABLE) + "(" +
				columnNames.substring(1) + ") values (" + values.substring(2) + ");";
		logger.info(sql);
		conn.updateWithParams(sql, params, res -> {
			nextOperate.nextOperate(conn, res);
		});
	}

	/**
	 * 根据id更新
	 *
	 * @param conn
	 * @param nextOperate
	 */
	public void updateById(SQLConnection conn, WriteDB nextOperate) {
		JsonArray params = new JsonArray();
		Map<String, Object> map = this.entries.getMap();
		if (null == map || map.isEmpty()) {
			nextOperate.nextOperate(conn, null);
			return;
		}
		Object value = map.remove(TABLE);
		map.remove(gmtCreate);
		StringBuilder sets = new StringBuilder();
		Map<String, String> subMap = this.getMap();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = subMap.get(entry.getKey());
			if (null != key) {
				sets.append(", ").append(key).append(" =? ");
				params.add(entry.getValue());
			}
		}
		map.put(TABLE, value);
		String sql = "update " + this.entries.getString(TABLE) + " set " + sets.substring(1) + " where id = ? ;";
		params.add(this.entries.getValue("id"));
		logger.info(sql);
		conn.updateWithParams(sql, params, res -> {
			nextOperate.nextOperate(conn, res);
		});
	}

	/**
	 * 根据id删除
	 *
	 * @param conn
	 * @param nextOperate
	 */
	public void deleteById(SQLConnection conn, WriteDB nextOperate) {
		String sql = " delete from " + this.entries.getString(TABLE) + " where id = ? ;";
		conn.updateWithParams(sql, new JsonArray().add(this.entries.getValue("id")), res -> {
			nextOperate.nextOperate(conn, res);
		});
	}

	/**
	 * 根据id删除 （假删）， 将del_flag 置为1
	 * @param conn
	 * @param nextOperate
	 */
	public void deleteByIdNotReal(SQLConnection conn, WriteDB nextOperate) {
		String sql = "update " + this.entries.getString(TABLE) + " set " + map.get(delFlag) + "=1 where id = ? ;";
		conn.updateWithParams(sql, new JsonArray().add(this.entries.getValue("id")), res -> {
			nextOperate.nextOperate(conn, res);
		});
	}

	/*public void deleteById(SQLConnection conn, WriteDB nextOperate) {
		String sql = "delete from " + this.entries.getString(TABLE_NAME) + " where id = ? ;";
		conn.updateWithParams(sql, new JsonArray().add(this.entries.getValue("id")), res -> {
			nextOperate.nextOperate(conn, res);
		});
	}*/

	/**
	 * 生成插入单条记录的sql语句
	 *
	 * @param params
	 * @return
	 */
	public String insertOne(JsonArray params) {
		Map<String, String> colMap = this.getMap();
		Object[] keyNames = colMap.keySet().toArray();
		StringBuilder colNames = new StringBuilder();
		for (Object keyName : keyNames) {
			colNames.append('`').append(colMap.get(keyName)).append("` , ");
		}
		colNames = colNames.replace(colNames.length()-3, colNames.length()-1, "");
		StringBuilder sql = new StringBuilder(" insert into ").append(this.entries.getString(TABLE))
				.append("( ").append(colNames).append(" ) values ( ");
		for (Object keyName : keyNames) {
			sql = sql.append(" ? , ");
			params.add(this.entries.getValue(keyName.toString()));
		}
		sql = sql.replace(sql.length()-3, sql.length()-1, " ) ; ");
		return sql.toString();
	}

	/**
	 * 生成插入单条记录部分字段的sql语句
	 *
	 * @param params
	 * @return
	 */
	public String insertOnePart(JsonArray params) {
		Map<String, String> colMap = this.getMap();
		Object tableName = this.entries.remove(TABLE);
		Object[] keyNames = this.entries.getMap().keySet().toArray();
		StringBuilder colNames = new StringBuilder();
		for (Object keyName : keyNames) {
			if (null != colMap.get(keyName)) {
				colNames.append('`').append(colMap.get(keyName)).append("` , ");
			}
		}
		colNames = colNames.replace(colNames.length()-3, colNames.length()-1, "");
		StringBuilder sql = new StringBuilder(" insert into ").append(tableName)
				.append("( ").append(colNames).append(" ) values ( ");
		for (Object keyName : keyNames) {
			if (null != colMap.get(keyName)) {
				sql = sql.append(" ? , ");
				params.add(this.entries.getValue(keyName.toString()));
			}
		}
		sql = sql.replace(sql.length()-3, sql.length()-1, " ) ; ");
		return sql.toString();
	}

	/**
	 * 生成根据id更新的sql语句
	 *
	 * @param params
	 * @return
	 */
	public String updateById(JsonArray params) {
		Map<String, String> colMap = this.getMap();
		StringBuilder sql = new StringBuilder(" update ").append(this.entries.getString(TABLE)).append(" set ");
		for (Map.Entry<String, String> entry : colMap.entrySet()) {
			if (this.entries.containsKey(entry.getKey())) {
				sql.append(" `").append(entry.getValue()).append("` = ? , ");
				params.add(this.entries.getValue(entry.getKey()));
			}
		}
		sql = sql.replace(sql.length()-3, sql.length()-1, " where id = ? ;\r\n ");
		params.add(this.getId());
		return sql.toString();
	}

	/**
	 * 生成插入多条的sql语句
	 *
	 * @param list
	 * @param params
	 * @return
	 */
	public static String insertMany(List<? extends BaseModel> list, JsonArray params) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		BaseModel baseModel = list.get(0);
		Map<String, String> colMap = baseModel.getMap();
		Object[] keyNames = colMap.keySet().toArray();
		StringBuilder colNames = new StringBuilder();
		for (Object keyName : keyNames) {
			colNames.append('`').append(colMap.get(keyName)).append("` , ");
		}
		colNames = colNames.replace(colNames.length()-3, colNames.length()-1, "");
		StringBuilder sql = new StringBuilder(" insert into ").append(baseModel.entries.getString(TABLE))
				.append("( ").append(colNames).append(" ) values    ");
		for (BaseModel bm : list) {
			sql = sql.append(" ( ");
			for (Object keyName : keyNames) {
				sql = sql.append(" ? , ");
				params.add(bm.entries.getValue(keyName.toString()));
			}
			sql = sql.replace(sql.length()-3, sql.length()-1, " ) , ");
		}
		return sql.replace(sql.length()-3, sql.length()-1, " ; ").toString();
	}


	public String getId() {
		return this.entries.getString(id);
	}

	public void setId(String id) {
		this.entries.put(BaseModel.id, id);
	}
	public String getGmtCreate() {
		return this.entries.getString(gmtCreate);
	}
	public void setGmtCreate(LocalDateTime gmtCreate) {
		this.entries.put(BaseModel.gmtCreate, gmtCreate.format(DateTimeFormatter.ISO_DATE_TIME));
	}
	public String getGmtModified() {
		return this.entries.getString(gmtModified);
	}

	public void setGmtModified(LocalDateTime gmtModified) {
		this.entries.put(BaseModel.gmtModified, gmtModified.format(DateTimeFormatter.ISO_DATE_TIME));
	}
	public Integer getDelFlag() {
		return this.entries.getInteger(delFlag);
	}

	public void setDelFlag(Integer delFlag) {
		this.entries.put(BaseModel.delFlag, delFlag);
	}
	public String getPermissionId() {
		return this.entries.getString(permissionId);
	}

	public void setPermissionId(String permissionId) {
		this.entries.put(BaseModel.permissionId, permissionId);
	}
	public Long getVersion() {
		return this.entries.getLong(version);
	}

	public void setVersion(Long version) {
		this.entries.put(BaseModel.version, version);
	}
	public Map<String, String> getMap() {
		return map;
	}

}
