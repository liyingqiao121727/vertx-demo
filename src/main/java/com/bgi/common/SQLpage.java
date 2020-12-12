package com.bgi.common;

import com.bgi.business.reqresp.response.RespVo;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.http.HttpServerRequest;

public class SQLpage {

    @SwaggerProperty("响应：返回分页结果")
    private Object list;
    @SwaggerProperty("响应：返回总数")
    private long total;

    @SwaggerProperty("请求：asc/desc（可选）")
    private String sort;
    @SwaggerProperty("请求：排序列（可选）")
    private String[] orderBy;
    @SwaggerProperty("请求：每页数目")
    private Long limit = 0L;
    @SwaggerProperty("请求：起始页（从1开始）")
    private Long page = 1L;
    private boolean exist;

    public interface OrderByExist {
        boolean isExist(String[] orderBy);
    }

    public SQLpage(long page, long limit, String[] orderBy, boolean desc, OrderByExist orderByExist) {
        page = page < 1 ? 1 : page;
        this.page = page;
        this.limit = limit;
        this.orderBy = orderBy;
        this.sort = desc ? "desc" : "asc";
        this.exist = null == orderByExist ? false : orderByExist.isExist(this.orderBy);
    }

    public SQLpage(HttpServerRequest request, OrderByExist orderByExist) {
        String value = request.getParam("page");
        if (null != value && !"".equals(value.trim())) {
            this.page = Long.parseLong(value);
        }
        value = request.getParam("limit");
        if (null != value && !"".equals(value.trim())) {
            this.limit = Long.parseLong(value);
        }
        String ob = request.getParam("orderBy");
        if (null != ob) {
            String[] obs = ob.split(",");
            for (int i = 0; i < obs.length; i++) {
                obs[i] = obs[i].trim();
            }
            this.orderBy = obs;
        }
        this.sort = "desc".equals(request.getParam("sort")) ? "desc" : "asc";
        this.exist = null == orderBy || orderBy.length < 1 ? false :
                null == orderByExist ? false : orderByExist.isExist(orderBy);
    }

    @Override
    public String toString() {
        if (0 == limit || page < 1) {
            return "";
        }

        if (exist && null != orderBy && orderBy.length > 0 && null != sort && !"".equals(sort.trim())) {
            StringBuilder sb = new StringBuilder("  ");
            for (String order : orderBy) {
                sb.append(order).append(", ");
            }
            return " order by " + sb.substring(0, sb.length()-2) + " " + sort + " limit " + (page - 1) * limit + " , " + limit + "  ";
        }

        return " limit " + (page - 1) * limit + " , " + limit + "  ";
    }

    public String toString(String pre) {
        if (0 == limit || page < 1) {
            return "";
        }

        if (exist && null != orderBy && orderBy.length > 0 && null != sort && !"".equals(sort.trim())) {
            StringBuilder sb = new StringBuilder("  ");
            for (String order : orderBy) {
                sb.append(pre).append(order).append(", ");
            }
            return " order by " + sb.substring(0, sb.length()-2) + " " + sort + " limit " + (page - 1) * limit + " , " + limit + "  ";
        }

        return " limit " + (page - 1) * limit + " , " + limit + "  ";
    }

    public boolean isDesc() {
        return "desc".equals(sort);
    }

    public SQLpage setDesc(boolean desc) {
        this.sort = desc ? "desc" : "asc";
        return this;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public SQLpage setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Long getLimit() {
        return limit;
    }

    public SQLpage setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    public Long getPage() {
        return page;
    }

    public SQLpage setPage(Long page) {
        this.page = page;
        return this;
    }

    public boolean isExist() {
        return exist;
    }

    public SQLpage setList(Object list) {
        this.list = list;
        return this;
    }

    public SQLpage setTotal(long total) {
        this.total = total;
        return this;
    }

    public Object getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }

    public String resp() {
        return RespVo.success("查询成功").addContent("total", total).addContent("list", list).toString();
    }
}
