package ru.urvanov.keystore.test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdWriter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseExport extends SpringTestBase {

    @Autowired
    private DataSource dataSource;
    
    @Test
    public void export() throws Exception
    {
        try (Connection jdbcConnection = dataSource.getConnection();
        ) {
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        // partial database export
        //QueryDataSet partialDataSet = new QueryDataSet(connection);
        //partialDataSet.addTable("FOO", "SELECT * FROM TABLE WHERE COL='VALUE'");
        //partialDataSet.addTable("BAR");
        //FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));

        // full database export
        IDataSet fullDataSet = connection.createDataSet();
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("data.xml"), "UTF-8");
                OutputStreamWriter dtdWriter = new OutputStreamWriter(new FileOutputStream("schema.dtd"), "UTF-8")) {
            FlatXmlDataSet.write(fullDataSet, writer, "UTF-8");
            FlatDtdWriter datasetWriter = new FlatDtdWriter(dtdWriter);
            datasetWriter.setContentModel(FlatDtdWriter.CHOICE);
            // You could also use the sequence model which is the default
            // datasetWriter.setContentModel(FlatDtdWriter.SEQUENCE);
            datasetWriter.write(fullDataSet);
        }
        // dependent tables database export: export table X and all tables that
        // have a PK which is a FK on X, in the right order for insertion
        //String[] depTableNames = 
        //  TablesDependencyHelper.getAllDependentTables( connection, "X" );
        //IDataSet depDataSet = connection.createDataSet( depTableNames );
        //FlatXmlDataSet.write(depDataSet, new FileOutputStream("dependents.xml"));          
        }
    }

}
