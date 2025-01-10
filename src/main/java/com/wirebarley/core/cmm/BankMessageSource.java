package com.wirebarley.core.cmm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@AllArgsConstructor
@Component
public class BankMessageSource {

    private final MessageSource messageSource;

    public String getMessage( String code ) {
        return getMessage( code, Locale.getDefault() );
    }

    public String getMessage( String code, Locale locale ) {
        return messageSource.getMessage( code, null, locale );
    }

    public String getRtCodeMessage(int code) {
        try{
            return this.getMessage(String.valueOf(code), Locale.getDefault());
        } catch (Exception e) {
            return this.getMessage("no.rt.msg", Locale.getDefault());
        }
    }

}
