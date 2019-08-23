package org.proli.avproject.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class BaiduXPanUtilTest {

    @Test
    public void uploadFileToXPan() throws IOException {


        //token 失效需 刷新
        String accessToken = "21.f54950913594969b50c295f7eeb69145.2592000.1569131095.323393023-16955123";
        String fileName = "JAVA核心知识点整理.pdf";
        File file = new File("E:\\data\\" + fileName);
        BaiduXPanUtil.uploadFileToXPan(file, accessToken);

    }

    @Test
    public void refreshToken() {

        String refreshToken = "22.4f736026dc2644013b55a429ad8a1005.315360000.1880356348.323393023-16955123";
        String apiKey = "rDaIiuePWAObpv9xQCWUseMZ";
        String secretKey = "w2g3bOGzAXhfsngAhrHV3bhTAPuv4yfV";
        String response = BaiduXPanUtil.refreshToken(refreshToken, apiKey, secretKey);
        System.out.println(response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String access_token = jsonObject.getString("access_token");
        String refresh_token = jsonObject.getString("refresh_token");
        String expires_in = jsonObject.getString("expires_in");

    }
}