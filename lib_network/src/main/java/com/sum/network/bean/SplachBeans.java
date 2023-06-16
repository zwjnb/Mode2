package com.sum.network.bean;

import java.io.Serializable;

public class SplachBeans implements Serializable {

    /**
     * name : 测试跳转777KU
     * wapurl : https://www.689joy.com/
     * iswap : 1
     * splash : https://feilvb.oss-ap-southeast-6.aliyuncs.com//gilet/uploads/images/368771edbc83ea47af3238811204de1b.png
     * downurl : https://feilvb.oss-ap-southeast-6.aliyuncs.com//gilet/uploads/files/apk/202304/19_1681873341_uXlzOLvvra.apk
     * version : 0
     * webview_set : WSD
     */

    private String name;
    private String wapurl;
    private int iswap;
    private String splash;
    private String downurl;
    private int version;
    private String webview_set;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWapurl() {
        return wapurl;
    }

    public void setWapurl(String wapurl) {
        this.wapurl = wapurl;
    }

    public int getIswap() {
        return iswap;
    }

    public void setIswap(int iswap) {
        this.iswap = iswap;
    }

    public String getSplash() {
        return splash;
    }

    public void setSplash(String splash) {
        this.splash = splash;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getWebview_set() {
        return webview_set;
    }

    public void setWebview_set(String webview_set) {
        this.webview_set = webview_set;
    }
}
