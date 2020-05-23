package com.mountblue.kbrshoppingsite.service.user;

import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.ProductImage;
import com.mountblue.kbrshoppingsite.repository.ProductImageRepository;
import com.mountblue.kbrshoppingsite.repository.ProductRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Base64;

import java.util.List;

@Service
@Transactional
public class ProductService {
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/files";

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductImageRepository productImageRepository;


    public void save(Product product, MultipartFile[] files) throws Exception {

        if (productRepository.findByName(product.getName()) != null) {
            throw new Exception("Product already exist");
        }

        productRepository.save(product);

        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {
            // Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename() + " ");  // why are you
            try {                   //appending space here

                //  Files.write(fileNameAndPath, file.getBytes());
                ProductImage productImage = new ProductImage(fileNames.toString(), file.getBytes());
                productImage.setImage(file.getBytes());
                productImage.setImageName(fileNames.toString());
                productImage.setProduct(productRepository.findProductByName(product.getName()));

                productImageRepository.save(productImage);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(long id, Product product) {
        Product existingProduct = productRepository.findById(id).get();
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStatus(product.isStatus());
        productRepository.save(existingProduct);
    }

    public List<Product> findProductByCategoryId(long id) {
        return productRepository.findByCategory_Id(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) throws NotFoundException {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        }
        throw new NotFoundException("Product Not Found");
    }

    public List<Product> stockDetailsByStock() {
        return productRepository.findAllByStatusIsFalseOrderByStock();
    }
}
