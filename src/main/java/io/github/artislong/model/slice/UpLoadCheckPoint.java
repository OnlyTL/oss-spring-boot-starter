package io.github.artislong.model.slice;

import cn.hutool.core.io.IoUtil;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**断点对象
 *
 * @author 陈敏
 * @version UpLoadCheckPoint.java, v 1.1 2022/2/9 22:52 chenmin Exp $
 * Created on 2022/2/9
 */
@Data
public class UpLoadCheckPoint implements Serializable {

    private static final long serialVersionUID = 5424904565837227164L;

    public static final String UPLOAD_MAGIC = "FE8BB4EA-B593-4FAC-AD7A-2459A36E2E62";

    private String magic;
    private int md5;
    private String uploadFile;
    private FileStat uploadFileStat;
    private String key;
    private String bucket;
    private String checkpointFile;
    private String uploadId;
    private List<UploadPart> uploadParts = Collections.synchronizedList(new ArrayList<>());
    private List<PartEntityTag> partEntityTags = Collections.synchronizedList(new ArrayList<>());
    private long originPartSize;

    /**
     * 从缓存文件中加载断点数据
     * @param checkpointFile 断点缓存文件
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized void load(String checkpointFile) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(checkpointFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        UpLoadCheckPoint ucp = (UpLoadCheckPoint) objectInputStream.readObject();
        assign(ucp);
        IoUtil.close(objectInputStream);
        IoUtil.close(inputStream);
    }

    /**
     * 将断点信息写入到断点缓存文件
     * @param checkpointFile 断点缓存文件
     * @throws IOException
     */
    public synchronized void dump(String checkpointFile) throws IOException {
        this.setMd5(hashCode());
        FileOutputStream fileOutputStream = new FileOutputStream(checkpointFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        IoUtil.close(objectOutputStream);
        IoUtil.close(fileOutputStream);
    }

    /**
     * 更新分块状态
     *
     * @throws IOException
     */
    public synchronized void update(int partIndex, PartEntityTag partEntityTag, boolean completed) throws IOException {
        this.getPartEntityTags().add(partEntityTag);
        this.getUploadParts().get(partIndex).setCompleted(completed);
    }

    /**
     * 检查断点缓存文件是否与断点一致
     */
    public synchronized boolean isValid(String checkpointFile) {
        // 比较checkpoint的magic和md5
        if (this.getMagic() == null || !this.getMagic().equals(UPLOAD_MAGIC) || this.getMd5() != hashCode()) {
            return false;
        }
        File file = new File(checkpointFile);
        // 检查断点缓存文件是否存在
        if (!file.exists()) {
            return false;
        }

        // 文件名，大小和上次修改时间必须与当前断点相同。
        // 如果有任何改变，则重新上传
        return this.getUploadFileStat().getSize() == file.length()
                && this.getUploadFileStat().getLastModified() == file.lastModified();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
        result = prime * result + ((checkpointFile == null) ? 0 : checkpointFile.hashCode());
        result = prime * result + ((magic == null) ? 0 : magic.hashCode());
        result = prime * result + ((partEntityTags == null) ? 0 : partEntityTags.hashCode());
        result = prime * result + ((uploadFile == null) ? 0 : uploadFile.hashCode());
        result = prime * result + ((uploadFileStat == null) ? 0 : uploadFileStat.hashCode());
        result = prime * result + ((uploadId == null) ? 0 : uploadId.hashCode());
        result = prime * result + ((uploadParts == null) ? 0 : uploadParts.hashCode());
        result = prime * result + (int) originPartSize;
        return result;
    }

    public void assign(UpLoadCheckPoint ucp) {
        this.setMagic(ucp.magic);
        this.setMd5(ucp.md5);
        this.setUploadFile(ucp.uploadFile);
        this.setUploadFileStat(ucp.uploadFileStat);
        this.setKey(ucp.key);
        this.setBucket(ucp.bucket);
        this.setCheckpointFile(ucp.checkpointFile);
        this.setUploadId(ucp.uploadId);
        this.setUploadParts(ucp.uploadParts);
        this.setPartEntityTags(ucp.partEntityTags);
        this.setOriginPartSize(ucp.originPartSize);
    }

}
