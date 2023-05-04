package kr.co.sampler.crawling.common.util;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jinriver
 */
public class StringUtil
{

    /**
     *
     */
    public static String ISO8859 = "8859_1";
    /**
     *
     */
    public static String defaultEncoding = "KSC5601";

    /**
     * <
     * PRE>
     * 문자열 내부에 " " 이 존재하면 trim하여 붙은 문자열로 리턴
     * 예) 현   대   => 현대
     *
     * 메소드 : iTrim
     *
     * @param str
     * @return
     * </PRE>
     */
    public static String iTrim(String str)
    {
        String ret = "";
        for (int i = 0; i < str.length(); i++)
        {
            String chr = str.substring(i, i + 1);
            if (chr != null && !chr.equals(" "))
            {
                ret += chr;
            }
        }

        return ret;
    }

    /**
     * <
     * PRE>
     * 오른쪽 공백 trim
     *
     * 메소드 : rTrim
     *
     * @param str
     * @return
     * </PRE>
     */
    public static String rTrim(String str)
    {
        int i;
        for (i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) != ' ')
            {
                break;
            }
        }
        return str.substring(0, i + 1);
    }

    /**
     * <
     * PRE>
     * 왼쪽 공백 trim
     *
     * 메소드 : lTri
     *
     * @param str
     * @return
     * </PRE>
     */
    public static String lTrim(String str)
    {
        int i;
        for (i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) != ' ')
            {
                break;
            }
        }
        return str.substring(i);
    }

    /**
     * 한글 변환 메소드.( ASCII -> KSC )
     *
     * @return java.lang.String
     * @param str java.lang.String
     */
    public static String ascToksc(String str)
    {
        try
        {
            if (str == null)
            {
                return null;
            }
            return new String(str.getBytes(ISO8859), defaultEncoding);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 인자로 넘어온 String중에서 blank를 삭제한다
     *
     * @return java.lang.String
     * @param src java.lang.String
     */
    public static String deleteBlank(String src)
    {

        StringBuffer sb = new StringBuffer(src.trim());

        int blankIdx = 0;
        while (true)
        {
            blankIdx = sb.toString().indexOf(' ');
            if (blankIdx > 0)
            {
                sb.deleteCharAt(blankIdx);
            } else
            {
                break;
            }
        }

        return sb.toString();

    }

    /**
     * 한글 변환 메소드.( KSC -> ASCII )
     *
     * @return java.lang.String
     * @param str java.lang.String
     */
    public static String kscToasc(String str)
    {
        try
        {
            if (str == null)
            {
                return null;
            }
            return new String(str.getBytes(defaultEncoding), ISO8859);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <font size=2>
     * 임의의 String을 주어진 크기만큼 잘라서 return한다. <br>
     * String의 substring에서는 한글 1 자의 length를 1로 나타나기 때문에 <br>
     * byte Array를 이용하여 한글 1자를 2byte로 인식하여 보다 정확한 substring기능을 구현한다. endIndex 는
     * String의 끝이다.
     * </font>
     *
     * @return java.lang.String
     * @param str java.lang.String
     * @param startIndex int
     */
    public static String subString(String str, int startIndex)
    {

        byte[] strBytes = str.getBytes();

        // 인자가 하나일경우 endIndex 를 String의 끝으로한다.
        int endIndex = strBytes.length;

        // byteArray를 String으로 만들어 return한다.
        return new String(strBytes, startIndex, endIndex - startIndex);

    }

    /**
     * <font size=2>
     * 임의의 String을 주어진 크기만큼 잘라서 return한다. <br>
     * String의 substring에서는 한글 1 자의 length를 1로 나타나기 때문에 <br>
     * byte Array를 이용하여 한글 1자를 2byte로 인식하여 보다 정확한 substring기능을 구현한다.
     * </font>
     *
     * @return java.lang.String
     * @param str java.lang.String
     * @param startIndex int
     * @param endIndex int
     */
    public static String subString(String str, int startIndex, int endIndex)
    {

        return new String(str.getBytes(), startIndex, endIndex - startIndex);

    }

    /*
     * src의 내용중에서 xx문자열을 yy로 치환
     */
    /**
     *
     * @param src
     * @param xx
     * @param yy
     * @return
     */
    static public String replaceStr(String src, String xx, String yy)
    {

        StringBuffer buf = new StringBuffer(src.length());
        int pos = -1;

        while ((pos = src.indexOf(xx)) > -1)
        {
            //src에서 첫번째 xx위치까지 잘라낸다.
            buf.append(src.substring(0, pos));

            //변경할 문자 추가.
            buf.append(yy);

            //src에서 xx길이만큼 위치변경
            src = src.substring(pos + xx.length());
        }

        //제일 마지막 토큰값을 buf에 추가시켜준다.
        buf.append(src);

        //System.out.println("StringUtil buf = "+buf.toString());
        return buf.toString();
    }

    /**
     *
     * @param source
     * @param delim
     * @return
     */
    public static Hashtable getTokenData(String source, char delim)
    {
        Hashtable hash = new Hashtable();
        String temp = null;
        int index = 0;

        //라인 마지막 마다  delim을 추가해줘야 한다.
        if (source.charAt(source.length() - 1) != delim)
        {
            source += "" + delim;
        }

//	    System.out.println("source "+source.charAt(source.length()-1));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == delim)
            {
                temp = sb.toString();
                if (temp == null || temp.equals(""))
                {
                    temp = "";
                }
                hash.put("COLUMN_" + i, temp);

                //System.out.println(this.column_name[index]+"["+index+"] = "+temp);
                sb = null;
                sb = new StringBuffer();
                index++;
            } else
            {
                sb.append("" + source.charAt(i));
            }
        }

        sb = null;

        return hash;
    }

    /**
     * <PRE>
     * Token객체에서 nextToken을 처리한다.
     * 에러발생시 처리를 위해 따로 함수로 뺐다.
     * @param st
     * @return
     * </PRE>
     */
    public static String getNextToken(StringTokenizer st)
    {
        String value = "";

        try
        {
            value = (String) st.nextToken();
        } catch (Exception e)
        {
            value = "";
        }

        return value.trim();
    }

    /**
     *
     * @param source
     * @param delim
     * @return
     */
    public static ArrayList getTokenData2(String source, char delim)
    {
        ArrayList arrSt = new ArrayList();
        String temp = null;
        int index = 0;

        //라인 마지막 마다  delim을 추가해줘야 한다.
        if (source.charAt(source.length() - 1) != delim)
        {
            source += "" + delim;
        }

        // 데이터에 || 와 같이 token과 token사이가 null string 인 경우에는
        // token이 인식되지 않으므로 공백문자를 삽입하도록 한다.
        source = StringUtil.replaceString(source, "||", "| |");

//	    System.out.println("source "+source.charAt(source.length()-1));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == delim)
            {
                temp = sb.toString();
                if (temp == null || temp.equals(""))
                {
                    temp = "";
                }

                arrSt.add(temp);

                //System.out.println(this.column_name[index]+"["+index+"] = "+temp);
                sb = null;
                sb = new StringBuffer();
                index++;
            } else
            {
                sb.append(source.charAt(i));
            }
        }

        sb = null;

        return arrSt;
    }

    /**
     *
     * @param source
     * @param delim
     * @return
     */
    public static Vector getTokenData(String source, String delim)
    {
        Vector v = new Vector();
        StringTokenizer st = new StringTokenizer(source, delim);
        String temp = null;
        while (st.hasMoreTokens())
        {
            temp = (String) st.nextToken();
            v.add(temp);
        }//while()

        return v;
    }

    /**
     *
     * @param source
     * @param delim
     * @return
     */
    public static Hashtable getTokenData(String source, char delim, String[] column)
    {
        Hashtable hash = new Hashtable();
        String temp = null;
        int index = 0;

        //라인 마지막 마다  delim을 추가해줘야 한다.
        if (source.charAt(source.length() - 1) != delim)
        {
            source += "" + delim;
            //	    System.out.println("source "+source.charAt(source.length()-1));
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == delim)
            {
                temp = sb.toString();
                if (temp == null || temp.equals(""))
                {
                    temp = "";
                }

                hash.put(column[index], temp);

                //System.out.println(this.column_name[index]+"["+index+"] = "+temp);
                sb = null;
                sb = new StringBuffer();
                index++;
            } else
            {
                sb.append("" + source.charAt(i));
            }
        }

        sb = null;

        return hash;
    }

    /**
     *
     * @param src
     * @param destStr
     * @return
     */
    public static String replaceLine(String src, String destStr)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            String line = null;
            InputStream is = new ByteArrayInputStream(src.getBytes());
            InputStreamReader reader = new InputStreamReader(is, "cp949");
            BufferedReader buf = new BufferedReader(reader);

            try
            {
                while ((line = buf.readLine()) != null)
                {
                    //		log(output+"\n");
                    if (!line.equals(""))
                    {
                        line.trim();
                        //sb.append(line+"\r\n");
                        sb.append(line + destStr);
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }


    /*
     *문자열 패딩-뒷쪽에 붙임.
     */
    /**
     *
     * @param inStr
     * @param chPad
     * @param nPadLen
     * @return
     */
    public static String postPadding(String inStr, char chPad, int nPadLen)
    {
        int i;
        if (inStr.length() < nPadLen)
        {
            for (i = inStr.length(); i < nPadLen; i++)
            {
                inStr = inStr + chPad;
            }
        }

        return inStr;
    }

    /*
     *문자열 패딩
     */
    /**
     *
     * @param inStr
     * @param chPad
     * @param nPadLen
     * @return
     */
    public static String padding(String inStr, char chPad, int nPadLen)
    {
        int i;
        if (inStr.length() < nPadLen)
        {
            for (i = inStr.length(); i < nPadLen; i++)
            {
                inStr = inStr + chPad;
            }
        }

        return inStr;
    }

    /*
     *문자열 패딩-앞쪽에 붙임.
     */
    /**
     *
     * @param maxlen
     * @param ch
     * @param data
     * @return
     */
    public static String prePadding(int maxlen, char ch, String data)
    {
        int inx;
        int jnx;
        int length;

        byte[] out_buff = new byte[20];
        byte[] byteData = data.getBytes();

        length = data.length();

        if (length > maxlen)
        {
            out_buff[0] = (byte) ch; //0x20;
            out_buff[1] = 0x00;
        } else
        {
            for (inx = 0; inx < maxlen - length; inx++)
            {
                out_buff[inx] = (byte) ch; //0x20;
            }

            jnx = 0;

            while (inx < maxlen)
            {
                out_buff[inx] = byteData[jnx];
                inx++;
                jnx++;
            }
            out_buff[maxlen] = 0x00;
        }

        String ret = new String(out_buff).substring(0, maxlen);
        return ret;
    }

    /**
     *
     * @param htmlSrc
     * @return
     */
    public static String removeHtmlTag(String htmlSrc)
    {

        int nStart = 0;
        int nEnd = 0;
        int nOffset = 0;
        int nLen = 0;
        int cnt = 0;
        if (htmlSrc == null || "".equals(htmlSrc))
        {
            return "";
        }

        while (true)
        {
            StringBuffer sb = new StringBuffer();
            nLen = htmlSrc.length();
            nStart = htmlSrc.indexOf("<");
            nEnd = htmlSrc.indexOf(">");
            //			System.out.println("nStart = "+nStart+" nEnd = "+nEnd);

            if ((nStart < 0 || nEnd < 0) || nStart >= nEnd)
            {
                //				System.out.println("[sch_stock_todayfocus] ("+nStart+","+nEnd+") break");
                break;
            }

            //			System.out.println("[sch_stock_todayfocus] remove html tag : ("+nStart+","+nEnd+") "+htmlSrc.substring(nStart,nEnd+1));
            for (int i = 0; i < nLen; i++)
            {
                if (i >= nStart && i <= nEnd)
                {
                    continue;
                }

                sb.append("" + htmlSrc.charAt(i));
            }

            htmlSrc = sb.toString();
            sb = null;

            //너무큰데이타나 무한루프 방지용 만약 1M 까지 허용
            if (cnt++ > 1024 * 1024)
            {
                break;
            }
        }

        htmlSrc = htmlSrc.trim();

        return htmlSrc;
    }

    /**
     * 주어진 문자열을 주어진 길이의 스트링으로 만든다. 모자란 길이만큼 padChar를 뒤에다가 붙인다.
     *
     * @param msg
     * @param padChar
     * @param length
     * @return
     */
    public static String toFixedLengthString(String msg, int length, char padChar)
    {
        int count = length - msg.length();

        if (count > 0)
        {
            // String Buffer 를 이용하여 string 조작을...
            StringBuffer strBuf = new StringBuffer();
            strBuf.append(msg);
            for (int i = 0; i < count; i++)
            {
                strBuf.append(padChar);
            }
            return strBuf.toString();
        }
        return msg;
    }

    /**
     * Add by jjungkey (2000/09/29) 주어진 문자열을 주어진 길이의 스트링으로 만든다. 모자란 길이만큼
     * padChar를 앞에다가 붙인다.
     *
     * @param msg
     * @param length
     * @param padChar
     * @return
     */
    public static String toFixedLengthStringFront(String msg, int length, char padChar)
    {
        int count = length - msg.length();

        if (count > 0)
        {
            // String Buffer 를 이용하여 string 조작을...
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < count; i++)
            {
                strBuf.append(padChar);
            }
            strBuf.append(msg);
            return strBuf.toString();
        }
        return msg;
    }

    /**
     * 주어진 정수를 주어진 길이의 스트링으로 만든다. 모자란 길이만큼 padChar를 앞에다가 붙인다.
     *
     * @param num
     * @param padChar
     * @param length
     * @return
     */
    public static String toFixedLengthString(int num, int length, char padChar)
    {
        String temp = String.valueOf(num);

        int count = length - temp.length();

        if (count > 0)
        {
            // String Buffer 를 이용하여 string 조작을...
            StringBuffer strBuf = new StringBuffer();

            for (int i = 0; i < count; i++)
            {
                strBuf.append(padChar);
            }
            strBuf.append(temp);
            return strBuf.toString();
        }
        return temp;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toDefaultEncoding(String s)
    {
        if (s == null || s.equals(""))
        {
            return s;
        }

        try
        {
            //    return new String(s.getBytes(ISO8859), defaultEncoding);
            return s;
        } catch (Exception e)
        {
            return s;
        }
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toISO8859(String s)
    {
        if (s == null || s.equals(""))
        {
            return s;
        }

        try
        {
            return new String(s.getBytes(defaultEncoding), ISO8859);
        } catch (Exception e)
        {
            return s;
        }
    }

    /**
     * <PRE>
     *
     * input string 의 내용이 null 혹은 space, &nbsp; 의 경우 output string 의 내용으로 채우도록
     * 한다. jsp 에서 빈번하게 사용될것이므로 util 의 성격이지만 HttpJspDASSBase 에서 제공하도록 한다.
     *
     * @param strInput
     * @param strOutput
     * @return
     *
     * </PRE>
     */
    public static String fillStr(String strInput, String strOutput)
    {
        if (strInput == null)
        {
            strInput = "";
        }
        strInput = strInput.trim();
        if (strInput.equals("") || strInput.equalsIgnoreCase("0") || strInput.equalsIgnoreCase("&nbsp;"))
        {
            return strOutput;
        }
        return (String) strInput;
    }

    /*
     * null 문자 이후를 trim한다.
     */
    /**
     * 문자열에서 \u0000을 모두 제거한다.
     *
     * @param str
     * @return
     */
    public static String nullTrim(String str)
    {
        if (str == null)
        {
            str = "";
        }

        //by jinriver 2008.03.17 value에 \u0000이 있으면 ''로 치환
        //\u0000은 null 이후의 문자는 제거함.
        //str = str.replaceAll("\u0000","");
        int nPos = str.indexOf("\u0000");
        if (nPos > -1)
        {

            str = str.substring(0, nPos);
            //str = StringUtil.replaceStr(str, "\u0000","");

        }

        return str;
    }

    public static String nullToSpace(byte[] src)
    {
        byte[] tmp = new byte[src.length];

        byte ch;
        for (int i = 0; i < src.length; i++)
        {
            ch = src[i];
            if (ch == 0x00)
            {
                ch = 0x20;
            }

            tmp[i] = ch;
        }

        String ret = new String(tmp);

        //System.out.println("[StringUtil.nullToSpace()] src = "+ret);
        return ret;
    }

    /**
     * <
     * PRE>
     * null 을 공백으로 변환
     *
     * 메소드 : null2String
     *
     * @param str
     * @return
     * </PRE>
     */
    public static String null2String(String str)
    {
        return null2String(str, "");
    }

    /**
     * Map 대응 Object 추가
     * @param str
     * @return
     */
    public static String null2String(Object str)
    {
        str = str==null?"":str.toString();
        return null2String(str, "");
    }

    public static String null2String(Object str, String def)
    {
        String nstr = str==null?"":str.toString();

        if (nstr == null || nstr.equals("") || nstr.equals("null"))
        {
            nstr = def;
        }

        return nstr;
    }

    /**
     * <
     * PRE>
     * null 을 기본정의한 문자열로 변경한다.
     *
     * 메소드 : null2String
     *
     * @param str
     * @param def
     * @return
     * </PRE>
     */
    public static String null2String(String str, String def)
    {
        if (str == null || str.equals("") || str.equals("null"))
        {
            str = def;
        }
        return str;
    }

    /**
     * 문자열안의 특정문자를 지정 문자열로 바꾸어 준다.
     *
     * @param stSource
     * @param stFind
     * @param stReplace
     * @return
     */
    public static String replaceString(String stSource, String stFind, String stReplace)
    {
        StringBuffer stbBuf = new StringBuffer(stSource);
        int nIndex = -1;
        int nLenMatch = stFind.length();
        while (true)
        {
            nIndex = stSource.indexOf(stFind, nIndex);
            if (nIndex == -1)
            {
                break;
            }
            stSource = stbBuf.replace(nIndex, nIndex + nLenMatch, stReplace).toString();

        }

        return stbBuf.toString();
    }

    /**
     * <PRE>
     *
     * @param name
     * @param de de 가 true 이면 ascending false 이면 descending
     * @return
     * </PRE>
     */
    public static String[] StringArraySort(String[] name, boolean de)
    {
        int idx = name.length;

        for (int i = 0; i < idx; i++)
        {
            for (int k = i + 1; k < idx; k++)
            {
                if (de)
                {
                    if (name[i].compareToIgnoreCase(name[k]) > 0)
                    {
                        String temp = name[k];
                        name[k] = name[i];
                        name[i] = temp;
                    }
                } else if (name[i].compareToIgnoreCase(name[k]) < 0)
                {
                    String temp = name[k];
                    name[k] = name[i];
                    name[i] = temp;
                }
            }
        }

        return name;
    }

    /**
     * HTML태그 및 특수문자를 변환해준다.
     *
     * @param str
     * @return String
     */
    public static String escapeXml(String str)
    {
        if (str == null)
        {
            str = "";
        }
        String resultString = str;
        resultString = resultString.replaceAll("&", "&amp;");
        resultString = resultString.replaceAll("\'", "&#039;");
        resultString = resultString.replaceAll("\"", "&#34;");
        resultString = resultString.replaceAll("<", "&lt;");
        resultString = resultString.replaceAll(">", "&gt;");
        return resultString;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String escapeXml(int str)
    {
        return escapeXml("" + str);
    }

    public static String preventCSRF(Object o){
        if (o == null)
        {
            return "";
        }
        String s1 = o.toString();
        return preventCSRF(s1);
    }
    public static String preventCSRF(String s)
    {
        String s1 = s;
        if (s1 == null)
        {
            return "";
        }
        if (s1.indexOf(35, 0) != -1)
        {
            s1 = replace(s1, "#", "&#35;");
        }
        if (s1.indexOf(38, 0) != -1)
        {
            s1 = replace(s1, "&", "&#38;");
        }
        if (s1.indexOf(60, 0) != -1)
        {
            s1 = replace(s1, "<", "&lt;");
        }
        if (s1.indexOf(62, 0) != -1)
        {
            s1 = replace(s1, ">", "&gt;");
        }
        if (s1.indexOf(40, 0) != -1)
        {
            s1 = replace(s1, "(", "&#40;");
        }
        if (s1.indexOf(41, 0) != -1)
        {
            s1 = replace(s1, ")", "&#41;");
        }

        return preventXSS(s1);
    }

    /**
     * 보안코딩 , 반드시 이 메쏘드를 이용해서 request를 처리해세요.
     *
     * @param src
     * @param target
     * @return
     */
    public static String preventCSRF(String src, String target)
    {
        String str = src;
        String retVal = preventCSRF(str);

        return null2String(retVal, target);
    }

    /*
     * XSS (Cross Site Scripting) 취약점 보안
     */
    static String[] xss_keyword =
            {
                    "alert", "object", "backgroud", "cmd", "style", "document", "write", "exec", "applet", "script", "javascript", "vbscript", "cookie", "location", "onfocus", "onblur", "ondrag", "onkeydown", "onkeyup", "onkeypress", "onload", "onclick", "ondbclick", "onmouseup", "onmousedown", "onmouseover", "onmousemove", "onmouseout", "onmousewheel"
            };

    public static String preventXSS(String val)
    {
//        String[] keyword =
//        {
//            "alert", "object", "backgroud", "cmd", "style", "document", "write", "exec", "applet", "script", "javascript", "vbscript", "cookie", "location", "onfocus", "onblur", "ondrag", "onkeydown", "onkeyup", "onkeypress", "onload", "onclick", "ondbclick", "onmouseup", "onmousedown", "onmouseover", "onmousemove", "onmouseout", "onmousewheel"
//        };
//
//        String ret = val;
//        if (val == null || "".equals(val))
//        {
//            return "";
//        }
//
//        for (int i = 0; i < keyword.length; i++)
//        {
//            if (val.toLowerCase().indexOf(keyword[i]) != -1)
//            {
//                ret = "";
//                break;
//            }
//        }

        String ret = val;
        if (val == null || "".equals(val))
        {
            return "";
        }

        HashMap hxss = null;

        if (hxss == null)
        {
            hxss = new HashMap();

            //hashmap에 xss 키를 키워드를 추가해놓은다.
            for (int i = 0; i < xss_keyword.length; i++)
            {
                hxss.put(xss_keyword[i], Boolean.TRUE);
            }
        }

        System.out.println("[StringUtil.preventXSS()]  입력된 XSS 키워드  = " + val);

        if (hxss.containsKey(val.toLowerCase()))
        {
            System.out.println("[StringUtil.preventXSS()] 해쉬키 XSS 방지 키워드 발견  = " + val);
            System.out.println("[StringUtil.preventXSS()] 해쉬키 XSS 방지 키워드 발견  = " + val);
            System.out.println("[StringUtil.preventXSS()] 해쉬키 XSS 방지 키워드 발견  = " + val+" StringUtil.preventXSS()");

            ret = "";
        } else
        {
            for (int i = 0; i < xss_keyword.length; i++)
            {
                if (val.toLowerCase().indexOf(xss_keyword[i]) != -1)
                {
                    System.out.println("[StringUtil.preventXSS()] indexOf XSS 방지 키워드 발견 !!  = " + val);
                    ret = "";
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * 문자열을 변경한다. 문자열 str에서 oldStr을 찾아 newStr로 변경
     *
     * @param str
     * @param oldStr
     * @param newStr
     * @return
     */
    public static String replace(String str, String oldStr, String newStr)
    {
        StringBuffer result = new StringBuffer(str.length());
        int index1 = 0, index2 = 0;

        while ((index2 = str.indexOf(oldStr, index1)) >= 0)
        {
            result.append(str.substring(index1, index2));
            result.append(newStr);
            index1 = index2 + oldStr.length();
        }
        result.append(str.substring(index1));

        return result.toString();
    }

    /*
     * @pattern 형식에 따라서 적절한지 여부를 판단해준다.
     * 무조건 소숫점2자리 정규식 : "[0-9]*\\.[0-9]{2}";
     * 소수점 있던 없던 자유 : "[0-9]*\\.?[0-9]*";
     * 숫자만 : "[0-9]"
     */
    //final static public String PATTERN_ONLY_DIGIT       =       "[0-9]{0,5}";
    /**
     *
     */
    final static public String PATTERN_ONLY_DIGIT = "^[+-]?\\d*(\\.?\\d*)$";

    /**
     *
     * @param pattern
     * @param value
     * @return
     */
    public static boolean checkPattern(String pattern, String value)
    {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        boolean ok = m.matches();

        //DASSLogger.debug("[StringUtil.checkPattern()] 패턴 [" + pattern + "], 입력 값 = " + value + ", 정상여부 : " + (ok ? "●" : "※"));
        return ok;
    }

    /**
     * 문자열을 받아 ksc5601로 바꿔준다.
     *
     * @param a
     * @return
     */
    public static String kor(String a)
    {
        String result = null;
        try
        {
            if (a != null)
            {
                result = new String(a.getBytes("ISO-8859-1"), "KSC5601");
            }
        } catch (UnsupportedEncodingException usee)
        {
        }
        return result;
    }

    /**
     * 문자열을 받아 iso-8859_1로 바꿔준다.
     *
     * @param a
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String eng(String a) throws UnsupportedEncodingException
    {
        String result = null;
        try
        {
            if (a != null)
            {
                result = new String(a.getBytes("KSC5601"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException usee)
        {
        }
        return result;
    }

    /**
     * /r/n new line을 웹에서 사용하기 위한 <br>로 바꿔준다.
     *
     * ' -> &quot;로 바꾸어서 DB에 입력한 것을 다시 돌려준다.
     *
     * @param comment
     * @return
     */
    public static String nl2br(String comment)
    {
        int length = comment.length();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; ++i)
        {
            String comp = comment.substring(i, i + 1);
            if ("\r".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                if ("\n".compareTo(comp) == 0)
                {
                    buffer.append("<BR>\r");
                } else
                {
                    buffer.append("\r");
                }
            }
            if ("&quot;".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("'");
            }
            buffer.append(comp);
        }
        return buffer.toString();
    }

    /**
     * html에 사용하는 태그를 위해 데이터베이스에 고대로 입력할때. ' 만 체크해줌
     *
     * @param comment
     * @return
     */
    public static String html2db(String comment)
    {
        int length = comment.length();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; ++i)
        {
            String comp = comment.substring(i, i + 1);
            if ("\'".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("\'\'");
            }
            if ("&".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("&amp;");
            }

            buffer.append(comp);
        }
        return kor(buffer.toString());
    }

    /**
     * 에러를 유발하지 않게 위해 db에 넣기 전에 text 처리를 해준다.
     *
     * @param comment
     * @return
     */
    public static String text2db(String comment)
    {
        int length = comment.length();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; ++i)
        {
            String comp = comment.substring(i, i + 1);
            if ("\'".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("\'\'");
            }
            if ("<".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("&lt;");
            }
            if (">".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("&gt;");
            }

            if ("&".compareTo(comp) == 0)
            {
                comp = comment.substring(++i, i + 1);
                buffer.append("&amp;");
            }

            buffer.append(comp);
        }
        return kor(buffer.toString());
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<HTML>");
        sb.append(" <HEAD>");
        sb.append("  <TITLE> New Document </TITLE>");
        sb.append("  <META NAME=\"Generator\" CONTENT=\"EditPlus\">");
        sb.append("</HEAD>");
        sb.append(" <BODY>");
        sb.append("  afafsafd");
        sb.append(" </BODY>");
        sb.append("</HTML>");

        String result = StringUtil.removeHtmlTag(sb.toString());

        System.out.println("[StringUtil] result = " + result);
    }

    /**
     * 자리수에 맞춰 앞을 '0'으로 채움
     */
    public static String makeFZero(String str, int size)
    {
        String result = "";

        if (str == null)
        {
            str = "";
        }

        if (str.length() < size)
        {
            for (int i = 0; i < size - str.length(); i++)
            {
                result += "0";
            }
        }

        return (result + str);
    }

    /**
     * a=1&b=2&c=3&d= 이런 웹 파라미터형식의 스트링을 파싱해서 HashMap으로 리턴해준다.
     *
     * @param paramvalue
     * @return
     */
    public static HashMap getParamter(String paramvalue)
    {

        ArrayList arrParam = StringUtil.getTokenData2(paramvalue, '&');

        String[] params = null;
        String param = null;
        HashMap mapParam = new HashMap();
        String name, value = null;

        for (int i = 0; i < arrParam.size(); i++)
        {
            //패스워드 '='처리
            param = (String) arrParam.get(i);
            int ivalueStart = param.indexOf("=");
            int ivalueEnd = param.length();
            if(ivalueStart > -1){
                name = param.substring(0, ivalueStart).trim();
                value = param.substring(ivalueStart+1, ivalueEnd).trim();
            }else{
                name = "";
                value = "";
            }
            /*
            params = param.split("[=]");

            if (params.length == 2)
            {
                name = params[0];
                value = params[1];
            } else
            {
                name = params[0];
                value = "";
            }
			*/
            System.out.println("[StringUtil.getParameter()] name = " + name + ", value = " + value);

            mapParam.put(name, value);
        }

        return mapParam;
    }

    /**
     * 펀드몰 설정일 설정 DateUtil클래스 사용 권장
     *
     * @deprecated
     */
    public static String dateFormat(String value)
    {

        if (value.trim().length() != 8)
        {
            return "-";
        } else
        {

            try
            {

                if (Integer.parseInt(value) == 0)
                {
                    return "-";
                } else
                {
                    return value.substring(0, 4) + "/" + value.substring(4, 6) + "/" + value.substring(6);
                }

            } catch (NumberFormatException e)
            {
                return "-";
            }
        }

    }

    /**
     * 리서치 시황속보 시간 설정 DateUtil클래스 사용 권장
     *
     * @deprecated
     */
    public static String timeFormat(String value)
    {

        if (value.trim().length() != 6)
        {
            return "-";
        } else
        {

            try
            {

                if (Integer.parseInt(value) == 0)
                {
                    return "-";
                } else
                {
                    return value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4);
                }

            } catch (NumberFormatException e)
            {
                return "-";
            }
        }

    }

    /**
     * 임의 비밀번호 8자리 생성(숫자+영문 소문자+특수문자)
     * @return
     */
    public static String twoRandomPw(){
        char pwCollection[] = new char[] { '1','2','3','4','5','6','7','8','9','0',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '!','@','#','$','%','^','&','*','(',')'
        };//배열에 선언

        String ranPw = "";

        for (int i = 0; i < 8; i++) {
            //Math.rondom()은 0.0이상 1.0미만의 난수를 생성해 준다.
            int selectRandomPw = (int)(Math.random()*(pwCollection.length));
            ranPw += pwCollection[selectRandomPw];
        }
        return ranPw;
    }

    /**
     *임의 비밀번호 8자리 생성(숫자+영문소문자+특수문자)
     * @return
     */
    public static String randomPw() {
        StringBuffer sb = new StringBuffer();
        StringBuffer sc = new StringBuffer("!@#$%^&*(");

        // 소문자 4개 생성
        for (int i = 0; i < 5; i++) {
            sb.append((char) ((Math.random() * 26) + 97)); // 아스키번호 97(a) 부터 // 26글자 중 택일
        }

        // 숫자 2개 생성
        for (int i = 0; i < 3; i++) {
            sb.append((char) ((Math.random() * 10) + 48)); // 아스키번호 48(1) 부터 // 10글자 중 택일
        }

        sb.setCharAt(((int) (Math.random() * 4) + 4), sc.charAt((int) (Math.random() * sc.length() - 1))); // 소문자 4개 중 1개

        return sb.toString(); // 비밀번호
    }

    /**
     * 임의 난수 생성(숫자)
     * @param length
     * @return
     */
    public static String excuteGenerate(int length) {
        Random random = new Random(System.currentTimeMillis());

        int range = (int)Math.pow(10,length);
        int trim = (int)Math.pow(10, length-1);
        int result = random.nextInt(range)+trim;

        if(result>range){
            result = result - trim;
        }

        return String.valueOf(result);
    }

    /**
     * 좌측에 원하는 char로 원하는 길이만큼 채움.
     * @param str 원데이터
     * @param size 원하는 길이
     * @param padChar 채울 char
     * @return
     */
    public static String leftPad(String str, int size, char padChar) {
        String result = "";

        if (str.getBytes().length < size) {
            result = str;

            for (int i = 0; i < size - str.getBytes().length; i++) {
                result = padChar + result;
            }

        }else {
            result = str;
        }

        return result;
    }

    /**
     * 우측에 원하는 char로 원하는 길이만큼 채움.
     * @param str 원데이터
     * @param size 원하는 길이
     * @param padChar 채울 char
     * @return
     */
    public static String rightPad(String str, int size, char padChar) {
        String result = "";

        if (str.getBytes().length < size) {
            result = str;

            for (int i = 0; i < size - str.getBytes().length; i++) {
                result = result + padChar;
            }

        }else {
            result = str;
        }

        return result;

    }

    /**
     * Agent OS 정보를 받아 PC, MOBLIE Return
     * @param str
     * @return
     */
    public static String getAgentType(String str) {
        String agent = str.toLowerCase();
        boolean flag = agent.matches(".*win16.*|.*win32.*|.*win64.*|.*mac.*");

        String agentType = "";

        if (flag) {
            agentType = "pc";
        } else {
            agentType = "mobile";
        }
        return agentType;
    }

    /**
     * 숫자에 천단위마다 콤마 넣기 숫자(&#44;)
     * @param num
     * @return String
     * */
    public static String toCommaNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num).replaceAll(",","&#44;");
    }


    /**
     * 숫자에 천단위마다 콤마 넣기 숫자(,)
     * @param num
     * @return String
     * */
    public static String toCommaFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num).toString();
    }

    /**
     * String parameter null 확인 리턴
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 생년월일을 parameter로 받아 나이를 리턴해준다.
     * @param birth
     * @return
     */
    public static int getAge(String birth)
    {
        int birthYear = Integer.parseInt(birth.substring(0,4));
        int birthMonth = Integer.parseInt(birth.substring(4,6));
        int birthDay = Integer.parseInt(birth.substring(6,8));

        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }

    public static String getUserAgentBrowerInfo(String agent){
        String browser = null;

        if (agent.indexOf("Trident") > -1) {
            browser = "MSIE";
        } else if (agent.indexOf("iPhone") > -1) {
            browser = "iPhone";
        } else if (agent.indexOf("iPad") > -1) {
            browser = "iPhone";
        } else if (agent.indexOf("Android") > -1) {
            browser = "Android";
        } else if (agent.indexOf("Chrome") > -1) {
            browser = "Chrome";
        } else if (agent.indexOf("Opera") > -1) {
            browser = "Opera";
        }
        return browser.toLowerCase();
    }

    public static String removeEndZero(String content) {
        return new DecimalFormat("#,###.########").format(new BigDecimal(content)).toString();
    }

    public static String getString(String src, int beginIndex, int endIndex) {
        String result = src.substring(beginIndex, endIndex);

        return result;
    }
}
