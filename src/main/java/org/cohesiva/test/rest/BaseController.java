package org.cohesiva.test.rest;

import com.amazonaws.AmazonServiceException;
import com.nameitwhatyoulike.aws.S3Client;
import org.cohesiva.test.rest.domain.Thing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by adam on 3/4/14.
 */
@Controller
@RequestMapping("/")
public class BaseController {
    @Autowired
    S3Client s3Client;

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/{fileName}")
    @ResponseBody
    public ResponseEntity<String> getUrl(@PathVariable("fileName")String fileName,@PathVariable("userId")String userId,@RequestParam("type") String type,HttpServletResponse response){
        try {
                    ActionType actiontype = ActionType.valueOf(type);
                    switch(actiontype) {
                        case DOWNLOAD:
                            return new ResponseEntity<String>(s3Client.generateDownloadURL(userId, fileName).toString(),HttpStatus.OK);
                        case UPLOAD:
                            return new ResponseEntity<String>(s3Client.generateUploadURL(userId, fileName).toString(),HttpStatus.OK);
                        default:
                            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
                    }
        }catch (AmazonServiceException exception) {
              throw exception;
            //TODO: add logging
//            log.error("Error Message: " + exception.getMessage());
//            log.error("HTTP  Code: "    + exception.getStatusCode());
//            log.error("AWS Error Code:" + exception.getErrorCode());
//            log.error("Error Type:    " + exception.getErrorType());
//            log.error("Request ID:    " + exception.getRequestId());
        }
    }

    private enum ActionType{
        DOWNLOAD,UPLOAD;
    }
}

