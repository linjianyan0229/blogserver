package com.example.blogserver.util;

import com.example.blogserver.vo.TocItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 工具：从正文解析目录（TOC）
 */
public final class MarkdownUtil {

    private MarkdownUtil() {
    }

    /** 匹配 1-3 级标题：# / ## / ### */
    private static final Pattern HEADING = Pattern.compile("^(#{1,3})\\s+(.+?)\\s*#*$");

    /**
     * 解析 Markdown 标题生成目录
     */
    public static List<TocItem> extractToc(String content) {
        List<TocItem> toc = new ArrayList<>();
        if (content == null || content.isBlank()) {
            return toc;
        }
        boolean inCodeBlock = false;
        int index = 0;
        for (String line : content.split("\\r?\\n")) {
            String trimmed = line.trim();
            // 跳过 ``` 代码块内的 #
            if (trimmed.startsWith("```")) {
                inCodeBlock = !inCodeBlock;
                continue;
            }
            if (inCodeBlock) {
                continue;
            }
            Matcher m = HEADING.matcher(trimmed);
            if (m.matches()) {
                int level = m.group(1).length();
                String text = m.group(2).trim();
                toc.add(new TocItem(level, text, "heading-" + index));
                index++;
            }
        }
        return toc;
    }
}
