package vttp.batch5.ssf.noticeboard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Value("${publishingServerUrl}")
	private String publishingServerUrl;

    @GetMapping("/")
    public String showLandingPage(Model model){
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String submitForm(@Valid @ModelAttribute Notice notice, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            return "notice";
        }

        List<String> responseTerms = noticeService.postToNoticeServer(notice);
        int statusCode = Integer.parseInt(responseTerms.get(0));
        String body = responseTerms.get(1);

        model.addAttribute("messageBody", body); 
        model.addAttribute("url", publishingServerUrl);

        return (statusCode>=200 && statusCode<300)? "successPage":"errorPage";
    }

    @GetMapping("/status")
    public ResponseEntity<String> checkHealth(){
        return (noticeService.healthCheck())? ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("") : 
                                                ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("not healthy");
    }

}
