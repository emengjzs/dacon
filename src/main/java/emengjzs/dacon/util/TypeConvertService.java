package emengjzs.dacon.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class TypeConvertService {

    private TypeConvertService() {

    }

    public static final ConversionService getInstance() {
        return ConvertServiceHolder.service;
    }

    private static class ConvertServiceHolder {
        private static final ConversionService service = new DefaultConversionService();
    }

}
