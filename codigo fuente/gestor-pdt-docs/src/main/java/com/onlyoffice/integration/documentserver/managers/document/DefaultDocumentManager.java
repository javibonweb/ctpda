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

package com.onlyoffice.integration.documentserver.managers.document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.onlyoffice.integration.DocumentServiceSupport;

@Component
@Primary
public class DefaultDocumentManager implements DocumentManager {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String USERADDRESS = "&userAddress=";

    @Value("${server.address}")
    private String serverAddress;
    @Value("${files.storage.folder}")
    private String storageFolder;
    @Value("${url.track}")
    private String trackUrl;
    @Value("${url.download}")
    private String downloadUrl;
    
    @Autowired
    private DocumentServiceSupport documentServiceSupport;
    

 
    // get file URL
    public String getFileUri(String fileName, Boolean forDocumentServer)
    {
    	final String serverPath = documentServiceSupport.getServerUrl();
    	final String hostAddress = serverAddress;
    	
        try
        {
            final String filePathDownload = !fileName.contains(hostAddress) ? fileName
                    : fileName.substring(fileName.indexOf(hostAddress) + hostAddress.length() + 1);

            return serverPath + "/download?"
            		+ "fileName=" + URLEncoder.encode(filePathDownload, java.nio.charset.StandardCharsets.UTF_8.toString())
            		+ USERADDRESS + URLEncoder.encode(hostAddress, java.nio.charset.StandardCharsets.UTF_8.toString());

        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

    // get the callback URL
    public String getCallback(String fileName)
    {
        String serverPath = documentServiceSupport.getServerUrl();
        final String hostAddress = serverAddress;

        try
        {
            String query = trackUrl+"?fileName="+
                    URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8.toString())
            + USERADDRESS + URLEncoder.encode(hostAddress, java.nio.charset.StandardCharsets.UTF_8.toString());

            return serverPath + query;
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

    // get URL to download a file
    public String getDownloadUrl(String fileName) {
        String serverPath = documentServiceSupport.getServerUrl(); 
        final String hostAddress = serverAddress; 

        try
        {
            String query = downloadUrl+"?fileName="
                    + URLEncoder.encode(fileName, java.nio.charset.StandardCharsets.UTF_8.toString())
            + USERADDRESS + URLEncoder.encode(hostAddress, java.nio.charset.StandardCharsets.UTF_8.toString());

            return serverPath + query;
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

}
