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
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private static final Map<Integer, Integer> pageViewCount = new HashMap<>();

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
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        User loggedUser = userServiceImpl.getUserByID(user.getId());
        addressService.insertAddress(propertyDTO.unWrapAddress());
        Property property = propertyDTO.unWrapProperty(loggedUser);
        propertyService.insertProperty(property);
        photoService.uploadPhotos(propertyDTO.getPhotoCollection(), property);
        return new ModelAndView("index", "messageSuccess", "Property listed successfuly!");
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewProperty(@PathVariable Integer id) {
        pageViewCount.merge(id, 1, Integer::sum);
        return new ModelAndView("SOME VIEW PAGE", "property", propertyService.getPropertyByID(id));
    }

    @Scheduled(cron = "1 * * * * ?")
    // seconds minutes hours days months years <-currently updating every minute (when seconds == 1)
    private void updateViewCount() {
        System.out.println("Updating Property View Counts!");
        List<Property> updatedProperties = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : pageViewCount.entrySet()) {
            Property property = propertyService.getPropertyByID(entry.getKey());
            property.setViews(property.getViews() + entry.getValue());
            updatedProperties.add(property);
        }
        propertyService.updateProperties(updatedProperties);
        pageViewCount.clear();
    }
}