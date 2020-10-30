package me.hoseok.twitterdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="app")
public class AppProperties {
    private String fileUploadDir;
    private String corsUrl;

    public String getFileUploadDir() {
        return fileUploadDir;
    }

    public void setFileUploadDir(String fileUploadDir) {
        this.fileUploadDir = fileUploadDir;
    }

    public String getCorsUrl() {
        return corsUrl;
    }

    public void setCorsUrl(String corsUrl) {
        this.corsUrl = corsUrl;
    }
}
