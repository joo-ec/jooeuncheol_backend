package com.wirebarley.bank.common.utils;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.core.exception.BizException;
import org.springframework.util.StringUtils;

import java.util.Base64;
public class BankScrtyUtil {

    public static String encodingToString(String data) {
        try {
            if (!StringUtils.hasText(data)) return "";
            return encode(data);
        } catch (Exception e) {
            throw new BizException(ResponseCode.ENCODING_ERR);
        }
    }

    public static String encode(String data) throws Exception {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decodingToString(String data) {
        try {
            if (!StringUtils.hasText(data)) return "";
            return decode(data);
        } catch (Exception e) {
            throw new BizException(ResponseCode.DECODING_ERR);
        }
    }

    public static String decode(String data) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        return new String(decodedBytes);
    }
}
