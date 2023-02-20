package es.cybercatapp.service.Controllers;

import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.dto.UserDtoForm;
import es.cybercatapp.service.dto.UserDtoForm2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class UserOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    private UserImpl userImpl;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"/"})
    public String doGetRegisterPage(Model model) {
        model.addAttribute("UserDtoForm", new UserDtoForm());
        return "Index";
    }
    @PostMapping(value = {"/"})
    public String doRegister(@Valid @ModelAttribute("UserDtoForm") UserDtoForm userDtoForm) throws DuplicatedResourceException {

       userImpl.create(userDtoForm.getUsername(),userDtoForm.getEmail(),
                    userDtoForm.getPassword(),null,null);

        return "paso2";
    }

    @Valid @ModelAttribute("UserDtoForm2")
    public UserDtoForm2 getUserDtoForm2() {
        Users user = userRepository.findByUsername("sergio.alvarez.varela");
        UserDtoForm2 userDtoForm2 = new UserDtoForm2();
        userDtoForm2.setEmail(user.getEmail());
        userDtoForm2.setUsername(user.getUsername());
        return  userDtoForm2;
    }

    @GetMapping(value = {"paso2"})
    public String doGetWrite() {
        return "paso2";
    }}
