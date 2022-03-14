--  DROP TABLE OSS_STORE;
--  DROP TABLE OSS_DATA;

CREATE TABLE OSS_STORE
(
    ID               VARCHAR2(50) PRIMARY KEY,
    NAME             VARCHAR2(100) NOT NULL,
    PATH             VARCHAR2(500) NOT NULL,
    SIZE             NUMBER,
    CREATE_TIME      DATE,
    LAST_UPDATE_TIME DATE,
    PARENT_ID        VARCHAR2(50) DEFAULT 0,
    TYPE             VARCHAR2(2)   NOT NULL,
    DATA_ID          VARCHAR2(50)
);

COMMENT ON TABLE OSS_STORE IS '对象存储';
COMMENT ON COLUMN OSS_STORE.ID IS '主键';
COMMENT ON COLUMN OSS_STORE.NAME IS '文件/目录名';
COMMENT ON COLUMN OSS_STORE.PATH IS '路径';
COMMENT ON COLUMN OSS_STORE.SIZE IS '文件大小';
COMMENT ON COLUMN OSS_STORE.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN OSS_STORE.LAST_UPDATE_TIME IS '最新修改时间';
COMMENT ON COLUMN OSS_STORE.PARENT_ID IS '父级目录ID';
COMMENT ON COLUMN OSS_STORE.TYPE IS '类型(D:目录;F:文件)';
COMMENT ON COLUMN OSS_STORE.DATA_ID IS '数据ID';

CREATE TABLE OSS_DATA
(
    ID   VARCHAR2(50) PRIMARY KEY,
    DATA BLOB
);

COMMENT ON TABLE OSS_DATA IS '数据';
COMMENT ON COLUMN OSS_DATA.ID IS '主键';
COMMENT ON COLUMN OSS_DATA.DATA IS '数据';


