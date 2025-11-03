package com.learn.wikimedialab.domain.exceptions.base;

import com.learn.wikimedialab.domain.values.ErrorCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class NotFoundTemplateException extends BaseTemplateException {

    @Serial
    private static final long serialVersionUID = -1602454289558545967L;

    public NotFoundTemplateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
