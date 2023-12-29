package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RequestMapping("/json")
@RestController
public class JsonController {

    private final S3Uploader s3Uploader;

    @Value("${cloud.aws.s3.json.path}")
    private String jsonFolderPath;

    @PutMapping("")
    public String uploadJson(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) throws IOException {
        String uploadPath = jsonFolderPath + type;
        String fileName = file.getOriginalFilename();

        if (s3Uploader.doesExist(uploadPath, fileName)) {
            s3Uploader.copyObject(uploadPath + "/" + fileName, uploadPath + "/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + ".json");
        }
        return s3Uploader.upload(file, uploadPath);
    }

//    TODO: json 목록 업로드
//    @PutMapping("")
//    public Response<Map<String, Object>> uploadJson(
//            @RequestParam("paramMap") Map<String, MultipartFile> paramMap
//    ) throws IOException {
//        Map<String, Object> resultMap = new HashMap<>();
//        Map<String, String> successMap = new HashMap<>();
//        Map<String, String> failMap = new HashMap<>();
//
//        paramMap.forEach((type, file) -> {
//            String uploadPath = jsonFolderPath + type;
//            String fileName = file.getOriginalFilename();
//
//            try {
//                if (s3Uploader.doesExist(uploadPath, fileName)) {
//                    s3Uploader.copyObject(uploadPath + "/" + fileName, uploadPath + "/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + ".json");
//                }
//                String updatedUrl = s3Uploader.upload(file, uploadPath);
//                successMap.put(type, updatedUrl);
//            } catch (IOException e) {
//                failMap.put(type, "");
//            }
//
//        });
//
//        resultMap.put("success", successMap);
//        resultMap.put("fail", failMap);
//        return Response.success(resultMap);
//    }
}
