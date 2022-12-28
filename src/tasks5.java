import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class tasks5 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        //1
        System.out.println(Arrays.toString(encrypt("Hello")));
        System.out.println(decrypt(new int[]{72, 33, -73, 84, -12, -3, 13, -13, -68}));
        //2
        System.out.println(canMove("Rook", "A8", "H8"));
        //3
        System.out.println(canComplete("butl", "beautiful"));
        //4
        System.out.println(sumDigProd(1, 2, 3, 4, 5, 6));
        //5
        System.out.println(sameVowelGroup(new String[]{"hoops", "chuff", "bot", "bottom"}));
        //6
        System.out.println(validateCard(1234567890123452L));
        //7
        System.out.println(numToEng(999));
        //8
        System.out.println(getSha256Hash("password123"));
        //9
        System.out.println(correctTitle("jOn SnoW, kINg IN thE noRth."));
        //10
        for (StringBuilder str: hexLattice(37)) {
            System.out.printf(str.toString());
        }
    }

    //1
    public static int[] encrypt(String str) {
        char[] strChars = str.toCharArray();
        int[] res = new int[str.length()];
        res[0] = strChars[0];

        for (int i=1; i<strChars.length; i++) {
            res[i] = strChars[i] - strChars[i-1];
        }
        return res;
    }

    public static StringBuilder decrypt(int[] ch) {
        char[] res = new char[ch.length];
        res[0] = (char) ch[0];

        for (int i=1; i<res.length; i++) {
            res[i] = (char) (ch[i] + res[i-1]);
        }

        StringBuilder result = new StringBuilder();
        for (char i: res) {
            result.append((char) i);
        }

        return result;
    }

    //2
    public static boolean canMove(String type, String coordsF, String coordsS) {
        String row = "ABCDEFGH";
        int xf = row.indexOf(coordsF.charAt(0));
        int xs = row.indexOf(coordsS.charAt(0));

        int yf = Integer.parseInt(coordsF.substring(1));
        int ys = Integer.parseInt(coordsS.substring(1));

        switch (type) {
            case "Pawn":
                return Math.abs(yf - ys) < 2 & xf == xs;
            case "Knight":
                return (Math.abs(yf - ys) == 1 & Math.abs(xf - xs) == 2) | (Math.abs(xf - xs) == 1 & Math.abs(yf - ys) == 2);
            case "Bishop":
                return Math.abs(xf - xs) == Math.abs(yf - ys);
            case "Rook":
                return Math.abs(xf - xs) == 0 | Math.abs(yf - ys) == 0;
            case "Queen":
                return Math.abs(xf - xs) == Math.abs(yf - ys) | Math.abs(xf - xs) == 0 | Math.abs(yf - ys) == 0;
            case "King":
                return Math.abs(xf - xs) < 2 & Math.abs(yf - ys) < 2;
            default:
                return false;
        }
    }

    //3
    public static boolean canComplete(String strf, String strs) {
        char[] str_f = strf.toCharArray();

        int count = 0;
        for (char i: str_f) {
            if (strs.indexOf(i) != -1) {

                strs = strs.substring(strs.indexOf(i)+1);
                count++;
            }else return false;
        }
        return count==strf.length();
    }

    //4
    public static int sumDigProd(int ... args) {
        int sum = 0;
        for (int i: args) {
            sum += i;
        }

        int res = Integer.toString(sum).length();
        while (res > 1) {
            int[] nums = new int[res];
            for (int i=0; i<res; i++) {
                nums[i] = Character.getNumericValue(Integer.toString(sum).charAt(i));
            }
            sum = 1;
            for (int i: nums) {
                sum *= i;
            }
            res = Integer.toString(sum).length();
        }
        return sum;
    }
    //5
    public static ArrayList<String> sameVowelGroup(String[] args) {
        String vowels = "aeiouy";
        Set<Character> set = new HashSet<>();
        StringBuilder un_vowels = new StringBuilder();
        for (char c: args[0].toCharArray()) {
            if (!set.contains(c) & vowels.contains(Character.toString(c))) {
                un_vowels.append(c);
                set.add(c);
            }
        }
        ArrayList<String> result = new ArrayList<>();
        result.add(args[0]);
        for (int i=1; i<args.length; i++) {
            boolean count = true;
            for (char c: un_vowels.toString().toCharArray()) {
                if (count) {
                    count = args[i].contains(Character.toString(c));
                }else break;
            }
            if (count) {
                result.add(args[i]);
            }
        }
        return result;
    }

    //6
    public static boolean validateCard(long num) {
        if (!(Long.toString(num).length() < 20 & Long.toString(num).length() > 13)) {
            return false;
        }

        int digit = Integer.parseInt(String.valueOf(String.valueOf(num).charAt(Long.toString(num).length()-1)));

        StringBuilder num_s = new StringBuilder(Long.toString(num).substring(0, Long.toString(num).length()-1));
        num_s.reverse();
        ArrayList<Integer> double_nums = new ArrayList<>();
        for (int i=0; i<num_s.length(); i++) {
            if (i%2!=0) {
                int c = Integer.parseInt(String.valueOf(num_s.charAt(i)));
                int dbnum = c*2;
                if (dbnum > 9) {
                    dbnum = Integer.parseInt(String.valueOf(String.valueOf(dbnum).charAt(0))) +
                            Integer.parseInt(String.valueOf(String.valueOf(dbnum).charAt(1)));
                }
                double_nums.add(dbnum);
            }else double_nums.add(Integer.parseInt(String.valueOf(String.valueOf(num_s).charAt(i))));
        }
        int sum = 0;
        for (int i: double_nums) {
            sum += i;
        }
        return digit==(10-Integer.parseInt(String.valueOf(String.valueOf(sum).charAt(String.valueOf(sum).length()-1))));
    }

    //7
    public static String numToEng(int num) {

        int units = num % 10;
        int dozens = (num % 100 - units) / 10;
        int hundreds = (num - dozens - units) / 100;

        String answer = "";

        String[] digit_words = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String[] dozens_words = new String[]{"zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
        String[] dozens_before_20 = new String[]{"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

        if (hundreds > 0) {
            answer += digit_words[hundreds];
            answer += " hundred ";
        }

        if (dozens > 1) {
            answer += dozens_words[dozens] + " ";
            if (units > 0) {
                answer += digit_words[units];
            }
        } else if (dozens > 0) {
            answer += dozens_before_20[units];
        } else {
            answer += digit_words[units];
        }

        return answer;
    }

    //8
    public static String getSha256Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            final String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    //9
    public static String correctTitle(String str) {
        char[] arr = str.toCharArray();
        StringBuilder strb = new StringBuilder();
        for (char i: arr) {
            strb.append(String.valueOf(i).toLowerCase());
        }

        String[] words_low = new String[]{"and", "the", "of", "in"};
        String[] words = strb.toString().split(" ");
        StringBuilder result = new StringBuilder();
        for (String i: words) {
            if (Arrays.asList(words_low).contains(i)) {
                result.append(i);
                result.append(" ");
                continue;
            }
            for (int c=0; c<i.length(); c++) {
                if (c==0) {
                    result.append(String.valueOf(i.charAt(c)).toUpperCase());
                }else result.append(i.charAt(c));
            }
            result.append(" ");
        }
        return result.toString();
    }

    //10
    public static StringBuilder[] hexLattice(int n) {
        if (n == 1) {
            return new StringBuilder[]{new StringBuilder("o")};
        }
        int x = 1;
        int centernum = 0;
        boolean rightnum = false;
        for (int i=1; x<=n; i++) {
            x += 6*i;
            centernum = (i*2)+1;
            if (x == n) {
                rightnum = true;
                break;
            }
        }
        if (!rightnum){
            return new StringBuilder[]{new StringBuilder("Incorrect number")};
        }

        StringBuilder[] hexfigure = new StringBuilder[centernum];
        StringBuilder currentstr = new StringBuilder();
        for (int i=0; i<centernum; i++) {
            currentstr = new StringBuilder("");
            if (i==centernum/2) {
                for (int j=0; j<centernum; j++) {
                    currentstr.append("o ");
                }
                currentstr.replace(currentstr.length()-1, currentstr.length(), "\n");
            } else if (i < centernum/2){
                int length = centernum + i - centernum/2;
                for (int j=0; j<centernum/2-i; j++) {
                    currentstr.append(" ");
                }
                for (int j=0; j<length; j++) {
                    currentstr.append("o ");
                }
                currentstr.replace(currentstr.length()-1, currentstr.length(), "\n");
            } else {
                int length = centernum - i + (centernum-1)/2;
                for (int j=0; j<i-centernum/2; j++) {
                    currentstr.append(" ");
                }
                for (int j=0; j<length; j++) {
                    currentstr.append("o ");
                }
                currentstr.replace(currentstr.length()-1, currentstr.length(), "\n");
            }

            hexfigure[i] = currentstr;
        }

        return hexfigure;
    }
}