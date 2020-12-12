package com.bgi.business.model;

import com.bgi.common.SQLpage;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.SQLpage;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoticeTip extends Notice {

    public NoticeTip(JsonObject entries) {
        super(entries);
    }

    /**
     * 项目id
     */
    @SwaggerProperty(value ="大合同id")
    private static final String bigAgreeId = "bigAgreeId";

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        boolean customUserIdExist = null != this.getCustomUserId() && !"".equals(this.getCustomUserId().trim());
        String sql = new StringBuilder(" select ")
                .append(baseColumnListJoinA())
                .append(customUserIdExist ? " , tu.`read` " : " ")
                .append(" , realName, np.proj_id as projId, tu1.id as tipId ")
                .append(" , tu.id as tipUsers , ").append(TipUser.baseColumnListJoinTU())
                .append(" from " + TABLE + " a inner join ( select id as bid ")
                .append(" from " + TABLE + " d ").toString();
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" inner join ( select notice_id as pid from ").append(NoticeProject.TABLE)
                    .append(" where proj_id = ?  ");
            params.add(this.getProjId());
            if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId())) {
                sb.append(" and big_agree_id = ? ");
                params.add(this.getBigAgreeId());
            }
            sb.append(" ) p on p.pid = d.id ");
        }
        sb.append(" where d.del_flag = 0 ");
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and d.user_id = ? ");
            params.add(this.getUserId());
        }
        if (null != this.getType()) {
            sb.append(" and d.type = ? ");
            params.add(this.getType());
        }
        if (null != this.getTitle()) {
            sb.append(" and d.title like ? ");
            params.add("%" + this.getTitle() + "%");
        }
        if (null != this.getNoticeTypeId() && !"".equals(this.getNoticeTypeId().trim())) {
            sb.append(" and d.notice_type_id = ? ");
            params.add(this.getNoticeTypeId());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid " +
                " left join ( select id as uid, real_name as realName from " + EarthEnergyUser.TABLE + " ) u " +
                " on a.user_id = u.uid inner join ( select * from " + TipUser.TABLE + " tusr where tusr.del_flag = 0 ");
        if (customUserIdExist) {
            sb.append(" and tusr.user_id = ? ");
            params.add(this.getCustomUserId());
        }
        if (null != this.getState()) {
            sb.append(" and tusr.state = ? ");
            params.add(this.getState());
        }
        if (null != this.entries.getInteger(TipUser.copyType)) {
            sb.append(" and tusr.copy_type = ? ");
            params.add(this.entries.getInteger(TipUser.copyType));
        }
        sb.append(" ) tu1 on tu1.notice_id = a.id left join " + TipUser.TABLE + " tu on tu.notice_id = a.id and tu.del_flag = 0 " +
                " left join " + EarthEnergyUser.TABLE + " uu on tu.user_id = uu.id left join " +
                NoticeProject.TABLE + " np on np.notice_id = a.id ");

        return sb.append(" ; ").toString();
    }


    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from ( select notice_id from " + TABLE + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        if (null != this.getType()) {
            sb.append(" and type = ? ");
            params.add(this.getType());
        }
        if (null != this.getTitle()) {
            sb.append(" and title like ? ");
            params.add("%" + this.getTitle() + "%");
        }
        if (null != this.getNoticeTypeId() && !"".equals(this.getNoticeTypeId().trim())) {
            sb.append(" and notice_type_id = ? ");
            params.add(this.getNoticeTypeId());
        }
        sb.append(" ) b on a.id = b.bid ");
        sb.append(" inner join ( select * from " + TipUser.TABLE + " tusr where tusr.del_flag = 0 ");
        if (null != this.getCustomUserId() && !"".equals(this.getCustomUserId().trim())) {
            sb.append(" and tusr.user_id = ? ");
            params.add(this.getCustomUserId());
        }
        if (null != this.getState()) {
            sb.append(" and tusr.state = ? ");
            params.add(this.getState());
        }
        if (null != this.entries.getInteger(TipUser.copyType)) {
            sb.append(" and tusr.copy_type = ? ");
            params.add(this.entries.getInteger(TipUser.copyType));
        }
        sb.append(" ) tu on tu.notice_id = a.id ");

        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" inner join ( select notice_id as pid from ").append(NoticeProject.TABLE)
                    .append(" where proj_id = ?  ");
            params.add(this.getProjId());
            if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId())) {
                sb.append(" and big_agree_id = ? ");
                params.add(this.getBigAgreeId());
            }
            sb.append(" ) p on p.pid = b.bid ");
        }

        return sb.append(" group by notice_id ) b ; ").toString();
    }

    public static String getByTimeLimit(String userId, String date, String now, int type,
                                        Integer state, Integer copyType, JsonArray params) {
        String sql = new StringBuilder(" select ")
                .append(baseColumnListJoinA())
                .append(" , tu.`read`, np.proj_id as projId ")
                .append(" , realName , tu1.id as tipId ")
                .append(" , tu.id as tipUsers , ").append(TipUser.baseColumnListJoinTU())
                .append(" from " + TABLE + " a inner join ( select id as bid ")
                .append(" from " + TABLE + " d ").toString();
        StringBuilder sb = new StringBuilder(sql);
        sb.append(" where d.del_flag = 0 ");
        sb.append(" and d.type = ? ");
        params.add(type);
        sb.append(" ) b on a.id = b.bid " +
                " left join ( select id as uid, real_name as realName from " + EarthEnergyUser.TABLE + " ) u " +
                " on a.user_id = u.uid inner join ( select * from " + TipUser.TABLE + " tusr where tusr.del_flag = 0 ");

        sb.append(" and tusr.tip_day > 0 ");
        sb.append(" and tusr.user_id = ? ");
        params.add(userId);
        sb.append(" and tusr.tip_time > ? ");
        params.add(date);
        sb.append(" and DATEDIFF(tusr.tip_time, ? ) <= tusr.tip_day ");
        params.add(now);
        if (null != state) {
            sb.append(" and tusr.state = ? ");
            params.add(state);
        }
        if (null != copyType) {
            sb.append(" and tusr.copy_type = ? ");
            params.add(copyType);
        }
        sb.append(" or tusr.tip_day is null ) tu1 on tu1.notice_id = a.id left join " + TipUser.TABLE +
                " tu on tu.notice_id = a.id and tu.del_flag = 0 " +
                " left join " + EarthEnergyUser.TABLE + " uu on tu.user_id = uu.id left join " +
                NoticeProject.TABLE + " np on np.notice_id = a.id ");

        return sb.append(" ; ").toString();
    }


    public String getBigAgreeId() {
        return this.entries.getString(bigAgreeId);
    }

    public void setBigAgreeId(String bigAgreeId) {
        this.entries.put(NoticeTip.bigAgreeId, bigAgreeId);
    }

}
