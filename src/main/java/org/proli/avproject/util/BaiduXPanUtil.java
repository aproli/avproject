package org.proli.avproject.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lijiansheng
 * @projectName micro-service-claim
 * @Description 百度网盘文件上传：文档地址：https://pan.baidu.com/union/document/upload
 * https://developer.baidu.com/newwiki/dev-wiki/?t=1518338594397
 * @Date 2019/8/811:37
 */
public class BaiduXPanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BaiduXPanUtil.class);

    /**
     * 文件上传分为三个阶段：预上传、分片上传、创建文件。只有完成这三步，才能将文件上传到网盘。
     *
     * @param file        待上传的文件
     * @param accessToken 百度网盘token
     * @throws IOException
     */
    static void uploadFileToXPan(File file, String accessToken) throws IOException {

        //文件地址固定，若想更改需联系 百度网盘工作人员
        String wPath = "/apps/shangbaoPic/" + file.getName();
        long fileLength = file.length();
        RandomAccessFile readFile = new RandomAccessFile(file, "rw");
        //普通用户单个分片大小固定为4MB
        int chunkSize = 4 * 1024 * 1024;
        //分片数
        long chunkTotal = fileLength / chunkSize;
        if (fileLength % chunkSize != 0) {
            chunkTotal++;
        }
        byte[] buf = new byte[chunkSize];
        int chunkCount = 0;
        int currentChunkSize;
        String chunkFile = file.getAbsolutePath() + ".part";
        //存放 分片文件 md5 值
        JSONArray jsonArray = new JSONArray();
        List<String> chunkFileNameList = new ArrayList<>();
        while ((currentChunkSize = readFile.read(buf)) != 1) {
            chunkCount++;
            RandomAccessFile writeFile = new RandomAccessFile(new File(chunkFile + chunkCount), "rw");
            chunkFileNameList.add(chunkFile + chunkCount);
            writeFile.write(buf, 0, currentChunkSize);
            writeFile.close();
            //单个文件 md5
            jsonArray.add(DigestUtils.md5Hex(new FileInputStream(chunkFile + chunkCount)));
            //获取全部分片MD5 字符串后 跳出
            if (chunkCount == chunkTotal) {
                break;
            }

        }
        String jsonArrayStr = JSON.toJSONString(jsonArray);
        //预上传
        String firstResponse = precreate(fileLength, jsonArrayStr, accessToken, wPath);
        JSONObject jsonObject = JSONObject.parseObject(firstResponse);
        int returnType = (int) jsonObject.get("return_type");
        if (returnType == 2) {
            logger.info("上传完成！");
            return;
        }

        String uploadId = jsonObject.getString("uploadid");
        String blockListStr = jsonObject.getString("block_list");
        List<Integer> integers = JSONArray.parseArray(blockListStr, Integer.class);

        if (CollUtil.isEmpty(integers)) {
            integers.add(0);
        }
        for (Integer integer : integers) {
            superfile(new File(chunkFileNameList.get(integer)), accessToken, wPath, uploadId, integer);
        }

        createFile(fileLength, accessToken, jsonArrayStr, wPath, uploadId);

        //分片文件删除
        for (String chunkFileName : chunkFileNameList) {
            FileUtil.del(chunkFileName);
        }

    }

    /**
     * 预上传
     */
    private static String precreate(long fileLength, String jsonArray, String accessToken, String wPath) {

        String precreateUrl = "https://pan.baidu.com/rest/2.0/xpan/file?method=precreate&access_token=" + accessToken;

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("path", wPath);
        paramMap.put("size", fileLength);
        paramMap.put("isdir", 0);
        paramMap.put("autoinit", 1);
        paramMap.put("rtype", 1);
        paramMap.put("block_list", jsonArray);
        String firstRequest = HttpUtil.post(precreateUrl, paramMap);
        logger.info(firstRequest);
        return firstRequest;

    }

    /**
     * 分片上传
     */
    private static void superfile(File file, String accessToken, String wPath, String uploadId, Integer index) {
        String superfile2 = "https://d.pcs.baidu.com/rest/2.0/pcs/superfile2?";
        HashMap<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("access_token", accessToken);
        paramMap2.put("method", "upload");
        paramMap2.put("type", "tmpfile");
        paramMap2.put("path", wPath);
        paramMap2.put("uploadid", uploadId);
        paramMap2.put("partseq", index);
        String queryStr = cn.hutool.http.HttpUtil.toParams(paramMap2);
        HashMap<String, Object> formParam = new HashMap<>();
        formParam.put("file", file);
        String sendRequest = cn.hutool.http.HttpUtil.post(superfile2 + queryStr, formParam);
        logger.info(sendRequest);
    }

    /**
     * 创建文件
     */
    private static void createFile(long fileSize, String accessToken, String jsonArray, String wPath, String uploadId) {
        String createUrl = "https://pan.baidu.com/rest/2.0/xpan/file?method=create&access_token=" + accessToken;
        HashMap<String, Object> createParamMap = new HashMap<>();
        createParamMap.put("path", wPath);
        createParamMap.put("size", fileSize);
        /**
         * isdir	string	是	是否目录，0 文件、1 目录
         */
        createParamMap.put("isdir", 0);
        /**
         * rtype int
         * 文件命名策略，默认0
         * 0 为不重命名，返回冲突
         * 1 为只要path冲突即重命名
         * 2 为path冲突且block_list不同才重命名
         * 3 为覆盖
         */
        createParamMap.put("rtype", 1);
        createParamMap.put("uploadid", uploadId);
        createParamMap.put("block_list", jsonArray);
        String thirdRequest = HttpUtil.post(createUrl, createParamMap);
        logger.info(thirdRequest);

    }


    /**
     * 刷新access_token
     */
    static String refreshToken(String refreshToken, String apiKey, String secretKey) {
        String refreshTokenUrl = "https://openapi.baidu.com/oauth/2.0/token?";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", "refresh_token");
        paramMap.put("refresh_token", refreshToken);
        paramMap.put("client_id", apiKey);
        paramMap.put("client_secret", secretKey);
        String urlParam = HttpUtil.toParams(paramMap);
        String response = HttpUtil.get(refreshTokenUrl + urlParam);
        logger.info(response);
        return response;

    }
}
