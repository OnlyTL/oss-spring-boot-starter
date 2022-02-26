package io.github.artislong.core.minio;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.github.artislong.constant.OssConstant;
import io.github.artislong.core.StandardOssClient;
import io.github.artislong.core.minio.model.MinioOssConfig;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 陈敏
 * @version MinioConfiguration.java, v 1.1 2021/11/24 15:20 chenmin Exp $
 * Created on 2021/11/24
 */
@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties({MinioOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.MINIO + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class MinioOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "minioOssClient";

    @Autowired
    private MinioOssProperties minioOssProperties;

    @Bean
    public void init() {
        List<MinioOssConfig> minioOssConfigs = minioOssProperties.getOssConfigs();
        if (minioOssConfigs.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, minioOssClient(minioClient(minioOssProperties), minioOssProperties));
        } else {
            String endpoint = minioOssProperties.getEndpoint();
            String accessKey = minioOssProperties.getAccessKey();
            String secretKey = minioOssProperties.getSecretKey();
            for (int i = 0; i < minioOssConfigs.size(); i++) {
                MinioOssConfig minioOssConfig = minioOssConfigs.get(i);
                if (ObjectUtil.isEmpty(minioOssConfig.getEndpoint())) {
                    minioOssConfig.setEndpoint(endpoint);
                }
                if (ObjectUtil.isEmpty(minioOssConfig.getAccessKey())) {
                    minioOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(minioOssConfig.getSecretKey())) {
                    minioOssConfig.setSecretKey(secretKey);
                }
                SpringUtil.registerBean(DEFAULT_BEAN_NAME + (i + 1), minioOssClient(minioClient(minioOssConfig), minioOssConfig));
            }
        }
    }

    public StandardOssClient minioOssClient(MinioClient minioClient, MinioOssConfig minioOssConfig) {
        return new MinioOssClient(minioClient, minioOssConfig);
    }

    public MinioClient minioClient(MinioOssConfig minioOssConfig) {
        return MinioClient.builder()
                .endpoint(minioOssConfig.getEndpoint())
                .credentials(minioOssConfig.getAccessKey(), minioOssConfig.getSecretKey())
                .build();
    }
}
