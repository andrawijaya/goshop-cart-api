package com.enigma.controller;

import com.enigma.entity.Price;
import com.enigma.entity.request.price.PriceRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.IPriceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
public class PriceController {
    private IPriceService priceService;

    private Logger logger = LoggerFactory.getLogger(PriceController.class);


    private ModelMapper modelMapper;

    @Autowired
    public PriceController(IPriceService priceService, ModelMapper modelMapper) {
        this.priceService = priceService;
        this.modelMapper = modelMapper;
    }

    // Get All With Paging
    @GetMapping
    public ResponseEntity getAllPrice(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "priceId") String sortBy
    ) throws Exception {
        Page<Price> prices = priceService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get price list", prices));
    }

    // Create Prices
    @PostMapping
    public ResponseEntity createPrices(@Valid @RequestBody PriceRequest priceRequest) throws Exception {
        Price price = modelMapper.map(priceRequest, Price.class);
        Price result = priceService.create(price);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success create price", result));
    }

    // delete price
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdPrice(@PathVariable("id") String id) throws Exception {
        priceService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // update price
    @PutMapping("/{id}")
    public ResponseEntity updatePrice(@PathVariable("id") String id, @RequestBody PriceRequest priceRequest) throws Exception {
        Price newPrice = modelMapper.map(priceRequest, Price.class);
        newPrice.setPriceId(id);
        priceService.update(newPrice, id);
        Price result = newPrice;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id " + id + " has been Updated !", result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdCategory(@PathVariable("id") String id) throws Exception {
        Optional<Price> price = priceService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get price By id", price));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", exception.getMessage()));
    }
}
