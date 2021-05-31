package util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class test {
    public static void main(String[] args) {
        test test = new test();
        test.sendMail("验证码","12345","57339509@qq.com");
    }
    public void sendMail(String subject, String content, String receiver) {
        try {
//            log.info("接收人邮箱receiver={}", receiver);
            //创建一个配置文件并保存
            Properties properties = new Properties();
            //发件邮箱主机地址
            properties.setProperty("mail.host","smtp.163.com");
            properties.setProperty("mail.transport.protocol","smtp");
            //发件邮箱主机端口
            properties.setProperty("mail.smtp.port","994");
            properties.setProperty("mail.smtp.auth","true");
            properties.put("mail.smtp.ssl.enable", true);
//            log.info("发件服务器为{}:{}", MAIL_HOST, MAIL_PORT);

            //设置SSL加密
//            if (isSSL){
//                MailSSLSocketFactory sf = new MailSSLSocketFactory();
//                sf.setTrustAllHosts(true);
//                properties.put("mail.smtp.ssl.enable", "true");
//                properties.put("mail.smtp.ssl.socketFactory", sf);
//            }

            //创建一个session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("ql@sylu.edu.cn","r53MFBFRbB2jxzZ1");
                }
            });

//            log.info("发件人邮箱账号密码{}/{}", MAIL_USER, MAIL_PASSWORD);

            //开启debug模式
            session.setDebug("true".equals("true"));
            //获取连接对象
            Transport transport = session.getTransport();
            //连接服务器
            transport.connect();
            //创建邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress("ql@sylu.edu.cn"));
            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(receiver));
            //邮件标题
            mimeMessage.setSubject(subject);
            //邮件内容
            mimeMessage.setContent(content,"text/html;charset=UTF-8");
            //发送邮件
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            //关闭连接
            transport.close();
            System.out.println("邮件发送成功");
//            log.info("邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("邮件发送异常");
//            log.error("邮件发送异常");
        }
    }
}
