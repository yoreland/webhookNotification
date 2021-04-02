package io.agora.github.notification.controller;

import com.google.gson.Gson;
import io.agora.github.notification.tdo.GithubNotificationResponse;
import io.agora.github.notification.tdo.WechatWebhookRequest;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.httpclient.HttpClient;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/")
public class WebhookController {

    private static Gson gson = new Gson();

    @RequestMapping(value = "/status", method = POST)
    public String status(){
        return "OK!";
    }

    @GetMapping(value = "/")
    public String index(){
        return "helloworld";
    }

    @PostMapping(value = "event")
    public String newNotification(@RequestBody String data) throws IOException {
        System.out.println(data);
        GithubNotificationResponse response = gson.fromJson(data, GithubNotificationResponse.class);
        if(response.getAction()!=null
                || "opened".equals(response.getAction())
                || "created".equals(response.getAction())){
            WechatWebhookRequest request = populateReportFromNotification(response);
            notifyWechatWebhook(request);
            return "Pushed!";
        }
        else{
            return "ignored!";
        }
    }

    private WechatWebhookRequest populateReportFromNotification(GithubNotificationResponse response) {
        return new WechatWebhookRequest("markdown", response.getFormatedMessage());
    }

    private void notifyWechatWebhook(WechatWebhookRequest request) throws IOException {
        String url = "/cgi-bin/webhook/send?key=ed05a5c1-3043-4107-89cd-5a412a197428";
        String host = "qyapi.weixin.qq.com";
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().setHost(host, 443, "https");
        HttpMethod method = postMethod(url, request.toJson());
        httpClient.executeMethod(method);
        String response = method.getResponseBodyAsString();
        System.out.println(response);

    }

    private static HttpMethod postMethod(String url, String body) throws IOException {
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Content-Type","application/json;charset=utf-8");
        post.setRequestBody(body);
        post.releaseConnection();
        return post;
    }
}
