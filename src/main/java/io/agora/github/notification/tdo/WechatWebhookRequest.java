package io.agora.github.notification.tdo;

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

    public void setText(Text text) {
        this.text = text;
    }

    class Text {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

