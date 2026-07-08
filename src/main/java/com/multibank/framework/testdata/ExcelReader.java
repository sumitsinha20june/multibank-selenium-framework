package com.multibank.framework.testdata;

import com.multibank.framework.models.TestCaseData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExcelReader {
    private static final int HEADER_ROW = 3;
    private static final int DATA_START_ROW = 4;

    private ExcelReader() {
    }

    public static List<TestCaseData> read(String filePath, String sheetName) {
        try (FileInputStream inputStream = new FileInputStream(Path.of(filePath).toFile());
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }
            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(HEADER_ROW);
            Map<String, Integer> headers = headers(headerRow, formatter);
            List<TestCaseData> rows = new ArrayList<>();
            for (int i = DATA_START_ROW; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                TestCaseData data = mapRow(row, headers, formatter);
                if (data.getTestCaseId() != null && !data.getTestCaseId().isBlank()) {
                    rows.add(data);
                }
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read Excel file: " + filePath, e);
        }
    }

    private static Map<String, Integer> headers(Row row, DataFormatter formatter) {
        Map<String, Integer> headers = new HashMap<>();
        for (Cell cell : row) {
            headers.put(formatter.formatCellValue(cell).trim(), cell.getColumnIndex());
        }
        return headers;
    }

    private static TestCaseData mapRow(Row row, Map<String, Integer> headers, DataFormatter formatter) {
        TestCaseData data = new TestCaseData();
        data.setJiraId(value(row, headers, formatter, "JiraId"));
        data.setTestCaseId(value(row, headers, formatter, "TestCaseId"));
        data.setRequirement(value(row, headers, formatter, "Requirement"));
        data.setPage(value(row, headers, formatter, "Page"));
        data.setTestClass(value(row, headers, formatter, "TestClass"));
        data.setTestMethod(value(row, headers, formatter, "TestMethod"));
        data.setTestData(value(row, headers, formatter, "TestData"));
        data.setRunMode(value(row, headers, formatter, "RunMode"));
        data.setPriority(value(row, headers, formatter, "Priority"));
        data.setScenario(value(row, headers, formatter, "Scenario"));
        data.setNotes(value(row, headers, formatter, "Notes"));
        return data;
    }

    private static String value(Row row, Map<String, Integer> headers, DataFormatter formatter, String name) {
        Integer index = headers.get(name);
        if (index == null) {
            return "";
        }
        Cell cell = row.getCell(index);
        return cell == null ? "" : formatter.formatCellValue(cell).trim();
    }
}
