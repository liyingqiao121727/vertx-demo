package com.bgi.business.service;

import com.bgi.business.model.MeasureFee;
import com.bgi.business.model.MeasureFeeTotal;
import com.bgi.business.model.WorkType;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.vtx.annotation.Component;
import com.bgi.business.model.MeasureFee;
import com.bgi.business.model.MeasureFeeTotal;
import com.bgi.business.model.WorkType;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import io.vertx.reactivex.ext.sql.SQLClientHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 措施报表 Service
 *
 * @author 李英乔
 * @since 2019-08-18
 */
@Component("measureService")
public class MeasureService {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(MeasureService.class);

    /**
     * reactive 数据库写入客户端
     */
    private @Autowired io.vertx.reactivex.ext.sql.SQLClient rxSqlClientW;

    /**
     * reactive 数据库查询客户端
     */
    private @Autowired io.vertx.reactivex.ext.sql.SQLClient rxSqlClientR;

    private static final String MEASURE_TOTAL = "措施汇总";

    /**
     * 获取Excel 单元格的值
     *
     * @param cell
     * @return
     */
    private Object getCellValue(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            default:
                cellValue = cell.getStringCellValue();
        }

        return cellValue;
    }

    /**
     * 措施报表导入
     *
     * @param xssfWorkbook
     * @param routingContext
     */
    public void excelImport(XSSFWorkbook xssfWorkbook, RoutingContext routingContext) {
        Iterator<Sheet> iterator = xssfWorkbook.sheetIterator();
        String projId = routingContext.request().getParam("projId");
        JsonArray paramProjId = new JsonArray(new ArrayList(1)).add(projId);
        JsonArray params = new JsonArray(new LinkedList());
        JsonArray paramsTotal = new JsonArray(new LinkedList());
        String sql = WorkType.getAll();
        List<MeasureFee> insertList = new LinkedList<>();
        List<MeasureFeeTotal> insertListTotal = new LinkedList<>();

        SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
            Single single = conn.rxQuery(sql).flatMap(resultSet -> {
                Map<String, WorkType> workTypeMap = WorkType.result2NameMap(resultSet);

                String sqlInsertTotal = null;
                String sqlInsert = null;
                while (iterator.hasNext()) {
                    Sheet sheet = iterator.next();
                    if (sheet.getSheetName().contains(MEASURE_TOTAL)) {
                        //导入措施汇总
                        String time = xssfWorkbook.getSheetAt(1).getSheetName();
                        time = time.trim();
                        time = LocalDate.parse(time+"01", DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
                        this.importSheetTotal(sheet, projId, time, workTypeMap, insertListTotal);
                        sqlInsertTotal = MeasureFeeTotal.insertMany(insertListTotal, paramsTotal);
                        logger.info(sqlInsertTotal);
                        continue;
                    }
                    //导入措施Excel Sheet
                    this.importSheet(sheet, projId, workTypeMap, insertList);
                    sqlInsert = MeasureFee.insertMany(insertList, params);
                    logger.info(sqlInsert);
                }

                String insert = sqlInsert;
                String insertTotal = sqlInsertTotal;
                Single single1 = Constant.SINGLE_UPSATE_RESULT.flatMap(o -> {
                    return conn.rxUpdateWithParams(MeasureFeeTotal.deleteByProjId(), paramProjId);
                }).flatMap(o -> {
                    return conn.rxUpdateWithParams(MeasureFee.deleteByProjId(), paramProjId);
                }).flatMap(o -> {
                    Single single2 = null;
                    if (null != insert && insertList.size() > 0) {
                        single2 = conn.rxUpdateWithParams(insert, params);
                    } else {
                        single2 = Constant.SINGLE_UPSATE_RESULT;
                    }
                    return single2;
                }).flatMap(o -> {
                    Single single2 = null;
                    if (null != insertTotal && insertListTotal.size() > 0) {
                        single2 = conn.rxUpdateWithParams(insertTotal, paramsTotal);
                    } else {
                        single2 = Constant.SINGLE_UPSATE_RESULT;
                    }
                    return single2;
                });

                return single1;
            });

            return single.doOnError(throwable -> {
                conn.rxRollback().doOnError(throwable1 -> {
                    logger.error(throwable1);
                }).subscribe();
                //routingContext.response().end(RespVo.failure("新增失败", throwable).toString());
            });
        }).subscribe(o -> {
            routingContext.response().end(RespVo.success("导入成功").toString());
        }, throwable -> {
            Throwable t = (Throwable) throwable;
            logger.error(t);
            routingContext.response().end(RespVo.failure("导入失败", t).toString());
        });
    }

    /**
     * 导入措施Excel Sheet
     *
     * @param sheet
     * @param projId
     * @param workTypeMap
     * @param insertList
     */
    private void importSheet(Sheet sheet, String projId, Map<String, WorkType> workTypeMap,
                             List<MeasureFee> insertList) {
        int lastRowNum = sheet.getLastRowNum() + 1;
        Row row = null;
        WorkType workType = null;
        Cell cell = null;
        Object cellValue = null;
        MeasureFee fee = null;
        String land = null, typeName = null, typeSubName = null;
        String actualSubTotal = null, actualTotal = null;

        String time = sheet.getSheetName().trim();
        time = LocalDate.parse(time+"01", DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        for (int i = 4; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            //地块
            /*cell = row.getCell(1);
            cellValue = this.getCellValue(cell);*/
            String value = null;// == cellValue ? null : cellValue.toString();
            /*if (null != value && !"".equals(value.trim())) {
                land = value.trim();
            }*/
            fee = new MeasureFee();
            fee.setProjId(projId);
            fee.setTime(time);
            /*if (null != land && !Constant.TOTAL.equals(land.trim())) {
                fee.setLandName(land);
            }*/
            //fee.setLandName(land);
            //类别
            cell = row.getCell(2);
            value = cell.getStringCellValue();
            if (null != value && !"".equals(value.trim())) {
                typeName = value.trim();
            }
            if (null != typeName) {
                fee.setTypeName(typeName);
            }
            //fee.setTypeName(typeName);
            workType = workTypeMap.get(value);
            if (null != workType) {
                fee.setTypeId(workType.getId());
            }

            //子类别
            cell = row.getCell(3);
            value = cell.getStringCellValue();
            if (null != value && !"".equals(value.trim())) {
                typeSubName = value.trim();
            }
            if (null != typeSubName && !Constant.TOTAL.equals(typeName)) {
                fee.setTypeSubName(typeSubName);
            }
            //fee.setTypeSubName(typeSubName);
            workType = workTypeMap.get(value);
            if (null != workType) {
                fee.setTypeSubId(workType.getId());
            }

            //内容
            cell = row.getCell(4);
            value = this.getCellValue(cell).toString();
            fee.setContent(value);

            //单价
            cell = row.getCell(5);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                fee.setUnitPrice(value);
            }

            //数量
            cell = row.getCell(6);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                fee.setAmount(value);
            }

            //实际金额
            cell = row.getCell(7);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                fee.setActualMoney(value);
            }

            //实际小计
            cell = row.getCell(8);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                actualSubTotal = value;
                //fee.setActualSubTotal(value);
            }
            if (null != actualSubTotal) {
                fee.setActualSubTotal(actualSubTotal);
            }
            //fee.setActualSubTotal(actualSubTotal);

            //实际合计
            cell = row.getCell(9);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                actualTotal = value;
                //fee.setActualTotal(value);
            }
            if (null != actualTotal) {
                fee.setActualTotal(actualTotal);
            }
            //fee.setActualTotal(actualTotal);

            //备注
            cell = row.getCell(10);
            value = this.getCellValue(cell).toString();
            fee.setRemark(value);

            insertList.add(fee);
        }

    }

    /**
     * 导入措施Excel 汇总 Sheet
     *
     * @param sheet
     * @param projId
     * @param time
     * @param workTypeMap
     * @param insertList
     */
    private void importSheetTotal(Sheet sheet, String projId, String time, Map<String, WorkType> workTypeMap,
                                  List<MeasureFeeTotal> insertList) {
        int lastRowNum = sheet.getLastRowNum() + 1;
        Row row = null;
        WorkType workType = null;
        Cell cell = null;
        Object cellValue = null;
        MeasureFeeTotal total = null;
        String land = null, typeName = null, typeSubName = null;
        String actualSubTotal = null, actualTotal = null, budgetSubTotal = null, budgetTotal = null;
        for (int i = 4; i < lastRowNum; i++) {
            row = sheet.getRow(i);
            //地块
            /*cell = row.getCell(1);
            cellValue = this.getCellValue(cell);*/
            String value = null;// == cellValue ? null : cellValue.toString();
            /*if (null != value && !"".equals(value.trim())) {
                land = value.trim();
            }*/
            total = new MeasureFeeTotal();
            total.setProjId(projId);
            total.setTime(time);
            /*if (null != land && !Constant.TOTAL.equals(land.trim())) {
                total.setLandName(land);
            }*/
            //total.setLandName(land);
            //类别
            cell = row.getCell(2);
            value = cell.getStringCellValue();
            if (null != value && !"".equals(value.trim())) {
                typeName = value.trim();
            }
            if (null != typeName) {
                total.setTypeName(typeName);
            }
            //total.setTypeName(typeName);
            workType = workTypeMap.get(value);
            if (null != workType) {
                total.setTypeId(workType.getId());
            }

            //子类别
            cell = row.getCell(3);
            value = cell.getStringCellValue();
            if (null != value && !"".equals(value.trim())) {
                typeSubName = value.trim();
            }
            if (null != typeSubName && !Constant.TOTAL.equals(typeName)) {
                total.setTypeSubName(typeSubName);
            }
            //total.setTypeSubName(typeSubName);
            workType = workTypeMap.get(value);
            if (null != workType) {
                total.setTypeSubId(workType.getId());
            }

            //内容
            cell = row.getCell(4);
            value = this.getCellValue(cell).toString();
            total.setContent(value);

            //单价
            cell = row.getCell(5);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                total.setUnitPrice(value);
            }

            //数量
            cell = row.getCell(6);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                total.setAmount(value);
            }

            //预算金额
            cell = row.getCell(7);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                total.setBudgetMoney(value);
            }

            //实际金额
            cell = row.getCell(8);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                total.setActualMoney(value);
            }

            //预算小计
            cell = row.getCell(9);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                budgetSubTotal = value;
                //total.setBudgetSubTotal(value);
            }
            if (null != budgetSubTotal && !Constant.TOTAL.equals(typeName)) {
                total.setBudgetSubTotal(budgetSubTotal);
            }
            //total.setBudgetSubTotal(budgetSubTotal);

            //实际小计
            cell = row.getCell(10);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                actualSubTotal = value;
                //total.setActualSubTotal(value);
            }
            if (null != actualSubTotal && !Constant.TOTAL.equals(typeName)) {
                total.setActualSubTotal(actualSubTotal);
            }
            //total.setActualSubTotal(actualSubTotal);

            //预算合计
            cell = row.getCell(11);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                budgetTotal = value;
                //total.setBudgetTotal(value);
            }
            if (null != budgetTotal) {
                total.setBudgetTotal(budgetTotal);
            }
            //total.setBudgetTotal(budgetTotal);

            //实际合计
            cell = row.getCell(12);
            value = this.getCellValue(cell).toString();
            if (null != value && !"".equals(value.trim())) {
                actualTotal = value;
                //total.setActualTotal(value);
            }
            if (null != actualTotal) {
                total.setActualTotal(actualTotal);
            }
            //total.setActualTotal(actualTotal);

            //备注
            cell = row.getCell(13);
            value = this.getCellValue(cell).toString();
            total.setRemark(value);

            insertList.add(total);
        }
    }

}
