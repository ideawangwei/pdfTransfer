package com.bonc.pdf.util.openoffice;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ldf on 2018/3/2.
 * 文件类型枚举类
 */
public enum FileTypeEnum {
    TYPE_PDF("pdf"),
    TYPE_DOC("doc"),
    TYPE_DOCX("docx"),
    TYPE_XLS("xls"),
    TYPE_XLSX("xlsx"),
    TYPE_PPT("ppt"),
    TYPE_PPTX("pptx"),
    TYPE_TXT("txt"),
    TYPE_ODT("ODT");
    private String type;
    FileTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
