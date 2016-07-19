package com.edxavier.vueloseaai.database.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Eder Xavier Rojas on 20/05/2016.
 */
@Database(name = Vuelos_DB.NAME, version = Vuelos_DB.VERSION)
public class Vuelos_DB {
    public static final String NAME = "AppDatabase"; // we will add the .db extension
    public static final int VERSION = 7;

    @Migration(version = 7, database = Vuelos_DB.class)
    public static class Migration7 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            // run some code here
            SQLite.update(Vuelos_tbl.class)
                    .set(Vuelos_tbl_Table.msg.eq("Invalid"))
                    .execute(database); // required inside a migration to pass the wrapper
        }
    }

    @Migration(version = 6, database = Vuelos_DB.class)
    public static class Migration6 extends AlterTableMigration<Vuelos_tbl> {

        public Migration6(Class<Vuelos_tbl> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "msg");
            Log.d("EDER","AddCol");
        }
    }
}

