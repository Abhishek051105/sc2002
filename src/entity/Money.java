package entity;

public final class Money { // bc we dont want anything to extend 
    public Money(){}

    public static String format(int cents){
        int dollar = cents / 100;
        int remainder = cents % 100;
        if (remainder <10){
            return ("$"+dollar+"."+"0"+remainder);
        }
        return ("$"+dollar+"."+remainder);

    }

}