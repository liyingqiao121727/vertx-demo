package com.bgi.business.model.mis;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.BaseModel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ProjectDetail extends Project {

    public ProjectDetail() {
        super();
    }

    public ProjectDetail(JsonObject entries) {
        super(entries);
    }

    @Override
    protected String columnList() {
        return " id, proj_no as projNo, `name`, `desc`, location, build_area as buildArea, " +
                " air_condition as airCondition, file_path as filePath, analyse, remark, " +
                " proj_status as projStatus, " +
                " proj_level as projLevel, " +
                " longitude, linkman, tel, postcode, work_dept as workDept, cmo, pm, finish, " +
                " latitude, essential, general_situation as generalSituation, glodon_remark as glodonRemark, " +
                " other_president as otherPresident, tender, create_time as createTime, finish_time as finishTime, " +
                BaseModel.baseColumnList();
    }

    public String list(JsonArray params) {
        String sql = " select a.id, a.fie_engine_no as projNo, a.fie_engine_name as `name`, " +
                " b.fie_con_engine_amt as money from " +
                "( select id, fie_engine_no, `fie_engine_name` from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and fie_engine_name = ? ");
            params.add(this.getName());
        }
        if (null != this.getProjStatus()) {
            sb.append(" and fie_engine_no = ? ");
            params.add(this.getProjStatus());
        }
        sb.append(" ) a left join "+ BigAgreement.TABLE + " b on b.for_foreig_key = a.id ;");

        return sb.toString();
    }

}
