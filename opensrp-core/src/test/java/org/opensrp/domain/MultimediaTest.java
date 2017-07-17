package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MultimediaTest {

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(Multimedia.class)
                .withIgnoredFields("id")
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Multimedia.class));
    }

    @Test
    public void testConstructor() {
        String fileCategory = "fileCategory";
        String filePath = "filePath";
        String contentType = "contentType";
        String providerId = "providerId";
        String caseId = "caseId";

        Multimedia multimedia = new Multimedia(caseId, providerId, contentType, filePath, fileCategory);

        Multimedia multimedia1 = new Multimedia().withCaseId(caseId).withProviderId(providerId)
                .withContentType(contentType)
                .withFilePath(filePath)
                .withFileCategory(fileCategory);

        assertTrue(multimedia.equals(multimedia1));
    }

}
