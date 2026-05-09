package students.builders;

import java.util.List;

public interface IBuilder {
    IBuilder select(String fields);
    IBuilder where(String condition);
    IBuilder from(String table);
    IBuilder orderBy(String orderByField);

    IBuilder select(String... fields);

    IBuilder where(String... conditions);

    String build();
}
