package io.agora.github.notification.model;

public class Owner {
    private String email;
    private String githubId;
    private String wechatName;

    public Owner(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }
}
