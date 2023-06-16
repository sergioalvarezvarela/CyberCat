package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.PaypalOrder;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.TestCheckDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class PaymentOperations {

    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @PostMapping("/course/{courseId}/payment/store-paypal-order")
    public ResponseEntity<Map<String, Boolean>> createOrder(@RequestBody PaypalOrder paypalOrder, @PathVariable String courseId, HttpServletRequest request, Locale locale) {
        Map<String, Boolean> response = new HashMap<>();
        if ("COMPLETED".equals(paypalOrder.getStatus())) {
            response.put("payment", true);
        } else {
            response.put("payment", false);
        }

        request.getSession().setAttribute("paymentResponse", response);

        return ResponseEntity.ok(response);
    }


}
