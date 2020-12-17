package com.budwk.app.base.utils;

import org.nutz.lang.util.NutMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wizzer@qq.com
 */
public class FileUtil {

    /**
     * 分页获取文件列表
     *
     * @param basePath   目录
     * @param pageNumber 页码
     * @param pageSize   页大小
     * @param sort       按文件名排序
     * @return 列表
     * @throws Exception
     */
    public static NutMap readListPage(String basePath, Integer pageNumber, Integer pageSize, String sort)
            throws Exception {
        int offset = (pageNumber - 1) * pageSize;
        int limit = pageNumber * pageSize;
        long total = 0;
        List<NutMap> list = new ArrayList<>();
        Comparator<Path> comparator = Comparator.naturalOrder();
        if ("desc".equals(sort)) {
            comparator = Comparator.reverseOrder();
        }
        try (Stream<Path> fileList = Files.list(Paths.get(basePath))) {
            total = fileList.count();
        }
        try (Stream<Path> fileList = Files.list(Paths.get(basePath)).sorted(comparator).skip(offset)
                .limit(limit)) {
            fileList.forEach(file -> {
                NutMap nutMap = NutMap.NEW();
                String fileName = file.getFileName().toString();
                nutMap.addv("fileName", fileName);
                if (Files.isDirectory(file.toAbsolutePath())) {
                    nutMap.addv("folder", true);
                    nutMap.addv("suffix", "folder");
                } else {
                    String suffix = fileName.substring(fileName.indexOf(".") + 1).toLowerCase();
                    nutMap.addv("folder", false);
                    nutMap.addv("suffix", suffix);
                }
                list.add(nutMap);
            });
            return NutMap.NEW().addv("total", total).addv("list", list);
        }
    }

}
