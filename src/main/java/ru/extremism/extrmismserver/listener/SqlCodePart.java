package ru.extremism.extrmismserver.listener;


public class SqlCodePart {

    private final String channelName;
    private final String tableName;

    public SqlCodePart(String channelName, String tableName) {
        this.channelName = channelName;
        this.tableName = tableName;
    }

    public String getSqlNotifyFunctionCode(){

        String s = String.format("""
                -- FUNCTION: notify_trigger()
                CREATE OR REPLACE FUNCTION notify_trigger()
                    RETURNS TRIGGER AS $$
                BEGIN
                    PERFORM pg_notify('%s', row_to_json(NEW)::text);
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;""",channelName);
        return s;
    }

    public String getSqlCreateTriggerCode(){
        String s = String.format("""
               CREATE OR REPLACE TRIGGER new_row_trigger
                    AFTER INSERT ON %s
                    FOR EACH ROW
                EXECUTE FUNCTION notify_trigger()""", tableName);
        return s;
    }
}
