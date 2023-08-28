package tr.com.meteor.crm.utils;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;

public class ArrayListToStringConverter implements AttributeConverter<HashSet<String>, String> {

    private static final String delimiter = " !##! ";

    @Override
    public String convertToDatabaseColumn(HashSet<String> strings) {
        if (strings == null) return "";

        return String.join(delimiter, strings);
    }

    @Override
    public HashSet<String> convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) return new HashSet<>();

        return new HashSet<>(Arrays.asList(s.split(delimiter)));
    }
}
