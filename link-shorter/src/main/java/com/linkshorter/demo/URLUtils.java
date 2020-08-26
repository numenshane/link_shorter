package com.linkshorter.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class URLUtils {

    /**
     * 判断一个字符串是否为url
     *
     * @param str String 字符串
     * @return boolean 是否为url
     * @author peng1 chen
     **/

    public static boolean isURL(String str) {

        //转换为小写

        str = str.toLowerCase();

        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms

                               + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@

                               + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184

                               + "|" // 允许IP和DOMAIN（域名）

                               + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.

                               + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名

                               + "[a-z]{2,6})" // first level domain- .com or .museum

                               + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数

                               + "((/?)|" // a slash isn't required if there is no file name

                               + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";

        return str.matches(regex);

    }


    public static String getDomain(String domain, int port) throws UnknownHostException {
        if (domain.equals("localhost") || domain.equals("127.0.0.1")) {
            return "http://" + domain + ":" + port + "/";
        }
        if (URLUtils.isIp(domain)) {
            return "https://" + domain + ":" + port + "/";
        }
        return "https://" + domain + "/";
    }

    private static boolean isIp(String IP){//判断是否是一个IP
        boolean b = false;
        IP = URLUtils.replaceWhiteSpace(IP);
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
            String s[] = IP.split("\\.");
            if(Integer.parseInt(s[0])<255)
                if(Integer.parseInt(s[1])<255)
                    if(Integer.parseInt(s[2])<255)
                        if(Integer.parseInt(s[3])<255)
                            b = true;
        }
        return b;
    }

    private static String replaceWhiteSpace(String IP){//去掉IP字符串前后所有的空格
        while(IP.startsWith(" ")){
            IP= IP.substring(1,IP.length()).trim();
        }
        while(IP.endsWith(" ")){
            IP= IP.substring(0,IP.length()-1).trim();
        }
        return IP;
    }

}
