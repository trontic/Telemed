package com.telemed;
import com.lowagie.text.DocumentException;
import com.telemed.model.Record;
import com.telemed.model.RecordRepositoryDB;
import com.telemed.model.User;
import com.telemed.tools.EmailSender;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.telemed.model.PDFGenerator;





@Controller
public class PDFController {
    @Autowired
    private RecordRepositoryDB recordRepository;

    @GetMapping("/pdf")
        public void  generatePdf(HttpServletResponse response, HttpSession session) throws DocumentException, IOException {

            User currentUser = (User) session.getAttribute("currentUser");
            PDFGenerator generator = new PDFGenerator();

            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);

            List<Record> recordList = (List<Record>) recordRepository.findAll();
            generator.setRecordList(recordList);
            generator.generate(currentUser, response);
    }
}
