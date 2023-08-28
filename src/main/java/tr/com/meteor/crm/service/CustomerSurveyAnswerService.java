package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.service.dto.CustomerSurveyDTO;
import tr.com.meteor.crm.utils.attributevalues.SurveyQuestionType;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.CustomerSurveyAnswerRepository;
import tr.com.meteor.crm.repository.SurveyAnswerRepository;
import tr.com.meteor.crm.repository.SurveyQuestionRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerSurveyAnswerService extends GenericIdNameAuditingEntityService<CustomerSurveyAnswer, UUID, CustomerSurveyAnswerRepository> {

    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final CustomerRepository customerRepository;

    public CustomerSurveyAnswerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                       BaseConfigurationService baseConfigurationService,
                                       CustomerSurveyAnswerRepository repository, SurveyQuestionRepository surveyQuestionRepository,
                                       SurveyAnswerRepository surveyAnswerRepository, CustomerRepository customerRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            CustomerSurveyAnswer.class, repository);
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.customerRepository = customerRepository;
    }

    public void saveAll(UUID customerId, String typeId, List<CustomerSurveyDTO> customerSurveyDTOS) throws Exception {
        if (StringUtils.isBlank(typeId)) {
            typeId = SurveyQuestionType.VARSAYILAN.getId();
        }

        Customer customer = customerRepository.findById(customerId).get();

        List<CustomerSurveyAnswer> customerSurveyAnswersToSave = new ArrayList<>();
        for (CustomerSurveyDTO dto : customerSurveyDTOS) {
            if (dto.getCustomerSurveyAnswer() == null) {
                continue;
            }

            List<CustomerSurveyAnswer> customerSurveyAnswers = repository
                .findAllByCustomer_IdAndSurveyAnswer_SurveyQuestion_Id(customerId, dto.getSurveyQuestion().getId());

            CustomerSurveyAnswer customerSurveyAnswer = new CustomerSurveyAnswer();

            if (customerSurveyAnswers.size() > 1) {
                repository.deleteAll(customerSurveyAnswers);
            } else if (customerSurveyAnswers.size() == 1) {
                customerSurveyAnswer = customerSurveyAnswers.get(0);
            }

            customerSurveyAnswer.setCustomer(customer);
            customerSurveyAnswer.setSurveyAnswer(dto.getCustomerSurveyAnswer());
            customerSurveyAnswer.setTime(Instant.now());

            customerSurveyAnswersToSave.add(customerSurveyAnswer);
        }

        repository.saveAll(customerSurveyAnswersToSave);

        List<UUID> newAnswerIds = customerSurveyAnswersToSave.stream().map(IdEntity::getId).collect(Collectors.toList());

        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAllByActiveIsTrueAndType_Id(typeId);
        List<UUID> surveyQuestionIds = surveyQuestions.stream().map(IdEntity::getId).collect(Collectors.toList());

        List<CustomerSurveyAnswer> customerSurveyAnswersToDelete = repository
            .findAllByCustomer_IdAndSurveyAnswer_SurveyQuestion_IdIn(customerId, surveyQuestionIds)
            .stream().filter(x -> !newAnswerIds.contains(x.getId())).collect(Collectors.toList());

        repository.deleteAll(customerSurveyAnswersToDelete);
    }

    public List<CustomerSurveyDTO> getAll(UUID customerId, String typeId) {
        if (StringUtils.isBlank(typeId)) {
            typeId = SurveyQuestionType.VARSAYILAN.getId();
        }

        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAllByActiveIsTrueAndType_Id(typeId);
        List<UUID> surveyQuestionIds = surveyQuestions.stream().map(IdEntity::getId).collect(Collectors.toList());
        Map<UUID, SurveyAnswer> customerSurveyAnswerMap =
            repository.findAllByCustomer_IdAndSurveyAnswer_SurveyQuestion_IdIn(customerId, surveyQuestionIds).stream()
                .collect(Collectors.toMap(x -> x.getSurveyAnswer().getSurveyQuestion().getId(), CustomerSurveyAnswer::getSurveyAnswer));

        Map<UUID, List<SurveyAnswer>> questionIdSurveyListMap = new HashMap<>();

        for (SurveyAnswer surveyAnswer : surveyAnswerRepository.findAllBySurveyQuestion_IdIn(surveyQuestionIds)) {
            List<SurveyAnswer> surveyAnswers = questionIdSurveyListMap.getOrDefault(surveyAnswer.getSurveyQuestion().getId(), new ArrayList<>());

            surveyAnswers.add(surveyAnswer);

            questionIdSurveyListMap.put(surveyAnswer.getSurveyQuestion().getId(), surveyAnswers);
        }

        List<CustomerSurveyDTO> customerSurveyDTOS = new ArrayList<>();

        for (SurveyQuestion surveyQuestion : surveyQuestions) {
            CustomerSurveyDTO customerSurveyDTO = new CustomerSurveyDTO();

            customerSurveyDTO.setSurveyQuestion(surveyQuestion);

            if (customerSurveyAnswerMap.containsKey(surveyQuestion.getId())) {
                customerSurveyDTO.setCustomerSurveyAnswer(customerSurveyAnswerMap.get(surveyQuestion.getId()));
            }

            customerSurveyDTO.setSurveyAnswers(questionIdSurveyListMap.getOrDefault(surveyQuestion.getId(), new ArrayList<>()));

            customerSurveyDTOS.add(customerSurveyDTO);
        }

        return customerSurveyDTOS;
    }
}
