package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorTraceTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(ErrorTrace.class));
    }

    @Test
    public void testConstructor() {
        final Class<?> clazz = ErrorTrace.class;
        String document = "document";
        String status = "status";
        String stackTrace = "stactTrace";
        String occurredAt = "occuredAt";
        String errorType = "errorType";
        DateTime dateTime = new DateTime(0l);

        final Object obj1 = FormTest.getInstance(clazz, dateTime, errorType, occurredAt, stackTrace, status, document);
        Affirm.affirmNotNull("Should have created an object", obj1);

        ErrorTrace errorTrace = (ErrorTrace) obj1;
        assertEquals(dateTime, errorTrace.getDate());
        assertEquals(status, errorTrace.getStatus());
        assertEquals(errorType, errorTrace.getErrorType());
        assertEquals(document, errorTrace.getDocumentType());
        assertEquals(stackTrace, errorTrace.getStackTrace());
        assertEquals(occurredAt, errorTrace.getOccurredAt());

        String recordId = "recordId";
        String name = "name";
        final Object obj2 = FormTest.getInstance(clazz, recordId, dateTime, name, occurredAt, stackTrace, status);
        errorTrace = (ErrorTrace) obj2;
        assertEquals(dateTime, errorTrace.getDate());
        assertEquals(status, errorTrace.getStatus());
        assertEquals(recordId, errorTrace.getRecordId());
        assertEquals(name, errorTrace.getErrorType());
        assertEquals(stackTrace, errorTrace.getStackTrace());
        assertEquals(occurredAt, errorTrace.getOccurredAt());

        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }
}
