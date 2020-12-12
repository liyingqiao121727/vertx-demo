package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayParentProject extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "pay_parent_project";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(payParentId, "pay_parent_id");put(projId, "proj_id");put(projNo, "proj_no");
        put(projName, "proj_name");put(oilFee, "oil_fee");put(roadBridgeFee, "road_bridge_fee");
        put(carFee, "car_fee");put(materialFee, "material_fee");put(taxiFee, "taxi_fee");
        put(officeFee, "office_fee");put(travelFee, "travel_fee");put(expressFee, "express_fee");
        put(foodFee, "food_fee");put(hourseFee, "hourse_fee");put(waterElectricFee, "water_electric_fee");
        put(liveOfficeFee, "live_office_fee");put(otherFee, "other_fee");put(shareTag, "share_tag");
        put(actualPay, "actual_pay");put(payTypeId, "pay_type_id");put(summary, "summary");
        put(share, "share");put(remark, "remark");put(shareTime, "share_time");
        put(all, "all");put(detailMoney, "detail_money");put(commitUserId, "commit_user_id");
        put(payTime, "pay_time");
    }};
    static { PayParentProject.map.putAll(BaseModel.map); }

    /**
     * 报销父id
     */
    @SwaggerProperty(value = "报销父id")
    private static final String payParentId = "payParentId";

    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";

    /**
     * 项目编号
     */
    @SwaggerProperty(value = "项目编号")
    private static final String projNo = "projNo";

    /**
     * 项目名称
     */
    @SwaggerProperty(value = "项目名称")
    private static final String projName = "projName";

    /**
     * 油费
     */
    @SwaggerProperty(value = "油费")
    private static final String oilFee = "oilFee";

    /**
     * 路桥费
     */
    @SwaggerProperty(value = "路桥费")
    private static final String roadBridgeFee = "roadBridgeFee";

    /**
     * 车补
     */
    @SwaggerProperty(value = "车补")
    private static final String carFee = "carFee";

    /**
     * 物料费
     */
    @SwaggerProperty(value = "物料费")
    private static final String materialFee = "materialFee";

    /**
     * 出租车费
     */
    @SwaggerProperty(value = "出租车费")
    private static final String taxiFee = "taxiFee";

    /**
     * 办公费
     */
    @SwaggerProperty(value = "办公费")
    private static final String officeFee = "officeFee";

    /**
     * 差旅费
     */
    @SwaggerProperty(value = "差旅费")
    private static final String travelFee = "travelFee";

    /**
     * 快递费
     */
    @SwaggerProperty(value = "快递费")
    private static final String expressFee = "expressFee";

    /**
     * 招待费
     */
    @SwaggerProperty(value = "招待费")
    private static final String foodFee = "foodFee";

    /**
     * 房租
     */
    @SwaggerProperty(value = "房租")
    private static final String hourseFee = "hourseFee";

    /**
     * 水电
     */
    @SwaggerProperty(value = "水电")
    private static final String waterElectricFee = "waterElectricFee";

    /**
     * 现场办公费
     */
    @SwaggerProperty(value = "现场办公费")
    private static final String liveOfficeFee = "liveOfficeFee";

    /**
     * 其它
     */
    @SwaggerProperty(value = "其它")
    private static final String otherFee = "otherFee";

    /**
     * 发票小计
     */
    @SwaggerProperty(value = "发票小计")
    private static final String all = "all";

    /**
     * 明细金额
     */
    @SwaggerProperty(value = "明细金额")
    private static final String detailMoney = "detailMoney";

    /**
     * 实际支付
     */
    @SwaggerProperty(value = "实际支付")
    private static final String actualPay = "actualPay";

    /**
     * 是否是分摊项目
     */
    @SwaggerProperty(value = "是否是分摊项目 0:不是，1：是")
    private static final String shareTag = "shareTag";

    /**
     * 分摊时间
     */
    @SwaggerProperty(value = "分摊时间")
    private static final String shareTime = "shareTime";

    /**
     * 是否分摊
     */
    @SwaggerProperty(value = "是否是分摊项目 0:不是，1：是")
    private static final String share = "share";

    /**
     * 支出项目类型
     */
    @SwaggerProperty(value = "支出项目类型")
    private static final String payTypeId = "payTypeId";

    /**
     * 摘要
     */
    @SwaggerProperty(value = "摘要")
    private static final String summary = "summary";

    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";

    /**
     * 报销时间
     */
    @SwaggerProperty(value = "报销时间")
    private static final String payTime = "payTime";

    /**
     * 备注
     */
    @SwaggerProperty(value = "提交人Id")
    private static final String commitUserId = "commitUserId";

    public PayParentProject(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public PayParentProject(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public PayParentProject() {
        super(null, TABLE);
    }

    public static String getByIdIn(int size) {
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnList())
                .append(" from ").append(TABLE).append(" where id in (    ");
        for (int i = 0; i < size; i++) {
            sb.append(" ?  , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ").append(" ) ; ").toString();
    }

    /*public static String updateShare(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" update ").append(TABLE).append(" set share = 1, share_time = now() where id = ? ; ");
        }
        return sb.toString();
    }*/

    public static String updateShare(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ").append(TABLE).append(" set `share` = 1, share_time = now() where id in ( ");
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sBuilder.append(" , ? ");
        }
        sb.append(size > 0 ? sBuilder.substring(2) : "")
                .append(" ) ; ");
        //.append(" ) and `share` is not null ; ");
        return sb.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    public static String baseColumnList() {
        return " id, pay_parent_id as payParentId, proj_id as projId, pay_type_id as payTypeId, " +
                " proj_no as projNo, proj_name as projName, oil_fee as oilFee, " +
                " road_bridge_fee as roadBridgeFee, car_fee as carFee, material_fee as materialFee, " +
                " taxi_fee as taxiFee, office_fee as officeFee, travel_fee as travelFee, " +
                " express_fee as expressFee, food_fee as foodFee, hourse_fee as hourseFee, " +
                " water_electric_fee as waterElectricFee, share, share_time as shareTime, " +
                " live_office_fee as liveOfficeFee, other_fee as otherFee, actual_pay as actualPay, " +
                " share_tag as shareTag, summary, remark, `all`, detail_money as detailMoney, " +
                " commit_user_id as commitUserId, " +
                BaseModel.baseColumnList();
    }


    public static String baseColumnListJoinP() {
        return " p.id, p.pay_parent_id as payParentId, p.proj_id as projId, p.pay_type_id as payTypeId, " +
                " p.proj_no as projNo, p.proj_name as projName, p.oil_fee as oilFee, " +
                " p.road_bridge_fee as roadBridgeFee, p.car_fee as carFee, p.material_fee as materialFee, " +
                " p.taxi_fee as taxiFee, p.office_fee as officeFee, p.travel_fee as travelFee, " +
                " p.express_fee as expressFee, p.food_fee as foodFee, p.hourse_fee as hourseFee, " +
                " p.water_electric_fee as waterElectricFee, p.share, p.share_time as shareTime, " +
                " p.live_office_fee as liveOfficeFee, p.other_fee as otherFee, p.actual_pay as actualPay, " +
                " p.share_tag as shareTag, p.summary, p.remark, p.`all`, p.detail_money as detailMoney, " +
                " p.commit_user_id as commitUserId, p.gmt_create as gmtCreate, p.pay_time as payTime ";
    }


    public static String baseColumnListJoinP1() {
        return " p.id, p.pay_parent_id as payParentId, p.proj_id as projectId, " +
                " p.proj_no as projNo, p.proj_name as projName, p.oil_fee as oilFee, " +
                " p.road_bridge_fee as roadBridgeFee, p.car_fee as carFee, p.material_fee as materialFee, " +
                " p.taxi_fee as taxiFee, p.office_fee as officeFee, p.travel_fee as travelFee, " +
                " p.express_fee as expressFee, p.food_fee as foodFee, p.hourse_fee as hourseFee, " +
                " p.water_electric_fee as waterElectricFee, p.share, p.share_time as shareTime, " +
                " p.live_office_fee as liveOfficeFee, p.other_fee as otherFee, p.actual_pay as actualPay, " +
                " p.share_tag as shareTag, p.summary, p.remark, p.`all`, p.detail_money as detailMoney, " +
                " p.commit_user_id as commitUserId, p.gmt_create as gmtCreate ";
    }


    public static String baseColumnSum() {
        return " sum(oil_fee) as oilFee, " +
                " sum(road_bridge_fee) as roadBridgeFee, sum(car_fee) as carFee, sum(material_fee) as materialFee, " +
                " sum(taxi_fee) as taxiFee, sum(office_fee) as officeFee, sum(travel_fee) as travelFee, " +
                " sum(express_fee) as expressFee, sum(food_fee) as foodFee, sum(hourse_fee) as hourseFee, " +
                " sum(water_electric_fee) as waterElectricFee, " +
                " sum(live_office_fee) as liveOfficeFee, sum(other_fee) as otherFee ";
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public static String sumByProjIdAndTimeAndType(String projId, String time, Integer type, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnSum());
                sb.append(" from ").append(TABLE).append(" where proj_id = ? ");
        if (null != time && !"".equals(time)) {
            sb.append(" and pay_time like ? ");
            params.add(time+"%");
        }
        if (null != type) {
            sb.append(" and `share_tag` = ? ");
            params.add(type);
        }
        sb.append(" and del_flag = 0; ");
        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTimeBetween(String projId, Integer type, String between, String end, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnSum());
        sb.append(" from ").append(TABLE).append(" where proj_id = ? ");
        if (null != between && !"".equals(between) && null != end && !"".equals(end)) {
            sb.append(" and pay_time between ? and ? ");
            params.add(between).add(end);
        }
        if (null != type) {
            sb.append(" and `share_tag` = ? ");
            params.add(type);
        }
        sb.append(" and del_flag = 0; ");
        return sb.toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinP() +
                " , t.name, u.real_name as realName from ( select * from " + TABLE + " where del_flag = 0 ";
        StringBuilder builder = new StringBuilder(sql);
        if (null != this.getShareTag()) {
            builder.append(" and share_tag = ? ");
            params.add(this.getShareTag());
        }
        builder.append(sqLpage.toString()).append(" ) p left join ").append(EarthEnergyUser.TABLE)
                .append(" u on u.id = p.commit_user_id ").append(" left join ").append(DicPayType.TABLE)
                .append(" d on p.pay_type_id = d.id left join ")
                .append(DicPayTypeParent.TABLE).append(" t on d.parent_id = t.id ;");
        return builder.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder builder = new StringBuilder(sql);
        if (null != this.getShareTag()) {
            builder.append(" and share_tag = ? ");
            params.add(this.getShareTag());
        }
        builder.append(" ; ");

        return builder.toString();
    }

    public Integer getShareTag() {
        return entries.getInteger(shareTag);
    }

    public Integer getShare() {
        return entries.getInteger(share);
    }

    public String getRemark() {
        return entries.getString(remark);
    }

    public List<GlodonCost> toShareList() {
        ArrayList<GlodonCost> list = new ArrayList<>();
        GlodonCost cost = new GlodonCost(null, false);
        cost.setName("油费");
        cost.setActualMoney(this.getOilFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("路桥费");
        cost.setActualMoney(this.getRoadBridgeFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("车补");
        cost.setActualMoney(this.getCarFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("物料费");
        cost.setActualMoney(this.getMaterialFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("出租车费");
        cost.setActualMoney(this.getOilFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("办公费");
        cost.setActualMoney(this.getOfficeFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("差旅费");
        cost.setActualMoney(this.getTravelFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("快递费");
        cost.setActualMoney(this.getExpressFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("招待费");
        cost.setActualMoney(this.getFoodFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("房租");
        cost.setActualMoney(this.getHourseFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("水电");
        cost.setActualMoney(this.getWaterElectricFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("现场办公费");
        cost.setActualMoney(this.getLiveOfficeFee());
        list.add(cost);

        cost = new GlodonCost(null, false);
        cost.setName("其它");
        cost.setActualMoney(this.getOtherFee());
        list.add(cost);

        return list;
    }

    public String getPayParentId() {
        return entries.getString(payParentId);
    }

    public String getProjId() {
        return entries.getString(projId);
    }

    public String getProjNo() {
        return entries.getString(projNo);
    }

    public String getProjName() {
        return entries.getString(projName);
    }

    public String getOilFee() {
        return entries.getString(oilFee);
    }

    public String getRoadBridgeFee() {
        return entries.getString(roadBridgeFee);
    }

    public String getCarFee() {
        return entries.getString(carFee);
    }

    public String getMaterialFee() {
        return entries.getString(materialFee);
    }

    public String getTaxiFee() {
        return entries.getString(taxiFee);
    }

    public String getOfficeFee() {
        return entries.getString(officeFee);
    }

    public String getTravelFee() {
        return entries.getString(travelFee);
    }

    public String getExpressFee() {
        return entries.getString(expressFee);
    }

    public String getFoodFee() {
        return entries.getString(foodFee);
    }

    public String getHourseFee() {
        return entries.getString(hourseFee);
    }

    public String getWaterElectricFee() {
        return entries.getString(waterElectricFee);
    }

    public String getLiveOfficeFee() {
        return entries.getString(liveOfficeFee);
    }

    public String getOtherFee() {
        return entries.getString(otherFee);
    }

    public String getAll() {
        return entries.getString(all);
    }

    public String getDetailMoney() {
        return entries.getString(detailMoney);
    }

    public String getActualPay() {
        return entries.getString(actualPay);
    }

    public String getShareTime() {
        return entries.getString(shareTime);
    }

    public String getPayTypeId() {
        return entries.getString(payTypeId);
    }

    public String getSummary() {
        return entries.getString(summary);
    }

    public String getPayTime() {
        return entries.getString(payTime);
    }

    public String getCommitUserId() {
        return entries.getString(commitUserId);
    }
}
