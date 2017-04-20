package zalezone.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.UUID;

public class FileUtils {

    private static final String DIR_IMAGE = "local_image";
    private static final String DIR_RECORD = "local_record";
    private static final String FRESCO_CACHE = "fresco_img";
    private static final String DIR_DATA_ROOT = "/data/data/";
    private static final String DIR_INTERNAL_CACHE = "cache";


    public static String getInternalCache(Context context) {
        String path = DIR_DATA_ROOT + context.getPackageName();
        if (!TextUtils.isEmpty(path)) {
            String dir = path + File.separator + DIR_INTERNAL_CACHE;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static String getFrescoCache(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String dir = path + File.separator + FRESCO_CACHE;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static String getImageCachePath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + DIR_IMAGE;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return "";
    }

    public static File newImageCacheFile(Context context) {
        String root = getImageCachePath(context);
        if (!TextUtils.isEmpty(root)) {
            return new File(root, UUID.randomUUID().toString() + ".jpg");
        }
        return null;
    }

    public static String getCachePath(Context context) {
        String state = android.os.Environment.getExternalStorageState();
        String path = "";
        if (state != null && state.equals(android.os.Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= 8) {
                File file = context.getExternalCacheDir();
                if (file != null) {
                    path = file.getAbsolutePath();
                }
                if (TextUtils.isEmpty(path)) {
                    path = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();
                }
            } else {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
            }
        } else if (context.getCacheDir() != null) {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }

    public static File getRecordPath(Context context) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + DIR_RECORD;
            File file = new File(root);
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        }
        return null;
    }

    public static void writeObject(Context context, Object obj, String fileName) {
        String path = getCachePath(context) + File.separator + fileName;
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Object readObject(Context context, String fileName) {
        String path = getCachePath(context) + File.separator + fileName;
        FileInputStream fileInputStream = null;
        ObjectInputStream input = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
            input = new ObjectInputStream(fileInputStream);
            return input.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static void deleteFile(String path) {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                File[] file = f.listFiles();
                if (file != null) {
                    for (File file2 : file) {
                        deleteFile(file2.toString());
                        file2.delete();
                    }
                }
                f.delete();
            } else {
                f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getFileBytes(String pathName) {
        File file = new File(pathName);
        FileInputStream fis = null;
        byte[] retBytes = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                int len = fis.available();
                retBytes = new byte[len];
                fis.read(retBytes);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return retBytes;
    }

    public static String getFileMimeType(String path) {
        File f = new File(path);
        try {
            return f.toURL().openConnection().getContentType();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检测内存卡是否可用
     */
    public static boolean isSDcardUsable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取内存卡路径
     */
    public static File getSDcardDir() {
        if (isSDcardUsable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    public static String getEntryPath(Context context, String dirName) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            File file = new File(new File(path).getParentFile(), dirName);
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    public static String getOldEntryPath(Context context, String dirName) {
        String path = getCachePath(context);
        if (!TextUtils.isEmpty(path)) {
            String root = path + File.separator + dirName;
            File file = new File(root);
            if (file.exists()) return file.getAbsolutePath();
        }
        return "";
    }

    public static void writeFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(byte[] bfile, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        if(flist != null && flist.length > 0){
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        DecimalFormat df = new DecimalFormat("#.00");
        double formatSize = 0d;
        if (size >= gb) {
            formatSize = Double.valueOf(df.format(size * 1.0 / gb));
            return formatSize + "GB";
        } else if (size >= mb) {
            formatSize = Double.valueOf(df.format(size * 1.0 / mb));
            return formatSize + "MB";
        } else {
            formatSize = Double.valueOf(df.format(size * 1.0 / kb));
            if (size == 0)
                return "0KB";
            return formatSize + "KB";
        }
    }

    /**
<<<<<<< HEAD
     * 获取本地路径的文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        String result = "";
        if (!TextUtils.isEmpty(filePath)) {
            String[] strs = filePath.split("/");
            if (strs.length > 0) {
                return strs[strs.length - 1];
            }
        }
        return result;
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if(files != null && files.length > 0){
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
            }
        }
    }
    /**
     * 移动文件
     *
     * @param srcFileName 源文件完整路径
     * @param destDirName 目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName) {

        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();

        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }

    /**
     * 移动目录
     *
     * @param srcDirName  源目录完整路径
     * @param destDirName 目的目录完整路径
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDirectory(String srcDirName, String destDirName) {

        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();

        /**
         * 如果是文件则移动，否则递归移动文件夹。删除最终的空源文件夹
         * 注意移动文件夹时保持文件夹的树状结构
         */
        File[] sourceFiles = srcDir.listFiles();
        if(sourceFiles != null && sourceFiles.length > 0){
            for (File sourceFile : sourceFiles) {
                if (sourceFile.isFile()) {
                    moveFile(sourceFile.getAbsolutePath(), destDir.getAbsolutePath());
                }else if (sourceFile.isDirectory()) {
                    moveDirectory(sourceFile.getAbsolutePath(),
                            destDir.getAbsolutePath() + File.separator + sourceFile.getName());
                }
            }
        }
        deleteFile(srcDirName);
        return true;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable throwable) {
            }
        }
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable == null) continue;
            try {
                closeable.close();
            } catch (Throwable throwable) {
            }
        }
    }
}
