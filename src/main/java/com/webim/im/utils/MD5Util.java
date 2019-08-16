package com.webim.im.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static String getRealPath(String path) {
        path = ClassLoader.getSystemClassLoader().getResource(path).getPath();
        return path;
    }
    private static String getMD5(String path, Boolean real) {
        return getMD5(real?path:getRealPath(path));
    }
    public static String getMD5(String pathname) {
        return getMD5(new File(pathname));
    }
    public static String getMD5(File file) {
        FileInputStream input= null;
        try {
            input = new FileInputStream(file);
            MappedByteBuffer buffer = input.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(buffer);
            BigInteger bi = new BigInteger(1, digest.digest());
            System.out.println(bi.toString(16)+"   length:"+bi.toString(16).length());
            return bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input!=null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        String path = "C:/Users/yjh/Desktop/6132ad1dffe019a67f0e2d1752cec6ba.jpg";
        System.out.println(getMD5(new File(path)));
    }
    //打印obj对象的field和value
    private static void showField(Object obj) {
        System.out.println("----------------------"+obj.getClass().getSimpleName());
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields) {
            Method method;
            try {
                method = obj.getClass().getMethod("get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1));
                System.out.println(field.getName()+":"+method.invoke(obj));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------------------");
    }
    //若该路径所代表的文件不存在，会创建该文件及其所在路径
    private static File createFile(String path) {
        File file = new File(path);
        System.out.println("file:"+file.getAbsolutePath());
        if(!file.exists()) {
            if(file.getParentFile()==null) {
                new File(file.getParent()).mkdirs();
                System.out.println("mkdirs:"+file.getParent());
            }
            try {
                file.createNewFile();
                System.out.println("createNewFile:"+file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
