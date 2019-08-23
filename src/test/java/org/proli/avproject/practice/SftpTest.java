package org.proli.avproject.practice;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ssh.Sftp;
import org.junit.Test;

import java.io.File;

public class SftpTest {


    @Test
    public void download(){

        Sftp sftp = new Sftp("219.239.42.95", 22, "admin", "123456");


    }

    @Test
    public void upload(){


        String  ftpTargetDir = "";
        String localSourceDir = "";

        Sftp sftp = new Sftp("219.239.42.95", 22, "admin", "123456");
        File[] localFiles = FileUtil.ls(localSourceDir);
        for (File localFile : localFiles) {



        }




    }
}
