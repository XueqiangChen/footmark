package com.example.footmark.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class HdfsFileSystemDemoTest {

    private MiniDFSCluster cluster;

    private FileSystem fs;

    @Before
    public void setUp() throws Exception {
        Configuration conf = new Configuration();
        if (System.getProperty("test.build.data") == null) {
            System.setProperty("test.build.data", "/tmp");
        }
        cluster = new MiniDFSCluster.Builder(conf).build();
        fs = cluster.getFileSystem();
        OutputStream out = fs.create(new Path("/dir/file"));
        out.write("content".getBytes("UTF-8"));
        out.close();
    }

    @After
    public void tearDown() throws Exception {
        if (fs != null) {
            fs.close();
        }
        if (cluster != null) {
            cluster.close();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void throwsFileNotFoundForNonExistentFile() throws IOException {
        // https://xie.infoq.cn/article/271644ffa7201ebfecff223ef
        // Failed to find winutils.exe
        fs.getFileStatus(new Path("no-such-file"));
    }

    @Test
    public void fileStatusForFile() throws IOException {
        Path file = new Path("/dir/file");
        FileStatus stat = fs.getFileStatus(file);
        assertEquals("/dir/file", stat.getPath().toUri().getPath());
        assertFalse(stat.isDirectory());
        assertEquals(7L, stat.getLen());
        assertTrue(stat.getModificationTime() <= System.currentTimeMillis());
        assertEquals(1, stat.getReplication());
        assertEquals(128 * 1024 * 1024L, stat.getBlockSize());
        assertEquals(System.getProperty("user.name"), stat.getOwner());
        assertEquals("supergroup", stat.getGroup());
        assertEquals("rw-r--r--", stat.getPermission().toString());
    }
}