package com.trade_accounting.components.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class NaiveXlsTableBuilder<T> {
    private String header;
    private String metadata;
    private String[] columns;
    private BiConsumer<T, Cell>[] mappings;
    List<T> data;

    public NaiveXlsTableBuilder<T> header(String header) {
        this.header = header;
        return this;
    }

    public NaiveXlsTableBuilder<T> metadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public NaiveXlsTableBuilder<T> columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public NaiveXlsTableBuilder<T> mappings(BiConsumer<T, Cell>... mappings) {
        this.mappings = mappings;
        return this;
    }

    public NaiveXlsTableBuilder<T> data(List<T> data) {
        this.data = data;
        return this;
    }

    public Workbook getWorkbook() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.createRow(0).createCell(0).setCellValue(header);
        sheet.createRow(1).createCell(0).setCellValue(metadata);

        var th = sheet.createRow(2);
        IntStream.range(0, columns.length).forEach(i -> {
            Cell cell = th.createCell(i);
            cell.setCellValue(columns[i]);
        });

        IntStream.range(3, data.size() + 3)
                .mapToObj(sheet::createRow)
                .forEach(this::populateRow);

        return workbook;
    }

    private void populateRow(Row row) {
        var idx = row.getRowNum() - 3;
        IntStream.range(0, mappings.length).forEach(i -> {
            mappings[i].accept(data.get(idx), row.createCell(i));
        });
    }

    public InputStream getAsStream() {
        try (var out = new ByteArrayOutputStream()) {
            getWorkbook().write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
