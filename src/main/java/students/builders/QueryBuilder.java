package students.builders;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBuilder implements IBuilder {
    private String selectClause;
    private String whereClause;
    private String fromClause;
    private String orderBy;

    private final Map<String, Object> columns;

    private StringBuilder sb = new StringBuilder();

    public QueryBuilder() {
        columns = new LinkedHashMap<>();
    }

    @Override
    public IBuilder select(String fields) {
        this.selectClause = "SELECT " + fields;
        return this;
    }

    @Override
    public IBuilder where(String condition) {
        this.whereClause = "WHERE " + condition;
        return this;
    }

    @Override
    public IBuilder from(String table) {
        sb.append(String.format(" FROM %s", table));
        return this;
    }

    @Override
    public IBuilder orderBy(String orderByField) {
        this.orderBy = " ORDER BY " + orderByField;
        return this;
    }

    @Override
    public IBuilder into(String intoTable) {
        sb.append("INSERT INTO ");
        sb.append(intoTable);
        return this;
    }

    @Override
    public IBuilder insert(String field, Object value) {
        columns.put(field, value);
        return this;
    }

    @Override
    public IBuilder delete(String fromTable) {
        sb.append("DELETE FROM ");
        sb.append(fromTable);
        return null;
    }

    @Override
    public IBuilder select(String... fields) {
        sb.append("SELECT ");
        for (int i = 0 ; i < fields.length; i++){
            sb.append(fields[i]);
            if (i < fields.length - 1){
                sb.append(", ");
            }
        }
        return this;
    }

    @Override
    public IBuilder where(String... conditions) {
        String condition;
        sb.append(" WHERE ");
        for (int i = 0; i < conditions.length; i++){
            condition = conditions[i];
            sb.append(condition);
            if (i < conditions.length - 1){
                sb.append(" AND ");
            }
        }
        return this;
    }

    @Override
    public String build() {
        String res = "";
        if (columns.size() > 0) {
                String columnNames = String.join(", ", columns.keySet());
                String placeHolder = columns.values()
                        .stream()
                        .map(o -> "?")
                        .collect(Collectors.joining(", "));
                sb.append(String.format("(%s)", columnNames));
                sb.append(String.format(" VALUES (%s)", placeHolder));
        }
        res = sb.toString();
        sb.setLength(0);
        return res;
    }
}
