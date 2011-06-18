package org.mybatisorm.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class StringUtil {
    private static final String UNSAFED_URL_CHARS = " ()[]{}";
    private static final String DOMAIN_PREFIX = "www.";
    private static final String HTTP_PROTOCOL = "http://";
    private static Logger log = Logger.getLogger(StringUtil.class);
    public static final int EMPTY_INT = 0;
    public static final long EMPTY_LONG = 0;

    public static final int ATYPE_AUTH_BBS = 128; //게시글 권한
    public static final int ATYPE_2 = 64;
    public static final int ATYPE_AUTH_MEMBER = 32; //사용자 권한
    public static final int ATYPE_4 = 16;
    public static final int ATYPE_5 = 8;
    public static final int ATYPE_6 = 4;
    public static final int ATYPE_7 = 2;
    public static final int ATYPE_8 = 1;

    public static final char[] UPPER_ALPHABETS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    public static final char[] LOWER_ALPHABETS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public static final char[] CHARACTERS_62_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z' };

    public static final char[] CHARACTERS_36_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
	    	'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
	    	'Z'};

	public static final char[] CHARACTERS_26_DIGIT =  {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final Map<Character,Integer> CHARACTERS_62_DIGIT_MAP = new HashMap<Character,Integer>();
    static {
    	for (int i = 0; i < CHARACTERS_62_DIGIT.length; i++) {
    		CHARACTERS_62_DIGIT_MAP.put(CHARACTERS_62_DIGIT[i], i);
    	}
    }
    
    public static final String SE_KEY = "maul.cosoms";

    /**
     * 입력받은 문자열이 null 이면 파라미터로 넘긴 기본값을 리턴한다.
     * null 이 아니라면 입력받은 문자열을 다시 리턴한다.
     */
    public static String withDefaultString(String str, String defaultString) {
        if (str == null) {
            return defaultString;
        }
        return str;
    }

    public static int withDefaultInt(String str) {
        return withDefaultInt(str, EMPTY_INT);
    }

    public static int withDefaultInt(String str, int defaultInt) {
        if (str == null) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultInt;
        }
    }

    public static long withDefaultLong(String str) {
        return withDefaultLong(str, EMPTY_LONG);
    }

    public static long withDefaultLong(String str, long defaultLong) {
        if (str == null) {
            return defaultLong;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return defaultLong;
        }
    }

    /**
     * 문자열의 byte size를 구한다.
     */
    public static int getByteSize(String str) {
        if (str == null) return 0;
        int size = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isTwoByteChar(str.charAt(i)))
                size++;
            size++;
        }
        return size;
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isTwoByteChar(char c) {
        return (c > 127);
    }


    private static final String SEED = "KINQ";
    public static final String CAFE_SEED = "efacefac";

    public static String encodeUrl(String str) throws UnsupportedEncodingException {
        if (str == null) return null;
        return URLEncoder.encode(str, "UTF-8");
    }

    public static String decodeUrl(String str) throws UnsupportedEncodingException {
        if (str == null) return null;
        return URLDecoder.decode(str, "UTF-8");
    }


    public static String escapeHtml(String s) {
        char[] aChar = s.toCharArray();

        int ln = aChar.length;
        for (int i = 0; i < ln; i++) {
//            char c = s.charAt(i);
            char c = aChar[i];
            if (c == '<' || c == '>' || c == '&' || c == '"') {
                StringBuffer b =
                        new StringBuffer(s.substring(0, i));
                switch (c) {
                    case '<': b.append("&lt;"); break;
                    case '>': b.append("&gt;"); break;
                    case '&':
                        if (i < ln - 1 && aChar[i + 1] == '#') {
                            b.append('&');
                        } else {
                            b.append("&amp;");
                        }
                        break;
                    case '"': b.append("&quot;"); break;
                }
                i++;
                int next = i;
                while (i < ln) {
                    c = s.charAt(i);
                    if (c == '<' || c == '>' || c == '&' || c == '"') {
                        b.append(s.substring(next, i));
                        switch (c) {
                            case '<': b.append("&lt;"); break;
                            case '>': b.append("&gt;"); break;
                            case '&':
                                if (i < ln - 1 && aChar[i + 1] == '#') {
                                    b.append('&');
                                } else {
                                    b.append("&amp;");
                                }
                                break;
                            case '"': b.append("&quot;"); break;
                        }
                        next = i + 1;
                    }
                    i++;
                }
                if (next < ln) b.append(s.substring(next));
                s = b.toString();
                break;
            } // if c ==
        } // for
        return s;
    }

    public static String escapeJavaScript(String s) {
        int ln = s.length();
        for (int i = 0; i < ln; i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'' || c == '\\' || c == '>' || c < 0x20) {
                StringBuffer b = new StringBuffer(ln + 4);
                b.append(s.substring(0, i));
                while (true) {
                    if (c == '"') {
                        b.append("\\\"");
                    } else if (c == '\'') {
                        b.append("\\'");
                    } else if (c == '\\') {
                        b.append("\\\\");
                    } else if (c == '>') {
                        b.append("\\>");
                    } else if (c < 0x20) {
                        if (c == '\n') {
                            b.append("\\n");
                        } else if (c == '\r') {
                            b.append("\\r");
                        } else if (c == '\f') {
                            b.append("\\f");
                        } else if (c == '\b') {
                            b.append("\\b");
                        } else if (c == '\t') {
                            b.append("\\t");
                        } else {
                            b.append("\\x");
                            int x = c / 0x10;
                            b.append((char)
                                    (x < 0xA ? x + '0' : x - 0xA + 'A'));
                            x = c & 0xF;
                            b.append((char)
                                    (x < 0xA ? x + '0' : x - 0xA + 'A'));
                        }
                    } else {
                        b.append(c);
                    }
                    i++;
                    if (i >= ln) {
                        return b.toString();
                    }
                    c = s.charAt(i);
                }
            } // if has to be escaped
        } // for each characters
        return s;

    }


    public static String lineFeedToBreakTag(String str) {
        try {
            if (str == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer(str);
            for (int i = 0, intIndex = 0; (intIndex = str.indexOf('\n', intIndex)) > -1; intIndex++, i += 5) { //i += 3 을 5로 수정
                sb.replace(intIndex + i, intIndex + i + 1, "<br />");
            }
            return sb.toString();
        } catch (NullPointerException npe) {
            return str;
        }
    }

    /** 주어진 길이(len)에서 문자열(s)의 길이를 제외한 우측을 특정 character(padc)로 채워준다. */
    public static String rpad(String s, int len, String padc) {
        return rpad(new StringBuilder(s), len, padc).toString();
    }

    public static StringBuilder rpad(StringBuilder sb, int len, String padc) {
        int rlen = sb.length();
        if (rlen >= len) {
            return sb;
        }
        for (int i = rlen; i < len; i++) {
            sb.append(padc);
        }
        return sb;
    }

    public static String rpad(int srcint, int length, String ch) {
        return rpad(String.valueOf(srcint), length, ch);
    }

    /** 주어진 길이(len)에서 문자열(s)의 길이를 제외한 좌측을 특정 character(padc)로 채워준다. */
    public static String lpad(String s, int len, String padc) {
        return lpad(new StringBuilder(s), len, padc).toString();
    }

    public static StringBuilder lpad(StringBuilder sb, int len, String padc) {
        int rlen = sb.length();
        if (rlen >= len) {
            return sb;
        }
        for (int i = rlen; i < len; i++) {
            sb.insert(0, padc);
        }
        return sb;
    }

    public static String lpad(int srcint, int length, String ch) {
        return lpad(String.valueOf(srcint), length, ch);
    }

    /*
     * 한 자리 수 정수를 두 자리의 문자열로 변경한다.
     */
	public static String twoDigit(int number) {
		return lpad(String.valueOf(number),2,"0");
	}
	
    public static String removeControlChar(String str) {
        StringBuffer resultBuffer = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ((c >= 0x00 && c <= 0x1F && c != 0x09 && c != 0x0A)
                    || (c >= 0x80 && c <= 0x9F) || c == 0x2028) {
//                resultBuffer.append(' ');
                resultBuffer.append("&#x");
                resultBuffer.append(Integer.toHexString(c).toUpperCase());
                resultBuffer.append(';');
            } else {
                resultBuffer.append(c);
            }
        }
        return resultBuffer.toString().replace("\\0", "");

    }

    public static String removeExtendedHanguel(String str) {
        return str.replaceAll("&#[0-9]{4};", "");
    }

    public static String stripTags(String html) {
        if (html == null) return null;
        return html.replaceAll("(<([^>]+)>)", "")
            .replaceAll("(&nbsp;)", " ")
            .replaceAll("(&amp;)", "&")
            .replaceAll("(&lt;)", "<")
            .replaceAll("(&gt;)", ">").trim();
    }

    public static String stripSpecificTags(String html, String tag) {
        if (html == null) return null;
        return html.replaceAll("(<" + tag + "([^>]+)>)", "")
            .replaceAll("(&nbsp;)", " ")
            .replaceAll("(&amp;)", "&")
            .replaceAll("(&lt;)", "<")
            .replaceAll("(&gt;)", ">").trim();
    }

    public static String escapeIssueKeywordPath(String str)
            throws UnsupportedEncodingException {
        if (str == null || str.length() < 1) return "";

        StringBuffer buffer = new StringBuffer();
        byte[] bytes = str.getBytes("EUC-KR");
        for (int j = 0; j < 2; j++) {
            String hex = Integer.toHexString(255 & bytes[j]);
            buffer.append("00".substring(hex.length()));
            buffer.append(hex);
            buffer.append("/");
        }
//        buffer.append(URLEncoder.encode(str, "EUC-KR"));
        buffer.append(str);
        return buffer.toString();
    }

    /**
     * camelcase 형태의 문자열을 underbar와 소문자로 조합된 형식으로
     * 바꾼다.
     */
    public static String camelCaseToUnderbar(String camelCased) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < camelCased.length(); i++) {
            char c = camelCased.charAt(i);
            char beforeChar = camelCased.charAt((i - 1 >=0) ? i - 1 : 0);
            if (Character.isUpperCase(c)) {
                if (i > 0 && Character.isLetter(beforeChar)) {
                    buf.append('_');
                }
                c = Character.toLowerCase(c);
            }
            buf.append(c);
        }
        return buf.toString();
    }

    public static String md5(String source) {
        byte[] defaultBytes = source.getBytes();
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            return null;
        }
    }
    
    /**
     * 문자열의 앞부분의 길이를 n만큼 잘라준다.
     * @param str
     * @return
     */
    public static String getHeadChar(String str, int n) {
        if(str == null || str.length() <= n) {
            return str;
        } else {
            return str.substring(0, n);
        }
    }

    /**
     * <p>HTML special character를 변환하여 준다.</p>
     * 자주 사용되는 몇몇 문자(' ', '&', '<', '>', '"')에 한하여만 변환처리한다.<br/>
     * String.replaceAll 을 연달아 사용하는 비효율적인 구조이므로 성능상 튜닝이 필요하다.<br/>
     * iso8859-1 special chararacter entire list : http://www.ramsch.org/martin/uni/fmi-hp/iso8859-1.html
     */
    public static String replaceHtmlSpecialCharacter(String html) {
        return html.replaceAll("&nbsp;", " ")
                   .replaceAll("&quot;", "\"")
                   .replaceAll("&lt;", "<")
                   .replaceAll("&gt;", ">")
                   .replaceAll("&amp;", "&");
    }

    public static String makeLinkedText(String plainText, String cssClass) {
        if (null == plainText) {
            return null;
        }
        plainText = stripTags(plainText);

        try {
            char[] chars = plainText.toCharArray();
            char[] compareBuffer = new char[chars.length];
            char[] protocolChars = HTTP_PROTOCOL.toCharArray();
            char[] domainChars = DOMAIN_PREFIX.toCharArray();
            int idxProtocolChars = 0;
            StringBuilder sb = new StringBuilder();
            boolean isLink = false;

            for (int i = 0; i < chars.length; i++) {
                char currentChar = chars[i];
                if (isLink) {
                    compareBuffer[idxProtocolChars++] = currentChar;
                    if (UNSAFED_URL_CHARS.indexOf(currentChar) >= 0) {
                        sb.append(linkerize(new String(compareBuffer, 0, idxProtocolChars - 1), cssClass));
                        sb.append(currentChar);
                        isLink = false;
                        idxProtocolChars = 0;
                    } else if (i == chars.length - 1) {
                        sb.append(linkerize(new String(compareBuffer, 0, idxProtocolChars), cssClass));
                        idxProtocolChars = 0;
                        isLink = false;
                    }
                } else if (arrayStartWith(plainText.substring(i).toCharArray(), protocolChars)) {
                    System.arraycopy(protocolChars, 0, compareBuffer, 0, protocolChars.length);
                    idxProtocolChars = protocolChars.length;
                    i += protocolChars.length - 1;
                    isLink = true;
                } else if (arrayStartWith(plainText.substring(i).toCharArray(), domainChars)) {
                    System.arraycopy(domainChars, 0, compareBuffer, 0, domainChars.length);
                    idxProtocolChars = domainChars.length;
                    i += domainChars.length - 1;
                    isLink = true;
                } else {
                    sb.append(currentChar);
                }
            }
            return sb.toString();
        } catch (RuntimeException e) {
            log.warn("failed to StringUtil.makeLinkedText(" + plainText + "), " + e);
            return plainText;
        }
    }

    private static boolean arrayStartWith(char[] cs, char[] protocolChars) {
        if (cs.length < protocolChars.length) {
            return false;
        }
        for (int i = 0; i < protocolChars.length; i++) {
            if (cs[i] != protocolChars[i]) {
                return false;
            }
        }
        return true;
    }

    private static String linkerize(String arTemp, String cssClass) {
        StringBuilder sb = new StringBuilder("<a href=\"");
        if (!arTemp.startsWith(HTTP_PROTOCOL))
            sb.append(HTTP_PROTOCOL);
        sb.append(arTemp);
        if (cssClass != null) {
            sb.append("\" class=\"");
            sb.append(cssClass);
        }
        sb.append("\" target=\"_blank\">");
        sb.append(arTemp);
        sb.append("</a>");
        return sb.toString();
    }

    public static String[] mergeArrays(String[] a1, String[] a2) {
        int a1Length = (a1 != null) ? a1.length : 0;
        int a2Length = (a2 != null) ? a2.length : 0;
        int addInfoSize = a1Length + a2Length;

        String[] addInfoAndVideoClip = new String[addInfoSize];
        if (a1 != null) {
            System.arraycopy(a1, 0, addInfoAndVideoClip, 0, a1Length);
        }
        if (a2 != null) {
            System.arraycopy(a2, 0, addInfoAndVideoClip, a1Length, a2Length);
        }
        return addInfoAndVideoClip;
    }

    public static boolean isWideChar(char c) {
        return (c == 126 || c == 37 || c == 38 || (c >= 64 && c<= 92));
    }

    /**
     * 두 스트링 객체를 비교. 둘 다 Null이거나 같은 스트링인지 체크
     * @param str1
     * @param str2
     * @return true 둘 다 Null이거나 같은 string인경우
     *         false otherwise
     */
    public static boolean isSameString(String str1, String str2) {
        if (str1 != null) {
            if (str2 != null) {
                return str1.compareTo(str2) == 0;
            } else { 
                return false;
            }
        } else {
            return (str2 == null);
        }
    }


    private static Pattern embedTagPattern = Pattern.compile(".*<embed[^>]*src=([\"']?)http://flvs.daum.net/flvPlayer.swf"
                + "\\?[^\\s\"']*vid=([a-zA-Z0-9_\\-]+\\$)\\1\\s[^>]*?type=([\"']?)application/x-shockwave-flash\\3\\s[^>]*>.*",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static String detectEmbedTags(String content) {
        Matcher matcher = embedTagPattern.matcher(content);
        // Embed Tag 포함
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    /**
     * XML에서 사용할 수 없는 글자를 제거
     * @param s
     * @return
     */
    public static String clearInvalidXmlChar(String s) {
         if (s == null) return null;
         return s.replaceAll("[\\x00-\\x20&&[^\\x09\\x0A\\x0D]]", " ");
    }

    public static String getNextValue(String str) {
        Pattern numberEndedPattern = Pattern.compile("([0-9]+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = numberEndedPattern.matcher(str);
        if (matcher.find()) {
            String postfix = matcher.group(1);
            String newPostfix = getNextNumber(postfix);
            return str.substring(0, str.length() - postfix.length()) + newPostfix;
        }
        else {
            char lastCharacter = str.charAt(str.length() - 1);
            char nextCharacter = getNextAlphabet(lastCharacter);
            return str.substring(0, str.length() - 1) + nextCharacter;
        }
    }

    /** 다음 순번의 알파벳을 리턴한다. 'Z'에 도달한 경우 다시 'A'를 리턴한다. */
    public static char getNextAlphabet(char aChar) {
        char[] alphabets = (Character.isUpperCase(aChar)) ? UPPER_ALPHABETS : LOWER_ALPHABETS;
        int idx = Arrays.binarySearch(alphabets, aChar);
        int nextIdx = idx + 1;
        if (idx == (alphabets.length - 1)) {
            nextIdx = 0;
        }
        return alphabets[nextIdx];
    }

    /** 다음 순번의 숫자를 리턴한다. 자리수를 보전하며(001 -> 002) 자리수를 넘어가는 증가가 이루어질 경우 0을 리턴한다. (999 -> 000) */
    public static String getNextNumber(String str) {
        int length = str.length();
        String strFormat = "";
        for (int i = 0; i < length; i++) {
            strFormat += "0";
        }
        String nextStr = new DecimalFormat(strFormat).format(Integer.valueOf(str) + 1);
        if (nextStr.length() != length) {
            return strFormat;
        }
        return nextStr;
    }

    private static Pattern imgTagPattern = Pattern.compile("<img ([^>]+?)>",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static String handleImageOnError(String content, String handleMethod) {
        if (handleMethod != null && handleMethod.length() > 0) {
            Matcher matcher = imgTagPattern.matcher(content);
            if (matcher.find()) {
                content = matcher.replaceAll("<img onerror=\"javascript:"
                        + handleMethod + "\" $1>");
            }
        }
        return content;
    }

    public static String noise(String org) {
        if (org == null) return null;
        int length = org.length();
        int headSize = Math.min(length / 2, 4);
        StringBuilder sb = new StringBuilder(org.substring(0, headSize));
        for (int i = 0; i < length - headSize; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    private static Pattern imgTagSrcPattern = Pattern.compile("<img([^>]+)src=[\"']?([^\"']+)[\"']?([^>]*)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    public static String addAnchorTagImage(String content) {
        Matcher matcher = imgTagSrcPattern.matcher(content);
        if (matcher.find()) {
            content = matcher.replaceAll("<a href=\"$2\" rel=\"lightbox\"><img$1src=\"$2\"$3></a>");
        }
        return content;
    }

    public static String escapeUnicodeString(String str, boolean escapeAscii) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!escapeAscii && ((ch >= 0x0020) && (ch <= 0x007e))) {
                sb.append(ch);
            }
            else if (ch < 0x0020) {
                sb.append(ch);
            }
            else {
                sb.append("\\u");
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);
                if (hex.length() == 2) {
                    sb.append("00");
                }
                sb.append(hex.toUpperCase(Locale.ENGLISH));
            }
        }
        return sb.toString();
    }

    private static int computeSimilarity(String str1, String str2) {
        HashSet<Character> set1 = new HashSet<Character>();
        HashSet<Character> set2 = new HashSet<Character>();

        for (char c : str1.toCharArray()) {
            set1.add(c);
        }
        for (char c : str2.toCharArray()) {
            set2.add(c);
        }

        int preSize = set1.size();
        if (preSize <= 15 ) return 0;		// "감사합니다.", "ㅋㅋ", "1등 ㅎㅎㅎ" 같은 단순한 것들은 봐준다.

        set1.retainAll(set2);

        if (log.isDebugEnabled()) {
            log.debug("Similar rate : " + set1.size() * 100 / preSize);
        }
        return set1.size() * 100 / preSize;
    }

    public static boolean checkSimilarity(String str1, String str2, int level) {
        return (str1 != null && str2 != null && level >= 0 && level <= 100 &&
                computeSimilarity(str1, str2) > level);
    }

    private static String escape(char ch) {
        StringBuffer sb = new StringBuffer();
        String ncStr = "*+-./0123456789@ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";

        if (ch > 0x7f) {
            sb.append("%u" + Integer.toHexString((int) ch).toUpperCase());
        }
        else if (ncStr.indexOf((int) ch) == -1) {
            sb.append('%');
            if (ch < 0xf) {
                sb.append('0');
            }
            sb.append(Integer.toHexString((int) ch).toUpperCase());
        }
        else {
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String toNumericCode(char extendedChar) {
        String unicodeString = escape(extendedChar);
        StringBuffer sb = new StringBuffer();
        if (unicodeString.indexOf("%u") >= 0) {
            // non-ASCII code -> Numeric character reference or character entity reference
            unicodeString = "&#" + Integer.parseInt(unicodeString.substring(2), 16) + ";";
        }
        sb.append(unicodeString);
        return sb.toString();
    }

    public static String convertStringToBrowserStyle(String string){
        if (string == null) {
            return null;
        }
        Charset charset = Charset.forName("EUC-KR");
        CharsetEncoder encoder = charset.newEncoder();

        StringBuffer sb = new StringBuffer();
        char ch;
        for (int i = 0; i < string.length(); i++) {
            ch = string.charAt(i);
            if(encoder.canEncode(ch)){
                sb.append(ch);
            }else{
                sb.append(toNumericCode(ch));
            }
        }
        return sb.toString();
    }

    /**
     * 주어진 integer 값을 5자리의 62진수 문자열로 변환합니다.
     * @param i max 916,132,831 (9억)
     * @return 62진수 문자열
     */
    public static String convertTo62Digit(int i) {
        Assert.isTrue(916132831 >= i);
        StringBuilder result = new StringBuilder();
        do {
            result.insert(0, CHARACTERS_62_DIGIT[i % 62]);
            i = i / 62;
        } while (i > 0);
        return lpad(result, 5, "0").toString();
    }

    /**
     * 주어진 long 값을 62진수 문자열로 변환합니다. 자리수 남는 부분은 "0"으로 채웁니다.
     * 
     * @param l 값
     * @param figure 자리수. figure 값이 0 이면 "0"으로 채우지 않습니다. 
     * @return 62진수 문자열
     */
    public static String convertTo62Digit(long l, int figure) {
    	StringBuilder result = new StringBuilder();
    	do {
    		result.insert(0, CHARACTERS_62_DIGIT[(int)(l % 62)]);
    		l = l / 62;
    	} while (l > 0);
    	return (figure > 0) ? lpad(result, figure, "0").toString() : result.toString();
    }
    
    /**
     * 62진수 문자열 랜덤 발생합니다.
     * 
     * @param figure 자리수 
     * @return 62진수 문자열
     */
    public static String rand62Digit(int figure) {
    	StringBuilder result = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < figure; i++) {
    		result.append(CHARACTERS_62_DIGIT[random.nextInt(62)]);
    	}
    	return result.toString();
    }
    
    /**
     * 36진수(대문자, 숫자) 문자열 랜덤 발생합니다.
     * 
     * @param figure 자리수 
     * @return 36진수 문자열
     */
    public static String rand36Digit(int figure) {
    	StringBuilder result = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < figure; i++) {
    		result.append(CHARACTERS_36_DIGIT[random.nextInt(36)]);
    	}
    	return result.toString();
    }
    
    /**
     * 주어진 62진수 문자열을 integer 값으로 변환합니다. (<-> convertTo62Digit)
     * @param i max 916,132,831 (9억) 범위내 62진수 문자열
     * @return 10진수 integer
     */
    public static int convert62ToInteger(String str) {
        Assert.isTrue(StringUtils.hasText(str));

        int i = 0;
        for (int j = 0 ; j<str.length(); j++) {
            for (int k = 0; k < CHARACTERS_62_DIGIT.length; k++) {
                if (str.charAt(j)==CHARACTERS_62_DIGIT[k]){
                    i += Math.pow(62,(str.length()-j-1))*k;
                    break;
                }
            }
        }
        return i;
    }
    
    /**
     * 주어진 62진수 문자열을 integer 값으로 변환합니다. (<-> convertTo62Digit)
     * @param i max 916,132,831 (9억) 범위내 62진수 문자열
     * @return 10진수 integer
     */
    public static long convert62ToLong(String str) {
    	Assert.isTrue(StringUtils.hasText(str));
    	
    	long l = 0;
    	int length = str.length();
    	for (int j = 0; j < length; j++) {
    		l += Math.pow(62,(length-j-1)) * CHARACTERS_62_DIGIT_MAP.get(str.charAt(j));
    	}
    	return l;
    }
    
	public static <T>String join(final Collection<T> objs, final String delimiter) {
		if (objs == null || objs.isEmpty())
			return "";
		Iterator<T> iter = objs.iterator();
		StringBuffer buffer = new StringBuffer(iter.next().toString());
		while (iter.hasNext())
			buffer.append(delimiter).append(iter.next().toString());
		return buffer.toString();
	}
	
	public static <T>String join(final Collection<T> objs, final String format, final String delimiter) {
		if (objs == null || objs.isEmpty())
			return "";
		Iterator<T> iter = objs.iterator();
		StringBuffer buffer = new StringBuffer(String.format(format, iter.next().toString()));
		while (iter.hasNext())
			buffer.append(delimiter).append(String.format(format, iter.next().toString()));
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		long l = 71239;
		String s = convertTo62Digit(l, 5);
		System.out.println(s);
		System.out.println(convert62ToLong(s));
	}

	public static String escapeScriptTag(String content) {
		return content.replaceAll("<(/?[Ss][Cc][Rr][Ii][Pp][Tt][^>]*)>", "&lt;$1&gt;")
				.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;")
				.replaceAll("'", "& #39;")
				.replaceAll("eval\\((.*)\\)", "")
				.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
	}
}

