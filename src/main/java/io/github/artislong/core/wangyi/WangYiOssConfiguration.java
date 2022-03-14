/** * $Id: WangYiOssConfiguration.java,v 1.0 2022/3/4 9:49 PM chenmin Exp $ */package io.github.artislong.core.wangyi;import cn.hutool.core.text.CharPool;import cn.hutool.core.util.ObjectUtil;import cn.hutool.extra.spring.SpringUtil;import com.netease.cloud.auth.BasicCredentials;import com.netease.cloud.auth.Credentials;import com.netease.cloud.services.nos.NosClient;import io.github.artislong.constant.OssConstant;import io.github.artislong.core.StandardOssClient;import io.github.artislong.core.wangyi.model.WangYiOssConfig;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;import org.springframework.boot.context.properties.EnableConfigurationProperties;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import java.util.Map;/** * @author 陈敏 * @version $Id: WangYiOssConfiguration.java,v 1.1 2022/3/4 9:49 PM chenmin Exp $ * Created on 2022/3/4 9:49 PM * My blog： https://www.chenmin.info */@Configuration@ConditionalOnClass(NosClient.class)@EnableConfigurationProperties({WangYiOssProperties.class})@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.WANGYI + CharPool.DOT + OssConstant.ENABLE,        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)public class WangYiOssConfiguration {    public static final String DEFAULT_BEAN_NAME = "wangYiOssClient";    @Autowired    private WangYiOssProperties wangYiOssProperties;    @Bean    public void wangYiOssClient() {        Map<String, WangYiOssConfig> wangYiOssConfigMap = wangYiOssProperties.getOssConfig();        if (wangYiOssConfigMap.isEmpty()) {            SpringUtil.registerBean(DEFAULT_BEAN_NAME, wangYiOssClient(wangYiOssProperties));        } else {            String endPoint = wangYiOssProperties.getEndPoint();            String accessKey = wangYiOssProperties.getAccessKey();            String secretKey = wangYiOssProperties.getSecretKey();            wangYiOssConfigMap.forEach((name, wangYiOssConfig) -> {                if (ObjectUtil.isEmpty(wangYiOssConfig.getEndPoint())) {                    wangYiOssConfig.setEndPoint(endPoint);                }                if (ObjectUtil.isEmpty(wangYiOssConfig.getAccessKey())) {                    wangYiOssConfig.setAccessKey(accessKey);                }                if (ObjectUtil.isEmpty(wangYiOssConfig.getSecretKey())) {                    wangYiOssConfig.setSecretKey(secretKey);                }                SpringUtil.registerBean(name, wangYiOssClient(wangYiOssConfig));            });        }    }    public StandardOssClient wangYiOssClient(WangYiOssConfig ossConfig) {        return new WangYiOssClient(nosClient(ossConfig), ossConfig);    }    public NosClient nosClient(WangYiOssConfig ossConfig) {        Credentials credentials = new BasicCredentials(ossConfig.getAccessKey(), ossConfig.getSecretKey());        NosClient nosClient = new NosClient(credentials);        nosClient.setEndpoint(ossConfig.getEndPoint());        return nosClient;    }}