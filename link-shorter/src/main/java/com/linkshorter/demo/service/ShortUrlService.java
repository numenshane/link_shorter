package com.linkshorter.demo.service;

import com.linkshorter.demo.DB.SourceUrl;
import com.linkshorter.demo.DemoApplication;
import com.linkshorter.demo.URLUtils;
import com.linkshorter.demo.repository.DynamoDBRepository;
import com.linkshorter.demo.repository.RedisRepository;
import com.linkshorter.demo.utils.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShortUrlService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private DynamoDBRepository dynamoDBRepository;

    /*
     *
     *
     *
     * */
    public String createShortUrlIfNeeded(String rootUrl, String suffix, String sourceUrl, HttpServletRequest request) throws UnknownHostException {
        if (suffix.equals("")) {
            suffix = "/";
        }
        SourceUrl shortUrl = dynamoDBRepository.getItemById(rootUrl, suffix);
        if (shortUrl != null) {
            return URLUtils.getDomain(request.getServerName(), request.getServerPort()) + shortUrl.getResourceId();
        }
        String resourceId = ShortUrlGenerator.shortUrl(sourceUrl);
        while (true) {
            String sourceUrlInRedis = redisRepository.getString(resourceId);
            if (sourceUrlInRedis != null && !sourceUrlInRedis.equals(sourceUrl)) {
                resourceId = ShortUrlGenerator.shortUrl(sourceUrl);
            } else {
                redisRepository.setString(resourceId, sourceUrl);
                SourceUrl sourceUrlToSaveInDb = constructSourceUrlData(rootUrl, suffix, resourceId);
                dynamoDBRepository.save(sourceUrlToSaveInDb);
                break;
            }
        }
        return URLUtils.getDomain(request.getServerName(), request.getServerPort()) + resourceId;
    }

    private SourceUrl constructSourceUrlData(String rootUrl, String suffix, String resourceId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new SourceUrl(rootUrl, suffix, resourceId, "1", df.format(calendar.getTime()));
    }

    public String getSourceUrlByResourceId(String resourceId) {
        return redisRepository.getString(resourceId);
    }
}
