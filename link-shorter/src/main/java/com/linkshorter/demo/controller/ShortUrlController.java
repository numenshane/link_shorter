package com.linkshorter.demo.controller;


import com.linkshorter.demo.URLUtils;
import com.linkshorter.demo.repository.DynamoDBRepository;
import com.linkshorter.demo.service.ShortUrlService;
import com.linkshorter.demo.utils.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/")
public class
ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @RequestMapping(value = "/{resource_Id}/**", method = RequestMethod.GET)
    public ResponseEntity getShortUrl(@PathVariable("resource_Id") String resourceId, HttpServletResponse response) throws IOException {
        String sourceUrl = shortUrlService.getSourceUrlByResourceId(resourceId);
        if (sourceUrl == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        response.sendRedirect(sourceUrl);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/api/v1/source_url/{source_url}/**", method = RequestMethod.POST)
    public ResponseEntity createShortUrlIfNeeded(@PathVariable("source_url") String sourceUrl, HttpServletRequest request) throws UnknownHostException {
        final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
        String url = "";
        String domain = "";
        String suffix = "";
        url = handleArguments(sourceUrl, arguments);

        if (URLUtils.isURL(url)) {
            domain = arguments.split("/")[0];
            suffix = arguments.substring(domain.length(), arguments.length());
        } else {
            return ResponseEntity.status(500).body("url is invalid");
        }
        domain = addProtocolToDomain(sourceUrl, domain);
        return ResponseEntity.ok(shortUrlService.createShortUrlIfNeeded(domain, suffix, url, request));
    }

    private String addProtocolToDomain(String sourceUrl, String domain) {
        if (("http:").equals(sourceUrl) || sourceUrl.equals("https:")) {
            domain = sourceUrl + "//" + domain;
        } else {
            domain = "http://" + sourceUrl;
        }
        return domain;
    }

    private String handleArguments(String sourceUrl, String arguments) {
        String url;
        if (!arguments.isEmpty()) {
            if (("http:").equals(sourceUrl) || sourceUrl.equals("https:")) {
                url = sourceUrl + "//" + arguments;
            } else {
                url = "http://" + sourceUrl + '/' + arguments;
            }
        } else {
            url = "http://" + sourceUrl;
        }
        return url;
    }
}
