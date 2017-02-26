package cn.wizzer.common.util;

import org.nutz.img.Images;
import org.nutz.repo.Base64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 条形码工具类(EMS面单)
 *
 * @author pandy 2017/1/17.
 */
public class Code128Util {

    public static enum Encode {
        Code128A, Code128B, Code128C, EAN128
    }

    static String[][] code128 = {{" ", " ", "00", "212222", "11011001100"},
            {"!", "!", "01", "222122", "11001101100"},
            {"\"", "\"", "02", "222221", "11001100110"},
            {"#", "#", "03", "121223", "10010011000"},
            {"$", "$", "04", "121322", "10010001100"},
            {"%", "%", "05", "131222", "10001001100"},
            {"&", "&", "06", "122213", "10011001000"},
            {"'", "'", "07", "122312", "10011000100",},
            {"(", "(", "08", "132212", "10001100100"},
            {")", ")", "09", "221213", "11001001000"},
            {"*", "*", "10", "221312", "11001000100"},
            {"+", "+", "11", "231212", "11000100100"},
            {",", ",", "12", "112232", "10110011100"},
            {"-", "-", "13", "122132", "10011011100"},
            {".", ".", "14", "122231", "10011001110"},
            {"/", "/", "15", "113222", "10111001100"},
            {"0", "0", "16", "123122", "10011101100"},
            {"1", "1", "17", "123221", "10011100110"},
            {"2", "2", "18", "223211", "11001110010",},
            {"3", "3", "19", "221132", "11001011100"},
            {"4", "4", "20", "221231", "11001001110",},
            {"5", "5", "21", "213212", "11011100100"},
            {"6", "6", "22", "223112", "11001110100"},
            {"7", "7", "23", "312131", "11101101110"},
            {"8", "8", "24", "311222", "11101001100"},
            {"9", "9", "25", "321122", "11100101100"},
            {":", ":", "26", "321221", "11100100110"},
            {";", ";", "27", "312212", "11101100100"},
            {"<", "<", "28", "322112", "11100110100"},
            {"=", "=", "29", "322211", "11100110010"},
            {">", ">", "30", "212123", "11011011000"},
            {"?", "?", "31", "212321", "11011000110"},
            {"@", "@", "32", "232121", "11000110110"},
            {"A", "A", "33", "111323", "10100011000"},
            {"B", "B", "34", "131123", "10001011000"},
            {"C", "C", "35", "131321", "10001000110"},
            {"D", "D", "36", "112313", "10110001000"},
            {"E", "E", "37", "132113", "10001101000"},
            {"F", "F", "38", "132311", "10001100010"},
            {"G", "G", "39", "211313", "11010001000"},
            {"H", "H", "40", "231113", "11000101000"},
            {"I", "I", "41", "231311", "11000100010"},
            {"J", "J", "42", "112133", "10110111000"},
            {"K", "K", "43", "112331", "10110001110"},
            {"L", "L", "44", "132131", "10001101110"},
            {"M", "M", "45", "113123", "10111011000"},
            {"N", "N", "46", "113321", "10111000110"},
            {"O", "O", "47", "133121", "10001110110"},
            {"P", "P", "48", "313121", "11101110110"},
            {"Q", "Q", "49", "211331", "11010001110"},
            {"R", "R", "50", "231131", "11000101110"},
            {"S", "S", "51", "213113", "11011101000"},
            {"T", "T", "52", "213311", "11011100010"},
            {"U", "U", "53", "213131", "11011101110"},
            {"V", "V", "54", "311123", "11101011000"},
            {"W", "W", "55", "311321", "11101000110"},
            {"X", "X", "56", "331121", "11100010110"},
            {"Y", "Y", "57", "312113", "11101101000"},
            {"Z", "Z", "58", "312311", "11101100010"},
            {"[", "[", "59", "332111", "11100011010"},
            {"\\", "\\", "60", "314111", "11101111010"},
            {"]", "]", "61", "221411", "11001000010"},
            {"^", "^", "62", "431111", "11110001010"},
            {"_", "_", "63", "111224", "10100110000"},
            {"NUL", "`", "64", "111422", "10100001100"},
            {"SOH", "a", "65", "121124", "10010110000"},
            {"STX", "b", "66", "121421", "10010000110"},
            {"ETX", "c", "67", "141122", "10000101100"},
            {"EOT", "d", "68", "141221", "10000100110"},
            {"ENQ", "e", "69", "112214", "10110010000"},
            {"ACK", "f", "70", "112412", "10110000100"},
            {"BEL", "g", "71", "122114", "10011010000"},
            {"BS", "h", "72", "122411", "10011000010"},
            {"HT", "i", "73", "142112", "10000110100"},
            {"LF", "j", "74", "142211", "10000110010"},
            {"VT", "k", "75", "241211", "11000010010"},
            {"FF", "I", "76", "221114", "11001010000"},
            {"CR", "m", "77", "413111", "11110111010"},
            {"SO", "n", "78", "241112", "11000010100"},
            {"SI", "o", "79", "134111", "10001111010"},
            {"DLE", "p", "80", "111242", "10100111100"},
            {"DC1", "q", "81", "121142", "10010111100"},
            {"DC2", "r", "82", "121241", "10010011110"},
            {"DC3", "s", "83", "114212", "10111100100"},
            {"DC4", "t", "84", "124112", "10011110100"},
            {"NAK", "u", "85", "124211", "10011110010"},
            {"SYN", "v", "86", "411212", "11110100100"},
            {"ETB", "w", "87", "421112", "11110010100"},
            {"CAN", "x", "88", "421211", "11110010010"},
            {"EM", "y", "89", "212141", "11011011110"},
            {"SUB", "z", "90", "214121", "11011110110"},
            {"ESC", "{", "91", "412121", "11110110110"},
            {"FS", "|", "92", "111143", "10101111000"},
            {"GS", "},", "93", "111341", "10100011110"},
            {"RS", "~", "94", "131141", "10001011110"},
            {"US", "DEL", "95", "114113", "10111101000"},
            {"FNC3", "FNC3", "96", "114311", "10111100010"},
            {"FNC2", "FNC2", "97", "411113", "11110101000"},
            {"SHIFT", "SHIFT", "98", "411311", "11110100010"},
            {"CODEC", "CODEC", "99", "113141", "10111011110"},
            {"CODEB", "FNC4", "CODEB", "114131", "10111101110"},
            {"FNC4", "CODEA", "CODEA", "311141", "11101011110"},
            {"FNC1", "FNC1", "FNC1", "411131", "11110101110"},
            {"StartA", "StartA", "StartA", "211412", "11010000100"},
            {"StartB", "StartB", "StartB", "211214", "11010010000"},
            {"StartC", "StartC", "StartC", "211232", "11010011100"},
            {"Stop", "Stop", "Stop", "2331112", "1100011101011"},};

