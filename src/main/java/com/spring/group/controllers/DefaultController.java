package com.spring.group.controllers;

import com.spring.group.dto.PropertyDTO;
import com.spring.group.models.property.Photo;
import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;
import com.spring.group.services.AddressServiceImpl;
import com.spring.group.services.PhotoServiceImpl;
import com.spring.group.services.PropertyServiceImpl;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class DefaultController {

    @Autowired
    AddressServiceImpl addressService;

    @Autowired
    PropertyServiceImpl propertyService;

    @Autowired
    PhotoServiceImpl photoService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("insert_entry", "propertyDTO", new PropertyDTO());
    }

    @PostMapping("/list")
    public ModelAndView listProperty(@Valid @ModelAttribute("propertyDTO") PropertyDTO propertyDTO,
                                     BindingResult bindingResult, Authentication auth) throws IOException {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("insert_entry");
        }
        User loggedUser = userServiceImpl.checkUserName(auth.getName()).get();
        addressService.insertAddress(propertyDTO.unWrapAddress());
        Property property = propertyDTO.unWrapProperty(loggedUser);
        propertyService.insertProperty(property);
        for (MultipartFile photo : propertyDTO.getPhotoCollection()) {
            photoService.insertPhoto(new Photo(photo.getBytes(), property));
        }
        return new ModelAndView("index", "messageSuccess", "Property listed successfuly!");
    }
}