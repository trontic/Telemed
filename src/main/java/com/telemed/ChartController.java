package com.telemed;

import com.telemed.model.ChartDataDTO;
import com.telemed.model.RecordRepositoryDB;
import com.telemed.model.User;
import com.telemed.model.UserRepositoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    RecordRepositoryDB recordRepository;

    @Autowired
    UserRepositoryDB userRepository;

    @GetMapping("/data")
    public List<ChartDataDTO> getChartData(@RequestParam("userId") int userId) {
        User user = (User) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return recordRepository.findChartDataByUser(user);
    }
}
