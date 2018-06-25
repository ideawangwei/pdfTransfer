package com.bonc.pdf.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;

/**
 * Created by ldf on 2018/2/27.
 * 图片水印配置
 */
@Data
@Accessors(chain = true)
public class ImageWaterMarkConfigure {

    public int[] color = {160, 32, 240, 128};
    public Object[] font = {"STZhongsong", Font.PLAIN, 20};//水印字体 若放大参数大，字体需适量放大
    public String type = "jpg"; //图片格式
    public int enlarge = 2;//放大参数 0~7
    public float quality = 1f;//1f~0.01f 提高生成的图片质量
    public double[] scale = {1.1, 1.1}; //水印比例

}
