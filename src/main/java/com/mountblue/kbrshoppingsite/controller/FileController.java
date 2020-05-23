package com.mountblue.kbrshoppingsite.controller;

import com.mountblue.kbrshoppingsite.model.ProductImage;
import com.mountblue.kbrshoppingsite.repository.ProductImageRepository;
import com.mountblue.kbrshoppingsite.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductImageService productImageService;

    @RequestMapping("/showImage")
    public String UploadPage(Model model) {

        String message = "";
        model.addAttribute("msg", message);
        return "uploadImage";
    }

    @PostMapping("/uploada")
    public String upload(Model model, @RequestParam("files") MultipartFile[] files) {
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename() + " ");
            try {
                Files.write(fileNameAndPath, file.getBytes());
                ProductImage productImage = new ProductImage(fileNames.toString(), file.getBytes());
                productImageRepository.save(productImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("msg", "Successfully uploaded file " + fileNames.toString());
        return "uploadImage";
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) throws FileNotFoundException {
        // Load file from database
        ProductImage dbFile = productImageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("JPEG"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "all-delivery.jpg" + "\"")
                .body(new ByteArrayResource(dbFile.getImage()));
    }
}
