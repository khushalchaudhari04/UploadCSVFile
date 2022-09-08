package com.example.uploadCsv.controller;

import com.example.uploadCsv.service.CsvFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class DownloadCsvRestApi {


    @Autowired
    CsvFileService csvFileService;

    /*
     * Download CSV Files
     */
    @GetMapping("/api/download/csv/")
    public void downloadFile(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=customers.csv");
        csvFileService.loadFile(response.getWriter());
    }
}

