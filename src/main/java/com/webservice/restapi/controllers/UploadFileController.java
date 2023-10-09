package com.webservice.restapi.controllers;

import com.webservice.restapi.DTO.FileDTO;
import com.webservice.restapi.utils.ExcelToDatabase;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("upload")
@CrossOrigin( origins = "*" )
public class UploadFileController {
    @PostMapping("/")
    public String uploadFile(@RequestParam("file") MultipartFile file , FileDTO fileDTO ) throws Exception {

        String uploadDir = System.getProperty("user.dir") + "/files/";
        String fileName = file.getOriginalFilename();
        Files.deleteIfExists(Path.of(uploadDir + fileName));
        File destFile = new File(uploadDir + fileName);
        file.transferTo(destFile);
        return new ExcelToDatabase().convertExcelToDatabase( fileDTO.getFileName(), uploadDir + fileName );

        //         Send a response code to the client indicating the Spring file upload was successful.
    }
}
