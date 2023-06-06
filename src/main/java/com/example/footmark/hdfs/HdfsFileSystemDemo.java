package com.example.footmark.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;
import java.net.URL;

public class HdfsFileSystemDemo {

    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    public static void urlCat(String url) throws Exception {
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            IOUtils.copyBytes(in, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(in);
        }
    }

    public static void fileSystemCat(String url) throws Exception {
        String uri = url;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        InputStream in = null;
        try {
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(in);
        }
    }

    // 实用 seek() 方法，将 Hadoop 文件系统中的一个文件在标准输出上显示两次。
    // seek() 方法是一种相对高开销的操作。
    // Run: hadoop FileSystemDoubleCat hdfs://localhost/user/tom/quangle.txt
    public static void fileSystemDoubleCat(String url) throws Exception {
        String uri = url;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FSDataInputStream in = null;
        try {
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in, System.out, 4096, false);
            in.seek(0); // go back to the start of the file
            IOUtils.copyBytes(in, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(in);
        }
    }

    // 将本地文件复制到 Hadoop 文件系统
    // Run: hadoop FileCopyWithProgress input/docs/1400-8.txt hdfs://localhost/user/tom/1400-8.txt
    public static void fileCopyWithProgress(String localSrc, String dst) throws Exception {

        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        // creat() 默认会创建父目录，如果不需要创建父目录，应该先通过exist()方法判断
        // boolean exist = fs.exists(new Path(dst));
        // 如果是在文件末尾追加内容的话，使用 append() 方法可以实现
        OutputStream out = fs.create(new Path(dst), new Progressable() {
            @Override
            public void progress() {
                System.out.println(".");
            }
        });

        IOUtils.copyBytes(in, out, 4096, true);
    }
}
