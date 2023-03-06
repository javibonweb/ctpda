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

package com.onlyoffice.integration.services.configurers.implementations;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.onlyoffice.integration.documentserver.managers.document.DocumentManager;
import com.onlyoffice.integration.documentserver.models.filemodel.Document;
import com.onlyoffice.integration.documentserver.models.filemodel.Permission;
import com.onlyoffice.integration.documentserver.util.file.FileUtility;
import com.onlyoffice.integration.documentserver.util.service.ServiceConverter;
import com.onlyoffice.integration.services.configurers.DocumentConfigurer;
import com.onlyoffice.integration.services.configurers.wrappers.DefaultDocumentWrapper;

import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;

@Service
@Primary
public class DefaultDocumentConfigurer implements DocumentConfigurer<DefaultDocumentWrapper>, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
    private DocumentManager documentManager;

    @Autowired
    private FileUtility fileUtility;

    @Autowired
    private ServiceConverter serviceConverter;
    
    @Autowired
    private DocumentosExpedientesService documentosExpedientesService;

    
    private transient DateTimeFormatter formatMiliseg;
    
    
    public void configure(Document document, DefaultDocumentWrapper wrapper){  // define the document configurer
        String fileName = wrapper.getFileName();  // get the fileName parameter from the document wrapper
        Permission permission = wrapper.getPermission();  // get the permission parameter from the document wrapper

        document.setTitle(fileName);  // set the title to the document config
        document.setUrl(documentManager.getDownloadUrl(fileName));  // set the URL to download a file to the document config
        //document.setUrlUser(documentManager.getFileUri(fileName, false));  // set the file URL to the document config
        document.setFileType(fileUtility.getFileExtension(fileName).replace(".",""));  // set the file type to the document config
        document.getInfo().setFavorite(wrapper.getFavorite());  // set the favorite parameter to the document config

        document.setKey(generateKey(fileName));  // set the key to the document config
        document.setPermissions(permission);  // set the permission parameters to the document config
    }
    
	private String generateKey(String fileName) {
		final Long idDoc = Long.parseLong(FileUtils.getNombreSinExt(fileName));
		final Documentos doc = documentosExpedientesService.obtener(idDoc).getDocumento();

		return serviceConverter.generateRevisionId(idDoc + "-" + getStrMilisegundos(doc.getFechaUltimaEdicion()));
	}

	private String getStrMilisegundos(LocalDateTime ldt) {
		if(ldt == null) {
			ldt = LocalDateTime.of(0, 1, 1, 0, 0, 0, 0); //Una fecha cualquiera vale, pero debe ser siempre la misma
		}
		
		if(formatMiliseg == null) {
			formatMiliseg = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
		}
		
		return ldt.format(formatMiliseg);		
	}
	
}
