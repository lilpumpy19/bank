package ru.shchegol.gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.shchegol.dto.FinishRegistrationRequestDto;

@FeignClient(name = "dealClient", url = "${app.url.deal}")
public interface FeignClientDeal {
    @PostMapping("/calculate/{statementId}")
    ResponseEntity<Void> finishRegistrationAndCalculate(@PathVariable("statementId") String statementId,
                                                        @RequestBody FinishRegistrationRequestDto request);

    @PostMapping("/document/{statementId}/send")
    ResponseEntity<Void> sendDocuments(@PathVariable("statementId") String statementId);

    @PostMapping("/document/{statementId}/sign")
    ResponseEntity<Void> signDocument(@PathVariable("statementId") String statementId);

    @PostMapping("/document/{statementId}/code")
    ResponseEntity<Void> codeDocument(@PathVariable("statementId") String statementId);

}
