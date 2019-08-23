package org.proli.avproject.practice;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.junit.Test;

public class QRCodeTest {

    /**
     * 生产二维码
     */
    @Test
    public void generate(){

        String localFilePath = "E:\\xxx.png";

        QrConfig config = new QrConfig(300,300);

        QrCodeUtil.generate("https://u.wechat.com/MLTtEhDF1zeLtonIVQFkYz4",config, FileUtil.newFile(localFilePath));
    }

    /**
     * 解析二维码
     */
    @Test
    public void deCode(){
        String file = "E:\\qrTest.jpg" ;
        String decode = QrCodeUtil.decode(FileUtil.file(file));
        System.out.println(decode);
    }
}
