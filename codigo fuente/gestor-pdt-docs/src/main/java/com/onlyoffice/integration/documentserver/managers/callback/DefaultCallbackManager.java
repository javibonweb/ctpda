/**
 *
 * (c) Copyright Ascensio System SIA 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.onlyoffice.integration.documentserver.managers.callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.integration.DocumentServiceSupport;
import com.onlyoffice.integration.documentserver.managers.jwt.JwtManager;
import com.onlyoffice.integration.documentserver.util.service.ServiceConverter;
import com.onlyoffice.integration.dto.Track;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.SneakyThrows;


@Component
@Primary
public class DefaultCallbackManager implements CallbackManager {

	private static final long serialVersionUID = 1L;
	
	@Value("${files.docservice.url.site}")
    private String docserviceUrlSite;
    @Value("${files.docservice.url.command}")
    private String docserviceUrlCommand;
    @Value("${files.docservice.header}")
    private String documentJwtHeader;

    @Autowired
    private DocumentServiceSupport documentService;
    
    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServiceConverter serviceConverter;

     
    private byte[] downloadBytes(String url) throws IOException {
        if (url == null || url.isEmpty()) throw new BaseException("Url argument is not specified");  // URL isn't specified

        URL uri = new URL(url);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) uri.openConnection();
        InputStream stream = connection.getInputStream();  // get input stream of the file information from the URL

        if (stream == null) {
            connection.disconnect();
            throw new BaseException("Input stream is null");
        }

        return stream.readAllBytes();
     }

    
    @SneakyThrows
    public void processSave(Track body, String fileName) {  // file saving process
        String downloadUri = body.getUrl();
        String uid = body.getUsers().get(0);
        saveToService(downloadUri, fileName, uid);
      }
    
	@SneakyThrows
	private void saveToService(String downloadUri, String fileName, String uid ) {
		documentService.guardar(fileName, uid, downloadBytes(downloadUri));
	}


    @SneakyThrows
    public void commandRequest(String method, String key) {  // create a command request
        String documentCommandUrl = docserviceUrlSite + docserviceUrlCommand;

        URL url = new URL(documentCommandUrl);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();

        HashMap<String, Object> params = new HashMap<>();
        params.put("c", method);
        params.put("key", key);

        String headerToken;
        if (jwtManager.tokenEnabled())  // check if a secret key to generate token exists or not
        {
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("payload", params);
            headerToken = jwtManager.createToken(payloadMap);  // encode a payload object into a header token
            connection.setRequestProperty(documentJwtHeader.equals("") ? "Authorization" : documentJwtHeader, "Bearer " + headerToken);  // add a header Authorization with a header token and Authorization prefix in it

            String token = jwtManager.createToken(params);  // encode a payload object into a body token
            params.put("token", token);
        }

        String bodyString = objectMapper.writeValueAsString(params);

        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);

        connection.setRequestMethod("POST");  // set the request method
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  // set the Content-Type header
        connection.setDoOutput(true);  // set the doOutput field to true
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(bodyByte);  // write bytes to the output stream
        }

        InputStream stream = connection.getInputStream();  // get input stream

        if (stream == null) throw new BaseException("Could not get an answer");

        String jsonString = serviceConverter.convertStreamToString(stream);  // convert stream to json string
        connection.disconnect();

        JSONObject response = serviceConverter.convertStringToJSON(jsonString);  // convert json string to json object

        String responseCode = response.get("error").toString();
        switch(responseCode) {
            case "0":
            case "4": {
                break;
            }
            default: {
                throw new BaseException(response.toJSONString());
            }
        }
    }

    @SneakyThrows
    public void processForceSave(Track body, String fileName) {  // file force saving process
        processSave(body, fileName);
    }
    
}
