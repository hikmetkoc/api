package tr.com.meteor.crm.utils.metadata;

import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.EntityMetadata;
import tr.com.meteor.crm.domain.FieldMetadata;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.repository.GenericIdEntityRepository;
import tr.com.meteor.crm.service.BaseEntityMetadataService;
import tr.com.meteor.crm.service.BaseFieldMetadataService;
import tr.com.meteor.crm.trigger.Trigger;
import tr.com.meteor.crm.utils.idgenerator.IdType;
import tr.com.meteor.crm.utils.request.Sort;
import tr.com.meteor.crm.utils.request.SortOrder;
import tr.com.meteor.crm.utils.validate.AttributeValueValidate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MetadataReader {

    private static Map<String, EntityMetadataFull> classMetadataList;
    private static String changeSetHeader =
        "" +
            "<?xml version=\"1.1\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<databaseChangeLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "                   xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n" +
            "                   xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd\">\n";
    private static String changeSetTemplate =
        "" +
            "    <changeSet author=\"mehmet.sen (generated)\" id=\"1568355267310-#change_set_number#\">\n" +
            "        <sql dbms=\"postgresql\" endDelimiter=\"\\nGO\" splitStatements=\"true\" stripComments=\"true\">\n" +
            "            CREATE INDEX #entity_name#_search_index ON #entity_name# USING GIN (to_tsvector('turkish', ' ' || #search_fields#));\n" +
            "        </sql>\n" +
            "    </changeSet>";
    private static String changeSetFooter = "</databaseChangeLog>";
    private final BaseEntityMetadataService baseEntityMetadataService;
    private final BaseFieldMetadataService baseFieldMetadataService;
    private final boolean writeToFile = System.getProperty("user.name").equals("mehmet.sen");

    public MetadataReader(BaseEntityMetadataService baseEntityMetadataService, BaseFieldMetadataService baseFieldMetadataService) {
        this.baseEntityMetadataService = baseEntityMetadataService;
        this.baseFieldMetadataService = baseFieldMetadataService;
    }

    public static Map<String, EntityMetadataFull> getClassMetadataList() {
        return classMetadataList;
    }

    public static FieldMetadataFull getCrmFieldMetadataFromPath(Class<? extends IdEntity> entityClass, String path) {
        EntityMetadataFull currentMetadata = MetadataReader.getClassMetadataList().get(entityClass.getName());
        String[] pathParts = path.split("\\.");

        FieldMetadataFull fieldMetadataFull = null;
        for (String pathPart : pathParts) {
            fieldMetadataFull = currentMetadata.getFieldMetadataMap().get(pathPart);
            currentMetadata = MetadataReader.getClassMetadataList().get(fieldMetadataFull.getJavaType());
        }

        return fieldMetadataFull;
    }

    @PostConstruct
    public void init() {
        List<EntityMetadata> allObjectsFromDb = baseEntityMetadataService.findAll();
        List<FieldMetadata> allFieldsFromDb = baseFieldMetadataService.findAll();

        Reflections reflections = new Reflections("tr.com.meteor.crm.domain");
        Reflections triggerPackage = new Reflections("tr.com.meteor.crm.trigger");

        Set<Class<?>> crmObjectAnnotated = reflections.getTypesAnnotatedWith(EntityMetadataAnn.class);
        Set<Class<?>> triggers = triggerPackage.getTypesAnnotatedWith(Component.class);

        classMetadataList = new HashMap<>();

        List<String> objectCsv = new ArrayList<>();
        List<String> fieldCsv = new ArrayList<>();
        Map<String, String> objectSearchChangeSet = new TreeMap<>();

        for (Class c : crmObjectAnnotated) {
            EntityMetadataAnn entityMetadataAnn = (EntityMetadataAnn) c.getAnnotation(EntityMetadataAnn.class);
            EntityMetadataFull entityMetadataFull = new EntityMetadataFull();

            entityMetadataFull.setName(c.getSimpleName());
            entityMetadataFull.setJavaType(c.getName());
            entityMetadataFull.setApiName(entityMetadataAnn.apiName());
            entityMetadataFull.setDisplayField(entityMetadataAnn.displayField());
            entityMetadataFull.setSize(entityMetadataAnn.size());
            entityMetadataFull.setEntityClass(c);// TODO: 10/5/2019 casting hatası olabilir buraya sonra bakılacak
            entityMetadataFull.setOwnerPath(entityMetadataAnn.ownerPath());
            entityMetadataFull.setAssignerPath(entityMetadataAnn.assignerPath());
            entityMetadataFull.setOtherPath(entityMetadataAnn.otherPath());
            entityMetadataFull.setSecondAssignerPath(entityMetadataAnn.secondAssignerPath());
            List<Sort> sorts = new ArrayList<>();

            Sort s = new Sort();
            s.setSortBy(entityMetadataAnn.sortBy());
            s.setSortOrder(entityMetadataAnn.sortOrder());
            sorts.add(s);
            entityMetadataFull.setSorts(sorts);

            entityMetadataFull.setTitle(entityMetadataAnn.title());
            entityMetadataFull.setPluralTitle(entityMetadataAnn.pluralTitle());

            allObjectsFromDb.stream()
                .filter(x -> x.getName().equals(c.getSimpleName()))
                .findFirst()
                .ifPresent(entityMetadata -> {
                    entityMetadataFull.setName(entityMetadata.getName());
                    String defaultSort = entityMetadata.getDefaultSort();

                    if ((defaultSort != null) && !(defaultSort.isEmpty())) {

                        try {
                            List<Sort> parseSorts = new ArrayList<>();

                            String[] splitBySemicolon = (entityMetadata.getDefaultSort()).split(";");
                            for (int i = 0; i < splitBySemicolon.length; i++) {
                                String[] splitByComma = splitBySemicolon[i].split(",");
                                Sort sort = new Sort();
                                sort.setSortBy(splitByComma[0]);
                                sort.setSortOrder(SortOrder.valueOf(splitByComma[1]));
                                parseSorts.add(sort);
                            }
                            entityMetadataFull.getSorts().clear();
                            entityMetadataFull.setSorts(parseSorts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    entityMetadataFull.setTitle(entityMetadata.getTitle());
                    entityMetadataFull.setPluralTitle(entityMetadata.getPluralTitle());
                });

            objectCsv.add(objectToCsvLine(entityMetadataFull));

            List<String> searchFieldNames = new ArrayList<>();

            for (Field field : ReflectionUtils.getAllFields(c)) {
                FieldMetadataAnn fieldMetadataAnn = field.getAnnotation(FieldMetadataAnn.class);
                AttributeValueValidate attributeValueValidate = field.getAnnotation(AttributeValueValidate.class);

                if (fieldMetadataAnn != null) {
                    FieldMetadataFull fieldMetadataFull = new FieldMetadataFull();

                    fieldMetadataFull.setName(field.getName());
                    fieldMetadataFull.setTitle(fieldMetadataAnn.title());

                    field.setAccessible(true);
                    fieldMetadataFull.setField(field);

                    if (field.getName().equals("id")) {
                        IdType idType = (IdType) c.getAnnotation(IdType.class);
                        fieldMetadataFull.setType(idType.idType().name());
                    } else if (StringUtils.isNotBlank(fieldMetadataAnn.type())) {
                        fieldMetadataFull.setType(fieldMetadataAnn.type());
                    } else {
                        fieldMetadataFull.setType(field.getType().getSimpleName());
                    }

                    fieldMetadataFull.setActive(fieldMetadataAnn.active());
                    fieldMetadataFull.setDisplay(fieldMetadataAnn.display());
                    fieldMetadataFull.setReadOnly(fieldMetadataAnn.readOnly());

                    if (attributeValueValidate != null) {
                        fieldMetadataFull.setAttributeName(attributeValueValidate.attributeId());
                    }

                    fieldMetadataFull.setRequired(fieldMetadataAnn.required());

                    if (!fieldMetadataAnn.defaultValue().isEmpty()) {
                        fieldMetadataFull.setDefaultValue(fieldMetadataAnn.defaultValue());
                    }

                    fieldMetadataFull.setPriority(fieldMetadataAnn.priority());
                    fieldMetadataFull.setJavaType(field.getType().getName());
                    fieldMetadataFull.setSearch(fieldMetadataAnn.search());

                    fieldMetadataFull.setFilterable(fieldMetadataAnn.filterable());

                    if (field.getType().getAnnotation(EntityMetadataAnn.class) != null) {
                        fieldMetadataFull.setType("Object");
                        fieldMetadataFull.setObjectApiName(field.getType().getAnnotation(EntityMetadataAnn.class).apiName());
                    }

                    allFieldsFromDb.stream()
                        .filter(x -> x.getId().equals(entityMetadataFull.getName() + "_" + fieldMetadataFull.getName()))
                        .findFirst()
                        .ifPresent(crmFieldDb -> {
                            if (crmFieldDb.getDefaultValue() != null && !crmFieldDb.getDefaultValue().isEmpty()) {
                                fieldMetadataFull.setDefaultValue(crmFieldDb.getDefaultValue());
                            }

                            fieldMetadataFull.setDisplay(crmFieldDb.isDisplay());
                            fieldMetadataFull.setPriority(crmFieldDb.getPriority());
                            fieldMetadataFull.setReadOnly(crmFieldDb.isReadonly());
                            fieldMetadataFull.setRequired(crmFieldDb.isRequired());
                            fieldMetadataFull.setTitle(crmFieldDb.getTitle());
                        });

                    entityMetadataFull.getFieldMetadataMap().put(field.getName(), fieldMetadataFull);

                    fieldCsv.add(fieldToCsvLine(entityMetadataFull, fieldMetadataFull));

                    if (fieldMetadataAnn.search()) searchFieldNames.add(field.getName());
                }
            }

            List<Class<?>> entityTriggers = triggers.stream()
                .filter(x -> x.getSimpleName().startsWith(entityMetadataFull.getName() + "Trigger"))
                .collect(Collectors.toList());
            if (!entityTriggers.isEmpty()) {
                entityMetadataFull.setTriggerClass((Class<? extends Trigger<? extends IdEntity, ? extends Serializable, ? extends GenericIdEntityRepository>>) entityTriggers.get(0));
            }

            classMetadataList.put(c.getName(), entityMetadataFull);

            if (!searchFieldNames.isEmpty()) {
                String search = searchFieldNames.stream().sorted()
                    .map(x -> "coalesce(lower(cast(" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, x) + " as text)), '')")
                    .collect(Collectors.joining(" || ' ' || "));

                objectSearchChangeSet.put(
                    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityMetadataFull.getName()),
                    changeSetTemplate
                        .replace("#entity_name#", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityMetadataFull.getName()))
                        .replace("#search_fields#", search)
                );
            }
        }

        try {
            objectCsv.sort(Comparator.comparing(String::toString));
            List<String> sortedObjectCsv = new ArrayList<>();
            sortedObjectCsv.add("id,name,display_field,title,plural_title");
            sortedObjectCsv.addAll(objectCsv);

            StringBuilder sb = new StringBuilder();
            for (String s : sortedObjectCsv) {
                sb.append(s).append("\r\n");
            }

            if (writeToFile)
                FileUtils.writeStringToFile(new File("C:\\Users\\mehmet.sen\\Desktop\\entity_metadata.csv"), sb.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fieldCsv.sort(Comparator.comparing(String::toString));
            List<String> sortedFieldCsv = new ArrayList<>();
            sortedFieldCsv.add("id,entity_metadata_id,name,title,display,read_only,required,default_value,priority");
            sortedFieldCsv.addAll(fieldCsv);

            StringBuilder sb = new StringBuilder();
            for (String s : sortedFieldCsv) {
                sb.append(s).append("\r\n");
            }

            if (writeToFile)
                FileUtils.writeStringToFile(new File("C:\\Users\\mehmet.sen\\Desktop\\field_metadata.csv"), sb.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (writeToFile) {
                List<String> indexedSearchChangeSet = IntStream.range(0, objectSearchChangeSet.size())
                    .mapToObj(i -> CollectionUtils.get(objectSearchChangeSet, i).getValue()
                        .replace("#change_set_number#", "" + i)).collect(Collectors.toList());

                String content = changeSetHeader + "";
                content += indexedSearchChangeSet.stream().map(s -> s + "\n").collect(Collectors.joining());
                content += changeSetFooter;

                FileUtils.writeStringToFile(new File("C:\\Users\\mehmet.sen\\Desktop\\search_change_set.txt"),
                    content,
                    "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedHashMap<String, EntityMetadataFull> sortedMap = new LinkedHashMap<>();

        classMetadataList.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        classMetadataList = sortedMap;
    }

    private String fieldToCsvLine(EntityMetadataFull entityMetadataFull, FieldMetadataFull fieldMetadataFull) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(entityMetadataFull.getName()).append("_").append(fieldMetadataFull.getName()).append(",");
        stringBuilder.append(entityMetadataFull.getName()).append(",");
        stringBuilder.append(fieldMetadataFull.getName()).append(",");
        stringBuilder.append(fieldMetadataFull.getTitle()).append(",");
        stringBuilder.append(fieldMetadataFull.isDisplay()).append(",");
        stringBuilder.append(fieldMetadataFull.isReadOnly()).append(",");
        stringBuilder.append(fieldMetadataFull.isRequired()).append(",");
        stringBuilder.append(fieldMetadataFull.getDefaultValue()).append(",");
        stringBuilder.append(fieldMetadataFull.getPriority());

        return stringBuilder.toString();
    }

    private String objectToCsvLine(EntityMetadataFull entityMetadataFull) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(entityMetadataFull.getName()).append(",");
        stringBuilder.append(entityMetadataFull.getName()).append(",");
        stringBuilder.append(entityMetadataFull.getDisplayField()).append(",");
        stringBuilder.append(entityMetadataFull.getTitle()).append(",");
        stringBuilder.append(entityMetadataFull.getPluralTitle());

        return stringBuilder.toString();
    }
}
