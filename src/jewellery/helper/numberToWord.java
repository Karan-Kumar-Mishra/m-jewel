/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jewellery.helper;

import java.text.DecimalFormat;

/**
 *
 * @author SACHIN MISHRA
 */
public class numberToWord {
  //string type array for one digit numbers     
private static final String[] twodigits = {"", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"};  
//string type array for two digits numbers   
private static final String[] onedigit = {"", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen", " Eighteen", " Nineteen"};  
//defining constructor of the class  
  
//user-defined method that converts a number to words (up to 1000)  
private static String convertUptoThousand(int number)   
{  
String soFar;  
if (number % 100 < 20)  
{  
    soFar = onedigit[number % 100];  
    number = number/ 100;  
}  
else   
{  
    soFar = onedigit[number % 10];  
    number = number/ 10;  
    soFar = twodigits[number % 10] + soFar;  
    number = number/ 10;  
}  
if (number == 0)   
    return soFar;  
return onedigit[number] + " Hundred " + soFar;  
}  
//user-defined method that converts a long number (0 to 999999999) to string    
public static String convertNumberToWord(long number)   
{  
//checks whether the number is zero or not  
if (number == 0)   
{   
//if the given number is zero it returns zero  
return "zero";   
}  
//the toString() method returns a String object that represents the specified long  
String num = Long.toString(number);  
//for creating a mask padding with "0"   
String pattern = "000000000";  
//creates a DecimalFormat using the specified pattern and also provides the symbols for the default locale  
DecimalFormat decimalFormat = new DecimalFormat(pattern);  
//format a number of the DecimalFormat instance  
num = decimalFormat.format(number);  
//format: XXXnnnnnnnnn  
//the subString() method returns a new string that is a substring of this string  
//the substring begins at the specified beginIndex and extends to the character at index endIndex - 1  
//the parseInt() method converts the string into integer  
int crores = Integer.parseInt(num.substring(0,2));  
//format: nnnXXXnnnnnn  
int lakhs  = Integer.parseInt(num.substring(2,4));  
//format: nnnnnnXXXnnn  
int thousands = Integer.parseInt(num.substring(4,6));  
//format: nnnnnnnnnXXX  
int hundreds = Integer.parseInt(num.substring(6,9));  
String tradcrores;  
switch (crores)   
{  
case 0:  
    tradcrores = "";  
    break;  
case 1 :  
    tradcrores = convertUptoThousand(crores)+ " Crore ";  
    break;  
default :  
    tradcrores = convertUptoThousand(crores)+ " Crore ";  
}  
String result =  tradcrores;  
String tradlakh;  
switch (lakhs)   
{  
case 0:  
    tradlakh = "";  
    break;  
case 1 :  
    tradlakh = convertUptoThousand(lakhs)+ " Lakh ";  
    break;  
default :  
    tradlakh = convertUptoThousand(lakhs)+ " Lakh ";  
}  
result =  result + tradlakh;  
String tradthousands;  
switch (thousands)   
{  
case 0:  
    tradthousands = "";  
    break;  
case 1 :  
    tradthousands = "One Thousand ";  
    break;  
default :  
    tradthousands = convertUptoThousand(thousands)+ " Thousand ";  
}  
result =  result + tradthousands;  
String tradhundred;  
tradhundred = convertUptoThousand(hundreds);  
result =  result + tradhundred;  
//removing extra space if any  
return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");  
}  
}
