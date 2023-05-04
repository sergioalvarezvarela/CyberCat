package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.ContentRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "contentImpl")
public class ContentImpl {


    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ContentRepository contentRepository;


    @Transactional
    public StringContent createTeoricContent(Long moduleId, String contentName, String html) throws InstanceNotFoundException, DuplicatedResourceException {
        try {

            Modules module = moduleRepository.findById(moduleId);
            Content contenido =  contentRepository.findContentsByContentNameAndModule(moduleId, contentName);
            if (contenido != null) {

                throw exceptionGenerationUtils.toDuplicatedResourceException("Content", contentName,
                        "createcontent.duplicated.exception");
            } else {
                StringContent content = new StringContent(contentName, module.getContents().size() + 1, Type.TEORIC, module, html);
                contentRepository.create(content);
                module.getContents().add(content);
                moduleRepository.update(module);
                return content;
            }

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId.toString(), Modules.class.toString(), "Module not found");
        }
    }

    @Transactional
    public TestQuestions createTestQuestionContent(Long moduleId, String contentName, String enunciado) throws InstanceNotFoundException, DuplicatedResourceException {
        try {

            Modules module = moduleRepository.findById(moduleId);
            Content contenido = contentRepository.findContentsByContentNameAndModule(moduleId, contentName);
            if (contenido != null) {

                throw exceptionGenerationUtils.toDuplicatedResourceException("Content", contentName,
                        "createcontent.duplicated.exception");
            } else {
                TestQuestions content = new TestQuestions(contentName, module.getContents().size() + 1, Type.TESTCHOOSE, module, enunciado, "Frase 1", "Frase 2", "Frase 3", "Frase 4", 1);
                contentRepository.create(content);
                return content;
            }

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId.toString(), Modules.class.toString(), "Module not found");
        }
    }

    @Transactional
    public StringComplete createStringCompleteContent(Long moduleId, String contentName, String enunciado, String frase, String frasecorrecta, String words) throws InstanceNotFoundException, DuplicatedResourceException {
        try {

            Modules module = moduleRepository.findById(moduleId);
            Content contenido =  contentRepository.findContentsByContentNameAndModule(moduleId, contentName);
            if (contenido != null) {

                throw exceptionGenerationUtils.toDuplicatedResourceException("Content", contentName,
                        "createcontent.duplicated.exception");
            } else {
                StringComplete content = new StringComplete(contentName, module.getContents().size() + 1, Type.TESTPUZZLE, module, enunciado, frase, frasecorrecta, words);
                contentRepository.create(content);
                return content;
            }

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId.toString(), Modules.class.toString(), "Module not found");
        }
    }


    @Transactional
    public void remove(Long contentId) throws InstanceNotFoundException {

        Content content = contentRepository.findById(contentId);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }
        contentRepository.remove(content);


    }

    @Transactional
    public void contentUpdate(Long moduleId, Long contentId, String newContentName) throws InstanceNotFoundException, DuplicatedResourceException {

        Content content = contentRepository.findById(contentId);
        Content content1 = contentRepository.findContentsByContentNameAndModule(moduleId, newContentName);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }
        if (content1 != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Content", content1.getContentId().toString(),
                    "updatecontent.duplicated.exception");
        } else {
            content.setContentName(newContentName);
            contentRepository.update(content);
        }


    }

    @Transactional
    public void puzzleContentUpdate(Long contentId, String enunciado, String sentence, String correctsentence, String words) throws InstanceNotFoundException, DuplicatedResourceException {

        StringComplete content = (StringComplete) contentRepository.findById(contentId);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }


        if (content.getEnunciado().equals(enunciado) && content.getSentence().equals(sentence) && content.getCorrectSentence().equals(correctsentence) && words.equals(content.getContent())) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Content", content.getContentId().toString(),
                    "editcontent.duplicated.exception");
        } else {
            content.setEnunciado(enunciado);
            content.setSentence(sentence);
            content.setCorrectSentence(correctsentence);
            content.setContent(words);
            contentRepository.update(content);
        }
    }

    @Transactional
    public void testContentUpdate(Long contentId, String question, String word1, String word2, String word3, String word4, int correct) throws InstanceNotFoundException, DuplicatedResourceException {

        TestQuestions content = (TestQuestions) contentRepository.findById(contentId);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }

        if (content.getQuestion().equals(question) && content.getOption1().equals(word1) && content.getOption2().equals(word2) && content.getOption3().equals(word3) && content.getOption4().equals(word4) && content.getCorrect() == correct) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Content", content.getContentId().toString(),
                    "editcontent.duplicated.exception");
        } else {
            content.setQuestion(question);
            content.setOption1(word1);
            content.setOption2(word2);
            content.setOption3(word3);
            content.setOption4(word4);
            content.setCorrect(correct);
            contentRepository.update(content);
        }
    }

    @Transactional
    public void stringContentUpdate(String html, Long contentId) throws InstanceNotFoundException, DuplicatedResourceException {

        StringContent content = (StringContent) contentRepository.findById(contentId);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }
        if (content.getHtml().equals(html)) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Content", content.getContentId().toString(),
                    "editcontent.duplicated.exception");
        } else {
            content.setHtml(html);
            contentRepository.update(content);
        }
    }



    @Transactional(readOnly = true)
    public Content findByContentId(Long contentId) throws InstanceNotFoundException {
        return contentRepository.findById(contentId);
    }


}
