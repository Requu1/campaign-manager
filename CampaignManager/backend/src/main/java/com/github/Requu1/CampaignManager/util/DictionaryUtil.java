package com.github.Requu1.CampaignManager.util;


import java.util.List;

public class DictionaryUtil {
    private static final List<String> KEYWORDS=List.of("electronics","sport","gaming","home","fashion");
    private static final List<String> TOWNS=List.of("Kraków", "Warszawa", "Gdańsk", "Wrocław", "Poznań", "Łódź");

    private DictionaryUtil(){}

    public static boolean areKeywordsValid(List<String> keywords){
        for(String keyword:keywords){
            if(!KEYWORDS.contains(keyword)){
                return false;
            }
        }
        return true;
    }

    public static boolean isTownValid(String town){
        return TOWNS.contains(town);
    }

    public static List<String> getAllKeywords(){
        return KEYWORDS;
    }

    public static List<String> getAllTowns() { return TOWNS; }

}
