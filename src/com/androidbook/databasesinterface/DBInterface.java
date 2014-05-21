package com.androidbook.databasesinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBInterface {

    public static final String ID = "id";
    public static final String UID = "UID";

    public static ResultSet query(Statement stat, String table, long uid, String... field) throws SQLException {
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        boolean bool = false;
        if (field == null || field.length == 0 || field[0].equals("")) {
            sb.append(" * ");
            bool = true;
        }
        if (!bool) {
            for (int i = 0; i < field.length; i++) {
                if (i == field.length - 1) {
                    sb.append(field[i]);
                    break;
                }
                sb.append(field[i]).append(",");
            }
        }
        sb.append(" from ")
                .append(table)
                .append(" where UID=")
                .append(uid);
        rs = stat.executeQuery(sb.toString());
        return rs;
    }

    public static int insert(Statement stat, String table, HashMap<String, Object> values) throws SQLException {
        String db_Name = DataBaseManager.getDb_name();
        System.out.println("--------db_name----->>>>"+db_Name);

        String fieldSql = "select COLUMN_NAME from information_schema.columns where table_name='" + table + "' and TABLE_SCHEMA = 'may_stv' ";
//        String fieldSql = "select COLUMN_NAME from information_schema.columns where table_name='" + table + "' and TABLE_SCHEMA = ' " + db_Name + " ' ";
        ResultSet rse = stat.executeQuery(fieldSql);
        List<String> fields = new ArrayList<String>();
        while (rse.next()) {
            System.out.println(rse.getString(1));
            fields.add(rse.getString(1));
        }

        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(table).append(" values(");
        for (int i = 0; i < fields.size(); ++i) {
            if (i == fields.size() - 1 || i == 0 && fields.size() == 1) {
                System.out.println("--fileds.get(i)-->>" + fields.get(i));
                sb.append(values.get(fields.get(i)));
                System.out.println("----" + values.get(fields.get(i)));
                continue;
            }
            System.out.println("----" + fields.get(i));
            System.out.println("----" + values.get(fields.get(i)));
            sb.append(values.get(fields.get(i))).append(",");
        }
        sb.append(");");
        System.out.println("sql----" + sb.toString());
        return stat.execute(sb.toString()) ? 0 : -1;
    }

    public static int update(Statement stat, String table, long uid, HashMap<String, Object> values, String... field) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("update ").append(table).append(" set ");
        for (int i = 0; i < field.length; i++) {
            if (i == field.length - 1 || i == 0 && field.length == 1) {
                sb.append(field[i]).append("=").append(values.get(field[i]));
                continue;
            }
            sb.append(field[i]).append("=").append(values.get(field[i])).append(",");
        }
        sb.append(" where UID=").append(uid).append(";");
        System.out.println("sql----" + sb.toString());
        return stat.execute(sb.toString()) ? 0 : -1;

    }

    public static int getMaxID(Statement stat, String table) throws SQLException {
        String maxId = "select max(id) from " + table;
        ResultSet rs2 = stat.executeQuery(maxId);
        int id = 0;
        if (rs2.next()) {
            id = rs2.getInt(1) + 1;
            rs2.close();
        }
        return id;
    }

    public static void blankField(long uid, Statement stat, String table, String field) throws SQLException {
        //删除更新字段中内容
        String delete = "update " + table + " set " + field + "='' where UID=" + uid;
        stat.execute(delete);
    }


}
