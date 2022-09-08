package com.example.uploadCsv.controller;

import com.example.uploadCsv.message.Message;
import com.example.uploadCsv.message.Response;
import com.example.uploadCsv.service.CsvFileService;
import com.example.uploadCsv.utils.ApacheCommonsCsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/upload/csv")
public class UploadCsvRestApi {
    @Autowired
    CsvFileService csvFileServices;

    @PostMapping("/multiple")
    public Response uploadMultipleFiles(@RequestParam("csvfiles") MultipartFile[] csvfiles) {

        Response response = new Response();

        MultipartFile[] readyUploadedFiles = Arrays.stream(csvfiles)
                .filter(x -> !StringUtils.isEmpty(x.getOriginalFilename())).toArray(MultipartFile[]::new);


        if (readyUploadedFiles.length == 0) {
            response.addMessage(new Message("", "No selected file to upload!", "fail"));
            return response;
        }



        String notCsvFiles = Arrays.stream(csvfiles).filter(x -> !ApacheCommonsCsvUtil.isCSVFile(x))
                .map(x -> x.getOriginalFilename()).collect(Collectors.joining(" , "));

        if (!StringUtils.isEmpty(notCsvFiles)) {
            response.addMessage(new Message(notCsvFiles, "Not Csv Files", "fail"));
            return response;
        }


        for (MultipartFile file : readyUploadedFiles) {
            try {
                csvFileServices.store(file.getInputStream());
                response.addMessage(new Message(file.getOriginalFilename(), "Upload Successfully!", "ok"));
            } catch (Exception e) {
                response.addMessage(new Message(file.getOriginalFilename(), e.getMessage(), "fail"));
            }
        }

        return response;
    }

    @PostMapping("/single")
    public Response uploadSingleCSVFile(@RequestParam("csvfile") MultipartFile csvfile) {

        Response response = new Response();

        // Checking the upload-file's name before processing
        if (csvfile.getOriginalFilename().isEmpty()) {
            response.addMessage(new Message(csvfile.getOriginalFilename(),
                    "No selected file to upload! Please do the checking", "fail"));

            return response;
        }

    

        if(!ApacheCommonsCsvUtil.isCSVFile(csvfile)) {
            response.addMessage(new Message(csvfile.getOriginalFilename(), "Error: this is not a CSV file!", "fail"));
            return response;
        }


        try {
            csvFileServices.store(csvfile.getInputStream());
            response.addMessage(new Message(csvfile.getOriginalFilename(), "Upload File Successfully!", "ok"));
        } catch (Exception e) {
            response.addMessage(new Message(csvfile.getOriginalFilename(), e.getMessage(), "fail"));
        }

        return response;
    }
}
