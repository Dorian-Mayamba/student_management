package students.builders;

public class QueryBuilder implements IBuilder {
    private String selectClause;
    private String whereClause;
    private String fromClause;
    private String orderBy;

    private StringBuilder sb = new StringBuilder();

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
        this.orderBy = "ORDER BY " + orderByField;
        return this;
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
        return sb.toString();
    }
}
