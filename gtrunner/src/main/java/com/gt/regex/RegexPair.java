package com.gt.regex;

public class RegexPair {
    private String regexString;
    private RegexType regexType;

    public RegexPair(String regexString, RegexType regexType){
        this.regexString = regexString;
        this.regexType = regexType;
    }

    public RegexType getRegexType() {
        return regexType;
    }

    public String getRegexString() {
        return regexString;
    }
}
