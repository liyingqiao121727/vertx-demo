package com.bgi.business.controller;

import com.bgi.business.model.Suggestion;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.business.model.Suggestion;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedList;

@Component(value = "suggestionController")
@Controller(value = SuggestionController.class, name = "意见反馈")
public class SuggestionController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(SuggestionController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> mysqlOperation;

    @RequestMapping(path="/suggestion/commit", method=HttpMethod.POST,
            response=Suggestion.class, description = "意见反馈接口")
    public Handler<RoutingContext> suggestionCommit() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            Suggestion suggestion = new Suggestion(bodyBuf.toJsonObject());
            String sql = suggestion.insertOne(params);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, params);
        });
    }

}
