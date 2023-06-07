package es.cybercatapp.service.conversor;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.service.dto.CourseDtoForm;
import es.cybercatapp.service.dto.ProfileDtoForm;
import es.cybercatapp.service.utils.ImageUtils;
import org.springframework.web.multipart.MultipartFile;

public class CoursesConversor {

    public static CourseDtoForm toCourseDtoForm(Courses courses, byte[] image, String path) {
        return (CourseDtoForm) new CourseDtoForm(courses.getCourseId(), courses.getCourse_name(), courses.getCourse_price(), courses.getCourse_category().toString(), courses.getCourse_description(), ImageUtils.toString64(image), path, courses.getGrade(), courses.getTotal_comments());
    }
}
