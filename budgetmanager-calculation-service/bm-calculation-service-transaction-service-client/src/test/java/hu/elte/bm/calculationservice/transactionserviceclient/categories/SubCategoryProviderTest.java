package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import hu.elte.bm.calculationservice.transactionserviceclient.exception.TransactionServiceException;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class SubCategoryProviderTest {

    private static final Long USER_ID = 1L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final String BASE_URL = "http://base.url/endpoint";
    private static final String CALLED_URL = BASE_URL + "?type=" + TYPE + "&userId=" + USER_ID;

    @InjectMocks
    private SubCategoryProvider underTest;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "findMainCategoriesUrl", BASE_URL);
    }

    @Test(expected = TransactionServiceException.class)
    public void testProvideWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<SubCategory[]> responseEntity = new ResponseEntity<>(new SubCategory[0], HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategory[].class)).thenReturn(responseEntity);
        // WHEN
        underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategory[].class);
    }

    @Test
    public void testProvideWhenServerSendsEmptyList() {
        // GIVEN
        ResponseEntity<SubCategory[]> responseEntity = new ResponseEntity<>(new SubCategory[0], HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategory[].class)).thenReturn(responseEntity);
        // WHEN
        List<SubCategory> result = underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategory[].class);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testProvideWhenServerSendsAList() {
        // GIVEN
        SubCategory subCategory = SubCategory.builder()
                .withId(1L)
                .withName("name")
                .withTransactionType(TransactionType.OUTCOME)
                .build();
        SubCategory[] subCategories = {subCategory};
        ResponseEntity<SubCategory[]> responseEntity = new ResponseEntity<>(subCategories, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategory[].class)).thenReturn(responseEntity);
        // WHEN
        List<SubCategory> result = underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategory[].class);
        Assert.assertEquals(List.of(subCategories), result);
    }

}
