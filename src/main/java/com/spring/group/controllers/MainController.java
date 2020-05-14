package com.spring.group.controllers;

import com.spring.group.dto.PropertyDTO;
import com.spring.group.models.property.Property;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.services.PhotoServiceImpl;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MainController {

    private static final Map<Integer, Integer> pageViewCount = new ConcurrentHashMap<>();

    @Autowired
    private PropertyServiceInterface propertyService;

    @Autowired
    private PhotoServiceImpl photoServiceImpl;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("insert_entry", "propertyDTO", new PropertyDTO());
    }

    @PostMapping("/list")
    public ModelAndView listProperty(@Valid @ModelAttribute("propertyDTO") PropertyDTO propertyDTO,
                                     BindingResult bindingResult, Authentication auth,
                                     RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("insert_entry");
        }
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        User loggedUser = userService.getUserByID(user.getId());
        Property property = propertyDTO.unWrapProperty(loggedUser);
        propertyService.insertProperty(property);
        photoServiceImpl.uploadPhotos(propertyDTO.getPhotoCollection(), property);
        redirectAttributes.addFlashAttribute("messageSuccess", "Property listed successfully!");
        return new ModelAndView("redirect:/");
    }

    //TODO Create a html page for property views.
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