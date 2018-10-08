package com.ybin.callaudio.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Yanbin on 2018/9/26.
 * 描述:
 */
public class ZipUtils  {
    private static final int BUFF_SIZE = 1024;

    /**
     * @param zos           压缩流
     * @param parentDirName 父目录
     * @param file          待压缩文件
     * @param buffer        缓冲区
     *                      URL：http://www.bianceng.cn/OS/extra/201609/50420.htm
     * @return 只要目录中有一个文件压缩失败，就停止并返回
     */
    private static boolean zipFile(ZipOutputStream zos, String parentDirName, File file, byte[] buffer) {
        String zipFilePath = parentDirName + file.getName();
        if (file.isDirectory()) {
            zipFilePath += File.separator;
            for (File f : file.listFiles()) {
                if (!zipFile(zos, zipFilePath, f, buffer)) {
                    return false;
                }
            }
            return true;
        } else {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                ZipEntry zipEntry = new ZipEntry(zipFilePath);
                zipEntry.setSize(file.length());
                zos.putNextEntry(zipEntry);
                while (bis.read(buffer) != -1) {
                    zos.write(buffer);
                }
                bis.close();
                return true;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * @param srcPath 待压缩的文件或目录
     * @param dstPath 压缩后的zip文件
     * @return 只要待压缩的文件有一个压缩失败就停止压缩并返回(等价于windows上直接进行压缩)
     */
    public static boolean zipFile(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return false;
        }
        byte[] buffer = new byte[BUFF_SIZE];
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dstPath));
            boolean result = zipFile(zos, "", srcFile, buffer);
            zos.close();
            return result;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param srcPath 待解压的zip文件
     * @param dstPath zip解压后待存放的目录
     * @return 只要解压过程中发生错误，就立即停止并返回(等价于windows上直接进行解压)
     */
    public static boolean unzipFile(String srcPath, String dstPath) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            return false;
        }
        File srcFile = new File(srcPath);
        if (!srcFile.exists() || !srcFile.getName().toLowerCase(Locale.getDefault()).endsWith("zip")) {
            return false;
        }
        File dstFile = new File(dstPath);
        if (!dstFile.exists() || !dstFile.isDirectory()) {
            dstFile.mkdirs();
        }
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
            BufferedInputStream bis = new BufferedInputStream(zis);
            ZipEntry zipEntry = null;
            byte[] buffer = new byte[BUFF_SIZE];
            if (!dstPath.endsWith(File.separator)) {
                dstPath += File.separator;
            }
            while ((zipEntry = zis.getNextEntry()) != null) {
                String fileName = dstPath + zipEntry.getName();
                File file = new File(fileName);
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                while (bis.read(buffer) != -1) {
                    fos.write(buffer);
                }
                fos.close();
            }
            bis.close();
            zis.close();
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
