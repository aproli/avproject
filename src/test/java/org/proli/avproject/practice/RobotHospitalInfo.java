package org.proli.avproject.practice;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RobotHospitalInfo {

    public static void main(String[] args) throws IOException {

        //Document doc = Jsoup.connect("http://example.com")
        //  .data("query", "Java")
        //  .userAgent("Mozilla")
        //  .cookie("auth", "token")
        //  .timeout(3000)
        //  .post();

//        getAllArea("https://yyk.99.com.cn/");

        String hospitalUrl = "https://yyk.99.com.cn/wangjgjd/133933/jianjie.html";
        getHospitalInfo(hospitalUrl);


    }

    /**
     * 获取省市地址
     * @param domainUrl
     * @throws IOException
     */
    private static void getAllArea(String domainUrl) throws IOException {
        Connection connect = Jsoup.connect(domainUrl);
        Document document = connect.get();
        //查找地区
        Elements areaList = document.select(".area-list li a[href]");
        for (Element area : areaList) {
            String href = area.attr("abs:href");
            String name = area.attr("title");
            System.out.println(name + " : "+href);
        }
    }

    /**
     * 获取区县
     * @param domainUrl
     * @param href  省市地址
     * @throws IOException
     */
    private static void getCity(String domainUrl,String href) throws IOException {
        Document area = Jsoup.connect(domainUrl + href).get();
        Elements areaList = area.select(".u-title-div a");
        for (Element element : areaList) {
            String href1 = element.attr("abs:href");
            String title = element.attr("title");

            System.out.println(title+" : "+href1);
        }
    }

    /**
     * String domainUrl = "https://yyk.99.com.cn/chongming/";
     * @param domainUrl
     * @throws IOException
     *
     * return https://yyk.99.com.cn/chongming/101060/
     */
    private static void getHospitalListByArea(String domainUrl) throws IOException {

        Document areaData = Jsoup.connect(domainUrl).get();

        Elements select1 = areaData.select(".m-box");

        for (Element element : select1) {
            Elements h3_span = element.select("h3 span");
            //如果存在
            if(!h3_span.text().contains("(0)")){

                Elements select = element.select(".m-table-2 table a[href]");
                for (Element element1 : select) {
                    String attr = element1.attr("abs:href");
                    String title = element1.attr("title");
                    System.out.println(title+" : "+attr);
                }


            }
        }
    }

    /**
     *   单个医院信息
     *  String domainUrl = "https://yyk.99.com.cn/chongming/101060/jianjie.html";
     * @param hospitalUrl
     * @throws IOException
     */
    private static void getHospitalInfo(String hospitalUrl) throws IOException {

        Document document = Jsoup.connect(hospitalUrl).get();

        /**
         *  依次逻辑 数据有问题
         * https://yyk.99.com.cn/shunqing/132352/jianjie.html
         * 医院名称 ，及所属区域
         *//*
        String[] split = document.select("div.crumb p").text().split(">");
        for (int i = 1; i < split.length; i++) {
            System.out.println(split[i]);
        }*/


        String hospitalName = document.select(".wrap-mn h1").text();
        Elements select = document.select(".wrap-mn .wrap-info dd p");
        String span = select.get(0).text().replace("别名：","");//别名
        String span1 = select.get(1).text().replace("性质：","");//性质
        String em = select.get(2).select("em").text();//电话
        String em1 = select.get(3).select("em").text();//地址

        //医院介绍 信息
        Elements table = document.select(".wrapper .wrap-box .present-cont .present-table");
        Element element = table.get(0);
        Elements rows = element.select("tr");
        for (Element row : rows) {
            Elements tds = row.select("td");
            for (int i = 0; i < tds.size(); i++) {
                if(i%2 == 0 ){
                    //key value
                    System.out.println(tds.get(i).text() +" : "+ tds.get(i+1).text());
                }

            }
        }
    }


}
