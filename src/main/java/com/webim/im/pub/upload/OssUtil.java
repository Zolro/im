package com.webim.im.pub.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OssUtil {

  static Logger logger = LoggerFactory.getLogger(OssUtil.class);

  // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
  // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
  // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
  // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
  // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
  private static final String endpoint;

  // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
  // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
  // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
  // private static String accessKeyId = "<yourAccessKeyId>";
  private static final String accessKeyId;
  // private static String accessKeySecret = "<yourAccessKeySecret>";
  private static final String accessKeySecret;

  // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
  // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
  private static final String bucketName;

  // 自定义域名绑定
  private static final String cname;
  // 文件上传路径的匹配字符串
  private static final String pathPattern; // eg. public/upload2/%m/%b/ %m:模块名,%b:基类名

  // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
  // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
  private static final OSSClient ossClient;

  static {
    try (InputStream inStream = OssUtil.class.getResourceAsStream("/oss2.properties")) {
      String file = OssUtil.class.getResource("/oss2.properties").getPath();
      Properties p = new Properties();
      p.load(inStream);
      endpoint = p.getProperty("endpoint");
      accessKeyId = p.getProperty("AccessKeyID");
      accessKeySecret = p.getProperty("AccessKeySecret");
      bucketName = p.getProperty("shoestp.bucket");
      cname = p.getProperty("shoestp.cname");
      pathPattern = p.getProperty("shoestp.path.pattern");
      ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
      initBucket();
    } catch (IOException e) {
      throw new AssertionError();
    }
  }

  private static final void initBucket() {
    // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
    // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
    if (ossClient.doesBucketExist(bucketName)) {
      logger.info("使用已存在的Bucket：" + bucketName + "。");
    } else {
      logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
      // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
      // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
      ossClient.createBucket(bucketName);
    }

    // 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
    // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
    BucketInfo info = ossClient.getBucketInfo(bucketName);
    logger.info("Bucket " + bucketName + "的信息如下：");
    logger.info("\t数据中心：" + info.getBucket().getLocation());
    logger.info("\t创建时间：" + info.getBucket().getCreationDate());
    logger.info("\t用户标志：" + info.getBucket().getOwner());
  }

  // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
  // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。

  /**
   * @param beanClass 根据基类将文件保存到不同的目录,用于计算fileKey
   * @param filename 上传文件的文件名,用于计算fileKey
   * @param file 待上传的文件
   * @return 返回图片的url地址
   * @author yingjianhua
   */
  public static <T> String upload(
      Class<T> beanClass, String filename, File file) {
    String fileKey =
        pathPattern.replaceAll("%m", moduleName(beanClass)).replaceAll("%b", beanName(beanClass))
            + filename;
    logger.info("upload:" + fileKey);
    return upload(fileKey, file);
  }

  private static String upload(String fileKey, File file) {
    ossClient.putObject(bucketName, fileKey, file);
    //    	String url =
    // cname==null?("http://"+bucketName+"."+endpoint+"/"+fileKey):("http://"+cname+"/"+fileKey);
    return "/" + fileKey;
  }

  // 取bean的模块名
  public static <T > String moduleName(Class<T> beanClass) {
    String[] s = beanClass.getName().split("\\.");
    return s[s.length - 2];
  }

  // 取bean的除去模块名后的剩余部分,并且首字母为小写
  public static <T> String beanName(Class<T> beanClass) {
    String s = beanClass.getSimpleName();
    for (int i = 1; i < s.length(); i++)
      if (Character.isUpperCase(s.charAt(i)))
        return Character.toLowerCase(s.charAt(i)) + s.substring(i + 1);
    return s;
  }
}
