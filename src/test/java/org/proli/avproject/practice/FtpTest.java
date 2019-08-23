package org.proli.avproject.practice;

import cn.hutool.extra.ftp.Ftp;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FtpTest {


    @Test
    public void download() throws IOException {

        /**
         * ftp 源文件目录
         */
        String ftpSourceDir = "/opt/sftp/wxgroup/BJ";
        /**
         * 本地目标文件目录
         */
        String targetDir = "E:\\data\\";

        String fileName = "10301120111557717053830.jpg";
        Ftp ftp = new Ftp("192.168.101.86", 21, "wechat01", "Aa111111");

        ftp.cd(ftpSourceDir);

        ftp.download(ftpSourceDir + "/20190513/201905131111/650102019000273/" + fileName,
                new File(targetDir + fileName));

        ftp.close();


    }
}
