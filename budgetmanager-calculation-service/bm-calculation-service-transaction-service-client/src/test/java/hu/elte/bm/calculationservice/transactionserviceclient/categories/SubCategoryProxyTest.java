package hu.elte.bm.calculationservice.transactionserviceclient.categories;

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
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class SubCategoryProxyTest {

    private static final Long USER_ID = 1L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final String BASE_URL = "http://base.url/endpoint";
    private static final String CALLED_URL = BASE_URL + "?type=" + TYPE + "&userId=" + USER_ID;

    @InjectMocks
    private SubCategoryProxy underTest;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "findSubCategoriesUrl", BASE_URL);
    }

    @Test(expected = TransactionServiceException.class)
    public void testGetCategoriesWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<SubCategoryListResponse> responseEntity = new ResponseEntity<>(new SubCategoryListResponse(), HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategoryListResponse.class);
    }

    @Test
    public void testGetCategoriesWhenServerSendsEmptyList() {
        // GIVEN
        ResponseEntity<SubCategoryListResponse> responseEntity = new ResponseEntity<>(new SubCategoryListResponse(), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        List<SubCategory> result = underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategoryListResponse.class);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCategoriesWhenServerSendsAList() {
        // GIVEN
        SubCategory subCategory = SubCategory.builder()
            .withId(1L)
            .withName("name")
            .withTransactionType(TransactionType.OUTCOME)
            .build();
        List<SubCategory> subCategoryList = List.of(subCategory);
        SubCategoryListResponse response = new SubCategoryListResponse();
        response.setSubCategoryList(subCategoryList);
        ResponseEntity<SubCategoryListResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, SubCategoryListResponse.class)).thenReturn(responseEntity);
        // WHEN
        List<SubCategory> result = underTest.getCategories(TYPE, USER_ID);
        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, SubCategoryListResponse.class);
        Assert.assertEquals(subCategoryList, result);
    }

}
