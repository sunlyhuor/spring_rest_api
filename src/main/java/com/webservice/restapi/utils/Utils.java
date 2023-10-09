package com.webservice.restapi.utils;

public class Utils {
    public static String convertSpaceToUnderscore( String text ){
        String[] newText = text.split(" ");
        if( newText.length > 1 ){
            text = "";
            for ( int i = 0; i < newText.length; i++ ){
                if( !newText[i].isEmpty() ){
                    if( i == newText.length - 1 ){
                        text += newText[i];
                    }else{
                        text += newText[i] + "_";
                    }
                }
            }
        }
        return text;
    }
}
