package com.webservice.restapi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExcelToDatabase {

    public  String convertExcelToDatabase(String tableName, String path) throws IOException, SQLException {
        tableName = Utils.convertSpaceToUnderscore(tableName);
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test_db", "huor", "Huor1234");
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS " + tableName);
        
        Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(path)));
        Sheet sheet = (Sheet) workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        String tableQuery = "CREATE TABLE " + tableName + " ( index SERIAL PRIMARY KEY, ";
        String insertQuery = "INSERT INTO " + tableName + "(";

        if (sheet.getPhysicalNumberOfRows() > 2) {
            for (Row row : sheet) {
                if (row.getRowNum() < 1) {
                    Row row2 = sheet.getRow(1);
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        if (i != row.getLastCellNum() - 1) {
                            insertQuery += Utils.convertSpaceToUnderscore(
                                    dataFormatter.formatCellValue(row2.getCell(i)).toLowerCase()) + " ,";
                            tableQuery += Utils.convertSpaceToUnderscore(
                                    dataFormatter.formatCellValue(row2.getCell(i)).toLowerCase()) + " " + row.getCell(i)
                                    + " NULL,";
                        } else {
                            insertQuery += Utils.convertSpaceToUnderscore(
                                    dataFormatter.formatCellValue(row2.getCell(i)).toLowerCase()) + ") VALUES ";
                            tableQuery += Utils.convertSpaceToUnderscore(
                                    dataFormatter.formatCellValue(row2.getCell(i)).toLowerCase()) + " " + row.getCell(i)
                                    + " NULL";
                        }
                    }
                }
                if (row.getRowNum() > 1) {
                    // Insert Query To Database Here this night Stuck Logical in there tmr check
                    // this code again start with loop and condition....
                    insertQuery += "(";
                    // System.out.println( row.getRowNum() );
                    for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                        if (i == sheet.getRow(0).getLastCellNum() - 1) {
                            if (row.getRowNum() + 1 == sheet.getPhysicalNumberOfRows()) {
                                insertQuery += (dataFormatter.formatCellValue(row.getCell(i)).isEmpty()) ? "NULL );"
                                        : "'" + dataFormatter.formatCellValue(row.getCell(i)) + "' );";
                            } else {
                                insertQuery += (dataFormatter.formatCellValue(row.getCell(i)).isEmpty()) ? "NULL ),"
                                        : "'" + dataFormatter.formatCellValue(row.getCell(i)) + "' ),";
                            }
                        } else {
                            insertQuery += (dataFormatter.formatCellValue(row.getCell(i)).isEmpty()) ? "NULL,"
                                    : "'" + dataFormatter.formatCellValue(row.getCell(i)) + "' ,";
                            // insertQuery += "'" + dataFormatter.formatCellValue(row.getCell(i)) + "' ,";
                        }
                    }
                }
            }
            tableQuery += ");";
            // System.out.println( tableQuery );
            // System.out.println( insertQuery );
            statement.execute(tableQuery);
            statement.execute(insertQuery);
            workbook.close();
            statement.close();
            connection.close();
            Files.deleteIfExists(Path.of(path));
            return "Successfully";
        } else {
            return "Please Follow our excel file format";
        }
    }
}
