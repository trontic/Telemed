package com.telemed;

import com.telemed.model.TherapyRepositoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    TherapyRepositoryDB therapyRepository;

    @PostMapping("/processValue")
    public MyResponse processValue(@RequestBody MyRequest request, Model model) {
        int receivedValue = request.getIntValue();

        // Process the value
        // For example, increment the received value
        //int processedValue = receivedValue + 1;

        //MyResponse response = new MyResponse();
        //response.setProcessedValue(processedValue);
        //return response;

        model.addAttribute(therapyRepository.findByRecord_Id(receivedValue));

        MyResponse response = new MyResponse();
        response.setProcessedValue(receivedValue);
        return response;

    }
}

class MyRequest {
    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}

class MyResponse {
    private int processedValue;

    public int getProcessedValue() {
        return processedValue;
    }

    public void setProcessedValue(int processedValue) {
        this.processedValue = processedValue;
    }
}