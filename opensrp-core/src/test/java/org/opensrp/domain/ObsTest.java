package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObsTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Obs.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfMultipleValuesFound() {
        List<Object> values = new ArrayList<>();
        values.add("dd");
        values.add("d");
        Obs obs = new Obs().withValues(values);

        obs.getValue();
    }

    @Test
    public void testGettingSingleValue() {
        Obs obs = new Obs().withValues(null);
        assertNull(obs.getValue());

        obs.setValue("d");
        assertEquals("d", obs.getValue());

    }

    @Test
    public void testAddToValueList() {
        Obs obs = new Obs().withValues(null);
        obs = obs.addToValueList(null);

        assertNull(obs.getValues());

        List<Object> values = new ArrayList<>();
        values.add("dd");
        obs = obs.addToValueList(values);

        assertEquals(1, obs.getValues().size());
        assertEquals("dd", obs.getValue());
    }

    @Test
    public void testCreatingObject() {
        List<Object> values = new ArrayList<>();
        values.add("dd");
        String fieldType = "fieldType";
        String dataType = "dataType";
        String fieldCode = "fieldCode";
        String parentCode = "parentCode";
        String value = "sd";
        String comment = "comment";
        String formSubmission = "formSubmission";

        Obs obs = new Obs()
                .withFieldType(fieldType)
                .withFieldDataType(dataType)
                .withFieldCode(fieldCode)
                .withParentCode(parentCode)
                .withValue(value)
                .withComments(comment)
                .withFormSubmissionField(formSubmission)
                .withEffectiveDatetime(new DateTime(0l))
                .withHumanReadableValues(values);

        assertEquals(fieldType, obs.getFieldType());
        assertEquals(fieldCode, obs.getFieldCode());
        assertEquals(dataType, obs.getFieldDataType());
        assertEquals(parentCode, obs.getParentCode());
        assertEquals(value, obs.getValue());
        assertEquals(comment, obs.getComments());
        assertEquals(formSubmission, obs.getFormSubmissionField());
        assertEquals(new DateTime(0l), obs.getEffectiveDatetime());
        assertEquals(values, obs.getHumanReadableValues());
    }
}
