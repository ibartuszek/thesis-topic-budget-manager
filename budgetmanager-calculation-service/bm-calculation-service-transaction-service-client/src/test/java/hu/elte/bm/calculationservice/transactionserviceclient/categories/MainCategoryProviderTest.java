package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.Collections;
import java.util.HashSet;
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
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class MainCategoryProviderTest {

    private static final Long USER_ID = 1L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final String BASE_URL = "http://base.url/endpoint";
    private static final String CALLED_URL = BASE_URL + "?type=" + TYPE + "&userId=" + USER_ID;

    @InjectMocks
    private MainCategoryProvider underTest;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "findMainCategoriesUrl", BASE_URL);
    }

    @Test(expected = TransactionServiceException.class)
    public void testProvideWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<MainCategory[]> responseEntity = new ResponseEntity<>(new MainCategory[0], HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategory[].class)).thenReturn(responseEntity);
        // WHEN
        underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategory[].class);
    }

    @Test
    public void testProvideWhenServerSendsEmptyList() {
        // GIVEN
        ResponseEntity<MainCategory[]> responseEntity = new ResponseEntity<>(new MainCategory[0], HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategory[].class)).thenReturn(responseEntity);
        // WHEN
        List<MainCategory> result = underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategory[].class);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testProvideWhenServerSendsAList() {
        // GIVEN
        MainCategory mainCategory = MainCategory.builder()
                .withId(1L)
                .withName("name")
                .withTransactionType(TransactionType.OUTCOME)
                .withSubCategorySet(new HashSet<>())
                .build();
        MainCategory[] mainCategories = {mainCategory};
        ResponseEntity<MainCategory[]> responseEntity = new ResponseEntity<>(mainCategories, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategory[].class)).thenReturn(responseEntity);
        // WHEN
        List<MainCategory> result = underTest.provide(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategory[].class);
        Assert.assertEquals(List.of(mainCategories), result);
    }

}
