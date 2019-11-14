package hu.elte.bm.calculationservice.transactionserviceclient.transactions;

import java.time.LocalDate;
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
import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class TransactionProxyTest {

    private static final Long USER_ID = 1L;
    private static final TransactionType TYPE = TransactionType.OUTCOME;
    private static final LocalDate START = LocalDate.now().minusDays(5);
    private static final LocalDate END = LocalDate.now();
    private static final String BASE_URL = "http://base.url/endpoint";
    private static final String CALLED_URL = BASE_URL + "?type=" + TYPE + "&userId=" + USER_ID + "&start=" + START + "&end=" + END;

    @InjectMocks
    private TransactionProxy underTest;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "findTransactionsUrl", BASE_URL);
    }

    @Test(expected = TransactionServiceException.class)
    public void testGetTransactionsWhenServerNotRespond() {
        // GIVEN
        ResponseEntity<TransactionListResponse> responseEntity = new ResponseEntity<>(new TransactionListResponse(), HttpStatus.REQUEST_TIMEOUT);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, TransactionListResponse.class)).thenReturn(responseEntity);

        // WHEN
        underTest.getTransactions(TYPE, USER_ID, START, END);

        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, TransactionListResponse.class);
    }

    @Test
    public void testGetTransactionsWhenServerSendsEmptyList() {
        // GIVEN
        ResponseEntity<TransactionListResponse> responseEntity = new ResponseEntity<>(new TransactionListResponse(), HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, TransactionListResponse.class)).thenReturn(responseEntity);

        // WHEN
        List<Transaction> result = underTest.getTransactions(TYPE, USER_ID, START, END);

        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, TransactionListResponse.class);
        Assert.assertNull(result);
    }

    @Test
    public void testGetTransactionsWhenServerSendsAList() {
        // GIVEN
        MainCategory mainCategory = MainCategory.builder()
                .withId(1L)
                .withName("name")
                .withTransactionType(TransactionType.OUTCOME)
                .withSubCategorySet(new HashSet<>())
                .build();
        Transaction transaction = Transaction.builder()
                .withId(1L)
                .withTitle("title")
                .withTransactionType(TransactionType.OUTCOME)
                .withAmount(1.0d)
                .withCurrency(Currency.EUR)
                .withMainCategory(mainCategory)
                .build();
        List<Transaction> transactionList = List.of(transaction);
        TransactionListResponse response = new TransactionListResponse();
        response.setTransactionList(transactionList);
        ResponseEntity<TransactionListResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(CALLED_URL, TransactionListResponse.class)).thenReturn(responseEntity);

        // WHEN
        List<Transaction> result = underTest.getTransactions(TYPE, USER_ID, START, END);

        // THEN
        Mockito.verify(restTemplate).getForEntity(CALLED_URL, TransactionListResponse.class);
        Assert.assertEquals(transactionList, result);
    }

}
