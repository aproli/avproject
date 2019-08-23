package org.proli.avproject.practice;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class FileTest {


    @Test
    public void readFileText() throws IOException {

        File file = new File("src/main/resources/static/workWordTxt.txt");
        List<String> textLines = FileUtils.readLines(file, Charset.defaultCharset());
        String separator = ":";
        HashMap<String, String> store = new HashMap<>();
        textLines.forEach(item ->{
            String[] split = item.split(separator);
            store.put(split[0],split[1]);

        });

        System.out.println(store.size());



    }
}
