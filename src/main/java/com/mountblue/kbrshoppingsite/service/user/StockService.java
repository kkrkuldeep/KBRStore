package com.mountblue.kbrshoppingsite.service.user;

import com.mountblue.kbrshoppingsite.model.CartItem;
import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.Stock;
import com.mountblue.kbrshoppingsite.repository.ProductRepository;
import com.mountblue.kbrshoppingsite.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;

    public void save(Stock stock) {
        Product stockProduct = productRepository.findById(stock.getProduct().getId()).get();
        Stock existStock=stockRepository.findByProduct(stock.getProduct());
        if (existStock != null) {
            stock.setId(existStock.getId());
            stock.setProductQuantityInStock(existStock.getProductQuantityInStock()+stock.getProductQuantityInStock());
            stockProduct.setStatus(true);
            stockProduct.setStock(stock.getProductQuantityInStock());
        }
        stockProduct.setStatus(true);
        stockProduct.setStock(stock.getProductQuantityInStock());
        productRepository.save(stockProduct);
        stockRepository.save(stock);
    }

    public Stock findByProduct_Id(long id) {
        return stockRepository.findByProduct_Id(id);
    }

    public void updateStock(CartItem cartItem) {
        Stock stock = stockRepository.findByProduct(cartItem.getProduct());
        stock.setProductQuantityInStock(stock.getProductQuantityInStock() - cartItem.getQuantity());
        Product product = cartItem.getProduct();
        product.setStock(stock.getProductQuantityInStock());
        if(stock.getProductQuantityInStock() <= 10) {
            stock.setEmptyStock(true);
            product.setStatus(false);
        }

        productRepository.save(product);
        stockRepository.save(stock);
    }
}
