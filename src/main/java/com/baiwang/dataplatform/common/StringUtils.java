package com.baiwang.dataplatform.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtils
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public class StringUtils {
    private static final String EMPTYSTR = "";
    private static final char[] hexStrings = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static String PATTERN_EMAIL = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9_\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    private static String PATTERN_TAX_NO = "^[a-zA-Z0-9]{15,20}$";
    private static String PATTERN_NUMBER = "^[0-9]*$";

    public static boolean isMatchRegex(String matchString, String regexString) {
        boolean flag = false;
        if (!isBlank(matchString)) {
            Pattern pattern = Pattern.compile(regexString);
            Matcher matcher = pattern.matcher(matchString);
            flag = matcher.matches();
            pattern = null;
            matcher = null;
        }

        matchString = null;
        return flag;
    }

    public static boolean isNumber(String numberStr) {
        return isMatchRegex(numberStr, PATTERN_NUMBER);
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            str = str.trim();
            return "".equals(str);
        }
    }

    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean hasBlank(String... str) {
        String[] var1 = str;
        int var2 = str.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            if (isBlank(s)) {
                return true;
            }
        }

        return false;
    }

    public static String getJSONDefault(Object o) {
        return JSONObject.toJSONString(o);
    }

    public static String getJSON(Object o) {
        return JSONObject.toJSONString(o, new SerializerFeature[]{SerializerFeature.WriteMapNullValue});
    }

    public static String getJSONWithDates(Object o) {
        return JSONObject.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
    }

    public static String getJSONWithDate(Object o) {
        return JSONObject.toJSONStringWithDateFormat(o, "yyyy-MM-dd", new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
    }

    public static String hashCode(String Str, boolean isUpperCase) {
        return isUpperCase ? hashCode(Str, "SHA-1").toUpperCase() : hashCode(Str, "SHA-1");
    }

    public static String hashCode(String str, String algorithm) {
        return str == null ? "" : toHexString(digest(str, algorithm));
    }

    public static String toHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            hexString.append(byteHEX(b[i]));
        }

        return hexString.toString();
    }

    public static char[] byteHEX(byte ib) {
        char[] ob = new char[]{hexStrings[ib >>> 4 & 15], hexStrings[ib & 15]};
        return ob;
    }

    public static byte[] digest(String str, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(str.getBytes());
            return digest.digest();
        } catch (Exception var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    public static String sortString(String... strings) {
        StringBuffer sb = new StringBuffer();
        Arrays.sort(strings);

        for(int i = 0; i < strings.length; ++i) {
            sb.append(strings[i]);
        }

        return sb.toString();
    }

    public static String getRandomString() {
        Random random = new Random();
        int length = 5 + random.nextInt(5);
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(chars.length);
            sb.append(chars[number]);
        }

        return sb.toString();
    }

    public static String getKeyValueStr(Object o) {
        StringBuffer sb = new StringBuffer();

        try {
            Field[] fields = o.getClass().getDeclaredFields();

            for(int i = 0; i < fields.length; ++i) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(o);
                if (value instanceof String) {
                    if (!isBlank((String)value)) {
                        sb.append(fields[i].getName() + "=" + value + "&");
                    }
                } else if (value instanceof Integer) {
                    sb.append(fields[i].getName() + "=" + (Integer)value + "&");
                } else if (value instanceof Float) {
                    sb.append(fields[i].getName() + "=" + (Float)value + "&");
                } else if (value instanceof Double) {
                    sb.append(fields[i].getName() + "=" + (Double)value + "&");
                } else if (value instanceof Boolean) {
                    sb.append(fields[i].getName() + "=" + (Boolean)value + "&");
                } else if (value != null) {
                    throw new IllegalArgumentException("the property of object only can be one of String,Integer,Float,Double,Boolean");
                }
            }

            if (sb.charAt(sb.length() - 1) == '&') {
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return sb.toString();
    }

    public static String byte2hex(byte[] b) {
        if (b == null) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            String stmp = "";

            for(int n = 0; n < b.length; ++n) {
                stmp = Integer.toHexString(b[n] & 255);
                if (stmp.length() == 1) {
                    sb.append("0" + stmp);
                } else {
                    sb.append(stmp);
                }
            }

            return sb.toString();
        }
    }

