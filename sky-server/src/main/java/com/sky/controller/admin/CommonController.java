package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "Common interface")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("File upload")
    public Result<String> upload(MultipartFile file) {
        try {
//            get the suffix of the file like .jpg .png etc
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//            generate random file name(random name) ensuring the filename in the cloud is unique
            String newFileName = UUID.randomUUID().toString() + suffix;
//            get the file path
            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
        } catch (IOException e) {
            log.error("File upload failed: {}", e);
            throw new RuntimeException(e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
