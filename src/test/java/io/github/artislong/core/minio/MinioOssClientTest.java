package io.github.artislong.core.minio;

import cn.hutool.core.io.FileUtil;
import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.model.OssInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈敏
 * @version MinioOssClientTest.java, v 1.1 2021/11/25 10:15 chenmin Exp $
 * Created on 2021/11/25
 */
@SpringBootTest
public class MinioOssClientTest {

    @Autowired
    private StandardOssClient ossClient;

    @Test
    void upLoad() {
        OssInfo ossInfo = ossClient.upLoad(FileUtil.getInputStream("C:\\Users\\15221\\Desktop\\vim.png"), "vim1.png");
        System.out.println(ossInfo);
    }

    @Test
    void downLoad() {
    }

    @Test
    void delete() {
    }

    @Test
    void copy() {
    }

    @Test
    void move() {
    }

    @Test
    void rename() {
    }

    @Test
    void getInfo() {
        OssInfo info = ossClient.getInfo("/", true);
        System.out.println(info);
    }

    @Test
    void isExist() {
    }

    @Test
    void isFile() {
    }

    @Test
    void isDirectory() {
    }

    @Test
    void createFile() {
    }

    @Test
    void createDirectory() {
    }
}