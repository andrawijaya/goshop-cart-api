package com.enigma.controller;

import com.enigma.entity.Vendor;
import com.enigma.entity.request.vendor.VendorRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.NotFoundException;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.IVendorService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
public class VendorController {
    private IVendorService vendorService;

    private Logger logger = LoggerFactory.getLogger(VendorController.class);


    private ModelMapper modelMapper;

    @Autowired
    public VendorController(IVendorService vendorService, ModelMapper modelMapper){
        this.vendorService = vendorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity getAllVendor(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "vendorId") String sortBy
    ) throws Exception {
        Page<Vendor> vendors = vendorService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get vendor list", vendors));
    }

    @PostMapping
    public ResponseEntity createVendor(@Valid @RequestBody VendorRequest vendorRequest) throws Exception{
            Vendor vendor = modelMapper.map(vendorRequest, Vendor.class);
            Vendor result = vendorService.create(vendor);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success create vendor", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdVendor(@PathVariable("id") String id) throws Exception{
        vendorService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateVendor(@PathVariable("id") String id, @RequestBody VendorRequest vendorRequest) throws Exception{
        Vendor newVendor = modelMapper.map(vendorRequest, Vendor.class);
        newVendor.setVendorId(id);
        vendorService.update(newVendor, id);
        Vendor result = newVendor;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id "+ id + " has been Updated !", result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdVendor(@PathVariable("id") String id) throws Exception{
        Optional<Vendor> vendor = vendorService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get product By id", vendor));
    }

    @GetMapping(params = {"keyword", "value"})
    public ResponseEntity getByFilterVendor(@RequestParam @NotBlank(message = "invalid keyword required") String keyword, @RequestParam String value) throws Exception {
        List<Vendor> result = vendorService.getByFilter(keyword, value);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success get product by " + keyword, result));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", exception.getMessage()));
    }

}