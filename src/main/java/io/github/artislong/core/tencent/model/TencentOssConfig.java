package io.github.artislong.core.tencent.model;

import io.github.artislong.model.SliceConfig;
import io.github.artislong.utils.PathUtil;
import lombok.Data;

/**
 * @author 陈敏
 * @version TencentOssConfig.java, v 1.1 2022/2/20 9:10 chenmin Exp $
 * Created on 2022/2/20
 */
@Data
public class TencentOssConfig {

    private String basePath;
    private String bucketName;
    private String secretId;
    private String secretKey;
    private String region;

    /**
     * 断点续传参数
     */
    private SliceConfig sliceConfig = new SliceConfig();

    public void valid() {
        this.sliceConfig.init();
        basePath = PathUtil.valid(basePath);
    }

}
