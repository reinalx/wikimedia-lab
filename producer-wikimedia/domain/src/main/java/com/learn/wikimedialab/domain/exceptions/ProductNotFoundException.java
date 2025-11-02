package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.NotFoundTemplateException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ProductNotFoundException extends NotFoundTemplateException {

    @Serial
    private static final long serialVersionUID = 1372454289558545967L;

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
