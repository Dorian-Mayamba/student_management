package students.utils;

import students.annotations.Column;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnnotationRetriever {
    public static<T> List<Column> getColumns(Class<T> clazz){
        return Arrays.stream(clazz.getDeclaredFields())
                .map(field -> field.getAnnotation(Column.class))
                .filter(Objects::nonNull)
                .toList();
    }
}
