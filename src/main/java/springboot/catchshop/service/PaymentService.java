package springboot.catchshop.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

/**
 * Payment Service (아임포트를 사용한 결제 시스템)
 * author: soohyun, last modified: 22.04.09
 */

@Service
@Transactional
public class PaymentService {

    private String impKey = "0735647262626011"; // REST API 키
    private String impSecret = "64d9bb66fb441e41c82334ee75b0df539e9652dde56212bc8478144e9596025e443e69676b775865"; // REST API secret
    private IamportClient client = new IamportClient(impKey, impSecret);;

    // 결제하기
    public IamportResponse<Payment> requestPayment(String imp_uid) {
        try {
            IamportResponse<Payment> payment_response = client.paymentByImpUid(imp_uid); // imp_uid를 통한 결제
            return payment_response;
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 결제 취소하기
    public void requestCancel(String imp_uid) {
        CancelData cancel_data = new CancelData(imp_uid, true); // imp_uid를 통한 전액취소

        try {
            IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);
        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
