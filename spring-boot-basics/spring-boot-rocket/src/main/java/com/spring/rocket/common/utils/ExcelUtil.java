package com.spring.rocket.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel 工具类
 * <p>基于 EasyExcel 封装，提供简单的导入导出功能</p>
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出 Excel 到 HttpServletResponse (下载)
     *
     * @param response  响应对象
     * @param filename  文件名（不需要后缀）
     * @param sheetName sheet名称
     * @param head      表头类
     * @param data      数据列表
     * @param <T>       泛型
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head, List<T> data) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String encodedFileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), head)
                    .sheet(sheetName)
                    // 自动列宽策略
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .doWrite(data);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            // 重置response，尝试返回JSON错误信息
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            try {
                response.getWriter().println("{\"code\":500,\"message\":\"导出Excel失败: " + e.getMessage() + "\"}");
            } catch (IOException ex) {
                log.error("写出错误响应失败", ex);
            }
        }
    }

    /**
     * 导出 Excel 到本地文件 (用于测试)
     *
     * @param filePath  文件路径
     * @param sheetName sheet名称
     * @param head      表头类
     * @param data      数据列表
     * @param <T>       泛型
     */
    public static <T> void write(String filePath, String sheetName, Class<T> head, List<T> data) {
        EasyExcel.write(filePath, head)
                .sheet(sheetName)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }

    /**
     * 读取 Excel (同步读取，数据量小的时候可以使用)
     *
     * @param inputStream 输入流
     * @param head        表头类
     * @param <T>         泛型
     * @return 数据列表
     */
    public static <T> List<T> readSync(InputStream inputStream, Class<T> head) {
        return EasyExcel.read(inputStream).head(head).sheet().doReadSync();
    }
    
    /**
     * 读取 Excel (同步读取，文件路径)
     *
     * @param filePath 文件路径
     * @param head     表头类
     * @param <T>      泛型
     * @return 数据列表
     */
    public static <T> List<T> readSync(String filePath, Class<T> head) {
        return EasyExcel.read(filePath).head(head).sheet().doReadSync();
    }

    /**
     * 读取 Excel (异步读取，处理大数据量)
     *
     * @param inputStream 输入流
     * @param head        表头类
     * @param consumer    数据消费者 (每解析一批数据回调一次)
     * @param <T>         泛型
     */
    public static <T> void read(InputStream inputStream, Class<T> head, Consumer<List<T>> consumer) {
        EasyExcel.read(inputStream, head, new DataListener<>(consumer)).sheet().doRead();
    }

    /**
     * 通用监听器
     */
    private static class DataListener<T> implements ReadListener<T> {
        
        // 默认每批处理 100 条
        private static final int BATCH_COUNT = 100;
        private final List<T> cachedDataList = new ArrayList<>(BATCH_COUNT);
        private final Consumer<List<T>> consumer;

        public DataListener(Consumer<List<T>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            cachedDataList.add(data);
            if (cachedDataList.size() >= BATCH_COUNT) {
                consumer.accept(cachedDataList);
                cachedDataList.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 处理剩余的数据
            if (!cachedDataList.isEmpty()) {
                consumer.accept(cachedDataList);
            }
        }
    }
}
