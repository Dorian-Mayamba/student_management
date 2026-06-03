package students.builders;

public interface IBuilder {
    IBuilder from(String table);
    IBuilder orderBy(String orderByField);
    IBuilder into(String intoTable);
    IBuilder insert(String field, Object value);

    IBuilder update(String field, Object value);

    IBuilder updateTable(String tableName);

    IBuilder delete(String fromTable);
    IBuilder select(String field);

    IBuilder where(String condition);

    String build();
}
