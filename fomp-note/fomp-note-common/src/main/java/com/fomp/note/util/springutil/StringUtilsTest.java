package com.fomp.note.util.springutil;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: ymx
 * @date: 2022/8/22
 * @version: 1.0.0.0
 */
public class StringUtilsTest {
    StringUtils stringUtils;
    /**
     1、字符串判断工具
         // 判断字符串是否为 null，或 ""。注意，包含空白符的字符串为非空
         boolean isEmpty(Object str)
         // 判断字符串是否是以指定内容结束。忽略大小写
         boolean endsWithIgnoreCase(String str, String suffix)
         // 判断字符串是否已指定内容开头。忽略大小写
         boolean startsWithIgnoreCase(String str, String prefix) // 是否包含空白符
         boolean containsWhitespace(String str)
         // 判断字符串非空且长度不为 0，即，Not Empty
         boolean hasLength(CharSequence str)
         // 判断字符串是否包含实际内容，即非仅包含空白符，也就是 Not Blank
         boolean hasText(CharSequence str)
         // 判断字符串指定索引处是否包含一个子串。
         boolean substringMatch(CharSequence str, int index, CharSequence substring)
         // 计算一个字符串中指定子串的出现次数
         int countOccurrencesOf(String str, String sub)
     2、字符串操作工具
         // 查找并替换指定子串
         String replace(String inString, String oldPattern, String newPattern)
         // 去除尾部的特定字符
         String trimTrailingCharacter(String str, char trailingCharacter) // 去除头部的特定字符
         String trimLeadingCharacter(String str, char leadingCharacter)
         // 去除头部的空白符
         String trimLeadingWhitespace(String str)
         // 去除头部的空白符
         String trimTrailingWhitespace(String str)
         // 去除头部和尾部的空白符
         String trimWhitespace(String str)
         // 删除开头、结尾和中间的空白符
         String trimAllWhitespace(String str)
         // 删除指定子串
         String delete(String inString, String pattern)
         // 删除指定字符（可以是多个）
         String deleteAny(String inString, String charsToDelete)
         // 对数组的每一项执行 trim() 方法
         String[] trimArrayElements(String[] array)
         // 将 URL 字符串进行解码
         String uriDecode(String source, Charset charset)
     3、路径相关工具方法
         // 解析路径字符串，优化其中的 “..”
         String cleanPath(String path)
         // 解析路径字符串，解析出文件名部分
         String getFilename(String path)
         // 解析路径字符串，解析出文件后缀名
         String getFilenameExtension(String path)
         // 比较两个两个字符串，判断是否是同一个路径。会自动处理路径中的 “..”
         boolean pathEquals(String path1, String path2)
         // 删除文件路径名中的后缀部分
         String stripFilenameExtension(String path) // 以 “. 作为分隔符，获取其最后一部分
         String unqualify(String qualifiedName)
         // 以指定字符作为分隔符，获取其最后一部分
         String unqualify(String qualifiedName, char separator)
     */
}