    /**
     * 获取条形码base64位编码图片
     *
     * @param barcode
     * @param p_code
     * @param height
     * @return
     */
    public static String getCodeImgbase64(String barcode, Encode p_code,
                                          int height) {
        String rs = "";
        // 获取条纹码
        String code = getCode(barcode, p_code);
        // 获取BASE64编码值
        rs = getBase64Img(code, height);
        // 设置图片头
        rs = "data:image/gif;base64," + rs;
        return rs;
    }

    /**
     * 获取条纹的编码值
     *
     * @param barcode
     * @param p_code
     * @return
     */
    private static String getCode(String barcode, Encode p_code) {
        String rs = "";
        List<Integer> dataidlist = new ArrayList<Integer>();// 存放所有的数据ID

        if (p_code == Encode.Code128A) {
            // 获取首位
            rs += getValue(p_code, "StartA", dataidlist);
            // 循环获取每一个编号的条纹
            for (int i = 0; i < barcode.length(); i++) {
                rs += getValue(p_code, barcode.substring(i, i + 1), dataidlist);
            }
            // 计算校验位
            int sum = 103;
            for (int i = 1; i < dataidlist.size(); i++) {
                sum += i * dataidlist.get(i);
            }
            int jycode = sum % 103;
            rs += code128[jycode][4];
            // 获取结束位
            rs += code128[106][4];

        } else if (p_code == Encode.Code128B) {
            // 获取首位
            rs += getValue(p_code, "StartB", dataidlist);
            // 循环获取每一个编号的条纹
            for (int i = 0; i < barcode.length(); i++) {
                rs += getValue(p_code, barcode.substring(i, i + 1), dataidlist);
            }
            // 计算校验位
            int sum = 104;
            for (int i = 1; i < dataidlist.size(); i++) {
                sum += i * dataidlist.get(i);
            }
            int jycode = sum % 103;
            rs += code128[jycode][4];
            // 获取结束位
            rs += code128[106][4];
        } else if (p_code == Encode.Code128C) {
            // 判断长度是否为偶数
            if (barcode.length() % 2 == 1) {
                System.out.println("编码长度必须为偶数！");
                rs = "";
            } else {
                // 获取首位
                rs += getValue(p_code, "StartC", dataidlist);
                // 循环获取每一个编号的条纹
                for (int i = 0; i < barcode.length() / 2; i++) {
                    rs += getValue(p_code, barcode.substring(i * 2, i * 2 + 2),
                            dataidlist);
                }
                // 计算校验位
                int sum = 105;
                for (int i = 1; i < dataidlist.size(); i++) {
                    sum += i * dataidlist.get(i);
                }
                int jycode = sum % 103;
                rs += code128[jycode][4];
                // 获取结束位
                rs += code128[106][4];
            }
        } else if (p_code == Encode.EAN128) {
            // 判断长度是否为偶数
            if (barcode.length() % 2 == 1) {
                System.out.println("编码长度必须为偶数！");
                rs = "";
            } else {
                // 获取首位
                rs += getValue(p_code, "StartC", dataidlist);
                // 增加控制位
                dataidlist.add(102);
                rs += code128[102][4];

                // 循环获取每一个编号的条纹
                for (int i = 0; i < barcode.length() / 2; i++) {
                    rs += getValue(p_code, barcode.substring(i * 2, i * 2 + 2),
                            dataidlist);
                }
                // 计算校验位
                int sum = 105;
                for (int i = 1; i < dataidlist.size(); i++) {
                    sum += i * dataidlist.get(i);
                }
                int jycode = sum % 103;
                rs += code128[jycode][4];
                // 获取结束位
                rs += code128[106][4];
            }
        }
        return rs;
    }

