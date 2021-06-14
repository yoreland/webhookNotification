package io.agora.github.notification.controller;

import com.google.gson.Gson;
import io.agora.github.notification.service.PropertyService;
import io.agora.github.notification.tdo.GithubNotificationResponse;
import io.agora.github.notification.tdo.WechatWebhookRequest;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.httpclient.HttpClient;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class WebhookController {

    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static final Gson gson = new Gson();

    @Autowired
    PropertyService propertyService;

    @GetMapping(value = "/")
    public String index(){
        logger.info("ignored repo list:");
        propertyService.getIgnoredRepoList().forEach(s -> logger.info(s));
        logger.info("ignored replier list:");
        propertyService.getStaffList().forEach(s -> logger.info(s));
        return "OK!";
    }

    @PostMapping(value = "event")
    public String newNotification(@RequestBody String data) throws IOException {
        GithubNotificationResponse response = gson.fromJson(data, GithubNotificationResponse.class);
        if(isFilteredMessage(response)){
            logger.info("ignore notification from sender {}", response.getSender());
            return "ignored!";
        }
        WechatWebhookRequest request = populateReportFromNotification(response);
        if("opened".equals(response.getAction())){
            request.mention("@all");
        }
        notifyWechatWebhook(request);
        return "Pushed!";
    }

    private boolean isFilteredMessage(GithubNotificationResponse message){
        if(message.isEmpty())
            return true;
        if(propertyService.getIgnoredRepoList().contains(message.getRepoName()))
            return true;
        if(propertyService.getBotReplierList().contains(message.getReplier()))
            return true;
        return !"opened".equals(message.getAction()) && !"created".equals(message.getAction());
    }

    private WechatWebhookRequest populateReportFromNotification(GithubNotificationResponse response) {
        boolean isStaffReplied = propertyService.getStaffList().contains(response.getReplier());
        logger.info("start to push notification to wechat, content is {}", response.getFormattedMessage(isStaffReplied));
        return new WechatWebhookRequest("text", response.getFormattedMessage(isStaffReplied));
    }

    private void notifyWechatWebhook(WechatWebhookRequest request) throws IOException {
        String url = "/cgi-bin/webhook/send?key=" + propertyService.getWebHookKey();
        String host = "qyapi.weixin.qq.com";
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().setHost(host, 443, "https");
        HttpMethod method = postMethod(url, request.toJson());
        httpClient.executeMethod(method);
        String response = method.getResponseBodyAsString();
        logger.info("pushed notification to wechat, result is {}", response);
    }

    private static HttpMethod postMethod(String url, String body) throws IOException {
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Content-Type","application/json;charset=utf-8");
        post.setRequestBody(body);
        post.releaseConnection();
        return post;
    }
}
