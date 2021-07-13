package com.zto.testcase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xmind.core.marker.IMarkerRef;

import java.util.List;
import java.util.Set;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 上午8:21 2021/5/1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XMindNodeModel {
    String content;
    String comment;
    Set<String> labels;
    Boolean markers;
    String link;
    Integer level;
    String iImage;

    List<XMindNodeModel> children;

    public enum ColumnEnum{
        content,
        comment,
        labels,
        markers,
        link,
        level,
        iImage;
    }
}
