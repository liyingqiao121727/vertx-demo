package com.bgi.business.controller;

import com.bgi.business.model.MeasureFee;
import com.bgi.business.model.MeasureFeeTotal;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.service.MeasureService;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component("measureController")
@Controller(value = MeasureController.class, name = "措施费用报表")
public class MeasureController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(MeasureController.class);

    private @Component.Autowired
    MeasureService measureService;

    private @Component.Autowired
    DbOperation<SQLConnection> mysqlOperation;

    private @Component.Autowired
    SQLClient extsqlClient;

    private @Component.Autowired
    SQLClient sqlClient;

    private @Component.Autowired
    io.vertx.reactivex.ext.sql.SQLClient rxSqlClientR;

    @RequestMapping(path="/measure/import/:projId", method=HttpMethod.POST,
            response=RespVo.class, description = "措施费用报表")
    public Handler<RoutingContext> machineFeeImport() {
        return routingContext -> {
            routingContext.request().setExpectMultipart(true);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            routingContext.request().uploadHandler(fu -> fu.handler(buffer -> {
                try {
                    os.write(buffer.getBytes());
                } catch (IOException e) {
                    logger.error(e);
                }
            })).endHandler(end -> {
                try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(os.toByteArray()))) {
                    measureService.excelImport(xssfWorkbook, routingContext);
                } catch (Exception e) {
                    logger.error(e);
                    routingContext.response().end(RespVo.failure(null, e).toString());
                }
            });
        };
    }

    @RequestMapping(path="/measure/page", method=HttpMethod.POST, request = MeasureFee.class,
            response=RespVo.class, description = "分页查询措施费用报表")
    public Handler<RoutingContext> machinePage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            MeasureFee machineFee = new MeasureFee(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, machineFee);
        });
    }

    @RequestMapping(path="/measure/total/page", method=HttpMethod.POST, request = MeasureFeeTotal.class,
            response=RespVo.class, description = "分页查询措施费用汇总报表")
    public Handler<RoutingContext> machineTotalPage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            MeasureFeeTotal machineFeeTotal = new MeasureFeeTotal(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, machineFeeTotal);
        });
    }

}
