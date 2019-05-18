# utils
常用工具类<br/>
RedisLock.java redis简单锁实现秒杀<br/>
SerialNumberUtil.java 生成数字序列<br/>
SftpUtil.java sftp连接工具类

# http使用TLSv1.2协议上传文件
由于jdk1.6不支持TLSv1.2协议，需要使用第三方加密包bcprov-jdk15on-160.jar，工具类为TLSSocketConnectionFactory.java，上传大文件参考：HttpUtils.txt

# jdk版本低导致加密算法不支持
调用外部服务报错：com.jcraft.jsch.JSchException: Algorithm negotiation fail jdk版本低有可能会有这个算法方面的问题，配置使用BouncyCastle 的JCE provider，在目录<java_home>\jre\lib\security\java.security 添加security.provider.10=org.bouncycastle.jce.provider.BouncyCastleProvider 使用第三方bcprov-jdk15on-160.jar加密算法可解决，使用代码：JCEInstallTest.java 测试配置是否成功，不报错即可。

# 文件解压
工具类： FileUtils

# java sftp
参考：file-server.jar

# java发送邮件及短信
参考：mail和sms

# java mina框架
mina是基于nio开发出来的一个无阻塞高性能通信框架，能够处理socket无法解决的高并发问题
参考：mina