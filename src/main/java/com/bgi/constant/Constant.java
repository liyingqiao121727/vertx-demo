package com.bgi.constant;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;

import java.util.HashMap;

public class Constant {
    public static final String CAPTCHA = "captcha:";
    public static final String USER_ID = "userId";
    public static final String SUB = "sub";
    public static final String PERMISSIONS_ID = "permissionsId";
    public static final String PERMISSIONS_NAME = "permissionsName";
    public static final String PERMISSIONS_TAG = "permissionsTag";
    public static final String[] WEEK = {"", "一", "二", "三", "四", "五", "六", "日"};
    public static final String EARTH_ENERGY_DEPT_IDS = " ( -4930165081127434350 ) ";
    public static final String DEPT_ID = "-4930165081127434350";
    public static final String MIS_AGREE_CHANGE_ADD = "-7111394852297224501";
    public static final String MIS_AGREE_CHANGE_MINUS = "-6705424979091437135";
    public static final String MIS_BIG_AGREE_CHANGE_ADD = "增";
    public static final String MIS_BIG_AGREE_CHANGE_MINUS = "减";
    public static final String MIS_PROJ_STATUS_DOING = "-8203770043851126962";
    public static final String MIS_PROJ_STATUS_CLOSE = "4847571321022270845";
    public static final JsonObject NULL_JSON_OBJECT = new JsonObject(new HashMap<>(0));
    public static final String TOTAL_IN = " ( '总计' ) ";
    public static final String TOTAL = "总计";
    public static final String SALARY = "职工薪酬";
    public static final String FINANCE = "财务成本";
    public static final String DEPT_SHARE = "所内分摊费用";
    public static final String PERSONAL_SHARE = "现场管理费";

    public static final Single<ResultSet> SINGLE_RESULT_SET =
            Single.create(emitter -> emitter.onSuccess(new ResultSet()));
    public static final Single<UpdateResult> SINGLE_UPSATE_RESULT =
            Single.create(emitter -> emitter.onSuccess(new UpdateResult()));
    public static final Integer NOTICE_UNREAD = 0;
}
