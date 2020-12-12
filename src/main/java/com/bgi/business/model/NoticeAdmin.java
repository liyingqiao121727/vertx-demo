package com.bgi.business.model;

import com.bgi.common.SQLpage;
import com.bgi.common.SQLpage;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class NoticeAdmin extends Notice {

    public NoticeAdmin(JsonObject entries) {
        super(entries);
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        boolean customUserIdExist = null != this.getCustomUserId() && !"".equals(this.getCustomUserId().trim());
        String sql = new StringBuilder(" select ")
                .append(baseColumn())
                .append(" , b.tname as typeName ")
                .append(customUserIdExist ? " , b.`read` " : " ")
                .append(" , realName from " + TABLE + " a inner join ( select id as bid , t.tname ")
                .append(customUserIdExist ? " , c.`read` " : " ")
                .append(" from " + TABLE + " d inner join ( select id as tid, `name` as tname from " +
                        NoticeType.TABLE + " where `show` = 1 ) t on d.notice_type_id = t.tid").toString();
        StringBuilder sb = new StringBuilder(sql);
        if (customUserIdExist) {
            sb.append(" inner join ( select notice_id as nid, `read` from ").append(NoticeUser.TABLE)
                    .append(" where user_id = ? ) c on c.nid = d.id ");
            params.add(this.getCustomUserId());
        }
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" inner join ( select notice_id as pid from ").append(NoticeProject.TABLE)
                    .append(" where proj_id = ? ) p on p.pid = d.id ");
            params.add(this.getProjId());
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
        if (null != this.getState()) {
            sb.append(" and d.state = ? ");
            params.add(this.getState());
        }
        if (null != this.getNoticeTypeId() && !"".equals(this.getNoticeTypeId().trim())) {
            sb.append(" and d.notice_type_id = ? ");
            params.add(this.getNoticeTypeId());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid " +
                " left join ( select id as uid, real_name as realName from " + EarthEnergyUser.TABLE + " ) u " +
                " on a.user_id = u.uid ");

        return sb.append(" ; ").toString();
    }
}
