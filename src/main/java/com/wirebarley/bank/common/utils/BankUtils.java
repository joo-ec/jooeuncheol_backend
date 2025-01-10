package com.wirebarley.bank.common.utils;

import java.util.random.RandomGenerator;

public class BankUtils {

    public static String isNull(Object checkStr, String value) { return (checkStr==null?(value==null?"":value):checkStr.toString()); }

    public static String isNull(Object checkStr) { return isNull(checkStr, null); }

    public static String generateRandomString(int length) {
        if ( length < 1 ) return "";

        RandomGenerator generator = RandomGenerator.getDefault();
        StringBuilder randomString = new StringBuilder();

        for( int i=0; i<length; i++) {
            randomString.append(generator.nextInt(10));
        }
        return randomString.toString();
    }
}