//    public static byte[] hex2byte(String str) {
//        if (str == null) {
//            return null;
//        } else {
//            str = str.trim();
//            int len = str.length();
//            if (len != 0 && len % 2 != 1) {
//                byte[] b = new byte[len / 2];
//
//                try {
//                    for(int i = 0; i < str.length(); i += 2) {
//                        b[i / 2] = (byte)Integer.decode("0X" + str.substring(i, i + 2));
//                    }
//
//                    return b;
//                } catch (Exception var4) {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }
//    }

    private static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
        cipher.init(1, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    private static byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/ECB/PKCS5Padding");
        cipher.init(2, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    public static String des3Encode(String key, String value) {
        byte[] outPutBytes = null;

        try {
            outPutBytes = des3EncodeECB(key.getBytes("UTF-8"), value.getBytes());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return byte2hex(outPutBytes);
    }

//    public static String des3Decode(String key, String value) {
//        byte[] outPutBytes = null;
//
//        try {
//            outPutBytes = des3DecodeECB(key.getBytes("UTF-8"), hex2byte(value));
//        } catch (Exception var4) {
//            var4.printStackTrace();
//        }
//
//        return new String(outPutBytes);
//    }

    public static String getXml(String nsrsbh, String jrdm, String fwdm, String xml) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><fwpackage><jsnr><fwdm>" + fwdm + "</fwdm><nsrsbh>" + nsrsbh + "</nsrsbh><jrdm>" + jrdm + "</jrdm></jsnr><ywnr><![CDATA[" + xml + "]]></ywnr></fwpackage>";
    }

    public static String getInvoiceXml(String jr_nsrsbh, String kp_nsrsbh, String fpqqlsh, String ghdwmc, String ghdwdm, String ghdwdzdh, String ghdwyhzh, String dh, String yx) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<fwpackage>\r\n\t<jsnr>\r\n\t\t<fwdm>1031</fwdm>\r\n\t\t<nsrsbh>" + jr_nsrsbh + "</nsrsbh>\r\n\t</jsnr>\r\n\t<ywnr><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n\t\t<request>\r\n\t\t\t<nsrsbh>" + kp_nsrsbh + "</nsrsbh>\r\n\t\t\t<fpqqlsh>" + fpqqlsh + "</fpqqlsh>\r\n\t\t\t<ghdwmc>" + ghdwmc + "</ghdwmc>\r\n\t\t\t<ghdwdm>" + (ghdwdm == null ? "" : ghdwdm.trim()) + "</ghdwdm>\r\n\t\t\t<ghdwdzdh>" + (ghdwdzdh == null ? "" : ghdwdzdh.trim()) + "</ghdwdzdh>\r\n\t\t\t<ghdwyhzh>" + (ghdwyhzh == null ? "" : ghdwyhzh.trim()) + "</ghdwyhzh>\r\n\t\t\t<dh>" + (dh == null ? "" : dh.trim()) + "</dh>\r\n\t\t\t<yx>" + (yx == null ? "" : yx.trim()) + "</yx>\r\n\t\t\t<tsqdbz>2</tsqdbz>\r\n\t\t</request>]]></ywnr>\r\n</fwpackage>";
    }

    public static Date calendarDay(Integer n) {
        Calendar curr = Calendar.getInstance();
        curr.set(5, curr.get(5) - n);
        return curr.getTime();
    }

    public static Date calendarMonth(Integer n) {
        Calendar curr = Calendar.getInstance();
        curr.set(2, curr.get(2) - n);
        return curr.getTime();
    }

    public static Date calendarYear(Integer n) {
        Calendar curr = Calendar.getInstance();
        curr.set(1, curr.get(1) - n);
        return curr.getTime();
    }

    public static void main(String[] args) {
        System.out.println(getInvoiceXml((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null));
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getErrInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException var9) {
                    var9.printStackTrace();
                }
            }

            if (pw != null) {
                pw.close();
            }

        }

        return sw.toString();
    }

    public static boolean isEmail(String email) {
        return isMatchRegex(email, PATTERN_EMAIL);
    }

    public static boolean isTaxNo(String taxNo) {
        return isMatchRegex(taxNo, PATTERN_TAX_NO);
    }
}

