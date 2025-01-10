package com.wirebarley.bank.common.utils;



import com.wirebarley.bank.common.type.IType;

import java.util.EnumSet;

public class EnumTypeUtils {

    public static <E extends Enum<E> & IType> E getEnumByCode(String code, Class<E> eClass) {
        if( code == null || code.isEmpty() ) {
            return null;
        }

        return EnumSet.allOf(eClass).stream()
                .filter(e -> e.getCode().equals(code) )
                .findFirst().get();
    }

}
