package com.bonc.pdf.util;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;

/**
 * Created by ldf on 2018/2/27.
 * pdf水印配置
 */
@Data
@Accessors(chain = true)
public class PdfWaterMarkConfigure {
    //透明度
    public float fillOpacity = 1f;
    public float lineWidth = 0.4f;
    public float[] lineDash = {0.4f, 0.4f, 0.2f};
    public float fontSize = 20;
    public int[] colorFill = {238, 209, 212};
    public int angle = 45;
    //水印密度
    public double density = 1.5;

}
