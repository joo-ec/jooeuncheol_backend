package com.wirebarley.core.exception;

import com.wirebarley.bank.common.type.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
public class BizException extends RuntimeException implements Supplier<BizException>  {

    private int errCode;

    public <T extends Exception> BizException(T e) {
        super(e);
        setErrCode(ResponseCode.SERVER_ERROR.getCode());
    }

    public <T extends Exception> BizException(ResponseCode responseCode) {
        setErrCode(responseCode.getCode());
    }
    public <T extends Exception> BizException(int responseCode) {
        setErrCode(responseCode);
    }

    public <T extends Exception> BizException(ResponseCode responseCode, T e) {
        super(e);
        setErrCode(responseCode.getCode());
    }

    public <T extends Exception> BizException(int responseCode, T e) {
        super(e);
        setErrCode(responseCode);
    }


    @Override
    public BizException get() { return this; }
}
