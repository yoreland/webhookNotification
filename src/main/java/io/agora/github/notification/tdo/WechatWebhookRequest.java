package io.agora.github.notification.tdo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WechatWebhookRequest {
    private String msgtype;
    private Text text;


    public WechatWebhookRequest(String msg, String content){
        this.msgtype = msg;
        this.text = new Text();
        this.text.setContent(content);
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Text getText() {
        return text;
    }

    public void mention(String owner){
        text.mentioned_list.add(owner);
    }

    public void setText(Text text) {
        this.text = text;
    }

    class Text {
        private String content;

        private List<String> mentioned_list = new ArrayList<>();

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String[] getMentioned_list() {
            return mentioned_list.toArray(new String[mentioned_list.size()]);
        }
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        System.out.println("parse notification josn: " + json);
        return json;
    }
}

