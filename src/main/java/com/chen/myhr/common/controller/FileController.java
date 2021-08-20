package com.chen.myhr.common.controller;

import com.chen.myhr.common.exception.MyException;
import com.chen.myhr.common.exception.MyExceptionCode;
import com.chen.myhr.common.utils.CommonConstants;
import com.chen.myhr.common.utils.MyUtils.MyDate;
import com.chen.myhr.common.utils.MyUtils.MyFile;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chen
 * @description 文件的上传、下载
 * @create 2021-08-20
 */
@Api(tags = {"文件的上传、下载"})
@RestController
@RequestMapping("/system")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!MyFile.checkAllowDownload(fileName)) {
                throw new MyException(MyExceptionCode.ERROR_FILE_NAME);
            }
            String realFileName = MyDate.getTime() + fileName.substring(fileName.indexOf("_"));
            String filePath = CommonConstants.DOWNLOAD_PATH + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            MyFile.setAttachmentResponseHeader(response, realFileName);
            MyFile.writeBytes(filePath, response.getOutputStream());

            if (delete) {
                MyFile.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败: {}", e.getMessage());
        }
    }

//    @PostMapping("/upload")
//    public Result uploadFile(MultipartFile file) throws Exception {
//        try {
//            // 上传文件路径
//            String filePath = CommonConstants.UPLOAD_PATH;
//            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.upload(filePath, file);
//            String url = serverConfig.getUrl() + fileName;
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("fileName", fileName);
//            ajax.put("url", url);
//            return ajax;
//        }
//        catch (Exception e)
//        {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
}
