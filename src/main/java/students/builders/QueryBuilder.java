package students.builders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBuilder implements IBuilder {
    private String orderBy;
    private final Map<String, Object> insertColumns;
    private final Map<String, Object> updateColumns;
    private final List<String> columns;

    private String tableName;
    private final List<String> conditions;
    private final StringBuilder sb;

    public QueryBuilder() {
        insertColumns = new LinkedHashMap<>();
        updateColumns = new LinkedHashMap<>();
        columns = new ArrayList<>();
        conditions = new ArrayList<>();
        sb = new StringBuilder();
    }

    @Override
    public IBuilder from(String table) {
        this.tableName = table;
        return this;
    }

    @Override
    public IBuilder orderBy(String orderByField) {
        this.orderBy = " ORDER BY " + orderByField;
        sb.append(this.orderBy);
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
        insertColumns.put(field, value);
        return this;
    }

    @Override
    public IBuilder update(String field, Object value) {
        updateColumns.put(field, value);
        return this;
    }

    @Override
    public IBuilder updateTable(String tableName) {
        sb.append(String.format("UPDATE %s SET ", tableName));
        return this;
    }

    @Override
    public IBuilder delete(String fromTable) {
        sb.append("DELETE FROM ");
        sb.append(fromTable);
        return this;
    }

    @Override
    public IBuilder select(String field) {
        columns.add(field);
        return this;
    }

    @Override
    public IBuilder where(String condition) {
        this.conditions.add(condition);
        return this;
    }

    @Override
    public String build() {
        String res = "";
        if (columns.size() > 0) {
            String cols = String.join(", ", columns);
            sb.append("SELECT ");
            sb.append(cols);
            sb.append(String.format(" FROM %s", tableName));
            columns.clear();
        }

        if (insertColumns.size() > 0) {
                String columnNames = String.join(", ", insertColumns.keySet());
                String placeHolder = insertColumns.values()
                        .stream()
                        .map(o -> "?")
                        .collect(Collectors.joining(", "));
                sb.append(String.format("(%s)", columnNames));
                sb.append(String.format(" VALUES (%s)", placeHolder));
            insertColumns.clear();
        }

        if (updateColumns.size() > 0){
            String placeholder = updateColumns
                    .keySet()
                    .stream()
                    .map(col -> String.format("%s= ?", col))
                    .collect(Collectors.joining(", "));
            sb.append(placeholder);
            updateColumns.clear();
        }

        if (conditions.size() > 0){
            sb.append(" WHERE ");
            for (int i = 0 ; i < conditions.size(); i++){
                sb.append(conditions.get(i));
                if (i < conditions.size() - 1){
                    sb.append(" AND ");
                }
            }
            conditions.clear();
        }

        res = sb.toString();
        sb.setLength(0);
        return res;
    }
}
