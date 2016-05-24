package ru.urvanov.keystore.convert;

import java.text.DecimalFormat;



public class MoneyToStrConverter {
    
    private static final String[][] nums = {{"","один", "два", "три", "четыре", "пять",
         "шесть", "семь", "восемь", "девять",
         "десять","одиннадцать","двенадцать","тринадцать","четырнадцать","пятнадцать",
         "шестнадцать","семнадцать", "восемнадцать", "девятнадцать"},
        {"","",
         "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", 
         "семьдесят", "восемьдесят", "девяносто"},  
        {"", "сто", "двести", "триста", "четыреста", "пятьсот",
         "шестьсот", "семьсот", "восемьсот", "девятьсот"}};

    private static final String[][] tailNums = {{"", "", ""},
      {"тысяча ","тысячи ","тысяч "},
      {"миллион ", "миллиона ", "миллионов "},
      {"миллиард ", "миллиарда ", "миллиардов "}};

    private static final String[] rubles = {"рубль","рубля", "рублей"};

    private static final String[] coins = {"копейка", "копейки", "копеек"};

    private static final String[] months = {"месяц", "месяца", "месяцев"};
    
    private static final String[] days = {"день", "дня", "дней"};
    
    public static String numConvert(double num) {
        //int[] fractAndInt = MoneyToStrConverter.getIntAndFract(num, 2);

        int fullNum = (int)Math.round(num*100);

        int intPart = fullNum / 100;
        
        //int intPart = fractAndInt[0];
        //int fractPart = fractAndInt[1];
        int fractPart = fullNum % 100;
        System.out.println("FractPart: " + num);
        String str =  numParse(intPart) + tailAppend(intPart, rubles) + 
        numParse(fractPart) + tailAppend(fractPart, coins);
        str = str.replaceAll("два копейки","две копейки");
        str = str.replace("два тысячи", "две тысячи");
        str = str.replaceAll("один копейка","одна копейка");
        str = str.replace("один тысяча", "одна тысяча");
        
        return str.trim();
         
    }
    
    public static String simpleConvert(double num) {
        int fullNum = (int)Math.round(num*100);
        int intPart = fullNum / 100;
        String str = numParse(intPart);
        str = str.replace("два тысячи", "две тысячи");
        str = str.replace("один тысяча", "одна тысяча");
        return str;
    }
    
    public static String numConvertWithDigitTail(double num) {
        int fullNum = (int) Math.round(num * 100);
        int intPart = fullNum / 100;
        int fractPart = fullNum % 100;
        System.out.println("FractPart: " + num);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String str = numParse(intPart) + tailAppend(intPart, rubles) + " "
                + decimalFormat.format(fractPart) + " "
                + tailAppend(fractPart, coins);
        str = str.replaceAll("два копейки", "две копейки");
        str = str.replace("два тысячи", "две тысячи");
        str = str.replaceAll("один копейка", "одна копейка");
        str = str.replace("один тысяча", "одна тысяча");
        return str.trim();
    }
    
    public static String dayConvert(int num) {
        return dateConverter(num, days);
    }
    
    public static String dateConverter(int num, String[] forms) {
        return (numParse(num) + tailAppend(num, forms)).trim();
    }
    
    public static String monthConvert(int num) {        
        return (numParse(num) + tailAppend(num, months)).trim();        
        
         
    }
    
    public static String percentsConvert(double num) {
        int fullNum = (int)Math.round(num*100);
        int intPart = fullNum / 100;        
        int fractPart = fullNum % 100;
        String str =  numParse(intPart);
        
        return str.trim();
    }
    
    private static String numParse(int num) {
        if (num == 0) return " ноль ";
        int triada = num;
        int mod = 0;
        String ret="";
        int triadaCount = 0;
        String digitals ="";
        String decs="";
        do {
            mod = triada % 1000;
            triada = triada / 1000;
                                    
            String hundreds = nums[2][mod / 100];
            int decmod = (mod % 100) / 10;
            System.out.println(mod);
            if (decmod == 1) {
                
                decs = nums[0][mod % 100];
                
            }
            else {
                if (decmod !=0) decs = nums[1][(mod % 100) / 10];
                digitals = nums[0][mod % 10];
            }
            ret = hundreds.trim() + " " + decs.trim() + " " + digitals.trim() + " " + tailAppend(mod, tailNums[triadaCount]) + ret;             
            triadaCount++;
            decs="";
            hundreds="";
            digitals="";
        }
        
        while (triada !=0);
        return ret;
    }
    
    private static int[] getIntAndFract(double num, int precision) {
        String[] dividedNums = String.valueOf(num).split("\\.");
        int[] nums = new int[2];
        nums[0] = Integer.parseInt(dividedNums[0]);
        nums[1] = (!dividedNums[1].equals("0") ? Integer.parseInt(dividedNums[1].substring(0, precision)): 0);
        return nums;
        
    }
    
    private static String tailAppend(int tail,  String[] tailNum) {
        if ((tail == 0) && tailNum[2].trim().equals("тысяч")) return "";
        if ((tail % 100) / 10 == 1) {
            System.out.println("ONE:");
            return tailNum[2];
        }
        tail = tail % 10;
        switch(tail) {
            case 1 : return tailNum[0];
            case 2 : 
            case 3 :
            case 4 : return tailNum[1];
            default: return tailNum[2];
            
        }
    }

    public static String percentsConvert2(Double num) {
        int fullNum = (int)Math.round(num*10);
        int intPart = fullNum / 10;        
        int fractPart = fullNum % 10;
        String str =  numParse(intPart) + " целых " + numParse(fractPart);
        
        return str.trim();
    }


}
