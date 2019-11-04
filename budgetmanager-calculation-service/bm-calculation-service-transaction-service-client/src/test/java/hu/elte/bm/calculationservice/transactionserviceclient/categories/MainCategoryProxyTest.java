package hu.elte.bm.calculationservice.transactionserviceclient.categories;

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

import hu.elte.bm.calculationservice.transactionserviceclient.exceptions.TransactionServiceException;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class MainCategoryProxyTest {

    private static final Long USER_ID = 1L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final String BASE_URL = "http://base.url/endpoint";
    private static final String CALLED_URL = BASE_URL + "?type=" + TYPE + "&userId=" + USER_ID;

    @InjectMocks
    private MainCategoryProxy underTest;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "findMainCategoriesUrl", BASE_URL);
    }

    @Test(expected = TransactionServiceException.class)
    public void testProvideWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<MainCategoryListResponse> responseEntity = new ResponseEntity<>(new MainCategoryListResponse(), HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategory[].class);
    }

    @Test
    public void testProvideWhenServerSendsEmptyList() {
        // GIVEN
        ResponseEntity<MainCategoryListResponse> responseEntity = new ResponseEntity<>(new MainCategoryListResponse(), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        List<MainCategory> result = underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategoryListResponse.class);
        Assert.assertNull(result);
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
        List<MainCategory> mainCategories = List.of(mainCategory);
        MainCategoryListResponse response = new MainCategoryListResponse();
        response.setMainCategoryList(mainCategories);
        ResponseEntity<MainCategoryListResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, MainCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        List<MainCategory> result = underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, MainCategoryListResponse.class);
        Assert.assertEquals(mainCategories, result);
    }

}