    /**
     * 通过条纹编码，返回B64位的编码图片
     *
     * @param code
     * @param height
     * @return
     */
    public static String getBase64Img(String code, int height) {
        String rs = "";
        try {
            char[] cs = code.toCharArray();
            int width = cs.length;
            ByteArrayOutputStream outputStream = null;
            try {
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                outputStream = new ByteArrayOutputStream();
                Graphics g = bufferedImage.getGraphics();
                for (int i = 0; i < cs.length; i++) {
                    if ("0".equals(cs[i] + "")) {
                        g.setColor(Color.WHITE);
                        g.fillRect(i, 0, 1, height);
                    }
                }
                Images.write(bufferedImage, "JPEG", outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 对字节数组Base64编码
            rs = Base64.encodeToString(outputStream.toByteArray(), false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 保存在指定路径
     *
     * @param barcode
     * @param p_code
     * @param path
     * @param Height
     * @return
     */
    public static String getCodeImg(String barcode, Encode p_code, String path,
                                    int Height) {
        String rs = "";
        rs = getCode(barcode, p_code);
        saveImg(rs, Height, path);
        return rs;
    }

    public static void saveImg(String barString, int Height, String path) {
        try {
            File myPNG = new File(path);
            OutputStream out = new FileOutputStream(myPNG);
            if (null == barString || null == out || 0 == barString.length()) {
                return;
            }
            int nImageWidth = 0;
            char[] cs = barString.toCharArray();
            nImageWidth = cs.length;

            BufferedImage bi = new BufferedImage(nImageWidth, Height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            for (int i = 0; i < cs.length; i++) {
                if ("0".equals(cs[i] + "")) {
                    g.setColor(Color.WHITE);
                    g.fillRect(i, 0, 1, Height);
                }
            }
            Images.write(bi, myPNG);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据编号获得条纹
     *
     * @param encode
     * @param p_Value
     * @return
     */
    private static String getValue(Encode encode, String p_Value,
                                   List<Integer> dataidlist) {

        String rs = "";
        if (encode == Encode.Code128A) {
            for (int i = 0; i < code128.length; i++) {
                if (code128[i][0].equals(p_Value)) {
                    rs = code128[i][4];
                    dataidlist.add(i);
                    break;
                }
                if (i == code128.length) {
                    System.out.println("存在无效的字符集:" + p_Value);
                }
            }

        } else if (encode == Encode.Code128B) {
            for (int i = 0; i < code128.length; i++) {
                if (code128[i][1].equals(p_Value)) {
                    rs = code128[i][4];
                    dataidlist.add(i);
                    break;
                }
                if (i == code128.length) {
                    System.out.println("存在无效的字符集:" + p_Value);
                }
            }

        } else if (encode == Encode.Code128C) {
            for (int i = 0; i < code128.length; i++) {
                if (code128[i][2].equals(p_Value)) {
                    rs = code128[i][4];
                    dataidlist.add(i);
                    break;
                }
                if (i == code128.length) {
                    System.out.println("存在无效的字符集:" + p_Value);
                }
            }

        } else if (encode == Encode.EAN128) {

            for (int i = 0; i < code128.length; i++) {
                if (code128[i][2].equals(p_Value)) {
                    rs = code128[i][4];
                    dataidlist.add(i);

                    break;
                }
                if (i == code128.length) {
                    System.out.println("存在无效的字符集:" + p_Value);
                }
            }
        }
        return rs;
    }

//    public static void main(String[] args) throws Exception {
//
//        //Code128Util c = new Code128Util();
//        String rs = getCodeImgbase64("ES7260246660A", Code128Util.Encode.Code128A, 40);
//        System.out.println(rs);
//        getCodeImg("ES7260246660A", Code128Util.Encode.Code128A, "D://A.PNG", 40);
//        getCodeImg("ES7260246660A", Code128Util.Encode.Code128B, "D://B.PNG", 40);
//        getCodeImg("117260246660", Code128Util.Encode.Code128C, "D://C.PNG", 40);
//        getCodeImg("117260246660", Code128Util.Encode.EAN128, "D://EAN.PNG", 40);
//
//    }

}
